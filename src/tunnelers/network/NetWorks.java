package tunnelers.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



/**
 *
 * @author Stepan
 */
public class NetWorks extends Thread{
    
    private static final int BUFFER_SIZE = 512;
    private static final int HANDSHAKE_ATTEMPTS = 4;
    private static final int HANDSHAKE_WAIT_MILLIS = 950;

    public static NetWorks connectTo(String address, int port, String client) throws IOException, InterruptedException{
        NetWorks tmp = new NetWorks(address, port, client);
        tmp.start();
        tmp.handshake();
        return tmp;
    }
    
    private final DatagramSocket datagramSocket;
    InetAddress address;        int port;
    String clientName;
    private MessagePasser runner;
    private Status status;
    private String disconnectReason;
    
    private NetWorks(String adress, int port, String clientName) throws IOException, NetworksException{
        this.datagramSocket = new DatagramSocket(  );
        //datagramSocket.connect(address, port);
        System.out.println("Socket connected: "+datagramSocket.isConnected());
        this.address = InetAddress.getByName(adress);
        this.port = port;
        this.clientName = clientName;
        this.status = Status.Joining;
        
        
        this.setMessageHandler(new MessagePasser(){
            @Override
            public void run(){
                confirmHandshake();
            }
        });
    }
    
    public void issueCommand(NetCommand cmd){
        String code = cmd.getCommandCode();
        System.out.println(code);
        sendMessage(code);
    }
    
    public final void setMessageHandler(MessagePasser r){
        this.runner = r;
    }
    
    private String receiveMessage() throws IOException{        
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket recv = new DatagramPacket(buffer, buffer.length );
        datagramSocket.receive( recv );
        return (new String(buffer)).trim();
    }
    
    synchronized public void sendMessage(String message){
        try{
            byte[] buffer = (clientName+":"+message).getBytes();
            
            DatagramPacket send = new DatagramPacket(buffer, buffer.length, address, port);
            datagramSocket.send(send);
        } catch (IOException e ){
            System.err.println("Unable to send network message: "+e.getMessage());
        }
    }
    
    private void handleMessage(){
        try{
            String data = receiveMessage();
            if(this.runner != null){
                this.runner.passMessage(data);                
            } else {
                System.err.format("\"%s\" was not handled, no handler found.%n", data);
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    private boolean handshake(){
        try{
            int attemptsLeft = HANDSHAKE_ATTEMPTS;
            while(attemptsLeft > 0){
				if(this.joinCancelled()){
					return false;
				}
				NetCommand cmd = new ConnectionCommand.Join(HANDSHAKE_ATTEMPTS - attemptsLeft);
                this.issueCommand(cmd);
				
                sleep(20);
                if(this.serverResponded()){
                    return true;
                }
                System.out.println("Connection failed, retrying "+attemptsLeft+" more times...");
                sleep(HANDSHAKE_WAIT_MILLIS);
                attemptsLeft--;
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        this.disconnect(Status.ServerUnreachable);
        return false;
    }
    synchronized private void confirmHandshake(){
        this.status = Status.ServerReady;
    }
    
    private boolean keepRunning(){
        Status[] restricted = new Status[]{Status.Disconnected, Status.JoiningCanceled, Status.ServerFull, Status.ServerUnreachable};
		for(Status s : restricted){
			if (this.status == s){
				return false;
			}
		}
		return true;
    }
    private boolean serverResponded(){
        return this.status != Status.Joining;
    }
    private boolean joinCancelled(){
		return this.status == Status.JoiningCanceled;
	}
    
    @Override
    public void run() {
        while(this.keepRunning()){
            handleMessage();
        }
    }
    
    public boolean canConnect(){
        return this.status == Status.ServerReady;
    }
    
    public void disconnect(){
        this.disconnect(Status.Disconnected);
    }
    
    synchronized public void disconnect(String reason){
        this.disconnectReason = reason;
        this.disconnect(Status.Kicked);
    }
    
    synchronized private void disconnect(Status status){
        super.interrupt();
        this.status = status;
        this.datagramSocket.close();
    }

	synchronized public void cancelJoinin(){
		this.status = Status.JoiningCanceled;
	}
	
    public String getStatusLabel() {
        switch(this.status){
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
            case ServerReady:
                return String.format("Server %s:%d is ready to be joined", this.address.getHostAddress(), this.port);
            case Connected:
                return String.format("Connected to %s:%d", this.address.getHostAddress(), this.port);
            case Disconnected:
                return String.format("Disconnected from %s:%d", this.address.getHostAddress(), this.port);
        }
        
    }
    
    private enum Status{
        Joining, JoiningCanceled,
        ServerReady, ServerUnreachable, ServerFull,
        Connected,
        Disconnected, Kicked
    }
    
}

class NetworksException extends IOException{

    NetworksException(String msg) {
        super(msg);
    }
}