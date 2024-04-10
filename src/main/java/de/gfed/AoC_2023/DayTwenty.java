package de.gfed.AoC_2023;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class DayTwenty extends Day{
    long low=0;
    long high=0;
   // Map<String, Boolean> ffStaates = new HashMap<>();
    Map<String, Module> predecessorOfRX = new HashMap<>();
    Map<String, Integer> predOfRXMet = new HashMap<>();
    Map<String, Long> predOfRXWait = new HashMap<>();

    DayTwenty(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 20);
        expectations=new long[]{791120136,215252378794009L};
        example = Arrays.asList(
                "broadcaster -> a",
                "%a -> inv, con",
                "&inv -> b",
                "%b -> con",
                "&con -> output"); //->8000*4000=32000000
        /*
        Now that the machines are ready they need a signal to boot up. There is a network of modules. Some
        each are connected and each is forwarding a (modified) signal. There is a module configuration that
        list each connection and the module:
        - names are modules
        - -> is a connection to one or more comma-separated modules
        - prefix % marks a flip-flop that holds state (on/off), flips state on low pulse and sends
        low (was on) or high (was off) pulse
        - prefix & marks a conjunction module that holds state (last pulse from each input), updates
        on input and sends high (all inputs now hold high) or low (otherwise) pulse
        - name broadcaster marks a module that multiply incoming signal
        - button (hidden) applies a single low pulse to broadcaster when pushed
        - output is the output
        Initially flip-flops are off and conjunction modules hold low pulses. Pulses are processed in the
        order they are sent and new button pulses wait for a finished process.
        If you push the button 1000 times. How many low and high pulse pass through any cable? Multiply!


        Part 2:
        In this network there is a module named "rx" that starts the machine when it receives a single low pulse during
        the entire "button-cycle". How many times needs the button to be pushed to meet this criterion?
        */

    }

    protected long evalInput(boolean bPart2) {
        Map<String, Module> modules;

        if (!bPart2) {
            modules=parseModules(input);
            for (int i=0; i<1000; i++)
                pushButton(modules,i,bPart2);
            return (low*high);// pushButton(modules, 1000);
        }

        long result;
        modules=parseModules(input);
        for (int i=1; i<100000; i++) {
            result = pushButton(modules, i, bPart2);
            if ( result != 0 )
                return result;
        }
        return 0;
    }

    private long pushButton(Map<String, Module> modules, int times, boolean bPart2){
        AtomicLong result = new AtomicLong();
        List<Module> activeModules = new ArrayList<>();
        Module activeModule= modules.get("broadcaster");
        Module nextModule;
        low++;
        activeModule.pendingInput.add(false);
        activeModules.add(activeModule);
        boolean pulse;
        while(!activeModules.isEmpty()){
            activeModule= activeModules.remove(0);
            pulse=processPulse(activeModule);

            // part 2
            if ( bPart2&& predOfRXMet.containsKey(activeModule.name) && pulse ){
                predOfRXMet.put(activeModule.name, predOfRXMet.get(activeModule.name)+1);
                if (!predOfRXWait.containsKey(activeModule.name))
                    predOfRXWait.put(activeModule.name, (long) times);
                if (!predOfRXMet.containsValue(0)){
                    result.set(1);
                    predOfRXWait.forEach((name, value) ->{
                        result.set(result.get() * value / gcd(result.get(), value));
                    });
                    return result.get();
                }
            } // end part 2

            for (String successor : activeModule.successors) {
                nextModule=modules.get(successor);
                if ( nextModule.conjunction )
                    nextModule.predecessorHigh.put(activeModule.name, pulse);
                if (!pulse || !nextModule.flipFlop) {
                    nextModule.pendingInput.add(pulse);
                    activeModules.add(nextModule);
                }
                if (pulse){
                    high++;
                } else {
                    low++;
                }
            }
        }
        return 0;
    }
    private long gcd(long a, long b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }
    private boolean processPulse(Module module){
        boolean pulse= module.pendingInput.remove(0);
        if ( module.broadcaster )
            return pulse;
        if ( module.conjunction ){
            return module.predecessorHigh.containsValue(false);
        }
        if ( module.flipFlop ) {
            if (!pulse){
                module.ffState[0]=!module.ffState[0];
                }
            return module.ffState[0];
        }
        return pulse;

    }

    private Map<String, Module> parseModules(List<String> input){
        Map<String, Module> result = new HashMap<>();
        Module item = null;
        for (String line : input) {
            List<String> successor=Arrays.stream(line.substring(line.indexOf(">")+2).split(", ")).toList();
            switch (line.charAt(0)){
                case 'b':
                    item = new Module("broadcaster",
                            true,false, false,new boolean[]{false}, new ArrayList<>(),new HashMap<>(),
                            successor);
                    break;
                case '%':
                    item = new Module(line.substring(1, line.indexOf(" ")),
                            false,true, false,new boolean[]{false}, new ArrayList<>(),new HashMap<>(),
                            successor);
                    break;
                case '&':
                    Map<String, Boolean> inputHigh = new HashMap<>();
                    item = new Module(line.substring(1, line.indexOf(" ")),
                            false,false, true,new boolean[]{false}, new ArrayList<>(),inputHigh,
                            successor);
                    break;
            }
            // part 2
            if (line.substring(line.indexOf(">")+2).contains("rx"))
                predecessorOfRX.put(item.name, item);
            Module finalItem = item;
            predecessorOfRX.forEach((s, module) -> {
                if (line.substring(line.indexOf(">")+2).contains(s))
                    predOfRXMet.put(finalItem.name, 0);
            });  //end Part 2

            result.put(item.name, item);
        }
        // dead ends
        result.put("output", new Module("output", false, false, false, new boolean[]{false}, new ArrayList<>(), new HashMap<>(), new ArrayList<>()));
        result.put("rx", new Module("output", false, false, false, new boolean[]{false}, new ArrayList<>(), new HashMap<>(), new ArrayList<>()));

        result.forEach((name, module) ->{
            if (module.successors!=null){
            for (String successor : module.successors) {
                if (result.get(successor).conjunction )
                    result.get(successor).predecessorHigh.put(name, false);
            }
        }});
        return result;
    }

    // ffState is array to bypass immutability
    record Module(String name,
                  boolean broadcaster, boolean flipFlop, boolean conjunction, boolean[] ffState, List<Boolean> pendingInput,
                  Map<String, Boolean> predecessorHigh, List<String> successors){}
}