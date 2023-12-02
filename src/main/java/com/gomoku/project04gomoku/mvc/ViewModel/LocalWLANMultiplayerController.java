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
        taContent.appendText("[System]The game has been restarted\\n");
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

