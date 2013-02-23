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
    
    public BookingDate(BookingDate pDate) {
        this.aDate = pDate.getDate();
    }
    
    public BookingDate(Day pDay, int pHour, int pMinute) {
        this.aDate = pDay.toString() + new Integer(pHour).toString() 
                + new Integer(pMinute).toString();
    }
    
    public void setDate(Day pDay, int pHour, int pMinute) {
        this.aDate = pDay.toString() + new Integer(pHour).toString() 
                + new Integer(pMinute).toString();
    }
        
    
    public void setDate(BookingDate pDate) {
        this.aDate = pDate.getDate();
    }
    
    public String getDate() {
        return aDate;
    }
    
    @Override
    public BookingDate clone() throws CloneNotSupportedException {
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
    
    public boolean isAfter(BookingDate pDate) {
               
        return pDate.isBefore(this);
    }
    
    public BookingDate increment(Day pDay, int pHour, int pMinute) {
        Day lDay = this.getDay();
        int lHour = this.getHour();
        int lMinute = this.getMinute();
        
        int temp;
        
        temp = (lMinute + pMinute)/60;
        
        lMinute = (lMinute + pMinute)%60;
        
        lHour = lHour + pHour + temp;
        
        temp = lHour/24;
        
        lHour = lHour%24;
        
        temp = (lDay.ordinal() + temp)%7;
        
        for(Day tDay: Day.values()){
            if(tDay.ordinal() == temp) {
                lDay = tDay;
            }                
        }
        
        return new BookingDate(lDay, lHour, lMinute);
    }
}
