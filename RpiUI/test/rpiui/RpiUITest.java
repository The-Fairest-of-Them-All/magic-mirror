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

/**
 *
 * @author Owner
 */
public class RpiUITest {
    RpiUI uiThread;
    Runnable mainUiThread;
    
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
        uiThread = new RpiUI();
        mainUiThread = new Runnable() {
            public void run() {uiThread.framer();}
        };
        mainUiThread.run();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of appendToJTextArea method, of class RpiUI.
     */
    @Test(timeout=2000)
    public void testAppendToJTextArea() {
        System.out.println("appendToJTextArea");
        String newText = "NEW";
        String quote = "I'm ready!\n-Spongebob Squarepants";
        
        uiThread.appendToJTextArea(uiThread.getQuoteJTextArea(), newText);
        assertEquals(quote + newText, uiThread.getQuoteJTextArea().getText());
    }

    /**
     * Test of appendToJTextAreaNewline method, of class RpiUI.
     */
    @Test(timeout=2000)
    public void testAppendToJTextAreaNewline() {
        System.out.println("appendToJTextAreaNewline");
        String newText = "NEW";
        String quote = "I'm ready!\n-Spongebob Squarepants";
        
        uiThread.appendToJTextAreaNewline(uiThread.getQuoteJTextArea(), newText);
        assertEquals(quote + "\n" + newText, uiThread.getQuoteJTextArea().getText());
    }

    /**
     * Test of insertIntoBeginningJTextArea method, of class RpiUI.
     */
    @Test(timeout=2000)
    public void testInsertIntoBeginningJTextArea() {
        System.out.println("insertIntoBeginningJTextArea");
        String newText = "NEW";
        String quote = "I'm ready!\n-Spongebob Squarepants";
        
        uiThread.insertIntoBeginningJTextArea(uiThread.getQuoteJTextArea(), newText);
        assertEquals(newText + quote, uiThread.getQuoteJTextArea().getText());
    }

    /**
     * Test of replaceJTextArea method, of class RpiUI.
     */
    @Test(timeout=2000)
    public void testReplaceJTextArea() {
        System.out.println("replaceJTextArea");
        String newText = "NEW";
        
        uiThread.replaceJTextArea(uiThread.getQuoteJTextArea(), newText);
        assertEquals(newText, uiThread.getQuoteJTextArea().getText());
    }

    /**
     * Test of getTwitterJTextArea method, of class RpiUI.
     */
    @Test(timeout=2000)
    public void testGetTwitterJTextArea() {
        System.out.println("getTwitterJTextArea");
        JTextArea expected = new JTextArea();
        assertEquals(expected.getClass(), uiThread.getTwitterJTextArea().getClass());
    }

    /**
     * Test of getQuoteJTextArea method, of class RpiUI.
     */
    @Test(timeout=2000)
    public void testGetQuoteJTextArea() {
        System.out.println("getQuoteJTextArea");
        JTextArea expected = new JTextArea();
        assertEquals(expected.getClass(), uiThread.getQuoteJTextArea().getClass());
    }

    /**
     * Test of getCalendarJTextArea method, of class RpiUI.
     */
    @Test(timeout=2000)
    public void testGetCalendarJTextArea() {
        System.out.println("getCalendarJTextArea");
        JTextArea expected = new JTextArea();
        assertEquals(expected.getClass(), uiThread.getCalendarJTextArea().getClass());
    }

    /**
     * Test of getWeatherJTextArea method, of class RpiUI.
     */
    @Test(timeout=2000)
    public void testGetWeatherJTextArea() {
        System.out.println("getWeatherJTextArea");
        JTextArea expected = new JTextArea();
        assertEquals(expected.getClass(), uiThread.getCalendarJTextArea().getClass());
    }

    /**
     * Test of getTimeJTextArea method, of class RpiUI.
     */
    @Test(timeout=2000)
    public void testGetTimeJTextArea() {
        System.out.println("getTimeJTextArea");
        JTextArea expected = new JTextArea();
        assertEquals(expected.getClass(), uiThread.getTimeJTextArea().getClass());
    }

    /**
     * Test of toggleDisplay method, of class RpiUI.
     */
    @Test(timeout=2000)
    public void testToggleDisplay() {
        System.out.println("toggleDisplay");
        RpiUI instance = new RpiUI();
        instance.toggleDisplay();
        assert(instance.display == false);
    }
}
