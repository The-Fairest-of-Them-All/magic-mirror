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
 *
 * @author Owner
 */
public class InvokeCommandLine {

    /**
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

}
