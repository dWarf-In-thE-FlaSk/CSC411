/*
 */
package ServerClient;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A class of static methods marshalling and unmarshalling calls from and to a
 * client communicating with the BookingServer.
 * 
 * The structure of a message have to be formatted in the following way:
 * 1. Message type:
 *      1 = request
 *      2 = response
 *     -1 = exception
 * 2. Request ID:
 *      Generated by the client and unique for every client but not globally
 * 3. Message:
 *      List:
 *          Key
 *          Value
 * 
 * @author Rikard Andersson
 */
public class Marshaller {
    
    /**
     * Takes an array of commands and their attributes and flattens it to an
     * array of bytes which can be sent as the data element in a DatagramPacket.
     * 
     * @param messageData An array of all the messages and attributes to send to
     * marshall (and later send to the client)
     * @return An array of bytes to send as the data in a DatagramPacket
     */
    public static byte[] marshall(Message messageData) {
        // Using StringBuilder as a more efficient way of building strings as
        // opposed to the operator += or StringBuffer
        StringBuilder marshalledStr = new StringBuilder();
        
        // First two things in the message is the message type and the requestID
        marshalledStr.append(String.valueOf(messageData.getMessageType()) + "/");
        marshalledStr.append(String.valueOf(messageData.getRequestID())+ "/");
        
        Iterator<String> listIterator = messageData.serializeMessageContent().iterator();
        
        while(listIterator.hasNext()) {
            
            marshalledStr.append(listIterator.next());
            
            // Appending the delimiter "/" between each object
            if(listIterator.hasNext()) {
                marshalledStr.append("/");
            }
        }
        return marshalledStr.toString().getBytes();
    }
    
    /**
     * 
     * @param data array of bytes as extracted from a DatagramPacket and
     * marshalled via a method call from the function above.
     * @return List of Strings formatted as in class-header
     */
    public static Message unmarshall(byte[] data) {
        String marshalledStr = data.toString();

        List<String> unMarshalledList = new ArrayList(Arrays.asList(marshalledStr.split("/")));
        
        int messageType = Integer.parseInt(unMarshalledList.get(0)); // NumberFormatException uncaught
        int requestID = Integer.parseInt(unMarshalledList.get(1)); // NumberFormatException uncaught
        
        
        Message unMarshalledMessage = initMessageWithType(messageType);
        unMarshalledMessage.setRequestID(requestID);
        // 
        unMarshalledMessage.unserializeAndSetMessageContent(unMarshalledList.subList(2, unMarshalledList.size()));
        
        return unMarshalledMessage;
    }
    
    private static Message initMessageWithType(int type) {
        switch(type) {
            case 1:
                return new RequestMessage();
            case 2:
                return new ResponseMessage();
            case -1:
                return new ExceptionMessage();
            default:
                return new ExceptionMessage();
        }
                
    } 
}
