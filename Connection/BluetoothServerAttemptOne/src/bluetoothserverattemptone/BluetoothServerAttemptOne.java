/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetoothserverattemptone;

import javax.bluetooth.*;
import com.intel.bluetooth.BlueCoveImpl;

/**
 *
 * @author Owner
 */
public class BluetoothServerAttemptOne {

    private static Object lock = new Object();
    public static String bluetoothAddress;
    public static int discoverableMode;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try {
            //The LocalDevice class defines the basic functions of the Bluetooth manager (From API)
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            bluetoothAddress = localDevice.getBluetoothAddress();
            System.out.println("My bluetooth address is " + bluetoothAddress);
            discoverableMode = localDevice.getDiscoverable();
            if (discoverableMode == DiscoveryAgent.NOT_DISCOVERABLE) {
                System.out.println("Discoverable mode is " + discoverableMode + " undiscoverable");
                try {
                    boolean available = localDevice.setDiscoverable(DiscoveryAgent.GIAC);
                    if (available) {
                        System.out.println("Set to discoverable");
                    } else {
                        System.out.println("Could not set device to discoverable mode.");
                    }
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (BluetoothStateException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Discoverable mode is " + discoverableMode + " discoverable");
            }

            //The DiscoveryAgent class provides methods to perform device and service discovery (From API)
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();

            // 3
            /*agent.startInquiry(DiscoveryAgent.GIAC, new MyDiscoveryListener());

            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Device Inquiry Completed. ");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        while(true) {
            System.out.println("Still going");
        }
    }

}
