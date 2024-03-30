package de.gfed.AoC_2023;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class DayThirteen {
    boolean debugMode;

    AoCInputConnector inputConnector;

    DayThirteen(boolean debugMode, AoCInputConnector inputConnector) {
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
                "..##..##.",
                "..#.##.#.",
                "##......#",
                "##......#",
                "..#.##.#.",
                "..##..##.",
                "#.#.##.#.");

        /*
        You walk through a valley full of sand (.), stones (#) and large mirrors. It is not recognizable
        what is real and what is reflected. You don't want to collide with mirrors but you want to
        orient yourself to them. Find a vertical and horizontal axis of symmetry per block. Correct
        axes reflect to at least one end, not every block has such axes.
        Add all smaller Column number of the vertical axis with 100 times all smaller row number of
        the horizontal one (Ex.: 5 + 100 * 3=305).
         */
        System.out.println("Day 13 (Exp. 305): " + evalInput(input,false));
        /*
        Surprise: Each Mirror has exactly one smudge that flips one position from . to # and vice versa.
        This correction may extend a reflection at the open end, build or change the reflection. Find it,
        correct it and calculate your solution again.
         */
        System.out.println("Day 13 Part 2: " + evalInput(input, true));
    }

    public void displayResult() {
        inputConnector.setDay(13);
        List<String> input = inputConnector.getInput();

        System.out.println("Day 13 (Exp. 27202): " + evalInput(input,false));

        System.out.println("Day 13 Part 2 (Exp. 41566): " + evalInput(input, true));

    }

    private int evalInput(List<String> input, boolean bPart2){
        int result =0;
        List<String> pattern= new ArrayList<>();
        for (int i = 0; i<input.size(); i++)
            if (!input.get(i).isEmpty()){
                pattern.add(input.get(i));
            } else {
                if (bPart2){
                    result+=evalPatternForPart2(pattern);
                } else {
                    result += findVerticalSymmetry(pattern);
                    result += findHorizontalSymmetry(pattern) *100;
                }
                pattern=new ArrayList<>();
            }
        if (bPart2){
            result+=evalPatternForPart2(pattern);
        } else {
            result += findVerticalSymmetry(pattern);
            result += findHorizontalSymmetry(pattern) * 100;
        }
        return result;
    }

    private int evalPatternForPart2(List<String> pattern){
        int result;
        result  = checkPatternForSmudges(pattern) * 100;
        if (result==0)
            result= checkPatternForSmudges(rotateInput(pattern));
        return result;
    }

    private int findVerticalSymmetry(List<String> input){
        return findHorizontalSymmetry(rotateInput(input));
    }

    private int findHorizontalSymmetry(List<String> input){
        AtomicInteger result = new AtomicInteger();
        List<Integer> reflector= new ArrayList<>();
        int formerLine;
        Map <String, Integer> uniqueLines = new HashMap<>();

        for (int i=0; i<input.size(); i++) {
            formerLine = Optional.ofNullable(uniqueLines.put(input.get(i), i)).orElse(i);
            if (formerLine!=i){
                if (i-formerLine ==1){
                    reflector.add(formerLine);
                }
            }}

        if ( reflector.isEmpty() )
            return 0;

        reflector.forEach(reflectorLine ->{
            int resultTemp=checkReflectorLine(input,reflectorLine);
            result.set(resultTemp!=0?resultTemp:result.get());
        });
        return result.get();
    }

    private int checkPatternForSmudges(List<String> input){
        int differentPoint;
        int reflection;
        for (int i=0; i<input.size(); i++){
            for (int j=i+1; j< input.size();j++){
                differentPoint=Arrays.mismatch( input.get(i).toCharArray(), input.get(j).toCharArray() );
                if (differentPoint!=-1){
                    differentPoint=Arrays.mismatch(input.get(i).substring(differentPoint+1).toCharArray(),
                            input.get(j).substring(differentPoint+1).toCharArray());
                    if (differentPoint==-1) {
                        reflection=checkForReflectionInMid(input,i,j);
                        if ( reflection!=0)
                            return reflection;
                    }
                }
            }
        }
        return 0;
    }
    private int checkForReflectionInMid(List <String> input, int i, int j){
        int max=0;
        String saveLine;
        int possibleReflection=Math.floorDiv(j-i, 2)+ i;

        saveLine= input.get(i);
        input.set(i, input.get(j));
        max=Math.max(max, checkReflectorLine(input, possibleReflection));
        input.set(i, saveLine);

        saveLine= input.get(j);
        input.set(j, input.get(i));
        max=Math.max(max, checkReflectorLine(input, possibleReflection));
        input.set(j, saveLine);
        return max;
    }
    private int checkReflectorLine(List<String> input, int reflectorLine){
        for (int i=0; i<Math.min(reflectorLine+1, input.size()-reflectorLine-1); i++){
            if (!input.get(reflectorLine+i+1).equals(input.get(reflectorLine-i))) {
                return 0;
            }
        }
        return reflectorLine+1;
}

    private List<String> rotateInput(List<String> input){
        String line;
        List<String> result = new ArrayList<>();
        StringBuilder[] inputRotated= new StringBuilder[input.get(0).length()];
        for (int i=0; i<input.size(); i++){
            line = input.get(i);
            for (int j=0; j<line.length();j++){
                inputRotated[j]=inputRotated[j]==null?
                        new StringBuilder(String.valueOf(line.charAt(j))):
                        inputRotated[j].insert(0, line.charAt(j));
            }
        }
        for (int i=0; i<inputRotated.length; i++)
            result.add(i, inputRotated[i].toString());

        return result;
    }
}
