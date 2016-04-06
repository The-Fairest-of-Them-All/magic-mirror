package fairestintheland.magicmirror;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ContextThemeWrapper;

import junit.framework.Assert;


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
        Assert.assertEquals("[Event:event1, Description:]", str);
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
    { final WifiAccess wifi = new WifiAccess("Feather","privatekey",mainActivity);
        Thread.currentThread().join(3000);
        Assert.assertEquals(true, wifi.isConnect());
    }
    @SmallTest
    public void testSendTXT() throws Exception
    {   String str =mainActivity.getJSONMessage().toString();
       Assert.assertEquals(true, str != null);
    }

    @SmallTest
      public void testTwitterConnection() throws Exception
    { CalendarEvent calendar = new CalendarEvent();
        String str= calendar.readCalendarEvent(mainActivity).toString();
        Assert.assertEquals("[Event:event1, Description:]",str);
        Assert.assertEquals("[Event:event1]",calendar.parseCalendarName().toString());
    }

    @SmallTest
      public void testParseTwitter() throws Exception
    { String consumerKey = "bUh6sDhIGpN4UdE55litSTD8W";
        String  consumerSecret = "OlByFoaS9lJ8ewZEw9DOPGgVrby9EM6SepllWXrCnraw49r9DC";
        String  accessToken = "4889865377-JoGReMh6w6yS2PQQ8hKcVpHlaIKRH1gM4vGf6ui";
        String accessTokenSecret = "qIvONWidLP10yKXsYyHfu1k3yzppUpxhbUc7TucF3bpp6";

        TwitterMessage tMess = new TwitterMessage( consumerKey, consumerSecret, accessToken, accessTokenSecret);
        tMess.getTweet();
        Thread.currentThread().join(1000);
        Assert.assertEquals(true, tMess.returnTweet()!= null);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }




}
