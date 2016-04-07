/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetooth;

import static bluetooth.BluetoothListenerThread.localDevice;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.*;

/**
 *
 * @author Owner
 */
public class BluetoothListenerThreadTest {
    
    BluetoothListenerThread bt;
    
    public BluetoothListenerThreadTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        bt = new BluetoothListenerThread();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class BluetoothListenerThread.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        BluetoothListenerThread instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}