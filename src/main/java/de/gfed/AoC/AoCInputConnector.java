package de.gfed.AoC;

import java.io.*;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.ArrayList;


public class AoCInputConnector {
    private final String urlPrefix = "https://adventofcode.com/2023/day/";
    private final String urlPostfix="/input";
    private String cookie;
    private int day;
    AoCInputConnector(){
    }

    public void setDay(int day){
        this.day=day;
    }
    public void setCookie(String cookie){
        this.cookie=cookie;
    }
    public List<String>  getInput() {
        List<String> result = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("input-Day" + day + ".txt"));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                result.add(line);
            }
            return result;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return fetchContentFromURL();
        }
    }
    private  List<String> fetchContentFromURL() {
        String URL = urlPrefix + day + urlPostfix;
        List<String> result = new ArrayList<>();

        try {
            java.net.URL oracle = new URI(URL).toURL();
            URLConnection conn = oracle.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; pl; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2");
            conn.addRequestProperty("Cookie", cookie);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("input-Day" + day + ".txt"));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                result.add(line);
                bufferedWriter.write(line + "\n");
                bufferedWriter.flush();
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
