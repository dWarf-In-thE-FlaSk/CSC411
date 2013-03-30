/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author rikardandersson, Lance
 */
public class BookingData {
    private ArrayList<String> aFacilityList;
        
    private HashMap<String, ArrayList<BookingEntity>> aRecord;
    
    private HashMap<String, ArrayList<Observer>> aObservers;

    public BookingData() {
        aFacilityList = new ArrayList<String>();
        aRecord = new HashMap<String, ArrayList<BookingEntity>>();
        aObservers = new HashMap<String, ArrayList<Observer>>();
        
    }
    
    public BookingData(ArrayList pFacilityList) {
        aFacilityList = pFacilityList;
        aRecord = new HashMap<String, ArrayList<BookingEntity>>();
        aObservers = new HashMap<String, ArrayList<Observer>>();
    }

    public void setFacilityList(ArrayList<String> pFacilityList) {
        this.aFacilityList = pFacilityList;
    }
    
    public Message getFacilityList() {
        ResponseMessage msg = new ResponseMessage();
        
        msg.setRequestSuccessful(true);
        msg.setResponseMessages(aFacilityList);
        
        return msg;
    }
    
    public void setRecord(HashMap<String, ArrayList<BookingEntity>> pRecord) {
        this.aRecord = pRecord;
    }
    
    public HashMap<String, ArrayList<BookingEntity>> getRecord() {
        return aRecord;
    }
    
    public Message addFacility(String pFacility) {
        ResponseMessage msg = new ResponseMessage();
        
        if (!aFacilityList.contains(pFacility)) {
            aFacilityList.add(pFacility);
            aRecord.put(pFacility, new ArrayList<BookingEntity>());
            aObservers.put(pFacility, new ArrayList<Observer>());
            
            msg.setRequestSuccessful(true);
        }
        else {
            msg.setRequestSuccessful(false);
        }
        return msg;
    }
    
    
    public void removeFacility(String pFacility){
        aFacilityList.remove(pFacility);
    }
  
    public String getFacilityByID(String pID) {
        String[] ID = pID.split("@");
        
        return ID[0];
    }
    
    /**
     * 
     * @param pFacility = facility name to register with
     * @param pStartDate = start date of booking
     * @param pEndDate = end date of booking
     * @return a response message with confirmation ID
     * @return 
     */
    public Message registerBooking(String pFacility, BookingDate pStartDate, BookingDate pEndDate) {
        ResponseMessage msg = new ResponseMessage();
        ExceptionMessage msgEx = new ExceptionMessage();
        
        if(!aFacilityList.contains(pFacility)) {
            
            msgEx.setExceptionType("InputException");
            msgEx.setExceptionMessage("wrong facility name, not exist!");
            
            return msgEx;            
        }
        
        //if any overlapping then fail to register
        if (!checkAvailability(pFacility, pStartDate, pEndDate)) {
            
            msg.setRequestSuccessful(false);
            msg.addResponseMessage("not available for overlapping");
            
            return msg;
        }
        
        ArrayList<BookingEntity> lList = aRecord.get(pFacility);
        
        //case that there is no overlapping entity in the list
        String lID = pFacility + "@" + lList.size();
        
        lList.add(new BookingEntity(pFacility, pStartDate, pEndDate, lID));
      
        msg.setRequestSuccessful(true);
        msg.addResponseMessage("New booking is made. Confirmation ID: ");
        msg.addResponseMessage(lID);
        
        return msg;
        
    }
    
    /**
     * 
     * @param pID = confirmation ID of booking to be changed
     * @param indicator = time interval to change, started with "a"(advance) or "p"(postpone)
     * @param pDate = time interval to advance or postpone
     * @return a response message with facility name
     * @throws CloneNotSupportedException 
     */
    public Message changeBooking(String pID, String indicator, int pHour) throws CloneNotSupportedException {
        ResponseMessage msg = new ResponseMessage();
        ExceptionMessage msgEx = new ExceptionMessage();
        
        String[] ID = pID.split("@");
        
        ArrayList<BookingEntity> lList = aRecord.get(ID[0]);
        
        boolean exist = false;
        BookingEntity lEntity = new BookingEntity();
        
        for(BookingEntity mEntity: lList) {
            if (mEntity.getConformationID().equals(pID)) {
                exist = true;
                lEntity = mEntity;
            }
        }
        
        if (exist == false) {
            
            msgEx.setExceptionType("InputException");
            msgEx.setExceptionMessage("wrong ID, not exist!");
            
            return msgEx; 
            
        }
        
        int day = 0;
        int hour = pHour;
        int minute = 0;
        
        BookingDate lStartDate;
        BookingDate lEndDate;
        
        if(indicator.startsWith("p")) {
            lStartDate = lEntity.getStartDate().increment(day, hour, minute);
            lEndDate = lEntity.getEndDate().increment(day, hour, minute);
        }
        else if(indicator.startsWith("a")) {
            lStartDate = lEntity.getStartDate().decrement(day, hour, minute);
            lEndDate = lEntity.getEndDate().decrement(day, hour, minute);
        }
        else {
            
            msgEx.setExceptionType("InputException");
            msgEx.setExceptionMessage("wrong Indicator, please enter advance or postpone");
            
            return msgEx; 
            
        }
        
        lEntity.setValid(false);
        if (!checkAvailability(lEntity.getFacility(), lStartDate, lEndDate)) {
            msg.setRequestSuccessful(false);
            msg.addResponseMessage("not available for overlapping");
            
            lEntity.setValid(true);
            return msg;
        }
        
        lEntity.setStartDate(lStartDate);
        lEntity.setEndDate(lEndDate);
        lEntity.setValid(true);
        
        msg.setRequestSuccessful(true);
        msg.addResponseMessage("The booking is changed: ");
        msg.addResponseMessage(ID[0]);
        
        return msg;
        
    }
    
    public Message cancelBooking (String pID) {
        ResponseMessage msg = new ResponseMessage();
        ExceptionMessage msgEx = new ExceptionMessage();
        
        String[] ID = pID.split("@");
        
        ArrayList<BookingEntity> lList = aRecord.get(ID[0]);
        
        boolean exist = false;
        BookingEntity lEntity = new BookingEntity();
        
        for(BookingEntity mEntity: lList) {
            if (mEntity.getConformationID().equals(pID)) {
                exist = true;
                lEntity = mEntity;
            }
        }
        
        if (exist == false) {
            
            msgEx.setExceptionType("InputException");
            msgEx.setExceptionMessage("wrong ID, not exist!");
            
            return msgEx; 
            
        }
        
        try {
            lList.remove(lEntity);
        }
        catch (IllegalArgumentException e) {
            msgEx.setExceptionType("Illegal Argument Exception");
            msgEx.setExceptionMessage("Booking not exist!");
        }
        
        msg.setRequestSuccessful(true);
        msg.addResponseMessage("The booking is now cancelled.");
        
        return msg;
    }
    
    
    /**
     * 
     * @param pFacility = facility name to check
     * @param pStartDate = start date
     * @param pEndDate = end date
     * @return 
     */
    public boolean checkAvailability (String pFacility, BookingDate pStartDate, BookingDate pEndDate) {
        ArrayList<BookingEntity> lList = aRecord.get(pFacility);
        
        for(BookingEntity lEntity: lList) {
            if (!lEntity.isValid()) {
                continue;
            }
            
            if (lEntity.isOverlapping(pStartDate, pEndDate)) {
                return false;
            }
        }
        
        return true;
    }
    
    public Message checkAvailability (String pFacility, List<String> pDays) throws CloneNotSupportedException {
        
        String info = "The facility " + pFacility + " is not available on: ";
        
        ArrayList<BookingEntity> bookings = this.aRecord.get(pFacility);
        
        for (BookingEntity lEntity: bookings) {
            if (pDays.contains(lEntity.getStartDate().getDay().toString())) {
                info = info + '\n' + "From " + lEntity.getStartDate().toString() + " to " + lEntity.getEndDate().toString();
            }
        }
        
        ResponseMessage avaibility = new ResponseMessage();
        avaibility.setRequestSuccessful(true);
        avaibility.addResponseMessage(info);
        
        return avaibility;
    }
    
    public Message checkAll () throws CloneNotSupportedException {
        String info = "The facilities " + " are not available on: ";
        
        for (String lFacility: aFacilityList) {
            
            ArrayList<BookingEntity> bookings = this.aRecord.get(lFacility);
        
            for (BookingEntity lEntity: bookings) {
                info = info + '\n' + lEntity.getFacility() + 
                        " From " + lEntity.getStartDate() + " to " + lEntity.getEndDate();
            }
        }
        
        ResponseMessage avaibility = new ResponseMessage();
        avaibility.setRequestSuccessful(true);
        avaibility.addResponseMessage(info);
        
        return avaibility;
    }
    
    /**
     * 
     * @param pFacility = facility name to monitor
     * @param pIPAddr = observer IP address
     * @param pInterval = time interval to monitor in hours
     */
    public Message addObserver(String pFacility, SocketAddress pIPAddr, int pInterval) {
        ResponseMessage msg = new ResponseMessage();
                
        Calendar lCDateTime = Calendar.getInstance();
	long now = lCDateTime.getTimeInMillis();
        long pExpireTime = now + pInterval*3600;
        
        ArrayList ObserverList = aObservers.get(pFacility);
        
        boolean sus = ObserverList.add(new Observer(pFacility, pIPAddr, pExpireTime));
        
        if (sus) {
            msg.setRequestSuccessful(sus);
        }
        else {
            msg.setRequestSuccessful(false);
            msg.addResponseMessage("client already exists");
        }
        
        return msg;
    }
    
    public ArrayList<Observer> getObservers(String pFacility) {
        ArrayList<Observer> candidates =  this.aObservers.get(pFacility);
        ArrayList<Observer> observers = new ArrayList<Observer>();
        
        Calendar lCDateTime = Calendar.getInstance();
	long now = lCDateTime.getTimeInMillis();
        
        for(Observer lObserver: candidates) {
            if (now <= lObserver.getaExpireTime()) {
                observers.add(lObserver);
            }
        }
        
        return observers;
    }
    
    public Message getAvailability (String pFacility) throws CloneNotSupportedException {
        String info = "The facility " + pFacility + " is not available on: ";
        
        ArrayList<BookingEntity> bookings = this.aRecord.get(pFacility);
        
        for (BookingEntity lEntity: bookings) {
                info = info + '\n' + "From " + lEntity.getStartDate().toString() + " to " + lEntity.getEndDate().toString();
        }
        
        ResponseMessage avaibility = new ResponseMessage();
        avaibility.setRequestSuccessful(true);
        avaibility.addResponseMessage(info);
        
        return avaibility;
    }
}
