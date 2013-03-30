package ServerClient;

/**
 *
 * @author Lance
 * 
 * 
 * This class is used in BookingData to store information of a specific date object
 */


public class BookingDate implements Cloneable{
    private String aDate;

    public BookingDate() {
        super();
    }
    
    public BookingDate(String pDate) {
        this.aDate = pDate;
    }
    
    public BookingDate(BookingDate pDate) {
        this.aDate = pDate.getDate();
    }
    
    public BookingDate(Day pDay, int pHour, int pMinute) {
        this.aDate = pDay.toString() + "/" + new Integer(pHour).toString() 
                + "/" +new Integer(pMinute).toString();
    }
    
    @Override
    public BookingDate clone() throws CloneNotSupportedException {
        return (BookingDate)super.clone();
    }
    
    @Override
    public String toString() {
        return this.aDate;
    }
    
    //Setters and getters
    public void setDate(Day pDay, int pHour, int pMinute) {
        this.aDate = pDay.toString() + "/" + new Integer(pHour).toString() 
                    + "/" +new Integer(pMinute).toString();
    }
        
    
    public void setDate(BookingDate pDate) {
        this.aDate = pDate.getDate();
    }
    
    public String getDate() {
        return aDate;
    }
    
    public Day getDay() {
        String[] result = aDate.split("/");
               
        return Day.valueOf(result[0]);
    }
    
    public int getHour() {
        String[] result = aDate.split("/");
        
        return new Integer(result[1]);
    }
    
     public int getMinute() {
        String[] result = aDate.split("/");
        
        return new Integer(result[2]);
    }
    
     /**
      * Checking if current is before the given date
      * 
      * @param pDate = BookingDate to compare to
      * @return Boolean
      */
    public boolean isBefore(BookingDate pDate) {
        boolean result;
        
        if (this.getDay().ordinal() < pDate.getDay().ordinal()) {
            result = true;
        }
        else if (this.getDay().ordinal() > pDate.getDay().ordinal()) {
            result = false;
        }
        else {
            if (this.getHour() < pDate.getHour()) {
                result = true;
            }
            else if (this.getHour() > pDate.getHour()) {
                result = false;
            }
            else {
                if (this.getMinute() < pDate.getMinute()) {
                    result = true;
                }
                
                else {
                    result = false;
                }    
            }
        }
        
        return result;
    }
    
    /**
     * Check if current date if after the given date
     * 
     * @param pDate = BookingDate to compare to
     * @return Boolean
     */
    public boolean isAfter(BookingDate pDate) {
               
        return pDate.isBefore(this);
    }
    
    /**
     * Increase current BookingDate by given time
     * 
     * @param pDay = number of days to add
     * @param pHour = number of hours to add
     * @param pMinute = number of minute to add
     * @return 
     */
    public BookingDate increment(int pDay, int pHour, int pMinute) {
        Day lDay = this.getDay();
        int lHour = this.getHour();
        int lMinute = this.getMinute();
        
        int temp;
        
        temp = (lMinute + pMinute)/60;
        
        lMinute = (lMinute + pMinute)%60;
        
        lHour = lHour + pHour + temp;
        
        temp = lHour/24;
        
        lHour = lHour%24;
        
        temp = (lDay.ordinal() + temp + pDay)%7;
        
        for(Day tDay: Day.values()){
            if(tDay.ordinal() == temp) {
                lDay = tDay;
            }                
        }
        
        return new BookingDate(lDay, lHour, lMinute);
    }
    
    
    /**
     * Decrease current BookingDate by given time
     * 
     * @param pDay = number of days to minus
     * @param pHour = number of hours to minus
     * @param pMinute = number of minute to minus
     * @return 
     */ 
    public BookingDate decrement(int pDay, int pHour, int pMinute) {
        return increment(-pDay, -pHour, -pMinute);
        
    }
}
