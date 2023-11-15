package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.GomokuStart;
import com.gomoku.project04gomoku.app.logic.Game;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class LocalMultiplayerController {
    @FXML private Canvas canvas; // The canvas for drawing the game board and pieces
    // Adding Start button in fxml later
    @FXML private Button RestartButton;
    @FXML private Button SettingButton;
    @FXML private Button BackButton;

    FXMLLoader fxmlLoader;
    private ChessUtils ChessUtils;
    private Game game;
    private GraphicsContext gc; // The graphics context for drawing on the canvas
    private final double padding = 20; // You can adjust the padding size as needed

    // Initialize the game and graphics context, and draw the empty game board
    public void initialize() {
        this.game = new Game();
        this.gc = canvas.getGraphicsContext2D();
        this.ChessUtils =new ChessUtils(canvas,game);
    }



    @FXML
    public void restartGame(ActionEvent event) {
        game.restartGame();
        ChessUtils.updateBoard();
    }

    @FXML
    public  void handleCanvasClick(javafx.scene.input.MouseEvent event) {
        ChessUtils.handleCanvasClick(event);
    }

    @FXML
    public void GoBackToMain(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure to end the game?", ButtonType.OK,ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {


            try {
                fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Menu.fxml"));
                Scene root = new Scene(fxmlLoader.load(), 800, 600);


                Stage stage = (Stage) BackButton.getScene().getWindow();


                stage.setScene(root);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
