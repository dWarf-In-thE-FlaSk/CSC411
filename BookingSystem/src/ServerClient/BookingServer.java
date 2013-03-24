/*
 * The BookingServer is the gateway to the rest of the server functionality and
 * acts as a controller for the rest of the fucntions.
 */
package ServerClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;

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
 * "days" - A list of days to check the availability for. On sending, in the RequestMessage this attribute should be formatted as a String of days with a comma as separator.
 * 
 * 4: Add facility
 * Attributes for the RequestMessage:
 * "facility" - the facility to be added.
 * 
 * 5: Cancel booking
 * "bookingID" - The bookingID to be cancelled
 * 
 * 6: Check all facilities
 * no input
 * 
 * 7: Register as an observer
 * "facility" - the facility to monitor
 * "interval" - the interval to monitor over
 * 
 * 8: Get available facilies
 * no input
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
        
        // Initializing the server with a few facilities.
        bookingData.addFacility("LTA");
        bookingData.addFacility("LTB");
        bookingData.addFacility("LTC");
        bookingData.addFacility("LTD");
        
        // A message is lost every [lossFrequency] message received
        int iterations = 0;
        int requestLossFreq = 3;
        int responseLossFreq = 6;

        while(true) {
            // We begin listening for requests, this will wait until data is received.
            dgSocket.receive(dgPacket); // Throws IOException
            
            // Check if we should simulate requst loss
            if((iterations % requestLossFreq) == 0) { // We don't simulate a request lost on the way to the server.
                data = dgPacket.getData();

                // Unmarshalling methods. See static Marshaller methods for reference
                Message receivedMessage = Marshaller.unmarshall(data);

                // The request ID of a message is the second element
                int requestID = receivedMessage.getRequestID();

                // If this request has already been processed once, get the response and resend it.
                Message returnMessage = serverLog.responsForRequest(requestID, dgPacket.getSocketAddress());

                // Else execute the operation and register the response.
                if (returnMessage == null) {
                    returnMessage = executeCommands(receivedMessage, bookingData, dgPacket.getSocketAddress());
                    serverLog.registerRequest(dgPacket.getSocketAddress(), requestID, returnMessage);
                }

                // Then return the response
                // First see if we should simulate a response loss.
                if (iterations % requestLossFreq == 0) {
                    // If not, send the response
                    data = Marshaller.marshall(returnMessage);
                    dgPacket.setData(data);
                    dgPacket.setAddress(dgPacket.getAddress());
                    dgSocket.send(dgPacket); // Throws IOException                    
                
                } else {
                    System.out.println("Simulating a lost response");
                }
            
            } else { // We simulate a request being lost on the way to the server.
                System.out.println("Simulating a lost request");
            }
            
            iterations++;
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
    public static Message executeCommands(Message message, BookingData bookingData, SocketAddress requester) {
        Message returnMessage = null;
        
        if(message.getMessageType() == 1) { // It's a requestMessage!
            RequestMessage reqMessage = (RequestMessage) message;
            try {
                switch (reqMessage.getRequest()) {
                    case 1: {
                        // Register
                        String facility = reqMessage.getAttribute("facility");
                        BookingDate startDate = new BookingDate(reqMessage.getAttribute("startDate"));
                        BookingDate endDate = new BookingDate(reqMessage.getAttribute("endDate"));                    
                        returnMessage = bookingData.registerBooking(facility, startDate, endDate);
                        if (returnMessageIsSuccessful(returnMessage)) {
                            notifyObservers(facility, bookingData);
                        }
                    }
                    case 2: {
                        // Change booking
                        String bookingID = reqMessage.getAttribute("bookingID");
                        String changeInterval = reqMessage.getAttribute("changeIndicator");
                        BookingDate changeDate = new BookingDate(reqMessage.getAttribute("changeDate")); // What is this attribute?
                        returnMessage = bookingData.changeBooking(bookingID, changeInterval, changeDate);
                        if (returnMessageIsSuccessful(returnMessage)) {
                            notifyObservers(bookingData.getFacilityByID(bookingID), bookingData); // Use a method in BookingData to get the facility based on bookingID
                        }
                    }
                    case 3: {
                        // Check availabillity
                        String facility = reqMessage.getAttribute("facility");
                        List<String> days = Arrays.asList(reqMessage.getAttribute("days").split(",")); 
                        returnMessage = bookingData.checkAvailability(facility, days);
                    }
                    case 4: {
                        // Register to observer
                        String facility = reqMessage.getAttribute("facility");
                        int interval = Integer.parseInt(reqMessage.getAttribute("interval"));
                        returnMessage = bookingData.addObserver(facility, requester, interval);
                    }
                    case 5: {
                        // Cancel booking - non-idempotent
                        String bookingID = reqMessage.getAttribute("bookingID");
                        returnMessage = bookingData.cancelBooking(bookingID);
                        if (returnMessageIsSuccessful(returnMessage)) {
                            notifyObservers(bookingData.getFacilityByID(bookingID), bookingData);
                        }
                    }
                    case 6: {
                        // Check all facilites - idempotent
                        returnMessage = bookingData.checkAll();
                    }
                    case 7: {
                        // Add facility
                        String facility = reqMessage.getAttribute("facility");
                        returnMessage = bookingData.addFacility(facility);
                    }
                    case 8: {
                        // Get the available facilities
                        returnMessage = bookingData.getFacilityList();
                    }
                    default: {
                        returnMessage = getServerExceptionMessage("Not a valid request type");
                    }
                }
            } catch (Exception e) {
                returnMessage = getServerExceptionMessage("An exception was thrown in the server: " + e.getMessage());
            }
        } else {
            returnMessage = getServerExceptionMessage("Server did not receive a correct request message");
        }

        returnMessage.setRequestID(message.getRequestID());
        return returnMessage;
    }
    
    private static void notifyObservers(String facility, BookingData bookingData) throws CloneNotSupportedException, IOException {
        
        // Get the list of observers for the facility being handled
        List<Observer> observers = bookingData.getObservers(facility);
        // Get the current availability for the facility
        Message observerMessage = bookingData.getAvailability(facility);
        // Set a requestID, it's irrelevant in this case so just put it to 0
        observerMessage.setRequestID(0);
        // Marshall the data being sent to the observer.
        byte[] data = Marshaller.marshall(observerMessage);
        
        // Make a DatagramSocket to send the packages through.
        DatagramSocket dgSocket = new DatagramSocket(8008);
        // Attach the data to the packet being sent.
        DatagramPacket dgPacket = new DatagramPacket(data, data.length); 
        
        for(Observer observer : observers) {
            dgPacket.setSocketAddress(observer.getaIPAddr());
            dgSocket.send(dgPacket);
        }
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
