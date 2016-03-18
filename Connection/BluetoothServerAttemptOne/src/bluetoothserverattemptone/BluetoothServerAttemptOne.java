/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetoothserverattemptone;

import javax.microedition.io.*;
import javax.bluetooth.*;
import javax.obex.*;
import com.intel.bluetooth.*;
import bluetoothserverattemptone.bluetoothListenerThread;

/**
 *
 * @author Owner
 */
public class BluetoothServerAttemptOne {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Thread bluetoothListenerThread = new Thread(new bluetoothListenerThread());
        bluetoothListenerThread.start();
    }    
}