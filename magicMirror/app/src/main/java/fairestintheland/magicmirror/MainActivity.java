package fairestintheland.magicmirror;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    //UI objects
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<Switch> theSwitches;
    private MenuAdapter<Switch> adapter;
    private ListView navList;
    private Button sleepButton;
    private Button syncButton;
    private boolean sleeping;
    private boolean[] switchStates;
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
    private String raspberryPiName;
    //string defined on android and raspberry sides to establish connection
    private final String UUIDSTRING = "a96d5795-f8c3-4b7a-9bad-1eefa9e11a94";
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

    TwitterMessage tMess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarEvent cEvent = new CalendarEvent();
        for (int i = 0; i < 10; i++) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        System.out.println("!!You got an event!!!" + cEvent.readCalendarEvent(this));

        tMess = new TwitterMessage();
        tMess.getTweet();
        switchStates = new boolean[]{false, false, false, false}; //Twitter,Email,Weather,Calendar
        context = this;
        sleeping = false;
        //ipAddress = "10.0.0.58";
        //ipAddress = "192.168.1.152";
        theSwitches = new ArrayList<Switch>();
        initSwitches();
        navList = (ListView) findViewById(R.id.left_drawer);
        adapter = new MenuAdapter<>(this, android.R.layout.simple_list_item_1, theSwitches);
        navList.setAdapter(adapter);
        //syncWithPi(); //TODO commented this out for testing with different computers
        sleepButton = (Button) findViewById(R.id.sleepButton);
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleeping = !sleeping;
                setSleepMode();
                if (sleeping) {
                    sleepButton.setText(R.string.wake_button);
                } else {
                    sleepButton.setText(R.string.sleep_button);
                }
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
        syncButton = (Button) findViewById(R.id.connect_button);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //syncWithPi();
                //callSync();
                new MyClientTask().execute();
            }
        });

        //start bluetooth services
        bluetoothInfo = (TextView) findViewById(R.id.bluetoothInfo);
        raspberryNameEditText = (EditText) findViewById(R.id.raspberryName);

        new BluetoothAsync().execute();

    }

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
                if (device.getName().equals(raspberryPiName)) {
                    tryToConnect();
                    return;
                }
            }
        }
    };

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
            bluetoothInfo.setText("Bluetooth unavailable on this device. Sorry.");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                //presents activity to ask user to turn on bluetooth, response processed by onActivityResult()
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                bluetoothInfo.setText("Bluetooth available and on. Great.");
            }

            //UUID is how the android app finds the raspberry app, this is also defined on the raspberry side
            uuid = UUID.fromString(UUIDSTRING);
        }
    }

    /**
     * Attempt a bluetooth connection to a device whose name is known from the device discovery process.
     * The device hostname is input by the user and this method is only called if that user input String
     * matches a device hostname found in the discovery process. This method establishes a BluetoothSocket
     * using createRfcommSocketToServiceRecord() and tries to connect to a listening BluetoothServerSocket
     * that uses the same UUID.
     */
    private void tryToConnect() {
        BluetoothDevice btDevice = device;
        System.out.println("Trying to connect to " + raspberryPiName);

        btDevice = bluetoothAdapter.getRemoteDevice(btDevice.getAddress());

        try {
            clientSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
            clientSocket.connect();
            System.out.println("Connected to raspberry pi.");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //write content to the socket if the socket was opened successfully
        writeContentToSocket(clientSocket);
    }

    /**
     *
     * @param clientSocket an open BluetoothSocket object
     */
    public void writeContentToSocket(BluetoothSocket clientSocket) {


        try {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            buffer = raspberryPiName.getBytes();
            clientSocketInputStream = clientSocket.getInputStream();
            clientSocketOutputStream = clientSocket.getOutputStream();
            //clientSocketOutputStream.write(buffer);
            //System.out.println("Wrote " + new String(buffer) + " to raspberry.");

            //send Twitter if true
            if (switchStates[0]) {
                String tweet = tMess.returnTweet();
                buffer = new byte[1024];  // buffer store for the stream
                buffer = tweet.getBytes();
                clientSocketOutputStream.write(buffer);
                System.out.println("Wrote " + new String(buffer) + " to raspberry.");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Responds to sync button press, starts discovery of remote bluetooth devices that are set to
     * discoverable. If the default string was not changed, the user is prompted to input a valid
     * hostname.
     *
     * @param view
     */
    public void startLooking(View view) {
        raspberryPiName = raspberryNameEditText.getText().toString().trim();

        //only start discovery if user has entered a remote hostname
        if (!raspberryPiName.equals("Enter raspberry computer name here")) {
            if (!raspberryPiName.equals("Please reenter raspberry pi bluetooth name")) {
                if (!raspberryPiName.isEmpty()) {
                    //if false, bluetooth off, otherwise start discovery, when results arrive the callback is BroadcastReceiver
                    bluetoothAvailable = bluetoothAdapter.startDiscovery();
                } else {
                    raspberryNameEditText.setText("Please reenter raspberry pi bluetooth name");
                }
            } else {
                raspberryNameEditText.setText("Please reenter raspberry pi bluetooth name");
            }
        } else {
            raspberryNameEditText.setText("Please reenter raspberry pi bluetooth name");
        }
    }

    /**
     * Respond to a user's interacting with an activity presented to turn on bluetooth capabilities.
     *
     * @param requestCode Defined in the calling code, allows response to multiple events,
     *                    REQUEST_ENABLE_BT represents a bluetooth activity request
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
    }

    /**
     * Overridden to unregister the BroadcastReceiver.
     */
    @Override
    protected void onDestroy() {
        //have to unregister BroadcastReceiver before the app closes
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
    //-----------------END BLUETOOTH SECTION-----------------------------------------------------------------



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

                response = dataInputStream.readUTF();*/

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
    }

    private void setSleepMode() {
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
    }

    private boolean hasValidConnection() {
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }
        return false;
    }

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


    class MenuAdapter<T> extends ArrayAdapter<Switch> {
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

            // Now we can fill the layout with the right values
            text.setText(list.get(position).getText());

            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switchStates[position] = isChecked;
                    Log.v("Switch State = ", list.get(position).getText() + " " + switchStates[position]);
                }
            });

            return v;
        }
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
}
