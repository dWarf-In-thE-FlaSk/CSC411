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
        this.setRequestSuccessful(Boolean.parseBoolean(serializedMessageContent.get(0)));
        this.setResponseMessages(serializedMessageContent.subList(1, serializedMessageContent.size()));
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
    
    public boolean equals(Object o) {
        if (o == null) {
            // If the object is null they should not be considered equal
            return false;
        } else if (!(o instanceof ResponseMessage)) {
            // If the object is not of the same class as this object then
            // it's not equal and we cannot continue the operations in else.
            return false;
        } else {
            // Type casting the object since we know it's the same type
            ResponseMessage rm = (ResponseMessage) o;
            // Se if the containers in the ResponseMessages matches. If the do
            // they are the same.
            return ((this.requestID == rm.getRequestID()) &&
                    (this.requestSuccessful == rm.isRequestSuccessful()) &&
                    this.responseMessages.equals(rm.getResponseMessages()));
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 1;

        hash += (this.requestID + 1) * 89;
        if (this.requestSuccessful) {
            hash += 13;
        }
        
        if (this.responseMessages != null) {
            hash += this.responseMessages.hashCode() * 89;
        }
        return hash;
    }
}
