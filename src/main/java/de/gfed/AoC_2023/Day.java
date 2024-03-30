package de.gfed.AoC_2023;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;

public abstract class Day {
    private boolean debugMode;
    AoCInputConnector inputConnector;
    private int day;
    List<String> input;
    List<String> example;
    long[] expectations =new long[]{0,0};

    Day(boolean debugMode, AoCInputConnector inputConnector,int day){
        this.debugMode=debugMode;
        this.inputConnector=inputConnector;
        this.day=day;
    }

    public void displayResults(){
        if (debugMode) {
            input=example;
            displayResultDeb();
        }
        else {
            inputConnector.setDay(day);
            input = inputConnector.getInput();
            displayResult();
        }
    }

    private void displayResultDeb(){
        System.out.println("Day "+ day + ": " + evalInput(false));
        System.out.println("Day "+ day + " Part 2: " + evalInput(true));
    }

    private void displayResult(){
        long result = evalInput(false);
        System.out.println("Day "+ day + " (Exp.:" + expectations[0] + "): " + result + " ->" + (result==expectations[0]));
        result = evalInput(true);
        System.out.println("Day "+ day + " Part 2 (Exp.:" + expectations[1] + "): "+ result + " ->" + (result==expectations[1]));
    }

    protected abstract long evalInput(boolean bPart2);

    protected long[] string2LongArray(String line){
        return Arrays.stream(
                        line.split(" "))
                .mapToLong(Long::parseLong).toArray();
    }

    protected long[] string2FilteredLongArray(String line){
        return Arrays.stream(
                        line.split(" ")).filter(c -> !Objects.equals(c, ""))
                .mapToLong(Long::parseLong).toArray();
    }

    protected List<Long> string2LongList(String line){
        return Arrays.stream(line.split(" "))
                .map(Long::parseLong).collect(Collectors.toList());
    }

}
