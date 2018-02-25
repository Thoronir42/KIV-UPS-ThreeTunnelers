package tunnelers.network;

import tunnelers.network.adapter.AAdapter;
import tunnelers.network.adapter.ConnectionStatus;
import tunnelers.network.codec.ICodec;
import tunnelers.network.command.*;
import tunnelers.network.handling.INetworkProcessor;
import tunnelers.network.handling.ISignalHandler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public final class Networks extends Thread implements ISignalHandler {
	private static final Logger log = Logger.getLogger(Networks.class.getSimpleName());

	private static final int TIMEOUT_PERIOD = 60000;
	private static final int MAX_INVALID_MESSAGES = 4;

	private final AAdapter adapter;
	private final ICodec codec;

	private String disconnectReason;

	private final CommandParser parser;
	private INetworkProcessor handler;
	private boolean keepRunning;
	private String connectionSecret;

	public Networks(AAdapter adapter, ICodec codec) {
		super(Networks.class.getSimpleName());

		this.adapter = adapter;
		this.codec = codec;

		this.parser = new CommandParser();
		this.keepRunning = true;

		adapter.setSignalHandler(this);
	}

	// todo: implement
	public boolean testServer(String address, int port) {
		return this.adapter.testServer(address, port);
	}

	/**
	 * Non-blocking Schedules connection creation.
	 */
	public boolean connectTo(String hostname, int port, String connectionSecret) {
		this.connectionSecret = connectionSecret;
		try {
			log.info("Creating connection to " + hostname + ":" + port);
			this.adapter.connectTo(hostname, port);
			log.fine("Connection created");
			return true;
		} catch (UnknownHostException e) {
			this.signal(new Signal(Signal.Type.UnknownHost, e.getMessage()));
			return false;
		}
	}

	public void update(long tick) {
		if (!this.adapter.isOpen()) {
			return;
		}

		long now = System.currentTimeMillis();
		long d = now - this.adapter.getLastActive();

		if (d > TIMEOUT_PERIOD) {
			// todo: implement timeout ping / disconnection
//			this.disconnect("Server failed to communicate for " + (timeout / 1000) + " seconds.");
		}
	}

	public boolean send(Command cmd) {
		if (!this.adapter.isOpen()) {
			log.warning("Attempted to send a command while connection is not open.");
			return false;
		}

		try {
			log.fine("Sending command " + cmd.getType().name());
			String message = parser.parse(cmd);
			message = this.codec.encode(message);
			this.adapter.send(message);

			log.fine("Connection sent: " + message);
			return true;
		} catch (IOException e) {
			log.warning("Command send failed: " + e.toString());
			return false;
		}
	}

	@Override
	public void run() throws CommandNotHandledException {
		log.info("Adapter thread starting");

		while (this.keepRunning) {
			try {
				ConnectionStatus status = adapter.getStatus();
				if (status == ConnectionStatus.Idle) {
					sleep(200); // TODO? optimize
					continue;
				}
				if (status == ConnectionStatus.Connecting) {
					log.info("Opening connection");
					adapter.open();
					log.fine("Connection opened");
					continue;
				}
				if (status == ConnectionStatus.Connected) {
					Command introduction = this.createCommand(CommandType.LeadIntroduce);
					if (connectionSecret.length() > 0) {
						introduction.append((byte) 1).append(connectionSecret);
					} else {
						introduction.append((byte) 0);
					}

					this.send(introduction);
					this.adapter.setStatus(ConnectionStatus.IntroductionSent);
					continue;
				}

				Command cmd = this.receiveCommand();
				if (status == ConnectionStatus.IntroductionSent) {
					if (cmd.getType() != CommandType.LeadIntroduce) {
						throw new CommandNotHandledException(cmd);
					}
					this.adapter.setStatus(ConnectionStatus.Identified);
				}

				if (!this.handler.handle(cmd)) {
					throw new CommandNotHandledException(cmd);
				}
				this.adapter.invalidCounterReset();

			} catch (CommandException e) {
				log.severe("Command error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
				this.adapter.invalidCounterIncrease();
				if (this.adapter.getInvalidCounter() > MAX_INVALID_MESSAGES) {
					this.disconnect("Received too many invallid messages");
				}

			} catch (InterruptedException e) {
				log.warning("Network: waiting interrupted");
			} catch (IOException e) {
				log.warning("TcpConnection error: " + e.toString());
				this.signal(new Signal(Signal.Type.ConnectionReset));
				this.adapter.close("Connection reset");
			}
		}
	}

	private void runIdle() {

	}


	private Command receiveCommand() throws IOException, CommandException {
		String message = this.adapter.receive();

		return this.parser.parse(message);
	}

	synchronized public void disconnect(String reason) {
		this.disconnectReason = reason;
		this.adapter.close(reason);
	}

	public void shutdown() {
		try {
			this.disconnect("Shutting down");
			this.keepRunning = false;
			super.interrupt();
			super.join();
			log.info("NetWorks ended succesfully");
		} catch (InterruptedException ex) {
			log.warning("Failed to close networks: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
	}

	public void setHandler(INetworkProcessor handler) {
		this.handler = handler;
	}


	public String getDisconnectReason() {
		return this.disconnectReason;
	}

	public Command createCommand(CommandType commandType) {
		return new Command(commandType);
	}

	public String getHostLocator() {
		return adapter.getHostString();
	}

	public void signal(Signal signal) {
		log.fine("Handling signal " + signal.getType().name());

		switch (signal.getType()) {
			case ConnectionEstablished:
				this.adapter.setStatus(ConnectionStatus.Connected);
				break;

			case ConnectionReset:
			case ConnectingFailedUnexpectedError:
			case ConnectingTimedOut:
			case ConnectionNoRouteToHost:
			case UnknownHost:
				this.adapter.setStatus(ConnectionStatus.Idle);
				break;
		}


		this.handler.signal(signal);
	}
}
