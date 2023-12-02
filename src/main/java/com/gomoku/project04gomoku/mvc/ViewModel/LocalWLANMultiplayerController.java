package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.GomokuStart;
import com.gomoku.project04gomoku.app.logic.Game;
import com.gomoku.project04gomoku.app.models.Board;
import com.gomoku.project04gomoku.app.logic.Player;
import com.gomoku.project04gomoku.app.models.Net;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalWLANMultiplayerController implements Net.NetStateChange {
    @FXML
    private Canvas canvas; // The canvas for drawing the game board and pieces
    FXMLLoader fxmlLoader;

    @FXML
    private Button RestartButton;
    @FXML
    private Button SettingButton;
    @FXML
    private Button BackButton;
    @FXML
    private Button ConnectButton;
    @FXML
    private Button StartButton;
    @FXML
    private Button SendButton;
    @FXML
    private Button UndoButton;
    @FXML
    private TextField tfIP;
    @FXML
    private TextField tfMessage;
    @FXML
    private TextArea taContent;
    @FXML
    private Label lbIP;

    private ChessUtils ChessUtils;
    private Game game;
    private final double padding = 20;
    private GraphicsContext gc; // The graphics context for drawing on the canvas

    private Net server;
    private Net client;

    public static final String NET = "net";
    public static final String MSG = "msg";
    public static final String CHESS = "chess";
    public static final String GAME = "game";
    public static final String UNDO = "undo";
    public static final String RESTART = "restart";
    public static final String OK = "ok";
    public static final String NO = "no";

    public static NetType netType;

    private boolean isMyTurn = true;


    public enum NetType {
        CLIENT, SERVER
    }

    // Initialize the game and graphics context, and draw the empty game board
    public void initialize() {
        this.game = new Game();
        this.gc = canvas.getGraphicsContext2D();
        this.ChessUtils = new ChessUtils(canvas, game);
        try {
            lbIP.setText("Local IP: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Replay(ActionEvent event) {
        ChessUtils.replayMoves();
    }

    private void restartGame() {
        game.restartGame(); // Reset game
        ChessUtils.updateBoard(); // Update the chessboard display
        taContent.appendText("[System] The game has been restarted\\n");
    }


    @FXML
    public void handleCanvasClick(MouseEvent event) {

        double paddedWidth = canvas.getWidth() - 2 * padding;
        double paddedHeight = canvas.getHeight() - 2 * padding;
        double cellWidth = paddedWidth / (Board.SIZE - 1);
        double cellHeight = paddedHeight / (Board.SIZE - 1);

        int col = (int) Math.round((event.getX() - padding) / cellWidth);
        int row = (int) Math.round((event.getY() - padding) / cellHeight);

        if (netType == NetType.SERVER) {
            if (isMyTurn) {
                if (game.getCurrentPlayer().getColor() == Player.PlayerColor.BLACK) {
                    if (col >= 0 && col < Board.SIZE && row >= 0 && row < Board.SIZE) {
                        game.handleCellClick(row, col);
                        ChessUtils.updateBoard();
                        ChessUtils.checkGameStatus();

                        // Send message to the other client
                        sendChessMove(col, row);
                    }
                } else {
                    taContent.appendText("[System] Please wait for the client to move!\n");
                }
            }
        } else if (netType == NetType.CLIENT) {
            if (isMyTurn) {
                if (game.getCurrentPlayer().getColor() == Player.PlayerColor.WHITE) {
                    if (col >= 0 && col < Board.SIZE && row >= 0 && row < Board.SIZE) {
                        game.handleCellClick(row, col);
                        ChessUtils.updateBoard();
                        ChessUtils.checkGameStatus();

                        // Send message to the other client
                        sendChessMove(col, row);
                    }
                } else {
                    taContent.appendText("[System] Please wait for the host to make a move！\n");
                }
            }
        }
    }

    private void sendChessMove(int col, int row) {
        String message = buildMessage(CHESS, col + ":" + row);
        if (netType == NetType.SERVER) {
            server.sendMessage(message);
            taContent.appendText("[Host] move: " + col + ", " + row + "\n");
        } else if (netType == NetType.CLIENT) {
            client.sendMessage(message);
            taContent.appendText("[Client] Move: " + col + ", " + row + "\n");
        }
    }

    @FXML
    public void GoBackToMain(ActionEvent event) throws IOException {
        try {
            Net.getInstance(netType).close();
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Menu.fxml"));
            Scene root = new Scene(fxmlLoader.load(), 800, 600);
            Stage stage = (Stage) BackButton.getScene().getWindow();
            stage.setScene(root);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSetting(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Setting.fxml"));
        fxmlLoader.setController(new SettingController());
        Parent root = fxmlLoader.load();
        Stage Setting = new Stage();
        Setting.setTitle("Setting");
        Setting.setScene(new Scene(root));
        Setting.show();
    }

    @FXML
    protected void handleStartServer(ActionEvent event) {
        server = Net.getInstance(NetType.SERVER);
        server.startServer();
        server.setNetStateChangeListener(this);
        netType = NetType.SERVER;
    }

    @FXML
    protected void handleConnectClicked(ActionEvent event) {

        if (tfIP.getText().matches("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)")) {
            client = Net.getInstance(NetType.CLIENT);
            client.setNetStateChangeListener(this);
            client.connectToServer(tfIP.getText());
            netType = NetType.CLIENT;
        }
    }

    @FXML
    protected void handleSendClicked(ActionEvent event) {
        if (!tfMessage.getText().isEmpty()) {
            String sender = (netType == NetType.SERVER) ? "[Host] " : "[Client] ";
            String message = buildMessage(MSG, tfMessage.getText());
            sendMessage(message);
            taContent.appendText(sender + tfMessage.getText() + "\n");
            tfMessage.setText("");
        }
    }

    @FXML
    protected void handleUndoClicked(ActionEvent event) {
        String requester = (netType == NetType.SERVER) ? "[Host]" : "[Client]";
        String message = buildMessage(UNDO, "undo");
        sendMessage(message);
        taContent.appendText(requester + " Request to regret\n");
    }


    @FXML
    protected void handleRestartClicked(ActionEvent event) {
        String requester = (netType == NetType.SERVER) ? "[Host]" : "[Client]";
        String message = buildMessage(RESTART, "restart");
        sendMessage(message);
        taContent.appendText(requester + " Request to restart the game\n");
    }


    @Override
    public void onConnect() {
        System.out.println("Some one connected");
        server.sendMessage(buildMessage(NET, OK));
        taContent.appendText("[System] Client is connected!\n");
        tfMessage.setDisable(false);
        SendButton.setDisable(false);
        taContent.appendText("[System] The host plays black and moves first\n");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
        String[] msgArray = message.split(":");
        switch (msgArray[0]) {
            case NET:
                if (msgArray[1].equals(OK)) {
                    taContent.appendText("[System] Connected to host!\n");
                    tfMessage.setDisable(false);
                    SendButton.setDisable(false);
                    tfIP.setDisable(true);
                    StartButton.setDisable(true);
                    ConnectButton.setDisable(true);
                    taContent.appendText("[System] The client plays white, please wait for the host to move first\n");
                }
                break;
            case MSG:
                StringBuilder msgContent = new StringBuilder();
                for (int i = 1; i < msgArray.length; i++) {
                    msgContent.append(msgArray[i]);
                    if (i + 1 < msgArray.length) {
                        msgContent.append(':');
                    }
                }

                if (netType == NetType.SERVER) {
                    taContent.appendText("[Client] " + msgContent.toString() + "\n");
                } else if (netType == NetType.CLIENT) {
                    taContent.appendText("[Host] " + msgContent.toString() + "\n");
                }
                break;
            case CHESS:
                // Parse the received chess move
                int col = Integer.parseInt(msgArray[1]);
                int row = Integer.parseInt(msgArray[2]);

                // Handle the move in your game logic
                game.handleCellClick(row, col); // This assumes that your game logic can handle moves from both players
                ChessUtils.updateBoard(); // Update the board

                // Display the move in the text area
                String playerType = netType == NetType.SERVER ? "Client " : "Host ";
                taContent.appendText("[" + playerType + "] Move: " + col + ", " + row + "\n");

                // Check if the game has ended after the move
                ChessUtils.checkGameStatus();
                break;
            case GAME:
                isMyTurn = true;
                break;
            case UNDO:
                if (msgArray[1].equals("undo")) {
                    String requester = netType == NetType.SERVER ? "[Host]" : "[Client]";
                    handleUndoRequest(requester);
                } else if (msgArray[1].equals(OK) || msgArray[1].equals(NO)) {
                    String responder = netType == NetType.SERVER ? "[Host]" : "[Client]";
                    String responseText = msgArray[1].equals(OK) ? " Agree to regret the move\n" : " Reject to regret the move\n";
                    taContent.appendText(responder + responseText);
                    if (msgArray[1].equals(OK)) {
                        ChessUtils.undoMove();
                        ChessUtils.updateBoard();
                    }
                }
                break;
            case RESTART:
                if (msgArray[1].equals("restart")) {
                    String requester = netType == NetType.SERVER ? "[Host]" : "[Client]";
                    handleRestartRequest(requester);
                } else if (msgArray[1].equals(OK) || msgArray[1].equals(NO)) {
                    String responder = netType == NetType.SERVER ? "[Host]" : "[Client]";
                    String responseText = msgArray[1].equals(OK) ? " Agree to restart the game\n" : " Refuse to restart the game\n";;
                    taContent.appendText(responder + responseText);
                    if (msgArray[1].equals(OK)) {
                        restartGame();
                    }
                }
                break;
        }
    }



    static String buildMessage(String head, String body) {
        return head + ':' + body;
    }
}