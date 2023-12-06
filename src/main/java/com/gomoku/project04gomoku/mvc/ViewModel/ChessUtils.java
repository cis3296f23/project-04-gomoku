/**
 * This class provides utility functions for drawing and updating the Gomoku (Five in a Row) game board
 * using JavaFX. It handles the graphical representation of the board and the game pieces, and also
 * responds to user interactions.
 */

package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.GomokuStart;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gomoku.project04gomoku.app.logic.Game;
import com.gomoku.project04gomoku.app.logic.Player;
import com.gomoku.project04gomoku.app.models.Board;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

/**
 * This class provides utility functions for drawing and updating the Gomoku (Five in a Row) game board
 * using JavaFX. It handles the graphical representation of the board and the game pieces, and also
 * responds to user interactions.
 */
public class ChessUtils {
    /**
     * The canvas where the game board is drawn.
     */
    private Canvas canvas;
    /**
     * The game logic and state.
     */
    private Game game;
    /**
     * The graphics context for drawing on the canvas.
     */
    private GraphicsContext gc;
    /**
     * The padding around the game board.
     */
    private final double padding = 20;

    /**
     * The CSS stylesheet for styling the alert dialog.
     */
    String css = Objects.requireNonNull(GomokuStart.class.getResource("css/alert.css")).toExternalForm();

    /**
     * Constructor to initialize the ChessUtils with a canvas and game logic.
     * It sets up the graphics context and draws the initial board.
     *
     * @param canvas The canvas where the game board is drawn.
     * @param game   The game logic and state.
     */
    public ChessUtils(Canvas canvas, Game game) {
        this.canvas = canvas;
        this.game = game;
        this.gc = canvas.getGraphicsContext2D();
        drawBoard();
    }

    /**
     * Updates the game board by redrawing it and placing pieces based on the current game state.
     */
    public void updateBoard() {
        drawBoard(); // Redraw the board
        Board.Move lastMove = game.getBoard().getLastMove();


        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Player player = game.getBoard().getCell(i, j);
                if (player != null) {
                    // Draw the piece of the current player
                    drawPiece(j, i, getPlayerColor(player));
                    if (lastMove != null && lastMove.x == i && lastMove.y == j) {
                        highlightPiece(j, i, getPlayerColor(player));
                    }
                }
            }
        }
    }

    /**
     * Highlights a specific game piece on the board.
     *
     * @param col   The column of the piece.
     * @param row   The row of the piece.
     * @param color The color of the piece.
     */
    public void highlightPiece(int col, int row, Color color) {
        double paddedWidth = canvas.getWidth() - 2 * padding;
        double paddedHeight = canvas.getHeight() - 2 * padding;
        double cellWidth = paddedWidth / (Board.SIZE - 1);
        double cellHeight = paddedHeight / (Board.SIZE - 1);
        double pieceDiameter = Math.min(cellWidth, cellHeight) * 0.8;
        double centerX = padding + col * cellWidth;
        double centerY = padding + row * cellHeight;


        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeOval(centerX - pieceDiameter / 2, centerY - pieceDiameter / 2, pieceDiameter, pieceDiameter);
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
        double pieceDiameter = Math.min(cellWidth, cellHeight) * 0.8;
        double centerX = padding + col * cellWidth;
        double centerY = padding + row * cellHeight;
        double topLeftX = centerX - pieceDiameter / 2;
        double topLeftY = centerY - pieceDiameter / 2;
        gc.setFill(color);
        gc.fillOval(topLeftX, topLeftY, pieceDiameter, pieceDiameter);
    }

    /**
     * Retrieves the JavaFX Color associated with a player's piece based on the player's color.
     *
     * @param player The player whose color needs to be determined.
     * @return The JavaFX Color corresponding to the player's color.
     *         If the player's color is BLACK, returns Color.BLACK.
     *         If the player's color is WHITE, returns Color.WHITE.
     *         If the player's color is EMPTY or in an unknown state, returns Color.TRANSPARENT.
     */
    public Color getPlayerColor(Player player) {
        if (player.getColor() == Player.PlayerColor.BLACK) {
            return Color.BLACK;
        } else if (player.getColor() == Player.PlayerColor.WHITE) {
            return Color.WHITE;
        }
        return Color.TRANSPARENT; // Fallback for an undefined state
    }

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
        int col = (int) Math.round((event.getX() - padding) / cellWidth);
        int row = (int) Math.round((event.getY() - padding) / cellHeight);
        if (col >= 0 && col < Board.SIZE && row >= 0 && row < Board.SIZE) {
            game.handleCellClick(row, col);
            updateBoard();
            checkGameStatus();
        }
    }

    /**
     * Checks the current status of the game (win, draw, ongoing) and displays an appropriate message
     * if the game has ended.
     */
    public void checkGameStatus() {
        if (game.isGameOver()) {
            String winner = game.getCurrentPlayer().getColor() == Player.PlayerColor.BLACK ? "Black" : "White";
            displayEndGameMessage(winner + " wins!");
        } else if (game.isDraw()) {
            displayEndGameMessage("It's a draw!");
        }
    }

    /**
     * Displays a message dialog showing the end game status (win/draw) and restarts the game.
     *
     * @param message The message to be displayed in the dialog.
     */
    public void displayEndGameMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Win!!");
        alert.setHeaderText("Game is over!");
        DialogPane dialogPane = alert.getDialogPane();


        dialogPane.getStylesheets().add(css);
        dialogPane.setGraphic(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            game.restartGame();
            updateBoard();
        }
    }

    /**
     * Replays the moves stored in the game's move history, creating a visual representation
     * of the game's progression.
     */
    public void replayMoves() {
        new Thread(() -> {
            game.clear();
            updateBoard();
            int size = game.getBoard().getMoveHistory().size();
            for (int i = 0; i < size; i++) {
                Board.Move move = game.getBoard().getMoveAt(i);
                if (move != null) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    Platform.runLater(() -> {
                        game.getBoard().setCell(move.x, move.y, move.player);
                        game.getBoard().getMoveHistory().pop();
                        updateBoard();
                    });
                }
                System.out.println("run："+i+" times");
            }
            //game.getBoard().setLastMove(null);
            System.out.println("replay over!");
        }).start();



    }

    /**
     * Undoes the last move in the game, updating the board accordingly.
     */
    public void undoMove() {
        if (game.undoLastMove()) {
            updateBoard();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No more moves to undo.", ButtonType.OK);
            alert.setHeaderText("Undo error!");
            alert.getDialogPane().getStylesheets().add(css);
            alert.showAndWait();
        }
    }

}
