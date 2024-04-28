package de.gfed.AoC_2023;

import java.util.*;
import java.util.stream.Collectors;

public class DayTwentyFive extends Day{
    Map<String, List<String>> wiring;
    List<String> nodes;
    List <Cut> cuts = new ArrayList<>();

    DayTwentyFive(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 25);

        expectations=new long[]{552682,0};
        example = Arrays.asList(
                "jqt: rhn xhk nvd",
                "rsh: frs pzl lsr",
                "xhk: hfx",
                "cmg: qnr nvd lhk bvb",
                "rhn: xhk bvb hfx",
                "bvb: xhk hfx",
                "pzl: lsr hfx nvd",
                "qnr: nvd",
                "ntq: jqt hfx bvb xhk",
                "nvd: lhk",
                "lsr: lhk",
                "rzs: qnr cmg lsr rsh",
                "frs: qnr lhk lsr");  // hfx/pzl, bvb/cmg, nvd/jqt ->54

        /*
        One last thing to do: The massive snow-maschine, consisting of many little machines, is overloaded.
        You have a wiring diagramm. Each line show a module that is bidirectional connect to other modules.
        You have to disconnect three wires (= delete one module on the right side) to divide the modules in
        two separate groups. Multiply the sizes of these two groups.

        Part 2:
        is for free!
        */

    }

    protected long evalInput(boolean bPart2) {
        if (!bPart2) {
            // takes about 4 min.
            wiring= new HashMap<String, List<String>>();
            createWiring(input);
            solveMinCut(nodes);
            return calcResult();
        }
        return 0;
    }

    private int calcResult(){
        cuts.sort((o1, o2) -> o1.weight() - o2.weight);
        String[] partGraph= cuts.get(0).node1.split(",");
        return partGraph.length * (nodes.size() - partGraph.length);

    }

    private void createWiring(List<String> input){
        nodes= new ArrayList<>();
        wiring=new HashMap<>();
        for (String line : input) {
            String[] keys = line.replace(":", "").split(" ");
                String key=keys[0];
                if(!nodes.contains(key))
                    nodes.add(key);
                if (!wiring.containsKey(key))
                    wiring.put(key, new ArrayList<>());
                for (int j=1; j< keys.length; j++){
                    if (!wiring.containsKey(keys[j]))
                        wiring.put(keys[j], new ArrayList<>());
                    if(!nodes.contains(keys[j]))
                        nodes.add(keys[j]);
                    wiring.get(key).add(keys[j]);
                    wiring.get(keys[j]).add(key);
                }
        }
    }

    private void solveMinCut(List <String> nodes){
        // https://i11www.iti.kit.edu/_media/teaching/winter2012/algo2/vorlesung5.pdf

        if (nodes.size()==2)
            return;

        List <String> neighbours=new ArrayList<>();
        List <String> unDone = new ArrayList<>(nodes);
        List <String> done=new ArrayList<>();
        String key = unDone.remove(0);
        done.add(key);
        while (unDone.size()>1){
            neighbours.addAll(wiring.get(key));
            neighbours.removeAll(done);
            key=countNeighbours(neighbours);
            unDone.remove(key);
            done.add(key);
        }
        done.add(combineNodes(unDone.get(0), key));
        done.remove(key);
        cuts.add(new Cut(unDone.get(0), key, wiring.get(unDone.get(0)).size()));
        solveMinCut(done);
    }

    private String countNeighbours(List<String> neighbours){
        return neighbours.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).get();
    }

    private String combineNodes(String node1, String node2){
        String key = node1 + "," + node2;

        List<String> neighbours = new ArrayList<>(wiring.get(node1));
        neighbours.addAll(wiring.get(node2));

        neighbours.removeAll(Collections.singleton(node1));
        neighbours.removeAll(Collections.singleton(node2));

        wiring.put(key, neighbours);

        wiring.forEach((label, neighbourhood) ->{
            Collections.replaceAll(neighbourhood, node1, key);
            Collections.replaceAll(neighbourhood, node2, key);
        });
        return key;
    }
    public record Cut (String node1, String node2, int weight){}
}
