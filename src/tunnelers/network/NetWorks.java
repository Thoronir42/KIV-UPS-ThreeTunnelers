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
    private static final int HANDSHAKE_ATTEMPTS = 10;

    public static NetWorks connectTo(String address, int port, String client) throws IOException, NetworksException, InterruptedException{
        NetWorks tmp = new NetWorks(address, port, client);
        tmp.start();
        if (!tmp.handshake()){
            throw new NetworksException("Handshake failed");
        }
        return tmp;
    }
    
    private final DatagramSocket datagramSocket;
    InetAddress address;        int port;
    String clientName;
    private boolean connectionConfirmed = false;
    private MessagePasser runner;
    
    public NetWorks(String adress, int port, String clientName) throws IOException, NetworksException{
        this.datagramSocket = new DatagramSocket(  );
        //datagramSocket.connect(address, port);
        System.out.println("Socket connected: "+datagramSocket.isConnected());
        this.address = InetAddress.getByName(adress);
        this.port = port;
        this.clientName = clientName;
        
        
        this.setMessageHandler(new MessagePasser(){
            @Override
            public void run(){
                confirmHandshake();
            }
        });
    }
    
    public void issueCommand(ANetworkCommand cmd){
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
    
    private void handleMessage(){
        try{
            System.out.format("Waiting for server...\n");
            String data = receiveMessage();
            if(this.runner != null){
                System.out.format("Handling: %s\n", data);
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
            int attemptsLeft = HANDSHAKE_ATTEMPTS;
            while(attemptsLeft > 0){
                this.issueCommand(new LobbyCommand(LobbyCommand.Action.Join, new Object[]{HANDSHAKE_ATTEMPTS - attemptsLeft}));
                wait(20);
                if(this.connectionConfirmed){
                    return true;
                }
                System.out.println("Connection failed, retrying "+attemptsLeft+" more times...");
                sleep(750);
                attemptsLeft--;
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        this.disconnect();
        return false;
    }
    synchronized private void confirmHandshake(){
        this.connectionConfirmed = true;
    }
    
    @Override
    public void run() {
        while(true){
            handleMessage();
            if(!connectionConfirmed){ break; }
        }
    }
    
    synchronized public void disconnect(){
        this.connectionConfirmed = false;
        super.interrupt();
        this.datagramSocket.close();
    }
}

class NetworksException extends IOException{

    NetworksException(String msg) {
        super(msg);
    }
}