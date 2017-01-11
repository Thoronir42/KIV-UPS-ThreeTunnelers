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
		this(null);
	}

	public NetAdapter(INetCommandHandler handler) {
		super(NetAdapter.class.getSimpleName());
		this.parser = new CommandParser();
		this.keepRunning = true;
		this.setHandler(handler);
	}

	public void setHandler(INetCommandHandler handler) {
		this.handler = handler;
	}

	public boolean serverPresent(String address, int port) {
		return false;
	}

	/**
	 * Non-blocking Schedules connection creation.
	 *
	 * @param clientName
	 * @param adress
	 * @param port
	 */
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
		if (this.connection == null || !this.connection.isOpen()) {
			return;
		}

		long now = System.currentTimeMillis();
		long d = now - this.connection.lastActive;
		this.log(String.format("Since last message from server: %d ms", d));
		if (d > 5000) {
			this.disconnect("Server was irresponsive.");
		}
	}

	public boolean issueCommand(Command cmd) {
		String message = parser.parse(cmd);
		try {
			if (this.connection == null) {
				this.log("Attempted to send a command while connection was null.");
				return false;
			}
			this.connection.send(message);
			this.log("Connection sent: " + message);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public void run() {
		this.log("Starting");

		while (this.keepRunning) {
			try {
				if (!isValidConnection(connection)) {
					continue;
				}

				// and now we wait...
				String message = this.connection.receive();
				this.log("Connection received: " + message);

				if (message == null || message.length() == 0) {
					this.log("Connection reset");
					Command err = new Command(CommandType.VirtConnectionTerminated);
					this.handler.handle(err);
					connection = null;
					continue;
				}

				try {
					Command cmd = this.parser.parse(message);
					this.log("Handling cmd: " + cmd.toString());
					if (this.handler.handle(cmd)) {
						this.connection.invalidCounterReset();
					} else {
						this.connection.invalidCounterIncrease();
					}

				} catch (CommandNotRecognisedException e) {
					this.logError(e.toString());
					this.connection.invalidCounterIncrease();
				}
				if (this.connection.getInvalidCounter() > 2) {
					this.disconnect("Received too many invallid messages");
				}
			} catch (InterruptedException e) {
				this.logError("Network: waiting interrupted");
				break;
			} catch (IOException e) {
				this.logError("Network run error: " + e.getClass() + ": " + e.getMessage());
				connection = null;
			}
		}
	}

	/**
	 * Ensures that provided connection is valid and returns true. If there are
	 * problems - no connection, connection creation not scheduled, returns
	 * false.
	 *
	 * @param connection
	 * @return
	 */
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

				Command introduction = this.createCommand(CommandType.RoomClientIntroduce);
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

	synchronized public void disconnect() {
		this.disconnect("");
	}

	synchronized public void disconnect(String reason) {
		this.disconnectReason = reason;
		try {
			if (this.connection == null) {
				this.logError("Disconnect error: not connected");
				return;
			}
			this.connection.close();
			this.log("disconnected: " + reason);
		} catch (IOException e) {
			this.logError("Disconnect error: " + e.getMessage());
		} finally {
			this.connection = null;
			Command terminator = new Command(CommandType.VirtConnectionTerminated);
			terminator.setData(reason);
			this.handler.handle(terminator);
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
			this.log("NetWorks ended succesfully");
		} catch (InterruptedException ex) {
			this.logError("Failed to close networks: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
	}

	private void log(String message) {
		System.out.println(this.getName() + ": " + message);
	}

	private void logError(String message) {
		System.err.println(this.getName() + ": " + message);
	}

	public Command createCommand(CommandType commandType) {
		return new Command(commandType);
	}
}
