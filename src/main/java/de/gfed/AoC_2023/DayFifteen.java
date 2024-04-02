package de.gfed.AoC_2023;

import java.util.*;

public class DayFifteen  extends Day{


    DayFifteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 15);
        expectations=new long[]{513214,102055};
        example = Arrays.asList(
                "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"); // expected: 1320

        /*
        You need to run a Hash-algorithm for some sequences. The algorithm is
        - Start with 0
        - Add ASCII-Code of character
        - multiply with 17 divide by 256, remainder is the start for the next character
        (rn=1 leads to 30)

        Calculate the sum.


          */
    }

    protected long evalInput(boolean bPart2) {
        if(!bPart2)
            return sumHashes(input.get(0));

        return 0;
    }

    private long sumHashes (String line){
        String[]strings = line.split(",");
        return Arrays.stream(strings).mapToLong(this::getHash).sum();
    }

    private int getHash(String element){
        int result =0;
        for (int i=0; i<element.length();i++){
            result=((result + (int) element.charAt(i)) *17)%256;
        }
        return result;
    }
}
