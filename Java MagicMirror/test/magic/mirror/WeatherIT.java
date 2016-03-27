/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magic.mirror;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class WeatherIT {
    
    public WeatherIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of getHiTemp method, of class Weather.
     */
    @Test
    public void testGetHiTemp() {
        System.out.println("getHiTemp");
        Weather instance = new Weather();
        String result = instance.getHiTemp();
        assertNotEquals(null, result);
    }

    /**
     * Test of getLoTemp method, of class Weather.
     */
    @Test
    public void testGetLoTemp() {
        System.out.println("getLoTemp");
        Weather instance = new Weather();
        String result = instance.getLoTemp();
        assertNotEquals(null, result);
    }

    /**
     * Test of getCurrentTemp method, of class Weather.
     */
    @Test
    public void testGetCurrentTemp() {
        System.out.println("getCurrentTemp");
        Weather instance = new Weather();
        String result = instance.getCurrentTemp();
        assertNotEquals(null, result);
    }

    /**
     * Test of getConditions method, of class Weather.
     */
    @Test
    public void testGetConditions() {
        System.out.println("getConditions");
        Weather instance = new Weather();
        String result = instance.getConditions();
        assertNotEquals(null, result);
    }

    /**
     * Test of precipitation method, of class Weather.
     */
    @Test
    public void testPrecipitation() {
        System.out.println("precipitation");
        Weather instance = new Weather();
        String result = instance.precipitation();
        assertNotEquals(null, result);
    }
    
}
