/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.net.SocketAddress;
import java.util.Date;

/**
 *
 * @author Lance
 */
public class Observer {
    private String aFacility;
    private SocketAddress aIPAddr;
    private long aExpireTime;

    public Observer(String aFacility, SocketAddress aIPAddr, long pExpireTime) {
        this.aFacility = aFacility;
        this.aIPAddr = aIPAddr;
        this.aExpireTime = pExpireTime;
    }

    public String getaFacility() {
        return aFacility;
    }

    public void setaFacility(String aFacility) {
        this.aFacility = aFacility;
    }

    public long getaExpireTime() {
        return aExpireTime;
    }

    public void setaExpireTime(long aExpireTime) {
        this.aExpireTime = aExpireTime;
    }

    public SocketAddress getaIPAddr() {
        return aIPAddr;
    }

    public void setaIPAddr(SocketAddress aIPAddr) {
        this.aIPAddr = aIPAddr;
    }

    
}
