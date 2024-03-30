package de.gfed.AoC_2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayOne extends Day{

    DayOne(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 1);
        expectations=new long[]{54081,54649};
        example = Arrays.asList(
                "2xjzgsjzfhzhm1",
                "qhklfjd39rpjxhqtftwopfvrrj2eight",
                "95btwo");

        //Concat the first and last digits of a line and add them together across multiple lines
        //Part 2: Ooops "eight etc.",... is also a number!

    }

    @Override
    protected long evalInput(boolean bPart2) {
        if (bPart2)
            input.replaceAll(this::replaceWords);
        return input.stream().mapToInt(this::digitsInLine).sum();
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
