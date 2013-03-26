/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.util.List;

/**
 *
 * @author Lance
 */
public class TestDatabase {
 
    public static void main(String args[]) throws CloneNotSupportedException {
        
        BookingData bookingData = new BookingData();
        
        bookingData.addFacility("LTA");
        bookingData.addFacility("LTB");
        bookingData.addFacility("LTC");
        bookingData.addFacility("LTD");
        
        System.out.println("Test begins");
        
        testGetFacilityName(bookingData);
        
        testRegister(bookingData);
        
        testChanging(bookingData);
    }
    
    public static void testGetFacilityName(BookingData bd) {
         
        
        System.out.println("Getting the list of facilities.");
        ResponseMessage msg = new ResponseMessage();
        
        msg = (ResponseMessage) bd.getFacilityList();
        List<String> list = msg.getResponseMessages();
        for (String aName: list) {
            System.out.println(aName);
        }
        
    }
    
    public static void testRegister (BookingData bd) {
        ResponseMessage msg = new ResponseMessage();
        
        BookingDate start = new BookingDate("MONDAY/8/00");
        BookingDate end = new BookingDate("MONDAY/10/00");
                
        msg = (ResponseMessage) bd.registerBooking("LTA", start, end);
        
        List<String> list = msg.getResponseMessages();
        for (String aName: list) {
            System.out.println(aName);
        }
        
        ResponseMessage msg2 = new ResponseMessage();
        
        BookingDate start2 = new BookingDate("MONDAY/9/00");
        BookingDate end2 = new BookingDate("MONDAY/11/00");
        
        msg2 = (ResponseMessage) bd.registerBooking("LTA", start2, end2);
         
        List<String> list2 = msg2.getResponseMessages();
        for (String aName: list2) {
            System.out.println(aName);
        }
        
    }    
    
    
    public static void testChanging (BookingData bd) throws CloneNotSupportedException {
        ResponseMessage msg = new ResponseMessage();
        
        BookingDate start = new BookingDate("MONDAY/8/00");
        BookingDate end = new BookingDate("MONDAY/10/00");
                
        msg = (ResponseMessage) bd.changeBooking("LTA#0", "advance", 10);
        
        List<String> list = msg.getResponseMessages();
        for (String aName: list) {
            System.out.println(aName);
        }
        
        
    }   
    
}
