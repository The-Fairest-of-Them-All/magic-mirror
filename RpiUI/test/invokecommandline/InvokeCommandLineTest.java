/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invokecommandline;

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
public class InvokeCommandLineTest {
    
    public InvokeCommandLineTest() {
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
     * Test of InvokeCommandLine method, of class InvokeCommandLine.
     */
    @Test
    public void testInvokeCommandLine_0args() {
        System.out.println("InvokeCommandLine");
        InvokeCommandLine instance = new InvokeCommandLine();
        instance.InvokeCommandLine();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of InvokeCommandLine method, of class InvokeCommandLine.
     */
    @Test
    public void testInvokeCommandLine_String_String() {
        System.out.println("InvokeCommandLine");
        String SSID = "test";
        String password = "ABCDEFG";
        InvokeCommandLine instance = new InvokeCommandLine();
        instance.InvokeCommandLine(SSID, password);
        assertEquals(SSID, instance.getSSID());
        assertEquals(password, instance.getPassword());
    }

    /**
     * Test of setSSID method, of class InvokeCommandLine.
     */
    @Test
    public void testSetSSID() {
        System.out.println("setSSID");
        String SSID = "Test";
        InvokeCommandLine instance = new InvokeCommandLine();
        instance.setSSID(SSID);
        //TODO  SHOULD USE REFLECTION
        assertEquals(SSID, instance.getSSID());
    }

    /**
     * Test of setPassword method, of class InvokeCommandLine.
     */
    @Test
    public void testSetPassword() {
        System.out.println("setPassword");
        String password = "passwordTest";
        InvokeCommandLine instance = new InvokeCommandLine();
        instance.setPassword(password);
        //TODO SHOULD USE REFLECTION
        assertEquals(password, instance.getPassword());
    }

    /**
     * Test of invoke method, of class InvokeCommandLine.
     */
    @Test
    public void testInvoke() {
        System.out.println("invoke");
        String expectedOutput = "ABCDEFG";
        String[] commandAndArgs = {"echo", expectedOutput};
        InvokeCommandLine instance = new InvokeCommandLine();
        instance.invoke(commandAndArgs);
        //TODO SHOULD CAPTURE SYS.OUT AND COMPARE RESULTS
        fail("nothing yet");
    }

    /**
     * Test of connectToNetwork method, of class InvokeCommandLine.
     */
    @Test
    public void testConnectToNetwork() {
        System.out.println("connectToNetwork");
        InvokeCommandLine instance = new InvokeCommandLine();
        instance.connectToNetwork();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
