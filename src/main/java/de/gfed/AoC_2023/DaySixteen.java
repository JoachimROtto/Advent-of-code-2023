package de.gfed.AoC_2023;

import java.util.Arrays;
import java.util.List;

public class DaySixteen  extends Day{

    final byte NORTH=1;
    final byte EAST=2;
    final byte SOUTH=4;
    final byte WEST=8;
    byte[][] activationMap;
    DaySixteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 16);
        expectations=new long[]{6978,7315};
        example = Arrays.asList(
                ".|...\\....",
                "|.-.\\.....",
                ".....|-...",
                "........|.",
                "..........",
                ".........\\",
                "..../.\\\\..",
                ".-.-/..|..",
                ".|....-|.\\",
                "..//.|...."); //->46
        /*
        You are led to a cave where the beam ends. Inside you find a contraption where
        light passes through a labyrinth of mirrors. You get a plan with
        - Mirrors : /\ reflect 90 degrees; | - split into two beams and reflect 90 and 270 degrees when
                    hit at the flat side or have no effect (>>>->>>)
        - Empty space : .
        The beam enters the top left corner. Spots, mirrors or air, are activated by a passing
        beam of light. Not enough spots are being passed.
        Part 1: How many spots are activated?

        Part 2:
        As you were told, the system produces too little activation. You can position them differently
        to select each entrance. Which entrance generates the most activation? (0,3 ->51)
        */

    }

    protected long evalInput(boolean bPart2) {
        long result=0;
        if (!bPart2)
            return takeAWalk(input, new byte[]{0,0,EAST});

        for (byte i =0; i< input.size();i++){
            for (byte j=0; j<input.get(i).length(); j++){
                if (i==0){
                    result=Math.max(result, takeAWalk(input, new byte[]{j,i,SOUTH}));
                }
                if (j==0){
                    result=Math.max(result, takeAWalk(input, new byte[]{j,i,EAST}));
                }
                if (i==input.size()-1){
                    result=Math.max(result, takeAWalk(input, new byte[]{j,i,NORTH}));
                }
                if (j==input.get(0).length()-1){
                    result=Math.max(result, takeAWalk(input, new byte[]{j,i,WEST}));
                }
            }
        }
        return result;
    }

    private long takeAWalk(List<String> input, byte [] entry){
        char[][] contraption =new char[input.size()][input.get(0).length()];
        this.activationMap=new byte[input.size()][input.get(0).length()];
        for (int i =0; i< input.size();i++){
            String line =input.get(i);
            for (int j=0; j<line.length(); j++){
                contraption[i][j]=line.charAt(j);
            }
        }
        takeNextStep(contraption, entry);
        return calcActivation();
    }

    private long takeNextStep(char[][]contraption,  byte[]move){
        long result;
        byte coordX=move[0];
        byte coordY=move[1];
        byte direction=move[2];

        if (coordX<0||coordY<0|| coordY>=contraption[0].length || coordX>=contraption.length)
            return 0;
        if ((direction & activationMap[coordY][coordX])!=0)
            return 0;

        activationMap[coordY][coordX]+=direction;
        result =1;

        if (contraption[coordY][coordX] =='-'){
            if ((direction & (EAST + WEST))!=0){
                result += takeNextStep(contraption, evalNexStep('.', move));
            } else {
                result += takeNextStep(contraption, evalNexStep('/', move)) +
                        takeNextStep(contraption, evalNexStep('\\', move));
            }
        } else if (contraption[coordY][coordX] =='|'){
            if ((direction & NORTH+SOUTH)!=0){
                result += takeNextStep(contraption, evalNexStep('.', move));
            } else {
                result += takeNextStep(contraption, evalNexStep('/', move)) +
                        takeNextStep(contraption, evalNexStep('\\', move));
            }
        } else {
            result += takeNextStep(contraption, evalNexStep(contraption[coordY][coordX], move));
        }
        return result;
    }
    private byte[] evalNexStep(char space, byte[] move) {
        byte coordX = move[0];
        byte coordY = move[1];
        byte direction = move[2];

        switch (direction){
            case NORTH:{
                if(space=='.')
                    coordY-=1;
                if(space=='/') {
                    direction = EAST;
                    coordX += 1;
                }
                if(space=='\\') {
                    direction = WEST;
                    coordX -= 1;
                }
                break;
            }
            case EAST:{
                if(space=='.')
                    coordX+=1;
                if(space=='/') {
                    direction = NORTH;
                    coordY -= 1;
                }
                if(space=='\\') {
                    direction = SOUTH;
                    coordY += 1;
                }
                break;
            }
            case SOUTH:{
                if(space=='.')
                    coordY+=1;
                if(space=='/') {
                    direction = WEST;
                    coordX -= 1;
                }
                if(space=='\\') {
                    direction = EAST;
                    coordX += 1;
                }
                break;
            }
            case WEST:{
                if(space=='.')
                    coordX-=1;
                if(space=='/') {
                    direction = SOUTH;
                    coordY += 1;
                }
                if(space=='\\'){
                    direction=NORTH;
                    coordY-=1;
                }
                break;
            }
        }
        return new byte[]{coordX, coordY, direction};
    }

    private long calcActivation(){
        long result=0;
        for (byte[] items:activationMap){
            for (byte item: items)
                result+=item>0?1:0;
        }
        return result;
    }
}