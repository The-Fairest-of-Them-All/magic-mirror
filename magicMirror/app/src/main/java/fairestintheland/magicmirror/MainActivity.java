package fairestintheland.magicmirror;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**used for create a connection between mobile application and the Raspberry Pi,
    and create a UI for the Android application*/
public class MainActivity extends AppCompatActivity {
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
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<Switch> theSwitches;
    private MenuAdapter<Switch> adapter;
    private ListView navList;
    private Button sleepButton;
    private Button syncButton;
    private Button settingButton;
    private Button twitterAccountButton;
    private boolean sleeping;
    public String eventMessage;
    TwitterMessage tMess;
    private boolean[] switchStates;
    EditText ipBar;

    //connection variables
    private Socket client;
    private PrintWriter writer;
    String ipAddress;
    JSONArray parcel;
    JSONObject JsonMessage;
    Context context;
    ConnectivityManager cm;

	/**
	set up the UI and event listeners for Android Application
	*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarEvent cEvent = new CalendarEvent();
        eventMessage = cEvent.readCalendarEvent(this);
        System.out.println("!!You got an event!!! " + eventMessage);



         tMess = new TwitterMessage();
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
        //syncWithPi(); TODO commented this out for testing with different computers
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


        settingButton = (Button) findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        syncButton = (Button) findViewById(R.id.connect_button);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getJSONMessage();

                syncWithPi();
            }
        });


        twitterAccountButton = (Button)findViewById(R.id.TwitterAccountButton_main);
        View.OnClickListener oclA = new View.OnClickListener(){
            public void onClick(View v){
                Intent activityAIntent = new Intent (MainActivity.this,TwitterAccountActivity.class);
                startActivity(activityAIntent);
            }
        };
        twitterAccountButton.setOnClickListener(oclA);

    }

	/**:  send message to the Raspberry Pi*/
    private void syncWithPi() {
        if (hasValidConnection()) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        EditText ipView = (EditText) findViewById(R.id.ipAddressTextInput);
                        ipAddress = ipView.getText().toString();
                        parcel = new JSONArray();
                        for (boolean b : switchStates) {
                            {
                                parcel.put(b);
                            }
                            client = new Socket(ipAddress, 55555);
                            System.out.println("Connected to raspberry pi.");
                            writer = new PrintWriter(client.getOutputStream(), true);
                            writer.write(parcel.toString());
                            writer.flush();
                            writer.close();
                            client.close();

                        }
                    } catch (IOException e) {
                       e.printStackTrace();
                    }
                }
            };
            t.start();

        } else {
            Toast.makeText(context, "Please Connect to Internet network", Toast.LENGTH_LONG).show();
        }
    }

	/** set  Raspberry Pi screen sleep*/
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();

        } else {
            Toast.makeText(context, "Please Connect to Internet network", Toast.LENGTH_LONG).show();
        }
    }

	/**check if the Android OS has network connection*/
    private boolean hasValidConnection() {
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }
        return false;
    }

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






    public JSONObject getJSONMessage()  {
        JSONObject newMessage = new JSONObject();
        try{
        for(int i =0;i <switchStates.length;i++){
            newMessage.put(String.valueOf(theSwitches.get(i).getText()),switchStates[i]);
        }
        newMessage.put("events",eventMessage);
        newMessage.put("tweets",tMess.getTweets());

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
}
