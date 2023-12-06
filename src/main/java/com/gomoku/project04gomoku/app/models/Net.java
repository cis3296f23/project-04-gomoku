package com.gomoku.project04gomoku.app.models;

import com.gomoku.project04gomoku.mvc.ViewModel.LocalWLANMultiplayerController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
/**
 * The Net class represents a network communication utility for Gomoku game.
 * It provides functionalities to act as a client or server in a local WLAN multiplayer setting.
 */
public class Net {
    /**
     * The socket used for communication.
     */
    private Socket socket;
    /**
     * Input stream for reading data from the socket.
     */
    private InputStream is;
    /**
     * Output stream for writing data to the socket.
     */
    private OutputStream os;
    /**
     * Buffered reader for reading characters from the input stream.
     */
    private BufferedReader br;
    /**
     * PrintWriter for writing formatted text to a file or socket.
     */
    private PrintWriter pw;
    /**
     * Callback interface for network state change events.
     */
    private NetStateChange nsc;
    /**
     * Server socket for listening to incoming connections.
     */
    private ServerSocket serverSocket;
    /**
     * The port number for communication.
     */
    private static final int PORT = 4040;
    /**
     * Singleton instance for the client.
     */
    private static Net client;
    /**
     * Singleton instance for the server.
     */
    private static Net server;
    /**
     * Private constructor to prevent instantiation.
     */
    private Net() {
    }
    /**
     * Initializes the network connection.
     *
     * @throws IOException if an I/O error occurs.
     */
    void initialize() throws IOException {
        is = socket.getInputStream();
        os = socket.getOutputStream();
        br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        pw = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
    }
    /**
     * Gets the singleton instance based on the network type.
     *
     * @param netType The type of network (CLIENT or SERVER).
     * @return The Net instance.
     */
    public static Net getInstance(LocalWLANMultiplayerController.NetType netType) {
        switch (netType) {
            case CLIENT:
                if (client == null) {
                    client = new Net();
                }
                return client;
            case SERVER:
                if (server == null) {
                    server = new Net();
                }
                return server;
            default:
                return server;
        }
    }
    /**
     * Starts the server in a separate thread.
     */
    public void startServer() {
        new ServerThread().start();
    }
    /**
     * Connects to the server with the given IP address.
     *
     * @param ip The IP address of the server.
     */
    public void connectToServer(String ip) {
        try {
            socket = new Socket(ip, PORT);
            initialize();
            startRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Starts the read thread for continuously reading messages from the socket.
     */
    void startRead() {
        ReadThread reader = new ReadThread();
        reader.start();
    }
    /**
     * Sends a message to the connected peer.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pw.println(message);
                pw.flush();
            }
        }).start();
    }
    /**
     * Closes the network connection.
     */
    public void close() {
        try {
            pw.close();
            socket.close();
            if (LocalWLANMultiplayerController.netType == LocalWLANMultiplayerController.NetType.SERVER) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Reads a message from the socket.
     *
     * @return The read message.
     */
    public String readMessage() {
        String message = null;
        try {
            message = br.readLine();
        }catch (SocketException se){
            if(nsc!=null){
                nsc.onDisconnect();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
    /**
     * Represents a callback interface for network state change events.
     */
    public interface NetStateChange {
        /**
         * Callback method invoked when the server is successfully started.
         */
        void onServerOK();
        /**
         * Callback method invoked when a connection is established.
         */
        void onConnect();
        /**
         * Callback method invoked when a message is received.
         *
         * @param message The received message.
         */
        void onMessage(String message);
        /**
         * Callback method invoked when the connection is disconnected.
         */
        void onDisconnect();
    }
    /**
     * Sets the network state change listener.
     *
     * @param nsc The NetStateChange listener.
     */
    public void setNetStateChangeListener(NetStateChange nsc) {
        this.nsc = nsc;
    }
    /**
     * Represents a thread for running the server.
     */
    class ServerThread extends Thread {
        /**
         * Runs the server in a loop, continuously accepting connections.
         */
        @Override
        public void run() {
            while (true) {
                try {
                    serverSocket = new ServerSocket(PORT);
                    System.out.println(serverSocket.getInetAddress());
                    if (nsc != null) {
                        nsc.onServerOK();
                    }
                    socket = serverSocket.accept();
                    initialize();
                    if (nsc != null) {
                        nsc.onConnect();
                        nsc.onMessage(buildMessage(LocalWLANMultiplayerController.NET, "some one connected!"));
                    }
                    startRead();
                    break;
                } catch (IOException e) {
                    System.out.print("Server failure\n");
                    e.printStackTrace();
                    try {
                        serverSocket.close();
                    } catch (IOException ex) {
                        //ignore this;
                    }
                }
            }
        }
    }
    /**
     * Represents a service for reading messages from the socket.
     */
    class ReadThread extends Service<String> {
        /**
         * Callback method invoked when the service is succeeded.
         * Notifies the NetStateChange listener about received messages.
         */
        @Override
        protected void succeeded() {
            super.succeeded();
            if (getValue() != null && getValue().length() > 0) {
                if (nsc != null) {
                    nsc.onMessage(getValue());
                }
            }
            this.restart();
        }
        /**
         * Creates a task for reading messages from the socket.
         *
         * @return The created task.
         */
        @Override
        protected Task<String> createTask() {
            return new Task<String>() {
                /**
                 * Executes the task by calling the readMessage method.
                 *
                 * @return The result of the readMessage method.
                 * @throws Exception If an error occurs during the task execution.
                 */
                protected String call() throws Exception {
                    return readMessage();
                }
            };
        }
    }
    /**
     * Builds a formatted message with a specified head and body.
     *
     * @param head The message head.
     * @param body The message body.
     * @return The formatted message.
     */
    static String buildMessage(String head, String body) {
        return head + ':' + body;
    }
}

