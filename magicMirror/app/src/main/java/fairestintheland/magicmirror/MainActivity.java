package fairestintheland.magicmirror;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarEvent cEvent = new CalendarEvent();
        for (int i = 0; i < 10; i++) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        System.out.println("!!You got an event!!!" + cEvent.readCalendarEvent(this));

        TwitterMessage tMess = new TwitterMessage();
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
        syncButton = (Button) findViewById(R.id.connect_button);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncWithPi();
            }
        });

    }

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
}
