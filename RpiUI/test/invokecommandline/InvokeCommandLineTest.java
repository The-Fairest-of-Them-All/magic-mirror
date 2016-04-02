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
        String SSID = "";
        String password = "";
        InvokeCommandLine instance = new InvokeCommandLine();
        instance.InvokeCommandLine(SSID, password);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSSID method, of class InvokeCommandLine.
     */
    @Test
    public void testSetSSID() {
        System.out.println("setSSID");
        String SSID = "";
        InvokeCommandLine instance = new InvokeCommandLine();
        instance.setSSID(SSID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPassword method, of class InvokeCommandLine.
     */
    @Test
    public void testSetPassword() {
        System.out.println("setPassword");
        String password = "";
        InvokeCommandLine instance = new InvokeCommandLine();
        instance.setPassword(password);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of invoke method, of class InvokeCommandLine.
     */
    @Test
    public void testInvoke() {
        System.out.println("invoke");
        String[] commandAndArgs = null;
        InvokeCommandLine instance = new InvokeCommandLine();
        instance.invoke(commandAndArgs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
