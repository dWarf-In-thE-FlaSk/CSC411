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
    
    public boolean addFacility(String pFacility) {
        if (!aFacilityList.contains(pFacility)) {
            aFacilityList.add(pFacility);
            return true;
        }
        else {
            return false;
        }
        
    }
    
    public void removeFacility(String pFacility){
        aFacilityList.remove(pFacility);
    }
    
    public DataMsg registerBooking(String pFacility, BookingDate pStartDate, BookingDate pEndDate) {
        
        if(!aFacilityList.contains(pFacility)) {
            
            return new DataMsg("wrongName", "");
            
        }
        
        //if any overlapping then fail to register
        if (!checkAvaibility(pFacility, pStartDate, pEndDate)) {
             return new DataMsg("overlap", "");
        }
        
        ArrayList<BookingEntity> lList = aRecord.get(pFacility);
        
        //case that there is no overlapping entity in the list
        String lID = pFacility + "#" + lList.size();
        
        lList.add(new BookingEntity(pFacility, pStartDate, pEndDate, lID));
      
        return new DataMsg("register", lID);
        
    }
    
    public DataMsg changeBooking(String pID, String indicator ,BookingDate pDate) throws CloneNotSupportedException {
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
            
            return new DataMsg("wrongID", "");
            
        }
              
        Integer day = pDate.getDay().ordinal();
        Integer hour = pDate.getHour();
        Integer minute = pDate.getMinute();
        
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
            
            return new DataMsg("wrongInd", "");
            
        }
        
        if (!checkAvaibility(lEntity.getFacility(), lStartDate, lEndDate)) {
             return new DataMsg("overlap", "");
        }
        
        lEntity.setStartDate(lStartDate);
        lEntity.setEndDate(lEndDate);
        
        return new DataMsg("change" , ID[0]);
        
    }
    
    public boolean checkAvaibility (String pFacility, BookingDate pStartDate, BookingDate pEndDate) {
        ArrayList<BookingEntity> lList = aRecord.get(pFacility);
        
        for(BookingEntity lEntity: lList) {
            if (lEntity.isOverlapping(pStartDate, pEndDate)) {
                return false;
            }
        }
        
        return true;
    }
     
}
