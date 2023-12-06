package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.GomokuStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import com.gomoku.project04gomoku.app.logic.AI;
import com.gomoku.project04gomoku.app.logic.Game;
import com.gomoku.project04gomoku.app.logic.HumanPlayer;
import com.gomoku.project04gomoku.app.logic.Player;
import com.gomoku.project04gomoku.app.logic.Player.PlayerColor;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * Controller class for Player vs Environment (PvE) mode.
 */
public class PVEController {
    /**
     * The game canvas where the Gomoku board is displayed and interactions occur.
     */
    @FXML private Canvas canvas;
    /**
     * Button for restarting the Gomoku game.
     */
    @FXML private Button RestartButton;
    /**
     * Button for navigating back to the main menu.
     */
    @FXML private Button BackButton;
    /**
     * Utility class for managing chess-related operations on the canvas.
     */
    private ChessUtils chessUtils;
    /**
     * String to store difficulty in text
     */
    private String diff;
    /**
     * The game instance managing the Gomoku game logic.
     */
    private Game game;
    /**
     * The AI instance managing the PvE logic.
     */
    private AI ai;

    /**
     * Constructor to set the difficulty level.
     *
     * @param diff The difficulty level ("Easy", "Normal", "Hard").
     */
    PVEController(String diff)
    {

        this.diff=diff;

    }
    /**
     * Initializes the controller, setting up the game and AI.
     */
    public void initialize() {
        this.game = new Game();
        this.game.setupGameMode(false); // Set to PvE mode
        // AI as white for now
        Player humanPlayer =  new HumanPlayer(PlayerColor.BLACK);
        Player aiPlayer  = new HumanPlayer(PlayerColor.WHITE);
        switch (diff) {
            case "Easy" -> this.ai = new AI(game.getBoard(), aiPlayer, humanPlayer, 2);
            case "Normal" -> this.ai = new AI(game.getBoard(), aiPlayer, humanPlayer, 4);
            case "Hard" -> this.ai = new AI(game.getBoard(), aiPlayer, humanPlayer, 6);
        }


        this.chessUtils = new ChessUtils(canvas, game);
    }

    /**
     * Handles the restart game button click event.
     *
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    public void restartGame(ActionEvent event) {
        game.restartGame();
        chessUtils.updateBoard();
    }
    /**
     * Handles the replay button click event.
     *
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    public void Replay(ActionEvent event)
    {
        chessUtils.replayMoves();
    }
    /**
     * Handles the canvas click event.
     *
     * @param event The MouseEvent triggered by the canvas click.
     */
    @FXML
    public void handleCanvasClick(MouseEvent event) {
        chessUtils.handleCanvasClick(event);
        // After human move, check if game is over or if it's AI's turn
        if (!game.isGameOver() && game.getCurrentPlayer().getType() == Player.PlayerType.COMPUTER) {
            AI.Move aiMove = ai.findBestMove();
            if (aiMove != null) {
                game.handleCellClick(aiMove.x, aiMove.y);
                chessUtils.updateBoard();
            }
        }
        chessUtils.checkGameStatus(); // Check if the game has ended
    }

    /**
     * Handles the "Go Back to Main" button click event.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If there is an issue loading the main menu FXML.
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
     * Opens the setting window.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If there is an issue loading the setting FXML.
     */
    @FXML
    private void openSetting(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Setting.fxml"));
        fxmlLoader.setController(new SettingController());
        Parent root = fxmlLoader.load();
        Stage Setting = new Stage();
        Setting.setTitle("Setting");
        Setting.setResizable(false);
        Setting.setScene(new Scene(root));
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
     * Handles the undo move button click event.
     *
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    public void undoMove(ActionEvent event) {
        chessUtils.undoMove();
    }
}
