/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpiui;

import JSONClasses.ConnectionDetails;
import JSONClasses.Location;
import com.google.gson.*;

/**
 *
 * @author Owner
 */
public class ParseMMData {
    private final String TWITTER_KEY;
    private final String CALENDAR_KEY;
    private final String WEATHER_KEY;
    private final String QUOTE_KEY;
    private final String EXIT_KEYWORD = "DONE";
    private final String MAKE_CONNECTION_KEYWORD = "CONNECT";

    public ParseMMData(String t, String c, String w, String q) {
        TWITTER_KEY = t;
        CALENDAR_KEY = c;
        WEATHER_KEY = w;
        QUOTE_KEY = q;
    }
    
    /**
     * Removes the leading "T: " from the input string and if there is a trailing "DONE" it removes that
     * as well.
     * 
     * @param input the unformatted String
     * @return the formatted String
     */
    public String parseTwitter(String input) {
        String[] splitInput;
        String parsed;
        String temp;
        temp = input.replaceFirst(TWITTER_KEY, "").trim();
        temp = temp.replaceAll(EXIT_KEYWORD, "").trim();
        //check whether other data got read along with the Twitter data
        /*if (temp.contains(QUOTE_KEY)) {
            int begins = temp.indexOf(QUOTE_KEY);
        }*/
        return temp;
    }
    
    /**
     * Removes the leading "Q: " from the input string and if there is a trailing "DONE" it removes that
     * as well.
     * 
     * @param input the unformatted String
     * @return the formatted String
     */
    public String parseQuote(String input) {
        String[] splitInput;
        String parsed;
        String temp;
        temp = input.replaceFirst(QUOTE_KEY, "").trim();
        temp = temp.replaceAll(EXIT_KEYWORD, "").trim();
        return temp;
    }
    
    /**
     * UNTESTED Removes the leading "W: " from the input string and if there is a trailing "DONE" it removes that
     * as well.
     * 
     * @param input the unformatted String
     * @return the formatted String
     */
    public String[] parseWeather(String input) {
        Location loc = new Location();
        String parsed;
        String temp;
        temp = input.replaceFirst(WEATHER_KEY, "").trim();
        temp = temp.replaceAll(EXIT_KEYWORD, "").trim();
        Gson gson = new Gson();
        gson.fromJson(temp, Location.class);
        String[] splitInput = {loc.getLongitude(), loc.getLattitude()};
        return splitInput;
    }
    
    /**
     * Removes the leading "C: " from the input string and if there is a trailing "DONE" it removes that
     * as well.
     * 
     * @param input the unformatted String
     * @return the formatted String
     */
    public String parseCalendar(String input) {
        String[] splitInput;
        String parsed;
        String temp;
        temp = input.replaceFirst(CALENDAR_KEY, "").trim();
        temp = temp.replaceAll(EXIT_KEYWORD, "").trim();
        return temp;
    }
    
    /**
     * UNTESTED Gets the SSID and password from the JSON string.
     * @param input
     * @return 
     */
    public String[] parseConnectionData(String input) {
        ConnectionDetails con = new ConnectionDetails();
        String temp;
        temp = input.replaceFirst(MAKE_CONNECTION_KEYWORD, "").trim();
        Gson gson = new Gson();
        gson.fromJson(temp, ConnectionDetails.class);
        String[] splitInput = {con.getSSID(), con.getPassword()};
       
        return splitInput;
    }
}
