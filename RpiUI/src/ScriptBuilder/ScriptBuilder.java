/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptBuilder;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chris
 */
public class ScriptBuilder {
    final String scriptName = "ENTER SCRIPT NAME HERE";
    final String fileName = "/etc/wpa_supplicant/wpa_supplicant.conf";
    final String script0[] = {"sudo", "chmod", "u+w", fileName};
    final String script1[] = {"sudo", "iwconfig", "wlan0", "essid", ""};
    final String script2[] = {"sudo", "wpa_supplicant", "-B", "-i", "wlan0", "-c", "/etc/wpa_supplicant/wpa_supplicant.conf"};
    final String script3[] = {"sudo", "iwconfig", "wlan0"};
    final String script4[] = {"sudo", "dhclient", "wlan0"};
    String WPA = "";
    
    //private String SSID = "BigBrother2.4GHz";
    //private String password = "jerry_is_a_manlet";
    private String SSID = "Katherine's iPhone";
    private String password = "fairest0";
        
    public ScriptBuilder(){
        run();
    }
    
    /**
     * 
     * @param SSID: The Name of the wifi
     * @param PASSWORD: Password for wifi
     */
    public ScriptBuilder(String SSID, String PASSWORD){
        this.SSID = SSID;
        this.password = PASSWORD;
        
        init();
        run();
    }
    private void init(){
        //TODO Create the Script
    }
    
    private void run(){
        if (SSID.equals("") || password.equals("")) {
            System.out.println("SSID and password need to be set.");
        } 
        else {
//            String[] connect = {scriptName, SSID, password};
//            invoke(connect);
              invoke(script0);
              WPA = "\nnetwork={"
                      + "\n\tssid=\""+SSID+"\""
                      + "\n\tpsk=\""+password+"\""
                      + "\n\tkey_mgmt=WPA-PSK" //Keith added this line to test
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
            Logger.getLogger(ScriptBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ScriptBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
