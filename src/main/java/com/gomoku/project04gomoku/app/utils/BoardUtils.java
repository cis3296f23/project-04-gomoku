package com.gomoku.project04gomoku.app.utils;

import com.gomoku.project04gomoku.app.models.Board;
import com.gomoku.project04gomoku.app.logic.Player;

public class BoardUtils {

    // Method for checking linear sequences, used to check whether there are consecutive chess pieces on rows, columns, and diagonals
    // Helper method to check line for a pattern
    public static boolean checkLine(Board board, int x, int y, int dx, int dy, int length, Player player) {
        for (int i = 0; i < length; i++) {
            if (x < 0 || y < 0 || x >= Board.SIZE || y >= Board.SIZE || board.getCell(x, y) != player) {
                return false;
            }
            x += dx;
            y += dy;
        }
        return true;
    }

    // Check for the existence of specific chess patterns (such as open four, half open four, etc.)
    // Helper method to check lines with possible gaps at the ends
    public static boolean checkLineWithGaps(Board board, int x, int y, int dx, int dy, int length, Player player, boolean openEnds) {
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

    // Check for the existence of split chess patterns (such as separated three pieces, etc.)
    // Helper method to check split patterns
    public static boolean CheckLineWithSplit(Board board, int x, int y, int dx, int dy, int length, Player player) {
        int gapCount = 0;
        int stoneCount = 0;

        for (int i = -1; i <= length; i++) {
            int checkX = x + i * dx;
            int checkY = y + i * dy;

            // Check if within board bounds
            if (checkX < 0 || checkY < 0 || checkX >= Board.SIZE || checkY >= Board.SIZE) {
                return false;
            }

            Player cellPlayer = board.getCell(checkX, checkY);
            if (i == -1 || i == length) {
                // Ends should be empty for split pattern
                if (cellPlayer != null) return false;
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
        return stoneCount == length - 1 && gapCount == 1;
    }

    // Other possible public methods...
}
