package tunnelers.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import tunnelers.network.codec.ICodec;
import tunnelers.network.codec.NoCodec;

/**
 *
 * @author Stepan
 */
public class Connection {

	private final ICodec codec;
	protected InetAddress address;
	protected int port;

	private Socket socket;
	protected BufferedReader reader;
	protected BufferedWriter writer;

	public Connection(String adress, int port, int receiveBufferSize) throws NetworksException {
		this(adress, port, receiveBufferSize, new NoCodec());
	}

	public Connection(String adress, int port, int receiveBufferSize, ICodec codec) throws NetworksException {
		try {
			this.codec = codec;
			this.address = InetAddress.getByName(adress);
			this.port = port;
		} catch (IOException e) {
			throw new NetworksException(e);
		}
	}

	public boolean isOpen() {
		return this.socket != null;
	}

	public void open() throws IOException {
		if (this.isOpen()) {
			throw new IOException("Connection is already open");
		}
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

		return this.codec.decode(reader.readLine());
	}

	public String getHostString() {
		return this.address.getHostAddress() + ':' + this.port;
	}
}
