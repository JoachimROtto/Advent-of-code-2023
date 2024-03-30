package de.gfed.AoC_2023;

import java.io.*;

public class AoCMain {
    public static void main(String[] args) {
        AoCInputConnector inputConnector = new AoCInputConnector();

        /*
        Paste your session cookie
        (Browser ->Log into www.adventofcode.com ->F12 ->App ->
        Cookies (on the left) ->adventofcode.com ->session
        in <Projectdirectory>cookie.txt
         */
        String cookie = "";
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader("cookie.txt"));
            cookie = bufferedReader.readLine();
            bufferedReader.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        inputConnector.setCookie("session=" + cookie);

        boolean isDebug = false;
        boolean checkAll = false;

        if ( checkAll ) {
            DayOne dayOne = new DayOne(isDebug, inputConnector,1 );
            dayOne.displayResults();
            DayTwo dayTwo = new DayTwo(isDebug, inputConnector);
            dayTwo.displayResults();
            DayThree dayThree = new DayThree(isDebug, inputConnector);
            dayThree.displayResults();
            DayFour dayFour = new DayFour(isDebug, inputConnector);
            dayFour.displayResults();
            DayFive dayFive = new DayFive(isDebug, inputConnector);
            dayFive.displayResults();
            DaySix daySix = new DaySix(isDebug, inputConnector);
            daySix.displayResults();
            DaySeven daySeven = new DaySeven(isDebug, inputConnector);
            daySeven.displayResults();
            DayEight dayEight = new DayEight(isDebug, inputConnector);
            dayEight.displayResults();
            DayNine dayNine= new DayNine(isDebug, inputConnector);
            dayNine.displayResults();
            DayTen dayTen= new DayTen(isDebug, inputConnector);
            dayTen.displayResults();
            DayEleven dayEleven= new DayEleven(isDebug, inputConnector);
            dayEleven.displayResults();
            DayTwelve dayTwelve = new DayTwelve(isDebug, inputConnector);
            dayTwelve.displayResults();
            System.out.println("Expectations are individual");
            DayThirteen dayThirteen = new DayThirteen(isDebug, inputConnector);
            dayThirteen.displayResults();
        }
        Day One = new DayOne(isDebug, inputConnector, 1);
        One.displayResults();

    }
}