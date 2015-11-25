package generic;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class BackServer extends Thread
{
    private static final int BUFFER_SIZE = 1024;
    
    DatagramSocket ds;
    private DatagramPacket lastReceived;
    
    @Override
    public void run(){
        try {
            ds = new DatagramSocket( tunnelers.Settings.DEFAULT_PORT );
            
            while (true) {
                System.out.format("Serverrr (%s:%d) ceka...%n", InetAddress.getLocalHost().getHostAddress(), ds.getLocalPort());
                
                String data = receiveMessage();
                System.out.println( ">> "+data);
                
                String reply = processData(data);
                
                sendMessage(reply);
            }
        } catch (Exception e) {
            System.err.println("Receiving message failed: " + e.getLocalizedMessage());
        }
    }
    
    private String processData(String input){
		System.out.format(">> Proccessing message '%s'...", input);
        String[] segs = input.trim().split("|");
        return replyFor(segs[0], segs[1]);
        
    }
    private String replyFor(String group, String type){
        
        switch(type){
            default:
                System.out.println(" it's not recognised.");
                return "C|WAT|"+group+"|"+type;
            case "UTHERE":
                System.out.println(" it's a handshake request.");
                return "C|COME";
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