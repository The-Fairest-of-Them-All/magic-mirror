/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Weather;

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
public class Helpful_HintsIT {
    
    public Helpful_HintsIT() {
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
     * Test of getStatement method, of class Helpful_Hints.
     */
    @Test
    public void testGetStatement() {
        System.out.println("getStatement");
        Weather weather = new Weather();
        Helpful_Hints instance = new Helpful_Hints(weather);
        String result = instance.getStatement();
        assertNotNull(result);
    }
    
}
