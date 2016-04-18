/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetooth;

import static bluetooth.BluetoothListenerThread.localDevice;
import java.io.IOException;
import java.io.InputStream;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.StreamConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.TestSubject;
import org.easymock.*;
import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 *
 * @author Owner
 */
@RunWith(EasyMockRunner.class)
public class BluetoothListenerThreadTest {
    @TestSubject
    BluetoothListenerThread bt = new BluetoothListenerThread();
    
    /*@Mock
    StreamConnection conn;
    
    @Mock
    LocalDevice localDev;*/
    
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        //bt = new BluetoothListenerThread();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class BluetoothListenerThread.
     */
    /*@Test
    public void testRun() {
        System.out.println("run");
        BluetoothListenerThread instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getTwitterKey method, of class BluetoothListenerThread.
     */
    @Test
    public void testGetTwitterKey() {
        System.out.println("getTwitterKey");
        String expResult = "T: ";
        String result = BluetoothListenerThread.getTwitterKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getQuoteKey method, of class BluetoothListenerThread.
     */
    @Test
    public void testGetQuoteKey() {
        System.out.println("getQuoteKey");
        String expResult = "Q: ";
        String result = BluetoothListenerThread.getQuoteKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCalendarKey method, of class BluetoothListenerThread.
     */
    @Test
    public void testGetCalendarKey() {
        System.out.println("getCalendarKey");
        String expResult = "C: ";
        String result = BluetoothListenerThread.getCalendarKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWeatherKey method, of class BluetoothListenerThread.
     */
    @Test
    public void testGetWeatherKey() {
        System.out.println("getWeatherKey");
        String expResult = "W: ";
        String result = BluetoothListenerThread.getWeatherKey();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testProcessConnection() throws IOException {
        InputStream input = null;
        //EasyMock.expect(conn.openInputStream()).andReturn(input);
        Assert.assertNotNull(input);
      
        
    }
    
    @Test
    public void testFindLocalDeviceAddressName () {
        fail("The test case is a prototype.");
    }
    
    /**
     * Mock device power state to off.
     */
    @Test
    public void testRunWithDevicePowerOff() {
        fail("The test case is a prototype.");
    }
    
    /**
     * Mock device power state to on and discoverable state to off.
     */
    @Test
    public void testRunWithDevicePowerOnDiscoverableOff() {
        fail("The test case is a prototype.");
    }
    
    /**
     * Test the run method, mock a connection to Android, then timeout to end the infinite loop.
     */
    @Test
    public void testWaitForConnectionWithTimeout() {
        fail("The test case is a prototype.");
    }
    
    /**
     * Test full run of program but provide no input. Expect socket to be closed immediately and waiting to
     * resume.
     */
    @Test
    public void testFullRunNoInput() {
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testFullRunDisplayInput() {
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testFullRunSleepKeyword() {
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testFullRunConnectKeyword() {
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testFullRunExitKeyword() {
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testFullRunUnlabeledInput() {
        fail("The test case is a prototype.");
    }
}