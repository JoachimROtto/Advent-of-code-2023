package de.gfed.AoC_2023;

import java.util.*;

public class DayEighteen extends Day{
    DayEighteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 18);
        expectations=new long[]{33491,87716969654406L};
        example = Arrays.asList("R 6 (#70c710)",
                "D 5 (#0dc571)",
                "L 2 (#5713f0)",
                "D 2 (#d2c081)",
                "R 2 (#59c680)",
                "D 2 (#411b91)",
                "L 5 (#8ceee2)",
                "U 2 (#caa173)",
                "L 1 (#1b58a2)",
                "U 2 (#caa171)",
                "R 2 (#7807d2)",
                "U 3 (#a77fa3)",
                "L 2 (#015232)",
                "U 2 (#7a21e3)"); //->62
        /*
        You will receive a dig plan that consists of directional information ((L)eft, (D)own,...) and
        thus describes the shore of a lagoon. What area does this lagoon cover?

        Part 2: Oh no the instructions (direction and color) were swapped. The direction consists of the
        first 5 hexadecimal digits as length and the last clockwise (starting with left) as direction
        (D 5 (#70c710) => length= decimal(0dv571) =461937, direction=1=down)
        **/

    }

    protected long evalInput(boolean bPart2) {
        return calcArea(input, bPart2);
}

    private long calcArea(List<String> input, boolean bPart2){
        List<long[]> coords=new ArrayList<>();
        coords.add(new long[]{0,0});
        long length=0;

        Map<String, int[] > directions = new HashMap<>();
        directions.put("R", new int[]{1,0});
        directions.put("D", new int[]{0,1});
        directions.put("L", new int[]{-1,0});
        directions.put("U", new int[]{0,-1});

        String[] newDirection=new String[]{"R", "D", "L", "U"};

        String line;
        String key;

        for (int i=0; i<input.size(); i++){
            line=input.get(i);
            if (!bPart2){
                coords.add(new long[]{
                        coords.get(i)[0]+
                                (long) directions.get(line.split(" ")[0])[0] *
                                        Integer.parseInt(line.split(" ")[1])
                                ,
                        coords.get(i)[1]+ (long) directions.get(line.split(" ")[0])[1] *Integer.parseInt(line.split(" ")[1])
                });
                length+=Integer.parseInt(line.split(" ")[1]);
            } else {
                key = newDirection[Integer.parseInt(line.substring(line.indexOf(")") - 1, line.length() - 1))];
                coords.add(new long[]{
                        coords.get(i)[0]+ (long) directions.get(key)[0]
                                        *Integer.parseInt(line.split("#")[1].substring(0,5), 16),
                        coords.get(i)[1]+ (long) directions.get(key)[1]
                                *Integer.parseInt(line.split("#")[1].substring(0,5), 16)
                });
                length+=Integer.parseInt(line.split("#")[1].substring(0,5),16);
            }
        }

        long result =0;
        int j;
        // shoelace formula
        for (int i =1; i<coords.size();i++){
            j = (i + 1) % input.size();
            result += coords.get(i)[0] * coords.get(j)[1] - coords.get(j)[0] * coords.get(i)[1];
        }
        return length + (Math.abs(result)  - length)/2 +1;
    }


}
