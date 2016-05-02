/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpiui;

import JSONClasses.Location;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Owner
 */
public class ParseMMDataTest {
    
    ParseMMData instance;
    
    public ParseMMDataTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new ParseMMData("Tw:", "Ca:", "We:", "Qu:");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parseWeather method, of class ParseMMData.
     */
    @Test
    public void testParseWeather() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        System.out.println("parseWeather");
        String input = "We:{\"latitude\":\"40.3012814\",\"longitude\":\"-75.1267708\"}DONE";
        String testLong = "-75.1267708";
        String testLat = "40.3012814";
        Location expLoc = new Location();
        expLoc.setLatitudeUsingString(testLat);
        expLoc.setLongitudeUsingString(testLong);
        instance.parseWeather(input);
        
        Field field = instance.getClass().getDeclaredField("loc");
        field.setAccessible(true);
        assertEquals(expLoc, field.get(instance));
    }
    
    /**
     * Test of parseWeather method, of class ParseMMData with String indicating no location data was sent.
     */
    @Test
    public void testParseWeatherNoLocationInput() {
        System.out.println("parseWeather");
        String input = "I need your location to get the weather.";
        String result = instance.parseWeather(input);
        assertEquals(input, result);
    }

    /**
     * Test of parseTwitter method, of class ParseMMData.
     */
    @Test
    public void testParseTwitter() {
        System.out.println("parseTwitter");
        String input = "Tw:bbbDONE";
        String expResult = "bbb";
        String result = instance.parseTwitter(input);
        assertEquals(expResult, result);
    }

    /**
     * Test of parseCalendar method, of class ParseMMData.
     */
    @Test
    public void testParseCalendar() {
        System.out.println("parseCalendar");
        String input = "Ca:cccDONE";
        String expResult = "ccc";
        String result = instance.parseCalendar(input);
        assertEquals(expResult, result);
    }

    /**
     * Test of parseQuote method, of class ParseMMData.
     */
    @Test
    public void testParseQuote() {
        System.out.println("parseQuote");
        String input = "Qu:dddDONE";
        String expResult = "ddd";
        String result = instance.parseQuote(input);
        assertEquals(expResult, result);
    }
    
}
