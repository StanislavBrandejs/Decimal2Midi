/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi.midi;


import dec2midi.data.StringNumber;
import dec2midi.data.StringNumberDuodecadic;
import dec2midi.data.StringNumberOctal;
import static dec2midi.midi.internal.HexStringToByteArray.convertHexStringToByteArray;
import dec2midi.midi.internal.NoteListGenerator;



/**
 *
 * @author Wizard
 * 
 * 
 * 
 *  notes:
  String keySigEvent is hardcoded in C major for now. 
 */
public class MidiEngine {
    
    

   private String tempo ; 
    private String hexString = null;
    private byte[] output = null;
    NoteListGenerator nlg;

   public MidiEngine() {
        this.tempo = "0F4240"; 
       nlg = new NoteListGenerator();
   }
   
   public NoteListGenerator getNoteListGenerator() {
   return nlg;
   }
    
    /**
     * @return the output
     */
    public byte[] getOutput() {
        return output;
    }
    
    public String getHexString() {return hexString;}


    
    public void setTempo(double tempo) throws Exception{
     
        
        if (tempo < 0 || tempo > 1) {throw new Exception("tempo out of bounds."); }
      /*  System.out.println("tempo:"+tempo);*/
        
        long tempoL = (long)  (100000+( (1-tempo)*1900000));
     /*   System.out.println("tempoL:" + tempoL); */
        String tempoS = Long.toHexString(tempoL);
        
        this.tempo = ("000000" + tempoS).substring(tempoS.length()) ;

    }
    
    public String getTempo(){
    return tempo;
    }

public void generateMidiHexString(StringNumber input, String trackText) throws Exception {

    System.out.println();
    System.out.println("Checking class of input String number : "+input.getClass().getName());
    System.out.println();
    String noteEvents = getNoteEvents(input);
    String content = getMidiMetadata(trackText) + noteEvents + getFooter();
    
    /* ONLY FOR DEBUGIN 
    System.out.println("header:" + getMidiFileHeader() + "length:" +getMidiFileHeader().length());
    System.out.println("trackheader:" + getMidiTrackHeader(content.length()) + "length:" +getMidiTrackHeader(content.length()).length());
    System.out.println("content:" +content + "length:" +content.length());
    System.out.println("content length:" +content.length());
    System.out.println("getNoteEv:" + noteEvents + "length:" +noteEvents.length());
    System.out.println("getfooter:" + getFooter() + "length:" +getFooter().length());
*/
    
    hexString = getMidiFileHeader() + getMidiTrackHeader(content.length()) + content;
    generateByteArrayFromHexString();


}

    private String getNoteEvents(StringNumber sn) throws Exception {
      
        if(sn instanceof StringNumberDuodecadic) {     
               System.out.println("Midi data generation began in duodecadic mode...");
               return nlg.duoDecadicNumberStringToNoteEvents(sn.getValue());
         
        }
        if(sn instanceof StringNumberOctal) {      
               System.out.println("Midi data generation began in octal mode...");
               return nlg.octalNumberStringToNoteEvents(sn.getValue());
              
        }
        
  return null;
  }




private void generateByteArrayFromHexString() throws Exception { 

    if(hexString != null) {
    output = convertHexStringToByteArray(hexString);
        System.out.println("...Output Byte array ready for file write.");
    } else throw new Exception("Hex string is empty");
}
        

private static String getMidiFileHeader() {

 // header_chunk = "MThd" + <header_length> + <format> + <ntrks> + <division>   
    
 String mThd = "4D546864";
 String mTHeaderLength ="00000006";
 String mTFormat ="0000"; //0-the file contains a single multi-channel track
  // String mTFormat ="0001"; // 1-the file contains one or more simultaneous tracks (or MIDI outputs) of a sequence
  // String mTFormat ="0002"; // 2-the file contains one or more sequentially independent single-track patterns 
 
 String mTNumberOfTracks = "0001";
 // x0xx xxxx xxxx xxxx - delta time in ticks
 String mTDivision = "0020"; // 32 ticks per quarter note
 
String headerChunk = mThd + mTHeaderLength + mTFormat + mTNumberOfTracks + mTDivision;  

return headerChunk;
}

private String getMidiTrackHeader(int length) throws Exception {

    int high = (length/2) / 256;
    int low = (length/2) - (high * 256);
            
    if(high>=256) throw new Exception("Midi content too long.");
    
    System.out.println("Midi content length: "+String.valueOf(length)+" - High Byte: "+high+" ("+Integer.toHexString(high)+"h)  Low Byte:"+low+" ("+Integer.toHexString(low)+"h)");
    // MTrk 
//length has to count all bytes beyond mtrk

String trackChunk = "4D54726b";

if(high<=9) {  trackChunk += "0000" + "0" + Integer.toHexString(high) + Integer.toHexString(low);
} else {       trackChunk +=  "0000" + Integer.toHexString(high) + Integer.toHexString(low);
}
    
return trackChunk;
}




  private String getMidiMetadata(String trackText) throws Exception {
    
      
      String hexText = asciiToHex(trackText);
      
      String textLength = Integer.toHexString(trackText.length()); 
         if(textLength.length() <= 1) {textLength = "0"+textLength;}
         if (textLength.length() > 2) throw new Exception("Text too long.");
         
      
 String name = "00FF03" + textLength + hexText;
 
      System.out.println("Midi track text event in hexa: 00FF03" + "("+ textLength + ")" + "("+ hexText + ")");
 
      // A MIDI event to set the tempo
  String tempoEvent = "00FF"+"5103"+tempo; // Default 1 million usec per crotchet
// A MIDI event to set the key signature. This is irrelent to
  //  playback, but necessary for editing applications 
                              //  C   Major  hardcoded (WILL BE FIXED IN FUTURE RELEASES) 
  String keySigEvent = "00FF"+"5902"+"00"+"00";
   // A MIDI event to set the time signature. This is irrelent to
  //  playback, but necessary for editing applications 

  String timeSigEvent = "00FF"+"5804" 
          +"04" // nn - numerator
          +"02" // dd - denominator
          +"30" // cc - number of midi clocks in click
          +"08"; // bb - number of notated 32nd notes in quarter note (24click/32)
 
 // return  tempoEvent + keySigEvent + timeSigEvent;
 return name + timeSigEvent + tempoEvent + keySigEvent;
 //return name + tempoEvent + keySigEvent + timeSigEvent;
}
  

    // Standard footer
  private String getFooter()
     {
    return "01FF2F00";
     }
  
  
  private static String asciiToHex(String asciiValue)
{
    char[] chars = asciiValue.toCharArray();
    StringBuilder hex = new StringBuilder();
    for (int i = 0; i < chars.length; i++)
    {
        hex.append(Integer.toHexString((int) chars[i]));
    }
    return hex.toString();
}


    
}
