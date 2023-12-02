package com.gomoku.project04gomoku.app.models;

import com.gomoku.project04gomoku.mvc.ViewModel.LocalWLANMultiplayerController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Net {

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private BufferedReader br;
    private PrintWriter pw;

    private NetStateChange nsc;

    private ServerSocket serverSocket;

    private static final int PORT = 4040;

    private static Net client;
    private static Net server;

    private Net() {
    }

    void initialize() throws IOException {
        is = socket.getInputStream();
        os = socket.getOutputStream();
        br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        pw = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
    }

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

    public void startServer() {
        new ServerThread().start();
    }


    public void connectToServer(String ip) {
        try {
            socket = new Socket(ip, PORT);
            initialize();
            startRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startRead() {
        ReadThread reader = new ReadThread();
        reader.start();
    }

    public interface NetStateChange {
        void onServerOK();

        void onConnect();

        void onMessage(String message);

        void onDisconnect();
    }

    public void setNetStateChangeListener(NetStateChange nsc) {
        this.nsc = nsc;
    }


    static String buildMessage(String head, String body) {
        return head + ':' + body;
    }
}

