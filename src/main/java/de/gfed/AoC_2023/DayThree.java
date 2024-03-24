package de.gfed.AoC_2023;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DayThree {
    boolean debugMode;
    AoCInputConnector inputConnector;

    DayThree(boolean debugMode, AoCInputConnector inputConnector){
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
                "........*.=..............588.....*786......$.........*........*.......390*.....886...*....227...728..852.......606....*863.......916..396...",
                ".....538...287................301........133.....539..........33.537......466..$...793....*........*...............218.....721........*.....",
                "...............986.$.......=.......................................*...%...............222..-.......701.271...............#.........437.....");


        /*
         A visual representation of some engine consists of lots of numbers and symbols.
         Any number adjacent to a symbol, even diagonally, is a "part number" and should
         be included in your sum.

         .......497...........................858...923...128..................227..801........487.....664...........................................
         436........765...................140*.....................859.............*.........*.................960........668........................
         ...............986.$.......=.......................................*...%...............222..-.......701.271...............#.........437.....

         leads to 858 + 801 + 487 + 140 + 222
         */
        List<Integer[]> listOfNumbers = buildListOfNumbers(input);
        List<Integer[]> listOfSymbols = buildListOfSymbols(input, "[^.0-9]");

        System.out.println("Day 3: " + sumPartNumbers(listOfNumbers, listOfSymbols));

        List<Integer[]> listOfAsterisks = buildListOfSymbols(input, "[*]");

        System.out.println("Day 3 Part 2: " + sumGearRatio(listOfNumbers, listOfAsterisks));

    }

    public void displayResult(){
        inputConnector.setDay(3);
        List<String> input = inputConnector.getInput();

        //List: number, row, col
        List<Integer[]> listOfNumbers = buildListOfNumbers(input);
        //List symbol: row, col
        List<Integer[]> listOfSymbols = buildListOfSymbols(input, "[^.0-9]");

        //Sol. 540131
        System.out.println("Day 3 (Exp.:540131): "+ sumPartNumbers(listOfNumbers, listOfSymbols));

        /*
            And a "gear" consists of two numbers sharing the same asterisk with their product
            as gear-ratio. Add them up!
            Ex. above leads to 487 * 222 + 858 * 140
         */
        //List asterisks: row, col
        List<Integer[]> listOfAsterisks = buildListOfSymbols(input, "[*]");

        //Sol. 86879020
        System.out.println("Day 3 Part 2 (Exp.:86879020): " + sumGearRatio(listOfNumbers, listOfAsterisks));

    }

    private  List<Integer[]> buildListOfNumbers(List<String> input) {
        List<Integer[]> result = new ArrayList<>();
        Pattern digitRegex = Pattern.compile("\\d+");
        String line;
        for (int i = 0; i < input.size(); i++) {
            line = input.get(i);
            Matcher digitMatcher = digitRegex.matcher(line);
            while (digitMatcher.find()) {
                Integer[] item = {Integer.parseInt(line.substring(digitMatcher.start(), digitMatcher.end()))
                        , i, digitMatcher.start()};
                result.add(item);
            }
        }
        return result;
    }

    private  List<Integer[]> buildListOfSymbols(List<String> input, String regEx) {
        List<Integer[]> result = new ArrayList<>();
        Pattern digitRegex = Pattern.compile(regEx);
        String line;
        for (int i = 0; i < input.size(); i++) {
            line = input.get(i);
            Matcher digitMatcher = digitRegex.matcher(line);
            while (digitMatcher.find()) {
                Integer[] item = {i, digitMatcher.start()};
                result.add(item);
            }
        }
        return result;
    }

    private  int sumPartNumbers(List<Integer[]> listOfNumbers, List<Integer[]> listOfSymbols) {
        int sum = 0;
        //O(n^2)!!
        for (Integer[] listOfNumber : listOfNumbers) {
            int start = listOfNumber[2] - 1;
            int end = listOfNumber[2] + String.valueOf(listOfNumber[0]).length() + 1;
            if (!correspondingSymbol(start, end, listOfNumber[1], listOfSymbols).isEmpty()) {
                sum += listOfNumber[0];
            }
        }
        return sum;
    }

    private  AtomicInteger sumGearRatio(List<Integer[]> listOfNumbers, List<Integer[]> listOfSymbols) {
        AtomicInteger sum = new AtomicInteger();
        Map<String, Integer> ratios= new HashMap<>();
        //O(n^2)!!
        for (Integer[] listOfNumber : listOfNumbers) {
            int start = listOfNumber[2] - 1;
            int end = listOfNumber[2] + String.valueOf(listOfNumber[0]).length() + 1;
            // Assumption: The assignment of a symbol to two numbers is clear
            correspondingSymbol(start, end, listOfNumber[1], listOfSymbols).forEach(element ->{
                    if (ratios.get(element[0] + "/" + element[1])!=null){
                        sum.addAndGet(listOfNumber[0] * ratios.get(element[0] + "/" + element[1]));
                    }
                    else {
                        ratios.put(element[0] + "/" + element[1], listOfNumber[0]);
                    }
            });
            }
        return sum;
    }

    private  List<Integer[]> correspondingSymbol(int start, int end, int i, List<Integer[]> listOfSymbols){
        List<Integer[]> relevantPartOfSymbols = listOfSymbols.stream()
                .filter(item -> item[0]==i | item[0]-1==i | item [0]+1 ==i)
                .toList();
        return relevantPartOfSymbols.stream()
                .filter(item -> item[1] >start-1 && item[1]< end)
                .toList();
    }


}