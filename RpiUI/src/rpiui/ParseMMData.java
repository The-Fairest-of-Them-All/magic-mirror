/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpiui;

import JSONClasses.ConnectionDetails;
import JSONClasses.Location;
import Weather.Helpful_Hints;
import Weather.Weather;
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
     * Removes the leading "T: " from the input string and if there is a
     * trailing "DONE" it removes that as well.
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
        temp = removeSquareBrackets(temp);
        return temp;
    }

    /**
     * Removes the leading "Q: " from the input string and if there is a
     * trailing "DONE" it removes that as well.
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
     * UNTESTED Removes the leading "W: " from the input string and if there is
     * a trailing "DONE" it removes that as well.
     *
     * @param input the unformatted String
     * @return the formatted String
     */
    public String parseWeather(String input) {
        String temp;
        temp = input.replaceFirst(WEATHER_KEY, "").trim();
        temp = temp.replaceAll(EXIT_KEYWORD, "").trim();
        if (temp.contains("I need your location to get the weather.")) {
            return temp;
        } else {
            Gson gson = new Gson();
            Location loc = gson.fromJson(temp, Location.class);
            Weather weat = new Weather(loc);
            weat.printCurrently();
            StringBuilder weather = new StringBuilder(weat.returnCurrently());
            Helpful_Hints hh = new Helpful_Hints(weat);
            weather.append("\n").append(hh.getStatement());
            return weather.toString();
        }
    }

    /**
     * Removes the leading "C: " from the input string and if there is a
     * trailing "DONE" it removes that as well.
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
        if (temp.equals("[]")) {
            temp = "No events today";
        } else {
            temp = removeSquareBrackets(temp);
        }
        return temp;
    }

    /**
     * UNTESTED Gets the SSID and password from the JSON string.
     *
     * @param input
     * @return
     */
    public String[] parseConnectionData(String input) {
        ConnectionDetails con;
        String temp;
        //String[] connectData;
        temp = input.replaceFirst(MAKE_CONNECTION_KEYWORD, "").trim();
        Gson gson = new Gson();
        con = gson.fromJson(temp, ConnectionDetails.class);
        String[] splitInput = {con.getSSID(), con.getPassword()};

        return splitInput;
    }
    
    /**
     * Removes any leading or trailing square brackets present in the String argument.
     * 
     * @param withBrackets a String that may or may not have square brackets surrounding it 
     * @return the same String that was passed in with the square brackets removed if they were present
     */
    private String removeSquareBrackets(String withBrackets) {
        if (withBrackets.startsWith("[")) {      
            if (withBrackets.endsWith("]")) { //leading and trailing square brackets present
                withBrackets = withBrackets.substring(1, withBrackets.length() - 2);
            } else { //only leading square bracket present
                withBrackets = withBrackets.substring(1, withBrackets.length() - 1);
            }
        } else if (withBrackets.endsWith("]")) { //only trailing square bracket present
            withBrackets = withBrackets.substring(0, withBrackets.length() - 2);
        }
        return withBrackets;
    }
}
