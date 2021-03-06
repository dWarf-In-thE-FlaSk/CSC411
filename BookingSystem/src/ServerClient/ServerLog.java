/**
 * ServerLog is a helping class for BookingServer keeping track of made requests
 * and their responses. This will allow BookingServer to query the ServerLog 
 * upon each request whether it's a new and unique one. This will help
 * preventing duplicate requests from one client due to responses being lost on
 * the way back from the server to the client. 
 */
package ServerClient;

import java.net.SocketAddress;
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
 * specific IP address and port. This can however be extended.
 * 
 * @author Rikard Andersson
 */
public class ServerLog {
    // log will map request and responses against the address where it came from
    // and to where it was sent. For reference to RequestResponsePair se below.
    private HashMap<SocketAddress, RequestResponsePair> log;
    
    public ServerLog() {
        log = new HashMap<SocketAddress, RequestResponsePair>();
    }
    
    /**
     * Public method to register a request and it's respons in the ServerLog.
     * 
     * @param id The ID of the request as generated by the client
     * @param client The client the request comes from and the response goes to.
     * @param response The respons sent back to the client
     */
    public void registerRequest(SocketAddress client, int id, Message response) {
        // If there is a previous request registered it will be overwritten here
        // See reason for this is in class description.
        log.put(client, new RequestResponsePair(id, response));
    }
    
    /**
     * A method to determine if a request is new or has been made before.
     * 
     * @param id The ID of the request as generated by the client.
     * @param client The client the request came from.
     * @return boolean indicating if the request by a certain ID exists
     */
    public boolean requestIsLoggedForClient(int id, SocketAddress client) {
        // If there is not key corresponding to the client 
        if (!log.containsKey(client)) { // Client has no registered requests
            return false;
        } else { // Check if last request is equal to the one being tried.
            return (log.get(client).getRequestID() == id);
        }
    }
    
    /**
     * Returning the respons for a request ID. If there is no such request (one 
     * matching the request ID) null is returned.
     * 
     * @param id the ID of the request as generated by the client
     * @param client The address from where the request came and to where it is
     * returned
     * @return Message containing the respons or null if request isn't the latest
     * one logged (for any reason).
     */
    public Message responsForRequest(int id, SocketAddress client) {
        if (this.requestIsLoggedForClient(id, client)) {            
            return this.log.get(client).getResponse();
        } else {
            return null;
        }
    }
    
    /**
     * A helper class storing request and respons in an object.
     * 
     * @author Rikard Andersson
     */
    public class RequestResponsePair {
        
        private int requestID;
        private Message response;
        
        /**
         * Constructor taking in parameters for requestID and response
         * 
         * @param requestID The request ID
         * @param response Returned response
         */
        public RequestResponsePair(int requestID, Message response) {

            this.requestID = requestID;
            this.response = response;
        }
        
        public void setRequestID(int requestID) {
            this.requestID = requestID;
        }

        public int getRequestID() {
            return this.requestID;
        }

        public void setResponse(Message response) {
            this.response = response;
        }

        public Message getResponse() {
            return this.response;
        }
        
        @Override
        public int hashCode() {
            int hash = 1;
            
            hash += (this.requestID + 1) * 89;
            
            if (this.response != null) {
                hash += this.response.hashCode() * 89;
            }
            return hash;
        }
        
        @Override
        public boolean equals(Object o) {
            if (o == null) {
                // If the object is null they should not be considered equal
                return false;
            } else if (!(o instanceof RequestResponsePair)) {
                // If the object is not of the same class as this object then
                // it's not equal and we cannot continue the operations in else.
                return false;
            } else {
                // Type casting the object
                RequestResponsePair rrp = (RequestResponsePair) o;
                // Evaluating whether the response and request in the two 
                // objects are the same or not and returning the result.
                return ((this.requestID == rrp.getRequestID()) &&
                        this.response.equals(rrp.getResponse()));
            }
            
        }
    }
}
