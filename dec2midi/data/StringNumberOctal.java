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
public class StringNumberOctal extends StringNumber{
    
    public StringNumberOctal(String name, String value) {
               super(name);
     this.value = treatValue(value);
    }
    
    
         @Override
        public void setAndTreatValue(String value) { 
this.value = treatValue(value);
    }
        
        private String treatValue(String value) {
                String v = value.replaceAll(",", ".");
         v = v.replaceAll("[^.0-7]","");
        int index = v.indexOf(".");
    String v1 = v.substring(index+1); 
    v1 = v1.replace(".", "");
        return v.substring(0,index+1)+ v1;
        
        }
    
        @Override
        public String getValue() {
    
       return value;
        

    }
        
                 @Override  
                public String getValue(int length) {
        return value.substring(0, length);
    }
    

    
}
