package de.gfed.AoC;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class DayTwo {
    static Map<String, Integer> maxDice  = new HashMap<String, Integer>() {{
        put("red", 12);
        put("green", 13);
        put("blue", 14);
    }};
    boolean debugMode;
    AoCInputConnector inputConnector;

    DayTwo(boolean debugMode, AoCInputConnector inputConnector){
        this.debugMode=debugMode;
        this.inputConnector=inputConnector;
    }

    public void displayResults(){
        if (debugMode)
            displayResultDeb();
        else
            displayResult();
    }

    private void displayResultDeb(){

        System.out.println("Day 2 " +filteredID("Game 42: 1 green, 4 red, 4 blue; 1 green, 6 red, 4 blue; 7 red, 4 green, 1 blue; 2 blue, 8 red, 8 green" ));
        System.out.println("Day 2 Part 2:: " +maxCubeProduct("Game 42: 1 green, 4 red, 4 blue; 1 green, 6 red, 4 blue; 7 red, 4 green, 1 blue; 2 blue, 8 red, 8 green" ));
        //

    }
    private void displayResult(){
        inputConnector.setDay(2);
        List<String> input=inputConnector.getInput();
        /*
         Games are described by an ID and multiple combinations of colored dice drawn from a bag.

         Game 1: 8 green, 4 red, 4 blue; 1 green, 6 red, 4 blue; 7 red, 4 green, 1 blue; 2 blue, 8 red, 8 green

         Sum up the IDs of games that do not violate a specified maximum number of dice per color.
         (= Content of the bag)
         */
        // Sol. 2085
        System.out.println("Day 2: " + input.stream().mapToInt(this::filteredID).sum());

        /*
            And what are fewest needed number of cubes to make a game valid/possible?
            Ex. above: 8 green, 8 red, 4 blue
            Multiply! (=256) And add.
         */
        //Sol. 79315
        System.out.println("Day 2 Part 2: " + input.stream().mapToInt(this::maxCubeProduct).sum());

    }

    private  int filteredID(String line ){
        AtomicBoolean isValid= new AtomicBoolean(true);
        int ID = Integer.parseInt(line.substring(line.indexOf(" ")+1, line.indexOf(":")));
        String[] games= line.substring(line.indexOf(":")+1).split(";");
        Arrays.stream(games).forEach(game ->{
            isValid.set(isValid.get() && checkGame(game));

        });
        return isValid.get() ? ID:0;
    }
    private  boolean checkGame(String game){
        AtomicBoolean result = new AtomicBoolean(true);
        String[] cubes= game.split(",");
        Arrays.stream(cubes).forEach(cube ->{
            String color = cube.substring(cube.indexOf(" ",1)+1);
            Integer count =Integer.parseInt(cube.substring(1, cube.indexOf(" ",1)));
            result.set(result.get() && count <= maxDice.get(color)) ;
        });
        return result.get();
    }

    private  int maxCubeProduct(String line){
        String[] games= line.substring(line.indexOf(":")+1).split(";");
        Map<String, Integer> maxDiceLoc  = new HashMap<String, Integer>() {{
            put("red", 0);
            put("green", 0);
            put("blue", 0);
        }};
        Arrays.stream(games).forEach(game ->{
            updateMaxCubes(maxDiceLoc, game);
        });
        return maxDiceLoc.values().stream().reduce(1, (a,b) -> a*b);
    }

    private  void updateMaxCubes(Map maxCubes, String game){
        String[] cubes= game.split(",");
        Arrays.stream(cubes).forEach(cube ->{
            String color = cube.substring(cube.indexOf(" ",1)+1);
            Integer count =Integer.parseInt(cube.substring(1, cube.indexOf(" ",1)));
            if ( (Integer) maxCubes.get(color) < count)
                maxCubes.put(color,count);
        });
    }
}
