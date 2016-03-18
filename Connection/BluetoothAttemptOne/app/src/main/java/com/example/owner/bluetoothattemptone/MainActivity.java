package com.example.owner.bluetoothattemptone;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 100;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    TextView bluetoothInfo;
    ListView discoverableList;
    boolean bluetoothAvailable;
    IntentFilter filter;
    BluetoothDevice device;
    ArrayList<Parcelable> devices = new ArrayList<>();
    BluetoothSocket clientSocket = null;
    String record;

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

        discoverableList = (ListView) findViewById(R.id.discoverableList);

        // Register the BroadcastReceiver
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        bluetoothInfo = (TextView) findViewById(R.id.bluetoothInfo);
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) { //bluetooth not supported
            bluetoothInfo.setText("Bluetooth unavailable on this device. Sorry.");
        }
        if (!bluetoothAdapter.isEnabled()) {
            //presents activity to ask user to turn on bluetooth, response processed by onActivityResult
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            bluetoothInfo.setText("Bluetooth available and on. Great.");
        }

        System.out.println("Trying to connect to Lenovo - hard coded version.");
        //____________________________________________________________________________________________________
        //device = bluetoothAdapter.getRemoteDevice(MAC ADDRESS GOES HERE);
        //UUID is how the android app finds the raspberry app
        UUID uuid = UUID.fromString("a96d5795-f8c3-4b7a-9bad-1eefa9e11a94");

        try {
            clientSocket = device.createRfcommSocketToServiceRecord(uuid);
            clientSocket.connect();
            System.out.println("Connected to raspberry pi.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //responds to the user's response to the bluetooth enable prompt if it was presented
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                bluetoothInfo.setText("You enabled bluetooth. Thanks.");
            } else {
                bluetoothInfo.setText("Bluetooth disabled.");
            }
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND
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
            }
        }
    };


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

    public void startLooking(View view) {
        //if false, bluetooth off, otherwise start discovery, when results arrive the callback is BroadcastReceiver
        bluetoothAvailable =  bluetoothAdapter.startDiscovery();
    }
}
