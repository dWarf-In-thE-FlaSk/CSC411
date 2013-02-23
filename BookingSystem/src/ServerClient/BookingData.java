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
    private ArrayList<String> aFacilityList;
        
    private HashMap<String, ArrayList<BookingEntity>> aRecord;

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
        String ID = pFacility + "#" + lList.size();
        
        lList.add(new BookingEntity(pFacility, pStartDate, pEndDate, ID));
      
        notifyObserver("register", ID);
        
        notifyObserver("update" ,pFacility);
        
        return true;
    }
    
    public boolean changeBooking() {
        //to do
        
        
        
        return true;
    }
    
    public void notifyObserver(String action, String msg) {
        
    }
}
