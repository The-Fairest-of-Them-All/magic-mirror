/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetoothserverattemptone;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;

/**
 *
 * @author Owner
 */
public class bluetoothListenerThread implements Runnable {

    //private static Object lock = new Object();
    public static String bluetoothAddress;
    public static int discoverableMode;
    public static StreamConnectionNotifier notifier;
    public static boolean discoverable;
    public static UUID uuid;
    public static String url;

    public bluetoothListenerThread() {}
    
    @Override
    public void run() {
        listen();
    }

    private void listen() {
        try {
            //The LocalDevice class defines the basic functions of the Bluetooth manager (From API)
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            bluetoothAddress = localDevice.getBluetoothAddress();
            System.out.println("My bluetooth address is " + bluetoothAddress);

            //check whether device is discoverable, if not set it, if it cannot be set to discoverable, 
            //discoverable is false
            discoverable = getOrSetDiscoverableMode(localDevice);

            if (discoverable) {

                //The DiscoveryAgent class provides methods to perform device and service discovery (From API)
                DiscoveryAgent agent = localDevice.getDiscoveryAgent();

                uuid = new UUID(80087355); // "04c6093b-0000-1000-8000-00805f9b34fb"
                url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
                notifier = (StreamConnectionNotifier) Connector.open(url);

            }

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

        while (true) {
            System.out.println("Still going");
        }
    }

    //returns false for an undiscoverable device or if setDiscoverable() could not be completed, true otherwise
    private static boolean getOrSetDiscoverableMode(LocalDevice localDevice) {
        boolean available = false;

        discoverableMode = localDevice.getDiscoverable();
        if (discoverableMode == DiscoveryAgent.NOT_DISCOVERABLE) {
            System.out.println("Discoverable mode is " + discoverableMode + " undiscoverable");
            try {
                available = localDevice.setDiscoverable(DiscoveryAgent.GIAC);
                if (available) {
                    System.out.println("Now set to discoverable");
                } else {
                    System.out.println("Could not set device to discoverable mode.");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (BluetoothStateException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Discoverable mode is " + discoverableMode + " discoverable");
            available = true;
        }
        return available;
    }
}
