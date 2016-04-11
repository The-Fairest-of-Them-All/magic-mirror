/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invokecommandline;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
public class InvokeCommandLineTest {
    
    InvokeCommandLine instance;
    
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
    public void testInvokeCommandLine_0args() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        System.out.println("InvokeCommandLine");
        instance = new InvokeCommandLine();
        
        Field field = instance.getClass().getDeclaredField("SSID");
        field.setAccessible(true);
        assertEquals("", field.get(instance));
        
        Field field2 = instance.getClass().getDeclaredField("password");
        field2.setAccessible(true);
        assertEquals("", field2.get(instance));
    }

    /**
     * Test of InvokeCommandLine method, of class InvokeCommandLine.
     */
    @Test
    public void testInvokeCommandLine_String_String() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        System.out.println("InvokeCommandLine");
        String SSID = "test";
        String password = "ABCDEFG";
        instance = new InvokeCommandLine(SSID, password);
        
        Field field = instance.getClass().getDeclaredField("SSID");
        field.setAccessible(true);
        assertEquals(SSID, field.get(instance));
        
        Field field2 = instance.getClass().getDeclaredField("password");
        field2.setAccessible(true);
        assertEquals(password, field2.get(instance));
    }

    /**
     * Test of setSSID method, of class InvokeCommandLine.
     */
    @Test
    public void testSetSSID() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        System.out.println("setSSID");
        String SSID = "Test";
        instance = new InvokeCommandLine();
        instance.setSSID(SSID);
        
        Field field = instance.getClass().getDeclaredField("SSID");
        field.setAccessible(true);
        assertEquals(SSID, field.get(instance));
    }

    /**
     * Test of setPassword method, of class InvokeCommandLine.
     */
    @Test
    public void testSetPassword() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        System.out.println("setPassword");
        String password = "passwordTest";
        instance = new InvokeCommandLine();
        instance.setPassword(password);
        
        Field field2 = instance.getClass().getDeclaredField("password");
        field2.setAccessible(true);
        assertEquals(password, field2.get(instance));
    }

    /**
     * Test of invoke method, of class InvokeCommandLine.
     */
    @Test
    public void testInvoke() {
        System.out.println("invoke");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        String expectedOutput = "ABCDEFG";
        String[] commandAndArgs = {"echo", expectedOutput};
        instance = new InvokeCommandLine();
        instance.invoke(commandAndArgs);
        String whatWasWritten = out.toString().trim();
        assertEquals(expectedOutput, whatWasWritten);
    }

    /**
     * Test of connectToNetwork method, of class InvokeCommandLine.
     */
    @Test
    public void testConnectToNetwork() {
        System.out.println("connectToNetwork");
        instance = new InvokeCommandLine();
        instance.connectToNetwork();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
