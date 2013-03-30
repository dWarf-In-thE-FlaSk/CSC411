package ServerClient;

import java.net.SocketAddress;
import java.util.Date;

/**
 *
 * @author Lance
 * 
 * This class is to represent an object of a user who want to monitor 
 * some facility. Fields include facility name, IP address and expire time.
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

    //Setters and getters
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

    @Override
    public String toString() {
        return (aFacility + " " + aIPAddr.toString());
    }
    
}
