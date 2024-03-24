package de.gfed.AoC;

import java.util.*;
import java.util.stream.IntStream;

public class DayTwelve {
    boolean debugMode;
    Map<String, Long> done;

    AoCInputConnector inputConnector;

    DayTwelve(boolean debugMode, AoCInputConnector inputConnector) {
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
                "???.### 1,1,3",
                ".??..??...?##. 1,1,3",
                "?#?#?#?#?#?#?#? 1,3,1,6",
                "????.#...#... 4,1,1",
                "????.######..#####. 1,6,5",
                "?###???????? 3,2,1");

        /*
        ?##??????#? 3,2,2
        ?## ? ?? ? ?? #?
        ?## ? ? ?? ?? #?
        ?## ? ?? ?? ? #?
        ? ##? ? ? ?? ? #?
        ? ##? ? ?? ?? #?
        ....
        You are given a record of working(#), not working(.) and unknown(?) springs. Each row ends with
        ordered number of working springs (1,1,3: there is one spring and another spring and 3 springs, each working
        and groupwise separated by non-working springs).
        How many different arrangements would fit the setting of a row? Sum the counts.
        Ex.: 1+4+1+1+4+10=21
         */

        System.out.println("Day 12 (Exp. 21): " + input.stream().mapToLong(this::evalRow).sum());
        input = unfoldForPartTwo(input);
        System.out.println("Day 12 Part 2 (Exp. 525152): " + input.stream().mapToLong(this::evalRow).sum());
    }

    public void displayResult() {
        inputConnector.setDay(12);
        List<String> input = inputConnector.getInput();

        System.out.println("Day 12 (Exp. 6852): " + input.stream().mapToLong(this::evalRow).sum());

        input = unfoldForPartTwo(input);
        System.out.println("Day 12 Part 2 (Exp. 8475948826693): " + input.stream().mapToLong(this::evalRow).sum());

    }

    private long evalRow(String line){
        int[] groupSizesWorkingSprings = Arrays.stream(
                line.substring(line.indexOf(" ")+1).split(",")).mapToInt(Integer::parseInt).toArray();

        done = new HashMap<>();
        return countPosibilities(line.substring(0, line.indexOf(" ")), 0, groupSizesWorkingSprings,0);
    }

    private long countPosibilities(String line, int lineStart, int[] springs, int curSpring){
        // Ende der Zeile, Ende der Quellen auch?
        if (line.length()<=lineStart)
            return springs.length==curSpring?1:0;

        //Ende der Quellen, Zeile (bzw. #) auch?
        if (springs.length==curSpring)
            return (line.substring(lineStart).contains("#"))?0:1;

        // Abbruch, weil unerfüllbar, wird erreicht!
        if (line.length()<lineStart+springs[curSpring] )
            return 0;

        if (isCached(line, lineStart, springs, curSpring))
            return getCache(line, lineStart, springs, curSpring);

        long result=0;

        // Rekursion nach Zeichen bis zur ersten # (->Quelle zwingend erarbeiten)
        if (!(line.charAt(lineStart)=='#')){
            result = result + countPosibilities(line, lineStart+1, springs, curSpring);
        }

        // Test und ggf. Abbruch: Kann die line mit der Quelle beginnen? (keine Punkte drin, keine # dahinter)
        if ((line.substring(lineStart, lineStart+ springs[curSpring]).contains("."))
                || ((line.length()>=lineStart+springs[curSpring]+1) &&
                (line.charAt(lineStart+ (springs[curSpring]))=='#')))
            return setAndReturnCache(line, lineStart, springs, curSpring, result);

        result =result + countPosibilities(line, lineStart+springs[curSpring] +1, springs, curSpring+1);
        return setAndReturnCache(line, lineStart, springs, curSpring, result);
    }

    private  List<String> unfoldForPartTwo(List<String> input) {
        List<String> result = new ArrayList<String>();
        input.forEach(line ->{
            result.add(
                    String.join("?", Collections.nCopies(5, line.substring(0, line.indexOf(" ")))) + " " +
                            String.join(",", Collections.nCopies(5, line.substring(line.indexOf(" ")+1)))
            );
        });
        return result;
    }

    private boolean isCached(String line, int lineStart, int[] springGroups, int groupStart){
        return (done.get(
                line.substring(lineStart) + Arrays.toString(Arrays.copyOfRange(springGroups, groupStart, springGroups.length))
        )!=null);
    }
    private long getCache(String line,int lineStart, int[] springGroups, int groupStart){
        return done.get(
                line.substring(lineStart) + Arrays.toString(Arrays.copyOfRange(springGroups, groupStart, springGroups.length))
        );
    }
    private long setAndReturnCache( String line, int lineStart, int[] springGroups, int groupStart, long result){
        done.put(
                line.substring(lineStart) + Arrays.toString(Arrays.copyOfRange(springGroups, groupStart, springGroups.length)),
                result
        );
        return result;
    }
}