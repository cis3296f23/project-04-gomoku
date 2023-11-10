package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.app.logic.Game;
import com.gomoku.project04gomoku.app.models.Board;
import com.gomoku.project04gomoku.app.logic.Game.Player;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class LocalMultiplayerController {
    @FXML private Canvas canvas; // The canvas for drawing the game board and pieces
    // Adding Start button in fxml later
    @FXML private Button start;
    private Game game;
    private GraphicsContext gc; // The graphics context for drawing on the canvas

    // Initialize the game and graphics context, and draw the empty game board
    public void initialize() {
        game = new Game();
        gc = canvas.getGraphicsContext2D();
        drawBoard();
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

    // Redraw the board and the current state of the game pieces
    private void updateBoard() {
        drawBoard(); // Redraw the board
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Player player = game.getBoard().getCell(i, j);
                if (player != Player.NONE) {
                    // Draw the piece of the current player
                    drawPiece(i, j, getPlayerColor(player));
                }
            }
        }
    }

    private void drawBoard() {
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // Draw the grid lines for the board
        double cellWidth = canvas.getWidth() / (Board.SIZE - 1);
        double cellHeight = canvas.getHeight() / (Board.SIZE - 1);
        gc.setStroke(Color.BLACK);
        for (int i = 0; i <= Board.SIZE - 1; i++) {
            gc.strokeLine(i * cellWidth, 0, i * cellWidth, canvas.getHeight());
            gc.strokeLine(0, i * cellHeight, canvas.getWidth(), i * cellHeight);
        }
    }

    private void drawPiece(int col, int row, Color color) {
        double cellWidth = canvas.getWidth() / (Board.SIZE -1);
        double cellHeight = canvas.getHeight() / (Board.SIZE -1);
        // Piece diameter as a fraction of cell size
        double pieceDiameter = Math.min(cellWidth, cellHeight) * 0.8; // Adjust the size as needed
        // Calculate the top-left position of the piece to center it on the intersection
        double centerX = col * cellWidth;
        double centerY = row * cellHeight;
        double topLeftX = centerX - pieceDiameter / 2;
        double topLeftY = centerY - pieceDiameter / 2;
        gc.setFill(color);
        // Draw the piece
        gc.fillOval(topLeftX, topLeftY, pieceDiameter, pieceDiameter);
    }

    // Get the color associated with a player
    private Color getPlayerColor(Player player) {
        return player == Player.BLACK ? Color.BLACK : Color.WHITE;
    }

    // Handle a click on the canvas (player's move)
    @FXML
    private void handleCanvasClick(javafx.scene.input.MouseEvent event) {
        double cellWidth = canvas.getWidth() / (Board.SIZE -1);
        double cellHeight = canvas.getHeight() / (Board.SIZE -1);
        // Find the nearest intersection point
        int col = (int) Math.round(event.getX() / cellWidth);
        int row = (int) Math.round(event.getY() / cellHeight);
        // Ensure the click is within the bounds of the board
        if (col >= 0 && col < Board.SIZE && row >= 0 && row < Board.SIZE) {
            game.handleCellClick(col, row); // Handle the click in the game logic
            System.out.println("col = " + col + " row = " + row); // debug
            updateBoard(); // Redraw the board with the new state
            checkGameStatus();  // Check if the game has been won or is a draw
        }
    }

    private void checkGameStatus() {
        if (game.isGameOver()) {
            String winner = game.getCurrentPlayer() == Player.BLACK ? "Black" : "White";
            displayEndGameMessage(winner + " wins!");  // Display the winner
        } else if (game.isDraw()) {
            displayEndGameMessage("It's a draw!"); // Display a draw message
        }
    }

    private void displayEndGameMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        // Wait for the player to acknowledge the message
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            game.restartGame(); // Restart the game
            updateBoard(); // Redraw the board
        }
    }
}
