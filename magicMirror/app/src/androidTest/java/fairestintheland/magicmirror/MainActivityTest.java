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
        Assert.assertEquals("[Event:event1, Description:]",str);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }




}
