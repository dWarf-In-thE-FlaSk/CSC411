/*
 * This class will store requests made and cache the results 
 * 
 */
package ServerClient;

import java.util.HashMap;

/**
 *
 */
public class ServerLog {
    // log will contain the latest requests made
    // The key will be the request ID
    // The value will be the respons to the request
    private HashMap<String, String> log;
    
    public void registerRequest(String ID, String response) {
        // If this is the first registration, initiate the log
        if (log == null) {
            log = new HashMap<String, String>();
        }
        // @TODO - Handle the case where this request is already in the log
        log.put(ID, response);
    }
    
    // Returning a boolean indicating whether or not the request id is new
    public boolean requestExists(String ID) {
        if (log == null) {
            return false;
        } else {
            return log.containsKey(ID);
        }
    }
    
    // Returning the respons for the requestID - returns null if there is no such ID
    public String responsForRequest(String ID) {
        if (log == null) {
            return null;
        } else {
            return log.get(ID);
        }
    }
}
