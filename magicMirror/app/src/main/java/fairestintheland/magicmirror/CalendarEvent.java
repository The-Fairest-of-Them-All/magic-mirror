package fairestintheland.magicmirror;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


        import android.content.Context;
        import android.database.Cursor;
        import android.net.Uri;

        import java.sql.SQLOutput;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;

/**
 * used for fetching the google Calendar events
 */
public class CalendarEvent {
	
		/**	
	ArrayList<String> currentEvent: 	collection of event which is from google calendar
	ArrayList<String> eventID: 		store all event ID
	ArrayList<String> nameOfEvent :	 store the name of the events
	ArrayList<String> startDates:	 store the start date of the events
	ArrayList<String> endDates:		 store the end date of the event
	ArrayList<String> descriptions:	 store the description of the event
	*/
	
    public static ArrayList<String> currentEvent = new ArrayList<String>();
    public static ArrayList<String> eventID = new ArrayList<String>();
    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();

	
	/**access into the google calendar and get all events  */
    public static ArrayList<String> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver().query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "title", "description", "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();

        for (int i = 0; i < CNames.length; i++) {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd/MM/yyyy hh:mm:ss a");
            Date date = new Date();
            String[] strSpit =formatter.format(date.getTime()).split(" ");
            String str =getDate(Long.parseLong(cursor.getString(2)));
            if(str.indexOf(strSpit[0])!=-1) {
                currentEvent.add("Event:"+cursor.getString(0));
                // currentEvent.add(str);
                // currentEvent.add(getDate(Long.parseLong(cursor.getString(3))));
                currentEvent.add(" Description:"+cursor.getString(1)+"; ");

               // System.out.println("you got a event!!!!!!!!!!!!!! "+currentEvent.toString());
                //      nameOfEvent.add(cursor.getString(0));
                //     startDates.add(str);
                //      endDates.add(getDate(Long.parseLong(cursor.getString(3))));
                //     descriptions.add(cursor.getString(1));
            }
            CNames[i] = cursor.getString(0);
            cursor.moveToNext();

        }
        return currentEvent;
    }

	/**change the date format from milliseconds to "dd/MM/yyyy  hh:mm:ss " */
    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}
