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
    
    private static final int BUFFER_SIZE = 64;
    
    private final DatagramSocket datagramSocket;
    InetAddress address;        int port;
    String clientName;
    private boolean communicationRelevant;
    private MessagePasser runner;
    
    public NetWorks(String adress, int port, String clientName) throws IOException, NetworksException{
        this.address = InetAddress.getByName(adress);
        this.port = port;
        this.clientName = clientName;
        this.datagramSocket = new DatagramSocket( );
        if (!this.handshake()){
            throw new NetworksException("Handshake failed");
        }
    }
    
    public void issueCommand(INetworkCommand cmd){
        sendMessage(cmd.getCommandCode());
    }
    
    public void setHandleMessage(MessagePasser r){
        this.runner = r;
    }
    
    private String receiveMessage() throws IOException{        
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket recv = new DatagramPacket(buffer, buffer.length );
        datagramSocket.receive( recv );
        return new String( buffer );
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
    
    public void handleMessage(){
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

    synchronized private boolean handshake(){
        try{
            this.sendMessage("handshake-rq");
            String reply = this.receiveMessage().trim();
            System.out.println(reply);
            return reply.equals("SRV:handshake-ok");
        } catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public void run() {
        this.communicationRelevant = true;
        while(communicationRelevant){
            handleMessage();
        }
    }
    
    synchronized public void endCommunication(){
        this.communicationRelevant = false;
        super.interrupt();
        this.datagramSocket.close();
    }    
}

class NetworksException extends IOException{

    NetworksException(String msg) {
        super(msg);
    }
}