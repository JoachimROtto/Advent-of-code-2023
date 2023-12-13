package de.gfed.AoC;

import java.io.BufferedReader;
import java.net.URI;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.List;
import java.util.ArrayList;


public class AoCInputConnector {
    private String urlPrefix = "https://adventofcode.com/2023/day/";
    private String urlPostfix="/input";
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
        String url = urlPrefix + day + urlPostfix;
        return fetchContentFromURL(url);
    }
    private  List<String> fetchContentFromURL(String URL) {
        StringBuilder content = new StringBuilder();
        List<String> result = new ArrayList<String>();
        try {
            java.net.URL oracle = new URI(URL).toURL();
            URLConnection conn = oracle.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; pl; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2");
            conn.addRequestProperty("Cookie", cookie);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                result.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
