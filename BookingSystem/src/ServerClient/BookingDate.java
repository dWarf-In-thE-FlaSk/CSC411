/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

/**
 *
 * @author Lance
 */
public class BookingDate {
    private String aDate;
    
    public void setDate(Day pDay, int pHour, int pMinute) {
        this.aDate = pDay.toString() + new Integer(pHour).toString() 
                + new Integer(pMinute).toString();
    }
        
    
    public void setDate(BookingDate pDate) {
        this.aDate = pDate.getDateString();
    }
    
    public String getDateString() {
        return aDate;
    }
    
    public BookingDate getDate() throws CloneNotSupportedException {
        return (BookingDate)this.clone();
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
     
    public boolean isBefore() {
        //code here
        
        return true;
    }
    
    public boolean isAfter() {
        //code here
        
        return true;
    }
}
