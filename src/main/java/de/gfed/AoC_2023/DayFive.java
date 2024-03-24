package de.gfed.AoC_2023;

import java.util.*;

public class DayFive {
    boolean debugMode;
    AoCInputConnector inputConnector;

    Map<String, long[]> maps = new HashMap<>();
    Map<String, String> transitions = new HashMap<>();
    long[] seeds;
    List<Long[]> seedsList = new ArrayList<>();

    DayFive(boolean debugMode, AoCInputConnector inputConnector){
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
                //"seeds: 79 14 55 13",
                "seeds: 79 14 55 13",
        "seed-to-soil map:",
        "50 98 2",
        "52 50 48",

        "soil-to-fertilizer map:",
        "0 15 37",
        "37 52 2",
        "39 0 15",

        "fertilizer-to-water map:",
        "49 53 8",
        "0 11 42",
        "42 0 7",
        "57 7 4",

        "water-to-light map:",
        "88 18 7",
        "18 25 70",

        "light-to-temperature map:",
        "45 77 23",
        "81 45 19",
        "68 64 13",

        "temperature-to-humidity map:",
        "0 69 1",
        "1 0 69",

        "humidity-to-location map:",
        "60 56 37" ,
        "56 93 4");

        /*
            An Almanac contains several mappings that build together a trail. Seeds (the initial
            value) are successively mapped to location.
            Format of mappings is destination - source - range:
            seed-to-soil map:
            50 98 2
            52 50 48"+
            ->two numbers starting with 98 are mapped to two to numbers starting with 50,
            48 numbers starting with 50 are mapped to 48 to numbers starting with 52
            Others leave unchanged
            => First step: 79->81; 14->14; 55->57 ;13->13

            4 Seeds lead to 4 locations, which is the lowest?
         */

        processInput(input);

        //35
        System.out.println("Day 5: " + Arrays.stream(processTransitions()).min());

        // Seeds consists of a pair, start and range: 79 14 is 79, 80, ... 79+14
        // Sol. for Ex. 46

        System.out.println("Day 5 Part 2: " + processTransitionsPart2());
        // Step X: 79, 14 .. -> 79 1 14 1 ... und dann mit Part2 prozessieren

    }

    public void displayResult(){
        inputConnector.setDay(5);
        List<String> input = inputConnector.getInput();

        processInput(input);

        //Sol. 313045984
        System.out.println("Day 5 (Exp.:313045984): " + Arrays.stream(processTransitions()).min());
        //Sol. 20283860
        System.out.println("Day 5 Part 2 (Exp.:20283860): " + processTransitionsPart2());

    }

    private void processInput(List<String> input ){
        String source="";

        seeds= string2LongArray(input.get(0).substring(input.get(0).indexOf(":")+2));
        for (int i = 0; i<seeds.length; i+=2){
            seedsList.add(new Long[]{seeds[i] ,seeds[i+1]});
        }

        for (int i=1; i<input.size(); i++){
            // Table section begins and is noted in transitions
            if (!Objects.equals(input.get(i), "")){
                if(input.get(i).substring(0,1).matches("[a-z]")) {
                source = input.get(i).split("-to-")[0];
                transitions.put(source, input.get(i).split("-to-")[1]
                        .replace(" map:", ""));
            }
            // Table section is processed
            else if (!Objects.equals(input.get(i), "") & input.get(i).substring(0,1).matches("\\d")) {
                if (maps.get(source)!=null){
                    long [] map = new long[maps.get(source).length+3];
                    System.arraycopy(maps.get(source), 0, map, 0,map.length-3);
                    System.arraycopy(string2LongArray(input.get(i)),0, map, map.length-3, 3);
                    maps.put(source, map);
                }
                else {
                    maps.put(source, string2LongArray(input.get(i)));

                }
            }
        }
        }
    }

    private long[] processTransitions(){
        String target;
        long []map;
        long pos;
        long[] result = new long [seeds.length];
        for (int j = 0; j< seeds.length; j++){
            pos=seeds[j];
            target="seed";
            while (!Objects.equals(target, "location")){
                map = maps.get(target);
                for (int i =0; i<map.length; i += 3){
                    // map: destination, source, range
                    if (map[i+1]<=pos & (map[i+1] + map[i+2])>pos){
                        pos=pos+map[i] - map[i+1];
                        //only first match is relevant
                        i=map.length;
                    }
                }
                target= transitions.get(target);
            }
            result[j]=pos;
        }
        return result;
    }

    private long processTransitionsPart2() {
        String target;
        // input
        List<Long[]> stateList = seedsList;
        // (no) matches per atomic round
        List<Long[]> ignoredList = new ArrayList<>();
        List<Long[]> transitionList = new ArrayList<>();
        long[] map;
        Long[] state;
        long result = Long.MAX_VALUE;
        target = "seed";
        // for all kind of transitions
        while (!Objects.equals(target, "location")) {
            map = maps.get(target);
            // for all transitions of a kind
            for (int i = 0; i < map.length; i += 3) {
                // for all ranges of a (former) seed
                while (!stateList.isEmpty()){
                    state= stateList.remove(0);
                    // map: destination, source, range
                    // no overlap
                    if ((state[0] + state[1]<= map[i+1]) || (state[0] >= map[i+1] + map[i+2])){
                        ignoredList.add(state);
                    }
                    // full overlap
                    else if ((state[0] >= map[i+1]) && (state[0] + state[1] <= map[i+1] + map[i+2])){
                        transitionList.add(new Long[]{state[0]+map[i] - map[i+1], state[1]});
                    }
                    // partial/comprehensive overlap
                    else {
                        // cut head
                        if (state[0]< map[i+1]){
                            ignoredList.add(new Long[]{state[0], map[i+1] - state[0]});
                            state[1] = state[0] - map[i+1] + state[1];
                            state[0] = map[i+1];
                        }
                        // cut tail
                        if (state[0] +state[1] > map[i+1] + map[i+2]){
                            ignoredList.add(new Long[]{map[i+1] + map[i+2], state[0] + state[1] - map[i+1] - map[i+2]});
                            state[1] = map[i+1] + map[i+2] - state[0];
                        }
                        state[0]=  state[0] + map[i] - map[i+1] ;
                        transitionList.add(state);
                    }
                }
                while (!ignoredList.isEmpty()){
                    stateList.add(ignoredList.remove(0));
                }
            }
            while (!transitionList.isEmpty()){
                stateList.add(transitionList.remove(0));
            }
            target = transitions.get(target);
        }
        while (!stateList.isEmpty()){
            state=stateList.remove(0);
            result= state[0]<result? state[0] : result ;
        }
        return result;
    }

    private long[] string2LongArray(String line){
        return Arrays.stream(
                        line.split(" "))
                .mapToLong(Long::parseLong).toArray();
    }

}
