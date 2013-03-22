/*
 * The BookingServer is the gateway to the rest of the server functionality and
 * acts as a controller for the rest of the fucntions.
 */
package ServerClient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 
 * This class will contain the main method that runs and listens for calls
 * 
 * 1: Register request
 * Attributes for the RequestMessage: 
 * "facility" - the facility to register for
 * "startDate" - start date 
 * "endDate" - end date
 * 
 * 2: Change booking
 * Attributes for the RequestMessage:
 * "bookingID" - The ID of the booking as returned by the server when the booking was made.
 * "changeIndicator" - if it's a postponement this should start with a "p", if it's an advancement it should start with an "a"
 * "bookingDate" - The data to change the booking to.
 * 
 * 3: Check availability
 * Attributes for the RequestMessage:
 * "facility" - the facility name
 * "startDate" - The start date to check from.
 * "endDate" - The end date to check until.
 * 
 * 4: Add facility
 * Attributes for the RequestMessage:
 * "facility" - the facility to be added.
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
     * @param message The Message which contains commands to execute and 
     * attributes to use in the command calls.
     * @param bookingData The BookingData object that exists in the main method
     * needs to be sent by reference since this method can't reach it otherwise
     * @return An List<String> with the results to return to the client
     */
    public static Message executeCommands(Message message, BookingData bookingData) {
        Message returnMessage = null;
        
        if(message.getMessageType() == 1) { // It's a requestMessage!
            RequestMessage reqMessage = (RequestMessage) message;
            
            switch (reqMessage.getRequest()) {
                case 1: {
                    // Register
                    String facility = reqMessage.getAttribute("facility");
                    BookingDate startDate = new BookingDate(reqMessage.getAttribute("startDate"));
                    BookingDate endDate = new BookingDate(reqMessage.getAttribute("endDate"));                    
                    returnMessage = bookingData.registerBooking(facility, startDate, endDate);
                    if (returnMessageIsSuccessful(returnMessage)) {
                        notifyObservers(facility);
                    }
                }
                case 2: {
                    // Change booking
                    String bookingID = reqMessage.getAttribute("bookingID");
                    String changeInterval = reqMessage.getAttribute("changeIndicator");
                    BookingDate changeDate = new BookingDate(reqMessage.getAttribute("changeDate")); // What is this attribute?
                    returnMessage = bookingData.changeBooking(bookingID, changeInterval, changeDate);
                    if (returnMessageIsSuccessful(returnMessage)) {
                        notifyObservers(null); // Use a method in BookingData to get the facility based on bookingID
                    }
                }
                case 3: {
                    // Check availabillity
                    String facility = reqMessage.getAttribute("facility");
                    BookingDate startDate = new BookingDate(reqMessage.getAttribute("startDate"));
                    BookingDate endDate = new BookingDate(reqMessage.getAttribute("endDate"));
                    returnMessage = bookingData.checkAvaibility(facility, startDate, endDate);
                }
                case 4: {
                    // Add facility
                    String facility = reqMessage.getAttribute("facility");
                    returnMessage = bookingData.addFacility(facility);
                }
                case 5: {
                    // Something else 1
                }
                case 6: {
                    // Something else 2
                }
                case 7: {
                    // Register to observer
                }
                default: {
                    returnMessage = getServerExceptionMessage("Not a valid request type");
                }
            }
        } else {
            returnMessage = getServerExceptionMessage("Server did not receive a correct request message");
        }

        returnMessage.setRequestID(message.getRequestID());
        return returnMessage;
    }
    
    private static void notifyObservers(String facility) {
        // Connect to the observers
    }
    
    private static boolean returnMessageIsSuccessful(Message returnMessage) {
        if(returnMessage.getMessageType() == 2) {
            ResponseMessage respMessage = (ResponseMessage) returnMessage;
            return respMessage.isRequestSuccessful();
        } else {
            return false;
        }
    }
    
    private static ExceptionMessage getServerExceptionMessage(String message) {
        ExceptionMessage exMessage = new ExceptionMessage();
        exMessage.setExceptionMessage(message);
        exMessage.setExceptionType("serverException");
        return exMessage;
    }
}
