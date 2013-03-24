/*
 */
package ServerClient;

import java.util.ArrayList;
import java.util.List;

/**
 * This type of Message will be sent when something goes wrong in the
 * communication between the client and the server. For instance if an exception
 * is thrown at server side as a result of a request from the client, it will 
 * return an ExceptionMessage.
 * 
 * @author Rikard Andersson
 */
public class ExceptionMessage implements Message {
    
    private static int messageType = -1;
    private int requestID;
    private String exceptionType;
    private String exceptionMessage;

    public String getExceptionType() {
        return exceptionType;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
    
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
    
    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }
    
    @Override
    public int getMessageType() {
        return messageType;
    }

    @Override
    public int getRequestID() {
        return requestID;
    }
    
    @Override
    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }
    
    @Override
    public List<String> serializeMessageContent() {
        List<String> serializedContent = new ArrayList<String>();
        serializedContent.add(String.valueOf(this.getMessageType()));
        serializedContent.add(String.valueOf(this.getRequestID()));
        serializedContent.add(exceptionType);
        serializedContent.add(exceptionMessage);
        return serializedContent;
    }
    @Override
    public void unserializeAndSetMessageContent(List<String> serializedMessageContent) {
        this.setExceptionType(serializedMessageContent.get(0));
        this.setExceptionMessage(serializedMessageContent.get(1));
    }
    
    public boolean equals(Object o) {
        if (o == null) {
            // If the object is null they should not be considered equal
            return false;
        } else if (!(o instanceof ExceptionMessage)) {
            // If the object is not of the same class as this object then
            // it's not equal and we cannot continue the operations in else.
            return false;
        } else {
            // Type casting the object since we know it's the same type
            ExceptionMessage em = (ExceptionMessage) o;
            // Se if the containers in the ResponseMessages matches. If the do
            // they are the same.
            return ((this.requestID == em.getRequestID()) &&
                    this.exceptionMessage.equals(em.getExceptionMessage()) &&
                    this.exceptionType.equals(em.getExceptionType()));
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 1;

        hash += (this.requestID + 1) * 89;
        hash += this.exceptionMessage.hashCode() * 13;
        hash += this.exceptionType.hashCode() * 13;
        
        return hash;
    }
}
