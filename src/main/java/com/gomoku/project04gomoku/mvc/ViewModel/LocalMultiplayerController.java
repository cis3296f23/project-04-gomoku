package com.gomoku.project04gomoku.mvc.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import com.gomoku.project04gomoku.GomokuStart;
import com.gomoku.project04gomoku.app.logic.Game;

public class LocalMultiplayerController {
    @FXML private Canvas canvas;
    @FXML private Button RestartButton;
    @FXML private Button SettingButton;
    @FXML private Button BackButton;


    private ChessUtils chessUtils;
    private Game game;

    public void initialize() {
        this.game = new Game();
        this.game.setupGameMode(true); // Explicitly set to PvP mode
        this.chessUtils = new ChessUtils(canvas, game);
    }

    @FXML
    public void restartGame(ActionEvent event) {
        game.restartGame();
        chessUtils.updateBoard();
    }

    @FXML
    public void Replay(ActionEvent event)
    {
        //TODO: Replay function
    }
    @FXML
    public void handleCanvasClick(javafx.scene.input.MouseEvent event) {
        chessUtils.handleCanvasClick(event);
    }

    @FXML
    public void GoBackToMain(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure to end the game?", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Menu.fxml"));
            Scene root = new Scene(fxmlLoader.load(), 800, 600);
            Stage stage = (Stage) BackButton.getScene().getWindow();
            stage.setScene(root);
            stage.show();
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
}
