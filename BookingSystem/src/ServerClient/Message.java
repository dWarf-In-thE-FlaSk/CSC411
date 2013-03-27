/*
 */
package ServerClient;

import java.util.List;

/**
 * This is an interface implemented by classes that represent messages being 
 * passed between client and server.
 * 
 * @author Rikard Andersson
 */
public interface Message {
    // This will return a flattened version of the content of the Message
    public List<String> serializeMessageContent();
    // This will build the part of the Message that is unique to the Message type. (And hence excluded from this interface)
    public void unserializeAndSetMessageContent(List<String> serializedMessageContent);
    // Every message will have a RequestID created and appended by the client.
    public int getRequestID();
    public void setRequestID(int requestID);
    // The MessageType will indicate what kind of message this is. It needs to be unique for each new implementation of Message.
    public int getMessageType();
    
    // equals() and hashCode() needs to be implemented for the ServerLog to work.
    @Override
    public boolean equals(Object o);
    @Override
    public int hashCode();
}