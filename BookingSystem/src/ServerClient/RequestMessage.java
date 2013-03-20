/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rikardandersson
 */
public class RequestMessage implements Message {

    private static int messageType = 1;
    private int requestID;
    private String request;
    private Map<String, String> attributes;
    
    @Override
    public List<String> serializeMessageContent() {
        List<String> serializedContent = new ArrayList<String>();
        serializedContent.add(String.valueOf(this.getMessageType()));
        serializedContent.add(String.valueOf(this.getRequestID()));
        
        
        return serializedContent;
    }

    @Override
    public int getMessageType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRequestID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void setRequestID(int requestID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unserializeAndSetMessageContent(List<String> serializedMessageContent) {
        throw new UnsupportedOperationException("Not supported yet.");
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
