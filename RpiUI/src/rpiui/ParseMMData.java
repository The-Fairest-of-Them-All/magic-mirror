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
    private String TWITTER_KEY;
    private String CALENDAR_KEY;
    private String WEATHER_KEY;
    private String QUOTE_KEY;

    public ParseMMData(String t, String c, String w, String q) {
        TWITTER_KEY = t;
        CALENDAR_KEY = c;
        WEATHER_KEY = w;
        QUOTE_KEY = q;
    }
    
    public ParseMMData() {
        TWITTER_KEY = "T: ";
        CALENDAR_KEY = "C: ";
        WEATHER_KEY = "W: ";
        QUOTE_KEY = "Q: ";
    }
    
    public String parseWeather(String input) {
        String[] splitInput;
        String parsed;
        String temp;
        temp = input.replaceFirst(WEATHER_KEY, "").trim();
        return temp;
    }
    
    public String parseTwitter(String input) {
        String[] splitInput;
        String parsed;
        String temp;
        temp = input.replaceFirst(TWITTER_KEY, "").trim();
        return temp;
    }
    
    public String parseCalendar(String input) {
        String[] splitInput;
        String parsed;
        String temp;
        temp = input.replaceFirst(CALENDAR_KEY, "").trim();
        return temp;
    }
    
    public String parseQuote(String input) {
        String[] splitInput;
        String parsed;
        String temp;
        temp = input.replaceFirst(QUOTE_KEY, "").trim();
        return temp;
    }
}
