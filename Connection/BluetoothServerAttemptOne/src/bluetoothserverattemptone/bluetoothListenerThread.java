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
 * A thread used to asynchronously query the device about its bluetooth capabilities and if the capabilities
 * are sufficient, i.e. there is a bluetooth device, it is turned on, and it can be set to discoverable mode,
 * creates a server side socket and listens for a connection request from android app. Finally, if the connection
 * is established, this thread reads in the data passed by the android app.
 * 
 * @author Keith Rasweiler
 */
public class bluetoothListenerThread implements Runnable {
    //this is the UUID that is used to create a socket between android and raspberry via bluetooth
    private final String UUIDSTRING = "a96d5795f8c34b7a9bad1eefa9e11a94";

    public static String bluetoothAddress; //MAC address of the bluetooth adapter on raspberry pi
    public static int discoverableMode; //values described in getOrSetDiscoverableMode()
    public static StreamConnectionNotifier notifier;
    public static boolean discoverable;
    public static UUID uuid; //UUID that is know on both client and server side used to create BluetoothSockets
    public static String url;
    public static StreamConnection connection;
    public static LocalDevice localDevice; //The LocalDevice class defines the basic functions of the Bluetooth manager
    public static String bluetoothFriendlyName;
   

    public bluetoothListenerThread() {
    }

    @Override
    public void run() {
        listen();
    }

    /**
     * Does preliminary checks on the bluetooth device connected to the computer. First checks to ensure that
     * the device is powered on, next tries to get the bluetooth MAC address and friendly name and prints them
     * to Sys.out. The friendly name and MAC address are needed to facilitate the connection along with the UUID.
     * Next, checks to see if device is discoverable and attempts to set it to discoverable if not using a call
     * to getOrSetDiscoverableMode(). Finally creates a StreamConnectionNotifier object and uses the acceptAndOpen()
     * method to accept an incoming bluetooth connection request. The resulting StreamConnection serves as the
     * server socket and is passed to processConnection().
     */
    private void listen() {
        if (LocalDevice.isPowerOn()) { //if power is off, do not do any further setting up
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
                bluetoothFriendlyName = localDevice.getFriendlyName();
                System.out.println("Bluetooth friendly name is " + bluetoothFriendlyName + ". This is what you should connect to.");

                //check whether device is discoverable, if not set it, if it cannot be set to discoverable, 
                //discoverable is false
                discoverable = getOrSetDiscoverableMode(localDevice);

                if (discoverable) {
                    //The DiscoveryAgent class provides methods to perform device and service discovery (From API)
                    DiscoveryAgent agent = localDevice.getDiscoveryAgent();

                    //UUID is how the android app finds the raspberry app                 
                    uuid = new UUID(UUIDSTRING, false);
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

    /**
     * Returns false for an undiscoverable device or if setDiscoverable() could not be completed, true otherwise.
     * Prints verbose output to Sys.out through the process.
     * 
     * @param localDevice
     * @return a boolean representing whether or not the device is discoverable
     */
    private static boolean getOrSetDiscoverableMode(LocalDevice localDevice) {
        boolean available = false;

        //Retrieves the local device's discoverable mode. The return value will be DiscoveryAgent.GIAC - 0x9E8B33 (10390323), 
        //DiscoveryAgent. -  0x9E8B00 (10390272), DiscoveryAgent.NOT_DISCOVERABLE - 0x00 (0), or a value in the 
        //range 0x9E8B00 to 0x9E8B3F.
        discoverableMode = localDevice.getDiscoverable();
        if (discoverableMode == DiscoveryAgent.NOT_DISCOVERABLE) {
            System.out.println("Discoverable mode is " + discoverableMode + " undiscoverable");
            try {
                available = localDevice.setDiscoverable(DiscoveryAgent.LIAC);
                if (available) {
                    discoverableMode = localDevice.getDiscoverable();
                    System.out.println("Now set to discoverable, " + discoverableMode);
                } else {
                    System.out.println("Could not set device to discoverable mode.");
                }
            } catch (IllegalArgumentException | BluetoothStateException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Discoverable mode is " + discoverableMode + " discoverable");
            available = true;
        }
        return available;
    }
    
    /**
     * Opens an InputStream from the StreamConnection object and receives data passed by android app into a
     * byte[] buffer.
     * 
     * @param connection 
     */
    private static void processConnection(StreamConnection connection) {
        try {
            // prepare to receive data
            InputStream inputStream = connection.openInputStream();

            System.out.println("waiting for input");

            //receive input from android app
            while (true) {
                byte[] inputBuffer = new byte[1024];
                int result = inputStream.read(inputBuffer);
                String input = new String(inputBuffer);
                System.out.println(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
