package de.gfed.AoC;

import java.util.*;


public class DayTen {
    boolean debugMode;
    AoCInputConnector inputConnector;
    int quadrantWinningLoop;
    List<List<int[]>> loops = new ArrayList<List<int[]>>();

    DayTen(boolean debugMode, AoCInputConnector inputConnector) {
        this.debugMode = debugMode;
        this.inputConnector = inputConnector;
    }

    public void displayResults() {
        if ( debugMode )
            displayResultDeb();
        else
            displayResult();
    }


    public void displayResultDeb() {
        List<String> input = Arrays.asList(
                "..F7." ,
                ".FJ|.",
                "SJ.L7",
                "|F--J",
                "LJ...");

/*
        You have made a quick sketch of a system of pipes.
        - S start, . ground (no pipe)
        - | horizontal pipe, - vertical pipe
        - L pipe bend the top to the right, F pipe bend the bottom to the right
        - J bend the top to the left,7 bend the bottom to the left
        Each pipe is one step, where is the longest loop? How many steps is the furthest point?
        .....
        .S-7.
        .|.|.
        .L-J.
        .....
        is 6
         */

        System.out.println("Day 10: " + searchLoop(input));

        /*
        There is a nest inside the loop. Size (count '.')?
        (Really inside the loop, no space between pipes is not enough)
         */

        System.out.println("Day 10 Part 2: " + sizeNest(loops.get(quadrantWinningLoop)));


    }

    public void displayResult() {
        inputConnector.setDay(10);
        List<String> input = inputConnector.getInput();
        input = new ArrayList<>(input);

        System.out.println("Day 10 (Exp. 7093): " + searchLoop(input));

        System.out.println("Day 10 Part 2 (Exp. 407): " + sizeNest(loops.get(quadrantWinningLoop)));

    }

    private int sizeNest(List<int[]> input){
        int result =0;
        int j;
        // shoelace formula
        for (int i =0; i<input.size();i++){
            j = (i + 1) % input.size();
            result += input.get(i)[0] * input.get(j)[1] - input.get(j)[0] * input.get(i)[1];
        }
        // surrounding pipes don't count
        return (Math.abs(result)  - input.size())/2 +1;
    }

    private Integer searchLoop (List<String> input){
        Integer result =0;
        int[] pos= findStart(input);
        int[] steps=new int[4];

        if (pos[0]!= input.size()) {
            steps[0]=Math.max(result, followThePipe(new int[]{pos[0] + 1, pos[1], 0}, input));
            result = steps[0];
        }

        if (pos[1]!= 0) {
            steps[1] = Math.max(result, followThePipe(new int[]{pos[0], pos[1] - 1, 1}, input));
            result = Math.max(steps[1], result);
        }

        if (pos[0]!=0) {
            steps[2] = Math.max(result, followThePipe(new int[]{pos[0] - 1, pos[1], 2}, input));
            result = Math.max(steps[2], result);
        }

        if (pos[0]!= input.get(pos[0]).length()) {
            steps[3] = Math.max(result, followThePipe(new int[]{pos[0], pos[1] + 1, 3}, input));
            result = Math.max(steps[3], result);
        }

        quadrantWinningLoop = Arrays.asList(Arrays.stream(steps).boxed().toArray(Integer[]::new))
                .indexOf(Integer.parseInt(result.toString()));
        return result;
    }
    private int[] findStart(List<String> input){
        int[] pos= new int[]{0,0,0};
        for (int i=0; i<input.size();i++){
            if (input.get(i).contains("S")) {
                pos[0] = i;
                pos[1] = input.get(i).indexOf("S");
            }
        }
        return pos;
    }

    private int followThePipe ( int[] pos, List<String> input){
        int result=1;
        List<int[]> loop = new ArrayList<>();
        loop.add(new int[]{pos[0], pos[1]});
        do {
            pos=getNextPipeAndQuadrantIn(pos, input.get(pos[0]).charAt(pos[1]), pos[2]);
            loop.add(new int[]{pos[0], pos[1]});
            result++;
        }
        while (pos[0]!=-1 && input.get(pos[0]).charAt(pos[1]) !='S');
        loops.add(loop);
        return pos[0]==-1 || input.get(pos[0]).charAt(pos[1]) !='S'? -1:  result/2 ;

    }

    private int[] getNextPipeAndQuadrantIn(int[] pos, char pipe, int quadrantIn){
        switch (pipe){
            case '|':
                return new int[]{quadrantIn==0? pos[0]+1:pos[0]-1,
                        pos[1], quadrantIn==0? 0:2};
            case '-':
                return new int[]{pos[0], quadrantIn==1? pos[1]-1:pos[1]+1,
                        quadrantIn==1? 1:3};
            case 'L':
                if (quadrantIn==0){
                    return new int[]{pos[0],pos[1]+1,3};
                }else{
                    return new int[]{pos[0]-1,pos[1],2};
                }
            case 'F':
                if (quadrantIn==1){
                    return new int[]{pos[0]+1,pos[1],0};
                }else{
                    return new int[]{pos[0],pos[1]+1,3};
                }
            case 'J':
                if (quadrantIn==0){
                    return new int[]{pos[0],pos[1]-1,1};
                }else{
                    return new int[]{pos[0]-1,pos[1],2};
                }
            case '7':
                if (quadrantIn==3){
                    return new int[]{pos[0]+1,pos[1],0};
                }else{
                    return new int[]{pos[0],pos[1]-1,1};
                }
            case 'S':
                return new int[]{pos[0],pos[1],-2};
            default:
                return new int[]{-1,-1,-1};
        }
    }
}
