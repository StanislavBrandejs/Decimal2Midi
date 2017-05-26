/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi.data.internal;

/**
 *
 * @author Wizard
 */
public class NumberspaceExtensionProvider {
    
    public NumberspaceExtensionProvider() {}
    
                            public static String numberspaceExtension(int number) throws Exception {
                        
                        if(number<=9) { return String.valueOf(number);}
                        
                        if(number<=35){
                        switch(number) {
                        
                            case 10: return "a";
                            case 11: return "b";
                            case 12: return "c";
                            case 13: return "d";
                            case 14: return "e";
                            case 15: return "f";
                            case 16: return "g";
                            case 17: return "h";
                            case 18: return "i";
                            case 19: return "j";
                            case 20: return "k";
                            case 21: return "l";
                            case 22: return "m";
                            case 23: return "n";
                            case 24: return "o";
                            case 25: return "p";
                            case 26: return "q";
                            case 27: return "r";
                            case 28: return "s";
                            case 29: return "t";
                            case 30: return "u";
                            case 31: return "v";
                            case 32: return "w";
                            case 33: return "x";
                            case 34: return "y";
                            case 35: return "z";
                            default: return null;
                            
                        }} else {throw new Exception("Extension for higher bases not supported");}
                        
                                
                           
                        
                        
                        }
    
    
}
