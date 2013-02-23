/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.io.IOException;
import java.net.*;

/**
 *
 * @author
 */
public class UDPClient {
    
    private static int port;
    private static InetAddress server;
    private static String message;
    public static void main(String args[]) throws Exception {
     
        DatagramSocket clientSocket=new DatagramSocket(); 
        byte[] sendBuffer= new byte[512];
        byte[] rcvBuffer= new byte[512];
        sendBuffer=message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, server, port); 
        sendPkt(clientSocket,sendPacket);
        DatagramPacket receivePacket=new DatagramPacket(rcvBuffer,rcvBuffer.length); 
        message=receivePkt(clientSocket,receivePacket); 
        
    }
    public UDPClient(String server, int port, String message) throws Exception {
        this.server = InetAddress.getByName(server);
        this.port=port;
        this.message=message;
           
        
    }
    
    public static void sendPkt (DatagramSocket Socket, DatagramPacket packet ) throws IOException{
        
        Socket.send(packet);
        
    }// We need some code here
    
     static String receivePkt (DatagramSocket Socket, DatagramPacket packet) throws IOException{
         
        Socket.receive(packet);
        String rcvMessage=new String(packet.getData()); 
         
        return(rcvMessage);
    }
}
