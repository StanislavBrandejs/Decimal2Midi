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
public class HexStringToByteArray {
    
    
    public HexStringToByteArray() {}
    
    public static byte[] convertHexStringToByteArray(String s) {
        

        System.out.println("Hex String to convert into byteArray in total length of ("+s.length()+") characters : " + s );
        
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len-1; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}
    
    
}
