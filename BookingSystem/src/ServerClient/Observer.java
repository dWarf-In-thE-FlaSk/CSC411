/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.util.Date;

/**
 *
 * @author Lance
 */
public class Observer {
    private String aFacility;
    private String aIPAddr;
    private Date aExpireDate;

    public Observer(String aFacility, String aIPAddr, Date pExpireDate) {
        this.aFacility = aFacility;
        this.aIPAddr = aIPAddr;
        this.aExpireDate = pExpireDate;
    }

    public String getaFacility() {
        return aFacility;
    }

    public void setaFacility(String aFacility) {
        this.aFacility = aFacility;
    }

    public String getaIPAddr() {
        return aIPAddr;
    }

    public void setaIPAddr(String aIPAddr) {
        this.aIPAddr = aIPAddr;
    }

    public BookingDate getaStartDate() {
        return aStartDate;
    }

    public void setaStartDate(BookingDate aStartDate) {
        this.aStartDate = aStartDate;
    }

    public BookingDate getaEndDate() {
        return aEndDate;
    }

    public void setaEndDate(BookingDate aEndDate) {
        this.aEndDate = aEndDate;
    }
    
    public 
}
