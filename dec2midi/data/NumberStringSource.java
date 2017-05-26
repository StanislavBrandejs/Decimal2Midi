/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi.data;

import static dec2midi.data.internal.XmlParser.parseXmlDataAndReturnNumberList;

import java.util.List;


/**
 *
 * @author Wizard
 */
public class NumberStringSource {
    
    List<StringNumber> snl;
    
    
    public NumberStringSource() {
    snl = parseXmlDataAndReturnNumberList();    
    }
    
    
    
    public List<StringNumber> getList(){
    return snl;
    }
    
    public void reParse() {
    
        snl = parseXmlDataAndReturnNumberList();
    }
    
    
    
    
    
        
    public String getNumberByName(String name) {
    
        for (StringNumber stringNumber : snl) { 
            if(stringNumber.getName().equals(name)){return stringNumber.getValue();}
            
        }
    
    return null ;
    
    }
    
    
       
    
        public StringNumber getStringNumberByName(String name) {
    
        for (StringNumber stringNumber : snl) { 
            if(stringNumber.getName().equals(name)){return stringNumber;}
            
        }
    
    return null ;
    
    }
    


    
        
    public void printNumberList(boolean justNames) {
            
        
        if(justNames) {
        
        for (int i = 0; i < snl.size(); i++) 
        
        {
            System.out.println("name:" + snl.get(i).getName());
        }
            
        } else {
        for (int i = 0; i < snl.size(); i++) 
        
        {
            System.out.println("name:" + snl.get(i).getName() + "value:" + snl.get(i).getValue(10));
        }
        }
        
        
    }
    
    
    
    
    
    
    
    

    
   
    
    

    
    
}
