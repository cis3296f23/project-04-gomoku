package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.GomokuStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class PVEController {
    @FXML private Canvas canvas;
    @FXML private Button RestartButton;
    @FXML private Button BackButton;

    private ChessUtils chessUtils;
    private Game game;
    private AI ai;

    public void initialize() {
        this.game = new Game();
        this.game.setupGameMode(false); // Set to PvE mode
        Player humanPlayer = new HumanPlayer(PlayerColor.BLACK); // Assuming human is black
        Player aiPlayer = new HumanPlayer(PlayerColor.WHITE); // AI as white for now
        this.ai = new AI(game.getBoard(), aiPlayer, humanPlayer);
        this.chessUtils = new ChessUtils(canvas, game);
    }

    @FXML
    public void restartGame(MouseEvent event) {
        game.restartGame();
        chessUtils.updateBoard();
    }

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
}
