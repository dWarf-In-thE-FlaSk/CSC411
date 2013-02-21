/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

/**
 *
 * @author rikardandersson
 */
public class BookingEntity {
    private Facility aFacility;
    private BookingDate aStartDate;
    private BookingDate aEndDate;
    int aConformationID;
    
    public void setFacility(Facility pFacility) {
        aFacility = pFacility;
    }
    
    public Facility getFacility() throws CloneNotSupportedException {
        return aFacility.clone();
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
    
    public void setConformationID(int pConformationID) {
        aConformationID = pConformationID;
    }
    
    public int getConformationID() {
        return aConformationID;
    }

}
