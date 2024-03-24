package de.gfed.AoC;

import java.util.*;

public class DaySix {
    boolean debugMode;
    AoCInputConnector inputConnector;

    long[] time;
    long[] distance;

    DaySix(boolean debugMode, AoCInputConnector inputConnector) {
        this.debugMode = debugMode;
        this.inputConnector = inputConnector;
    }

    public void displayResults() {
        if (debugMode)
            displayResultDeb();
        else
            displayResult();
    }

    public void displayResultDeb() {
        List<String> input = Arrays.asList(
                "Time:      7  15   30",
                "Distance:  9  40  200");
        /*
        A time and a record distance are given for a race. The race starts with a time to charge
        the boat (Your choice) and the ride and ends after the given time. The charging time sets
        the speed so the first example may lead to 3 seconds charging time and 4 seconds ride with
        speed 3 and therefor distance 12. A race is won by beating the record. How many variants win.
        Product!
         */

        //Y = (T -C) C / M
        processInput(input);

        //288
        System.out.println("Day 6: " + Arrays.stream(getWonRaces()).reduce(1, (a,b) -> a*b));

        // Oops! Bad kerning. 7 15 30 means 71530

        time = new long[]{Long.parseLong(input.get(0).replaceAll(" ", "").split(":")[1])};
        distance = new long[]{Long.parseLong(input.get(1).replaceAll(" ", "").split(":")[1])};

        //71503
        System.out.println("Day 6 Part 2: "  + Arrays.stream(getWonRaces()).reduce(1, (a,b) -> a*b));

    }

    public void displayResult() {
        inputConnector.setDay(6);
        List<String> input = inputConnector.getInput();

        processInput(input);

        //Sol. 741000
        System.out.println("Day 6 (Exp.:741000): " + Arrays.stream(getWonRaces()).reduce(1, (a,b) -> a*b));

        // Oops! Bad kerning. 7 15 30 means 71530

        time = new long[]{Long.parseLong(input.get(0).replaceAll(" ", "").split(":")[1])};
        distance = new long[]{Long.parseLong(input.get(1).replaceAll(" ", "").split(":")[1])};

        //38220708
        System.out.println("Day 6 Part 2 (Exp.:38220708): "  + Arrays.stream(getWonRaces()).reduce(1, (a,b) -> a*b));

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