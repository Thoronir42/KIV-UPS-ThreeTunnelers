package tunnelers.network;

import java.io.IOException;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandParser;
import tunnelers.network.command.CommandType;

/**
 *
 * @author Stepan
 */
public class NetWorks extends Thread {

	private static final int BUFFER_SIZE = 512;

	String clientName;
	private Connection connection;

	private final CommandParser parser;

	private int invalidResponseCounter;

	private String disconnectReason;

	private byte LastMsgId = 0;

	INetCommandHandler handler;

	public NetWorks() {
		this.parser = new CommandParser();
	}
	
	public void setHandler(INetCommandHandler handler){
		this.handler = handler;
	}

	public boolean serverPresent(String address, int port) {
		return false;
	}

	public boolean connectTo(String clientName, String adress, int port) {
		try {
			this.connection = new Connection(adress, port, BUFFER_SIZE);
			this.clientName = clientName;
			this.connection.send(clientName);
			return true;
		} catch (NetworksException e) {
			System.err.println("Connecting failed: " + e.getMessage());
			return false;
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
	
	public void tmpSendText(String Text){
		try{
			this.connection.send(Text);
		} catch (Exception e){
			System.err.println("Send text error: " + e.getMessage());
		}
		
	}

	@Override
	public void run() {
		System.out.println("Networks: starting");
		
		while (true) {
			try {
				if(connection == null){
					sleep(200);
					continue;
				}
				String data = this.connection.receive();
				
				Command cmd = new Command(CommandType.MsgPlain, (byte)0);
				cmd.setData(data);
				
				this.handler.handle(cmd);
				if(true)
					continue;
				cmd = this.parser.parse(data);
				this.handler.handle(cmd);
			} catch (CommandNotRecognisedException e) {
				if (!this.handleInvalidResponse()) {
					return;
				}
			} catch (InterruptedException e) {
				return;
			} catch (IOException e) {
				System.err.println("Network run error: " + e.getClass() + ": " +e.getMessage());
			}
		}
	}

	private boolean handleInvalidResponse() {
		this.invalidResponseCounter++;
		return this.invalidResponseCounter <= 12; // TODO: configurability
	}

	public void disconnect() {
		this.disconnect("");
	}

	synchronized public void disconnect(String reason) {
		this.disconnectReason = reason;
		try{
			if(this.connection == null){
				System.out.println("DIsconnect error: not connected");
				return;
			}
			this.connection.close();
		} catch (NetworksException e){
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
