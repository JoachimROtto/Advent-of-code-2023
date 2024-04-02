package de.gfed.AoC_2023;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class DayFifteen  extends Day{


    DayFifteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 15);
        expectations=new long[]{513214,258826};
        example = Arrays.asList(
                "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"); // expected: 1320

        /*
        You need to run a Hash-algorithm for some sequences. The algorithm is
        - Start with 0
        - Add ASCII-Code of character
        - multiply with 17 divide by 256, remainder is the start for the next character
        (rn=1 leads to 30)

        Calculate the sum.

        Part2:
        Now you can read the instructions. These specify how different boxes should be equipped
        with lenses. cm=5 means that a lens with a focal length of 5 should be marked as cm and
        inserted behind the other lenses. An existing cm lens would be replaced. tp- removes a tp
        lens. The hash of the label () indicates the correct box.
        The boxes are in a row and focus the light of the reflector dish. Calculate the power:
        sum number of box + 1 * number of Slot * focal length.
        Box 0 with rn 1 and ot 7 leads to (1 * 1 * 1) + (1 * 2 * 7) = 15

        */
    }

    protected long evalInput(boolean bPart2) {
        if(!bPart2)
            return sumHashes(input.get(0));

        return calculateStrength(input.get(0));
    }

    private long sumHashes (String line){
        String[]strings = line.split(",");
        return Arrays.stream(strings).mapToLong(this::getHash).sum();
    }

    private long calculateStrength (String line){
        AtomicLong result= new AtomicLong();
        String[]strings = line.split(",");
        List< AbstractMap.SimpleEntry<String, String>> box;
        Map <Integer, List< AbstractMap.SimpleEntry<String, String>>>Boxes = new HashMap<>();
        for (int i=0; i<strings.length;i++){
            String[] elements= strings[i].split("=");
            Boxes.computeIfAbsent(getHash(elements[0],true), k -> new ArrayList<>());
            if (elements.length==1){
                box=Boxes.get(getHash(elements[0],true));
                for (int j = 0; j<box.size(); j++){
                    if (box.get(j).getKey().equals(elements[0].replace("-",""))){
                        box.remove(j);
                        break;
                    }
                }
            } else {
                box=Boxes.get(getHash(elements[0]));
                int lensPosition=-1;
                for (int j = 0; j<box.size(); j++){
                    if (box.get(j).getKey().equals(elements[0])){
                        lensPosition=j;
                        break;
                    }
                }
                if (lensPosition!=-1){
                    box.set(lensPosition,new AbstractMap.SimpleEntry<>(elements[0], elements[1]));
                } else {
                    box.add(new AbstractMap.SimpleEntry<>(elements[0], elements[1]));
                }
            }
        }
        Boxes.forEach((key, list) -> {
            result.addAndGet(boxPower(key, list));
        });


        return result.get();
    }

    private int boxPower(int boxNumber, List<AbstractMap.SimpleEntry<String, String>> lenses){
        int result=0;
        for (int i=0; i<lenses.size();i++){
            result += (boxNumber+1) * (i+1) * Integer.parseInt(lenses.get(i).getValue());
        }
        return result;
    }

    private int getHash(String element, boolean bPart2){
        int result =0;
        for (int i=0; i<element.length();i++){
            if (bPart2&& (int) element.charAt(i) <97)
                return result;
            result = ((result + (int) element.charAt(i)) *17)%256;
        }
        return result;
    }

    private int getHash(String element){
        return getHash(element, false);
    }
}