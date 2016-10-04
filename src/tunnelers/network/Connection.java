package tunnelers.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;
import tunnelers.network.codec.ICodec;
import tunnelers.network.codec.NoCodec;
import tunnelers.network.command.NCG;

/**
 *
 * @author Stepan
 */
public class Connection {
	
	private final ICodec codec;
	private final DatagramSocket datagramSocket;
	protected InetAddress address;
	protected int port;
	
	protected Semaphore rcvSemaphore;
	
	protected final byte[] buffer;
	
	public Connection(String adress, int port, int receiveBufferSize) throws NetworksException{
		this(adress, port, receiveBufferSize, new NoCodec());
	}
	
	public Connection(String adress, int port, int receiveBufferSize, ICodec codec) throws NetworksException {
		try{
			this.codec = codec;
			this.datagramSocket = new DatagramSocket();
			this.address = InetAddress.getByName(adress);
			this.port = port;
			
			this.rcvSemaphore = new Semaphore(16, true);
			
			this.buffer = new byte[receiveBufferSize];
		} catch (UnknownHostException | SocketException e){
			throw new NetworksException(e);
		}
	}
	
	synchronized public void send(String message) throws NetworksException {
		try {
			int i = 0;
			for(byte b : message.getBytes()){
				buffer[i] = b;
			}

			DatagramPacket send = new DatagramPacket(buffer, buffer.length, address, port);
			datagramSocket.send(send);
		} catch (IOException e) {
			throw new NetworksException(e.getMessage());
		}
	}
	
	private String receiveMessage() throws IOException, InterruptedException {
		DatagramPacket recv = new DatagramPacket(buffer, buffer.length);
		datagramSocket.receive(recv);
		return (new String(buffer)).trim();
	}

	

	public NCG.NetCommand handleMessage() throws IOException, CommandNotRecognisedException, InterruptedException{
		String data = receiveMessage();
		NCG.NetCommand cmd = NCG.NetCommand.parse(data);
		if (cmd == null) {
			throw new CommandNotRecognisedException(data);
		}
		
		return cmd;
	}

	void close() {
		this.datagramSocket.close();
	}

	String getHostString() {
		return this.address.getHostAddress() + ':' + this.port;
	}
}
