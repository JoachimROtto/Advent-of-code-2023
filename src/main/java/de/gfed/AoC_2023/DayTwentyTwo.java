package de.gfed.AoC_2023;

import java.util.*;

public class DayTwentyTwo extends Day{

    Map<Integer,Integer> removable=new HashMap<>();
    boolean[][] supports;
    List<Brick> bricks;
    int [][][] occupied;

    DayTwentyTwo(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 22);

        expectations=new long[]{463,89727};
        example = Arrays.asList(
                "1,0,1~1,2,1" ,
                "0,0,2~2,0,2" ,
                "0,2,3~2,2,3" ,
                "0,0,4~0,2,4" ,
                "2,0,5~2,2,5" ,
                "0,1,6~2,1,6",
                "1,1,8~1,1,9"); // ->5 / 6+0+0+0+0+1+0=7
        /*
        There is a stack of large compacted bricks of sand. You were given a list of the stones how they fell
        down and got stacked. The stone in the first line is directly on the ground. Each line consists
        of 2 groups of three values each that indicate the x,y,z extent. 0,0,10~1,0,10 represents a brick
        that extends 2 cubes in the x-direction, 2,2,2~2,2,2 is a single cube. The ground is at z=0 so
        5,5,1~5,6,1 and 0,2,1~0,2,5 are both resting on the ground.
        You need to take some of the bricks. You need to consider how they are stacked and wich brick supports
        bricks that are also supported by other bricks. How many bricks can be removed safely? Stones that can
        be removed either count both.

        part 2:
        That's not enough stones. You have to remove stones where others will fall. For each stone, find
        out how many would then fall. What is the total?
        */

    }

    protected long evalInput(boolean bPart2) {
        if (!bPart2) {
            bricks= parseInput(input);
            rearrangeBricks(bricks);
            return countRemovableBricks();
            }

        long result= 0;
        for (Brick brick : bricks) {
            List<Integer> chain=new ArrayList<>();
            chain.add(brick.ID);
            result+=bricksChainReactionResult(chain, 0).size()-1;
        }
        return result;
    }

    private List<Integer> bricksChainReactionResult(List<Integer> bricks, int start){
        int ID;
        Map<Integer, Integer> supported = new HashMap<>();
        for (int i=start; i<bricks.size(); i++){
            ID = bricks.get(i);
            for (int j=0; j<supports[ID].length; j++) {
                if (supports[ID][j]){
                    supported.put(j,j);
                }
            }
        }
        start= bricks.size();
        if (supported.isEmpty())
            return bricks;
        supported.forEach((label, value) ->{
            boolean willNotFall=false;
            for (int j=0; j<supports.length; j++) {
                if (supports[j][label]){
                    if (!supported.containsKey(j) && (!bricks.contains(j)))
                        willNotFall=true;
                }
            }
            if (!willNotFall && !bricks.contains(label))
                bricks.add(label);
        });
        return bricksChainReactionResult(bricks, start);
    }

    private void rearrangeBricks(List<Brick> bricks){
        List<Brick> fallenBricks=new ArrayList<>(bricks);
        Brick currentBrick;
        while (!fallenBricks.isEmpty()) {
            currentBrick = fallenBricks.remove(0);
            allocSpaceAndFindSupported(currentBrick, occupied);
        }
    }

    private void allocSpaceAndFindSupported(Brick currentBrick, int [][][] occupied){

        int newZ=0;
        int supporter=0;
        for (int i= currentBrick.x[0]; i <currentBrick.x[1]+1; i++){
            for (int j= currentBrick.y[0]; j <currentBrick.y[1]+1; j++) {
                for (int k =0; k < occupied[0][0].length ; k++) {
                    if (occupied[i][j][k]!=0){
                        newZ=Math.max(newZ,k);
                        if (newZ==k)
                            supporter=occupied[i][j][k];
                    }
                }
            }
        }
        newZ++;
        removable.remove(supporter);

        for (int i= currentBrick.x[0]; i <currentBrick.x[1]+1; i++){
            for (int j= currentBrick.y[0]; j <currentBrick.y[1]+1; j++) {
                for (int k = newZ; k < (newZ + currentBrick.z[1]-currentBrick.z[0]+1 ); k++) {
                    occupied[i][j][k]=currentBrick.ID;
                }
                currentBrick.z[1]=newZ + currentBrick.z[1]-currentBrick.z[0];
                currentBrick.z[0]= newZ;
                if (occupied[i][j][newZ-1]!=0 && occupied[i][j][newZ-1]!=currentBrick.ID){
                    supports[occupied[i][j][newZ-1]][currentBrick.ID] = true;
                    removable.remove(occupied[i][j][newZ-1]);

                }
            }
        }
    }

    public int countRemovableBricks(){
        Map<Integer,Integer> supporterList=new HashMap<>();
        Map<Integer,Integer> nonRemovable=new HashMap<>();
        int supporter=0;
        for (int i=0; i< supports.length; i++){
            for (int j=0; j< supports.length; j++){
                if (supports[j][i]) {
                    supporter++;
                    supporterList.put(j, 0);
                }
            }
            if ( supporter>1 ) {
                removable.putAll(supporterList);
            } else {
                nonRemovable.putAll(supporterList);
            }
            supporter=0;
            supporterList=new HashMap<>();
        }
        nonRemovable.forEach((label, value) ->{
            removable.remove(label);
        });
        return removable.size();
    }
    private List<Brick> parseInput(List<String> input){
        int maxX=0;
        int maxY=0;
        int maxZ=0;

        supports=new boolean[input.size()+1][input.size()+1];

        List<Brick> result = new ArrayList<>();
        List<Brick> result1 = new ArrayList<>();

        for (int i=0; i<input.size(); i++){
            int[] line = Arrays.stream(input.get(i).replace("~", ",").split(",")).mapToInt(Integer::parseInt).toArray();
            result.add(new Brick (i +1,
                    new int[]{line[0], line[3]}, new int[]{line[1], line[4]}, new int[]{line[2], line[5]}, new int[1]));
            removable.put(i+1, 0);
            maxX=Math.max(maxX, line[3]);
            maxY=Math.max(maxY, line[4]);
            maxZ=Math.max(maxZ, line[5]);
        }
        result.sort(Comparator.comparingInt(i -> i.z[0]));
        for (int i=0; i<result.size();i++){
            Brick a= result.get(i);
            result1.add(new Brick(i+1, a.x, a.y,a.z,a.layer));
        }
        occupied=new int[maxX+1][maxY+1][maxZ+1];
        return  result1;
    }
    record Brick(int ID,int[]x, int[]y, int[]z, int[] layer){}
}