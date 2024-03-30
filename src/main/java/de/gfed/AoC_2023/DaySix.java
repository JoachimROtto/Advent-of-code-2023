package de.gfed.AoC_2023;

import java.util.*;

public class DaySix extends Day{
    long[] time;
    long[] distance;

    DaySix(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 6);
        expectations=new long[]{741000,38220708};
        example =  Arrays.asList(
                "Time:      7  15   30",
                "Distance:  9  40  200");
        /*
        A time and a record distance are given for a race. The race starts with a time to charge
        the boat (Your choice) and the ride and ends after the given time. The charging time sets
        the speed so the first example may lead to 3 seconds charging time and 4 seconds ride with
        speed 3 and therefor distance 12. A race is won by beating the record. How many variants win.
        Product!

        Y = (T -C) C / M

        Part 2:
        Oops! Bad kerning. 7 15 30 means 71530
         */

    }

    protected long evalInput(boolean bPart2) {
        processInput(input);
        if (!bPart2)
            return  Arrays.stream(getWonRaces()).reduce(1, (a,b) -> a*b);

        time = new long[]{Long.parseLong(input.get(0).replaceAll(" ", "").split(":")[1])};
        distance = new long[]{Long.parseLong(input.get(1).replaceAll(" ", "").split(":")[1])};

        return Arrays.stream(getWonRaces()).reduce(1, (a,b) -> a*b);
    }

    private int[] getWonRaces(){
        int [] result = new int[time.length];
        int count;
        for (int i = 0; i<time.length;i++){
            count=0;
            for (int j=0; j<=time[i]; j++){
                if ((time[i] - j)*j>distance[i])
                    count++;
            }
            result[i] =count;
        }
        return result;
    }

    private void processInput(List <String> input){
        time= string2LongArray(input.get(0).substring(input.get(0).indexOf(":")+2));
        distance= string2LongArray(input.get(1).substring(input.get(1).indexOf(":")+2));
    }

    private long[] string2LongArray(String line){
        return Arrays.stream(
                        line.split(" ")).filter(c -> !Objects.equals(c, ""))
                .mapToLong(Long::parseLong).toArray();
    }

}
