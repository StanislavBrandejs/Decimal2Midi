/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi.midi.internal;

/**
 *
 * @author Wizard
 */
public class MidiContent {


String content;

public MidiContent(){

content = new String();

}

public String getLength() {

return Integer.toHexString(((content.length())/2));
    
}

public void addEvent(String event) {

content = content + event;
}

    
}
