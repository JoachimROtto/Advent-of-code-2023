package de.gfed.AoC;

import java.util.*;
import java.util.stream.Collectors;


public class DayNine {
    boolean debugMode;
    AoCInputConnector inputConnector;
    boolean isPart2=false;


    DayNine(boolean debugMode, AoCInputConnector inputConnector) {
        this.debugMode = debugMode;
        this.inputConnector = inputConnector;
    }

    public void displayResults() {
        if ( debugMode )
            displayResultDeb();
        else
            displayResult();
    }


    public void displayResultDeb() {
        List<String> input = Arrays.asList(
                "0 3 6 9 12 15",
                "1 3 6 10 15 21",
                "10 13 16 21 30 45");

        /*
        Your handy Oasis And Sand Instability Sensor produces a report with many
        changing values that you will extrapolate. To do this, make a new sequence
        with the differences between the steps. And a new sequence with this
        sequence.... until the sequence consist of 0. Add a 0 and step back and
        add the last number to each sequence.
        10 13 16 21 30 45
          3  3  5  9  15
            0  2  4  6
              2  2  2
                0  0  0 =>2; =>8 =>23 =>68
         Make a sum of the predictions!
        Example: 18+28+68=114
         */


        System.out.println("Day 8: " + input.stream().mapToLong(this::predictNext).sum());

        /*
        And now backwards: Add a 0 add the beginning and extrapolate (first of line - new first
        of next line)
        10 13 16 21 30 45
          3  3  5  9  15
            0  2  4  6
              2  2  2
                0  0  =>0 ; =>2 =>-2 =>5 =>5
          Again: sum
          Ex. -3
         */
        isPart2=true;
        System.out.println("Day 8 Part 2: " + input.stream().mapToLong(this::predictNext).sum());


    }

    public void displayResult() {
        inputConnector.setDay(9);
        List<String> input = inputConnector.getInput();
        input = new ArrayList<>(input);

        // Sol. 1637452029
       System.out.println("Day 8 (Exp. 1637452029): " + input.stream().mapToLong(this::predictNext).sum());
        isPart2=true;
        //Sol. 908
        System.out.println("Day 8 Part 2 (Exp. 908): " + input.stream().mapToLong(this::predictNext).sum());

    }


    private long predictNext(String line){
        return predictNextRecursion(string2LongList(line));
    }

    private long predictNextRecursion(List<Long> line){
        long result =0;
        if (line.stream().allMatch(val ->val==0)) {
            return 0;
        } else{
            if (!isPart2) {
                return line.get(line.size() - 1) + predictNextRecursion(nextLine(line));
            } else{
                return line.get(0) -predictNextRecursion(nextLine(line));
            }
        }
    }
    private List<Long> nextLine(List<Long> previousLine){
        List<Long> result=new ArrayList<>();
        while (previousLine.size()>1){
            result.add(previousLine.get(1) - previousLine.get(0));
            previousLine.remove(0);
        }
        return result;
    }

    private List<Long> string2LongList(String line){
        return Arrays.stream(line.split(" "))
                .map(Long::parseLong).collect(Collectors.toList());
    }

}