/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invokecommandline;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to allow programmatic connection of the Raspberry Pi to the user's local
 * network by taking the user's SSID and password as arguments and appending
 * those formatted values to the Raspberry Pi's wpa.supplicant file.
 *
 * @author Keith
 */
public class InvokeCommandLine {

    final String scriptName = "ENTER SCRIPT NAME HERE";
    final String fileName = "/etc/wpa_supplicant/wpa_supplicant.conf";
    final String script0[] = {"sudo", "chmod", "u+w", fileName};
    final String script1[] = {"sudo", "iwconfig", "wlan0", "essid", ""};
    final String script2[] = {"sudo", "wpa_supplicant", "-B", "-i", "wlan0", "-c", "/etc/wpa_supplicant/wpa_supplicant.conf"};
    final String script3[] = {"sudo", "iwconfig", "wlan0"};
    final String script4[] = {"sudo", "dhclient", "wlan0"};
    String WPA = "";

    private String SSID = "";
    private String password = "";

    /**
     * No arg constructor.
     *
     */
    public InvokeCommandLine() {
    }

    /**
     * Constructor taking both the user's SSID and password as arguments.
     *
     * @param SSID
     * @param password
     */
    public InvokeCommandLine(String SSID, String password) {
        this.SSID = SSID;
        this.password = password;
    }

    /**
     * Sets SSID to the passed argument.
     *
     * @param SSID
     */
    public void setSSID(String SSID) {
        this.SSID = SSID;
        script1[4] = this.SSID;
    }

    /**
     * Sets password to the passed argument.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    //TODO remove this, it's only added for testing until introspection works
    public String getSSID() {
        return SSID;
    }
    //TODO remove this, it's only added for testing until introspection works
    public String getPassword() {
        return password;
    }

    /**
     * Invokes any cmd or terminal command whose bin file is on the default
     * PATH.
     *
     * @param commandAndArgs the command and command line arguments
     */
    public void invoke(String[] commandAndArgs) {
        try {
            Process p = Runtime.getRuntime().exec(commandAndArgs);
            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(InvokeCommandLine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(InvokeCommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Used to write the classes SSID and password values into the Raspberry
     * Pi's wpa.supplicant file to attempt a programmatic connection to the
     * user's home network. Invokes a terminal script defined by scriptName to
     * do this.
     */
    public void connectToNetwork() throws IOException {
        if (SSID.equals("") || password.equals("")) {
            System.out.println("SSID and password need to be set.");
        } 
        else {
//            String[] connect = {scriptName, SSID, password};
//            invoke(connect);
              invoke(script0);
              WPA = "network={"
                      + "\n\tssid:\""+SSID+"\""
                      + "\n\tpsk:\""+password+"\""
                      + "\n}";
              try{
                FileWriter fw = new FileWriter(fileName, true);
                fw.write(WPA);
                fw.close();
              }
              catch(IOException io){
                  System.err.println("IOException: " + io.getMessage());
              }
              invoke(script1);
              invoke(script2);
              invoke(script3);
              invoke(script4);
              //TODO ADD APPENDING WPA_SUPPLICANT
        }
    }
}
