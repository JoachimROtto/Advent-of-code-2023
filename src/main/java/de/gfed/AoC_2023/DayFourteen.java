package de.gfed.AoC_2023;

import java.util.Arrays;

public class DayFourteen extends Day{


    DayFourteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 14);
        expectations=new long[]{111979,908};
        example = Arrays.asList(
                "O....#....",
                "O.OO#....#",
                ".....##...",
                "OO.#O....O",
                ".O.....O#.",
                "O.#..O.#.#",
                "..O..#O..O",
                ".......O..",
                "#....###..",
                "#OO..#...."); // expected: 136

        /*
        You reach the center of the mirrors: a massive parabolic reflector dish consisting of
        smaller mirrors. The dish is barely working since the mirrors a pointing in slightly
        wrong directions. You can move the mirrors via a delicate systems of rocks on a large
        plattform.
        Your input  are the positions of rounded rocks (O), cube-shaped rocks (#)
        and empty spaces on this plattform. If you move the platform (e.g. to north) all
        rounded rocks will move to the north until they are stopped by occupied spaces or
        cube-shaped rocks.
        ..O.#
        ...#.
        O.OO.
        to north->
        O.O.#
        ..O#.
        ...O.
        For security reasons you have to calculate the load of the plattform first. The row
        numbers start with one in the south and the load is the sum of the row number times
        the stones contained. Ex. above: 1*1 + 1*2 + 2*3 = 9.
        What's the load if you tilt north?

        Part2:
        It was not enough! There is a spin cycle. Each cycle the platform tilt to north, west,
        south and east. Try it 1000000000 times and calculate the load again.
          */
    }

    protected long evalInput(boolean bPart2) {
        if(!bPart2)
            return calcLoad(shiftNorth());
        return 0;
    }

    private int[][] shiftNorth(){
        String line;
        // New table for the platform one column represents one column of the plattform
        // and consist alternately of the number of rounded rocks, the position of a cube-formed
        // stone and ends with the current position for adding rounded rocks.
        // Ex. [2][5][2]...[][2] means (as a column) OO...#OO...

        int[][] result=new int [input.size()][input.get(0).length() * 2];
        for (int i=0;i<input.size();i++){
            line=input.get(i);
            for(int j=0; j<line.length();j++){
                switch (line.charAt(j)){
                    case '.':
                        break;
                    case 'O' :
                        result[j][result[j][result.length-1]]++;
                        break;
                    case '#':
                        result[j][result[j][result.length-1]+1]=i;
                        result[j][result.length-1]+=2;
                }
            }
        }
        return result;
    }
    private int calcLoad(int[][] plattform){
        int rowNumber;
        int result=0;
        for (int i=0; i<plattform.length; i++){
            for (int j=0; j<=plattform[i][plattform.length-1];j+=2){
                rowNumber=plattform[i].length/2+1 -(j==0?0:plattform[i][j-1]+1);
                for (int count=plattform[i][j]; count>0;count--)
                    result+= --rowNumber;
            }
        }
        return result;
    }
}
