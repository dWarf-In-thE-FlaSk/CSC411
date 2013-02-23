/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

/**
 *
 * @author rikardandersson, Lance
 */
public class BookingEntity {
    private Facility aFacility;
    private BookingDate aStartDate;
    private BookingDate aEndDate;
    private String aConformationID;
    
    public BookingEntity(Facility pFaci, BookingDate pStart, BookingDate pEnd, String pID) {
        aFacility = pFaci;
        aStartDate = pStart;
        aEndDate = pEnd;
        aConformationID = pID;
    }
    
    public void setFacility(Facility pFacility) {
        aFacility = pFacility;
    }
    
    public Facility getFacility() {
        return aFacility;
    }
    
    public void setStartDate(BookingDate pStartDate) {
        aStartDate = pStartDate;
    }
    
    public BookingDate getStartDate() throws CloneNotSupportedException {
        return aStartDate.clone();
    }
    
    public void setEndDate(BookingDate pEndDate) {
        aEndDate = pEndDate;
    }
    
    public BookingDate getEndDate() throws CloneNotSupportedException {
        return aEndDate.clone();
    }
    
    public void setConformationID(String pConformationID) {
        aConformationID = pConformationID;
    }
    
    public String getConformationID() {
        return aConformationID;
    }

    public boolean isOverlapping(BookingDate pStartDate, BookingDate pEndDate) {
        if(aStartDate.isBefore(pEndDate) || aEndDate.isAfter(pStartDate)) {
            return true;
        }
        else {
            return false;
        }
    }
}
