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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 100;
    //private final String RASPBERRY_PI_NAME = "Lenovo-PC";
    private String raspberryPiName;
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
            super.onPostExecute(result);
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND, this is called every time a new device is found
    //during device discovery, if a name matching the string defined by RASPBERRY_PI_NAME is found
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
                } else if (device.getName().equals(raspberryPiName.toUpperCase())) {
                    tryToConnect();
                    return;
                }
            }
        }
    };

    //checks to see if bluetooth is available and enabled on the phone, if it is not enabled,
    //user will be presented with an activity to turn on bluetooth ,the response will be handled
    //by onActivityResult
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

    //attempt a bluetooth connection to a device whose name is known from the device discovery process
    private void tryToConnect() {
        BluetoothDevice btDevice = device;
        System.out.println("Trying to connect to " + raspberryPiName);

        btDevice = bluetoothAdapter.getRemoteDevice(btDevice.getAddress());

        try {
            clientSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
            clientSocket.connect();
            System.out.println("Connected to raspberry pi.");

            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes to send to write()
            buffer = raspberryPiName.getBytes();
            clientSocketInputStream = clientSocket.getInputStream();
            clientSocketOutputStream = clientSocket.getOutputStream();
            clientSocketOutputStream.write(buffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //responds to button press, starts discovery of remote bluetooth devices that are set to discoverable
    public void startLooking(View view) {
        raspberryPiName = raspberryNameEditText.getText().toString().trim();

        //only start discovery if user has entered a remote hostname
        if (!raspberryPiName.equals("Enter raspberry computer name here")) {
            //if false, bluetooth off, otherwise start discovery, when results arrive the callback is BroadcastReceiver
            bluetoothAvailable = bluetoothAdapter.startDiscovery();
        } else


        {
            raspberryNameEditText.setText("Please reenter raspberry pi bluetooth name");
        }
    }

    //respond to a user's interacting with an activity presented to turn on bluetooth capabilities
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
