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

    // Helper method to check lines with possible gaps at the ends
    private boolean checkLineWithGaps(int x, int y, int dx, int dy, int length, Player player, boolean openEnds) {
        int count = 0;
        int gaps = openEnds ? 1 : 0;

        for (int i = -gaps; i < length + gaps; i++) {
            int checkX = x + i * dx;
            int checkY = y + i * dy;
            if (checkX < 0 || checkY < 0 || checkX >= Board.SIZE || checkY >= Board.SIZE) {
                return false;
            }

            if (i < 0 || i >= length) {
                if (board.getCell(checkX, checkY) != null) {
                    return false;
                }
            } else {
                if (board.getCell(checkX, checkY) != player) {
                    return false;
                }
            }
        }

        return true;
    }

    // Helper method to check split patterns
    private boolean checkSplitPattern(int x, int y, int dx, int dy, int length, Player player) {
        int gapCount = 0;
        int stoneCount = 0;

        for (int i = -1; i <= length; i++) {
            int checkX = x + i * dx;
            int checkY = y + i * dy;

            if (checkX < 0 || checkY < 0 || checkX >= Board.SIZE || checkY >= Board.SIZE) {
                return false;
            }

            Player cellPlayer = board.getCell(checkX, checkY);
            if (i == -1 || i == length) {
                if (cellPlayer != null) return false; // Ends should be empty
            } else {
                if (cellPlayer == player) {
                    stoneCount++;
                } else if (cellPlayer == null) {
                    gapCount++;
                } else {
                    return false; // Encountered opponent's stone
                }
            }
        }

        return stoneCount == length - 1 && gapCount == 1; // Ensure correct number of stones and single gap
    }

    // Method to check for Five in a Row
    private boolean checkFiveInRow(int x, int y, Player player) {
        return checkLine(x, y, 1, 0, 5, player) ||
                checkLine(x, y, 0, 1, 5, player) ||
                checkLine(x, y, 1, 1, 5, player) ||
                checkLine(x, y, 1, -1, 5, player);
    }

    // Method to check for Open Four
    private boolean checkOpenFour(int x, int y, Player player) {
        if (board.getCell(x, y) != null) return false;
        return checkLineWithGaps(x, y, 1, 0, 4, player, true) ||
                checkLineWithGaps(x, y, 0, 1, 4, player, true) ||
                checkLineWithGaps(x, y, 1, 1, 4, player, true) ||
                checkLineWithGaps(x, y, 1, -1, 4, player, true);
    }

    // Method to check for Open Three
    private boolean checkOpenThree(int x, int y, Player player) {
        return checkLineWithGaps(x, y, 1, 0, 3, player, true) ||
                checkLineWithGaps(x, y, 0, 1, 3, player, true) ||
                checkLineWithGaps(x, y, 1, 1, 3, player, true) ||
                checkLineWithGaps(x, y, 1, -1, 3, player, true);
    }

    // Method to check for Half-Open Three
    private boolean checkHalfOpenThree(int x, int y, Player player) {
        return checkLineWithGaps(x, y, 1, 0, 3, player, false) ||
                checkLineWithGaps(x, y, 0, 1, 3, player, false) ||
                checkLineWithGaps(x, y, 1, 1, 3, player, false) ||
                checkLineWithGaps(x, y, 1, -1, 3, player, false);
    }

    // Method to check for Open Two
    private boolean checkOpenTwo(int x, int y, Player player) {
        return checkLineWithGaps(x, y, 1, 0, 2, player, true) ||
                checkLineWithGaps(x, y, 0, 1, 2, player, true) ||
                checkLineWithGaps(x, y, 1, 1, 2, player, true) ||
                checkLineWithGaps(x, y, 1, -1, 2, player, true);
    }

    // Method to check for Half-Open Two
    private boolean checkHalfOpenTwo(int x, int y, Player player) {
        return checkLineWithGaps(x, y, 1, 0, 2, player, false) ||
                checkLineWithGaps(x, y, 0, 1, 2, player, false) ||
                checkLineWithGaps(x, y, 1, 1, 2, player, false) ||
                checkLineWithGaps(x, y, 1, -1, 2, player, false);
    }

    // Method to check for Split Three
    private boolean checkSplitThree(int x, int y, Player player) {
        return checkSplitPattern(x, y, 1, 0, 3, player) ||
                checkSplitPattern(x, y, 0, 1, 3, player) ||
                checkSplitPattern(x, y, 1, 1, 3, player) ||
                checkSplitPattern(x, y, 1, -1, 3, player);
    }

    // Method to check for Split Two
    private boolean checkSplitTwo(int x, int y, Player player) {
        return checkSplitPattern(x, y, 1, 0, 2, player) ||
                checkSplitPattern(x, y, 0, 1, 2, player) ||
                checkSplitPattern(x, y, 1, 1, 2, player) ||
                checkSplitPattern(x, y, 1, -1, 2, player);
    }
}
