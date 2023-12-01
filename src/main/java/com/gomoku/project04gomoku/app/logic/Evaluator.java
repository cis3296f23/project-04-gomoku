package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;
import com.gomoku.project04gomoku.app.utils.BoardUtils;

public class Evaluator {
    private final Board board;

    // ●●●●●
    final static int FIVE_IN_ROW = 100000;
    // ○●●●●○
    final static int OPEN_FOUR = 10000;
    // ●●●●○ or ●●●●×
    final static int HALF_OPEN_FOUR = 5000;
    // ○●●●○
    final static int OPEN_THREE = 2500;
    // ●○●●○ or ●●○●○
    // final static int SPLIT_THREE = 1000;
    // ●●●○ or ●●●×
    final static int HALF_OPEN_THREE = 500;
    // ○●●○
    final static int OPEN_TWO = 100;
    // ●●○ or ●●×
    final static int HALF_OPEN_TWO = 50;
    // ●○●○
    // final static int SPLIT_TWO = 75;

    public Evaluator(Board board) {
        this.board = board;
    }

    public int evaluateBoard(Player currentPlayer) {
        int score = 0;
        boolean[][] evaluated = new boolean[Board.SIZE][Board.SIZE]; // Track evaluated cells
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Player p = board.getCell(i,j);
                if (p == null) System.out.print("- ");
                else if (p.getColor() == Player.PlayerColor.WHITE) System.out.print("o ");
                else System.out.print("x ");
            }
            System.out.println();
        }
        System.out.println();
        /*System.out.println("////////////////////");*/
        // Evaluate the board and adjust the score based on the patterns found
        // Iterate over every cell in the board
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {

               /* if (!board.isEmpty(x, y)) {
                    System.out.println(board.getCell(x, y).getColor() + " x:" + x + " y:" + y);

                }*/

                // Ensure that we count a pattern only if it starts from this cell
                // to avoid double counting
                if (evaluated[x][y]  || board.isEmpty(x, y)) {
                    continue; // Skip already evaluated cells
                }

                // Check for Five in a Row
                if (checkFiveInRow(x, y, currentPlayer)) {
                    score += FIVE_IN_ROW;
                    markEvaluated(evaluated, x, y, 1, 0, 5);
                    System.out.println("Five in a row found at " + x + "," + y);
                    continue; // Skip to next cell
                }

                // Check for Open Four
                if (checkOpenFour(x, y, currentPlayer)) {
                    score += OPEN_FOUR;
                    markEvaluated(evaluated, x, y, 1, 0, 4);
                    System.out.println("Open four found at " + x + "," + y);
                    continue; // Skip to next cell
                }


                // Check for Half-Open Four
                if (checkHalfOpenFour(x, y, currentPlayer)) {
                    score += HALF_OPEN_FOUR;
                    markEvaluated(evaluated, x, y, 1, 0, 4);
                    System.out.println("Half open four found at " + x + "," + y);
                    continue;
                }

                // Check for Open Three
                if (checkOpenThree(x, y, currentPlayer)) {
                    score += OPEN_THREE;
                    markEvaluated(evaluated, x, y, 1, 0, 3);
                    System.out.println("Open three found at " + x + "," + y);
                    continue;
                }

                /*// Check for Split Three
                if (checkSplitThree(x, y, currentPlayer)) {
                    score += SPLIT_THREE;
                    markEvaluated(evaluated, x, y, 1, 0, 3); // assuming horizontal split three
                    continue;
                }*/

                // Check for Half-Open Three
                if (checkHalfOpenThree(x, y, currentPlayer)) {
                    score += HALF_OPEN_THREE;
                    markEvaluated(evaluated, x, y, 1, 0, 3);
                    System.out.println("Half open three found at " + x + "," + y);
                    continue;
                }

                // Check for Open two
                if (checkOpenTwo(x, y, currentPlayer)) {
                    score += OPEN_TWO;
                    markEvaluated(evaluated, x, y, 1, 0, 2);
                    System.out.println("Open two found at " + x + "," + y);
                    continue;
                }

                // Check for Half-Open two
                if (checkHalfOpenTwo(x, y, currentPlayer)) {
                    score += HALF_OPEN_TWO;
                    markEvaluated(evaluated, x, y, 1, 0, 2);
                    System.out.println("Half open two found at " + x + "," + y);
                    continue;
                }

               /* // Check for Split Two
                if (checkSplitTwo(x, y, currentPlayer)) {
                    score += SPLIT_TWO;
                    markEvaluated(evaluated, x, y, 1, 0, 2); // assuming horizontal split two
                    continue;
                }
                evaluated[x][y] = true;*/
                // ... Add similar logic for other patterns
            }
        }
        return score;
    }


    // Helper method to mark cells as counted
    private void markEvaluated(boolean[][] evaluated, int x, int y, int dx, int dy, int length) {
        for (int i = 0; i < length; i++) {
            if (x >= 0 && y >= 0 && x < Board.SIZE && y < Board.SIZE) {
                evaluated[x][y] = true;
                x += dx;
                y += dy;
            }
        }
    }

    private boolean checkLine(int x, int y, int dx, int dy, int length, Player player) {
        return BoardUtils.checkLine(board, x, y, dx, dy, length, player);
    }

    public boolean checkLineWithGaps(int x, int y, int dx, int dy, int length, Player player, boolean openEnds) {
        return BoardUtils.checkLineWithGaps(board, x, y, dx, dy, length, player, openEnds);
    }

    public boolean CheckLineWithSplit(int x, int y, int dx, int dy, int length, Player player) {
        return BoardUtils.CheckLineWithSplit(board, x, y, dx, dy, length, player);
    }

    // Method to check for Five in a Row
    private boolean checkFiveInRow(int x, int y, Player player) {
        boolean found = checkLine(x, y, 1, 0, 5, player) ||
                checkLine(x, y, 0, 1, 5, player) ||
                checkLine(x, y, 1, 1, 5, player) ||
                checkLine(x, y, 1, -1, 5, player);
        if (found) System.out.println("Five in a Row found at " + x + "," + y);
        return found;
    }

    // Method to check for Open Four
    private boolean checkOpenFour(int x, int y, Player player) {
        boolean found = (board.getCell(x, y) == null || board.getCell(x, y) == player) &&
                (checkLineWithGaps(x, y, 1, 0, 4, player, true) ||
                        checkLineWithGaps(x, y, 0, 1, 4, player, true) ||
                        checkLineWithGaps(x, y, 1, 1, 4, player, true) ||
                        checkLineWithGaps(x, y, 1, -1, 4, player, true));
        if (found) System.out.println("Open Four found at " + x + "," + y);
        return found;
    }

    // Method to check for Half Open Four
    private boolean checkHalfOpenFour(int x, int y, Player player) {
        boolean found = checkLineWithGaps(x, y, 1, 0, 4, player, false) ||
                checkLineWithGaps(x, y, 0, 1, 4, player, false) ||
                checkLineWithGaps(x, y, 1, 1, 4, player, false) ||
                checkLineWithGaps(x, y, 1, -1, 4, player, false);
        if (found) System.out.println("Half Open Four at " + x + "," + y);
        return found;
    }

    // Method to check for Open Three
    private boolean checkOpenThree(int x, int y, Player player) {
        boolean found = (board.getCell(x, y) == null || board.getCell(x, y) == player) &&
                (checkLineWithGaps(x, y, 1, 0, 3, player, true) ||
                        checkLineWithGaps(x, y, 0, 1, 3, player, true) ||
                        checkLineWithGaps(x, y, 1, 1, 3, player, true) ||
                        checkLineWithGaps(x, y, 1, -1, 3, player, true));
        if (found) System.out.println("Open Three at " + x + "," + y);
        return found;
    }

    // Method to check for Half-Open Three
    private boolean checkHalfOpenThree(int x, int y, Player player) {
        boolean found =  checkLineWithGaps(x, y, 1, 0, 3, player, false) ||
                checkLineWithGaps(x, y, 0, 1, 3, player, false) ||
                checkLineWithGaps(x, y, 1, 1, 3, player, false) ||
                checkLineWithGaps(x, y, 1, -1, 3, player, false);
        if (found) System.out.println("Half Open Three at " + x + "," + y);
        return found;
    }

    // Method to check for Open Two
    private boolean checkOpenTwo(int x, int y, Player player) {
        boolean found =  checkLineWithGaps(x, y, 1, 0, 2, player, true) ||
                checkLineWithGaps(x, y, 0, 1, 2, player, true) ||
                checkLineWithGaps(x, y, 1, 1, 2, player, true) ||
                checkLineWithGaps(x, y, 1, -1, 2, player, true);
        if (found) System.out.println("Open Two at " + x + "," + y);
        return found;
    }

    // Method to check for Half-Open Two
    private boolean checkHalfOpenTwo(int x, int y, Player player) {
        boolean found =  checkLineWithGaps(x, y, 1, 0, 2, player, false) ||
                checkLineWithGaps(x, y, 0, 1, 2, player, false) ||
                checkLineWithGaps(x, y, 1, 1, 2, player, false) ||
                checkLineWithGaps(x, y, 1, -1, 2, player, false);
        if (found) System.out.println("Hlaf Open Two at " + x + "," + y);
        return found;
    }

  /*  // Method to check for Split Three
    private boolean checkSplitThree(int x, int y, Player player) {
        return CheckLineWithSplit(x, y, 1, 0, 3, player) ||
                CheckLineWithSplit(x, y, 0, 1, 3, player) ||
                CheckLineWithSplit(x, y, 1, 1, 3, player) ||
                CheckLineWithSplit(x, y, 1, -1, 3, player);
    }*/

    // Method to check for Split Two
    private boolean checkSplitTwo(int x, int y, Player player) {
        boolean found = CheckLineWithSplit(x, y, 1, 0, 2, player) ||
                CheckLineWithSplit(x, y, 0, 1, 2, player) ||
                CheckLineWithSplit(x, y, 1, 1, 2, player) ||
                CheckLineWithSplit(x, y, 1, -1, 2, player);
        if (found) System.out.println("Split Two at " + x + "," + y);
        return found;
    }

    // Getters for the constants
    public static int getFiveInRow() {
        return FIVE_IN_ROW;
    }

    public static int getOpenFour() {
        return OPEN_FOUR;
    }

    public static int getHalfOpenFour() {
        return HALF_OPEN_FOUR;
    }

    public static int getOpenThree() {
        return OPEN_THREE;
    }

    public static int getHalfOpenThree() {
        return HALF_OPEN_THREE;
    }

    public static int getOpenTwo() {
        return OPEN_TWO;
    }

    public static int getHalfOpenTwo() {
        return HALF_OPEN_TWO;
    }

  /*  public static int getSplitThree() {
        return SPLIT_THREE;
    }*/

  /*  public static int getSplitTwo() {
        return SPLIT_TWO;
    }*/

    public int evaluateBoardForAll(Player currentPlayer) {
        int totalScore = 0;

        // Iterate over every cell in the board
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                if (!board.isEmpty(x, y) && board.getCell(x, y) == currentPlayer) {
                    // Evaluate the score for each piece of the current player
                    totalScore += evaluateBoardFromCell(x, y, currentPlayer);
                }
            }
        }

        return totalScore;
    }

    private int evaluateBoardFromCell(int x, int y, Player currentPlayer) {
        int score = 0;

        // Check for Five in a Row
        if (checkFiveInRow(x, y, currentPlayer)) {
            score += FIVE_IN_ROW;
        }

        // Check for Open Four
        if (checkOpenFour(x, y, currentPlayer)) {
            score += OPEN_FOUR;
        }

        // Check for Half-Open Four
        if (checkHalfOpenFour(x, y, currentPlayer)) {
            score += HALF_OPEN_FOUR;
        }

        // Check for Open Three
        if (checkOpenThree(x, y, currentPlayer)) {
            score += OPEN_THREE;
        }

        // Check for Half-Open Three
        if (checkHalfOpenThree(x, y, currentPlayer)) {
            score += HALF_OPEN_THREE;
        }

        // Check for Open Two
        if (checkOpenTwo(x, y, currentPlayer)) {
            score += OPEN_TWO;
        }

        // Check for Half-Open Two
        if (checkHalfOpenTwo(x, y, currentPlayer)) {
            score += HALF_OPEN_TWO;
        }

        // ... Add similar logic for other patterns, if any

        return score;
    }

}
