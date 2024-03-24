package de.gfed.AoC;

import java.util.*;


public class DayEight {
    boolean debugMode;
    AoCInputConnector inputConnector;

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

        System.out.println("Day 8: " + takeAWalk("AAA", "ZZZ", directions, successor));

        /* Now you are a ghost and start at every element that ends with A. You follow the
        instruction for each element and you are done when every element ends with a Z. How
        many steps do you need now?
         */

        //System.out.println("Day 8 Part 2: " + takeAWalk(evalStartPositions(successor), ".*Z$", directions, successor));
        String[] startNodes = evalStartPositions(successor);
        long result=1;
        for (String startNode : startNodes) {
            result = lcm(result, takeAWalk(startNode, ".*Z$", directions, successor));
        }
        System.out.println("Day 8 Part 2: " + result);

    }

    public void displayResult(){
        inputConnector.setDay(8);
        List<String> input = inputConnector.getInput();
        input=new ArrayList<>(input);

        String directions=input.remove(0);
        input.remove(0);
        Map <String, String[]>successor = mapToSuccessors(input);

        // Sol. 20659
        System.out.println("Day 8 (Exp. 20659): " + takeAWalk("AAA", "ZZZ", directions, successor));

        // Sol. this way takes too long
        /*
        Lets do sort of research
        Find startNodes (STRG+F) and get endNodes + length (println added)
        Find endNodes with this endNodes
        Be puzzled that each endNodes leads to exactly the same node in the same time
        ->result is the least common multiplier of the walks!
         */
        String[] startNodes = evalStartPositions(successor);
        long result=1;
        for (String startNode : startNodes) {
            result = lcm(result, takeAWalk(startNode, ".*Z$", directions, successor));
        }
        System.out.println("Day 8 Part 2 (Exp. 15690466351717): " + result);
    }

    private Map<String, String[]> mapToSuccessors(List<String> input){
        Map<String, String[]>result = new HashMap<>();
        for (String line: input){
            result.put(line.substring(0,3), new String[]{line.substring(7,10),line.substring(12,15)});
        }
        return result;
    }

    private long takeAWalk(String starts, String end, String directions, Map<String, String[]> successor){
        long result=0;
        int directionIndex=0;
        String position=starts;
        while (!position.matches(end)){
                position=successor.get(position)
                        [directions.substring(directionIndex,directionIndex+1).compareTo("L")==0?0:1];
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
    private long lcm(long x, long y) {
        long max = Math.max(x, y);
        long min = Math.min(x, y);
        long lcm = max;
        while (lcm % min != 0) {
            lcm += max;
        }
        return lcm;
    }
}