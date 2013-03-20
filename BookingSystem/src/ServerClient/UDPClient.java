/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author
 */
public class UDPClient {
    
    private static int port;
    private static InetAddress server;
    //private static String message; //message cannot be a static field
    
    
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
        while(true){
            
            ArrayList message =new ArrayList();
            List rcvMessage =new ArrayList();
            
            Scanner input =new Scanner(System.in);
            input.useDelimiter("|,\\.");
            
            while(input.hasNext()){
                message.add(input.next());
            }
            
            DatagramSocket clientSocket=new DatagramSocket(); 
            byte[] sendBuffer= new byte[1024];
            byte[] rcvBuffer= new byte[1024];
            
            
            sendBuffer=Marshaller.marshall(message);
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, server, port); 
            
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket=new DatagramPacket(rcvBuffer,rcvBuffer.length); 
            //*******************************************
            
            //Timmer and loop Here!!!
            
            clientSocket.receive(receivePacket);
            while (receivePacket.getLength()!=0){
                byte[] data = receivePacket.getData();
                rcvMessage = Marshaller.unmarshall(data);
                //break the loop and print out the result!
            }
            //*******************************************
            
          
        }
        
    }
    public UDPClient(String server, int port, String message) throws Exception {
        this.server = InetAddress.getByName(server);
        this.port=port;
       
           
        
    }
    
    /*public static void sendPkt (DatagramSocket Socket, DatagramPacket packet ) throws IOException{
        
        Socket.send(packet);
        
    }// We need some code here
    
     static List<String> receivePkt (DatagramSocket Socket, DatagramPacket packet) throws IOException{
         
        Socket.receive(packet);
        
        while (packet.getLength()!=0){
        byte[] data = packet.getData();
        List<String> rcvMessage = Marshaller.unmarshall(data);
         
        return(rcvMessage);
       }
        */
    }
}
