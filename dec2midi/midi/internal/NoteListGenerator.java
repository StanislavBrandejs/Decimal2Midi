/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi.midi.internal;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wizard
 */
public class NoteListGenerator {
    
    
    //konstants
    
         public enum Scale {
    MAJOR, DORIAN, PHRYGIAN, LYDIAN, MIXOLYDIAN, MINOR, LOCRIAN
};
     
     public enum RootKey {
     C, CIS, D, DIS, E, F, FIS, G, GIS, A, AIS, B
     }
     
     public enum Extra {BASIC, DUAL, TRIPLE, QUAD, ARPEGIATO2, ARPEGIATO4 }
     
     
     public static final int FULLNOTE = 10;    //16ticks
     public static final int DOUBLENOTE = 20;  //32 ticks
     public static final int QUADNOTE = 40;    //64 ticks
     public static final int HALFNOTE = 8;     //8 ticks
     public static final int QUARTERNOTE = 4;  //4 ticks
     
     
     
     //variables
  
    private int noteDuration;
        private int octave;
        private RootKey rootKey;
        private Scale scale; 
        private Extra extra;
        
        private int arpPosition;
        private int arpIncrement;
        private int arpOctaveSpan;


public NoteListGenerator(int noteDuration, int octave, Scale scale, RootKey rootKey, Extra extra){

    checkAndSetNoteDuration(noteDuration);
    if (octave>=0 && octave <= 8){
    this.octave = octave; } else this.octave = 3;
    this.scale = scale; 
    this.rootKey = rootKey;
    this.extra = extra;
   
initArp();

    
}

public NoteListGenerator(){
this.noteDuration = FULLNOTE;
    this.octave = 3;
    this.scale = Scale.MAJOR; 
    this.rootKey = RootKey.C;
    this.extra = Extra.BASIC;
    
    initArp();


}

private void initArp() {
            this.arpOctaveSpan = 4;
        this.arpIncrement = 12;
        this.arpPosition= 0;
}

private void initArp(int arpIncrement, int arpOctaveSpan) {
this.arpIncrement = arpIncrement;
this.arpOctaveSpan = arpOctaveSpan;
this.arpPosition= 0;
}





public String octalNumberStringToNoteEvents(String octal) {
 String octStr = octal.replaceAll("[^0-7]","");

    switch (getScale()) {
        case MAJOR: { String[] labelSet = {"0","2","4","5","7","9","b","c"} ; octStr = relabelByScale(octStr,labelSet); break; }
        case DORIAN: { String[] labelSet = {"0","2","3","5","7","9","a","c"} ; octStr = relabelByScale(octStr,labelSet); break; }      
        case PHRYGIAN: { String[] labelSet = {"0","2","3","5","7","8","a","c"} ; octStr = relabelByScale(octStr,labelSet); break; }
        case LYDIAN: { String[] labelSet = {"0","2","4","6","7","9","b","c"} ; octStr = relabelByScale(octStr,labelSet); break; }
        case MIXOLYDIAN: { String[] labelSet = {"0","2","4","5","7","9","a","c"} ; octStr = relabelByScale(octStr,labelSet); break; }
        case MINOR: { String[] labelSet = {"0","2","3","5","7","8","a","c"} ; octStr = relabelByScale(octStr,labelSet); break; }
        case LOCRIAN: { String[] labelSet = {"0","1","3","5","6","8","a","c"} ; octStr = relabelByScale(octStr,labelSet); break; }
        
        default:
            throw new AssertionError();
        
        }
    
     return getNoteEvents(transformToIntString(octStr));
            
    
        
    }

private String relabelByScale(String octStr, String[] relabels){

    
    octStr = octStr.replaceAll("7", relabels[7]);
    octStr =     octStr.replaceAll("6", relabels[6]);
    octStr =     octStr.replaceAll("5", relabels[5]);
    octStr =     octStr.replaceAll("4", relabels[4]);
    octStr =     octStr.replaceAll("3", relabels[3]);
    octStr =     octStr.replaceAll("2", relabels[2]);
    octStr =     octStr.replaceAll("1", relabels[1]);
    octStr =     octStr.replaceAll("0", relabels[0]);
        return octStr;

}
 
/**
 * Returns a String containing note events as specified in MIDI file.
 * Input string requires duodecadic number in String format.
 * <p>
 *
 * @param  duoDec  input duodecadic number: 0123456789ab
 * @return      String with midi note events.
 * @see         String
 */
  public String duoDecadicNumberStringToNoteEvents(String duoDec) {
  
  String duoDecStr = duoDec.replaceAll("[^0-9ab]","");
  
        
     return getNoteEvents(transformToIntString(duoDecStr)); }
  
  
  
  
    private List<Integer> transformToIntString(String string) {
      
        List<Integer> intList = new ArrayList<>(); 
        int incrAmount = (getOctave()*12)+getRootKeyInt();
        System.out.println("NoteList generator-Incrementation Amout: "+incrAmount);
      for(int i = 0, n = string.length() ; i < n ; i++) { 
            
          switch(String.valueOf(string.charAt(i))) {
                      case "0":  intList.add(0+incrAmount); 
                      break;
                      case "1":  intList.add(1+incrAmount);
                      break;
                      case "2":  intList.add(2+incrAmount);
                      break;
                      case "3":  intList.add(3+incrAmount);
                      break;
                      case "4":  intList.add(4+incrAmount);
                      break;
                      case "5":  intList.add(5+incrAmount);
                      break;
                      case "6":  intList.add(6+incrAmount);
                      break;
                      case "7":  intList.add(7+incrAmount);
                      break;
                      case "8":  intList.add(8+incrAmount);
                      break;
                      case "9":  intList.add(9+incrAmount);
                      break;
                      case "a":  intList.add(10+incrAmount);
                      break;
                      case "b":  intList.add(11+incrAmount);
                      break;
                      case "c":  intList.add(12+incrAmount);
          
          } }
  
  return intList;
  }

      
  
  private String getNoteEvents(List<Integer> intList) {
     String noteEvents = ""; 
  
      switch (extra) {
          case BASIC: 
               for (Integer integer : intList) {noteEvents += getBasicNote(integer); }
               System.out.println("Note event generation - Basic mode");
              break; 
              case ARPEGIATO4: 
              initArp();
              for (Integer integer : intList) {noteEvents += getArpegiatedNote(integer); }     
              System.out.println("Note event generation - Arp 4 mode");
              break; 
              case ARPEGIATO2:
               initArp(12, 2);
              for (Integer integer : intList) {noteEvents += getArpegiatedNote(integer); }
              System.out.println("Note event generation - Arp 2 mode");
                break; 
                
              case DUAL:
                  for (int i = 0; i < (intList.size()-1); i+=2) {
                          List<Integer> chordList = new ArrayList<>();
                          chordList.add(intList.get(i));
                          chordList.add(intList.get(i+1));                      
                      noteEvents += getChord(chordList);            
                  }
                  System.out.println("Note event generation - Dual mode");
                    break; 
                    
                  case TRIPLE:
                  for (int i = 0; i < (intList.size()-2); i+=3) {
                     List<Integer> chordList = new ArrayList<>();
                          chordList.add(intList.get(i));
                          chordList.add(intList.get(i+1));
                           chordList.add(intList.get(i+2));
                      noteEvents += getChord(chordList); 
                      
                  }
                  System.out.println("Note event generation - Triple mode");
                    break; 
                    
                   case QUAD:
                  for (int i = 0; i < (intList.size()-3); i+=4) {
                   List<Integer> chordList = new ArrayList<>();
                          chordList.add(intList.get(i));
                          chordList.add(intList.get(i+1));
                           chordList.add(intList.get(i+2));
                            chordList.add(intList.get(i+3));
                      noteEvents += getChord(chordList);   
                      
                  }
                       System.out.println("Note event generation - Quad mode");
             
              break;
              
          default:
              throw new AssertionError();
      }
     
 
 
        return noteEvents;
  }
  
  private String getArpegiatedNote(Integer integer) {
      
      String noteEvent;
       int arpInteger = integer+(arpPosition*arpIncrement);
              
             /*  System.out.println("integer value:"+integer+"arpInteger:"+arpInteger+"arpPosition:"+arpPosition+"arpIncrement"+arpIncrement);*/
              
              if(arpInteger< 127) { integer = arpInteger; }
           /*   System.out.println("new integer:"+integer);*/
                  
           noteEvent = getBasicNote(integer);             
              arpPosition++;
            if(arpPosition>=(arpOctaveSpan)){ arpPosition = 0;  }
  
           return noteEvent;
  
  }
  
  private String getChord(List<Integer> integers) {
  
      String notesOn = "";
      String notesOff = "";
      boolean firstNote = true;
      for (Integer integer : integers) {
         String noteTone = Integer.toHexString(integer);
          
            String leadingZerosNoteTone = ("00" + noteTone).substring(noteTone.length());
    
      notesOn += "00"  + //delta
                      "90" + //note on
                  leadingZerosNoteTone +
              "7f";  // velocity
       
      if(firstNote){
      notesOff  += String.format("%02d", getNoteDuration()) + //delta
                      "80" + //note off
                  leadingZerosNoteTone +
                    "00";  // velocity
      firstNote =false;
      } else {
      notesOff  += "00" + //delta
                      "80" + //note off
                  leadingZerosNoteTone +
                    "00";  // velocity
       }
      }
  return notesOn + notesOff;
  
  }
 
  private String getBasicNote(int integer){
      String noteTone = Integer.toHexString(integer);
String leadingZerosNoteTone = ("00" + noteTone).substring(noteTone.length());
    
      String noteOn = "00"  + //delta
                      "90" + //note on
                  leadingZerosNoteTone +
              "7f";  // velocity
       
      String noteOff  = String.format("%02d", getNoteDuration()) + //delta
                      "80" + //note off
                  leadingZerosNoteTone +
                    "00";  // velocity

  return noteOn + noteOff;

  }

  private int getRootKeyInt(){
  
      switch (getRootKey()) {
          case C: return 0;
          case CIS: return 1;
          case D: return 2;
          case DIS: return 3;
          case E: return 4;
          case F: return 5;
          case FIS: return 6;
          case G: return 7;
          case GIS: return 8;
          case A: return 9;
          case AIS: return 10;
          case B: return 11;
          default:
              throw new AssertionError();
      }
  }
 
    /**
     * @return the noteDuration
     */
    public int getNoteDuration() {
        return noteDuration;
    }
    
            public String getNoteDurationString() {
            switch (noteDuration) {
                case QUARTERNOTE:
                    return "Quarter";
                    case HALFNOTE: return "Half";
                    case FULLNOTE: return "Full";
                    case DOUBLENOTE: return "Double";
                    case QUADNOTE: return "Quad";
                    
                default:
                    throw new AssertionError();
            }
        
        }
            
    /**
     * @param noteDuration the noteDuration time to set. Has to be QUARTERNOTE (4) ||  HALFNOTE (8) ||  FULLNOTE (10) ||  DOUBLENOTE(20) || QUADNOTE (40)
     */
    public void setNoteDuration(int noteDuration) {
        checkAndSetNoteDuration(noteDuration);
    
    }
    
                public void setNoteDurationString(String noteDurationString) {
            
                    
                switch (noteDurationString) {
                    case "Quarter": noteDuration = QUARTERNOTE; break;
                    case "Half": noteDuration = HALFNOTE; break;
                    case "Full": noteDuration = FULLNOTE; break;
                    case "Double": noteDuration = DOUBLENOTE; break;
                    case "Quad": noteDuration = QUADNOTE; break;
                    default:
                        throw new AssertionError();
                }
            
            }
    
    private void checkAndSetNoteDuration(int noteDuration){
        if(noteDuration == QUARTERNOTE || noteDuration == HALFNOTE || noteDuration == FULLNOTE || noteDuration == DOUBLENOTE || noteDuration == QUADNOTE){    
        this.noteDuration = noteDuration;} else throw new AssertionError();
    }

    /**
     * @return the octave
     */
    public int getOctave() {
        return octave;
    }

    /**
     * @param octave the octave to set
     */
    public void setOctave(int octave) {
            if (octave>=0 && octave <= 8){
    this.octave = octave; } else this.octave = 3;
    }

    /**
     * @return the rootKey
     */
    public RootKey getRootKey() {
        return rootKey;
    }

    /**
     * @param rootKey the rootKey to set
     */
    public void setRootKey(RootKey rootKey) {
        this.rootKey = rootKey;
    }

    /**
     * @return the scale
     */
    public Scale getScale() {
        return scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(Scale scale) {
        this.scale = scale;
    }

    /**
     * @return the extra
     */
    public Extra getExtra() {
        return extra;
    }

    /**
     * @param extra the extra to set
     */
    public void setExtra(Extra extra) {
        this.extra = extra;
    }
  
    
}
