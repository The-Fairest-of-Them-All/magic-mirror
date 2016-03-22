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
import bluetooth.bluetoothListenerThread;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    static final int BITMAP_SIZE = 8;
    static final int SocketServerPORT = 60_000;

    static ServerSocket serverSocket;
    static String ipAddress;
    static String message = "";
    static Thread socketServerThread;

    private static JTextArea quoteArea;
    private static JTextArea twitArea;
    private static JTextArea calArea;
    private static JTextArea weatArea;
    private static JTextArea timeArea;
    static GridBagConstraints twitc;
    static JPanel p;
    static RpiUI uiThread;
    static Runnable mainUiThread;
    static LocalTime time;
    static String currentTime;

    /**
     * @param args the command line arguments
     */
    static String sample = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
            + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
            + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi"
            + " ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit"
            + " in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur "
            + "sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit "
            + "anim id est laborum.";
    static String quote = "I'm ready - Spongebob Squarepants";

    /**
     * set up the java Frame for mirror screen
     */
    private void framer() {
        JFrame frame = new JFrame("Raspberry Pi App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setPreferredSize(screenSize);
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice gd = ge.getDefaultScreenDevice();
//        gd.setFullScreenWindow(frame);

        p = new JPanel(new GridBagLayout());
        p.setBackground(Color.black);
        p.setOpaque(true);
        p.setPreferredSize(screenSize);

        twitArea = new JTextArea(
                sample + sample, 6, 20);
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
                sample, 6, 20);
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
                sample, 6, 20);
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
                quote, 6, 20);
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

        timeArea = new JTextArea(currentTime, 6, 20);
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

        frame.setVisible(true);

    }

    public void appendToJTextArea(JTextArea area, String newText) {
        area.append(newText);
    }

    public void appendToJTextAreaNewline(JTextArea area, String newText) {
        area.append('\n' + newText);
    }

    public void insertIntoBeginningJTextArea(JTextArea area, String newText) {
        area.insert(newText, 0);
    }

    public void replaceJTextArea(JTextArea area, String newText) {
        String formerText = area.getText();
        area.replaceRange(newText, 0, formerText.length());
    }

    public JTextArea getTwitterJTextArea() {
        return twitArea;
    }

    public JTextArea getQuoteJTextArea() {
        return quoteArea;
    }

    public static void main(String[] args) throws IOException {
        time = LocalTime.now();
        currentTime = time.format(DateTimeFormatter.ofPattern("hh:mm a"));
        
        //reference to the UI thread so that if bluetooth info is received, it can be added from uiThread
        uiThread = new RpiUI();

        mainUiThread = new Runnable() {
            public void run() {
                uiThread.framer();
            }
        };

        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                uiThread.framer();
            }
        })*/
        javax.swing.SwingUtilities.invokeLater(mainUiThread);

        System.out.println("Starting raspberry pi program.");

        /*ipAddress = getIpAddress();
        System.out.println("My IP address is: " + ipAddress);
        socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();*/
        Thread bluetoothListenerThread = null;
        try {
            bluetoothListenerThread = new Thread(new bluetoothListenerThread(uiThread));
            bluetoothListenerThread.start();
            bluetoothListenerThread.join();
        } catch (Exception e) {
            uiThread.replaceJTextArea(twitArea, ("Threw bluetooth exception."));
            e.printStackTrace();
        }
    }

    /**
     * used for create a connection as a sever
     */
    private static class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                serverSocket = new ServerSocket(SocketServerPORT);

                System.out.println("I'm waiting here: " + serverSocket.getLocalPort());
                while (true) {
                    socket = serverSocket.accept();
                    System.out.println("Connected to android.");
                    dataInputStream = new DataInputStream(
                            socket.getInputStream());
                    dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());

                    String messageFromClient;
                    BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    //If no message sent from client, this code will block the program
                    messageFromClient = buff.readLine();
                    System.out.println("Android sent me this: " + messageFromClient);
                    System.out.print("\n");
                    /*messageFromClient = dataInputStream.readUTF();
                                      
                    count++;
                    message += "#" + count + " from " + socket.getInetAddress()
                            + ":" + socket.getPort() + "\n"
                            + "Msg from client: " + messageFromClient + "\n";

                    System.out.println(message);
                    String msgReply = "Hello from Raspberry Pi (Regular computer right now), you are #" + count;
                    dataOutputStream.writeUTF(msgReply);*/
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                final String errMsg = e.toString();

                System.out.println(errMsg);

            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * get IP address of the current device
     */
    private static String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

}
