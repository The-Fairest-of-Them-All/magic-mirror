/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpiui;

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
        instance = new ParseMMData("T: ", "C: ", "W: ", "Q: ");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parseWeather method, of class ParseMMData.
     */
    @Test
    public void testParseWeather() {
        System.out.println("parseWeather");
        String input = "W: aaaDONE";
        String expResult = "aaa";
        String result = instance.parseWeather(input);
        assertEquals(expResult, result);
    }

    /**
     * Test of parseTwitter method, of class ParseMMData.
     */
    @Test
    public void testParseTwitter() {
        System.out.println("parseTwitter");
        String input = "T: bbbDONE";
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
        String input = "C: cccDONE";
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
        String input = "Q: dddDONE";
        String expResult = "ddd";
        String result = instance.parseQuote(input);
        assertEquals(expResult, result);
    }
    
}
