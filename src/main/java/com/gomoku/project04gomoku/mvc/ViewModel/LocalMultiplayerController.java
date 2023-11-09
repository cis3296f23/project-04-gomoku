package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.app.logic.Game;
import com.gomoku.project04gomoku.app.models.Board;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;


public class LocalMultiplayerController {
    @FXML
    private Canvas canvas;
    @FXML
    private Button start;
    private Game game;
    private GraphicsContext gc;

    public void initialize() {
        game = new Game();
        gc = canvas.getGraphicsContext2D();
        drawBoard();
    }

    private void updateBoard() {
        drawBoard(); // Redraw the board

        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int player = game.getBoard().getCell(i, j);
                if (player != 0) {
                    Color color = (player == 1) ? Color.BLACK : Color.WHITE;
                    drawPiece(i, j, color);
                }
            }
        }
    }

    @FXML
    private void drawBoard() {
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double cellWidth = canvas.getWidth() / Board.SIZE;
        double cellHeight = canvas.getHeight() / Board.SIZE;

        gc.setStroke(Color.BLACK);
        for (int i = 0; i <= Board.SIZE; i++) {
            gc.strokeLine(i * cellWidth, 0, i * cellWidth, canvas.getHeight());
            gc.strokeLine(0, i * cellHeight, canvas.getWidth(), i * cellHeight);
        }
    }

    private void drawPiece(int x, int y, Color color) {
        double cellWidth = canvas.getWidth() / Board.SIZE;
        double cellHeight = canvas.getHeight() / Board.SIZE;
        // Adjust the piece size if necessary
        double pieceDiameter = Math.min(cellWidth, cellHeight) * 0.75;

        // Calculate the center of the intersection
        double centerX = (x * cellWidth) + (cellWidth / 2);
        double centerY = (y * cellHeight) + (cellHeight / 2);

        // Calculate the top left corner of the piece so it's centered in the cell
        double topLeftX = centerX - (pieceDiameter / 2);
        double topLeftY = centerY - (pieceDiameter / 2);

        gc.setFill(color);
        gc.fillOval(topLeftX, topLeftY, pieceDiameter, pieceDiameter);
    }

    @FXML
    private void handleCanvasClick(javafx.scene.input.MouseEvent event) {
        double cellWidth = canvas.getWidth() / Board.SIZE;
        double cellHeight = canvas.getHeight() / Board.SIZE;

        int x = (int) (event.getX() / cellWidth);
        int y = (int) (event.getY() / cellHeight);

        game.handleCellClick(x, y);
        updateBoard();
        checkGameStatus();
    }

    private void checkGameStatus() {
        if (game.isGameOver()) {
            String winner = game.getCurrentPlayer() == 1 ? "Black" : "White";
            displayEndGameMessage(winner + " wins!");
        } else if (game.isDraw()) {
            displayEndGameMessage("It's a draw!");
        }
    }

    private void displayEndGameMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            game.restartGame();
            updateBoard();
        }
    }

    @FXML
    public void startGame() {
        game.startGame();
        updateBoard();
    }

    @FXML
    public void restartGame() {
        game.restartGame();
        updateBoard();
    }

}
