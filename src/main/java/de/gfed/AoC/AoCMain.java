package de.gfed.AoC;


public class AoCMain {
    public static void main(String[] args) {
        boolean isDebug=true;
        AoCInputConnector inputConnector = new AoCInputConnector();

        /*
        Paste your session cookie here (Browser ->Log into www.adventofcode.com ->F12 ->App ->
        Cookies (on the left) ->adventofcode.com ->session
         */

        String cookie="";
        inputConnector.setCookie("session=" + cookie);
        DayOne dayOne= new DayOne(isDebug, inputConnector);
        dayOne.displayResults();
        DayTwo dayTwo= new DayTwo(isDebug, inputConnector);
        dayTwo.displayResults();
        DayThree dayThree= new DayThree(isDebug, inputConnector);
        dayThree.displayResults();
        DayFour dayFour= new DayFour(isDebug, inputConnector);
        dayFour.displayResults();

    }
}