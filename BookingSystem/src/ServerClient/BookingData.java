/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author rikardandersson, Lance
 */
public class BookingData {
    /*
    private Facility[] aAllFacility = {new Facility("LTA"), 
        new Facility("LTB"), new Facility("LTC"), new Facility("LTD")};
    
    */
    
    private HashMap<Facility, ArrayList<BookingEntity>> aRecord;
    
    public boolean registerBooking(String pFacility, BookingDate pStartDate, BookingDate pEndDate) {
                
        Facility lFacility = Facility.valueOf(pFacility);
        
        ArrayList lList = aRecord.get(lFacility);
        
        
        
        
        
        return true;
    }
}
