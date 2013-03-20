/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.util.Map;

/**
 *
 * @author Lance & Rikard Andersson
 */
public class DataMsg {
    
    private int messageType;
    private String requestID;
    private String command;
    private String msg; // depreceate
    private Map<String, String> attributes;

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public void setAttributes(List<Object> attributes) {
        this.attributes = attributes;
    }
    
    public void setCommand(String command) {
        this.command = command;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getRequestID() {
        return requestID;
    }

    public List<Object> getAttributes() {
        return attributes;
    }

    public DataMsg(String action, String msg) {
        this.command = action;
        this.msg = msg;
    }

    public String getCommand() {
        return command;
    }

    public String getMsg() {
        return msg;
    }    
}
