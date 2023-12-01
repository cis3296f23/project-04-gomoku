package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;

import static com.gomoku.project04gomoku.app.utils.BoardUtils.isVaild;

public class Evaluator {
    private final Board board;


    // ●●●●●
    final static int FIVE_IN_ROW = 5000000;
    // ○●●●●○
    final static int OPEN_FOUR = 5000000;
    // ●●●●○ or ●●●●×
    final static int HALF_OPEN_FOUR = 100000;
    // ○●●●○
    final static int OPEN_THREE =8000;
    // ●○●● or ●●○●
    final static int SPLIT_THREE =  7000;
    // ●●●○ or ●●●×
    final static int HALF_OPEN_THREE = 500;
    // ○●●○
    final static int OPEN_TWO = 50;
    // ●●○ or ●●×
    final static int HALF_OPEN_TWO = 10;
    // ●○●○
    // final static int SPLIT_TWO = 75;

    String FIVE_IN_ROW_STRING = "11111";
    String OPEN_FOUR_STRING = "011110";
    String HALF_OPEN_FOUR_1_1 = "01111-";
    String HALF_OPEN_FOUR_1_2 = "-11110";
    String HALF_OPEN_FOUR_2_1 = "10111";
    String HALF_OPEN_FOUR_2_2 = "11101";

    String HALF_OPEN_FOUR_3 = "11011";
    String OPEN_THREE_STRING = "01110";
    String SPLIT_THREE_STRING_1_1 = "1011";
    String SPLIT_THREE_STRING_1_2 = "1101";
    String HALF_OPEN_THREE_1_1 = "00111-";
    String HALF_OPEN_THREE_1_2 = "-11100";
    String HALF_OPEN_THREE_2_1 = "01011-";
    String HALF_OPEN_THREE_2_2 = "-11010";
    String HALF_OPEN_THREE_3_1 = "01101-";
    String HALF_OPEN_THREE_3_2 = "-10110";
    String HALF_OPEN_THREE_4_1 = "10011";
    String HALF_OPEN_THREE_4_2 = "11001";
    String HALF_OPEN_THREE_5 = "10101";
    String HALF_OPEN_THREE_6 = "-01110-";
    String OPEN_TWO_1 = "001100";
    String OPEN_TWO_2 = "01010";
    String OPEN_TWO_3 = "1001";
    String HALF_OPEN_TWO_1_1 = "00011-";
    String HALF_OPEN_TWO_1_2 = "-11000";
    String HALF_OPEN_TWO_2_1 = "00101-";
    String HALF_OPEN_TWO_2_2 = "-10100";
    String HALF_OPEN_TWO_3_1 = "01001-";
    String HALF_OPEN_TWO_3_2 = "-10010";
    String HALF_OPEN_TWO_4 = "10001";
    final static  int[][] DiRECTION = {{0,-1,0,1},{-1,0,1,0},{-1,-1,1,1},{-1,1,1,-1}};


    public Evaluator(Board board) {
        this.board = board;
    }

    public int evaluateBoard(Player currentPlayer)
    {
        Player.PlayerType type = currentPlayer.getType()== Player.PlayerType.HUMAN ? Player.PlayerType.COMPUTER : Player.PlayerType.HUMAN;
        int Ally = 0;
        int enemy =0;
        for(int i = 0; i<Board.SIZE;i++)
        {
            for(int j=0;j<Board.SIZE;j++)
            {

                if(board.getCell(i,j).getType()== currentPlayer.getType())
                {
                    Ally+=evaluatedChess(i,j,currentPlayer);

                }
                else if(board.getCell(i,j).getType()==type)
                {
                    enemy += evaluatedChess(i,j,board.getCell(i,j));
                }
            }
        }
        return 10*Ally -enemy;
    }

    int evaluatedChess(int x, int y, Player color)
    {

        int score = 0;
        for(int dir =0;dir<4;dir++)
        {
            StringBuilder s= new StringBuilder();
            int rBegin = x + DiRECTION[dir][0]*4;
            int cBegin = y + DiRECTION[dir][1]*4;
            int rDir = DiRECTION[dir][2];
            int cDir = DiRECTION[dir][3];
            int rEnd = x+rDir*4;
            int cEnd =y+cDir*4;
            int r = rBegin;
            int c = cBegin;
            while(r != rEnd || c!=cEnd)
            {
                if(isVaild(r,c))
                {
                    if(board.getCell(r,c).getType()==color.getType() )
                    {
                        s.append("1");
                    }
                    else if(board.getCell(r,c).getType()== Player.PlayerType.EMPTY)
                    {
                        s.append("0");
                    }
                    else s.append("-");
                }
                else
                {
                    s.append("#");
                }
                r+=rDir;
                c+=cDir;
            }
            String complated = s.toString();
            if(complated.contains(FIVE_IN_ROW_STRING))
            {
                score += FIVE_IN_ROW;
            }
            if(complated.contains(OPEN_FOUR_STRING))
            {
                score += OPEN_FOUR;
            }
            if(complated.contains(HALF_OPEN_FOUR_1_1) ||
                    complated.contains(HALF_OPEN_FOUR_1_2) ||
                    complated.contains(HALF_OPEN_FOUR_2_1) ||
                    complated.contains(HALF_OPEN_FOUR_2_2) ||
                    complated.contains(HALF_OPEN_FOUR_3))
            {
                score +=HALF_OPEN_FOUR;
            }
            if(complated.contains(OPEN_THREE_STRING))
            {
                score += OPEN_THREE;
            }
            if(complated.contains(SPLIT_THREE_STRING_1_1)||
                s.toString().contains(SPLIT_THREE_STRING_1_2))
            {
                score += SPLIT_THREE;
            }
            if(complated.contains(HALF_OPEN_THREE_1_1)||
            complated.contains(HALF_OPEN_THREE_1_2)||
            complated.contains(HALF_OPEN_THREE_2_1)||
            complated.contains(HALF_OPEN_THREE_2_2)||
            complated.contains(HALF_OPEN_THREE_3_1)||
            complated.contains(HALF_OPEN_THREE_3_2)||
            complated.contains(HALF_OPEN_THREE_4_1)||
            complated.contains(HALF_OPEN_THREE_4_2)|| complated.contains(HALF_OPEN_THREE_5)|| complated.contains(HALF_OPEN_THREE_6))
            {
                score += HALF_OPEN_THREE;
            }
            if(complated.contains(OPEN_TWO_1)||
                    complated.contains(OPEN_TWO_2)||
                    complated.contains(OPEN_TWO_3))
            {
                score += OPEN_TWO;
            }
            if(complated.contains(HALF_OPEN_TWO_1_1)||
                    complated.contains(HALF_OPEN_TWO_1_2)||
                    complated.contains(HALF_OPEN_TWO_2_1)||
                    complated.contains(HALF_OPEN_TWO_2_2)||
                    complated.contains(HALF_OPEN_TWO_3_1)||
                    complated.contains(HALF_OPEN_TWO_3_2)||
                    complated.contains(HALF_OPEN_TWO_4))
            {
                score += HALF_OPEN_TWO;
            }
        }
        return score;
    }

//    public int evaluateBoard(Player currentPlayer) {
//        int score = 0;
//        System.out.println(currentPlayer.getType());
//        boolean[][] evaluated = new boolean[Board.SIZE][Board.SIZE]; // Track evaluated cells
//        /*System.out.println("////////////////////");*/
//        // Evaluate the board and adjust the score based on the patterns found
//        // Iterate over every cell in the board
//        for (int x = 0; x < Board.SIZE; x++) {
//            for (int y = 0; y < Board.SIZE; y++) {
//
//               /* if (!board.isEmpty(x, y)) {
//                    System.out.println(board.getCell(x, y).getColor() + " x:" + x + " y:" + y);
//
//                }*/
//
//                // Ensure that we count a pattern only if it starts from this cell
//                // to avoid double counting
//                if (evaluated[x][y]) {
//                    continue; // Skip already evaluated cells
//                }
//
//                // Check for Five in a Row
//                if (checkFiveInRow(x, y, currentPlayer)) {
//                    score += FIVE_IN_ROW;
//                    markEvaluated(evaluated, x, y, 1, 0, 5);
//                    System.out.println("Five in a row found at " + x + "," + y);
//                    continue; // Skip to next cell
//                }
//
//                // Check for Open Four
//                if (checkOpenFour(x, y, currentPlayer)) {
//                    score += OPEN_FOUR;
//                    markEvaluated(evaluated, x, y, 1, 0, 4);
//                    System.out.println("Open four found at " + x + "," + y);
//                    continue; // Skip to next cell
//                }
//
//
//                // Check for Half-Open Four
//                if (checkHalfOpenFour(x, y, currentPlayer)) {
//                    score += HALF_OPEN_FOUR;
//                    markEvaluated(evaluated, x, y, 1, 0, 4);
//                    System.out.println("Half open four found at " + x + "," + y);
//                    continue;
//                }
//
//                // Check for Open Three
//                if (checkOpenThree(x, y, currentPlayer)) {
//                    score += OPEN_THREE;
//                    markEvaluated(evaluated, x, y, 1, 0, 3);
//                    System.out.println("Open three found at " + x + "," + y);
//                    continue;
//                }
//
//                /*// Check for Split Three
//                if (checkSplitThree(x, y, currentPlayer)) {
//                    score += SPLIT_THREE;
//                    markEvaluated(evaluated, x, y, 1, 0, 3); // assuming horizontal split three
//                    continue;
//                }*/
//
//                // Check for Half-Open Three
//                if (checkHalfOpenThree(x, y, currentPlayer)) {
//                    score += HALF_OPEN_THREE;
//                    markEvaluated(evaluated, x, y, 1, 0, 3);
//                    System.out.println("Half open three found at " + x + "," + y);
//                    continue;
//                }
//
//                // Check for Open two
//                if (checkOpenTwo(x, y, currentPlayer)) {
//                    score += OPEN_TWO;
//                    markEvaluated(evaluated, x, y, 1, 0, 2);
//                    System.out.println("Open two found at " + x + "," + y);
//                    continue;
//                }
//
//                // Check for Half-Open two
//                if (checkHalfOpenTwo(x, y, currentPlayer)) {
//                    score += HALF_OPEN_TWO;
//                    markEvaluated(evaluated, x, y, 1, 0, 2);
//                    System.out.println("Half open two found at " + x + "," + y);
//                    continue;
//                }
//
//               /* // Check for Split Two
//                if (checkSplitTwo(x, y, currentPlayer)) {
//                    score += SPLIT_TWO;
//                    markEvaluated(evaluated, x, y, 1, 0, 2); // assuming horizontal split two
//                    continue;
//                }
//                evaluated[x][y] = true;*/
//                // ... Add similar logic for other patterns
//            }
//        }
//        return score;
//    }
//
//
//    // Helper method to mark cells as counted
//    private void markEvaluated(boolean[][] evaluated, int x, int y, int dx, int dy, int length) {
//        for (int i = 0; i < length; i++) {
//            if (x >= 0 && y >= 0 && x < Board.SIZE && y < Board.SIZE) {
//                evaluated[x][y] = true;
//                x += dx;
//                y += dy;
//            }
//        }
//    }
//
//    private boolean checkLine(int x, int y, int dx, int dy, int length, Player player) {
//        return BoardUtils.checkLine(board, x, y, dx, dy, length, player);
//    }
//
//    public boolean checkLineWithGaps(int x, int y, int dx, int dy, int length, Player player, boolean openEnds) {
//        return BoardUtils.checkLineWithGaps(board, x, y, dx, dy, length, player, openEnds);
//    }
//
//    public boolean CheckLineWithSplit(int x, int y, int dx, int dy, int length, Player player) {
//        return BoardUtils.CheckLineWithSplit(board, x, y, dx, dy, length, player);
//    }
//
//    // Method to check for Five in a Row
//    private boolean checkFiveInRow(int x, int y, Player player) {
//        boolean found = checkLine(x, y, 1, 0, 5, player) ||
//                checkLine(x, y, 0, 1, 5, player) ||
//                checkLine(x, y, 1, 1, 5, player) ||
//                checkLine(x, y, 1, -1, 5, player);
//        if (found) System.out.println("Five in a Row found at " + x + "," + y);
//        return found;
//    }
//
//    // Method to check for Open Four
//    private boolean checkOpenFour(int x, int y, Player player) {
//        boolean found = (board.getCell(x, y) == null || board.getCell(x, y) == player) &&
//                (checkLineWithGaps(x, y, 1, 0, 4, player, true) ||
//                        checkLineWithGaps(x, y, 0, 1, 4, player, true) ||
//                        checkLineWithGaps(x, y, 1, 1, 4, player, true) ||
//                        checkLineWithGaps(x, y, 1, -1, 4, player, true));
//        if (found) System.out.println("Open Four found at " + x + "," + y);
//        return found;
//    }
//
//    // Method to check for Half Open Four
//    private boolean checkHalfOpenFour(int x, int y, Player player) {
//        boolean found = checkLineWithGaps(x, y, 1, 0, 4, player, false) ||
//                checkLineWithGaps(x, y, 0, 1, 4, player, false) ||
//                checkLineWithGaps(x, y, 1, 1, 4, player, false) ||
//                checkLineWithGaps(x, y, 1, -1, 4, player, false);
//        if (found) System.out.println("Half Open Four at " + x + "," + y);
//        return found;
//    }
//
//    // Method to check for Open Three
//    private boolean checkOpenThree(int x, int y, Player player) {
//        boolean found = (board.getCell(x, y) == null || board.getCell(x, y) == player) &&
//                (checkLineWithGaps(x, y, 1, 0, 3, player, true) ||
//                        checkLineWithGaps(x, y, 0, 1, 3, player, true) ||
//                        checkLineWithGaps(x, y, 1, 1, 3, player, true) ||
//                        checkLineWithGaps(x, y, 1, -1, 3, player, true));
//        if (found) System.out.println("Open Three at " + x + "," + y);
//        return found;
//    }
//
//    // Method to check for Half-Open Three
//    private boolean checkHalfOpenThree(int x, int y, Player player) {
//        boolean found =  checkLineWithGaps(x, y, 1, 0, 3, player, false) ||
//                checkLineWithGaps(x, y, 0, 1, 3, player, false) ||
//                checkLineWithGaps(x, y, 1, 1, 3, player, false) ||
//                checkLineWithGaps(x, y, 1, -1, 3, player, false);
//        if (found) System.out.println("Half Open Three at " + x + "," + y);
//        return found;
//    }
//
//    // Method to check for Open Two
//    private boolean checkOpenTwo(int x, int y, Player player) {
//        boolean found =  checkLineWithGaps(x, y, 1, 0, 2, player, true) ||
//                checkLineWithGaps(x, y, 0, 1, 2, player, true) ||
//                checkLineWithGaps(x, y, 1, 1, 2, player, true) ||
//                checkLineWithGaps(x, y, 1, -1, 2, player, true);
//        if (found) System.out.println("Open Two at " + x + "," + y);
//        return found;
//    }
//
//    // Method to check for Half-Open Two
//    private boolean checkHalfOpenTwo(int x, int y, Player player) {
//        boolean found =  checkLineWithGaps(x, y, 1, 0, 2, player, false) ||
//                checkLineWithGaps(x, y, 0, 1, 2, player, false) ||
//                checkLineWithGaps(x, y, 1, 1, 2, player, false) ||
//                checkLineWithGaps(x, y, 1, -1, 2, player, false);
//        if (found) System.out.println("Hlaf Open Two at " + x + "," + y);
//        return found;
//    }
//
//  /*  // Method to check for Split Three
//    private boolean checkSplitThree(int x, int y, Player player) {
//        return CheckLineWithSplit(x, y, 1, 0, 3, player) ||
//                CheckLineWithSplit(x, y, 0, 1, 3, player) ||
//                CheckLineWithSplit(x, y, 1, 1, 3, player) ||
//                CheckLineWithSplit(x, y, 1, -1, 3, player);
//    }*/
//
//    // Method to check for Split Two
//    private boolean checkSplitTwo(int x, int y, Player player) {
//        boolean found = CheckLineWithSplit(x, y, 1, 0, 2, player) ||
//                CheckLineWithSplit(x, y, 0, 1, 2, player) ||
//                CheckLineWithSplit(x, y, 1, 1, 2, player) ||
//                CheckLineWithSplit(x, y, 1, -1, 2, player);
//        if (found) System.out.println("Split Two at " + x + "," + y);
//        return found;
//    }
//
//    // Getters for the constants
//    public static int getFiveInRow() {
//        return FIVE_IN_ROW;
//    }
//
//    public static int getOpenFour() {
//        return OPEN_FOUR;
//    }
//
//    public static int getHalfOpenFour() {
//        return HALF_OPEN_FOUR;
//    }
//
//    public static int getOpenThree() {
//        return OPEN_THREE;
//    }
//
//    public static int getHalfOpenThree() {
//        return HALF_OPEN_THREE;
//    }
//
//    public static int getOpenTwo() {
//        return OPEN_TWO;
//    }
//
//    public static int getHalfOpenTwo() {
//        return HALF_OPEN_TWO;
//    }
//
//  /*  public static int getSplitThree() {
//        return SPLIT_THREE;
//    }*/
//
//  /*  public static int getSplitTwo() {
//        return SPLIT_TWO;
//    }*/
//
//    public int evaluateBoardForAll(Player currentPlayer) {
//        int totalScore = 0;
//
//        // Iterate over every cell in the board
//        for (int x = 0; x < Board.SIZE; x++) {
//            for (int y = 0; y < Board.SIZE; y++) {
//                if (!board.isEmpty(x, y) && board.getCell(x, y) == currentPlayer) {
//                    // Evaluate the score for each piece of the current player
//                    totalScore += evaluateBoardFromCell(x, y, currentPlayer);
//                }
//            }
//        }
//
//        return totalScore;
//    }
//
//    private int evaluateBoardFromCell(int x, int y, Player currentPlayer) {
//        int score = 0;
//
//        // Check for Five in a Row
//        if (checkFiveInRow(x, y, currentPlayer)) {
//            score += FIVE_IN_ROW;
//        }
//
//        // Check for Open Four
//        if (checkOpenFour(x, y, currentPlayer)) {
//            score += OPEN_FOUR;
//        }
//
//        // Check for Half-Open Four
//        if (checkHalfOpenFour(x, y, currentPlayer)) {
//            score += HALF_OPEN_FOUR;
//        }
//
//        // Check for Open Three
//        if (checkOpenThree(x, y, currentPlayer)) {
//            score += OPEN_THREE;
//        }
//
//        // Check for Half-Open Three
//        if (checkHalfOpenThree(x, y, currentPlayer)) {
//            score += HALF_OPEN_THREE;
//        }
//
//        // Check for Open Two
//        if (checkOpenTwo(x, y, currentPlayer)) {
//            score += OPEN_TWO;
//        }
//
//        // Check for Half-Open Two
//        if (checkHalfOpenTwo(x, y, currentPlayer)) {
//            score += HALF_OPEN_TWO;
//        }
//
//        // ... Add similar logic for other patterns, if any
//
//        return score;
//    }

}
