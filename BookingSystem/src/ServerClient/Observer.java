/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

/**
 *
 * @author Lance
 */

public interface Observer
{
	/**
	 * 
	 * @param action = index to indicate the type of operation
	 * @param msg = the message passed to observer
	 */
	void handleEvent(String action, String msg);
	

}