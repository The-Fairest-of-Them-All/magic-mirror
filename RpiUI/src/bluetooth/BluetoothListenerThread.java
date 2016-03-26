/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetooth;

import java.io.IOException;
import java.io.InputStream;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import com.intel.bluetooth.BluetoothStackBlueZ;
import com.intel.bluetooth.BluetoothStackBlueZNativeTests;

import rpiui.RpiUI;

/**
 * A thread used to asynchronously query the device about its bluetooth
 * capabilities and if the capabilities are sufficient, i.e. there is a
 * bluetooth device, it is turned on, and it can be set to discoverable mode,
 * creates a server side socket and listens for a connection request from
 * android app. Finally, if the connection is established, this thread reads in
 * the data passed by the android app.
 *
 * @author Keith Rasweiler
 */
public class BluetoothListenerThread implements Runnable {

    //this is the UUID that is used to create a socket between android and raspberry via bluetooth
    private final String UUIDSTRING = "a96d5795f8c34b7a9bad1eefa9e11a94";
    private static final String EXIT_KEYWORD = "DONE";
    private static final String TWITTER_KEY = "T: ";
    private static final String CALENDAR_KEY = "C: ";
    private static final String WEATHER_KEY = "W: ";
    private static final String QUOTE_KEY = "Q: ";

    public static String bluetoothAddress; //MAC address of the bluetooth adapter on raspberry pi
    public static int discoverableMode; //values described in getOrSetDiscoverableMode()
    public static StreamConnectionNotifier notifier;
    public static boolean discoverable;
    public static UUID uuid; //UUID that is know on both client and server side used to create BluetoothSockets
    public static String url;
    public static StreamConnection connection;
    public static LocalDevice localDevice; //The LocalDevice class defines the basic functions of the Bluetooth manager
    public static String bluetoothFriendlyName;
    public static RpiUI mainThread;

    public BluetoothListenerThread(RpiUI mainThread) {
        this.mainThread = mainThread;
    }

    /**
     * Invoked on start of the Runnable BluetoothListenerThread thread. This only calls listen().
     */
    @Override
    public void run() {
        listen();
    }

    /**
     * Does preliminary checks on the bluetooth device connected to the
     * computer. First checks to ensure that the device is powered on, next
     * tries to get the bluetooth MAC address and friendly name and prints them
     * to Sys.out. The friendly name and MAC address are needed to facilitate
     * the connection along with the UUID. Next, checks to see if device is
     * discoverable and attempts to set it to discoverable if not using a call
     * to getOrSetDiscoverableMode(). Finally creates a StreamConnectionNotifier
     * object and uses the acceptAndOpen() method to accept an incoming
     * bluetooth connection request. The resulting StreamConnection serves as
     * the server socket and is passed to processConnection().
     */
    private void listen() {
        if (!LocalDevice.isPowerOn()) { //if power is off, do not do any further setting up, just display message
            System.out.println("Bluetooth is turned off.");
            return;
        } else {
            try {
                //The LocalDevice class defines the basic functions of the Bluetooth manager (From API)
                localDevice = LocalDevice.getLocalDevice();
            } catch (BluetoothStateException e) {
                System.err.println("BluetoothStateException in localDevice = LocalDevice.getLocalDevice();");
                e.printStackTrace();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            try {
                bluetoothAddress = localDevice.getBluetoothAddress();
                System.out.println("My bluetooth address is " + bluetoothAddress);
                mainThread.replaceJTextArea(mainThread.getTwitterJTextArea(), "My bluetooth address is " + bluetoothAddress);
                bluetoothFriendlyName = localDevice.getFriendlyName();
                System.out.println("Bluetooth friendly name is " + bluetoothFriendlyName + ". This is what you should connect to.");
                mainThread.appendToJTextAreaNewline(mainThread.getTwitterJTextArea(), "Bluetooth friendly name is " + bluetoothFriendlyName + ". This is what you should connect to.");
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
                    System.out.println("Connection to Android accepted. Bluetooth socket open.");
                    processConnection(connection);
                    
                    //close connection at end of every loop iteration
                    System.out.println("Closing bluetooth socket.");
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns false for an undiscoverable device or if setDiscoverable() could
     * not be completed, true otherwise. Prints verbose output to Sys.out
     * through the process.
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
                available = localDevice.setDiscoverable(DiscoveryAgent.GIAC);
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
     * Opens an InputStream from the StreamConnection object and receives data
     * passed by android app into a byte[] buffer. Writes the received data into
     * the display.
     *
     * @param connection an open StreamConnection object
     */
    private static void processConnection(StreamConnection connection) {
        try {
            // prepare to receive data
            InputStream inputStream = connection.openInputStream();

            System.out.println("waiting for input");

            //receive input from android app, break from this loop when inputStream.read() returns -1
            while (true) {
                byte[] inputBuffer = new byte[1024];
                int result = inputStream.read(inputBuffer);
                String input = new String(inputBuffer);
                input = input.trim();
                //if result is -1, the read from inputBuffer read nothing so we assume that we should close the socket on this side
                if (input.equals(EXIT_KEYWORD) || result == -1) {
                    inputStream.close();
                    break;
                } else {
                    /*check to see if String represents Twitter data by comparing first 3 characters to defined string.
                        and based on that result, write into the appropriate seciton on the screen*/
                    if (input.regionMatches(0, TWITTER_KEY, 0, 3)) {
                        System.out.println("THIS IS TWITTER: " + input);
                        mainThread.appendToJTextAreaNewline(mainThread.getTwitterJTextArea(), input);
                    } else if (input.regionMatches(0, QUOTE_KEY, 0, 3)) {
                        System.out.println("THIS IS QUOTE: " + input);
                        mainThread.replaceJTextArea(mainThread.getQuoteJTextArea(), input);
                    } else if (input.regionMatches(0, WEATHER_KEY, 0, 3)) {
                        System.out.println("THIS IS WEATHER: " + input);
                        mainThread.replaceJTextArea(mainThread.getWeatherJTextArea(), input);
                    } else if (input.regionMatches(0, CALENDAR_KEY, 0, 3)) {
                        System.out.println("THIS IS CALENDAR: " + input);
                        mainThread.replaceJTextArea(mainThread.getCalendarJTextArea(), input);
                    } else {
                        //append any "other" data to the Twitter area and print to Sys.out
                        System.out.println("This: " + input + " isn't twitter, weather, quote, or calendar data");
                        mainThread.appendToJTextAreaNewline(mainThread.getTwitterJTextArea(), input);
                    }

                    //TODO temporary lat and long data so we can get weather integrated while working on location in android
                    //this is Temple coordinates
                    System.out.println("Temp version of weather.");
                    String latitude = "39.981830";
                    String longitude = "-75.155407";
                    StringBuilder weather = new StringBuilder("Latitude: ");
                    weather.append(latitude).append("\n").append("Longitude: ").append(longitude);
                    mainThread.replaceJTextArea(mainThread.getWeatherJTextArea(), weather.toString());

                    System.out.println("End of input.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
