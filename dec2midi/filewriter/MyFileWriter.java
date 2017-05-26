/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi.filewriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 *
 * @author Wizard
 */
public class MyFileWriter {
 

    public MyFileWriter() {}

public void writeMidiFile(File file, byte[] midiByteArray) throws IOException {

     if (file != null) {
         
        try (OutputStream os = new FileOutputStream(file)) {        
       os.write(midiByteArray);
      os.flush();
       System.out.println("File "+file.getName()+" created sucessfully");
      
    }
                
     }



}

    
    
    
    
}
