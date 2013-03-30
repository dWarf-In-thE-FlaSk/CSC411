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
 * "bookingDate" - The outData to change the booking to.
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
        int port = 8008;
        System.out.println("Adding a socket to port " + port);
        DatagramSocket dgSocket = new DatagramSocket(port);
        
        // Creating a new representation of the booking reservation system
        System.out.println("Creating a new representation of the booking reservation system");
        BookingData bookingData = new BookingData();
        
        // Creating a serverlog
        System.out.println("Creating a new server log");
        ServerLog serverLog = new ServerLog();
      
        // Initializing the server with a few facilities.
        System.out.println("Creating new facilities in the reservation system");
        bookingData.addFacility("LTA");
        bookingData.addFacility("LTB");
        bookingData.addFacility("LTC");
        bookingData.addFacility("LTD");
        
        // A message is lost every [lossFrequency] message received
        int iterations = 0;
        int requestLossFreq = 7;
        int responseLossFreq = 11;
        
        while(true) {
            
            byte[] outData = new byte[2048];
            byte[] inData = new byte[2048];
            DatagramPacket outPacket = new DatagramPacket(outData, outData.length);
            DatagramPacket inPacket = new DatagramPacket(inData, inData.length);
            
            // For the message loss
            iterations++;
            System.out.println("On iteration: " + iterations);
           
            // We begin listening for requests, this will wait until outData is received.
            System.out.println("Listening for incoming requests");
            dgSocket.receive(inPacket);
            
            // Check if we should simulate requst loss
            if(iterations % requestLossFreq != 0) { // We don't simulate a request lost on the way to the server.
                inData = inPacket.getData();
                
                // Printing out what we have received.
                System.out.println("Received data: " + new String(inData, "UTF-8"));
                
                // SocketAddress of the packet will be used a lot so for readability:
                SocketAddress senderAddress = inPacket.getSocketAddress();

                // Unmarshalling methods. See static Marshaller methods for reference
                Message receivedMessage = Marshaller.unmarshall(inData);

                // The request ID of a message is the second element
                int requestID = receivedMessage.getRequestID();
                
                Message returnMessage = null;
                // Check if we have received a RequestMessage.
                // If we have, proceed to get the result for the message.
                if (receivedMessage.getMessageType() == 1) {
                    
                    // Now it's safe to cast the message to a RequestMessage
                    RequestMessage reqMessage = (RequestMessage) receivedMessage;
                    
                    // Check if the client want us to use the serverlog
                    if (reqMessage.isUsesServerLog()) {
                        
                        // If this request has already been processed once, get the response and resend it.
                        System.out.println("Using serverlog");
                        returnMessage = serverLog.responsForRequest(requestID, senderAddress);
                        
                        // Else execute the operation and register the response.
                        if (returnMessage == null) {
                            
                            // Executing the request.
                            System.out.println("Did not find that the request had been sent before");
                            returnMessage = executeCommands(reqMessage, bookingData, senderAddress);
                            
                            // Making sure the request is not a "register as observer call".
                            // In this case null is returned by the "executeCommands" call.
                            if (returnMessage != null) {
                                // We register the new response in the serverlog.
                                serverLog.registerRequest(senderAddress, requestID, returnMessage);
                                System.out.println("Registering request: " + requestID + 
                                    " for " + senderAddress.toString());
                            }
                        
                        } else {
                            System.out.println("Request found to be duplicate of previous request. Returning the logged response.");                            
                        }
                        
                    } else { // If the client does not want us to use serverlog.
                        // We just execute the command.
                        System.out.println("NOT using serverlog");
                        returnMessage = executeCommands(reqMessage, bookingData, senderAddress);
                    }
                } else { // If the received Message is not a RequestMessage.
                    // Return an exception cause the message is not a RequestMessage
                    returnMessage = getServerExceptionMessage("Server did not receive a RequestMessage");
                }
                
                // Now we want to return the response to wherever it came from.
                // First see if we should simulate a response loss.
                // We also check that the returnMessage is not null (happens if the request was "register as observer")
                if (iterations % responseLossFreq != 0 && returnMessage != null) {
                    // If not, send the response
                    outData = Marshaller.marshall(returnMessage);
                    System.out.println("Return message as array of byte: " + new String(outData, "UTF-8"));
                    outPacket.setData(outData);
                    outPacket.setLength(outData.length);
                    outPacket.setSocketAddress(senderAddress);
                    dgSocket.send(outPacket); // Throws IOException                    
                    System.out.println("Returning message");
                } else {
                    if (returnMessage == null) {
                        System.out.println("The request was to register as observer, no return message.");
                    }
                    if (iterations % responseLossFreq == 0) {
                        System.out.println("Simulating a lost response.");
                    }
                }
            
            } else { // We simulate a request being lost on the way to the server.
                System.out.println("Simulating a lost request");
            }
        }
    }
    
    /**
     * Method that translates the calls made to the server into actual method
     * invocations and returns the feedback.
     * 
     * @param requestMessage The RequestMessage which contains commands to execute and 
     * attributes to use in the command calls.
     * @param bookingData The BookingData object that exists in the main method
     * needs to be sent by reference since this method can't reach it otherwise
     * @return An List<String> with the results to return to the client
     */
    public static Message executeCommands(RequestMessage requestMessage, BookingData bookingData, SocketAddress requester) {
        Message returnMessage = null;
        
        try {
            switch (requestMessage.getRequest()) {
                case 1: {
                    // Register
                    String facility = requestMessage.getAttribute("facility");
                    BookingDate startDate = new BookingDate(requestMessage.getAttribute("startDate"));
                    BookingDate endDate = new BookingDate(requestMessage.getAttribute("endDate"));
                    System.out.println("Registering a booking for facility " + facility +
                            " from " + startDate.getDate() +
                            " to " + endDate.getDate());
                    returnMessage = bookingData.registerBooking(facility, startDate, endDate);

                    // If something was changed for the facility as a result of the request, send a notification to the observers.
                    if (returnMessageIsSuccessful(returnMessage)) {
                        notifyObservers(facility, bookingData);
                    }
                    break;
                }
                case 2: {
                    // Change booking
                    String bookingID = requestMessage.getAttribute("bookingID");
                    String changeIndicator = requestMessage.getAttribute("changeIndicator");
                    int hours = Integer.parseInt(requestMessage.getAttribute("hours")); // What is this attribute?
                    System.out.println("Changing booking with ID " + bookingID +
                            " with indicator " + changeIndicator +
                            " with number of hours " + hours);
                    returnMessage = bookingData.changeBooking(bookingID, changeIndicator, hours);

                    // If the change was successfull we want to notify the observers.
                    if (returnMessageIsSuccessful(returnMessage)) {
                        notifyObservers(bookingData.getFacilityByID(bookingID), bookingData); // Use a method in BookingData to get the facility based on bookingID
                    }
                    break;
                }
                case 3: {
                    // Check availabillity
                    String facility = requestMessage.getAttribute("facility");
                    List<String> days = Arrays.asList(requestMessage.getAttribute("days").split(","));
                    System.out.println("Checking availability for facility " + facility +
                    " for the days: " + days.toString());
                    returnMessage = bookingData.checkAvailability(facility, days);
                    break;
                }
                case 4: {
                    // Register to observer
                    String facility = requestMessage.getAttribute("facility");
                    int interval = Integer.parseInt(requestMessage.getAttribute("interval"));
                    System.out.println("Registering an observer for facility " +  facility);
                    returnMessage = bookingData.addObserver(facility, requester, interval);

                    if (returnMessageIsSuccessful(returnMessage)) {
                        System.out.println("Successful in registering observer");
                    }
                    // Setting the returnMessage to null to indicate that it should not be returned.
                    returnMessage = null;
                    break;
                }
                case 5: {
                    // Cancel booking - non-idempotent
                    String bookingID = requestMessage.getAttribute("bookingID");
                    System.out.println("Canceling booking with ID " + bookingID);
                    returnMessage = bookingData.cancelBooking(bookingID);
                    if (returnMessageIsSuccessful(returnMessage)) {
                        notifyObservers(bookingData.getFacilityByID(bookingID), bookingData);
                    }
                    break;
                }
                case 6: {
                    // Check all facilites - idempotent
                    System.out.println("Checking all the facilities for bookings");
                    returnMessage = bookingData.checkAll();
                    break;
                }
                case 7: {
                    // Add facility
                    String facility = requestMessage.getAttribute("facility");
                    System.out.println("Adding a facility with name " + facility);
                    returnMessage = bookingData.addFacility(facility);
                    break;
                }
                case 8: {
                    // Get the available facilities
                    System.out.println("Getting the list of facilities.");
                    returnMessage = bookingData.getFacilityList();
                    break;
                }
                default: {
                    returnMessage = getServerExceptionMessage("Not a valid request type");
                    break;
                }
            }
            
        } catch (CloneNotSupportedException e) { // If BookingData throws a CloneExceptionMessage to the server, we want to know.
            returnMessage = getServerExceptionMessage("A CloneExceptionMessage was thrown to server by BookingData");
        
        } catch (Exception e) { // If an exception is caught we print the problem and return a message to the client.
            returnMessage = getServerExceptionMessage("An exception was thrown in the server: " + e.getMessage());
        
        } finally { // No matter what happens we want to return the returnMessage.
            
            if (returnMessageIsSuccessful(returnMessage)) {
                System.out.println("The request was successful!");
            } else {
                System.out.println("The request appears to be unsuccessful!");
            }
            
            if (returnMessage != null) {
                returnMessage.setRequestID(requestMessage.getRequestID());
            }
            
            return returnMessage;
        }

    }
    
    /**
     * 
     * @param facility - The facility for which to notify the observers
     * @param bookingData - The bookingData to be operated upon. This needs to
     * be passed since the BookingServer class is static.
     * @throws CloneNotSupportedException
     * @throws IOException 
     */
    private static void notifyObservers(String facility, BookingData bookingData) throws IOException {
        System.out.println("Notifying observers for facility " + facility);
        // Get the list of observers for the facility being handled
        List<Observer> observers = bookingData.getObservers(facility);
        // Get the current availability for the facility
        Message observerMessage;
        // Catch the error if something went wrong in generating the message to the observer. 
        try {
            observerMessage = bookingData.getAvailability(facility);
        } catch (CloneNotSupportedException e) {
            observerMessage = getServerExceptionMessage("Something happened to the facility you are monitorin but an error was generated while trying to notify you.");
        }
        // Set a requestID, it's irrelevant in this case so just put it to 0
        observerMessage.setRequestID(0);
        // Marshall the outData being sent to the observer.
        byte[] data = Marshaller.marshall(observerMessage);
        
        // Make a DatagramSocket to send the packages through. (Can't have 8008 since it's still open in the main() function)
        DatagramSocket dgSocket = new DatagramSocket(8009);
        // Attach the outData to the packet being sent.
        DatagramPacket dgPacket = new DatagramPacket(data, data.length); 
        
        for(Observer observer : observers) {
            dgPacket.setSocketAddress(observer.getaIPAddr());
            dgSocket.send(dgPacket);
        }
        // Have to close the socket to be able to use the same port later.
        dgSocket.close();
    }
    
    /**
     * Check a Message to see if a request generated a successful ResponseMessage
     */
    private static boolean returnMessageIsSuccessful(Message returnMessage) {
        if (returnMessage == null) {
            return false;
        }
        if(returnMessage.getMessageType() == 2) {
            ResponseMessage respMessage = (ResponseMessage) returnMessage;
            return respMessage.isRequestSuccessful();
        } else {
            return false;
        }
    }
    /**
     * Private method to get a standard "serverException" ExceptionMessage
     */
    private static ExceptionMessage getServerExceptionMessage(String message) {
        System.out.println("Returning a server exception with message:");
        System.out.println(message);
        ExceptionMessage exMessage = new ExceptionMessage();
        exMessage.setExceptionMessage(message);
        exMessage.setExceptionType("serverException");
        return exMessage;
    }
}
