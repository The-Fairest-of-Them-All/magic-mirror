/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magic.mirror;

/**
 *
 * @author chris
 */
public class ScriptBuilder {
    private String SSID = "";
    private String PASSWORD = "";
    private String FileName = "";
        
    public ScriptBuilder(){
        
    }
    
    /**
     * 
     * @param SSID: The Name of the wifi
     * @param PASSWORD: Password for wifi
     */
    public ScriptBuilder(String SSID, String PASSWORD){
        this.SSID = SSID;
        this.PASSWORD = PASSWORD;
        
        init();
        run();
    }
    private void init(){
        //TODO Create the Script
    }
    
    private void run(){
        //TODO Run the Script
    }
}
