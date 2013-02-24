/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

/**
 *
 * @author Lance
 */

public interface Observable
{
    /**
     * 
     * @param action = index to indicate the type of operation
     * @param msg = the message to be passed to observer
     */
    void notifyObserver(String action, String msg);
	
    /**
     * 
     * @param pObserver 
     */
    void addObserver(Observer pObserver);

    /**
     * 
     * @param pObserver 
     */
    
    void removeObserver();
}