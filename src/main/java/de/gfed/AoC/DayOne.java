package de.gfed.AoC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayOne {
    boolean debugMode;
    AoCInputConnector inputConnector;

    DayOne(boolean debugMode, AoCInputConnector inputConnector){
        this.debugMode=debugMode;
        this.inputConnector=inputConnector;
    }

    public void displayResults(){
        if (debugMode)
            displayResultDeb();
        else
            displayResult();
    }


    public void displayResultDeb(){
        List<String> input = Arrays.asList(
                "2xjzgsjzfhzhm1",
                "qhklfjd39rpjxhqtftwopfvrrj2eight",
                "95btwo");
        System.out.println("Day 1: " + input.stream().mapToInt(this::digitsInLine).sum());

        input.replaceAll(this::replaceWords);
        System.out.println("Day 1 Part 2: " + input.stream().mapToInt(this::digitsInLine).sum());
    }

    public void displayResult(){
        inputConnector.setDay(1);
        List<String> input=inputConnector.getInput();

        //Concat the first and last digits of a line and add them together across multiple lines
        //Sol: 54081
        System.out.println("Day 1 (Exp.:54081): " + input.stream().mapToInt(this::digitsInLine).sum());

        //Part 2: Ooops "eight",... is also a digit!
        //Sol: 54649
        input.replaceAll(this::replaceWords);
        System.out.println("Day 1 Part 2 (Exp.:54649): " + input.stream().mapToInt(this::digitsInLine).sum());
    }

      private  int digitsInLine(String line){
        Pattern digitRegex = Pattern.compile("\\d");
        Matcher digitMatcher = digitRegex.matcher(line);
        if (!digitMatcher.find())
            return 0;
        int firstDigit= Integer.parseInt(digitMatcher.group().substring(0,1));
        int lastDigit = firstDigit;
        while (digitMatcher.find()) {
            lastDigit=Integer.parseInt(digitMatcher.group().substring(0,1));
        }
        return firstDigit * 10 + lastDigit;
    }
    private  String replaceWords(String line){
        Map<String, String> dict= new HashMap<>();
        //The numbers can be combined ("oneight"), so the beginning and the end must (sometimes) be saved
        dict.put("one", "o1e");
        dict.put("two", "t2o");
        dict.put("three", "t3e");
        dict.put("four", "f4r");
        dict.put("five", "f5e");
        dict.put("six", "s6x");
        dict.put("seven", "s7n");
        dict.put("eight", "e8t");
        dict.put("nine", "n9e");

        String result = line;
        for (Map.Entry<String, String> entry : dict.entrySet()) {
            result = result.replaceAll(entry.getKey(), entry.getValue());
        }
        return result;
    }


}
