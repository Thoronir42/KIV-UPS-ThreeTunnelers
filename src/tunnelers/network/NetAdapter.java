package tunnelers.network;

import java.io.IOException;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandParser;
import tunnelers.network.command.CommandType;

/**
 *
 * @author Stepan
 */
public class NetAdapter extends Thread {

	private static final int BUFFER_SIZE = 512;

	private Connection connection;
	private short LastMsgId = 0;
	private NetClient localClient;
	private int invalidResponseCounter;
	private String disconnectReason;

	private final CommandParser parser;
	private INetCommandHandler handler;
	private boolean keepRunning;

	public NetAdapter() {
		super(NetAdapter.class.getSimpleName());
		this.parser = new CommandParser();
		this.keepRunning = true;
	}

	public void setHandler(INetCommandHandler handler) {
		this.handler = handler;
	}

	public boolean serverPresent(String address, int port) {
		return false;
	}

	public void connectTo(String clientName, String adress, int port) {
		try {
			this.connection = new Connection(adress, port, BUFFER_SIZE);
			this.localClient = new NetClient(clientName);
		} catch (NetworksException e) {
			Command err = new Command(CommandType.VirtConnectingError);
			err.setData(e.getMessage());
			this.handler.handle(err);
		}
	}

	public boolean issueCommand(Command cmd) {
		String code = parser.parse(cmd);
		System.out.println(code);
		try {
			this.connection.send(code);
			return true;
		} catch (NetworksException e) {
			return false;
		}
	}

	public void tmpSendText(String Text) {
		try {
			this.connection.send(Text);
		} catch (Exception e) {
			System.err.println("Send text error: " + e.getMessage());
		}

	}

	@Override
	public void run() {
		System.out.println("Networks: starting");

		while (this.keepRunning) {
			try {
				if (!isValidConnection(connection)) {
					continue;
				}

				// and now we wait...
				String message = this.connection.receive();

				if (message == null) {
					Command err = new Command(CommandType.VirtConnectionTerminated);
					this.handler.handle(err);
					connection = null;
					continue;
				}

				try {
					Command cmd = this.parser.parse(message);
					this.handler.handle(cmd);
				} catch (CommandNotRecognisedException e) {
					if (!this.handleInvalidResponse()) {
						return;
					}
				}
			} catch (InterruptedException e) {
				System.err.println("Network: waiting interrupted");
				break;
			} catch (IOException e) {
				System.err.println("Network run error: " + e.getClass() + ": " + e.getMessage());
				connection = null;
			}
		}
	}

	private boolean isValidConnection(Connection connection) {
		if (connection == null) {
			try {
				sleep(200);
			} catch (InterruptedException ex) {
				// 
			}
			return false;
		}

		if (!connection.isOpen()) {
			try {
				connection.open();
				handler.handle(new Command(CommandType.VirtConnectionEstabilished));

				Command introduction = this.createCommand(CommandType.RoomPlayerIntroduce);
				introduction.setData(localClient.getName());
				this.issueCommand(introduction);
			} catch (IOException e) {
				Command err = new Command(CommandType.VirtConnectingTimedOut);
				err.setData(e.getMessage());
				handler.handle(err);
				this.connection = null;
				return false;
			}
		}

		return true;
	}

	private boolean handleInvalidResponse() {
		this.invalidResponseCounter++;
		return this.invalidResponseCounter <= 4; // TODO: configurability
	}

	public void disconnect() {
		this.disconnect("");
	}

	synchronized public void disconnect(String reason) {
		this.disconnectReason = reason;
		try {
			if (this.connection == null) {
				System.out.println("Disconnect error: not connected");
				return;
			}
			this.connection.close();
		} catch (NetworksException e) {
			System.out.println("Disconnect error: " + e.getMessage());
		} finally {
			this.connection = null;
		}
	}

	public String getDisconnectReason() {
		return this.disconnectReason;
	}

	public void shutdown() {
		try {
			this.disconnect("Shutting down");
			this.keepRunning = false;
			super.join();
			System.out.println("NetWorks ended succesfully");
		} catch (InterruptedException ex) {
			System.err.println("Failed to close networks: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
	}

	public Command createCommand(CommandType commandType) {
		LastMsgId++;
		return new Command(commandType);
	}
}
