package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;
import com.gomoku.project04gomoku.app.utils.BoardUtils;

import java.util.Random;

public class Evaluator {
    final static int FIVE = 10000000;       // ●●●●●
    final static int BLOCKED_FIVE = FIVE;   // ●●●●●○
    final static int FOUR = 100000;         // ●●●●
    final static int BLOCKED_FOUR = 1500; // ●●●●○ ●○●●●
    final static int THREE = 1000;          // ●●●
    final static int BLOCKED_THREE = 150;   // ●●●○
    final static int TWO = 200; // ●●
    final static int BLOCKED_TWO = 15;  // ●
    final static int ONE = 10;
    final static int BLOCKED_ONE = 1;

    /*
        UNACCOUNTED
        SPLIT_THREE     ●●○●○ or ●○●●○
        TWO_THREE       ●○●●○
        TWO_SPLIT_THREE
        THREE_FOUR
        BLOCKED_FOUR_FOUR

        FOUR_FOUR
        FOUR_THREE
        THREE_THREE
        TWO_TWO

     */
    //
    //  or
    // final static int SPLIT_THREE = 1000; //
    // ●●●○ or ●●●×
    final static int HALF_OPEN_THREE = 500;
    // ○●●○
    final static int OPEN_TWO = 100;
    // ●●○ or ●●×
    final static int HALF_OPEN_TWO = 50;
    // ●○●○
    // final static int SPLIT_TWO = 75;



    public enum DIRECTION{
        // DIAGONAL_SLASH means top right to left bottom, "/"
        // DIAGONAL_BACKSLASH means top left to right bottom, "\"
        HORIZONTAL, VERTICAL, DIAGONAL_SLASH, DIAGONAL_BACKSLASH
    }

    public Board board;
    public Evaluator(Board board){
        this.board = board;
    }
    /*
    public static void main(String[] args){
        System.out.println("Hello World");
        Board board = generateRandomBoard();
        printBoard(board);
        System.out.println("");
        printLine(getLine(board, 0, 0, DIRECTION.DIAGONAL_BACKSLASH));
    }

    /*
        Debug method to generate a board filled with random pieces
     */
    public static Board generateRandomBoard(){
        Random rand = new Random();
        Board board = new Board();
        for(int i=0; i<Board.SIZE; i++){
            for(int j=0; j<Board.SIZE; j++){
                int value = rand.nextInt(3);
                switch(value){
                    case 0:
                        board.setCell(i, j, null);
                        break;
                    case 1:
                        board.setCell(i, j, new ComputerPlayer(Player.PlayerColor.BLACK));
                        break;
                    case 2:
                        board.setCell(i, j, new ComputerPlayer(Player.PlayerColor.WHITE));
                        break;
                    default:
                        System.out.println("ERROR: unexpected value at generateRandomBoard()");
                }
            }
        }
        return board;
    }

    public static void printLine(Player[] line){
        for(int i=0; i<line.length; i++){
            Player p = line[i];
            if (p == null) System.out.print("- ");
            else if (p.getColor() == Player.PlayerColor.WHITE) System.out.print("o ");
            else System.out.print("x ");
        }
    }
    public static void printBoard(Board board){
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Player p = board.getCell(i,j);
                if (p == null) System.out.print("- ");
                else if (p.getColor() == Player.PlayerColor.WHITE) System.out.print("o ");
                else System.out.print("x ");
            }
            System.out.println();
        }
    }

    public static int evaluatePoint(Board board, int x, int y, Player currentPlayer){
        int score = 0;
        Player[] horizontal = getLine(board, x, y, DIRECTION.HORIZONTAL);
        Player[] vertical = getLine(board,x, y, DIRECTION.VERTICAL);
        Player[] slash = getLine(board, x, y, DIRECTION.DIAGONAL_SLASH);
        Player[] backslash = getLine(board, x, y, DIRECTION.DIAGONAL_BACKSLASH);

        score += analyzeLine(horizontal, currentPlayer, null);
        score += analyzeLine(vertical, currentPlayer, null);
        score += analyzeLine(slash, currentPlayer, null);
        score += analyzeLine(backslash, currentPlayer, null);
        return score;
    }

    //analyze the line returned by getLine, and returns a score
    public static int analyzeLine(Player[] line, Player currentPlayer, boolean[][] evaluated){
        if(line.length != 9){
            System.out.println("ERROR! Incorrect array length at analyzeLine()");
            return 0;
        }

        //check the shape of the line by counting the pieces on both sides starting from middle
        final int mid = 4;  // 4 is the index of middle in array with length 9
        int gapCount = 0;        //gapCount should be <= 1
        int blockedCount = 0;    //blockCount should be <= 1
        int pieceCount = 0;      //pieceCount determines the shape, should be 1 <= pieceCount <= 5

        //check null and if starting point equals currentPlayer
        if(line[mid] == null || line[mid].getColor() != currentPlayer.getColor()){
            return 0;
        }

        //check left side of the array from middle
        for(int i=4; i>=0; i--){
            if(line[i] == null){
                System.out.printf("i = %d is null\n", i);
            }

            if(line[i] == null && gapCount == 0){
                gapCount++;
            } else if(line[i] == null && gapCount >= 1){
                gapCount++;
                break;
            }else if(line[i].getColor() != currentPlayer.getColor() && blockedCount == 0){
                blockedCount++;
                break;
            }else if(line[i].getColor() == currentPlayer.getColor()){
                pieceCount++;
            }else{
                System.out.println("ERROR: unknown situation at analyzeLine()");
            }
        }

        //check right side of the array from middle
        for(int i=4; i<9; i++){
            if(line[i] == null && gapCount == 0){  //problem with using ==
                gapCount++;
            }else if(line[i] == null && gapCount >= 1){
                gapCount++;
                break;
            }else if(line[i].getColor() != currentPlayer.getColor() && blockedCount == 0){
                blockedCount++;
                break;
            }else if(line[i].getColor() == currentPlayer.getColor()){
                pieceCount++;
            }else{
                System.out.println("ERROR: unknown situation at analyzeLine()");
            }
        }

        if(pieceCount == 5){
            return FIVE;
        }else if(pieceCount == 4){
            return FOUR;
        }else if(pieceCount == 3){
            return THREE;
        }else if(pieceCount == 2){
            return TWO;
        }else if(pieceCount == 1){
            return ONE;
        }

        return 0;
    }

    //return a line (array of size 9) given the location and direction
    public static Player[] getLine(Board board, int x, int y, DIRECTION DIRECTION){
        Player[] toReturn = new Player[9];  // stretch 4 steps from the starting point on each side of direction,
                                            // resulting in an array of length 9

        int dx = 0, dy = 0;
        int dx2 = 0, dy2 = 0;       //two sides of direction

        switch(DIRECTION){
            case HORIZONTAL:        //if DIRECTION.HORIZONTAL, then check left to right
                dx = 0;   dy=-1;    //left
                dx2 = 0;  dy2=1;     //right
                break;
            case VERTICAL:          //if DIRECTION.VERTICAL, then check up to down
                dx = -1;   dy = 0;  //up
                dx2 = 1;  dy2 = 0;  //down
                break;
            case DIAGONAL_SLASH:    //if DIRECTION.DIAGONAL_SLASH, then check top right to bottom left
                dx = -1;   dy = 1;  //top right
                dx2 = 1; dy2 = -1;  //bottom left
                break;
            case DIAGONAL_BACKSLASH:    //if DIRECTION.DIAGONAL_BACKSLASH, then check top left to bottom right
                dx = -1;   dy = -1;
                dx2 = 1;   dy2 = 1;
                break;
        }

        int temp_x = x + dx*4;  //setting the point to start [ O X X X X X X X X ]
        int temp_y = y + dy*4;

        //System.out.printf("dx = %d, dy = %d \n", dx, dy); //DEBUG
        //System.out.printf("dx2 = %d, dy2 = %d \n", dx2, dy2); //DEBUG
        for(int i=0; i<9; i++){
            if(checkOutOfBoard(x, y)){
                toReturn[i] = null;
            }else {
                toReturn[i] = board.getCell(temp_x, temp_y);
            }
            //System.out.printf("temp_x = %d, temp_y = %d \n", temp_x, temp_y); //DEBUG
            temp_x += dx2;
            temp_y += dy2;
        }

        return toReturn;
    }

    //return true for out of bound
    //return false for NOT out of bound
    public static boolean checkOutOfBoard(int x, int y){
        return x < 0 || y < 0 || x >= Board.SIZE || y >= Board.SIZE;
    }

    public int evaluateBoard(Board board, Player currentPlayer) {
        int score = 0;
        for(int i=0; i<board.SIZE; i++){
            for(int j=0; j<board.SIZE; j++){
                score += evaluatePoint(board, i, j, currentPlayer);
            }
        }
        return score;
    }
        /*
        int score = 0;
        boolean[][] evaluated = new boolean[Board.SIZE][Board.SIZE]; // Track evaluated cells
        // Evaluate the board and adjust the score based on the patterns found
        // Iterate over every cell in the board
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {


                // Ensure that we count a pattern only if it starts from this cell
                // to avoid double counting
                if (evaluated[x][y]) {
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

                // Check for Split Three
        /*
        if (checkSplitThree(x, y, currentPlayer)) {
                    score += SPLIT_THREE;
                    markEvaluated(evaluated, x, y, 1, 0, 3); // assuming horizontal split three
                    continue;
                }*/

                // Check for Half-Open Three
        /*
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
 /*
            }
        }
        return score;
    }
/*
/*
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
/*
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


}
