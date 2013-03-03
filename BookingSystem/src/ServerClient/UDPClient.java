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
    private static String message; //message cannot be a static field
    
    
    public static void main(String args[]) throws Exception {
        //we should have a while loop to ask user to input
        
        /* refer to the following code for taking instruction from user
        
        ArrayList message = new ArrayList();
        Scanner scan = new Scanner(System.in); 
        scan.useDelimiter(" |,|\\."); 
	while (scan.hasNext()) { 
            meaasge.add(scan.next());
	}
        
        * then pass this message to marshaller
        */
        
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
