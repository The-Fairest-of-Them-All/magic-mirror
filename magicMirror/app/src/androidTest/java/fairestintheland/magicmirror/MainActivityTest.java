package fairestintheland.magicmirror;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ContextThemeWrapper;
import android.widget.Switch;

import junit.framework.Assert;

import java.util.ArrayList;


public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {
    MainActivity mainActivity;



    public MainActivityTest() {
        super(MainActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ContextThemeWrapper context = new ContextThemeWrapper(getInstrumentation().getTargetContext(), R.style.AppTheme);
        setActivityContext(context);
        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);
        mainActivity = (MainActivity)getActivity();
    }


    @SmallTest
    public void testGetCalendar() throws Exception
    { CalendarEvent calendar = new CalendarEvent();
        String str= calendar.readCalendarEvent(mainActivity).toString();
        Assert.assertEquals(true, str!=null);
    }

    @SmallTest
    public void testParse() throws Exception
    { CalendarEvent calendar = new CalendarEvent();
        calendar.readCalendarEvent(mainActivity);
        Assert.assertEquals("[Event:event1]", calendar.parseCalendarName().toString());
    }

    @SmallTest
    public void testGetSSID() throws Exception
    {
        Assert.assertEquals("Feather", mainActivity.ssidView.getText().toString());
        Assert.assertEquals("privatekey", mainActivity.passwordView.getText().toString());
    }

    @SmallTest
    public void testCheckSSID() throws Exception
    {  Thread.currentThread().join(3000);
        final WifiAccess wifi = new WifiAccess("Feather","privatekey",mainActivity);
        Thread.currentThread().join(5000);
        Assert.assertEquals(true, wifi.isConnect());
    }
    @SmallTest
    public void testSendTXT() throws Exception
    {   String str =mainActivity.getJSONMessage().toString();
        Assert.assertEquals(true, str != null);
    }


    @SmallTest
    public void testTwitterConnectionAndParseTwitter() throws Exception
    {   String consumerKey = "bUh6sDhIGpN4UdE55litSTD8W";
        String consumerSecret = "OlByFoaS9lJ8ewZEw9DOPGgVrby9EM6SepllWXrCnraw49r9DC";
        String accessToken = "4889865377-JoGReMh6w6yS2PQQ8hKcVpHlaIKRH1gM4vGf6ui";
        String accessTokenSecret = "qIvONWidLP10yKXsYyHfu1k3yzppUpxhbUc7TucF3bpp6";
        TwitterMessage tMess = tMess = new TwitterMessage(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        tMess.getTweet();
        Thread.currentThread().join(10000);
        Assert.assertEquals(true, tMess.returnTweet()!= null);
    }


    @SmallTest
    public void testswitches() throws Exception
    {  ArrayList<Switch> theSwitches = mainActivity.theSwitches;
        boolean[] SwitchState  = {true,true,true,true};
        for(int i = 0; i<4;i++){
            SwitchState[i]=theSwitches.get(i).isChecked();
        }

        Assert.assertEquals(false,   SwitchState[0]);
        Assert.assertEquals(false, SwitchState[1]);
        Assert.assertEquals(false, SwitchState[2]);
        Assert.assertEquals(false,   SwitchState[3]);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }




}

