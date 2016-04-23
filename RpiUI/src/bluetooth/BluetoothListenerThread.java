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
import javax.bluetooth.RemoteDevice;
import com.intel.bluetooth.*;
import invokecommandline.InvokeCommandLine;
import rpiui.ParseMMData;
import ScriptBuilder.ScriptBuilder;

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

    //Key strings used to determine what type of data has been received from Android.
    private static final String TWITTER_KEY = "Tw:";
    private static final String CALENDAR_KEY = "Ca:";
    private static final String WEATHER_KEY = "We:";
    private static final String QUOTE_KEY = "Qu:";
    private static final String EXIT_KEYWORD = "DONE";
    private static final String SLEEP_KEYWORD = "SLEEP";
    private static final String MAKE_CONNECTION_KEYWORD = "CONNECT";

    private BluetoothListenerThread btThread;

    /**
     * MAC address of the Bluetooth adapter on raspberry pi
     */
    public static String bluetoothAddress;

    /**
     * Represents the nature of the device's current discoverable mode. Valid
     * values are DiscoveryAgent.GIAC - 0x9E8B33 (10390323), DiscoveryAgent.LIAC
     * - 0x9E8B00 (10390272), DiscoveryAgent.NOT_DISCOVERABLE - 0x00 (0), or a
     * value in the range 0x9E8B00 to 0x9E8B3F.
     */
    public static int discoverableMode;

    /**
     * Used along with url to facilitate listening for connections that specify
     * the matching UUID.
     */
    public static StreamConnectionNotifier notifier;

    /**
     * Return value of getOrSetDiscoverableMode(). True if this device is
     * discoverable, false if not.
     */
    public static boolean discoverable;

    /**
     * UUID that is know on both client and server side used to create
     * BluetoothSockets.
     */
    public static UUID uuid;

    /**
     * Needed to open notifier.
     */
    public static String url;

    /**
     * The connection to the Android app which is created when a connection is
     * accepted.
     */
    public static StreamConnection connection;

    /**
     * The LocalDevice class defines the basic functions of the Bluetooth
     * manager.
     */
    public static LocalDevice localDevice;

    /**
     * The Bluetooth hostname that the device uses for connections to other
     * devices.
     */
    public static String bluetoothFriendlyName;

    /**
     * Reference to the main thread so that insertions into JTextAreas and
     * printing to the screen are all handled by the UI thread.
     */
    public static RpiUI mainThread;

    /**
     * Used to get the list of known remote Bluetooth devices.
     */
    public static DiscoveryAgent discoveryAgent;

    /**
     * Array of remote devices known to the Bluetooth Adapter.
     */
    public static RemoteDevice[] remotes;

    /**
     * Constructor which takes a reference to an RpiUI object as an argument.
     *
     * @param mainThread
     */
    public BluetoothListenerThread(RpiUI mainThread) {
        this.mainThread = mainThread;
    }

    /**
     * No arg constructor which sets the reference to the UI thread to null.
     */
    public BluetoothListenerThread() {
        this.mainThread = null;
    }

    /**
     * Returns the keyword that designates a Twitter message.
     *
     * @return a String representing the Twitter message keyword
     */
    public static String getTwitterKey() {
        return TWITTER_KEY;
    }

    /**
     * Returns the keyword that designates a Quote message.
     *
     * @return a String representing the Quote message keyword
     */
    public static String getQuoteKey() {
        return QUOTE_KEY;
    }

    /**
     * Returns the keyword that designates a Calendar message.
     *
     * @return a String representing the Calendar message keyword
     */
    public static String getCalendarKey() {
        return CALENDAR_KEY;
    }

    /**
     * Returns the keyword that designates a Weather message.
     *
     * @return a String representing the Weather message keyword
     */
    public static String getWeatherKey() {
        return WEATHER_KEY;
    }

    /**
     * Invoked on start of the Runnable BluetoothListenerThread thread. This
     * only calls listen().
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
        //if power is off, do not do any further setting up, just display message
        if (!LocalDevice.isPowerOn()) {
            System.out.println("Bluetooth is turned off.");
        } else {
            try {
                findLocalDeviceAddressName();

                //check whether device is discoverable, if not set it, if it cannot be set to discoverable, 
                //discoverable is false
                discoverable = getOrSetDiscoverableMode(localDevice);

                if (discoverable) {
                    mainThread.appendToJTextAreaNewline(mainThread.getTwitterJTextArea(), "I am now discoverable");

                    //The DiscoveryAgent class provides methods to perform device and service discovery (From API)
                    //get a discovery agent from the local device
                    discoveryAgent = localDevice.getDiscoveryAgent();

                    //list of bonded devices known to this device
                    remotes = discoveryAgent.retrieveDevices(DiscoveryAgent.PREKNOWN);

                    //UUID is how the android app finds the raspberry app                 
                    uuid = new UUID(UUIDSTRING, false);
                    //btspp is the URL scheme for an RFCOMM StreamConnection
                    //url required format is btspp: //hostname: [ CN | UUID ]; parameters for StreamConnectionNotifier
                    url = "btspp://localhost:" + uuid.toString() + ";name=RaspberryServer";
                    //url = "btspp://localhost:" + uuid.toString() + ";name=RaspberryServer;authenticate=false;authorize=false;encrypt=false";
                    notifier = (StreamConnectionNotifier) Connector.open(url);

                    //invoke the script to turn on Bluetooth functionality using the bluetoothctl terminal tool
                    InvokeCommandLine test = new InvokeCommandLine();
                    String[] commandAndArgs = {"sudo", "../scripts/bluetooth-initial-connect-script.sh"};
                    test.invoke(commandAndArgs);

                    /*wait for bluetooth connection from Android app, the program will wait at the acceptAndOpen
                        method call line indefinitely for incoming connections so when the program has started
                        whether it is the first run or any subsequent run, the program runs up to that line
                        before waiting for the conneciton*/
                    while (true) {
                        System.out.println("Waiting for bluetooth connection from Android app...");
                        try {
                            //listen for client to connect to the url defined by url
                            connection = notifier.acceptAndOpen();
                            System.out.println("Connection to Android accepted. Bluetooth socket open.");
                            processConnection(connection);

                            //close connection at end of every loop iteration
                            connection.close();
                            System.out.println("Closed bluetooth socket.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (BluetoothStateException e) {
                System.err.println("BluetoothStateException");
                e.printStackTrace();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Tries to locate the local device and if it is found, locates and prints
     * information about the Bluetooth address of the device and its friendly
     * name.
     */
    private void findLocalDeviceAddressName() {
        try {
            //The LocalDevice class defines the basic functions of the Bluetooth manager (From API)
            localDevice = LocalDevice.getLocalDevice();
            System.out.println("Got a local device");

            bluetoothAddress = localDevice.getBluetoothAddress();
            System.out.println("My bluetooth address is " + bluetoothAddress);
            mainThread.replaceJTextArea(mainThread.getTwitterJTextArea(), "My bluetooth address is " + bluetoothAddress);

            bluetoothFriendlyName = localDevice.getFriendlyName();
            System.out.println("Bluetooth friendly name is " + bluetoothFriendlyName + ". This is what you should connect to.");
            mainThread.appendToJTextAreaNewline(mainThread.getTwitterJTextArea(), "Bluetooth friendly name is " + bluetoothFriendlyName + ". This is what you should connect to.");
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns false for an undiscoverable device or if setDiscoverable() could
     * not be completed, true otherwise. Prints verbose output to Sys.out
     * through the process.
     *
     * @param localDevice the Bluetooth adapter
     * @return a boolean representing whether or not the device is discoverable
     */
    private static boolean getOrSetDiscoverableMode(LocalDevice localDevice) {
        boolean available = false;

        //Retrieves the local device's discoverable mode. The return value will be DiscoveryAgent.GIAC - 0x9E8B33 (10390323), 
        //DiscoveryAgent.LIAC -  0x9E8B00 (10390272), DiscoveryAgent.NOT_DISCOVERABLE - 0x00 (0), or a value in the 
        //range 0x9E8B00 to 0x9E8B3F.
        discoverableMode = localDevice.getDiscoverable();
        if (discoverableMode == DiscoveryAgent.NOT_DISCOVERABLE) {
            System.out.println("Not discoverable: mode is " + discoverableMode);
            try {
                //attempt to set device to discoverable mode
                available = localDevice.setDiscoverable(DiscoveryAgent.GIAC);
                if (available) { //if successful
                    discoverableMode = localDevice.getDiscoverable();
                    System.out.println("Now set to discoverable: mode is " + discoverableMode);
                } else {
                    System.out.println("Could not set device to discoverable mode.");
                }
            } catch (IllegalArgumentException | BluetoothStateException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Discoverable: mode is " + discoverableMode);
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

            //create instance of ParseMMData class to format the incoming data
            ParseMMData parser = new ParseMMData(getTwitterKey(), getCalendarKey(), getWeatherKey(), getQuoteKey());

            System.out.println("waiting for input");

            //receive input from android app, break from this loop when inputStream.read() returns -1 or if EXIT_KEYWORD is read
            while (true) {
                byte[] inputBuffer = new byte[1024];
                int result = inputStream.read(inputBuffer);
                String input = new String(inputBuffer);
                input = input.trim();
                //if result is -1, the read from inputBuffer read nothing so we assume that we should close the socket on this side
                if (input.regionMatches(0, EXIT_KEYWORD, 0, 4) || result == -1) {
                    inputStream.close();
                    break;
                    //if the SLEEP_KEYWORD is read, hide all JTextArea objects
                } else if (input.regionMatches(0, SLEEP_KEYWORD, 0, 5) || result == -1) {
                    mainThread.toggleDisplay();
                    inputStream.close();
                    break;
                } else if (input.regionMatches(0, MAKE_CONNECTION_KEYWORD, 0, 7) || result == -1) {
                    System.out.println("THIS IS CONNECTION DATA: " + input);
                    String[] connect = parser.parseConnectionData(input);
                    String ssid = connect[0];
                    String password = connect[1];
                    ScriptBuilder sb = new ScriptBuilder(ssid, password);
                    //InvokeCommandLine invoke = new InvokeCommandLine(connect[0], connect[1]);
                    //invoke.connectToNetwork();
                    
                    
                    //TODO in InvokeCommandLine.java, add in the name of the script to connect
                    mainThread.appendToJTextAreaNewline(mainThread.getTwitterJTextArea(), input);
                    inputStream.close();
                    break;
                } else if (input.regionMatches(0, TWITTER_KEY, 0, 3)) {
                    System.out.println("THIS IS TWITTER: " + input);
                    String twit = parser.parseTwitter(input);
                    mainThread.replaceJTextArea(mainThread.getTwitterJTextArea(), twit);
                } else if (input.regionMatches(0, QUOTE_KEY, 0, 3)) {
                    System.out.println("THIS IS QUOTE: " + input);
                    String quote = parser.parseQuote(input);
                    mainThread.replaceJTextArea(mainThread.getQuoteJTextArea(), quote);
                } else if (input.regionMatches(0, WEATHER_KEY, 0, 3)) {
                    System.out.println("THIS IS WEATHER: " + input);
                    String weather = parser.parseWeather(input);
                    mainThread.replaceJTextArea(mainThread.getWeatherJTextArea(), weather);
                } else if (input.regionMatches(0, CALENDAR_KEY, 0, 3)) {
                    System.out.println("THIS IS CALENDAR: " + input);
                    String cal = parser.parseCalendar(input);
                    mainThread.replaceJTextArea(mainThread.getCalendarJTextArea(), cal);
                } else {
                    //append any "other" data to the Twitter area and print to Sys.out
                    System.out.println("This: " + input + " isn't twitter, weather, quote, or calendar data");
                    //mainThread.appendToJTextAreaNewline(mainThread.getTwitterJTextArea(), input);
                } //System.out.println("End of input.");
            }
            System.out.println("End of input.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
