package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.GomokuStart;
import com.gomoku.project04gomoku.app.logic.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LocalWLANMultiplayerController {
    @FXML
    private Canvas canvas; // The canvas for drawing the game board and pieces
    // Adding Start button in fxml later
    FXMLLoader fxmlLoader;
    @FXML private Button RestartButton;
    @FXML private Button SettingButton;
    @FXML private Button BackButton;
    private utils utils;
    private Game game;
    private GraphicsContext gc; // The graphics context for drawing on the canvas
    private final double padding = 20; // You can adjust the padding size as needed

    // Initialize the game and graphics context, and draw the empty game board
    public void initialize() {
        this.game = new Game();
        this.gc = canvas.getGraphicsContext2D();
        this.utils=new utils(canvas,game);
    }

    @FXML
    public void startGame() {
        game.startGame();
        utils.updateBoard();
    }

    @FXML
    public void restartGame() {
        game.restartGame();
        utils.updateBoard();
    }
    @FXML
    public  void handleCanvasClick(javafx.scene.input.MouseEvent event) {
        utils.handleCanvasClick(event);
    }

    @FXML
    public void GoBackToMain(ActionEvent event) throws IOException {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/menu.fxml"));
            Scene root = new Scene(fxmlLoader.load(), 800, 600);


            Stage stage = (Stage) BackButton.getScene().getWindow();


            stage.setScene(root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
