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
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import com.gomoku.project04gomoku.GomokuStart;
import com.gomoku.project04gomoku.app.logic.Game;


/**
 * Controller class for the local multiplayer Gomoku game view.
 * Handles user interactions and updates the UI accordingly.
 */
public class LocalMultiplayerController {
    /**
     * The game canvas where the Gomoku board is displayed and interactions occur.
     */
    @FXML private Canvas canvas;
    /**
     * Button for restarting the Gomoku game.
     */
    @FXML private Button RestartButton;
    /**
     * Button for accessing and modifying bgm settings.
     */
    @FXML private Button SettingButton;
    /**
     * Button for navigating back to the main menu.
     */
    @FXML private Button BackButton;
    /**
     * Button for undoing the last move in the Gomoku game.
     */
    @FXML private Button UndoButton;


    /**
     * Utility class for managing chess-related operations on the canvas.
     */
    private ChessUtils ChessUtils;
    /**
     * The game instance managing the Gomoku game logic.
     */
    private Game game;
    /**
     * Initializes the controller by setting up the Gomoku game and the ChessUtils.
     */
    public void initialize() {
        this.game = new Game();
        this.game.setupGameMode(true); // Explicitly set to PvP mode
        this.ChessUtils = new ChessUtils(canvas, game);
    }
    /**
     * Handles the restart game button action.
     * @param event The ActionEvent triggering the method.
     */
    @FXML
    public void restartGame(ActionEvent event) {
        game.restartGame();
        ChessUtils.updateBoard();
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
     * Handles the canvas click event.
     * @param event The MouseEvent triggering the method.
     */
    @FXML
    public void handleCanvasClick(javafx.scene.input.MouseEvent event) {
        ChessUtils.handleCanvasClick(event);
    }
    /**
     * Handles the back to main menu button action.
     * @param event The ActionEvent triggering the method.
     * @throws IOException if an I/O error occurs while loading the main menu view.
     */
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
    /**
     * Handles the open setting button action.
     * @param event The ActionEvent triggering the method.
     * @throws IOException if an I/O error occurs while loading the setting view.
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
     * Handles the undo move button action.
     * @param event The ActionEvent triggering the method.
     */
    @FXML
    public void undoMove(ActionEvent event) {
        ChessUtils.undoMove();
    }
}
