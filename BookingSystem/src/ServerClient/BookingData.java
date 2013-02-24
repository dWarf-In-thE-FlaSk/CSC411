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
public class BookingData implements Observable {
    private ArrayList<String> aFacilityList;
        
    private HashMap<String, ArrayList<BookingEntity>> aRecord;
    
    private Observer aObserver;

    public BookingData() {
        aFacilityList = new ArrayList<String>();
        aRecord = new HashMap<String, ArrayList<BookingEntity>>();
        
    }
    
    public BookingData(ArrayList pFacilityList, HashMap pRecord) {
        aFacilityList = pFacilityList;
        aRecord = pRecord;
    }

    public void setFacilityList(ArrayList<String> pFacilityList) {
        this.aFacilityList = pFacilityList;
    }
    
    public ArrayList<String> getFacilityList() {
        return aFacilityList;
    }
    
    public void setRecord(HashMap<String, ArrayList<BookingEntity>> pRecord) {
        this.aRecord = pRecord;
    }
    
    public HashMap<String, ArrayList<BookingEntity>> getRecord() {
        return aRecord;
    }
    
    public boolean registerBooking(String pFacility, BookingDate pStartDate, BookingDate pEndDate) {
        
        if(!aFacilityList.contains(pFacility)) {
            notifyObserver("wrongName", "");
            return false;
        }
        
        ArrayList<BookingEntity> lList = aRecord.get(pFacility);
        
        //if any overlapping then fail to register
        for(BookingEntity lEntity: lList) {
            if (lEntity.isOverlapping(pStartDate, pEndDate)) {
                notifyObserver("overlap", "");
                return false; 
            }    
        }
        
        //case that there is no overlapping entity in the list
        String lID = pFacility + "#" + lList.size();
        
        lList.add(new BookingEntity(pFacility, pStartDate, pEndDate, lID));
      
        notifyObserver("register", lID);
        
        notifyObserver("update" ,pFacility);
        
        return true;
    }
    
    public boolean changeBooking(String pID, String indicator ,String pDate) throws CloneNotSupportedException {
        String[] ID = pID.split("#");
        
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
            notifyObserver("wrongID", "");
            return false;
        }
        
        String[] result = pDate.split("/");
        
        Integer day = new Integer(result[0]);
        Integer hour = new Integer(result[1]);
        Integer minute = new Integer(result[3]);
        
        BookingDate lStartDate;
        BookingDate lEndDate;
        
        if(indicator.startsWith("a")) {
            lStartDate = lEntity.getStartDate().increment(day, hour, minute);
            lEndDate = lEntity.getEndDate().increment(day, hour, minute);
        }
        else if(indicator.startsWith("p")) {
            lStartDate = lEntity.getStartDate().decrement(day, hour, minute);
            lEndDate = lEntity.getEndDate().decrement(day, hour, minute);
        }
        else {
            notifyObserver("wrongInd", "");
            return false; 
        }
        
        for(BookingEntity mEntity: lList) {
            if (mEntity.isOverlapping(lStartDate, lEndDate)) {
                notifyObserver("overlap", "");
                return false; 
            }    
        }
        
        lEntity.setStartDate(lStartDate);
        lEntity.setEndDate(lEndDate);
        
        notifyObserver("change" , pID);
        notifyObserver("update" , ID[0]);
        
        return true;
    }
    

    @Override
    public void notifyObserver(String action, String msg) {
        if (aObserver == null)
        {
            throw new NullPointerException("Error: no server response");
        }
	else
	{
            aObserver.handleEvent(action, msg);
	}    
    }

    @Override
    public void addObserver(Observer pObserver) {
        aObserver = pObserver;
    }

    @Override
    public void removeObserver() {
        aObserver = null;
    }
}
