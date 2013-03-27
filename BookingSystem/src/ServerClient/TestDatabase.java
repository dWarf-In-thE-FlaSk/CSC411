/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.net.InetSocketAddress;
import java.util.ArrayList;
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
        
        testGetAvailability(bookingData);
        
        testRegisterObserver(bookingData);
        
        testGetObservers(bookingData);
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
        System.out.println("Try to make a new booking.");
        
        ResponseMessage msg = new ResponseMessage();
        
        BookingDate start = new BookingDate("MONDAY/8/00");
        BookingDate end = new BookingDate("MONDAY/10/00");
                
        msg = (ResponseMessage) bd.registerBooking("LTA", start, end);
        
        List<String> list = msg.getResponseMessages();
        for (String aName: list) {
            System.out.println(aName);
        }
        
        ResponseMessage msg2 = new ResponseMessage();
        
        BookingDate start2 = new BookingDate("FRIDAY/9/00");
        BookingDate end2 = new BookingDate("FRIDAY/11/00");
        
        msg2 = (ResponseMessage) bd.registerBooking("LTC", start2, end2);
         
        List<String> list2 = msg2.getResponseMessages();
        for (String aName: list2) {
            System.out.println(aName);
        }
        
    }    
    
    
    public static void testChanging (BookingData bd) throws CloneNotSupportedException {
        System.out.println("Try to change an existing booking.");
        
        ResponseMessage msg = new ResponseMessage();
        
        msg = (ResponseMessage) bd.changeBooking("LTA#0", "advance", 2);
        
        List<String> list = msg.getResponseMessages();
        for (String aName: list) {
            System.out.println(aName);
        }
        
        
    }   
    
    public static void testGetAvailability(BookingData bd) throws CloneNotSupportedException {
        System.out.println("Try to get the availability of a specific facility");
        
        ResponseMessage msg = new ResponseMessage();
        
        msg = (ResponseMessage) bd.getAvailability("LTA");
        
        List<String> list = msg.getResponseMessages();
        for (String aName: list) {
            System.out.println(aName);
        }
        
        ResponseMessage msg2 = new ResponseMessage();
        
        ArrayList aList = new ArrayList();
        aList.add(Day.MONDAY.toString());
        aList.add(Day.FRIDAY.toString());
        aList.add(Day.SUNDAY.toString());
        
        msg2 = (ResponseMessage) bd.checkAvailability("LTC", aList);
        
        List<String> list2 = msg2.getResponseMessages();
        for (String aName: list2) {
            System.out.println(aName);
        }
        
    }
    
    public static void testRegisterObserver(BookingData bd)  {
        System.out.println("Try to register an observer");
        
        ResponseMessage msg = new ResponseMessage();
        
        InetSocketAddress addr = new InetSocketAddress("192.168.0.10", 8800);
        
        msg = (ResponseMessage) bd.addObserver("LTA", addr, 48);
        
        List<String> list = msg.getResponseMessages();
        for (String aName: list) {
            System.out.println(aName);
        }
        
    }
    
    
    public static void testGetObservers(BookingData bd) throws CloneNotSupportedException {
        System.out.println("Try to get the observers of a specific facility");
        
        List<Observer> observers = bd.getObservers("LTA");
        
        for (Observer aObser: observers) {
            System.out.println(aObser.toString());
        }   
    }    
    
    
}
