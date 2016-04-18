package fairestintheland.magicmirror;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**used for create a connection between mobile application and the Raspberry Pi,
 and create a UI for the Android application*/
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    //UI objects

    /**
     DrawerLayout mDrawerLayout: 	a Layout for the all switch state, and it can hide on the left side of the application
     ActionBarDrawerToggle mDrawerToggle: Toggle for hide and display the Drawer Layout
     CharSequence mTitle:	 the title of the drawer layout
     ArrayList<Switch> theSwitches:	store all switch states
     MenuAdapter<Switch> adapter:  set up all elements for the switch listview
     ListView navList: a ListView which is placed in the DrawerLayout and display the switches’ view
     Button sleepButton: set the sleeping state
     Button syncButton: send all switch states to Raspberry Pi
     boolean sleeping: state for switch off the mirror screen
     boolean[] switchState: store all switch states
     EditText ipBar: user enter the Raspberry’s IP address
     Socket client: client socket which used for set up the connection
     PrintWriter writer: used to send data to other side in socket
     String ipAddress: IP address of the Raspberry Pi
     JSONArray parcel: store all messages which will send to the sever
     Context context: context of current activity
     ConnectivityManager cm: ConnectivityManager of current context

     */
    static final int TwitterAccount_REQUEST_CODE = 1111;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<Switch> theSwitches;
    private MenuAdapter<Switch> adapter;
    private ListView navList;
    private Button sleepButton;
    private Button settingButton;
    private Button syncButton;
    private Button wifiButton;
    private Button twitterAccountButton;
    private Button discoverButton;
    private boolean sleeping;
    private boolean[] switchStates;
      EditText ssidView ;
      EditText passwordView ;
    EditText ipBar;

    //connection variables
    private Socket client;
    private PrintWriter writer;
    String ipAddress;
    JSONArray parcel;
    Context context;
    ConnectivityManager cm;

    BluetoothSocketConnection bluetoothSocketConnection;

    //bluetooth vars
    //int used to determine what activity was presented in the onActivityResult() method
    private static final int REQUEST_ENABLE_BT = 100;
    private String raspberryPiName = "";
    //string defined on android and raspberry sides to establish connection
    private final String UUIDSTRING = "a96d5795-f8c3-4b7a-9bad-1eefa9e11a94";
    private static final String EXIT_KEYWORD = "DONE";
    private static final String SLEEP_KEYWORD = "SLEEP";
    private static final String MAKE_CONNECTION_KEYWORD = "CONNECT";
    private static final String TWITTER_KEY = "T: ";
    private static final String CALENDAR_KEY = "C: ";
    private static final String WEATHER_KEY = "W: ";
    private static final String QUOTE_KEY = "Q: ";

    private final int DATA = 0;
    private final int SLEEP = 1;
    private final int CONNECT = 2;

    String consumerKey = "bUh6sDhIGpN4UdE55litSTD8W";
    String consumerSecret = "OlByFoaS9lJ8ewZEw9DOPGgVrby9EM6SepllWXrCnraw49r9DC";
    String accessToken = "4889865377-JoGReMh6w6yS2PQQ8hKcVpHlaIKRH1gM4vGf6ui";
    String accessTokenSecret = "qIvONWidLP10yKXsYyHfu1k3yzppUpxhbUc7TucF3bpp6";

    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    TextView bluetoothInfo;
    ListView discoverableList;
    boolean bluetoothAvailable;
    IntentFilter filter;
    BluetoothDevice device;
    ArrayList<Parcelable> devices = new ArrayList<>(); //hostname and MAC address of every device that is discoverable
    BluetoothSocket clientSocket = null;
    UUID uuid;
    InputStream clientSocketInputStream;
    OutputStream clientSocketOutputStream;
    EditText raspberryNameEditText;
    ArrayList<String> currentEvent;
    CalendarEvent cEvent;

    TwitterMessage tMess;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    //GetLocation myLocation;
    String latitude = "";
    String longitude = "";
    public String eventMessage;
    Set<BluetoothDevice> bondedDevices;

    LocationRequest mLocationRequest;
    LocationSettingsRequest.Builder mLocationSettingsRequestBuilder;
    final int MY_PERMISSIONS_REQUEST_LOCATION = 1001;
    Intent gpsOptionsIntent;
    final int ENABLE_LOCATION = 900;
    int locationSettings;

    String[] quotes = {"Strive not to be a success, but rather to be of value. -Albert Einstein",
            "Every strike brings me closer to the next home run. -Babe Ruth",
            "Life is what happens to you while you're busy making other plans. -John Lennon",
            "I'm ready - Spongebob Squarepants"};
    int quotePosition;

    Handler toastMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String stringMess = bundle.getString("myKey");
            try{
                Toast.makeText(context,stringMess, Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cEvent = new CalendarEvent();
        currentEvent = cEvent.readCalendarEvent(this);
        String[] test = cEvent.getCNames();
        System.out.println("!!You got an event!!!" + cEvent.readCalendarEvent(this));

        quotePosition = quotes.length;

        tMess = new TwitterMessage(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        tMess.getTweet();

        theSwitches = new ArrayList<Switch>();
        initSwitches();
        switchStates = LoadSwitchStates(); //Twitter,Quote,Weather,Calendar

        context = this;
        sleeping = false;

        navList = (ListView) findViewById(R.id.left_drawer);
        adapter = new MenuAdapter<>(this, android.R.layout.simple_list_item_1, theSwitches);
        navList.setAdapter(adapter);

        sleepButton = (Button) findViewById(R.id.sleepButton);
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleeping = !sleeping;
                //setSleepMode();
                makeRaspberrySleep();
                /*if (sleeping) {
                    sleepButton.setText(R.string.wake_button);
                } else {
                    sleepButton.setText(R.string.sleep_button);
                }*/
            }
        });

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        settingButton = (Button) findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        twitterAccountButton = (Button)findViewById(R.id.TwitterAccountButton_main);
        View.OnClickListener oclA = new View.OnClickListener(){
            public void onClick(View v){
                Intent activityAIntent = new Intent (MainActivity.this,TwitterLogActivity.class);
                startActivityForResult(activityAIntent, TwitterAccount_REQUEST_CODE);
            }
        };
        twitterAccountButton.setOnClickListener(oclA);

      ssidView = (EditText) findViewById(R.id.ssidText);
         passwordView = (EditText) findViewById(R.id.passWordText);
        ssidView.setText("Feather");
        passwordView.setText("privatekey");

        wifiButton = (Button) findViewById(R.id.WiFibutton);
        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final WifiAccess wi = new WifiAccess(ssidView.getText().toString(), passwordView.getText().toString(), context);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        Bundle bundle = new Bundle();
                        String stringMess = "Wrong Account Info,please enter again!";
                        //if( wi.isConnect()){
                            stringMess ="Correct wifi Info!";
                            tryToConnect(CONNECT);
                        //}
                        bundle.putString("myKey",stringMess);
                        Message msg = toastMessageHandler.obtainMessage();
                        msg.setData(bundle);
                        toastMessageHandler.sendMessage(msg);
                    }
                }, 3000);
            }
        });

        /*syncButton = (Button) findViewById(R.id.connect_button);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSONMessage();

                //syncWithPi();
                //callSync();
                new MyClientTask().execute();
            }
        });*/

        //start bluetooth services
        bluetoothInfo = (TextView) findViewById(R.id.bluetoothInfo);
        raspberryNameEditText = (EditText) findViewById(R.id.raspberryName);
        raspberryPiName = LoadHostName();
        raspberryNameEditText.setText(raspberryPiName);

        new BluetoothAsync().execute();

        //ask user to turn on location if it is not currently enabled
        getOrSetLocation();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    } //End onCreate()


    //---------LOCATION SECTION--------------------------------------------------------------------------
    /**
     * Checks the permissions for access to the user's location and if location is not currently enabled,
     * presents an activity requesting that user enables location on the phone.
     */
    private void getOrSetLocation() {
        locationSettings = 0;
        try {
            locationSettings = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

            if (locationSettings == 0) {
                gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(gpsOptionsIntent, ENABLE_LOCATION);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overridden to manually manage the Google play connection.
     */
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    /**
     * Overridden to manually manage the Google play connection.
     */
    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Returns the latitude as a String.
     *
     * @return latitude as a String
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude as a String.
     *
     * @return longitude as a String
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Called when the app has successfully connected to the Google play services. Checks that appropriate
     * permissions are declared in the Manifest and if they are, tries to retrieve the last known location.
     *
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        //check that Manifest declares permissions and that they are allowed
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(this, "CurrentLocation retrieved", Toast.LENGTH_LONG).show();
            latitude = (String.valueOf(mLastLocation.getLatitude()));
            longitude = (String.valueOf(mLastLocation.getLongitude()));
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);
        } else {
            Toast.makeText(this, "CurrentLocation was null", Toast.LENGTH_LONG).show();
            System.out.println("CurrentLocation was null");
        }
    }

    /**
     * Called before passing data to raspberry. Checks that GPS is enabled, checks that Manifest declarations
     * are ok, and if these are successful, tries to retrieve most recent location.
     */
    public void lookForLocation() {
        //if location is not yet enabled, ask user to enable it
        if (locationSettings == 0) {
            getOrSetLocation();
        }

        //check that Manifest declares permissions and that they are allowed
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(this, "CurrentLocation retrieved", Toast.LENGTH_LONG).show();
            latitude = (String.valueOf(mLastLocation.getLatitude()));
            longitude = (String.valueOf(mLastLocation.getLongitude()));
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);
        } else {
            Toast.makeText(this, "CurrentLocation was null", Toast.LENGTH_LONG).show();
            System.out.println("CurrentLocation was null");
        }
    }

    /**
     * Called after consulting the Manifest to check that permissions are met and granted.
     *
     * @param requestCode int value passed to the request
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("CurrentLocation permission granted.");
                    Toast.makeText(this, "CurrentLocation permission granted", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("CurrentLocation permission denied.");
                    Toast.makeText(this, "CurrentLocation permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOC", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOC", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    /*protected void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        //mGoogleApiClient.disconnect();
        SaveSwitchStates();
        SaveHostName();
        myLocation.stopLocationServices();
        super.onStop();
    }*/
    //-----------END LOCATION SECTION---------------------------------------------------------------------

    //-----------FILE I/O-------------------------------------------------------------------------------

    /**
     * Saves the state of the boolean switches which designate user preferences.
     */
    protected void SaveSwitchStates()
    {
        String saveLocation = "switches.ser";
        FileOutputStream output;
        try
        {
            output = openFileOutput(saveLocation, Context.MODE_PRIVATE);
            ObjectOutputStream objOut = new ObjectOutputStream(output);
            objOut.writeObject(switchStates);
            objOut.close();
            output.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads in the boolean switches which designate user preferences and returns them as a boolean[].
     *
     * @return a boolean[] representing user preferences
     */
    protected boolean[] LoadSwitchStates()
    {
        boolean[] b = new boolean[4];
        try {
            FileInputStream input = openFileInput("switches.ser");
            ObjectInputStream objIn = new ObjectInputStream(input);
            b = (boolean[])objIn.readObject();
            input.close();
            objIn.close();

        }catch(FileNotFoundException ex) //initial load, make default array
        {
            b = new boolean[]{false,false,false,false};
            return b;
        }
        catch (Exception e) //something else went wrong
        {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * Saves the bluetooth hostname any time a successful connection has been made.
     */
    protected void SaveHostName() {
        String fileName = "hosts.txt";
        FileOutputStream output;
        try
        {
            output = openFileOutput(fileName, Context.MODE_PRIVATE);
            output.write(raspberryPiName.getBytes());
            output.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Recalls the bluetooth hostname and returns it as a String.
     *
     * @return a String representing the saved bluetooth hostname
     */
    protected String LoadHostName() {
        String s = "";
        try {
            FileInputStream input = openFileInput("hosts.txt");
            byte[] bytes = new byte[1024];
            input.read(bytes);
            for(byte b : bytes)
            {
                s += (char)b;
            }
            input.close();

        }catch(FileNotFoundException ex) //initial load, make default string
        {
            return "Enter raspberry computer name here";
        }
        catch (Exception e) //something else went wrong
        {
            e.printStackTrace();
        }
        return s;
    }
    //-----------END FILE I/O---------------------------------------------------------------------------

    //----------BLUETOOTH SECTION-------------------------------------------------------------------------
    /**
     * Performed asynchronously to set up bluetooth services in the background. This overrides the
     * doInBackground and onPostExecute methods. It checks to see if bluetooth is supported on the
     * android device first. If so, it checks to see whether bluetooth is enabled. If bluetooth is
     * not enabled, it prompts to=he user with an activity so they can turn on bluetooth if desired.
     * It also provides output to Sys.out for the programmer through the process.
     */
    public class BluetoothAsync extends AsyncTask<Void, Void, Void> {
        BluetoothAsync() {}

        @Override
        protected Void doInBackground(Void... arg0) {
            setupBluetooth();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //textResponse.setText(response);
            System.out.println("BluetoothAsync complete.");
            super.onPostExecute(result);
        }
    }

    /**
     * Checks to see if bluetooth is available and enabled on the phone, if it is not enabled,
     * user will be presented with an activity to turn on bluetooth ,the response will be handled
     * by onActivityResult
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setupBluetooth() {
        // Register the BroadcastReceiver
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) { //bluetooth not supported
            bluetoothInfo.setText("Bluetooth unavailable on this device.");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                //presents activity to ask user to turn on bluetooth, response processed by onActivityResult()
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                bluetoothInfo.setText("Bluetooth available and on.");
            }

            //UUID is how the android app finds the raspberry app, this is also defined on the raspberry side
            uuid = UUID.fromString(UUIDSTRING);
        }
    }

    /**
     * Respond to a user's interacting with an activity presented to turn on various capabilities.
     * It handles twitter, bluetooth, and location requests by examining the requestCode to determine
     * the request type.
     *
     * @param requestCode Defined in the calling code, allows response to multiple events,
     *                    REQUEST_ENABLE_BT represents a bluetooth activity request,
     *                    TwitterAccount_REQUEST_CODE is a twitter request, and
     *                    ENABLE_LOCATION is for a location request
     * @param resultCode Results of user's interaction with the activity
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //responds to the user's response to the bluetooth enable prompt if it was presented
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                bluetoothInfo.setText("You enabled bluetooth. Thanks.");
            } else {
                bluetoothInfo.setText("Bluetooth disabled.");
            }
        }

        if (requestCode == TwitterAccount_REQUEST_CODE) {
            System.out.println("!!!!!!!you got a intent from twitter!!!!!!!!!!!!!!!!!!!!!!!!");
            if (resultCode == RESULT_OK) {
                String accountState = data.getStringExtra("accountState");
                if(accountState.equals("true")){
                    System.out.println("!!!!!!!!!!1success change the aaccount!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                else{ System.out.println("!!!!!!!!!!1false change the aaccount!!!!!!!!!!!1!!!!");}
            }
        }

        //responds to the user's response to the location enable prompt if it was presented
        if (requestCode == ENABLE_LOCATION) {
            if (resultCode == RESULT_OK) {
                System.out.println("You enabled location.");
            } else {
                System.out.println("You did not enable location.");
            }
        }
    }

    /**
     * Responds to discover button press, starts discovery of remote bluetooth devices that are set to
     * discoverable. If the default string was not changed, the user is prompted to input a valid
     * hostname.
     *
     * @param view
     */
    public void startLooking(View view) {
        discoverButton = (Button) findViewById(R.id.discoverDevices);
        discoverButton.setEnabled(false);
        discoverButton.setClickable(false);
        //String originalText = discoverButton.getText().toString();
        //discoverButton.setText("Don't click right now");

        //check if bluetooth is enabled before trying to use it
        if (!bluetoothAdapter.isEnabled()) {
            new BluetoothAsync().execute();
        } else {
            //only start discovery if user has entered a remote hostname
            if (!raspberryNameEditText.getText().toString().trim().equals("Enter raspberry computer name here")) {
                if (!raspberryNameEditText.getText().toString().trim().isEmpty()) {
                    raspberryPiName = raspberryNameEditText.getText().toString().trim();

                    //if false, bluetooth off, otherwise start discovery, when results arrive the callback is BroadcastReceiver
                    bluetoothAvailable = bluetoothAdapter.startDiscovery();
                } else {
                    raspberryNameEditText.setText("Please enter raspberry pi bluetooth name");
                }
            }
        }
        discoverButton.setEnabled(true);
        discoverButton.setClickable(true);
        //discoverButton.setText(originalText);
    }

    /**
     * Create a BroadcastReceiver for ACTION_FOUND, this is called every time a new device is found
     * during device discovery. If a name matching the string defined by raspberryPiName, which the
     * user can enter via am EditText object, tryToConnect() is called to create a BluetoothSocket
     * and attempt to connect to a listening ServerBluetoothSocket. If the uesr does not enter the
     * remote hostname correctly, tryToConnect() will not successfully connect.
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                // Add the name and address to an array adapter to show in a ListView
                System.out.println("Found " + device.getName() + " at " + device.getAddress());
                //discoverableList.add(device.getName() + "\n" + device.getAddress());
                System.out.println(devices);

                //try a connection based on device name
                try {
                    if (device.getName().equals(raspberryPiName)) {
                        BluetoothDevice btDevice = device;
                        //TODO initialConnect is the original, insecureConnectAndSend is attempt at no user input
                        initialConnect();
                        //insecureConnectAndSend();
                        return;
                    }
                } catch (NullPointerException e) {
                    System.out.println("The name was null");
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * Attempt a bluetooth connection to a device whose name is known from the device discovery process.
     * The device hostname is input by the user and this method is only called if that user input String
     * matches a device hostname found in the discovery process. This method establishes a BluetoothSocket
     * using createRfcommSocketToServiceRecord() and tries to connect to a listening BluetoothServerSocket
     * that uses the same UUID. Before the method completes, the BluetoothSocket is closed so that on another
     * button press to sync data, a new connection is established.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initialConnect() {
        BluetoothDevice btDevice = device;
        System.out.println("Trying to establish initial connection to " + raspberryPiName);

        btDevice = bluetoothAdapter.getRemoteDevice(btDevice.getAddress());

        try {
            boolean bonded = btDevice.createBond();
            if (bonded) {
                System.out.println("Paired with raspberry pi.");
            }
            /*clientSocket = btDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            //clientSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
            clientSocket.connect();
            System.out.println("Connected to raspberry pi.");
            //close socket after initial connection is made
            clientSocket.close();
            System.out.println("Socket successfully closed. Now paired.");*/
            SaveHostName();
            System.out.println("Saved the hostname " + raspberryPiName);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    //TODO this is the insecure method, it is called by the BroadcastReciever right now instead of initialConnect
    public void insecureConnectAndSend() {
        BluetoothDevice btDevice = device;
        System.out.println("Trying to establish insecure connection to " + raspberryPiName);

        try {
            //clientSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
            clientSocket = btDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            clientSocket.connect();
            System.out.println("Connected to raspberry pi.");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        writeContentToSocket(clientSocket);

        //close socket before end of method so every time the sync button is pressed, a new connection is made
        try {
            clientSocket.close();
            SaveHostName(); //sae host name after successful connect
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Called by the click of the Connect button. Method progression is
     * syncContent->tryToConnect->writeContentToSocket
     * If the devices are already paired, content is delivered to raspberry pi.
     *
     * @param view
     */
    public void syncContent(View view) {
        syncButton = (Button) findViewById(R.id.connect_button);
        syncButton.setEnabled(false);
        syncButton.setClickable(false);
        //String originalText = syncButton.getText().toString();
        //syncButton.setText("Don't click right now");

        //check if bluetooth is enabled before trying to use it
        if (!bluetoothAdapter.isEnabled()) {
            new BluetoothAsync().execute();
        } else {
            //only start discovery if user has entered a remote hostname
            if (!raspberryNameEditText.getText().toString().trim().equals("Enter raspberry computer name here")) {
                if (!raspberryNameEditText.getText().toString().trim().isEmpty()) {
                    raspberryPiName = raspberryNameEditText.getText().toString().trim();

                    //call tryToConnect and specify that the app content should be passed
                    tryToConnect(DATA);
                } else {
                    raspberryNameEditText.setText("Please enter raspberry pi bluetooth name");
                }
            }
        }
        syncButton.setEnabled(true);
        syncButton.setClickable(true);
        //syncButton.setText(originalText);
    }

    /**
     * Attempt a bluetooth connection to a device whose name is known from the list of bonded devices.
     * The device hostname is input by the user and this method compares the user input hostname to the
     * names of bonded devices and proceeds to deliver content if a match is foundT. his method establishes a
     * BluetoothSocket using createRfcommSocketToServiceRecord() and tries to connect to a listening
     * BluetoothServerSocket that uses the same UUID. Before the method completes, the BluetoothSocket is
     * closed so that on another button press to sync data, a new connection is established. It takes an
     * int argument which represents whether the user wants to pass data or connection infor to the raspberry
     * pi or wants to command the raspberry pi to sleep.
     *
     * @param dataOrSleepORConnect an int that takes once of 3 defined values, DATA, SLEEP, or CONNECT
     */
    private void tryToConnect(int dataOrSleepORConnect) {
        BluetoothDevice btDevice = null;
        //query bonded devices, store them in a Set
        bondedDevices = bluetoothAdapter.getBondedDevices();
        raspberryPiName = raspberryNameEditText.getText().toString().trim();
        //compare bonded device names with what the user entered into the raspberryNameEditText field
        Iterator<BluetoothDevice> iterator = bondedDevices.iterator();
        while (iterator.hasNext()) {
            BluetoothDevice temp = iterator.next();
            String tempName = temp.getName();
            if (tempName.equals(raspberryPiName)) {
                btDevice = temp;
                break;
            }
        }

        //if device is found, try to connect via bluetoothSockets, if not, break
        if (btDevice == null) {
            System.out.println("Device name that you input did not match a bonded device.");
            return;
        } else {
            System.out.println("Trying to connect to bonded device " + raspberryPiName);
            try {
                //TODO the following is original
                clientSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
                //TODO try this for no auth clientSocket = btDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                clientSocket.connect();
                System.out.println("Connected to raspberry pi.");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            //determine which method to call to write data to raspberry based on the passed int value
            if (dataOrSleepORConnect == DATA) {
                //write content to the socket if the socket was opened successfully
                writeContentToSocket(clientSocket);
            }else if (dataOrSleepORConnect == SLEEP){
                writeSleepToSocket(clientSocket);
            } else if (dataOrSleepORConnect == CONNECT) {
                writeConnectDataToSocket(clientSocket);
            }

            //close socket before end of method so every time the sync button is pressed, a new connection is made
            try {
                clientSocket.close();
                SaveHostName(); //sae host name after successful connect
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes the SSID and password to the raspberry pi for programmatic connection to the user's network.
     *
     * @param clientSocket an open and connected BluetoothSocket object
     */
    public void writeConnectDataToSocket(BluetoothSocket clientSocket) {
        try {
            //JSONObject obj = getWIFIAccount();
            String obj = makeConnectionIntoObject();

            if (obj == null) {
                return;
            }

            byte[] buffer;  // buffer store for the stream
            clientSocketOutputStream = clientSocket.getOutputStream();

            //write SSID and password to raspberry pi
            StringBuilder build = new StringBuilder(MAKE_CONNECTION_KEYWORD);
            build.append(obj.toString());
            buffer = build.toString().getBytes();
            clientSocketOutputStream.write(buffer);
            System.out.println("Wrote " + new String(buffer) + " to raspberry.");
            clientSocketOutputStream.flush();

            //write break keyword to end to socket connection on both sides
            clientSocketOutputStream.write(EXIT_KEYWORD.getBytes());
            System.out.println("Wrote " + EXIT_KEYWORD + " to raspberry.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * The method is passed an open BluetoothSocket. It checks all four switch states in order to
     * determine what data to send to the raspberry app and then sends that data in order as well.
     * The data is always sent as a byte array so it must be converted or serialized before sending
     * in most cases.
     *
     * @param clientSocket an open and connected BluetoothSocket object
     */
    public void writeContentToSocket(BluetoothSocket clientSocket) {
        try {
            byte[] buffer;  // buffer store for the stream
            //buffer = raspberryPiName.getBytes();
            //clientSocketInputStream = clientSocket.getInputStream();
            clientSocketOutputStream = clientSocket.getOutputStream();
            //clientSocketOutputStream.write(buffer);
            //System.out.println("Wrote " + new String(buffer) + " to raspberry.");

            /*In each of the following conditional statements, if the user has requested the data by
            *   flipping a switch to turn that feature on, the data is requested from the appropriate
            *   class, and the data is appended to a string containing a key that will help the raspberry
            *   pi identify the class of data it has received.*/

            //send Twitter if true
            if (switchStates[0]) {
                String tweet = tMess.returnTweet();
                //buffer = new byte[1024];  // buffer store for the stream
                StringBuilder output = new StringBuilder(TWITTER_KEY);
                output.append(tweet);
                buffer = output.toString().getBytes();
                clientSocketOutputStream.write(buffer);
                System.out.println("Wrote " + new String(buffer) + " to raspberry.");
                clientSocketOutputStream.flush();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Log.d("Sleep", "Issue sleeping.");
            }

            //send quote data, rotate through quote array
            if(switchStates[1]) {
                StringBuilder output = new StringBuilder(QUOTE_KEY);
                if (quotePosition == quotes.length)
                    quotePosition = 0;
                output.append(quotes[quotePosition]);
                quotePosition++;
                buffer = output.toString().getBytes();
                clientSocketOutputStream.write(buffer);
                System.out.println("Wrote " + new String(buffer) + " to raspberry.");
                clientSocketOutputStream.flush();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Log.d("Sleep", "Issue sleeping.");
            }

            //send location data so that Raspberry can request weather data
            if (switchStates[2]) {
                if (latitude.isEmpty() || longitude.isEmpty()) {
                    lookForLocation();
                }
                if (latitude.isEmpty() || longitude.isEmpty()) {
                    StringBuilder output = new StringBuilder(WEATHER_KEY);
                    output.append("I need your location to get the weather.");
                    buffer = output.toString().getBytes();
                    clientSocketOutputStream.write(buffer);
                    System.out.println("Wrote " + new String(buffer) + " to raspberry.");
                    clientSocketOutputStream.flush();
                } else {
                    latitude = getLatitude();
                    longitude = getLongitude();
                    StringBuilder output = new StringBuilder(WEATHER_KEY);
                    //append the JSON form of the lat and long data
                    output.append(makeLocationIntoObject());
                    buffer = output.toString().getBytes();
                    clientSocketOutputStream.write(buffer);
                    System.out.println("Wrote " + new String(buffer) + " to raspberry.");
                    clientSocketOutputStream.flush();
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Log.d("Sleep", "Issue sleeping.");
            }

            //send calendar events if true
            if  (switchStates[3]) {
                currentEvent = cEvent.readCalendarEvent(this);
                //buffer = new byte[1024];  // buffer store for the stream
                StringBuilder output = new StringBuilder(CALENDAR_KEY);
                output.append(currentEvent.toString());
                buffer = output.toString().getBytes();
                clientSocketOutputStream.write(buffer);
                System.out.println("Wrote " + new String(buffer) + " to raspberry.");
                clientSocketOutputStream.flush();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Log.d("Sleep", "Issue sleeping.");
            }

            //write break keyword to end to socket connection on both sides
            clientSocketOutputStream.write(EXIT_KEYWORD.getBytes());
            System.out.println("Wrote " + EXIT_KEYWORD + " to raspberry.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overridden to unregister the BroadcastReceiver.
     */
    @Override
    protected void onDestroy() {
        //Let's make sure we save before we leave
        SaveSwitchStates();
        //have to unregister BroadcastReceiver before the app closes
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    /**
     * If the user pressed the sleep button and the raspberryNameEditText field passes validation,
     * call tryToConnect with the SLEEP keyword.
     */
    private void makeRaspberrySleep() {
        sleepButton = (Button) findViewById(R.id.sleepButton);
        sleepButton.setEnabled(false);
        sleepButton.setClickable(false);
        //String originalText = sleepButton.getText().toString();
        //sleepButton.setText("Don't click right now");

        //check if bluetooth is enabled before trying to use it
        if (!bluetoothAdapter.isEnabled()) {
            new BluetoothAsync().execute();
        } else {
            //only start trying to send sleep message if text passes validation
            if (!raspberryNameEditText.getText().toString().trim().equals("Enter raspberry computer name here")) {
                if (!raspberryNameEditText.getText().toString().trim().isEmpty()) {
                    raspberryPiName = raspberryNameEditText.getText().toString().trim();

                    //call tryToConnect and specify that the SLEEP keyword should be passed
                    tryToConnect(SLEEP);
                } else {
                    raspberryNameEditText.setText("Please enter raspberry pi bluetooth name");
                }
            }
        }
        sleepButton.setEnabled(true);
        sleepButton.setClickable(true);
        //sleepButton.setText(originalText);
    }

    /**
     * Write the SLEEP_KEYWORD to raspberry pi to cause it to stop displaying all JTextAreas.
     *
     * @param clientSocket open and connected BluetoothSocket
     */
    public void writeSleepToSocket(BluetoothSocket clientSocket) {
        try {
            byte[] buffer;  // buffer store for the stream
            clientSocketOutputStream = clientSocket.getOutputStream();

            buffer = SLEEP_KEYWORD.getBytes();
            clientSocketOutputStream.write(buffer);
            System.out.println("Wrote " + new String(buffer) + " to raspberry.");
            clientSocketOutputStream.flush();

            //write break keyword to end to socket connection on both sides
            clientSocketOutputStream.write(EXIT_KEYWORD.getBytes());
            System.out.println("Wrote " + EXIT_KEYWORD + " to raspberry.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-----------------END BLUETOOTH SECTION-----------------------------------------------------------------




	/** set  Raspberry Pi screen sleep*/
    /*private void setSleepMode() {
        if (hasValidConnection()) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        JSONObject sleep = new JSONObject();
                        sleep.put("sleep_mode", sleeping);
                        client = new Socket(ipAddress, 6685);
                        writer = new PrintWriter(client.getOutputStream(), true);
                        writer.write(sleep.toString());
                        writer.flush();
                        writer.close();
                        client.close();

                    } catch (IOException e) {
                        Toast.makeText(context, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();

        } else {
            Toast.makeText(context, "Please Connect to Internet network", Toast.LENGTH_LONG).show();
        }
    }*/

	/**check if the Android OS has network connection*/
    /*private boolean hasValidConnection() {
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }
        return false;
    }*/

	/**initialize the switches*/
    private void initSwitches() {
        String[] switchNames = getResources().getStringArray(R.array.settings);

        for (final String name : switchNames) {
            final Switch s = new Switch(this);
            s.setText(name);
            theSwitches.add(s);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/**inner class which set up all elements for the switch listview*/
    class MenuAdapter<T> extends ArrayAdapter<Switch> {
		
		/**
		List<Switch> list: store all switch states
		Context context: context of activity which this class used in
		*/
		
        List<Switch> list;
        Context context;

        public MenuAdapter(Context context, int resource, List<Switch> switches) {
            super(context, resource, switches);
            this.context = context;
            list = switches;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Switch getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

		/** Set up the view for one element which is on specify position(index)*/
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;

            // First let's verify the convertView is not null
            if (convertView == null) {
                // This a new view we inflate the new layout
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.switch_row_layout, null);
            }
            TextView text = (TextView) v.findViewById(R.id.switch_name);
            Switch toggle = (Switch) v.findViewById(R.id.prefSwitch);
            toggle.setChecked(switchStates[position]);
            // Now we can fill the layout with the right values
            text.setText(list.get(position).getText());

            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switchStates[position] = isChecked;
                    Log.v("Switch State = ", list.get(position).getText() + " " + switchStates[position]);
                    SaveSwitchStates();
                }
            });

            return v;
        }
    }

    public JSONObject getJSONMessage()  {
        JSONObject newMessage = new JSONObject();
        try{
            for(int i =0;i <switchStates.length;i++){
                newMessage.put(String.valueOf(theSwitches.get(i).getText()),switchStates[i]);
            }
            newMessage.put("events",eventMessage);
            newMessage.put("tweets",tMess.returnTweet());

            System.out.println("JSON Message!!!!!!!!!!!!!!!!!!!!!!" + newMessage.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Ubable to collect all data to JSON!");
        }
        return newMessage;
    }

    public JSONObject getWIFIAccount(){
        JSONObject newMessage = new JSONObject();
        try{
            EditText ssidView = (EditText) findViewById(R.id.ssidText);
            EditText passwordView = (EditText) findViewById(R.id.passWordText);

            newMessage.put("SSID",ssidView.getText());
            newMessage.put("password",passwordView.getText());

            System.out.println("WIFI Message!!!!!!!!!!!!!!!" + newMessage.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Ubable to collect all data to JSON!");
        }
        return newMessage;
    }

    /**
     * Compiles the SSID and password into a JSON object.
     *
     * @return a JSON String containing the SSID and password entered by the user
     */
    public String makeConnectionIntoObject() {
        EditText ssidView = (EditText) findViewById(R.id.ssidText);
        EditText passwordView = (EditText) findViewById(R.id.passWordText);

        ConnectionData connection = new ConnectionData(ssidView.getText().toString(), passwordView.getText().toString());
        Gson gson = new Gson();

        String json = gson.toJson(connection);
        return json;
    }

    /**
     * Compiles the latitude and longitude into a JSON object.
     *
     * @return a JSON String containing the latitude and longitude of the most recent location
     */
    public String makeLocationIntoObject() {
        CurrentLocation loc = new CurrentLocation(latitude, longitude);
        Gson gson = new Gson();

        String json = gson.toJson(loc);
        return json;
    }

    /**
     * Overridden to unregister the BroadcastReceiver.
     */
    /*@Override
    protected void onDestroy() {
        unregisterReceiver(bluetoothSocketConnection.getmReceiver());
        super.onDestroy();
    }*/


    /**
     * Respond to a user's interacting with an activity presented to turn on bluetooth capabilities.
     * This will alert the user as to whether they did or did not successfully enable bluetooth when
     * called with requestCode == bluetoothSocketConnection.getREQUEST_ENABLE_BT()
     *
     * @param requestCode Defined in the calling code, allows response to multiple events,
     *                    REQUEST_ENABLE_BT represents a bluetooth activity request
     * @param resultCode Results of user's interaction with the activity
     * @param data
     */
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //responds to the user's response to the bluetooth enable prompt if it was presented
        if (requestCode == bluetoothSocketConnection.getREQUEST_ENABLE_BT()) {
            if (resultCode == RESULT_OK) {
                //TODO change ref - bluetoothInfo.setText("You enabled bluetooth. Thanks.");
            } else {
                //TODO cheange ref - bluetoothInfo.setText("Bluetooth disabled.");
            }
        }
    }*/

    /*
    //-----------------IP SECTION----------------------------------------------------------------------------
    public class MyClientTask extends AsyncTask<Void, Void, Void> {


        MyClientTask() {

        }

        public int unsignedToBytes(byte b) {
            return b & 0xFF;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            //byte a = unsignedToBytes((byte)-64);
            //byte b = (byte)-64 & 0xFF;

            InetAddress addr = null;
            byte[] ipAddr = new byte[]{-64,-88,43,69};
            ipAddr = new byte[]{-64,-88,43,-93};
            ipAddr = new byte[]{-64,-88,1,-104}; //1.152
            ipAddr = new byte[]{-64,-88,1,-93}; //1.163
            try {
                addr = InetAddress.getByAddress(ipAddr);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            try {
                //socket = new Socket("192.168.43.69",60000);
                //socket = new Socket(addr, 60000);
                socket = new Socket(addr, 55555);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                /*if (msgToServer != null) {
                    dataOutputStream.writeUTF(msgToServer);
                }

                response = dataInputStream.readUTF();****END COMMENT

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //textResponse.setText(response);
            super.onPostExecute(result);
        }

    }

    public void callSync() {
        new Sync().execute();
    }

    public class Sync extends AsyncTask<Void, Void, Void> {

        Sync() {}

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //EditText ipView = (EditText) findViewById(R.id.ipAddressTextInput);
                //ipAddress = ipView.getText().toString();
                Log.d("Sync", "Past getting IP. It is " + ipAddress);
                parcel = new JSONArray();
                for (boolean b : switchStates) {
                    //{
                    parcel.put(b);
                }
                Log.d("Sync", "Parcel created" + parcel.toString());
                client = new Socket("192.168.1.152", 55555);

                Log.d("Sync", "Connected to raspberry pi");
                System.out.println("Connected to raspberry pi.");

                writer = new PrintWriter(client.getOutputStream(), true);
                Log.d("Sync", "About to write to the socket.");
                writer.write(parcel.toString());
                Log.d("Sync", "Wrote to socket");
                writer.flush();
                writer.close();
                client.close();
                //}
            } catch (IOException e) {
                Toast.makeText(context, "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            EditText ipView = (EditText) findViewById(R.id.ipAddressTextInput);
            ipView.setText("Connected");
            super.onPostExecute(aVoid);
        }
    }
//--------------------END IP SECTION------------------------------------------------------------------------------



    private void syncWithPi() {
        if (hasValidConnection()) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        EditText ipView = (EditText) findViewById(R.id.ipAddressTextInput);
                        ipAddress = ipView.getText().toString();
                        Log.d("Sync", "Past getting IP. It is " + ipAddress);
                        parcel = new JSONArray();
                        for (boolean b : switchStates) {
                            //{
                            parcel.put(b);
                        }
                        Log.d("Sync", "Parcel created" + parcel.toString());
                        client = new Socket(ipAddress, 60000);

                        Log.d("Sync", "Connected to raspberry pi");
                        System.out.println("Connected to raspberry pi.");

                        writer = new PrintWriter(client.getOutputStream(), true);
                        Log.d("Sync", "About to write to the socket.");
                        writer.write(parcel.toString());
                        Log.d("Sync", "Wrote to socket");
                        writer.flush();
                        writer.close();
                        client.close();
                        //}
                    } catch (IOException e) {
                        Toast.makeText(context, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            t.start();

        } else {
            Toast.makeText(context, "Please Connect to Internet network", Toast.LENGTH_LONG).show();
        }
    }*/
}
