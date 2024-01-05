package de.gfed.AoC;

import java.util.*;

public class DaySeven {
    boolean debugMode;
    AoCInputConnector inputConnector;

    String cardStrength = "23456789TJQKA";

    DaySeven(boolean debugMode, AoCInputConnector inputConnector) {
        this.debugMode = debugMode;
        this.inputConnector = inputConnector;
    }

    public void displayResults() {
        if (debugMode)
            displayResultDeb();
        else
            displayResult();
    }

    public void displayResultDeb() {
        List<String> input = Arrays.asList(
                "32T3K 765",
                "T55J5 684",
                "KK677 28",
                "KTJJT 220",
                "QQQJA 483");
        /*
        You get a list of hands and bids:
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
        The strength of a hand consist of the count and kind of pairs (No Pair, one pair, two pair,
        three of a kind, full house, four, five of a kind) and the first cards (A, K, Q, J, T, 9,...2).
        KK677 is stronger than KTJJT. The hands are ranked by strength and the result is the sum
         of ranks multiplied by bid. (->765 * 1 + 220 * 2 + 28 * 3 + 684 * 4 + 483 * 5=6440)
         */

        //6440
        System.out.println("Day 7: " + processInput(input, false));

        // Oops! Js are jockers und are used the best way. KTJJT is now four of a kind.

        //5905
        System.out.println("Day 7: Part 2" + processInput(input, false));

    }

    public void displayResult() {
        inputConnector.setDay(7);
        List<String> input = inputConnector.getInput();


        //Sol.  250946742
        System.out.println("Day 7: " + processInput(input, false));


        //Sol. 251824095
        System.out.println("Day 7: Part 2" + processInput(input, true));

    }


    private long processInput(List <String> input, boolean bPart2){
        long result=0;
        List<String[]> cards = new ArrayList<>();

        for (String line: input)
             cards.add(processLine(line, bPart2));

        List<String[]> cardsRanked = new ArrayList<>();

        for (int i=1; i<8; i++){
            int finalI = i;
            cardsRanked.addAll(sortGroups(cards.stream().filter(a -> a[1].equals(Integer.toString(finalI))).toList(), bPart2));
        }

        for (int i =0; i < cardsRanked.size(); i++){
            result=result +
                    (i +1)
                    * Long.parseLong(cardsRanked.get(i)[2]);
        }
        return result;
    }
    private List<String[]> sortGroups(List<String[]> input, boolean bPart2){
        List<String[]> result = new ArrayList<>(input);
        Comparator<String[]> compareHand = (String[] hand1, String[] hand2)-> handInOrder(hand1[0], hand2[0], bPart2);
        result.sort(compareHand);
        return result;
    }
    private int handInOrder(String a, String b, boolean bPart2){
        String charStrength = "BCDEFGHILMNOP";
        if (bPart2)
            charStrength = "CDEFGHILMBNOP";
        for (int i=0; i<charStrength.length();i++){
            a=a.replace(cardStrength.substring(i, i+1), charStrength.substring(i, i+1));
            b=b.replace(cardStrength.substring(i, i+1), charStrength.substring(i, i+1));
        }
        return a.compareTo(b) ;
    }
    private String[] processLine(String input, boolean bPart2){
        Map <String, Integer> charCounts = new HashMap<>();
        Integer count=0;
        String key;
        byte twoPair=0;
        for (int i =0; i<5; i++) {
            key=input.substring(i, i + 1);
            count = (charCounts.get(key)==null? 1 : charCounts.get(key) +1);
            charCounts.put(key, count);
            count =0;
        }

        for (Integer occ: charCounts.values()){
            count = count > occ?count: occ;
            if (occ==2) twoPair++;
        }

        if ( bPart2 ) {
            if ( charCounts.get("J") != null ) {
                if ( charCounts.get("J") == count ) {
                    switch (count) {
                        case 1:
                        case 2:
                        case 4:
                            count = twoPair == 2 ? count + 2 : count + 1;
                            twoPair = 0;
                            break;
                        case 3:
                            count = twoPair == 1 ? count + 2 : count + 1;
                    }
                } else {
                    int countJ = charCounts.get("J");
                    if ( count == 2 & twoPair == 2 ) {
                        count = countJ == 1 ? count + 1 : count + 2;
                        twoPair = count == 3 ? 1 : twoPair;
                    } else {
                        count = count + countJ;
                        twoPair = 0;
                    }

                }
            }
        }

        count = count>7? 7 : count;
        if (count>2) count++;
        if (count==2 & twoPair==2) count ++;
        if (count>4) count++;
        if (count==4 & twoPair==1) count ++;

        return new String[]{input.substring(0,5), count.toString(), input.substring(6)};
    }

}