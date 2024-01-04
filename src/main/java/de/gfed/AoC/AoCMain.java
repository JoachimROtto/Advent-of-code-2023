package de.gfed.AoC;


public class AoCMain {
    public static void main(String[] args) {
        boolean isDebug=false;
        AoCInputConnector inputConnector = new AoCInputConnector();

        /*
        Paste your session cookie here (Browser ->Log into www.adventofcode.com ->F12 ->App ->
        Cookies (on the left) ->adventofcode.com ->session
         */
        String cookie="";

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
        DaySeven daySeven= new DaySeven(isDebug, inputConnector);
        daySeven.displayResults();

    }
}