/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

/**
 *
 * @author Lance
 */

public class Facility {
    private String aName;

    public Facility(String pName) {
        aName = pName;
    }
    
    @Override
    public Facility clone() throws CloneNotSupportedException {  
        return (Facility) super.clone();  
    }
    
    public void setName(String pName) {
        aName = pName;
    }
    
    public String getName() {
        return aName;
    }
}
