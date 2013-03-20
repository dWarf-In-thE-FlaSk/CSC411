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
        Message returnMessage = null;
        
        if(message.getMessageType() == 1) { // It's a requestMessage!
            RequestMessage reqMessage = (RequestMessage) message;
            
            switch (reqMessage.getRequest()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    
                default:
                    returnMessage = getServerExceptionMessage("Not a valid request type");
            }
        }
        // Get the messageType and RequestID before starting to get commands
        int messageType = message.getMessageType();
        int requestID = message.getRequestID();
        returnMessage.setRequestID(requestID);
        
        
        returnMessage.setRequestID(message.getRequestID());
        return returnMessage;
    }
    
    private static void notifyObservers(ArrayList<SocketAddress> observers, Message message) {
        // Connect to the observers
    }
    
    private static ExceptionMessage getServerExceptionMessage(String message) {
        ExceptionMessage exMessage = new ExceptionMessage();
        exMessage.setExceptionMessage(message);
        exMessage.setExceptionType("serverException");
        return exMessage;
    }
}
