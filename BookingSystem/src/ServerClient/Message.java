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
public interface Message {
    public List<String> serializeMessageContent();
    public void unserializeAndSetMessageContent(List<String> serializedMessageContent);
    public int getRequestID();
    public void setRequestID(int requestID);
    public int getMessageType();
    @Override
    public boolean equals(Object o);
    @Override
    public int hashCode();
}
