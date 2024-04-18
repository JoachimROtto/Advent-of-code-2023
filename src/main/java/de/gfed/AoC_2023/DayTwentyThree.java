
package de.gfed.AoC_2023;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DayTwentyThree extends Day{
    char[][] map;
    boolean[][] visited;
    int [] start;
    int[] stop;
    int walkLabelCounter=0;
    List<List<Integer>> walksList  = new ArrayList<>();
    Map<String, int[] > directions = new HashMap<>();
    Map< Integer, String > lineage = new HashMap<>();
    List<int[]> segments  = new ArrayList<>();
    DayTwentyThree(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 23);
        directions.put("EAST", new int[]{0,1});
        directions.put("SOUTH", new int[]{1,0});
        directions.put("WEST", new int[]{0,-1});
        directions.put("NORTH", new int[]{-1,0});
        expectations=new long[]{2214,89727};
        example = Arrays.asList(
                "#.#####################",
                "#.......#########...###",
                "#######.#########.#.###",
                "###.....#.>.>.###.#.###",
                "###v#####.#v#.###.#.###",
                "###.>...#.#.#.....#...#",
                "###v###.#.#.#########.#",
                "###...#.#.#.......#...#",
                "#####.#.#.#######.#.###",
                "#.....#.#.#.......#...#",
                "#.#####.#.#.#########v#",
                "#.#...#...#...###...>.#",
                "#.#.#v#######v###.###v#",
                "#...#.>.#...>.>.#.###.#",
                "#####v#.#.###v#.#.###.#",
                "#.....#...#...#.#.#...#",
                "#.#########.###.#.#.###",
                "#...###...#...#...#.###",
                "###.###.#.###v#####v###",
                "#...#...#.#.>.>.#.>.###",
                "#.###.###.#.###.#.#v###",
                "#.....###...###...#...#",
                "#####################.#"); //->max(90, 86, 82, 82, 74, 94)=94 ; P2:154

/*
        It's time for a break. Why not do some hiking? You have a map that shows forests, paths and slippery
        places (you can only slide in pointed direction). You want to see a lot, so you choose the longest
        route and never step onto the same ground twice.
        How long is the longest path from top left to bottom right?

        part 2:
        The slopes are not that slippery. Treat them as normal paths. Again: How log is the longest path?
        */

    }

    protected long evalInput(boolean bPart2) {
        long result=0;
        if (!bPart2) {
            map = parseInput(input);
            visited=new boolean[map.length][map[0].length];
            start=new int[]{0, input.get(0).indexOf(".")};
            stop=new int[]{input.size()-1, input.get(input.size()-1).lastIndexOf(".")};
            lineage.put(walkLabelCounter, "-");
            result= takeAllWalks(start, stop, directions.get("SOUTH"), walkLabelCounter);
            return result;
        }
        removeSlopes();
        listPartialPaths(start, stop, directions.get("SOUTH"),0);
        // langsam(20s) und falsches Ergebnis, doch eher wieder dijsktra?
        //takeSegmentWalks(new int[]{-1,1}, stop, new ArrayList<>());
        return  findLongestWalk();
    }

    private int findLongestWalk(){
        int length=0;
        int result=0;
        for (List<Integer> integers : walksList) {
            for (Integer integer : integers) {
                int[] segment=segments.get(integer);
                StringBuilder line=new StringBuilder();
                for (int i=0; i< segment.length; i++  ){
                    line.append(segment[i] + " ");
                }
                // System.out.println(line);
                length+=segment[4];
            }
            //System.out.println(length-1);
            result=Math.max(result, length-1);
            length=0;
        }
        return result;
    }

    private int takeSegmentWalks(int[] start, int [] stop, List <Integer> walk ){
        AtomicInteger result = new AtomicInteger();
        if (Arrays.equals(start,stop)) {
            walksList.add(walk);
            return result.get();
        }
        List<Integer> nextSegments = new ArrayList<>();
        for (int i=0; i< segments.size(); i++){
            if (Arrays.equals(segments.get(i),0, 2, start, 0, 2) &&
                    !walk.contains(i) && !walk.contains( i + (i%2==0?1:-1))){
                nextSegments.add(i);
            }
        }
        nextSegments.forEach(nextSegment ->{
            List <Integer> newWalk =new ArrayList<>();
            newWalk.addAll(walk);
            newWalk.add(nextSegment);
            result.set(Math.max(result.get(), takeSegmentWalks(new int[]{segments.get(nextSegment)[2], segments.get(nextSegment)[3]},
                    stop, newWalk)+segments.get(nextSegment)[4]));
        });
        return result.get();
    }

    private int listPartialPaths(int[] start, int [] stop, int[] formerDirection, int labelWalk){
        int [] pos = start;
        int [] saveStart= new int[]{ start[0]-formerDirection[0], start[1]-formerDirection[1]};
        if (start==stop)
            return 0;
        boolean segmentEnds =false;
        List <int[]> validDirections=new ArrayList<>();
        int length=1;
        int [] savePos= new int[2];
        while (!segmentEnds){
            validDirections=new ArrayList<>();
            int[] finalFormerDirection = formerDirection;
            List<int[]> finalValidDirections =new ArrayList<>();;
            int[] finalPos1 = pos;
            directions.forEach((label, value) ->{
                if(nextStep(finalPos1, value, finalFormerDirection, labelWalk)!=null) {
                    finalValidDirections.add(value);
                }});

            if (finalValidDirections.size()!=1){
                savePos= new int[]{ pos[0], pos[1]};
                segmentEnds =true;
            }
            if (!segmentEnds ) {
                length++;
                visited[pos[0]][pos[1]]=true;
                pos = nextStep(pos, finalValidDirections.get(0),formerDirection, 0);
                formerDirection=finalValidDirections.get(0);
            }
            if(finalValidDirections.size()>1)
                visited[savePos[0]][savePos[1]]=false;
        }

        segments.add(new int[]{saveStart[0],saveStart[1],savePos[0],savePos[1], length});
        segments.add(new int[]{savePos[0],savePos[1],saveStart[0],saveStart[1], length});

        int[] finalPos = pos;
        int[] finalFormerDirection1 = formerDirection;
        directions.forEach((label, value) ->{
            if(nextStep(finalPos, value, finalFormerDirection1, labelWalk)!=null) {//finalFormerDirection1->value
                listPartialPaths(nextStep(finalPos, value, finalFormerDirection1, labelWalk), stop, value, 0);
            }});

        return 0;

    }

    private void removeSlopes(){
        for (int i=0; i< map.length; i++  ){
            for (int j=0; j< map[0].length; j++ ){
                if (map[i][j]!='#')
                    map[i][j]='.';
            }
        }
    }

    private int[] nextStep(int [] coord, int[] direction, int[] formerDirection, int walkLabel){
        if (direction[0]*formerDirection[0]==-1 || direction[1]*formerDirection[1]==-1 )
            return null;
        int[] result =new int[]{coord[0]+direction[0], coord[1]+direction[1]};
        if (result[0] <0 ||result[0] > map.length-1 ||
                result[1] <0 ||result[1] > map[0].length-1 )
            return null;
        if (map[result[0]][result[1]]=='#' || visited[result[0]][result[1]])
            return null;
        if((map[coord[0]][coord[1]]=='<' && direction[1]!=-1) ||
                (map[coord[0]][coord[1]]=='>' && direction[1]!=1) ||
                (map[coord[0]][coord[1]]=='v' && direction[0]!=1) ||
                (map[coord[0]][coord[1]]=='^' && direction[0]!=-1))
            return null;

        return result;
    }

    private char[][] parseInput(List<String> input){
        char[][] result = new char[input.size()][input.get(0).length()];
        for (int i=0; i< input.size(); i++  ){
            String line = input.get(i);
            for (int j=0; j< line.length(); j++ ){
                result[i][j]=line.charAt(j);
            }
        }
        return result;
    }

    private int takeAllWalks(int[] start, int [] stop, int[] formerDirection, int labelWalk){
        AtomicInteger result = new AtomicInteger();
        if (start==stop)
            return 0;
        AtomicInteger walkLabel= new AtomicInteger();
        directions.forEach((label, value) ->{
            if(nextStep(start, value, formerDirection, 0)!=null) {
                result.set(Math.max(result.get(),
                        takeAllWalks(nextStep(start, value, formerDirection, walkLabel.get()), stop, value, labelWalk + walkLabel.get()) + 1)
                );
                walkLabel.getAndIncrement();
            }
        });
        return result.get();
    }
    }

