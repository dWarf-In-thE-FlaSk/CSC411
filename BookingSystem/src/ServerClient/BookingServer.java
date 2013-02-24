/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

/**
 * 
 * This class will contain the main method that runs and listens for calls
 * 
 * I will contain functions:
 * 
 * 
 * @TODO - do we need to handle corrupted packets or splitting a message into 
 * several packets?
 */
public class BookingServer implements Observer{
        
    public static void main(String args[]) throws Exception { // Specify later 
        // Create a socket for UPD-port 8008
        DatagramSocket dgSocket = new DatagramSocket(8008);
        BookingData bookingData = new BookingData();
        ServerLog serverLog = new ServerLog();
        byte[] data = new byte[1024];
        DatagramPacket dgPacket = new DatagramPacket(data, data.length);

        while(true) {
            dgSocket.receive(dgPacket); // Throws IOException
            data = dgPacket.getData(); // This will be the array of bytes we need to unmarshal
            String receivedMessage = Marshaller.unmarshallMessage(data);
            String requestID = Marshaller.unmarshallRequestID(data);
            
            // If this request has already been processed once, get the response and resend it.
            String returnMessage = serverLog.responsForRequest(receivedMessage);
            
            // Else execute the operation and register the response.
            if (returnMessage == null) {
                returnMessage = doSomethingWithMessage(receivedMessage);
                serverLog.registerRequest(requestID, returnMessage);
            }
            
            // Then return the response.
            data = Marshaller.marshallResponse(returnMessage, requestID);
            dgPacket.setData(data);
            dgPacket.setAddress(dgPacket.getAddress());
            dgSocket.send(dgPacket); // Throws IOException
        }
    }
    
    // Placeholder class until we have some functions to call in bookingData
    public static String doSomethingWithMessage(String message) {
        switch (1) {
            case 1: return "confirmationID";
            default: return "Invalid command";
        }
                
    }

    @Override
    /**
     * @param action = index of action type
     * @param msg = message passed to server
     */
    public void handleEvent(String action, String msg) {
        
        //I do not use switch for only JRE 1.7 supports String
        if (action.equals("wrongName")) {
            
        }
        
        if (action.equals("overlap")) {
            
        }
        
        if (action.equals("register")) {
            
        }
        
        if (action.equals("update")) {
            
        }
        
        if (action.equals("wrongID")) {
            
        }
        
        if (action.equals("change")) {
            
        }
        
    }
    
    
}
