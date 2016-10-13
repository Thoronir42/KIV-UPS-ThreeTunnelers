package tunnelers.core.engine;

import generic.BackPasser;
import java.io.IOException;
import javafx.event.EventHandler;
import tunnelers.network.CommandNotRecognisedException;
import tunnelers.network.Connection;
import tunnelers.network.NetworkEvent;
import tunnelers.network.NetworksException;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandParser;
import tunnelers.network.command.CommandType;

/**
 *
 * @author Stepan
 */
public class NetWorks extends Thread {

	private static final int BUFFER_SIZE = 512;
	private static final int HANDSHAKE_ATTEMPTS = 4;
	private static final int HANDSHAKE_WAIT_MILLIS = 950;
	private static final int MILLIS_BEFORE_PANIC = 2500;

	String clientName;
	private Connection connection;
	
	private EventHandler<NetworkEvent> onCommandReceived;

	private CommandParser parser;
	
	private int invalidResponseCounter;
	
	private Status status;
	private String disconnectReason;
	
	private byte LastMsgId = 0;

	public NetWorks() {
		this.status = Status.Joining;

		this.setCommandPasser(new BackPasser<Command>() {
			@Override
			public void run() {
				nwInternalHandle(this.get());
			}
		});
		
		this.parser = new CommandParser();
	}
	
	public boolean serverPresent(String address, int port) {
		return false;
	}
	
	public boolean connectTo(String clientName, String adress, int port){
		try{
			this.connection = new Connection(adress, port, BUFFER_SIZE);
			this.clientName = clientName;
			return true;
		} catch (NetworksException e){ 
			return false;
		}
	}

	public boolean issueCommand(Command cmd) {
		String code = parser.parse(cmd);
		System.out.println(code);
		try{
			this.connection.send(code);
			return true;
		} catch (NetworksException e){
			return false;
		}
	}

	public final void setCommandPasser(BackPasser r) {
		System.err.println("BackPasser is deprecated in setCommandPasser");
		//FIXME this.cmdPasser = r;
	}

	synchronized private void nwInternalHandle(Command cmd) {
		if (cmd == null) {
			this.status = Status.ServerUnrecognised;
			this.disconnectReason = "Server didn't respond properly";
			return;
		}

		if (cmd.getType() == CommandType.LeadApprove) {
			this.status = Status.ServerReady;
		} else if (cmd.getType() == CommandType.LeadDeny) {
			switch (0) {
				case 0:
					this.status = Status.ServerFull;
					break;
				case 1:
					this.status = Status.ServerUnrecognised;
					break;
			}
		}
	}

	private boolean keepRunning() {
		return this.status.keepRunning;
	}

	@Override
	public void run() {
		while (this.keepRunning()) {
			try{
				String data = this.connection.receiveMessage();
				Command cmd = this.parser.parse(data);
				if(cmd == null){
					throw new CommandNotRecognisedException(data);
				}
			} catch(CommandNotRecognisedException e){
				if(!this.handleInvalidResponse()){
					return;
				}
			} catch(InterruptedException e){
				return;
			} catch(IOException e){
				System.err.println(e.getMessage());
			}
		}
	}
	
	private boolean handleInvalidResponse(){
		this.invalidResponseCounter++;
		return this.invalidResponseCounter <= 12; // TODO: configurability
	}

	public boolean canConnect() {
		return this.status == Status.ServerReady;
	}

	public void disconnect() {
		this.disconnect(Status.Disconnected);
	}

	synchronized public void disconnect(String reason) {
		this.disconnectReason = reason;
		this.disconnect(Status.Kicked);
	}

	synchronized private void disconnect(Status status) {
		super.interrupt();
		this.status = status;
	}

	synchronized public void cancelJoining() {
		this.status = Status.JoiningCanceled;
	}

	public String getStatusLabel() {
		String address = this.connection.getHostString();
		switch (this.status) {
			default:
				return "Unknown status";
			case Joining:
				return String.format("Attempting to join %s", address);
			case JoiningCanceled:
				return String.format("Attempt to join %s was cancelled by user.", address);
			case ServerUnreachable:
				return String.format("No host found at %s", address);
			case ServerFull:
				return String.format("Server on %s is already full", address);
			case ServerUnrecognised:
				return String.format("Server on %s wasn't recognised", address);
			case ServerReady:
				return String.format("Server %s is ready to be joined", address);
			case Connected:
				return String.format("Connected to %s", address);
			case Disconnected:
				return String.format("Disconnected from %s", address);
		}
	}

	public String getDisconnectReason() {
		return this.disconnectReason;
	}

	public void close() {
		if(connection != null){
			this.connection.close();
		}
		try {	
			this.interrupt();
			this.join();
			System.out.println("NetWorks ended succesfully");
		} catch (InterruptedException ex) {
			System.err.println("Failed to close networks: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
	}

	Command createCommand(CommandType commandType) {
		return new Command(commandType, ++LastMsgId);
	}

	private enum Status {
		Idle,
		Joining, JoiningCanceled(false),
		ServerReady, ServerUnreachable(false), ServerFull(false), ServerUnrecognised(false),
		Connected,
		Disconnected(false), Kicked(false);

		public final boolean keepRunning;

		private Status() {
			this(true);
		}

		private Status(boolean keepRunning) {
			this.keepRunning = keepRunning;
		}
	}

}
