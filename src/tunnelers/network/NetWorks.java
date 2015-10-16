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
    InetAddress address;
    int port;
    private boolean communicationRelevant;
    private MessagePasser runner;
    
    public NetWorks(String adress, int port) throws IOException{
        this.address = InetAddress.getByName(adress);
        this.port = port;
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
            byte[] buffer = message.getBytes();

            System.out.format(" Odesilam data (%d)%n", message.length());
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
            System.out.format( "Datain:\t%s%n", data);
            System.out.println("Networks received message "+data);
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
            System.out.println("Message handled");
        }
    }
    
    synchronized public void endCommunication(){
        this.communicationRelevant = false;
    }
    
    
}