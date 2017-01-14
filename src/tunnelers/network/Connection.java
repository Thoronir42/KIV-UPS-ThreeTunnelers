package tunnelers.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import tunnelers.network.codec.ICodec;
import tunnelers.network.codec.NoCodec;

/**
 *
 * @author Stepan
 */
public class Connection {

	protected final String hostname;
	protected final int port;
	
	
	private final ICodec codec;
	protected InetAddress address;
	
	private long latency;

	private Socket socket;
	protected BufferedReader reader;
	protected BufferedWriter writer;
	protected long lastActive;
	
	private short lastMsgId;
	private int invalidMessageCounter;
	

	public Connection(String hostname, int port, int receiveBufferSize) {
		this(hostname, port, receiveBufferSize, new NoCodec());
	}

	public Connection(String hostname, int port, int receiveBufferSize, ICodec codec) {
		this.hostname = hostname;
		this.port = port;
		this.lastActive = System.currentTimeMillis();
		this.codec = codec;
		this.lastMsgId = 0;
		this.invalidMessageCounter = 0;
	}
	
	public int invalidCounterIncrease(){
		return  ++this.invalidMessageCounter;
	}
	public void invalidCounterReset(){
		this.invalidMessageCounter = 0;
	}

	public boolean isOpen() {
		return this.socket != null;
	}

	public void open() throws UnknownHostException, IOException {
		if (this.isOpen()) {
			throw new IOException("Connection is already open");
		}
		this.address = InetAddress.getByName(this.hostname);
		
		this.socket = new Socket(address, port);

		this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void close() throws IOException {
		if (!isOpen()) {
			return;
		}
		this.socket.close();
		this.socket = null;
		this.writer = null;
		this.reader = null;
	}

	synchronized public void send(String message) throws IOException {
		if (writer == null) {
			throw new IOException("Failed to send message: Connection is not open");
		}

		message = this.codec.encode(message);
		if (message.charAt(message.length() - 1) != '\n') {
			message += '\n';
		}

		this.writer.write(message);
		this.writer.flush();
	}

	public String receive() throws IOException, InterruptedException {
		if (reader == null) {
			throw new IOException("Failed to receive message: Connection is not open");
		}
		String message = reader.readLine();
		if(message == null){
			return null;
		}
		this.lastActive = System.currentTimeMillis();
		
		

		return this.codec.decode(message.trim());
	}

	public String getHostString() {
		return this.hostname + ':' + this.port;
	}

	int getInvalidCounter() {
		return this.invalidMessageCounter;
	}

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}
}
