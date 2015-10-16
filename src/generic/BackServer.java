package generic;

import java.net.*;
import java.io.*;
import java.util.Scanner;

//	-Djava.net.preferIPv4Stack=true
public class BackServer extends Thread
{
    private static final int BUFFER_SIZE = 60; 
    
    DatagramSocket ds;    
    
    private DatagramPacket lastReceived;
    
    public boolean cont;
    
    @Override
    public void run(){
        try {
            ds = new DatagramSocket( tunnelers.structure.Settings.DEFAULT_PORT );
            
            cont = true;
            while (cont) {
                System.out.format("Serverrr (%s:%d) ceka...%n", ds.getLocalSocketAddress(), ds.getLocalPort());
                
                String data = receiveMessage();
                System.out.println( ">> "+data);
                
                if(!data.contains(":")){
                    data = "NA:"+data;
                }
                sendMessage(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String receiveMessage() throws IOException{
        byte[] buffer = new byte[BUFFER_SIZE];
        lastReceived = new DatagramPacket(buffer, buffer.length);
        ds.receive(lastReceived);
        
        InetAddress adresa = lastReceived.getAddress();
        int port = lastReceived.getPort();

        System.out.format("> Klient %s:%d(%s)%n", adresa.getHostAddress(), port, adresa.getHostName());
        return new String( buffer );
    }
    synchronized public void sendMessage(String text) throws IOException{
        byte[] buffer = text.getBytes();
        System.out.print("Odesilam = "+text+" o velikosti "+buffer.length+" \n");
        
        InetAddress adresa = lastReceived.getAddress();
        int port = lastReceived.getPort();
        
        DatagramPacket sendX = new DatagramPacket( buffer, buffer.length, adresa, port);
        ds.send( sendX );
    }
    
    public static void main( String[] args ) throws InterruptedException, IOException
    {
        BackServer server = new BackServer();
        server.start();
        Scanner sc = new Scanner(System.in);
        String text;
        while((text = sc.nextLine())!= "exit"){
            server.sendMessage("SRV:" + text);
        }
        server.cont = false;
        server.join();
        
        
    }
}
