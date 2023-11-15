
/**
 * This class provides utility functions for drawing and updating the Gomoku (Five in a Row) game board
 * using JavaFX. It handles the graphical representation of the board and the game pieces, and also
 * responds to user interactions.
 */

package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.app.logic.Game;
import com.gomoku.project04gomoku.app.models.Board;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;

import java.util.Optional;


public class ChessUtils {
    private Canvas canvas;
    private Game game;
    private GraphicsContext gc;
    private final double padding = 20;

    /**
     * Constructor to initialize the ChessUtils with a canvas and game logic.
     * It sets up the graphics context and draws the initial board.
     *
     * @param canvas The canvas where the game board is drawn.
     * @param game   The game logic and state.
     */
    ChessUtils(Canvas canvas, Game game) {
        this.canvas=canvas;
        this.game=game;
        this.gc = canvas.getGraphicsContext2D();
        drawBoard();
    }
    /**
     * Updates the game board by redrawing it and placing pieces based on the current game state.
     */
    public  void updateBoard() {
        drawBoard(); // Redraw the board
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Game.Player player = game.getBoard().getCell(i, j);
                if (player != Game.Player.NONE) {
                    // Draw the piece of the current player
                    drawPiece(i, j, getPlayerColor(player));
                }
            }
        }
    }
    /**
     * Draws the game board including the grid lines.
     */
    public void drawBoard() {
        double paddedWidth = canvas.getWidth() - 2 * padding;
        double paddedHeight = canvas.getHeight() - 2 * padding;
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // Draw the grid lines for the board
        double cellWidth = paddedWidth / (Board.SIZE - 1);
        double cellHeight = paddedHeight / (Board.SIZE - 1);
        gc.setStroke(Color.BLACK);
        for (int i = 0; i < Board.SIZE - 1; i++) {
            gc.strokeLine(padding + i * cellWidth, padding, padding + i * cellWidth, padding + paddedHeight);
            gc.strokeLine(padding, padding + i * cellHeight, padding + paddedWidth, padding + i * cellHeight);
        }
        // *
        // Draw the last lines on the right and bottom edges
        gc.strokeLine(padding + paddedWidth, padding, padding + paddedWidth, padding + paddedHeight);
        gc.strokeLine(padding, padding + paddedHeight, padding + paddedWidth, padding + paddedHeight);
    }
    /**
     * Draws a game piece on the board.
     *
     * @param col   The column where the piece should be drawn.
     * @param row   The row where the piece should be drawn.
     * @param color The color of the piece.
     */
    public void drawPiece(int col, int row, Color color) {
        double paddedWidth = canvas.getWidth() - 2 * padding;
        double paddedHeight = canvas.getHeight() - 2 * padding;
        double cellWidth = paddedWidth / (Board.SIZE - 1);
        double cellHeight = paddedHeight / (Board.SIZE - 1);
        // Piece diameter as a fraction of cell size
        double pieceDiameter = Math.min(cellWidth, cellHeight) * 0.8;
        // Calculate the top-left position of the piece to center it on the intersection
        double centerX = padding + col * cellWidth;
        double centerY = padding + row * cellHeight;
        double topLeftX = centerX - pieceDiameter / 2;
        double topLeftY = centerY - pieceDiameter / 2;
        gc.setFill(color);
        // Draw the piece
        gc.fillOval(topLeftX, topLeftY, pieceDiameter, pieceDiameter);
    }

    // Get the color associated with a player
    public Color getPlayerColor(Game.Player player) {
        return player == Game.Player.BLACK ? Color.BLACK : Color.WHITE;
    }

    // Handle a click on the canvas (player's move)
    /**
     * Handles mouse click events on the canvas. It calculates the clicked cell and updates the game
     * state based on the player's move.
     *
     * @param event The mouse event that occurred on the canvas.
     */
    public void handleCanvasClick(javafx.scene.input.MouseEvent event) {
        double paddedWidth = canvas.getWidth() - 2 * padding;
        double paddedHeight = canvas.getHeight() - 2 * padding;
        double cellWidth = paddedWidth / (Board.SIZE - 1);
        double cellHeight = paddedHeight / (Board.SIZE - 1);
        // Find the nearest intersection point, accounting for padding
        int col = (int) Math.round((event.getX() - padding) / cellWidth);
        int row = (int) Math.round((event.getY() - padding) / cellHeight);
        // Ensure the click is within the bounds of the board
        if (col >= 0 && col < Board.SIZE && row >= 0 && row < Board.SIZE) {
            game.handleCellClick(col, row); // Handle the click in the game logic
            System.out.println("col = " + col + " row = " + row); // debug
            updateBoard(); // Redraw the board with the new state
            checkGameStatus();  // Check if the game has been won or is a draw
        }
    }
    /**
     * Checks the current status of the game (win, draw, ongoing) and displays an appropriate message
     * if the game has ended.
     */
    public void checkGameStatus() {
        if (game.isGameOver()) {
            String winner = game.getCurrentPlayer() == Game.Player.BLACK ? "Black" : "White";
            displayEndGameMessage(winner + " wins!");  // Display the winner
        } else if (game.isDraw()) {
            displayEndGameMessage("It's a draw!"); // Display a draw message
        }
    }
    /**
     * Displays a message dialog showing the end game status (win/draw) and restarts the game.
     *
     * @param message The message to be displayed in the dialog.
     */
    public void displayEndGameMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        // Wait for the player to acknowledge the message
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            game.restartGame(); // Restart the game
            updateBoard(); // Redraw the board
        }
    }
}
