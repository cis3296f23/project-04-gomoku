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

    public Board getBoard() {
        return board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void handleCellClick(int x, int y) {
        if (!gameOver && board.isEmpty(x, y)) {
            board.setCell(x, y, currentPlayer);
            if (checkWin(x, y)) {
                gameOver = true;
            } else {
                currentPlayer = 3 - currentPlayer;
            }
        }
    }

    public boolean checkWin(int x, int y) {
        int player = board.getCell(x, y);
        return checkLine(x, y, 1, 0, player) + checkLine(x, y, -1, 0, player) == 4 ||
                checkLine(x, y, 0, 1, player) + checkLine(x, y, 0, -1, player) == 4 ||
                checkLine(x, y, 1, 1, player) + checkLine(x, y, -1, -1, player) == 4 ||
                checkLine(x, y, 1, -1, player) + checkLine(x, y, -1, 1, player) == 4;
    }

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
        if (gameOver) {
            return false;
        }
        return board.isFull();
    }

}
