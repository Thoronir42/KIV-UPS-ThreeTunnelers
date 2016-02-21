package tunnelers.network;

import generic.BackPasser;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import tunnelers.Settings;

/**
 *
 * @author Stepan
 */
public class NetWorks extends Thread {

	private static final int BUFFER_SIZE = 512;
	private static final int HANDSHAKE_ATTEMPTS = 4;
	private static final int HANDSHAKE_WAIT_MILLIS = 950;
	private static final int MILLIS_BEFORE_PANIC = 2500;

	public static NetWorks createInstance() throws IOException {
		Settings s = Settings.getInstance();
		NetWorks nw = new NetWorks(s.getServerAddress(), s.getServerPort(), "Dummy");
		return nw;
	}

	public static NetWorks connectTo(String address, int port, String client) throws IOException, InterruptedException {
		NetWorks tmp = new NetWorks(address, port, client);
		tmp.start();
		tmp.handshake();
		return tmp;
	}

	public static int fetchLobbies(BackPasser<String[]> passer) {
		NCG.NetCommand cmd = new ConnectionCommand.FetchLobbies(Settings.VERSION);

		return 0;
	}

	public static boolean serverPresent(String address, int port) {
		NCG.NetCommand.RoomNumber = 00;
		LeadCommand.StillThere cmd = new LeadCommand.StillThere(0);

		return false;
	}

	private final DatagramSocket datagramSocket;
	InetAddress address;
	int port;
	String clientName;

	private BackPasser<NCG.NetCommand> cmdPasser;
	private Status status;
	private String disconnectReason;

	private NetWorks(String adress, int port, String clientName) throws IOException {
		this.datagramSocket = new DatagramSocket();
		this.address = InetAddress.getByName(adress);
		this.port = port;
		this.clientName = clientName;
		this.status = Status.Joining;

		this.setCommandPasser(new BackPasser<NCG.NetCommand>() {
			@Override
			public void run() {
				nwInternalHandle(this.get());
			}
		});
	}

	public void issueCommand(NCG.NetCommand cmd) {
		String code = cmd.getCommandCode();
		System.out.println(code);
		sendMessage(code);
	}

	public final void setCommandPasser(BackPasser r) {
		this.cmdPasser = r;
	}

	private String receiveMessage() throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		DatagramPacket recv = new DatagramPacket(buffer, buffer.length);
		datagramSocket.receive(recv);
		return (new String(buffer)).trim();
	}

	synchronized public void sendMessage(String message) {
		try {
			byte[] buffer = (message).getBytes();

			DatagramPacket send = new DatagramPacket(buffer, buffer.length, address, port);
			datagramSocket.send(send);
		} catch (IOException e) {
			System.err.println("Unable to send network message: " + e.getMessage());
		}
	}

	private void handleMessage() {
		try {
			String data = receiveMessage();
			NCG.NetCommand cmd = NCG.NetCommand.parse(data);
			if (cmd == null) {
				System.err.println("Netcommand Unrecognised: " + data);
				return;
			} else {
				System.out.println("Received: " + cmd.getCommandCode());
			}
			if (this.cmdPasser != null) {
				this.cmdPasser.pass(cmd);
			} else {
				System.err.format("\"%s\" was not handled, no handler found.%n", cmd.getCommandCode());
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	private boolean handshake() {
		try {
			int attemptsLeft = HANDSHAKE_ATTEMPTS;
			while (attemptsLeft > 0) {
				if (this.joinCancelled()) {
					return false;
				}
				NCG.NetCommand cmd = new ConnectionCommand.CreateLobby(HANDSHAKE_ATTEMPTS - attemptsLeft);
				this.issueCommand(cmd);

				sleep(20);
				if (this.serverResponded()) {
					return true;
				}
				System.out.println("Host didn't respond, retrying " + attemptsLeft + " more times...");
				sleep(HANDSHAKE_WAIT_MILLIS);
				attemptsLeft--;
			}
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		this.disconnect(Status.ServerUnreachable);
		return false;
	}

	synchronized private void nwInternalHandle(NCG.NetCommand cmd) {
		if (cmd == null) {
			this.status = Status.ServerUnrecognised;
			this.disconnectReason = "Server didn't respond properly";
			return;
		}

		if (cmd instanceof LeadCommand.Approve) {
			this.status = Status.ServerReady;
		} else if (cmd instanceof LeadCommand.Deny) {
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

	private boolean serverResponded() {
		return this.status != Status.Joining;
	}

	private boolean joinCancelled() {
		return this.status == Status.JoiningCanceled;
	}

	@Override
	public void run() {
		while (this.keepRunning()) {
			handleMessage();
		}
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
		this.datagramSocket.close();
	}

	synchronized public void cancelJoining() {
		this.status = Status.JoiningCanceled;
	}

	public String getStatusLabel() {
		switch (this.status) {
			default:
				return "Unknown status";
			case Joining:
				return String.format("Attempting to join %s:%d", this.address.getHostAddress(), this.port);
			case JoiningCanceled:
				return String.format("Attempt to join %s:%d was cancelled by user.", this.address.getHostAddress(), this.port);
			case ServerUnreachable:
				return String.format("No host found at %s:%d", this.address.getHostAddress(), this.port);
			case ServerFull:
				return String.format("Server on %s:%d is already full", this.address.getHostAddress(), this.port);
			case ServerUnrecognised:
				return String.format("Server on %s:%d wasn't recognised", this.address.getHostAddress(), this.port);
			case ServerReady:
				return String.format("Server %s:%d is ready to be joined", this.address.getHostAddress(), this.port);
			case Connected:
				return String.format("Connected to %s:%d", this.address.getHostAddress(), this.port);
			case Disconnected:
				return String.format("Disconnected from %s:%d", this.address.getHostAddress(), this.port);
		}
	}

	public String getDisconnectReason() {
		return this.disconnectReason;
	}

	private enum Status {

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
