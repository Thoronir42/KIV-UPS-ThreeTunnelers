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
                System.out.format("Serverrr (%s:%d) ceka...%n", InetAddress.getLocalHost().getHostAddress(), ds.getLocalPort());
                
                String data = receiveMessage();
                System.out.println( ">> "+data);
                
                if(!data.contains(":")){
                    data = "NA:"+data;
                }
                sendMessage(data);
            }
        } catch (Exception e) {
            System.err.println("Receiving message failed: " + e.getLocalizedMessage());
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
        System.out.print(">>> "+text+" ("+buffer.length+") \n");
        
        InetAddress adresa = lastReceived.getAddress();
        int port = lastReceived.getPort();
        
        DatagramPacket sendX = new DatagramPacket( buffer, buffer.length, adresa, port);
        ds.send( sendX );
    }
    
    synchronized public void stopServer() throws InterruptedException{
        super.interrupt();
        this.cont = false;
        this.ds.close();
    }
    
    public static void main( String[] args )
    {
        try{
            BackServer server = new BackServer();
            server.start();
            Scanner sc = new Scanner(System.in);
            String text;
            while(!"exit".equals(text = sc.nextLine())){
                server.sendMessage("SRV:" + text);
            }
            System.out.println("Stopping server");
            server.stopServer();
        } catch (InterruptedException | IOException e){
            System.err.println(e.getMessage());
        }
        
    }
}
