/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi.data;

/**
 *
 * @author Wizard
 */
public class StringNumber {
    
    
    protected String name;
    protected String value;
    
    public StringNumber(String name){this.name = name;}
    
    public StringNumber(String name, String value) {
        this.name = name;
        this.value = treatValue(value);
    
    }
    
    public StringNumber(StringNumber sn){
    this.name = sn.name;
    this.value = sn.value;
    }
    
    private String treatValue(String value){
         String v = value.replaceAll(",", ".");
         v = v.replaceAll("[^.0-9]","");
        int index = v.indexOf(".");
    String v1 = v.substring(index+1); 
    v1 = v1.replace(".", "");
        return v.substring(0,index+1)+ v1;
    
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param Name the name to set
     */
    public void setName(String Name) {
        this.name = Name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
        public String getValue(int length) {
        return value.substring(0, length);
    }

    /**
     * @param value the value to set
     */
        
    public void setAndTreatValue(String value) {

        this.value = treatValue(value);
    }
    

    
        
        public String getWholePart(){
            
            String[] parts = value.split("\\.");
           return parts[0]; }
    
        
        public String getDecimalPart(){
        
            String[] parts = value.split("\\.");
           if (parts.length==2){
            return parts[1];} else {return null;}
        }
        
        public String[] getSplittedParts() {
        String[] parts = value.split("\\.");
        return parts;
        }
        
    
    
    
    
    
}
