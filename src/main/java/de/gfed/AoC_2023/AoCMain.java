package de.gfed.AoC_2023;

import java.io.*;

public class AoCMain {
    public static void main(String[] args) {
        AoCInputConnector inputConnector = new AoCInputConnector();

        boolean isDebug = false;
        boolean checkAll = true;

        Day[] days = new Day[]{
                new DayOne(isDebug, inputConnector),
                new DayTwo(isDebug, inputConnector),
                new DayThree(isDebug, inputConnector),
                new DayFour(isDebug, inputConnector),
                new DayFive(isDebug, inputConnector),
                new DaySix(isDebug, inputConnector),
                new DaySeven(isDebug, inputConnector),
                new DayEight(isDebug, inputConnector),
                new DayNine(isDebug, inputConnector),
                new DayTen(isDebug, inputConnector),
                new DayEleven(isDebug, inputConnector),
                new DayTwelve(isDebug, inputConnector),
                new DayThirteen(isDebug, inputConnector)
        };
        for (int i =(checkAll?0:days.length-1); i<days.length; i++){
            days[i].displayResults();
        }
        System.out.println("Expectations are individual");

    }
}