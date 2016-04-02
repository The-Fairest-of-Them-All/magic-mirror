/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpiui;

import javax.swing.JTextArea;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static rpiui.RpiUI.sample;

/**
 *
 * @author Owner
 */
public class RpiUITest {
    
    public RpiUITest() {
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
     * Test of appendToJTextArea method, of class RpiUI.
     */
    @Test
    public void testAppendToJTextArea() {
        System.out.println("appendToJTextArea");
        JTextArea area = new JTextArea("ORIGINAL", 6, 20);
        String newText = "NEW";
        RpiUI instance = new RpiUI();
        instance.appendToJTextArea(area, newText);
        assertEquals("ORIGINALNEW", area.getText());
    }

    /**
     * Test of appendToJTextAreaNewline method, of class RpiUI.
     */
    @Test
    public void testAppendToJTextAreaNewline() {
        System.out.println("appendToJTextAreaNewline");
        JTextArea area = null;
        String newText = "";
        RpiUI instance = new RpiUI();
        instance.appendToJTextAreaNewline(area, newText);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertIntoBeginningJTextArea method, of class RpiUI.
     */
    @Test
    public void testInsertIntoBeginningJTextArea() {
        System.out.println("insertIntoBeginningJTextArea");
        JTextArea area = null;
        String newText = "";
        RpiUI instance = new RpiUI();
        instance.insertIntoBeginningJTextArea(area, newText);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of replaceJTextArea method, of class RpiUI.
     */
    @Test
    public void testReplaceJTextArea() {
        System.out.println("replaceJTextArea");
        JTextArea area = null;
        String newText = "";
        RpiUI instance = new RpiUI();
        instance.replaceJTextArea(area, newText);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTwitterJTextArea method, of class RpiUI.
     */
    @Test
    public void testGetTwitterJTextArea() {
        System.out.println("getTwitterJTextArea");
        RpiUI instance = new RpiUI();
        JTextArea expResult = null;
        JTextArea result = instance.getTwitterJTextArea();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getQuoteJTextArea method, of class RpiUI.
     */
    @Test
    public void testGetQuoteJTextArea() {
        System.out.println("getQuoteJTextArea");
        RpiUI instance = new RpiUI();
        JTextArea expResult = null;
        JTextArea result = instance.getQuoteJTextArea();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCalendarJTextArea method, of class RpiUI.
     */
    @Test
    public void testGetCalendarJTextArea() {
        System.out.println("getCalendarJTextArea");
        RpiUI instance = new RpiUI();
        JTextArea expResult = null;
        JTextArea result = instance.getCalendarJTextArea();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWeatherJTextArea method, of class RpiUI.
     */
    @Test
    public void testGetWeatherJTextArea() {
        System.out.println("getWeatherJTextArea");
        RpiUI instance = new RpiUI();
        JTextArea expResult = null;
        JTextArea result = instance.getWeatherJTextArea();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTimeJTextArea method, of class RpiUI.
     */
    @Test
    public void testGetTimeJTextArea() {
        System.out.println("getTimeJTextArea");
        RpiUI instance = new RpiUI();
        JTextArea expResult = null;
        JTextArea result = instance.getTimeJTextArea();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class RpiUI.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        RpiUI.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toggleDisplay method, of class RpiUI.
     */
    @Test
    public void testToggleDisplay() {
        System.out.println("toggleDisplay");
        RpiUI instance = new RpiUI();
        instance.toggleDisplay();
        assert(instance.display == false);
    }
    
}
