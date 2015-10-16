package generic;

import java.net.*;
import java.io.*;

//	-Djava.net.preferIPv4Stack=true
public abstract class BackServer
{
    public static void main( String[] args )
    {
        int bufferSize = 60; 
        DatagramSocket ds;
        try
        {
            ds = new DatagramSocket( tunnelers.structure.Settings.DEFAULT_PORT );
            while (true)
            {
                byte[] buffer = new byte[bufferSize];
                System.out.print( "Server ceka...\n");
                DatagramPacket recv = new DatagramPacket(buffer, buffer.length);
                ds.receive(recv);
                
                InetAddress adresa = recv.getAddress();
                int port = recv.getPort();
                
                System.out.format("Pripojil se klient z %s:%d(%s)%n", adresa.getHostAddress(), port, adresa.getHostName());
                
                String data = new String( buffer );
                System.out.print( "Prijal sem data - "+data+"\n");
                data = "SRV:"+data;
                buffer = data.getBytes();
                
                
                System.out.print("Odesilam = "+data+" o velikosti "+buffer.length+" \n");
                DatagramPacket sendX = new DatagramPacket( buffer, buffer.length, adresa, port);
                ds.send( sendX );
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
