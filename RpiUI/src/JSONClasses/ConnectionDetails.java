/*
 * A class to allow the use of GSON to collect the SSID and password went by the Android device.
 */
package JSONClasses;

/**
 * A class to allow the use of GSON to collect the SSID and password went by the Android device.
 * @author Owner
 */
public class ConnectionDetails {
    private String SSID;
    private String password;
    
    /**
     * No arg constructor.
     */
    public ConnectionDetails() {};
    
    /**
     * Constructor which sets SSID and password. Used by GSON in the fromJson method.
     * @param s the SSID
     * @param p the password
     */
    public ConnectionDetails(String s, String p) {
        SSID = s;
        password = p;
    }
    
    /**
     * Used to retrieve the network SSID.
     * @return the network SSID
     */
    public String getSSID() {
        return SSID;
    }
    
    /**
     * Used to retrieve the password.
     * @return the network password
     */
    public String getPassword() {
        return password;
    }
}
