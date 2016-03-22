package com.example.owner.bluetoothattemptone;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.*;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
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


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //discoverableList = (ListView) findViewById(R.id.discoverableList);
        bluetoothInfo = (TextView) findViewById(R.id.bluetoothInfo);
        raspberryNameEditText = (EditText) findViewById(R.id.raspberryName);

        new BluetoothAsync().execute();
    }

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
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes to send to write()

        try {
            buffer = raspberryPiName.getBytes();
            clientSocketInputStream = clientSocket.getInputStream();
            clientSocketOutputStream = clientSocket.getOutputStream();
            clientSocketOutputStream.write(buffer);
            System.out.println("Wrote " + new String(buffer) + " to raspberry.");
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
}
