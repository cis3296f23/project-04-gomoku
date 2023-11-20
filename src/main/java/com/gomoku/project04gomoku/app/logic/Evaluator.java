package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;

public class Evaluator {
    private Board board;

    // ●●●●●
    final static int FIVE_IN_ROW = 100000;
    // ○●●●●○
    final static int OPEN_FOUR = 10000;
    // ●●●●○ or ●●●●×
    final static int HALF_OPEN_FOUR = 5000;
    // ○●●●○
    final static int OPEN_THREE = 2500;
    // ●●●○ or ●●●×
    final static int HALF_OPEN_THREE = 500;
    // ○●●○
    final static int OPEN_TWO = 100;
    // ●●○ or ●●×
    final static int HALF_OPEN_TWO = 50;
    // ●○●●○ or ●●○●○
    final static int SPLIT_THREE = 1000;
    // ●○●○
    final static int SPLIT_TWO = 75;

    public Evaluator(Board board) {
        this.board = board;
    }

    public int evaluateBoard(Player currentPlayer) {
        int score = 0;
        // Evaluate the board and adjust the score based on the patterns found
        return score;
    }

    // Method to check line for a pattern
    private boolean checkLine(int x, int y, int dx, int dy, int length, Player player) {
        int count = 0;
        for (int i = 0; i < length; i++) {
            if (x < 0 || y < 0 || x >= Board.SIZE || y >= Board.SIZE || board.getCell(x, y) != player) {
                return false;
            }
            x += dx;
            y += dy;
        }
        return true;
    }

}
