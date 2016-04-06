/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpiui;

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
     * Removes the leading "W: " from the input string and if there is a trailing "DONE" it removes that
     * as well.
     * 
     * @param input the unformatted String
     * @return the formatted String
     */
    public String parseWeather(String input) {
        String[] splitInput;
        String parsed;
        String temp;
        temp = input.replaceFirst(WEATHER_KEY, "").trim();
        temp = temp.replaceAll(EXIT_KEYWORD, "").trim();
        return temp;
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
    
    public String[] parseConnectionData(String input) {
        String[] splitInput = null;
        String temp;
        //temp = input.replaceFirst("SSID", "").trim();
        return splitInput;
    }
}
