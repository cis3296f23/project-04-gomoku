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

    void init() throws IOException {
        is = socket.getInputStream();
        os = socket.getOutputStream();
        br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        pw = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
    }


}

