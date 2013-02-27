/*
 * The BookingServer is the gateway to the rest of the server functionality and
 * acts as a controller for the rest of the fucntions.
 */
package ServerClient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
        
        ArrayList<String> aList = new ArrayList<String>();
        aList.add("LTA");
        aList.add("LTB");
        aList.add("LTC");
        aList.add("LTD");
        
        bookingData.setFacilityList(aList); // Maybe we should just have a function in BookingData : addFacility(String name)

        while(true) {
            dgSocket.receive(dgPacket); // Throws IOException
            data = dgPacket.getData();
            
            // Unmarshalling methods. See static Marshaller methods for reference
            List<String> receivedMessages = Marshaller.unmarshall(data);
            
            // The request ID of a message is the second element
            String requestID = receivedMessages.get(1);
            
            // If this request has already been processed once, get the response and resend it.
            List<String> returnMessages = serverLog.responsForRequest(requestID, dgPacket.getSocketAddress());
            
            // Else execute the operation and register the response.
            if (returnMessages == null) {
                returnMessages = executeCommands(receivedMessages, bookingData);
                serverLog.registerRequest(dgPacket.getSocketAddress(), requestID, returnMessages);
            }
            
            // Then return the response.
            data = Marshaller.marshall(returnMessages);
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
    public static List<String> executeCommands(List<String> message, BookingData bookingData) {
        List<String> returnMessage = new ArrayList<String>();
        Iterator<String> iterator = message.iterator();
        
        try {
            // Get the messageType and RequestID before starting to get commands
            String messageType = iterator.next();
            String requestID = iterator.next();
            returnMessage.add("1");
            returnMessage.add(requestID);
            
            while (iterator.hasNext()) {
                int command = Integer.parseInt(iterator.next());
                switch(command) {
                    case 1:
                        // New booking                        
                        break;
                    case 2:
                        // Change booking
                        break;
                    case 3:
                        // Check availability of booking
                        break;
                    case 4:
                        // Monitor a facility
                        break;
                }
            }
            
        // This can happen if a String being parsed expected to contain a 
        // command is not an int. 
        } catch (NumberFormatException e) {
            returnMessage.add("error_message");
            returnMessage.add("No such command");
            returnMessage.add(e.getMessage());
        // This can happen for example if the user does not supply enough 
        // attributes for a command.
        } catch (NoSuchElementException e) {
            returnMessage.add("error_message");
            returnMessage.add("Wrong number of arguments");
            returnMessage.add(e.getMessage());
        } finally {
            return returnMessage;
        }
    }

    
    
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
        String action = pDataMsg.getAction();
        
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
