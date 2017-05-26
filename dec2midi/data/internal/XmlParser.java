/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi.data.internal;

import dec2midi.data.StringNumber;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Wizard
 */
public class XmlParser {
    
    
    public XmlParser() {}
    
    
     public static List<StringNumber> parseXmlDataAndReturnNumberList(){
    //lolz
        System.out.println("XML data parsing commenced...");

        try {

            List<StringNumber> stringNumberList = new ArrayList<>();
              

        InputStream xmlData = dec2midi.Dec2Midi.class.getResourceAsStream("resources/xmlData.xml");  
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlData);
       
         //   System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            
            
            NodeList nameList = doc.getElementsByTagName("number");
            for (int i = 0; i < nameList.getLength(); i++) {
 
         //   System.out.println( "name: " + nameList.item(i).getNodeName());

            if(nameList.item(i).getChildNodes().getLength()>0) {         
             String numberName = null, numberValue = null;
                    
         //   System.out.println("not empty node");
            
                
                for (int j = 1; j < nameList.item(i).getChildNodes().getLength(); j++) {
             
                    if (nameList.item(i).getChildNodes().item(j).getNodeName().equals("name")) {
                        numberName = nameList.item(i).getChildNodes().item(j).getTextContent();
                    }
                    if (nameList.item(i).getChildNodes().item(j).getNodeName().equals("value")) {
                        numberValue = nameList.item(i).getChildNodes().item(j).getTextContent();
                    }
                  
                }
                
                if (numberName != null & numberValue != null)
                
                { stringNumberList.add(new StringNumber(numberName, numberValue));
                    System.out.println("New number called " + numberName +" used as new StringNumber object");                      
                }
                
                
                
            }  else {
           //   System.out.println("empty node");
            }
       
            }
            
            System.out.println("...Parsing completed (" + stringNumberList.size() + ") xml <number> entries returned as objects.");
            
            return stringNumberList;

            }
  
        
         catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
            
        }
        
        System.out.println("Something went horribly wrong,...no list over here, no list over there.");
        return null;
        
    }
    
}
