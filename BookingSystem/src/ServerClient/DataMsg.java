/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

/**
 *
 * @author Lance
 */
public class DataMsg {
    private String action;
    
    private String msg;

    public DataMsg(String action, String msg) {
        this.action = action;
        this.msg = msg;
    }

    public String getAction() {
        return action;
    }

    public String getMsg() {
        return msg;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
}
