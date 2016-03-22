/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raspberrypiserver;

import java.net.*;
import java.io.*;
import java.util.Enumeration;

/**
 *
 * @author Owner
 */
public class RaspberryPiServer {

    static final int BITMAP_SIZE = 8;
    static final int SocketServerPORT = 55555;
    
    static ServerSocket serverSocket;
    static String ipAddress;
    static String message = "";
    static Thread socketServerThread;

    public static void main(String[] args) throws IOException {

        System.out.println("Starting raspberry pi program.");
        ipAddress = getIpAddress();
        System.out.println("My IP address is: " + ipAddress);
        socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

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
                    dataInputStream = new DataInputStream(
                            socket.getInputStream());
                    dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());

                    String messageFromClient = "";

                    //If no message sent from client, this code will block the program
                    messageFromClient = dataInputStream.readUTF();

                    count++;
                    message += "#" + count + " from " + socket.getInetAddress()
                            + ":" + socket.getPort() + "\n"
                            + "Msg from client: " + messageFromClient + "\n";

                    System.out.println(message);
                    String msgReply = "Hello from Raspberry Pi (Regular computer right now), you are #" + count;
                    dataOutputStream.writeUTF(msgReply);

                    //TODO comment the rest of the try block out up to --- for code to run as before
                    //-----------------------------------------------------------------------------------
                    //request preferences count and actual preferences
                    msgReply = "Send me preferences (as a bitmap)";
                    dataOutputStream.writeUTF(msgReply);

                    //android sends a single byte (can extend if more modules added)
                    
                    //listens for preferences
                    byte bitmap;
                    bitmap = dataInputStream.readByte();

                   
                    //get number of 1's present in bitmap, this is number of modules requested
                    byte bitmapCopy;
                    int numberOfModulesRequested = 0;
                    for (int i = 0; i < BITMAP_SIZE; i++) {
                        bitmapCopy = bitmap;
                        bitmapCopy >>>= i;
                        if ((bitmapCopy & 1) == 1) {
                            numberOfModulesRequested++;
                        }
                    }
                    
                    String[] modules = new String[numberOfModulesRequested - 1];
                    
                    //TODO translate 1's from bitmap into string array - mostly for debugging

                    //get number of modules requested -this is the number of 1's present in the bitmap passed by android app 
                    int moduleCounter = 0;
                    for (int i = 0; i < BITMAP_SIZE; i++) {
                        bitmapCopy = bitmap;
                        bitmapCopy >>>= i;
                        if ((bitmapCopy & 1) == 1) {
                            switch (i) {
                                case 0:
                                    modules[moduleCounter] = "";
                                    moduleCounter++;
                                    break;
                                case 1:
                                    modules[moduleCounter] = "";
                                    moduleCounter++;
                                    break;
                                case 2:
                                    modules[moduleCounter] = "";
                                    moduleCounter++;
                                    break;
                                case 3:
                                    modules[moduleCounter] = "";
                                    moduleCounter++;
                                    break;
                                case 4:
                                    modules[moduleCounter] = "";
                                    moduleCounter++;
                                    break;
                                case 5:
                                    modules[moduleCounter] = "";
                                    moduleCounter++;
                                    break;
                                case 6:
                                    modules[moduleCounter] = "";
                                    moduleCounter++;
                                    break;
                                case 7:
                                    modules[moduleCounter] = "";
                                    moduleCounter++;
                                    break;
                                default:
                                    System.out.println("Error in determing requested modules.");
                            }
                        }
                    }

                    //use string array to start code requesting individual modules
                    for (int i = 0; i < numberOfModulesRequested; i++) {
                        switch (modules[i]) {
                            case "Twitter":
                                //TODO send request for Twitter info to android
                                break;
                            case "Weather":
                                //TODO send request for weather to ---android or weather api
                                break;
                            case "Quote":
                                //TODO send request for quote
                                break;
                        }
                    }
                    
                    //---------------------------------------------------------------------------------
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
