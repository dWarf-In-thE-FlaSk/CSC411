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
public class ExceptionMessage implements Message {

    @Override
    public List<String> serialize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMessageType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMessageType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRequestID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRequestID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
