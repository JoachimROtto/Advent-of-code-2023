package de.gfed.AoC_2023;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toMap;

public class DayNineteen extends Day{
    HashMap<String, List<Rule>> workflows;
    List<Part> parts = new ArrayList<>();

    DayNineteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 19);
        expectations=new long[]{353046,125355665599537L};
        example = Arrays.asList(
                "px{a<2006:qkq,m>2090:A,rfg}",
                "pv{a>1716:R,A}",
                "lnx{m>1548:A,A}",
                "rfg{s<537:gd,x>2440:R,A}",
                "qs{s>3448:A,lnx}",
                "qkq{x<1416:A,crn}",
                "crn{x>2662:A,R}",
                "in{s<1351:px,qqz}",
                "qqz{s>2770:qs,m<1801:hdj,R}",
                "gd{a>3333:R,R}",
                "hdj{m>838:A,pv}",
                "",
                "{x=787,m=2655,a=1222,s=2876}",
                "{x=1679,m=44,a=2067,s=496}",
                "{x=2036,m=264,a=79,s=2244}",
                "{x=2461,m=1339,a=466,s=291}",
                "{x=2127,m=1623,a=2188,s=1013}"); //->62
        /*
        The elves organize their spare parts according to a system. You will receive a plan and a parts list.
        A part is rated in four categories
        - x: Extremely cool looking
        - m: Musical (it makes a noise when you hit it)
        - a: Aerodynamic
        - s: Shiny
        =>{x=787,m=2655,a=1222,s=2876} and passes workflows to consider acceptance or rejection.
        Workflow ex{x>10:one,m<20:two,a>30:R,A}: x>10? pass to Workflow one .... a>30? reject,
        no rule matches? accept. The first rule that matches the part being considered is applied immediately
        and the part never returns to this workflow again.

        Add the ratings of every category of every accepted part.

        Part 2:
        That's not fast enough. Values of ratings can have a range from 1 to 4000. Preprocess a list of all accepted combinations.
        How long is this list.
        */

    }

    protected long evalInput(boolean bPart2) {
        input=new ArrayList<>(input);
        if (!bPart2) {
            workflows = parseWorkflows(input);
            parts = parseParts(input);
            return sumRatingsAcceptedParts(filterParts(parts));
        }

        Map<Character, Integer[]> validValues= new HashMap<>();
        validValues.put('x' , new Integer[]{1, 4000});
        validValues.put('m' , new Integer[]{1, 4000});
        validValues.put('a' , new Integer[]{1, 4000});
        validValues.put('s' , new Integer[]{1, 4000});

        return moveThroughWorkflowAndCount(validValues, "in");
    }

    private long moveThroughWorkflowAndCount(Map<Character, Integer[]> input, String nextWorkflow){
        long result= 0;
        Map<Character, Integer[]> matchedInput;
        Map<Character, Integer[]> remainingInput;
        Integer[] matchedBandwidth;
        Integer[] remainingBandwidth;
        if (nextWorkflow.equals("A")){
            result=1;
            for (Map.Entry<Character, Integer[]> entry : input.entrySet()) {
                result = result * (entry.getValue()[1] - entry.getValue()[0]+1);
            }
            return result;
        }
        if (nextWorkflow.equals("R"))
            return 0;
        matchedInput = deepCopy(input);//new HashMap<>(input);
        remainingInput = deepCopy(input);//new HashMap<>(input);
        for (Rule rule : workflows.get(nextWorkflow)) {
            if (!rule.catchAll){
                matchedBandwidth=matchedInput.get(rule.category);
                remainingBandwidth=remainingInput.get(rule.category);
                if (rule.beBigger){
                    matchedBandwidth[0]=Math.max(matchedBandwidth[0], rule.value)+1;
                    remainingBandwidth[1]=Math.min(remainingBandwidth[1], rule.value);
                } else{
                    matchedBandwidth[1]=Math.min(matchedBandwidth[1], rule.value)-1;
                    remainingBandwidth[0]=Math.max(remainingBandwidth[0], rule.value);
                }
            } else {
                matchedInput=deepCopy(remainingInput);
            }
            result+= moveThroughWorkflowAndCount(matchedInput, rule.target);
            matchedInput=deepCopy(remainingInput);
        }
        return result;
    }

    private Map<Character, Integer[]> deepCopy(Map<Character, Integer[]> source){
        Map<Character, Integer[]> result=new HashMap<>();
        source.forEach((category, value) ->{
            result.put(category, new Integer[]{value[0], value[1]});
        });
        return result;
    }

        private long sumRatingsAcceptedParts(List<Part> parts){
        AtomicLong result = new AtomicLong();
        for (Part part : parts) {
            part.ratings.forEach((category, value) -> {
                result.addAndGet(value);
            });
        }
        return result.get();
    }

    private List<Part> filterParts(List<Part> parts){
        List<Part> result=new ArrayList<>();
        parts.forEach(part ->{
            if( chainedAcceptance(part,"in") )
                result.add(part);
        });
        return result;
    }

    private boolean chainedAcceptance(Part part, String workflow){
        if (workflow.equals("A"))
            return true;
        if (workflow.equals("R"))
            return false;
        String next;
        List<Rule> rules = workflows.get(workflow);
        for(int i=0; i<rules.size(); i++){
            if(!(next=getNextStep(part, rules.get(i))).isEmpty())
                return chainedAcceptance(part, next);
        }
        // dead end considered as reject
        return false;
    }

    private String getNextStep (Part part, Rule rule){
        if ( rule.catchAll )
            return rule.target;
        if (part.ratings.get(rule.category)> rule.value && rule.beBigger )
            return rule.target;
        if (part.ratings.get(rule.category)< rule.value && !rule.beBigger )
            return rule.target;
        return "";
    }

    private HashMap<String, List<Rule>> parseWorkflows(List<String> input){
        HashMap<String, List<Rule>> result = new HashMap<>();
        String line;
        while (!(line = input.remove(0)).isEmpty()){
            List<Rule> rules=new ArrayList<>();
            String[] rulesRaw = line.substring(line.indexOf('{')+1, line.length()-1).split(",");
            for (String ruleRaw : rulesRaw){
                if (!ruleRaw.contains(":")){
                    rules.add(new Rule('-', -1, false, ruleRaw, true));
                } else {
                    rules.add(new Rule(ruleRaw.charAt(0), Integer.parseInt(ruleRaw.substring(2, ruleRaw.indexOf(':'))),
                            ruleRaw.contains(">"), ruleRaw.substring(ruleRaw.indexOf(":")+1) , false));
                }
            }
            result.put(line.substring(0, line.indexOf("{")), rules);
        }
        return result;
    }

    private List<Part> parseParts(List<String> input){
        List<Part> result = new ArrayList<>();
        while (!input.isEmpty()){
            String line = input.remove(0);
            String[] ratings = line.substring(1, line.length()-1).split(",");
            List<Number> numbers=new ArrayList<>();
            for (String rating: ratings){
                numbers.add(new Number(rating.charAt(0), Integer.parseInt(rating.substring(2))));
            }
            result.add(new Part(numbers));
        }
        return result;
    }
    record Part (Map<Character, Integer> ratings){
        public Part(List<Number> numbers){
            this(numbers.stream().collect(toMap(n -> n.category, n -> n.value)));
        }
    }
    record Number(char category, Integer value){}
    record Rule(char category, int value, boolean beBigger, String target, boolean catchAll){}
}