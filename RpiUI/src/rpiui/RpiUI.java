/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpiui;

import javax.swing.*;
import java.awt.*;
import java.awt.GraphicsDevice.*;
import java.net.*;
import java.io.*;
import java.util.Enumeration;
import bluetooth.BluetoothListenerThread;
import invokecommandline.InvokeCommandLine;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import com.intel.bluetooth.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KatieHorn create the UI for Raspberry PI and display the information
 * on the screen.
 */
public class RpiUI extends JFrame {

    /**
     * int SocketServerPORT :	the port number of the sever String message:
     * received messages which is from client ServerSocket serverSocket: used
     * for create a connection as a sever String ipAddress:	the IP address of
     * the Raspberry Pi Thread socketServerThread: Thread which used for handle
     * the network action
     */
    
    //THESE VARS UNUSED CURRENTLY
    /*static final int BITMAP_SIZE = 8;
    static final int SocketServerPORT = 60_000;

    static ServerSocket serverSocket;
    static String ipAddress;
    static String message = "";
    static Thread socketServerThread;*/

    private static JTextArea quoteArea;
    private static JTextArea twitArea;
    private static JTextArea calArea;
    private static JTextArea weatArea;
    private static JTextArea timeArea;
    static GridBagConstraints twitc;
    static JPanel p;
    static RpiUI uiThread;
    static Runnable mainUiThread;
    static Thread bluetoothListenerThread;
    static ActionListener updateClockDisplay;
    static Timer t;
    boolean display = true; //determines whether or not the JTextAreas should be displayed
    JFrame frame;

    static String sample = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
            + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
            + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi"
            + " ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit"
            + " in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur "
            + "sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit "
            + "anim id est laborum.";
    static String quote = "I'm ready!\n-Spongebob Squarepants";

    /**
     * No arg constructor.
     */
    public RpiUI() {}
    
    /**
     * Sets up the java Frame for the mirror screen. Adds placeholder text to each area. Divides the screen into
     * a grid system to specify where each designated area is located. Sets design specifications for the layout.
     */
    public void framer() {
        //private void framer() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        frame = new JFrame("Raspberry Pi App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setPreferredSize(screenSize);
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice gd = ge.getDefaultScreenDevice();
//        gd.setFullScreenWindow(frame);

        p = new JPanel(new GridBagLayout());
        p.setBackground(Color.black);
        p.setOpaque(true);
        p.setPreferredSize(screenSize);

        twitArea = new JTextArea(
                /*sample + sample*/"", 6, 20);
        twitArea.setFont(new Font("Roman", Font.BOLD, 20));
        twitArea.setLineWrap(true);
        twitArea.setWrapStyleWord(true);
        twitArea.setOpaque(false);
        twitArea.setEditable(false);
        twitArea.setForeground(Color.white);
        twitc = new GridBagConstraints();
        twitc.gridy = 0;
        twitc.gridx = 2;
        twitc.gridheight = 3;

        twitc.fill = GridBagConstraints.VERTICAL;
        twitc.anchor = GridBagConstraints.FIRST_LINE_END;

        JLabel blank = new JLabel();
        //     blank.setForeground(Color.red);
        JLabel blank2 = new JLabel();
        //   blank2.setForeground(Color.red);
        JLabel blank3 = new JLabel();
        //    blank3.setForeground(Color.red);
        JLabel blank4 = new JLabel();
        //  blank4.setForeground(Color.red);
        JLabel blank5 = new JLabel();
        //  blank5.setForeground(Color.red);
        JLabel blank6 = new JLabel();
        //      blank6.setForeground(Color.red);

        GridBagConstraints topmiddlec = new GridBagConstraints();
        topmiddlec.gridy = 0;
        topmiddlec.gridx = 1;
        topmiddlec.weightx = 60;
        topmiddlec.ipadx = 45;

        calArea = new JTextArea(
                /*sample*/"", 6, 20);
        calArea.setFont(new Font("Roman", Font.BOLD, 20));
        calArea.setLineWrap(true);
        calArea.setWrapStyleWord(true);
        calArea.setOpaque(false);
        calArea.setEditable(false);
        calArea.setForeground(Color.white);
        GridBagConstraints calc = new GridBagConstraints();
        calc.gridy = 0;
        calc.gridx = 0;
        calc.weightx = 30;
        calc.anchor = GridBagConstraints.FIRST_LINE_START;

        GridBagConstraints row2left = new GridBagConstraints();
        row2left.gridy = 1;
        row2left.gridx = 0;
        row2left.weightx = 60;
        row2left.weighty = 30;

        GridBagConstraints row2mid = new GridBagConstraints();
        row2mid.gridy = 1;
        row2mid.gridx = 1;
        row2mid.weightx = 60;
        row2mid.weighty = 30;

        weatArea = new JTextArea(
                /*sample*/"", 6, 20);
        weatArea.setFont(new Font("Roman", Font.BOLD, 20));
        weatArea.setLineWrap(true);
        weatArea.setWrapStyleWord(true);
        weatArea.setOpaque(false);
        weatArea.setEditable(false);
        weatArea.setForeground(Color.white);
        GridBagConstraints weatc = new GridBagConstraints();
        weatc.gridy = 2;
        weatc.gridx = 0;
        weatc.gridheight = 2;
        weatc.fill = GridBagConstraints.VERTICAL;
        weatc.anchor = GridBagConstraints.FIRST_LINE_START;

        GridBagConstraints row3mid = new GridBagConstraints();
        row3mid.gridx = 1;
        row3mid.gridy = 2;

        GridBagConstraints row3r = new GridBagConstraints();
        row3r.gridx = 2;
        row3r.gridy = 2;

        quoteArea = new JTextArea(
                /*quote*/"", 6, 20);
        quoteArea.setFont(new Font("Roman", Font.BOLD, 20));
        quoteArea.setLineWrap(true);
        quoteArea.setWrapStyleWord(true);
        quoteArea.setOpaque(false);
        quoteArea.setEditable(false);
        quoteArea.setForeground(Color.white);
        GridBagConstraints quotec = new GridBagConstraints();
        quotec.gridx = 1;
        quotec.gridy = 3;
        quotec.anchor = GridBagConstraints.PAGE_END;

        timeArea = new JTextArea("", 6, 20);
        timeArea.setFont(new Font("Roman", Font.BOLD, 30));
        timeArea.setLineWrap(false);
        timeArea.setWrapStyleWord(true);
        timeArea.setOpaque(false);
        timeArea.setEditable(false);
        timeArea.setForeground(Color.white);
        GridBagConstraints quotet = new GridBagConstraints();
        quotet.gridx = 1;
        quotet.gridy = 1;
        quotet.anchor = GridBagConstraints.PAGE_START;

        GridBagConstraints lastblank = new GridBagConstraints();
        lastblank.gridx = 2;
        lastblank.gridy = 3;

        p.add(twitArea, twitc);
        //p.add(blank, topmiddlec);
        p.add(timeArea, topmiddlec);
        p.add(calArea, calc);
        p.add(blank2, row2left);
        p.add(blank3, row2mid);
        p.add(weatArea, weatc);
        p.add(blank4, row3mid);
        p.add(blank5, row3r);
        p.add(quoteArea, quotec);
        frame.add(p);

        frame.pack();

        frame.setVisible(display);
    }

    /**
     * Appends the String newText to the existing text in the JTextArea area.
     * 
     * @param area JTextArea corresponding to the area into which you want to write
     * @param newText a string to write into area
     */
    public void appendToJTextArea(JTextArea area, String newText) {
        try {
            area.append(newText);
        } catch (NullPointerException e) {
            System.out.println("The area was null.");
            e.printStackTrace();
        }
    }

    /**
     * Appends the String newText to the existing text in the JTextArea area on a newline.
     * 
     * @param area JTextArea corresponding to the area into which you want to write
     * @param newText a string to write into area
     */
    public void appendToJTextAreaNewline(JTextArea area, String newText) {
        try {
            area.append('\n' + newText);
        } catch (NullPointerException e) {
            System.out.println("The area was null.");
            e.printStackTrace();
        }
    }

    /**
     * Inserts the String newText to the beginning of the existing text in the JTextArea area.
     * 
     * @param area JTextArea corresponding to the area into which you want to write
     * @param newText a string to write into area
     */
    public void insertIntoBeginningJTextArea(JTextArea area, String newText) {
        try {
            area.insert(newText, 0);
        } catch (NullPointerException e) {
            System.out.println("The area was null.");
            e.printStackTrace();
        }
    }

    /**
     * Replaces the existing text in the JTextArea are with the String newText.
     * 
     * @param area JTextArea corresponding to the area into which you want to write
     * @param newText a string to write into area
     */
    public void replaceJTextArea(JTextArea area, String newText) {
        try {
            String formerText = area.getText();
            area.replaceRange(newText, 0, formerText.length());
        } catch (NullPointerException e) {
            System.out.println("The area was null.");
            e.printStackTrace();
        }
    }

    /**
     * Returns the JTextArea for Twitter.
     * 
     * @return the JTextAre for Twitter 
     */
    public JTextArea getTwitterJTextArea() {
        return twitArea;
    }

    /**
     * Returns the JTextArea for Quotes.
     * 
     * @return the JTextAre for Quotes 
     */
    public JTextArea getQuoteJTextArea() {
        return quoteArea;
    }

    /**
     * Returns the JTextArea for the Calendar.
     * 
     * @return the JTextAre for the Calendar
     */
    public JTextArea getCalendarJTextArea() {
        return calArea;
    }

    /**
     * Returns the JTextArea for Weather.
     * 
     * @return the JTextAre for Weather
     */
    public JTextArea getWeatherJTextArea() {
        return weatArea;
    }

    /**
     * Returns the JTextArea for Time.
     * 
     * @return the JTextAre for Time 
     */
    public JTextArea getTimeJTextArea() {
        return timeArea;
    }

    /**
     * This is the main function for the Raspberry Pi side of the MagicMirror project.
     * 
     * @param args command line arguments
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Starting raspberry pi program.");

        //reference to the UI thread so that if bluetooth info is received, it can be added from uiThread
        uiThread = new RpiUI();

        //declare a Runnable mainUiThread that starts the framer method of RpiUi
        mainUiThread = new Runnable() {
            public void run() {
                uiThread.framer();
            }
        };
        //javax.swing.SwingUtilities.invokeLater(mainUiThread);
        mainUiThread.run();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RpiUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        InvokeCommandLine test = new InvokeCommandLine();
        //String[] commandAndArgs = {"ls", "-la"};
        String[] commandAndArgs = {"echo", "HEY"};
        test.invoke(commandAndArgs);

        bluetoothListenerThread = null;
        try {
            bluetoothListenerThread = new Thread(new BluetoothListenerThread(uiThread));
            bluetoothListenerThread.start();
        } catch (Exception e) {
            uiThread.replaceJTextArea(twitArea, ("Threw bluetooth exception."));
            e.printStackTrace();
        }

        //declare an actionlistener which updates the time
        updateClockDisplay = new ActionListener() {
            LocalTime time;
            String currentTime;

            //overridden to update the time shown in the display every second
            @Override
            public void actionPerformed(ActionEvent e) {
                //long timeLong = System.currentTimeMillis();
                time = LocalTime.now();
                currentTime = time.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                uiThread.replaceJTextArea(timeArea, currentTime);
            }
        };
        //schedule the Timer to execute the updateClockDisplay every second and start the timer
        t = new Timer(1000, updateClockDisplay);
        t.start();

        //join the bluetoothListenerThread thread, stop the timer
        try {
            Thread.State state;
            if (bluetoothListenerThread.getState() == Thread.State.RUNNABLE) {
                bluetoothListenerThread.join();
            }
            System.out.println("Bluetooth thread joined.");
            if (t.isRunning()) {
                t.stop();
            }
            System.out.println("Stopped the clock.");
            
            //TODO need to have a way to join the UI thread
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Flips the display boolean and changes the visibility of all JTextAreas which display text to
     * reflect the new value. Called by BluetoothListenerThread when it receives the SLEEP_KEYWORD
     * from the Android app.
     */
    public void toggleDisplay() {
        display = !display;
        twitArea.setVisible(display);
        calArea.setVisible(display);
        timeArea.setVisible(display);
        weatArea.setVisible(display);
        quoteArea.setVisible(display);
    }
}