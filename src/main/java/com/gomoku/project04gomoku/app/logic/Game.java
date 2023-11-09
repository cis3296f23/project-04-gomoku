package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;

public class Game {
    private Board board;
    private int currentPlayer;
    private boolean gameOver;

    public Game() {
        board = new Board();
        currentPlayer = 1;
        gameOver = false;
    }

    public enum Chess {
        EMPTY, BLACK, WHITE
    }



    public Board getBoard() {
        return board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void handleCellClick(int x, int y) {
        if (!gameOver && board.isEmpty(x, y)) {
            board.setCell(x, y, currentPlayer);

            // Immediately after setting the cell, check for a win.
            if (checkWin(x, y)) {
                gameOver = true;
                return; // Exit the method if a win is detected.
            }

            // If the move didn't result in a win, check for a draw.
            if (board.isFull()) {
                gameOver = true; // The game is a draw because the board is full.
                return; // Exit the method if it's a draw.
            }

            // If no win or draw, switch players.
            currentPlayer = 3 - currentPlayer;
        }
    }


    /*
        Check connect 5 by calling checkLine in 8 directions
        If a direction and its opposed direction has a total of 4 pieces (5-1, exclude self), current player wins
     */
    public boolean checkWin(int x, int y) {
        int player = board.getCell(x, y);
        return checkLine(x, y, 1, 0, player) + checkLine(x, y, -1, 0, player) == 4 ||
                checkLine(x, y, 0, 1, player) + checkLine(x, y, 0, -1, player) == 4 ||
                checkLine(x, y, 1, 1, player) + checkLine(x, y, -1, -1, player) == 4 ||
                checkLine(x, y, 1, -1, player) + checkLine(x, y, -1, 1, player) == 4;
    }

    /*
        Return the number of same set of pieces in the direction of (dx, dy)
     */
    private int checkLine(int x, int y, int dx, int dy, int player) {
        int count = 0;
        x += dx;
        y += dy;
        while (x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE && board.getCell(x, y) == player) {
            count++;
            x += dx;
            y += dy;
        }
        return count;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void startGame() {
        board = new Board();
        currentPlayer = 1;
        gameOver = false;
    }

    public void restartGame() {
        board.reset();
        currentPlayer = 1;
        gameOver = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isDraw() {
        // A draw can only happen if the game is not already won by someone.
        return !gameOver && board.isFull();
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

}
