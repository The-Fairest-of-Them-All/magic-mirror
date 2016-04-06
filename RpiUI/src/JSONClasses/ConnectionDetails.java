/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSONClasses;

/**
 *
 * @author Owner
 */
public class ConnectionDetails {
    private String SSID;
    private String password;
    
    public ConnectionDetails() {};
    
    public ConnectionDetails(String s, String p) {
        SSID = s;
        password = p;
    }
    
    public String getSSID() {
        return SSID;
    }
    
    public String getPassword() {
        return password;
    }
}
