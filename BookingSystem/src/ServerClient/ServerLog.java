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
    // The key will be the user making the request
    // The value will be the request made
    private HashMap<String, String> log;
    
    public void registerRequest(String requester, String request) {
        // If this is the first registration, initiate the log
        if (log == null) {
            log = new HashMap<String, String>();
        }
        // Handle the case where this request is already in the log
        log.put(requester, request);
    }
    
    // Returning the latest request made by the requester
    public String lastRequest(String requester) {
        if (log == null) {
            return null;
        } else if (log.containsKey(requester)) {
            return log.get(requester);
        } else {
            return null;
        }
    }
}
