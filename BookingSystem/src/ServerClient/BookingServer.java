/*
 * The BookingServer is the gateway to the rest of the server functionality and
 * acts as a controller for the rest of the fucntions.
 */
package ServerClient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 
 * This class will contain the main method that runs and listens for calls
 * 
 * It will contain functions:
 * 
 * @TODO: Do we need to handle corrupted packets or splitting a message into 
 * several packets?
 * @TODO: When the marshalling and unmarshalling functions have been written, 
 * make corresponding changes in this class.
 * 
 * @author Rikard Andersson
 */
public class BookingServer {
        
    public static void main(String args[]) throws Exception { // Specify later 
        // Create a socket for UPD-port 8008
        DatagramSocket dgSocket = new DatagramSocket(8008);
        
        BookingData bookingData = new BookingData();
        
        ServerLog serverLog = new ServerLog();
        
        byte[] data = new byte[1024];
        
        DatagramPacket dgPacket = new DatagramPacket(data, data.length);
        
        bookingData.addFacility("LTA");
        bookingData.addFacility("LTB");
        bookingData.addFacility("LTC");
        bookingData.addFacility("LTD");
        

        while(true) {
            dgSocket.receive(dgPacket); // Throws IOException
            data = dgPacket.getData();
            
            // Unmarshalling methods. See static Marshaller methods for reference
            Message receivedMessage = Marshaller.unmarshall(data);
            
            // The request ID of a message is the second element
            int requestID = receivedMessage.getRequestID();
            
            // If this request has already been processed once, get the response and resend it.
            Message returnMessage = serverLog.responsForRequest(requestID, dgPacket.getSocketAddress());
            
            // Else execute the operation and register the response.
            if (returnMessage == null) {
                returnMessage = executeCommands(receivedMessage, bookingData);
                serverLog.registerRequest(dgPacket.getSocketAddress(), requestID, returnMessage);
            }
            
            // Then return the response.
            data = Marshaller.marshall(returnMessage);
            dgPacket.setData(data);
            dgPacket.setAddress(dgPacket.getAddress());
            dgSocket.send(dgPacket); // Throws IOException
        }
    }
    
    /**
     * Method that translates the calls made to the server into actual method
     * invocations and returns the feedback.
     * 
     * @param message The List<String> of messages which contains commands
     * to execute and attributes to use in the command calls.
     * @param bookingData The BookingData object that exists in the main method
     * needs to be sent by reference since this method can't reach it otherwise
     * @return An List<String> with the results to return to the client
     */
    public static Message executeCommands(Message message, BookingData bookingData) {
        Message returnMessage = new ResponseMessage();
        
            // Get the messageType and RequestID before starting to get commands
        int messageType = message.getMessageType();
        int requestID = message.getRequestID();
        returnMessage.setRequestID(requestID);
        
        return returnMessage;
    }
    
    private static void notifyObservers(ArrayList<SocketAddress> observers, Message message) {
        // Connect to the observers
    }

    // I think this should be done at the client. Since there's no permanent
    // connection between client and server it makes more sense to ask the user
    // to give instructions which are then translated by the client to match the
    // interface between client and server.
    public byte[] Start(ArrayList pFacility) {
        String lFacility = "";
		
        for (int i = 0; i < pFacility.size(); i++) {
            lFacility = lFacility + pFacility.get(i) +" ";
	}
        
        String startMsg = "Welcome to Booking System!\n" +
                "facility list: " + lFacility + '\n'+
                "please select the following three options:(by index)\n" +
                "1. Make a new booking.\n(Indicate facility name, start and end date)\n" +
                "2. Change a booking.\n(Indicate confirmation ID, advance/postpone and offset)\n" +
                "3. Check avaiablity of a facility.\n(Indicate facility name)\n" +
                "4. Monitor a facility\n(Indicate facility name and interval)\n" +
                "5. ";
        
        return startMsg.getBytes();
    }
    
    
    public void handleDataMsg(DataMsg pDataMsg) {
        String action = pDataMsg.getCommand();
        
        String msg = pDataMsg.getMsg();
        
        //I do not use switch for only JRE 1.7 supports String
        if (action.equals("wrongName")) {
            
        }
        
        if (action.equals("overlap")) {
            
        }
        
        if (action.equals("register")) {
            
        }
        
        if (action.equals("wrongID")) {
            
        }
        
        if (action.equals("change")) {
            
        }
        
    }
    
    
}
