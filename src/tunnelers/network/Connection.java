package tunnelers.network;

import java.io.BufferedReader;
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
	private final Socket socket;
	protected InetAddress address;
	protected int port;

	protected BufferedReader reader;
	protected OutputStreamWriter writer;

	public Connection(String adress, int port, int receiveBufferSize) throws NetworksException {
		this(adress, port, receiveBufferSize, new NoCodec());
	}

	public Connection(String adress, int port, int receiveBufferSize, ICodec codec) throws NetworksException {
		try {
			this.codec = codec;
			this.address = InetAddress.getByName(adress);
			this.port = port;
			this.socket = new Socket(address, port);

			this.writer = new OutputStreamWriter(socket.getOutputStream());
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			throw new NetworksException(e);
		}
	}

	synchronized public void send(String message) throws NetworksException {
		if (message.charAt(message.length() - 1) != '\n') {
			message += '\n';
		}

		try {
			this.writer.write(message);
			this.writer.flush();
		} catch (IOException e) {
			throw new NetworksException(e);
		}
	}

	public String receive() throws IOException, InterruptedException {
		String rcv = reader.readLine();
		System.out.println("Connection received: " + rcv);
		return rcv;
	}

	public void close() throws NetworksException {
		try {
			this.socket.close();
		} catch (IOException e) {
			throw new NetworksException(e);
		}
	}

	public String getHostString() {
		return this.address.getHostAddress() + ':' + this.port;
	}
}
