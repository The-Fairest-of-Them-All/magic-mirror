/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invokecommandline;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Owner
 */
public class InvokeCommandLine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //File file = new File("C:\\Users\\Owner\\Documents\\mirror\\magic-mirror\\InvokeCommandLine\\fake.txt");
            Map<String, String> env = System.getenv();
            File file = new File("fake.txt");
            //Process p = new ProcessBuilder("ipconfig").redirectOutput(Redirect.appendTo(file)).start();
            //Process p = new ProcessBuilder("ls").redirectOutput(file).start();
            ProcessBuilder proc = new ProcessBuilder("ipconfig");
            Map<String, String> env2 = proc.environment();
            proc.redirectOutput(Redirect.appendTo(file));
            proc.start();

//        try {
//            //String[] commandAndArgs = new String[10];
//            //String[] commandAndArgs = {"ls", "-la"};
//            String[] commandAndArgs = {"ipconfig", ">>", "fake.txt"};
//            //commandAndArgs[1] = "testFile.txt";
//            Process p = Runtime.getRuntime().exec(commandAndArgs);
//            p.waitFor();
//            
////            commandAndArgs[0] = "cat";
////            commandAndArgs[1] = "ipconfig";
////            commandAndArgs[2] = ">>";
////            commandAndArgs[3] = "testFile.txt";
////            p = Runtime.getRuntime().exec(commandAndArgs);
////            p.waitFor();
//            
//            
//            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        } catch (IOException ex) {
//            Logger.getLogger(InvokeCommandLine.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(InvokeCommandLine.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
