package fairestintheland.magicmirror;

//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;

/*
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
*/

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by Raymond on 4/6/2016.
 */

public class FileIOTest
{
    @Mock
    MainActivity activity;

    @Mock
    FileOutputStream output;
    ObjectOutputStream objout;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveSwitchStates()
    {
        try {
            when(activity.openFileOutput(anyString(),anyInt())).thenReturn(output);
            activity.SaveSwitchStates();
            verify(activity, times(1)).openFileOutput(anyString(), anyInt());
            objout = new ObjectOutputStream(output);
            objout.writeObject(anyCollectionOf(boolean.class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testLoadSwitchStates()
    {}

    @Test
    public void testSaveHostName()
    {
        try {
            when(activity.openFileOutput(anyString(),anyInt())).thenReturn(output);
            activity.SaveHostName();
            verify(activity, times(1)).openFileOutput(anyString(), anyInt());
            objout = new ObjectOutputStream(output);
            objout.writeObject(anyCollectionOf(boolean.class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadHostName()
    {}

}
