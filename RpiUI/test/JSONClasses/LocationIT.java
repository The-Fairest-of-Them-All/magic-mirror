/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSONClasses;

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
public class LocationIT {
    Location loc;
    
    public LocationIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        loc = new Location();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getLongitude method, of class Location.
     */
    @Test
    public void testGetLongitude() {
        System.out.println("getLongitude");
        assertEquals("getLongitude Works", "39.9819", loc.longitude);
    }

    /**
     * Test of getLattitude method, of class Location.
     */
    @Test
    public void testGetLattitude() {
        System.out.println("getLattitude");
        assertEquals("getLattitude Works","-75.1529", loc.latitude);
    }
    
}
