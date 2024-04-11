package de.gfed.AoC_2023;

import java.util.*;

public class DayTwentyOne extends Day{

    int[] start=new int[2];

    Map<String, int[] > directions = new HashMap<>();

    DayTwentyOne(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 21);

        directions.put("EAST", new int[]{1,0});
        directions.put("SOUTH", new int[]{0,1});
        directions.put("WEST", new int[]{-1,0});
        directions.put("NORTH", new int[]{0,-1});

        expectations=new long[]{3671,609708004316870L};
        example = Arrays.asList(
                "...........",
                ".....###.#.",
                ".###.##..#.",
                "..#.#...#..",
                "....#.#....",
                ".##..S####.",
                ".##..#...#.",
                ".......##..",
                ".##.#.####.",
                ".##..##.##.",
                "..........."); //6 steps->16 plots
        /*
        An elf shows you a map with stones (#) and garden plots(.). He can move one step north,
        south, east or west beginning from his starting point (S) if there is no stone there.
        How many garden plots can he be on after 64 steps?
        ..#  ->  .O#    ->O.#
        .S.      O.O      .O.
        ...      .O.      O.O  =4


        part 2:
        Ooops 64? I've meant 26501365. And don't forget the magic map. When boundaries appear
        there it triples in every direction. The start remains in the middle segment.
        */

    }

    protected long evalInput(boolean bPart2) {
        int[][] map= processInput(input);
        List<int[]> plots=new ArrayList<>();
        int[] result;
        plots.add(start);

        if (!bPart2) {
            for (int i=1; i<65;i++) {
                plots = nextStep(i,map, plots);
            }
            result=countEvenOddSpots(map,bPart2);
            return result[0];
        }

        // Of course, extending the loop is not possible.

        //After "map.length" steps the map is traversed once and the spots visited alternate: result after step 63 +
        // result after step 64 + stones = map-size. Therefore, 26501365 / map.length gives the number of cards completely
        // passed through. Assumption: The distribution of steps is the same there. After division, half of the length
        // remains as a remainder and the result must be corrected.

        for (int i=1; i<map.length+1;i++)
            plots= nextStep(i,map, plots);

        result=countEvenOddSpots(map, bPart2);
        long mapMultiplier= 26501365 / map.length;
        return ((mapMultiplier + 1) * (mapMultiplier + 1) * result[1]) + (mapMultiplier * mapMultiplier * result[0])
                - ((mapMultiplier + 1) * result[3]) + (mapMultiplier * result[2]);

    }

    private List<int[]> nextStep(int times,int[][]  input, List<int[]> plots){
        List<int[]> result=new ArrayList<>();
        while (!plots.isEmpty()){
            int[] plot=plots.remove(0);
            directions.forEach((direction, move) ->{
                int[]newPlot=returnValidMove(plot, move, input);
                if (newPlot!=null) {
                    result.add(newPlot);
                    input[newPlot[0]][newPlot[1]]=times;
                }
            });
        }
        return result;
    }

    private int[] returnValidMove(int[] plot, int[] move, int[][] input){
        int[]result=new int[]{plot[0]+move[0], plot[1]+move[1]};

        if (result[0]>-1 && result[1]>-1 &&
                result[0]<input.length && result[1]<input[0].length &&
                input[result[0]][result[1]]==1000 )
            return result;
        return null;
    }

    private int[] countEvenOddSpots(int[][] input, boolean bPart2 ){
        int[] result = new int[4];
        for (int i=0; i< input.length; i++  ){
            for (int j=0; j< input[i].length; j++ ){
                if (input[i][j]>-1 && input[i][j]<1000)
                    if (!bPart2){
                        result[ input[i][j] % 2 ]++;
                    } else {
                        result[ (i+j) % 2 ]++;
                        if (input[i][j]> input.length/2)
                            result[ (i+j) % 2 + 2]++;
                    }
            }
        }
        return result;
    }
    private int[][] processInput(List<String> input ){
        int[][] result = new int[input.size()][input.get(0).length()];
         for (int i=0; i< input.size(); i++  ){
             String line = input.get(i);
             for (int j=0; j< line.length(); j++ ){
                 if (line.charAt(j)=='S')
                     start=new int[]{i,j};
                 result[i][j]=(line.charAt(j)=='#')?-1:1000;
             }
         }
        return result;
    }
}