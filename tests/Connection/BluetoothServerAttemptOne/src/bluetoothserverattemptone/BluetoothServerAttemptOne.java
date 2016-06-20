/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetoothserverattemptone;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Owner
 */
public class BluetoothServerAttemptOne {

    private static InetAddress addr;
    static String hostname;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Thread bluetoothListenerThread = new Thread(new bluetoothListenerThread());
        bluetoothListenerThread.start();

        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(BluetoothServerAttemptOne.class.getName()).log(Level.SEVERE, null, ex);
        }
        hostname = addr.getHostName();
        System.out.println("My IP address is " + addr);
        System.out.println("My hostname is " + hostname);
    }
}
