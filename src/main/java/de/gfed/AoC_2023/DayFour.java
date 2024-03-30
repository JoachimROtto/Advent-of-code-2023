package de.gfed.AoC_2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class DayFour extends Day{
    DayFour(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 4);
        expectations=new long[]{26218,9997537};
        example = Arrays.asList(
                "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                "Card 5: 87 83 22 28 36 | 88 30 70 12 93 22 82 36",
                "Card 6: 31 18 13 56 74 | 74 77 10 23 35 67 36 11");
        //-> 13 // Part 2: 30

        /*
        A number of cards show an ID, a number of winning numbers and your own numbers.
        A match is one point, each further doubles the result. Add up!

        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19

         leads 8 + 2
         */

        /* Part2:
        Ooops, new rules on the back. N Winnings get no points but copies of the next N cards!
        And the copies play.
         */

    }

    protected long evalInput(boolean bPart2) {
        List <List<Integer>> numbers = preProcessInput(input);
        List<Integer> winnings= new ArrayList<>();
        for( List<Integer> number : numbers) {
            winnings.add(determineWinnings(number));
        }

        if (!bPart2)
            return winnings.stream().mapToInt(this::powerVariant).sum();
        return determineWinningsPartTwo(winnings);
    }

    private  List <List<Integer>>  preProcessInput(List<String> input){
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        input.forEach(line ->{
            //keep it readable
            line=line.replace("  ", " "); // no empty element followed by number < 10
            String[] numStrings=line.replace("| ", "")
                    .substring(line.indexOf(":")+2)
                    .split(" ");
            Integer[] numbers =
                    Stream.of(numStrings).map(Integer::parseInt).toArray(Integer[]::new);
            result.add(new LinkedList<>(Arrays.asList(numbers)));
        });
        return result;

    }
    private  int determineWinnings(List<Integer> input){
        int winningNumber;
        int count = 0;
        // Assumption: Always 10 winning + 25 own numbers ; Winning numbers are clear
        // Assumption Example : Always 5 winning + 8 own numbers ; Winning numbers are clear
        for (int i=0; i<10;i++) {
            winningNumber = input.remove(0);
            if (input.contains(winningNumber)) {
                count++;
            }
        }
        return count;
    }

    private  int powerVariant(int number){
        return (int) Math.pow(2, number-1);
    }

    private  int determineWinningsPartTwo(List<Integer> input){
        int result=0;
        int[] cardsCount= new int[input.size()];
        for (int i=0; i<input.size(); i++)
            cardsCount[i]=1;
        for (int i=0; i<input.size(); i++){
            for (int j=1; j<input.get(i)+1; j++){
                if (j+i <input.size()){
                    cardsCount[i+j]=cardsCount[i+j] + cardsCount[i];
                }
            }
            result += cardsCount[i];
        }
        return result;
    }
}
