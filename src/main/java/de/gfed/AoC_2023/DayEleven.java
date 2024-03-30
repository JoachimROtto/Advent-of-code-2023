package de.gfed.AoC_2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DayEleven extends Day{
    DayEleven(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 11);
        expectations=new long[]{10292708,790194712336L};
        example = Arrays.asList(
                "...#......",
                ".......#..",
                "#.........",
                "..........",
                "......#...",
                ".#........",
                ".........#",
                "..........",
                ".......#..",
                "#...#.....");
        /*
        You have a map of galaxies
         -- see above --
        with space (.) and galaxies (#). As a first step the space expands (rows and columns with no galaxy
        will be doubled). Above row 4 and 8 and column 3,6 and 9.
        Now find the shortest distance (rows + columns) between to galaxies and add them (each pair once).

        Part 2:
        The expansion of space has happened  1000000 times. So each empty row or column insert
        1000000 empty rows or columns (=999999 more)
         */
    }

    protected long evalInput(boolean bPart2) {
        if (!bPart2)
            return computeDistances(processInput(input,1));
        return computeDistances(processInput(input,1000000-1));
    }

    private int[][] processInput(List<String> input, int expansionGrade){
        List<int[]> galaxies = new ArrayList<>();
        int[][] result;
        int offsetY=0;
        int step=0;
        int[] offsetX=new int[input.get(0).length()];
        Arrays.fill(offsetX, expansionGrade);

        for (int i=0;i<input.size();i++){
            if (input.get(i).indexOf('#')==-1)
                offsetY+=expansionGrade;
            while ((step=input.get(i).indexOf('#', step))!=-1){
                galaxies.add(new int[]{step, i+offsetY});
                offsetX[step]=0;
                step++;
            }
        }
        for(int i=0; i<offsetX.length;i++){
            if (i!=0)
                offsetX[i] = offsetX[i-1] + 1 + offsetX[i];
        }
        result=new int[galaxies.size()][];
        for (int i =0; i<galaxies.size();i++){
            result[i]=new int[]{offsetX[galaxies.get(i)[0]], galaxies.get(i)[1]};
        }
        return result;
    }
    private long computeDistances(int[][] galaxies){
        long result =0;
        for (int i=0;i<galaxies.length;i++){
            for (int j=i;j<galaxies.length;j++){
                result += Math.abs(galaxies[j][0]-galaxies[i][0])+ Math.abs(galaxies[j][1]-galaxies[i][1]);
            }
        }
        return result;
    }
}