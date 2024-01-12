package de.gfed.AoC;

import java.util.*;


public class DayTen {
    boolean debugMode;
    AoCInputConnector inputConnector;


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
        There is a nest in the loop. Size (count '.')? (Really in the loop, no space between pipes is not enough)
         */

        System.out.println("Day 10 Part 2: " + sizeNest(input));


    }

    public void displayResult() {
        inputConnector.setDay(10);
        List<String> input = inputConnector.getInput();
        input = new ArrayList<>(input);

        // Sol. 7093
        System.out.println("Day 10 (Exp. 7093): " + searchLoop(input));

        //System.out.println("Day 10 Part 2: " + searchLoop(input));

    }

    private int sizeNest(List<String> input){
        int result =0;
        int[] pos= findStart(input);

        // Den Quadranten der Loop sichern
        // Den Weg mit X markieren, den Rest mit 0
        // Von rechsts durchgehen und mit X tooglen=>true 0=1 und zählen

        return result;
    }

    private int searchLoop (List<String> input){
        int result =0;
        int[] pos= findStart(input);

        int quadrantOut;

        if (pos[0]!=0)
            result = Math.max(result, followThePipe(new int[]{pos[0]-1,pos[1], 2}, input));

        if (pos[0]!= input.get(pos[0]).length())
            result = Math.max(result, followThePipe(new int[]{pos[0],pos[1]+1, 3}, input));

        if (pos[0]!= input.size())
            result = Math.max(result, followThePipe(new int[]{pos[0]+1,pos[1], 0}, input));

        if (pos[1]!= 0)
            result = Math.max(result, followThePipe(new int[]{pos[0],pos[1]-1, 1}, input));
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
        do {
            pos=getNextPipeAndQuadrantIn(pos, input.get(pos[0]).charAt(pos[1]), pos[2]);
            result++;
        }
        while (pos[0]!=-1 && input.get(pos[0]).charAt(pos[1]) !='S');

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