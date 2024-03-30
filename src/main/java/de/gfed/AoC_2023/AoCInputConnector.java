package de.gfed.AoC_2023;

import java.io.*;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.nio.file.Files;

public class AoCInputConnector {
    private final String urlPrefix = "https://adventofcode.com/2023/day/";
    private final String urlPostfix="/input";
    private String cookie;
    private int day;
    AoCInputConnector(){
        setCookie();
    }

    public void setDay(int day){
        this.day=day;
    }
    private void setCookie(){
        /*
        Paste your session cookie

        Browser ->Log into www.adventofcode.com ->F12 ->App ->
        Cookies (on the left) ->adventofcode.com ->session

        in <Projectdirectory>cookie.txt or below
         */
        cookie = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("cookie.txt"));
            cookie = bufferedReader.readLine();
            bufferedReader.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        cookie = "session=" + cookie;
    }

    public List<String>  getInput() {
        try {
            return fetchContentFromFile();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return fetchAndCacheContentFromURL();
        }
    }

    private  List<String> fetchContentFromFile() throws IOException {
        List<String> result = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("input/input-Day" + day + ".txt"));
        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            result.add(line);
        }
        return result;
    }
    private  List<String> fetchAndCacheContentFromURL() {
        String URL = urlPrefix + day + urlPostfix;
        List<String> result = new ArrayList<>();

        try {
            java.net.URL oracle = new URI(URL).toURL();
            URLConnection conn = oracle.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; pl; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2");
            conn.addRequestProperty("Cookie", cookie);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Files.createDirectories(Paths.get("input"));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("input/input-Day" + day + ".txt"));
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
