/*
 */
package ServerClient;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the message returned from the server to the client after a
 * RequestMessage is sent the opposite direction. That is unless an exception is
 * thrown at the server side in which case an ExceptionMessage will be passed
 * instead.
 * 
 * @author Rikard Andersson
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
        serializedContent.addAll(this.getResponseMessages());
        
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
        if (responseMessages == null) {
            this.setResponseMessages(new ArrayList<String>());
        }
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
