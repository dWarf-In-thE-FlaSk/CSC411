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
    public List<String> serialize();
    public int getRequestID();
    public void setRequestID();
}
