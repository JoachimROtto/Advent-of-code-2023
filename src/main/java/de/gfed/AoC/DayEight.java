package de.gfed.AoC;

import java.util.*;


public class DayEight {
    boolean debugMode;
    AoCInputConnector inputConnector;
    long test = 15690466351717L;

    DayEight(boolean debugMode, AoCInputConnector inputConnector){
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
                "LLR",
                "",
                "AAA = (BBB, BBB)",
                "BBB = (AAA, ZZZ)",
                "ZZZ = (ZZZ, ZZZ)");

        /*
        You have a list of left/right instructions and a list of elements and their left and
        right successor. Start with AAA and follow the instructions (start from their beginning if
        needed) until you reach ZZZ. How many steps have you made?
        Example: 6
         */

        input=new ArrayList<>(input);
        String directions=input.remove(0);
        input.remove(0);
        Map <String, String[]>successor = mapToSuccessors(input);

        System.out.println("Day 8: " + takeAWalk(new String[]{"AAA"}, "ZZZ", directions, successor));

        /* Now you are a ghost and start at every element that ends with A. You follow the
        instruction for each element and you are done when every element ends with a Z. How
        many steps do you need now?
         */

        System.out.println("Day 8 Part 2: " + takeAWalk(evalStartPositions(successor), ".*Z$", directions, successor));


    }

    public void displayResult(){
        inputConnector.setDay(8);
        List<String> input = inputConnector.getInput();
        input=new ArrayList<>(input);

        String directions=input.remove(0);
        input.remove(0);
        Map <String, String[]>successor = mapToSuccessors(input);

        // Sol. 20659
        System.out.println("Day 8: " + takeAWalk(new String[]{"AAA"}, "ZZZ", directions, successor));

        // Sol. this way takes too long
        System.out.println("Day 8 Part 2: " + takeAWalk(evalStartPositions(successor), ".*Z$", directions, successor));

    }

    private Map<String, String[]> mapToSuccessors(List<String> input){
        Map<String, String[]>result = new HashMap<>();
        for (String line: input){
            result.put(line.substring(0,3), new String[]{line.substring(7,10),line.substring(12,15)});
        }
        return result;
    }

    private long takeAWalk(String[] starts, String end, String directions, Map<String, String[]> successor){
        long result=0;
        int directionIndex=0;
        String[] position=starts;
        boolean isEnd=false;
        while (!isEnd){
            isEnd=true;
            for (int i= 0; i<position.length; i++){
                position[i]=successor.get(position[i])
                        [directions.substring(directionIndex,directionIndex+1).compareTo("L")==0?0:1];
                isEnd=isEnd && position[i].matches(end);
            }
            result++;
            directionIndex=(directionIndex+1) % directions.length();
        }
        return result;
    }

    public String[] evalStartPositions(Map<String, String[]> successor){
        List<String> result=new ArrayList<>();
        for (String key: successor.keySet()){
            if (key.endsWith("A"))
                result.add(key);
        }
        return result.toArray(new String[0]);
    }
}