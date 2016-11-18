package tunnelers.network;

import java.io.IOException;
import tunnelers.common.IUpdatable;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandParser;
import tunnelers.network.command.CommandType;

/**
 *
 * @author Stepan
 */
public class NetAdapter extends Thread implements IUpdatable {

	private static final int BUFFER_SIZE = 512;

	private Connection connection;
	private NetClient localClient;
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

	@Override
	public void update(long tick) {
		if (this.connection == null) {
			return;
		}

		long now = System.currentTimeMillis();
		long d = now - this.connection.lastActive;
		System.out.println(d);
		if (d > 5000) {
			this.disconnect("Server was irresponsive.");
		}
	}

	public boolean issueCommand(Command cmd) {
		String message = parser.parse(cmd);
		try {
			if(this.connection == null){
				System.err.println("Attempted to send a command while connection was null.");
				return false;
			}
			this.connection.send(message);
			System.out.println("Connection sent: " + message);
			return true;
		} catch (IOException e) {
			return false;
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
				System.out.println("Connection received: " + message);

				if (message == null || message.length() == 0) {
					System.out.println("Something is wronk");
					Command err = new Command(CommandType.VirtConnectionTerminated);
					this.handler.handle(err);
					connection = null;
					continue;
				}

				try {
					Command cmd = this.parser.parse(message);
					System.out.println("Handling cmd: " + cmd.toString());
					this.handler.handle(cmd);
					this.connection.invalidCounterReset();
				} catch (CommandNotRecognisedException e) {
					System.err.println(e.toString());
					if (this.connection.invalidCounterIncrease() > 4) {// TODO: configurability
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
		} catch (IOException e) {
			System.err.println("Disconnect error: " + e.getMessage());
		} finally {
			this.connection = null;
			this.handler.handle(new Command(CommandType.VirtConnectionTerminated));
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
		return new Command(commandType);
	}
}
