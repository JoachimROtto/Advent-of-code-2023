package de.gfed.AoC_2023;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class DayTwo extends Day{
    static Map<String, Integer> maxDice  = new HashMap<>() {{
        put("red", 12);
        put("green", 13);
        put("blue", 14);
    }};

    DayTwo(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 2);
        expectations=new long[]{2085,79315};
        example = Arrays.asList(
                "Game 42: 1 green, 4 red, 4 blue; 1 green, 6 red, 4 blue; 7 red, 4 green, 1 blue; 2 blue, 8 red, 8 green");

        /*
         Games are described by an ID and multiple combinations of colored dice drawn from a bag.

         Game 1: 8 green, 4 red, 4 blue; 1 green, 6 red, 4 blue; 7 red, 4 green, 1 blue; 2 blue, 8 red, 8 green

         Sum up the IDs of games that do not violate a specified maximum number of dice per color.
         (= Content of the bag)

         And what are fewest needed number of cubes to make a game valid/possible?
         Ex. above: 8 green, 8 red, 4 blue
         Multiply! (=256) And add.
         */

    }

    protected long evalInput(boolean bPart2) {
        if (!bPart2)
            return input.stream().mapToInt(this::filteredID).sum();
        return input.stream().mapToInt(this::maxCubeProduct).sum();
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
            int count =Integer.parseInt(cube.substring(1, cube.indexOf(" ",1)));
            result.set(result.get() && count <= maxDice.get(color)) ;
        });
        return result.get();
    }

    private  int maxCubeProduct(String line){
        String[] games= line.substring(line.indexOf(":")+1).split(";");
        Map<String, Integer> maxDiceLoc  = new HashMap<>() {{
            put("red", 0);
            put("green", 0);
            put("blue", 0);
        }};
        Arrays.stream(games).forEach(game ->{
            updateMaxCubes(maxDiceLoc, game);
        });
        return maxDiceLoc.values().stream().reduce(1, (a,b) -> a*b);
    }

    private  void updateMaxCubes(Map<String, Integer> maxCubes, String game){
        String[] cubes= game.split(",");
        Arrays.stream(cubes).forEach(cube ->{
            String color = cube.substring(cube.indexOf(" ",1)+1);
            Integer count =Integer.parseInt(cube.substring(1, cube.indexOf(" ",1)));
            if ( maxCubes.get(color) < count)
                maxCubes.put(color,count);
        });
    }
}
