/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetoothserverattemptone;

import java.io.IOException;
import java.io.InputStream;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
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
    public static StreamConnection connection;
    public static LocalDevice localDevice;

    public bluetoothListenerThread() {
    }

    @Override
    public void run() {
        listen();
    }

    private void listen() {
        if (LocalDevice.isPowerOn()) {
            try {
                //The LocalDevice class defines the basic functions of the Bluetooth manager (From API)
                localDevice = LocalDevice.getLocalDevice();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            try {
                bluetoothAddress = localDevice.getBluetoothAddress();
                System.out.println("My bluetooth address is " + bluetoothAddress);

                //check whether device is discoverable, if not set it, if it cannot be set to discoverable, 
                //discoverable is false
                discoverable = getOrSetDiscoverableMode(localDevice);

                if (discoverable) {
                    //The DiscoveryAgent class provides methods to perform device and service discovery (From API)
                    DiscoveryAgent agent = localDevice.getDiscoveryAgent();

                    //UUID is how the android app finds the raspberry app
                    //String uuidString = "a96d5795-f8c3-4b7a-9bad-1eefa9e11a94";
                    String uuidString = "a96d5795f8c34b7a9bad1eefa9e11a94";
                    uuid = new UUID(uuidString, false);
                    //btspp is the URL scheme for an RFCOMM StreamConnection
                    //url required format is btspp: //hostname: [ CN | UUID ]; parameters for StreamConnectionNotifier
                    url = "btspp://localhost:" + uuid.toString() + ";name=RaspberryServer";
                    notifier = (StreamConnectionNotifier) Connector.open(url);

                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            while (true) {
                System.out.println("Waiting for connection from Android app...");
                try {
                    //listen for client to connect to the url defined by url
                    connection = notifier.acceptAndOpen();
                    System.out.println("Connection to Android accepted.");
                    processConnection(connection);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Bluetooth is turned off. Don't think we can do anything about that");
        }
    }

//returns false for an undiscoverable device or if setDiscoverable() could not be completed, true otherwise
    private static boolean getOrSetDiscoverableMode(LocalDevice localDevice) {
        boolean available = false;

        discoverableMode = localDevice.getDiscoverable();
        if (discoverableMode == DiscoveryAgent.NOT_DISCOVERABLE) {
            System.out.println("Discoverable mode is " + discoverableMode + " undiscoverable");
            try {
                available = localDevice.setDiscoverable(DiscoveryAgent.LIAC);
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

    private static void processConnection(StreamConnection connection) {
        try {
            // prepare to receive data
            InputStream inputStream = connection.openInputStream();

            System.out.println("waiting for input");

            while (true) {
                int command = inputStream.read();

                System.out.println(command);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
