/*
 */
package ServerClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This type of Message is sent from the client to the server and contains a
 * mapping of all the attributes required for the server to execute the command.
 * The client have to append the correct attributes, correctly named when
 * constructing this message in order for the server to be able to construct a
 * correct ResponseMessage.
 * 
 * @see Message
 * 
 * @author Rikard Andersson
 */
public class RequestMessage implements Message {

    private static int messageType = 1;
    private int requestID;
    private int request;
    private boolean usesServerLog;
    private Map<String, String> attributes;  

    public boolean isUsesServerLog() {
        return usesServerLog;
    }

    public void setUsesServerLog(boolean usesServerLog) {
        this.usesServerLog = usesServerLog;
    }
    
    public void setRequest(int request) {
        this.request = request;
    }

    public int getRequest() {
        return this.request;
    }

    @Override
    public int getMessageType() {
        return this.messageType;
    }

    @Override
    public int getRequestID() {
        return this.requestID;
    }
    
    @Override
    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }
    
    public String getAttribute(String key) {
        return attributes.get(key);
    }
    
    public void setAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new HashMap<String, String>();
        }
        attributes.put(key, value);
    }
    
    private Map<String, String> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<String, String>();
        }
        return attributes;
    }
    
    /**
     * This serialized part of the message will be structured in this order.
     * 1. request       -   the int indicating which request is to be carried out
     * 2. usesServerLog -   A boolean that decides whether or not the serverlog
     *                      (and hence the at most once invocation) will be used
     *                      when executing the request on serverside.
     * 3. attributes    -   A map of all the attributes for the command. Must be
     *                      input with the correct key in order for the server
     *                      to be able to fetch them when executing the request.
     *
     * @return A list with the flattened version of the part of the message that
     * is unique to this implementation of Message. The parts generic to all
     * Messages (i.e. messageType and requestID) is handeled by the Marshaller.
     */
    @Override
    public List<String> serializeMessageContent() {
        System.out.println("Serializing request message");
        List<String> serializedContent = new ArrayList<String>();
        
        serializedContent.add(String.valueOf(this.getRequest()));
        serializedContent.add(String.valueOf(this.isUsesServerLog()));
                
        for (Map.Entry<String, String> entry : getAttributes().entrySet()) {
            System.out.println("In requestMessage - serializing key: " + entry.getKey() + " with value: " + entry.getValue());
            serializedContent.add(entry.getKey());
            serializedContent.add(entry.getValue());
        }
        
        System.out.println("Serialized message: " + serializedContent.toString());
        return serializedContent;
    }
    
    /**
     * Does the opposite of the above method. The method builds the object from
     * a flattened version of it. This flattened version is represented as a 
     * list of Strings.
     * 
     * @param serializedMessageContent 
     */
    @Override
    public void unserializeAndSetMessageContent(List<String> serializedMessageContent) {
        
        System.out.print("RequestMessage got a serialized object to unserialize: " + serializedMessageContent.toString());
        
        this.setRequest(Integer.parseInt(serializedMessageContent.get(0)));
        this.setUsesServerLog(Boolean.parseBoolean(serializedMessageContent.get(1)));
                
        if (attributes == null) {
            attributes = new HashMap<String, String>();
        }
        
        Iterator<String> iter = serializedMessageContent.subList(2, serializedMessageContent.size()).iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            if(iter.hasNext()) {
                String value = iter.next();
                this.setAttribute(key, value);
                System.out.println("In requestMessage - found key: " + key + " with value: " + value);
            }
        }
    } 
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            // If the object is null they should not be considered equal
            return false;
        } else if (!(o instanceof RequestMessage)) {
            // If the object is not of the same class as this object then
            // it's not equal and we cannot continue the operations in else.
            return false;
        } else {
            // Type casting the object since we know it's the same type
            RequestMessage rm = (RequestMessage) o;
            // Se if the containers in the ResponseMessages matches. If the do
            // they are the same.
            return ((this.requestID == rm.getRequestID()) &&
                    (this.request == rm.getRequest()) &&
                    (this.usesServerLog = rm.isUsesServerLog()) &&
                    this.attributes.equals(rm.attributes));
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 1;

        hash += (this.requestID + 1) * 89;
        hash += (this.request + 3) * 13;
        
        if (this.usesServerLog) {
            hash += 13;
        }
        
        if (this.attributes != null) {
            hash += this.attributes.hashCode() * 89;
        }
        return hash;
    }
}
