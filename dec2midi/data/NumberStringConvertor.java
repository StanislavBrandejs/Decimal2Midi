/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi.data;
 
import dec2midi.data.internal.NumberspaceExtensionProvider;
import java.math.BigDecimal;

/**
 *
 * @author Wizard
 */
public class NumberStringConvertor {
    
    public final static int MAXITERATIONS = 7936;
    public final static int DEFAULT_ITERATIONS = 1024;
    private int iterationCount;

    
    public NumberStringConvertor() {
      this.iterationCount = DEFAULT_ITERATIONS;
    }
    
   
    public NumberStringConvertor(int iterationCount) {
        if(iterationCount>=0 && iterationCount<=MAXITERATIONS){
    this.iterationCount = iterationCount;}
        else {this.iterationCount = DEFAULT_ITERATIONS;}
    
    }
    
    

    public StringNumberOctal decadic2Octal(StringNumber sn) throws Exception {
 
        return new StringNumberOctal(sn.getName(), decadicStringConversion(sn, 8, getIterationCount()));
    }
    
    public StringNumberDuodecadic decadic2DuoDecadic(StringNumber sn) throws Exception {
           
         return new StringNumberDuodecadic(sn.getName(), decadicStringConversion(sn, 12, getIterationCount()));
    }
    

        private String decadicStringConversion(StringNumber dn, int base, int iterationCount) throws Exception {
            
            long startTime = System.nanoTime();
            System.out.println();
            System.out.println("Conversion of number " + dn.getName() + " to base ("+base + ") begun...");
            
            String[] dnp = dn.getSplittedParts();
            if(dnp.length == 1) {     
           String output = wholePartConversion(dn.getWholePart(), base);
            long endTime = System.nanoTime();
           double duration = (endTime-startTime) / (double) 1000000;
           System.out.println("...Conversion finished with duration of "+duration+" milis.");
           return output;
            }
           
           if(dnp.length == 2) {    
               String output = String.join(".", wholePartConversion(dn.getWholePart(), base),decimalPartConversion(dn.getDecimalPart(), base, iterationCount));
           long endTime = System.nanoTime();
           double duration = (endTime-startTime) / (double) 1000000;
           System.out.println("...Conversion finished with duration of "+duration+" milis.");
               return output; }

            return null;  }
        
        
        
            private String wholePartConversion(String wholePart, int base) {
    
                      try {
         Long l = Long.parseLong(wholePart);
         return Long.toString(l, base); 
         
      } catch (NumberFormatException nfe) {
         System.out.println("NumberFormatException: " + nfe.getMessage());
                       
      }
                      
return "0";
    }
                                 
            private String decimalPartConversion(String decimalPart, int base, int iterationCount) throws Exception {
      
                            BigDecimal bDdP = new BigDecimal("0."+decimalPart);
                            BigDecimal bDBase = new BigDecimal(base);
                            
                        //    System.out.println("bDdP=" + bDdP + " bDBase="+bDBase);
                            String convertedDecimal = "";
                            
                            for (int i = 0; i < iterationCount; i++) {

                                if(bDdP.compareTo(BigDecimal.ZERO)==0){return convertedDecimal;}
                                BigDecimal nextChunk = bDdP.multiply(bDBase);
                                int number =  nextChunk.intValue();
                                bDdP = nextChunk.subtract(new BigDecimal(number));
                                 convertedDecimal = convertedDecimal + NumberspaceExtensionProvider.numberspaceExtension(number);
                              //   System.out.println("nextChunk="+nextChunk+ " number="+number+ "bDdP=" +bDdP+"convertedDecimal="+convertedDecimal);
                                 
                            }
                            return convertedDecimal;
    }

                /**
     * @return the max iterationCount
     */
    public  int getMaxIterations() {
        return MAXITERATIONS;
    }
            
    /**
     * @return the iterationCount
     */
    public  int getIterationCount() {
        return iterationCount;
    }

    /**
     * @param aIterationCount the iterationCount to set
     */
    public  void setIterationCount(int aIterationCount) {
        
        if (aIterationCount<=MAXITERATIONS && aIterationCount >=0) {
        iterationCount = aIterationCount;    
        }
        
    }

    
}
