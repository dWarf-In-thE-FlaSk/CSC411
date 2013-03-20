/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.util.List;

/**
 *
 * @author rikardandersson
 */
public class ResponseMessage implements Message {
    
    private static int messageType = 1;
    private int requestID;
    
    @Override
    public List<String> serializeMessageContent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMessageType() {
        return messageType;
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
}
