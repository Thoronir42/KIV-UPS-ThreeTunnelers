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

	String clientName;
	private Connection connection;

	private final CommandParser parser;

	private int invalidResponseCounter;

	private String disconnectReason;

	private byte LastMsgId = 0;

	INetCommandHandler handler;

	public NetAdapter() {
		super(NetAdapter.class.getSimpleName());
		this.parser = new CommandParser();
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
			this.clientName = clientName;
		} catch (NetworksException e) {
			Command err = new Command(CommandType.VirtConnectingError, 0);
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

		while (true) {
			try {
				if (connection == null) {
					sleep(200);
					continue;
				}
				if (!connection.isOpen()) {
					try {
						connection.open(this.clientName);
						handler.handle(new Command(CommandType.VirtConnectionEstabilished, 0));

						Command introduction = new Command(CommandType.RoomPlayerIntroduce, 1);
						introduction.setData(clientName);
						this.issueCommand(introduction);
					} catch (IOException e) {
						Command err = new Command(CommandType.VirtConnectingTimedOut, 0);
						err.setData(e.getMessage());
						handler.handle(err);
						this.connection = null;
						continue;
					}
				}

				// and now we wait...
				String data = this.connection.receive();
				if (data == null) {
					Command err = new Command(CommandType.VirtConnectionTerminated, 0);
					handler.handle(err);
					connection = null;
				} else {
					try {
						Command cmd = this.parser.parse(data);
						this.handler.handle(cmd);
					} catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
						Command err = new Command(CommandType.VirtCommandParseError, 0);
						err.setData("For data: " + data);
						this.handler.handle(err);
					}
				}
			} catch (CommandNotRecognisedException e) {
				if (!this.handleInvalidResponse()) {
					return;
				}
			} catch (InterruptedException e) {
				break;
			} catch (IOException e) {
				System.err.println("Network run error: " + e.getClass() + ": " + e.getMessage());
			}
		}
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
				System.out.println("DIsconnect error: not connected");
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

	public void close() {
		try {
			this.disconnect("Shutting down");
			super.interrupt();
			super.join();
			System.out.println("NetWorks ended succesfully");
		} catch (InterruptedException ex) {
			System.err.println("Failed to close networks: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
	}

	public Command createCommand(CommandType commandType) {
		return new Command(commandType, ++LastMsgId);
	}
}
