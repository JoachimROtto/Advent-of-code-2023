package de.gfed.AoC;


public class AoCMain {
    public static void main(String[] args) {
        boolean isDebug=false;
        AoCInputConnector inputConnector = new AoCInputConnector();

        /*
        Paste your session cookie here (Browser ->Log into www.adventofcode.com ->F12 ->App ->
        Cookies (on the left) ->adventofcode.com ->session
         */
        String cookie="53616c7465645f5f00dda7434672b8c6c1de93c4b8f8b08c4f4dbcabe99b5ce564b709f10d180d5f072e6e051614efab6bd6f13eae3135a5d9ea1e1b550d3db3";

        inputConnector.setCookie("session=" + cookie);

        boolean checkAll=false;

        if (checkAll){
            DayOne dayOne= new DayOne(isDebug, inputConnector);
            dayOne.displayResults();
            DayTwo dayTwo= new DayTwo(isDebug, inputConnector);
            dayTwo.displayResults();
            DayThree dayThree= new DayThree(isDebug, inputConnector);
            dayThree.displayResults();
            DayFour dayFour= new DayFour(isDebug, inputConnector);
            dayFour.displayResults();
            DayFive dayFive= new DayFive(isDebug, inputConnector);
            dayFive.displayResults();
            DaySix daySix= new DaySix(isDebug, inputConnector);
            daySix.displayResults();
        }

    }
}