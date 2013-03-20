/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rikardandersson
 */
public class ResponseMessage implements Message {
    
    private static int messageType = 2;
    private int requestID;
    private boolean requestSuccessful;
    private List<String> responseMessages;
    
    @Override
    public List<String> serializeMessageContent() {
        List<String> serializedContent = new ArrayList<String>();
        
        serializedContent.add(String.valueOf(this.getMessageType()));
        serializedContent.add(String.valueOf(this.getRequestID()));
        serializedContent.add(String.valueOf(requestSuccessful));
        serializedContent.addAll(responseMessages);
        
        return serializedContent;
    }

    @Override
    public void unserializeAndSetMessageContent(List<String> serializedMessageContent) {
        this.setRequestID(Integer.parseInt(serializedMessageContent.get(1)));
        this.setRequestSuccessful(Boolean.parseBoolean(serializedMessageContent.get(2)));
        this.setResponseMessages(serializedMessageContent.subList(3, serializedMessageContent.size()));
    }
    
    public boolean isRequestSuccessful() {
        return requestSuccessful;
    }

    public List<String> getResponseMessages() {
        return responseMessages;
    }

    @Override
    public int getMessageType() {
        return messageType;
    }

    @Override
    public int getRequestID() {
        return requestID;
    }

    public void setRequestSuccessful(boolean requestSuccessful) {
        this.requestSuccessful = requestSuccessful;
    }

    public void setResponseMessages(List<String> responseMessages) {
        this.responseMessages = responseMessages;
    }
    
    @Override
    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }    
    
    public void addResponseMessage(String message) {
        if (this.responseMessages == null) {
            responseMessages = new ArrayList<String>();
        }
        responseMessages.add(message);
    }
}
