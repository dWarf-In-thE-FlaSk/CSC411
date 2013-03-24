/*
 */
package ServerClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * This type of Message is sent from the client to the server and contains a
 * mapping of all the attributes required for the server to execute the command.
 * The client have to append the correct attributes, correctly named when
 * constructing this message in order for the server to be able to construct a
 * correct ResponseMessage.
 * 
 * @author Rikard Andersson
 */
public class RequestMessage implements Message {

    private static int messageType = 1;
    private int requestID;
    private int request;
    private Map<String, String> attributes;

    @Override
    public List<String> serializeMessageContent() {
        List<String> serializedContent = new ArrayList<String>();
        serializedContent.add(String.valueOf(this.getMessageType()));
        serializedContent.add(String.valueOf(this.getRequestID()));
        serializedContent.add(String.valueOf(this.getRequest()));
        
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            serializedContent.add(entry.getKey());
            serializedContent.add(entry.getValue());
        }
        return serializedContent;
    }
    
    @Override
    public void unserializeAndSetMessageContent(List<String> serializedMessageContent) {
        this.setRequestID(Integer.parseInt(serializedMessageContent.get(1)));
        this.setRequest(Integer.parseInt(serializedMessageContent.get(2)));
        
        if (attributes == null) {
            attributes = new HashMap<String, String>();
        }
        
        Iterator<String> iter = serializedMessageContent.subList(3, serializedMessageContent.size()).iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            if(iter.hasNext()) {
                this.setAttribute(key, iter.next());
            }
        }
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
    
}
