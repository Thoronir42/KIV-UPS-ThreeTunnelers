package tunnelers.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javafx.event.Event;



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
    
    public NetWorks(String adress, int port, String clientName) throws IOException{
        this.address = InetAddress.getByName(adress);
        this.port = port;
        this.clientName = clientName;
        this.datagramSocket = new DatagramSocket( );
    }
    
    public void setHandleMessage(MessagePasser r){
        this.runner = r;
    }
    
    public void issueCommand(INetworkCommand cmd){
        sendMessage(cmd.getCommandCode());
    }
    
    synchronized public void sendMessage(String message){
        try{
            byte[] buffer = (clientName+":"+message).getBytes();
            
            DatagramPacket send = new DatagramPacket(buffer, buffer.length, address, port);
            datagramSocket.send(send);
        } catch (IOException e ){
            System.err.println(e.getMessage());
        }
    }
    
    public void handleMessage(){
        byte[] buffer = new byte[BUFFER_SIZE];
        try{
            DatagramPacket recv = new DatagramPacket(buffer, buffer.length );
            datagramSocket.receive( recv );
            String data = new String( buffer );
            if(this.runner != null){
                this.runner.passMessage(data);
                
            } else {
                System.err.format("\"%s\" was not handled, no handler found.%n", data);
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
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