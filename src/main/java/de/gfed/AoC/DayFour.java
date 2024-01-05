package de.gfed.AoC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayFour {
    boolean debugMode;
    AoCInputConnector inputConnector;

    DayFour(boolean debugMode, AoCInputConnector inputConnector){
        this.debugMode=debugMode;
        this.inputConnector=inputConnector;
    }

    public void displayResults(){
        if (debugMode)
            displayResultDeb();
        else
            displayResult();
    }

    public void displayResultDeb(){
        List<String> input = Arrays.asList(
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

        List <List<Integer>> numbers = preProcessInput(input);

        List<Integer> winnings= new ArrayList<Integer>();
        for( List<Integer> number : numbers) {
            winnings.add(determineWinnings(number));
        }

        System.out.println("Day 4: " + winnings.stream().mapToInt(this::power).sum());

        System.out.println("Day 4 Part 2: " + determineWinningsPartTwo(winnings));

    }

    public void displayResult(){
        inputConnector.setDay(4);
        List<String> input = inputConnector.getInput();

        List <List<Integer>> numbers = preProcessInput(input);
        List<Integer> winnings= new ArrayList<Integer>();
        for( List<Integer> number : numbers) {
            winnings.add(determineWinnings(number));
        }
        //Sol. 26218
        System.out.println("Day 4 (Exp.:26218): " + winnings.stream().mapToInt(this::power).sum());
        //System.out.println("Day 4 Part 2: " + numbers2.stream().mapToInt(DayFour::determineWinningsPartTwo).sum());

        /*
        Ooops, new rules on the back. N Winnings get no points but copies of the next N cards!
        And the copies play.
         */
        //Sol. 9997537
        System.out.println("Day 4 Part 2 (Exp.:9997537): " + determineWinningsPartTwo(winnings));

    }

    private  List <List<Integer>>  preProcessInput(List<String> input){
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        input.stream().forEach(line ->{
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
        int winningNumber = 0;
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

    private  int power(int number){
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
