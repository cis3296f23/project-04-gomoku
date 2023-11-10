package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;

public class Game {
    private Board board;
    private Player currentPlayer;
    private boolean gameOver;

    // Enum for the players and empty cells
    public enum Player {
        NONE, // Represents an empty cell
        BLACK, // Represents the black player
        WHITE; // Represents the white player

        // Method to get the next player
        public Player next() {
            // Ternary operator to switch between BLACK and WHITE
            return (this == BLACK) ? WHITE : (this == WHITE) ? BLACK : NONE;
        }
    }

    public Game() {
        // Initialize
        board = new Board();
        currentPlayer = Player.BLACK;
        gameOver = false;
    }

    public void startGame() {
        board.reset(); // Clear the board
        currentPlayer = Player.BLACK; // Start with the black player
        gameOver = false;
    }

    public void restartGame() {
        board.reset();
        currentPlayer = Player.BLACK;
        gameOver = false;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void handleCellClick(int x, int y) {
        // Only handle the click if the game is not over and the cell is empty
        if (!gameOver && board.isEmpty(x, y)) {
            // Set the cell to the current player's enum
            board.setCell(x, y, currentPlayer);

            if (checkWin(x, y)) {
                gameOver = true;
                return;
            }

            if (board.isFull()) {
                gameOver = true;
                return;
            }

            // Switch to the next player
            currentPlayer = currentPlayer.next();
        }
    }

    public boolean checkWin(int x, int y) {
        Player player = board.getCell(x, y);
        if (player == Player.NONE) {
            return false; // If the cell is empty, it can't be part of a win
        }

        // Check lines emanating from the last move for a win
        return board.checkLine(x, y, 1, 0, player) + board.checkLine(x, y, -1, 0, player) == 4 ||
                board.checkLine(x, y, 0, 1, player) + board.checkLine(x, y, 0, -1, player) == 4 ||
                board.checkLine(x, y, 1, 1, player) + board.checkLine(x, y, -1, -1, player) == 4 ||
                board.checkLine(x, y, 1, -1, player) + board.checkLine(x, y, -1, 1, player) == 4;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isDraw() {
        return !gameOver && board.isFull();
    }
}
