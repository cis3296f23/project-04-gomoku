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
import java.util.Objects;
import java.util.Properties;

/**
 * The controller for the local WLAN multiplayer view in the Gomoku game.
 * Implements Net.NetStateChange to handle network state changes.
 */
public class LocalWLANMultiplayerController implements Net.NetStateChange {
    /**
     * The game canvas where the Gomoku board is displayed and interactions occur.
     */
    @FXML
    private Canvas canvas; // The canvas for drawing the game board and pieces
    /**
     * FXMLLoader for loading FXML files.
     */
    FXMLLoader fxmlLoader;
    /**
     * Button for restarting the Gomoku game.
     */
    @FXML
    private Button RestartButton;
    /**
     * Button for accessing and modifying bgm settings.
     */
    @FXML
    private Button SettingButton;
    /**
     * Button for navigating back to the main menu.
     */
    @FXML
    private Button BackButton;
    /**
     * Button for connect to a WLAN host
     */
    @FXML
    private Button ConnectButton;
    /**
     * Button for start a WLAN game
     */
    @FXML
    private Button StartButton;
    /**
     * Button for send a message
     */
    @FXML
    private Button SendButton;
    /**
     * Button for undoing the last move in the Gomoku game.
     */
    @FXML
    private Button UndoButton;
    /**
     * Text field for entering the IP address when connecting to a network.
     */
    @FXML
    private TextField tfIP;
    /**
     * Text field for entering messages to be sent over the network.
     */
    @FXML
    private TextField tfMessage;
    /**
     * Text area for displaying the content of messages and system notifications.
     */
    @FXML
    private TextArea taContent;
    /**
     * Label for displaying the local IP address when hosting a network game.
     */
    @FXML
    private Label lbIP;

    /**
     * Utility class for managing chess-related operations on the canvas.
     */
    private ChessUtils ChessUtils;
    /**
     * The game instance managing the Gomoku game logic.
     */
    private Game game;
    /**
     * The padding around the game board.
     */
    private final double padding = 20;
    /**
     * The graphics context for drawing on the canvas
     */
    private GraphicsContext gc;
    /**
     * The network server instance for hosting a WLAN multiplayer game.
     */
    private Net server;
    /**
     * The network client instance for connecting to a hosted WLAN multiplayer game.
     */
    private Net client;
    /**
     * Constant string representing the network communication type.
     */
    public static final String NET = "net";
    /**
     * Constant string representing a generic message.
     */
    public static final String MSG = "msg";

    /**
     * Constant string representing a chess move message.
     */
    public static final String CHESS = "chess";
    /**
     * Constant string representing a game state message.
     */
    public static final String GAME = "game";
    /**
     * Constant string representing an undo move request.
     */
    public static final String UNDO = "undo";
    /**
     * Constant string representing a game restart request.
     */
    public static final String RESTART = "restart";
    /**
     * Constant string representing an affirmative response.
     */
    public static final String OK = "ok";
    /**
     * Constant string representing a negative response.
     */
    public static final String NO = "no";
    /**
     * Enumeration representing the type of network connection (Client or Server).
     */
    public static NetType netType;
    /**
     * Flag indicating whether it is the current player's turn.
     */
    private boolean isMyTurn = true;
    /**
     * Enum to represent the type of network connection (Client or Server).
     */
    public enum NetType {
        /**
         * Represents the client role in a client-server architecture.
         */
        CLIENT,
        /**
         * Represents the server role in a client-server architecture.
         */
        SERVER
    }

    /**
     * Initialize the game and graphics context, and draw the empty game board
     */
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

    /**
     * Handles the replay button action.
     * @param event The ActionEvent triggering the method.
     */
    @FXML
    public void Replay(ActionEvent event) {
        ChessUtils.replayMoves();
    }

    /**
     * Handles the restart game button action.
     */
    private void restartGame() {
        game.restartGame(); // Reset game
        ChessUtils.updateBoard(); // Update the chessboard display
        taContent.appendText("[System] The game has been restarted \n");
    }
    /**
     * Handles the canvas click event.
     * @param event The MouseEvent triggering the method.
     */
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
            } else {
                taContent.appendText("[System] Please wait for the client to move!!\n");
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
                    taContent.appendText("[System] Please wait for the host to make a move! \n");
                }
            }  else {
                taContent.appendText("[System] Please wait for the host to make a move!! \n");
            }
        }
    }

    /**
     * Sends a chess move message to the opponent over the network.
     *
     * @param col The column of the chess move.
     * @param row The row of the chess move.
     */
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
    /**
     * Navigates back to the main menu view when triggered by the corresponding button.
     *
     * @param event The ActionEvent triggered by the button.
     * @throws IOException If an error occurs during the loading of the main menu scene.
     */
    @FXML
    public void GoBackToMain(ActionEvent event) throws IOException {
        try {
            // Load the main menu scene
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Menu.fxml"));
            Scene root = new Scene(fxmlLoader.load(), 800, 600);
            Stage stage = (Stage) BackButton.getScene().getWindow();
            stage.setScene(root);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the settings view when triggered by the corresponding button.
     *
     * @param event The ActionEvent triggered by the button.
     * @throws IOException If an error occurs during the loading of the settings scene.
     */
    @FXML
    private void openSetting(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Setting.fxml"));
        fxmlLoader.setController(new SettingController());
        Parent root = fxmlLoader.load();
        Stage Setting = new Stage();
        Setting.setTitle("Setting");
        Setting.setScene(new Scene(root));
        Setting.setResizable(false);
        Setting.setOnCloseRequest(e -> {
            // create a dialog windows
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText("Warning");
            alert.setContentText("Are you sure to exit?");
            alert.getDialogPane().getStylesheets().add( Objects.requireNonNull(GomokuStart.class.getResource("css/alert.css")).toExternalForm());
            // wait for response
            alert.showAndWait().ifPresent(response -> {

                if (response != ButtonType.OK) {

                    e.consume();
                }

            });
            Properties settingfile = SettingController.loadSettings();
            ;
            MusicPlayer.setVolume(Double.parseDouble(settingfile.getProperty("volume", "0.5")));

        });
        Setting.show();
    }
    /**
     * Handles the start server event when triggered by the corresponding button.
     *
     * @param event The ActionEvent triggered by the button.
     */
    @FXML
    protected void handleStartServer(ActionEvent event) {
        server = Net.getInstance(NetType.SERVER);
        server.startServer();
        server.setNetStateChangeListener(this);
        netType = NetType.SERVER;
        isMyTurn = false;
    }
    /**
     * Handles the connect event when triggered by the corresponding button.
     *
     * @param event The ActionEvent triggered by the button.
     */
    @FXML
    protected void handleConnectClicked(ActionEvent event) {

        if (tfIP.getText().matches("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)")) {
            client = Net.getInstance(NetType.CLIENT);
            client.setNetStateChangeListener(this);
            client.connectToServer(tfIP.getText());
            netType = NetType.CLIENT;
        }
    }
    /**
     * Handles the send event when triggered by the corresponding button.
     *
     * @param event The ActionEvent triggered by the button.
     */
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
    /**
     * Handles the undo event when triggered by the corresponding button.
     *
     * @param event The ActionEvent triggered by the button.
     */
    @FXML
    protected void handleUndoClicked(ActionEvent event) {
        String requester = (netType == NetType.SERVER) ? "[Host]" : "[Client]";
        String message = buildMessage(UNDO, "undo");
        sendMessage(message);
        taContent.appendText(requester + " Request to regret\n");
    }

    /**
     * Handles the restart event when triggered by the corresponding button.
     *
     * @param event The ActionEvent triggered by the button.
     */
    @FXML
    protected void handleRestartClicked(ActionEvent event) {
        String requester = (netType == NetType.SERVER) ? "[Host]" : "[Client]";
        String message = buildMessage(RESTART, "restart");
        sendMessage(message);
        taContent.appendText(requester + " Request to restart the game\n");
    }

    /**
     * Callback method triggered upon successful network connection.
     */
    @Override
    public void onConnect() {
        System.out.println("Some one connected");
        server.sendMessage(buildMessage(NET, OK));
        taContent.appendText("[System] Client is connected!\n");
        isMyTurn = true;
        tfMessage.setDisable(false);
        SendButton.setDisable(false);
        taContent.appendText("[System] The host plays black and moves first\n");
    }
    /**
     * Callback method triggered upon receiving a network message.
     *
     * @param message The received network message.
     */
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
                    String responseText = msgArray[1].equals(OK) ? " Agree to restart the game\n" : " Refuse to restart the game\n";
                    ;
                    taContent.appendText(responder + responseText);
                    if (msgArray[1].equals(OK)) {
                        restartGame();
                    }
                }
                break;
        }
    }
    /**
     * Handles the undo request received from the opponent.
     *
     * @param requestor The side requesting the undo (either "[Host]" or "[Client]").
     */
    private void handleUndoRequest(String requestor) {
        String requesterDisplay = requestor.equals("[Host]") ? "[Host]" : "[Client]";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, requesterDisplay + " Do you agree with the request to regret the move?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                sendMessage(buildMessage(UNDO, OK));
                ChessUtils.undoMove();
                ChessUtils.updateBoard();
            } else {
                sendMessage(buildMessage(UNDO, NO));
            }
        });
    }
    /**
     * Handles the restart request received from the opponent.
     *
     * @param requestor The side requesting the restart (either "[Host]" or "[Client]").
     */
    private void handleRestartRequest(String requestor) {
        String requesterDisplay = requestor.equals("[Host]") ? "[Host]" : "[Client]";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, requesterDisplay + " Request to restart the game, do you agree?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                sendMessage(buildMessage(RESTART, OK));
                restartGame();
            } else {
                sendMessage(buildMessage(RESTART, NO));
            }
        });
    }

    /**
     * Sends a message over the network based on the current network type (SERVER or CLIENT).
     *
     * @param message The message to be sent.
     */
    private void sendMessage(String message) {
        if (netType == NetType.SERVER) {
            server.sendMessage(message);
        } else if (netType == NetType.CLIENT) {
            client.sendMessage(message);
        }
    }
    /**
     * Callback method triggered upon network disconnection.
     */
    @Override
    public void onDisconnect() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "The line is disconnected!", ButtonType.OK);
        alert.setOnCloseRequest(event -> System.exit(0));
        alert.show();
    }

    /**
     * Callback method triggered upon successful server establishment.
     */
    @Override
    public void onServerOK() {
        System.out.println("server OK");
        taContent.appendText("[System] Host establishment successful!\n");
        StartButton.setDisable(true);
        ConnectButton.setDisable(true);
        tfIP.setDisable(true);
    }
    /**
     * Builds a network message by combining the head and body.
     *
     * @param head The header of the message.
     * @param body The body of the message.
     * @return The constructed network message.
     */
    static String buildMessage(String head, String body) {
        return head + ':' + body;
    }
}