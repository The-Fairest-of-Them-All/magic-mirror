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
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private boolean reachable = false;

    public ParseMMData(String t, String c, String w, String q) {
        TWITTER_KEY = t;
        CALENDAR_KEY = c;
        WEATHER_KEY = w;
        QUOTE_KEY = q;
    }

    /**
     * Removes the leading "T: " from the input string and if there is a
     * trailing "DONE" it removes that as well. Removes any leading or trailing
     * square brackets that may be present.
     *
     * @param input the unformatted Twitter String
     * @return the formatted Twitter String
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
     * @param input the unformatted Quote String
     * @return the formatted Quote String
     */
    public String parseQuote(String input) {
        String temp;
        temp = input.replaceFirst(QUOTE_KEY, "").trim();
        temp = temp.replaceAll(EXIT_KEYWORD, "").trim();
        return temp;
    }

    /**
     * Removes the leading "W: " from the input string and if there is a
     * trailing "DONE" it removes that as well. If no location data was passed,
     * just returns the input String, but if location data was provided, sends
     * that data to a Weather object to fetch the weather.
     *
     * @param input the unformatted Weather String
     * @return the formatted Weather String or a message that Location is
     * required
     */
    public String parseWeather(String input) {
        String temp;
        temp = input.replaceFirst(WEATHER_KEY, "").trim();
        temp = temp.replaceAll(EXIT_KEYWORD, "").trim();
        
        //Location was off on phone
        if (temp.contains("I need your location to get the weather.")) { 
            return temp;
        } 
        //Location should not be displayed
        else if (temp.equals("")) {
            return temp;
        } else{
            //check if google is reachable to test whether network connectivity is available
            reachable = pingHost("www.google.com", 80, 5000);
           
            StringBuilder weather = new StringBuilder();
            if (reachable) {
                Gson gson = new Gson();
                Location loc = gson.fromJson(temp, Location.class);
                Weather weat = new Weather(loc);
                weat.printCurrently();
                weather.append(weat.returnCurrently());
                Helpful_Hints hh = new Helpful_Hints(weat);
                weather.append("\n").append(hh.getStatement());
            } else {
                weather.append("Internet connectivity needed to access the weather.");
            }
            return weather.toString();
        }
    }

    /**
     * Removes the leading "C: " from the input string and if there is a
     * trailing "DONE" it removes that as well. Removes any leading or trailing
     * square brackets that may be present.
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
     * Gets the SSID and password from the JSON string.
     *
     * @param input a JSON object containing SSID and password parameters
     * @return a String[] containing the SSID and password
     */
    public String[] parseConnectionData(String input) {
        ConnectionDetails con;
        String temp;
        temp = input.replaceFirst(MAKE_CONNECTION_KEYWORD, "").trim();
        Gson gson = new Gson();
        con = gson.fromJson(temp, ConnectionDetails.class);
        String[] splitInput = {con.getSSID(), con.getPassword()};

        return splitInput;
    }

    /**
     * Removes any leading or trailing square brackets present in the String
     * argument.
     *
     * @param withBrackets a String that may or may not have square brackets
     * surrounding it
     * @return the same String that was passed in with the square brackets
     * removed if they were present
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

    public static boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }
}
