/**
 * ServerLog is a helping class for BookingServer keeping track of made requests
 * and their responses. This will allow BookingServer to query the ServerLog 
 * upon each request whether it's a new and unique one. This will help
 * preventing duplicate requests from one client due to responses being lost on
 * the way back from the server to the client. 
 */
package ServerClient;

import java.util.HashMap;

/**
 * ServerLog is a helping class for BookingServer keeping track of made requests
 * and their responses. This will allow BookingServer to query the ServerLog 
 * upon each request whether it's a new and unique one. This will help
 * preventing duplicate requests from one client due to responses being lost on
 * the way back from the server to the client.
 * 
 * Since the client application is a fairly simple application running on single
 * thread the ServerLog will only store the latest request/response from/to a
 * specific IP address and port.
 * 
 * @TODO: Decided if we should only apply this technique to non-idempotent 
 * operations.
 * @TODO: Delete previous operations 
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
