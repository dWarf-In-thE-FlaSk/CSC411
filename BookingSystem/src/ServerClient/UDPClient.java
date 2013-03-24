/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 *
 * @author
 */
public class UDPClient{
    
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
        int requestID=0;
        while(true){
            
            requestID++;
            
            ArrayList<String> message =new ArrayList<String>();
            Message rcvMessage=null;
            RequestMessage reqMessage=new RequestMessage();
            String responseString=new String(); 
            boolean timeout=true;
            Scanner input =new Scanner(System.in);
            
            
            input.useDelimiter(" |,|\\.");
            
            
           makeMessage(reqMessage,message,requestID);
            
            while(input.hasNext()){
                message.add(input.next());
            }
            
            
            
            DatagramSocket clientSocket=new DatagramSocket(); 
            byte[] sendBuffer= new byte[1024];
            byte[] rcvBuffer= new byte[1024];
            
            
            sendBuffer=Marshaller.marshall(reqMessage);
            //************************************************Start transmit/retransmit message 
            boolean finish=false;
            
            while (!finish){
            
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, server, port); 
            
                while (timeout){
                    clientSocket.send(sendPacket);
                    
                    DatagramPacket receivePacket=new DatagramPacket(rcvBuffer,rcvBuffer.length); 
                //*******************************************
            
              //Timmer and loop Here!!!
                    boolean received=false;
                    clientSocket.setSoTimeout(10000);  // in ms
            
                
                        try {
                            while(!received){
                                clientSocket.receive(receivePacket);
                                while (receivePacket.getLength()!=0){
                                    byte[] data = receivePacket.getData();
                                   
                                    // do the unmarshaller
                                    rcvMessage = Marshaller.unmarshall(data);
                                    
                            /*        if (rcvMessage.getMessageType()==-1){                       //Error message
                                        ExceptionMessage a=(ExceptionMessage)rcvMessage;
                                        responseString=a.getExceptionMessage();
                                    }
                                    if (rcvMessage.getMessageType()==2){
                                        ResponseMessage a=(ResponseMessage)rcvMessage;
                                        List<String> responseList=a.getResponseMessages();
                                        responseString=responseList.get(responseList.size()-1); //response String
                                        
                                    }*/
                                    received=true;
                                    timeout=false;
                                    finish=true;
                        //break the loop and print out the result!
                                }
                            }
                        }catch (SocketTimeoutException e) {         //when timeout print error messageand resend
                            System.out.println("Timeout reached!!! " + e);
                            System.out.println("Resend Message now");
                            timeout=true;
                            
                    
                    }
                        System.out.println("Response Message: ");
                        if (rcvMessage.getMessageType()==-1){                       //Error message
                               ExceptionMessage a=(ExceptionMessage)rcvMessage;
                               responseString=a.getExceptionMessage();
                               System.out.println(responseString);
                         }
                        else if (rcvMessage.getMessageType()==2){
                               ResponseMessage a=(ResponseMessage)rcvMessage;
                               List<String> responseList=a.getResponseMessages();
                               responseString=responseList.get(responseList.size()-1);
                               
                               if (a.isRequestSuccessful())
                                  System.out.println("Request Unsuccessful"+ responseString);
                               else 
                                  System.out.println("Request successful. Booking ID:"+responseString); 
                               
                                                   
                         }
            //*************************
            
            
                    }
            
                    
                    
            }
        
    }
 }
    public UDPClient(String server, int port, String message) throws Exception {
        this.server = InetAddress.getByName(server);
        this.port=port;
       
           
        
    }
    
    
    static void makeMessage (RequestMessage reqMessage,ArrayList<String> message, int requestID){
        
        reqMessage.setRequestID(requestID);
        reqMessage.setRequest(new Integer(message.get(0)));
        
        switch (reqMessage.getRequest()){
            
            case 1:{
                
                reqMessage.setAttribute("facility",message.get(1));
                reqMessage.setAttribute("startDate",message.get(2));
                reqMessage.setAttribute("endDate",message.get(3));
                
            }
            case 2:{
                reqMessage.setAttribute("bookingID",message.get(1));
                reqMessage.setAttribute("changeIndicator",message.get(2));
                reqMessage.setAttribute("changeDate",message.get(3));
            }
            case 3:{
                reqMessage.setAttribute("facility",message.get(1));
                reqMessage.setAttribute("startDate",message.get(2));
                reqMessage.setAttribute("endDate",message.get(3));
            }
            case 4:{
                reqMessage.setAttribute("facility",message.get(1));
                
            }
        }
        
        
    }
    

    }

