package tunnelers.network;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import tunnelers.common.IUpdatable;
import tunnelers.core.engine.PersistentString;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandParser;
import tunnelers.network.command.CommandType;
import tunnelers.network.command.INetworkProcessor;
import tunnelers.network.command.Signal;

/**
 *
 * @author Stepan
 */
public final class NetAdapter extends Thread implements IUpdatable {

	private static final int BUFFER_SIZE = 512;

	private Connection connection;
	private PersistentString connectionSecret;
	private String disconnectReason;

	private final CommandParser parser;
	private INetworkProcessor handler;
	private boolean keepRunning;

	public NetAdapter() {
		this(null);
	}

	public NetAdapter(INetworkProcessor handler) {
		super(NetAdapter.class.getSimpleName());
		this.parser = new CommandParser();
		this.keepRunning = true;
		this.setHandler(handler);
	}

	public void setHandler(INetworkProcessor handler) {
		this.handler = handler;
	}

	public boolean serverPresent(String address, int port) {
		return false;
	}

	/**
	 * Non-blocking Schedules connection creation.
	 *
	 * @param connectionSecret
	 * @param hostname
	 * @param port
	 */
	public void connectTo(PersistentString connectionSecret, String hostname, int port) {
		this.connectionSecret = connectionSecret;
		if (this.connectionSecret == null) {
			throw new IllegalArgumentException("Connection secret must be specified");
		}

		this.connection = new Connection(hostname, port, BUFFER_SIZE);
	}

	@Override
	public void update(long tick) {
		if (this.connection == null || !this.connection.isOpen()) {
			return;
		}

		long now = System.currentTimeMillis();
		long d = now - this.connection.lastActive;
		int timeout = 60000;
		if (d > timeout) {
//			this.disconnect("Server failed to communicate for " + (timeout / 1000) + " seconds.");
		}
	}

	public boolean send(Command cmd) {
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
				if (!ensureConnectionValid(connection)) {
					sleep(200); // TODO? optimize
					continue;
				}
				try {
					Command cmd = this.receiveCommand();
					if (!this.handler.handle(cmd)) {
						throw new CommandNotHandledException(cmd);
					}
					if (this.connection == null) {
						continue;
					}
					this.connection.invalidCounterReset();
				} catch (CommandException e) {
					this.logError("Command error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
					this.connection.invalidCounterIncrease();
				}

				if (this.connection.getInvalidCounter() > 2) {
					this.disconnect("Received too many invallid messages");
				}

			} catch (InterruptedException e) {
				this.logError("Network: waiting interrupted");
			} catch (IOException e) {
				this.logError("Connection error: " + e.toString());
				this.handler.signal(new Signal(Signal.Type.ConnectionReset));
				connection = null;
			}
		}
	}

	private Command receiveCommand() throws IOException, InterruptedException, CommandException {
		String message = this.connection.receive();

		if (message == null || message.length() == 0) {
			throw new IOException("Connection reset");
		}

		return this.parser.parse(message);

	}

	/**
	 * Ensures that provided connection is valid and returns true. If there are
	 * problems - no connection, connection creation not scheduled, returns
	 * false.
	 *
	 * @param connection
	 * @return
	 */
	private boolean ensureConnectionValid(Connection connection) {
		if (connection == null) {
			return false;
		}
		if (connection.isOpen()) {
			return true;
		} else {
			try {
				connection.open(5000);
				this.handler.signal(new Signal(Signal.Type.ConnectionEstabilished));

				Command introduction = this.createCommand(CommandType.LeadIntroduce);
				String secret = this.connectionSecret.get();
				if (secret.length() > 0) {
					introduction.append((byte) 1).append(secret);
				} else {
					introduction.append((byte) 0);
				}

				this.send(introduction);

				return true;
			} catch (UnknownHostException e) {
				this.handler.signal(new Signal(Signal.Type.UnknownHost, e.getMessage()));
			} catch (SocketTimeoutException e) {
				this.handler.signal(new Signal(Signal.Type.ConnectingTimedOut));
			} catch (NoRouteToHostException e) {
				this.handler.signal(new Signal(Signal.Type.ConnectionNoRouteToHost));
			} catch (IOException e) {
				System.err.println(e.toString());
				this.handler.signal(new Signal(Signal.Type.ConnectingFailedUnexpectedError, e.getMessage()));
			}
			this.log("Closing connection");
			this.connection = null;
		}
		return false;

	}

	synchronized public void disconnect() {
		this.disconnect("Closed by client");
	}

	synchronized public void disconnect(String reason) {
		this.disconnectReason = reason;
		try {
			if (this.connection != null) {
				this.connection.close();
				this.log("disconnected: " + reason);
			}
		} catch (IOException e) {
			this.logError("Disconnect error: " + e.getMessage());
		} finally {
			this.connection = null;
			this.handler.signal(new Signal(Signal.Type.ConnectionReset, reason));
		}
	}

	public String getDisconnectReason() {
		return this.disconnectReason;
	}

	public void shutdown() {
		try {
			this.disconnect("Shutting down");
			this.keepRunning = false;
			super.interrupt();
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

	public String getHostLocator() {
		if (this.connection == null) {
			return "N/A";
		}

		return connection.getHostString();
	}
}
