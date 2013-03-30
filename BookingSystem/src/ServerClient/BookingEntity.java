package ServerClient;

/**
 *
 * @author rikardandersson, Lance
 * 
 * This class is used to store information for a specific booking, 
 * including facility name, start date, end date and confirmation ID.
 * Filed valid is only for use in BookingData.
 * 
 */
public class BookingEntity {
    private String aFacility;
    private BookingDate aStartDate;
    private BookingDate aEndDate;
    private String aConformationID;
    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    //Default constructor
    public BookingEntity() {
        super();
    }
    
    public BookingEntity(String pFaci, BookingDate pStart, BookingDate pEnd, String pID) {
        aFacility = pFaci;
        aStartDate = pStart;
        aEndDate = pEnd;
        aConformationID = pID;
        valid = true;
    }
    
    //Setters and getters
    public void setFacility(String pFacility) {
        aFacility = pFacility;
    }
    
    public String getFacility() {
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

    /**
     * 
     * @param pStartDate = start date to check overlapping
     * @param pEndDate = end date to check overlapping
     * @return Boolean
     */
    public boolean isOverlapping(BookingDate pStartDate, BookingDate pEndDate) {
        if(aStartDate.isAfter(pEndDate) || aEndDate.isBefore(pStartDate)) {
            return false;
        }
        else {
            return true;
        }
    }
}
