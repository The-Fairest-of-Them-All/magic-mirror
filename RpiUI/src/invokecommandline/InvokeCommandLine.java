/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invokecommandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to allow programmatic connection of the Raspberry Pi to the user's local network by taking the user's SSID and
 * password as arguments and appending those formatted values to the Raspberry Pi's wpa.supplicant file.
 *
 * @author Keith
 */
public class InvokeCommandLine {
    
    final String scriptName = "ENTER SCRIPT NAME HERE";
    
    private String SSID;
    private String password;
    
    /**
     * No arg constructor.
     * 
     */
    public void InvokeCommandLine() {}
    
    /**
     * Constructor taking both the user's SSID and password as arguments.
     * 
     * @param SSID
     * @param password 
     */
    public void InvokeCommandLine(String SSID, String password) {
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
    }
    
    /**
     * Sets password to the passed argument.
     * 
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSSID() {
        return SSID;
    }
    public String getPassword() {
        return password;
    }

    /**
     * Invokes any cmd or terminal command whose bin file is on the default PATH.
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
     * Used to write the classes SSID and password values into the Raspberry Pi's wpa.supplicant file to attempt a
     * programmatic connection to the user's home network. Invokes a terminal script defined by scriptName
     * to do this.
     */
    public void connectToNetwork() {
        if (SSID.equals("") || password.equals("")) {
            System.out.println("SSID and password need to be set.");
            return;
        } else {
            String[] connect = {scriptName, SSID, password};
            invoke(connect);
        }
    }
}
