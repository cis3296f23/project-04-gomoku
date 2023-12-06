package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;

import java.util.Random;

/**
 * The `Evaluator` class provides methods for evaluating the state of a Gomoku game board.
 * It assigns scores to different patterns and shapes on the board, helping the computer player
 * make strategic moves.
 */
public class Evaluator {
    /**
     * Represents the score assigned to a consecutive-five pattern.
     */
    final static int FIVE = 10000000;
    /**
     * Represents the score assigned to a blocked consecutive-five pattern.
     */
    final static int BLOCKED_FIVE = FIVE;
    /**
     * Represents the score assigned to a consecutive-four pattern.
     */
    final static int FOUR = 100000;
    /**
     * Represents the score assigned to a blocked consecutive-four pattern.
     */
    final static int BLOCKED_FOUR = 1500;
    /**
     * Represents the score assigned to a consecutive-three pattern.
     */
    final static int THREE = 1000;

    /**
     * Represents the score assigned to a blocked consecutive-three pattern.
     */
    final static int BLOCKED_THREE = 150;
    /**
     * Represents the score assigned to a consecutive-two pattern.
     */
    final static int TWO = 200;
    /**
     * Represents the score assigned to a blocked consecutive-two pattern.
     */
    final static int BLOCKED_TWO = 15;
    /**
     * Represents the score assigned to a single piece.
     */
    final static int ONE = 10;
    /**
     * Represents the score assigned to a blocked single piece.
     */
    final static int BLOCKED_ONE = 1;

    /*
        UNACCOUNTED
        SPLIT_THREE
        TWO_THREE
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

    /**
     * Represents the score assigned to a half-open three pattern.
     */
    final static int HALF_OPEN_THREE = 500;

    /**
     * Represents the score assigned to an open two pattern.
     */
    final static int OPEN_TWO = 100;


    /**
     * Represents the score assigned to a half-open two pattern.
     */
    final static int HALF_OPEN_TWO = 50;
    // final static int SPLIT_TWO = 75;
    /**
     * The possible directions to evaluate patterns on the board.
     */
    public enum DIRECTION{
        /**
         * Represents the horizontal direction from left to right.
         */
        HORIZONTAL,

        /**
         * Represents the vertical direction from top to bottom.
         */
        VERTICAL,

        /**
         * Represents the diagonal direction from top right to left bottom ("/").
         */
        DIAGONAL_SLASH,

        /**
         * Represents the diagonal direction from top left to right bottom ("\").
         */
        DIAGONAL_BACKSLASH
    }

    /**
     * The game board to be evaluated.
     */
    public Board board;
    /*
    public Evaluator(Board board){
        this.board = board;
    }
    */
    /**
     * Evaluates the entire game board and returns a score based on the given player's perspective.
     *
     * @param board          The game board to be evaluated.
     * @param currentPlayer  The player for whom the evaluation is done.
     * @return The total score of the board from the perspective of the given player.
     */
    public static int evaluateBoard(Board board, Player currentPlayer) {
        int score = 0;
        for(int i=0; i<board.SIZE; i++){
            for(int j=0; j<board.SIZE; j++){
                score += evaluatePoint(board, i, j, currentPlayer);
            }
        }
        return score;
    }
    /**
     * Calculates the score of a point by analyzing its surroundings in four directions.
     *
     * @param board         The game board.
     * @param x             The x-coordinate of the point.
     * @param y             The y-coordinate of the point.
     * @param currentPlayer The player whose perspective the evaluation is done from.
     * @return The score of the point based on various patterns in the four directions.
     */
    private static int evaluatePoint(Board board, int x, int y, Player currentPlayer){
        int score = 0;

        //no need to check out of bound here, why? check how it is called evaluateBoard()
        //return 0 if it is empty, or it is opponent's piece
        if(board.getCell(x, y) == null || board.getCell(x, y).getColor() != currentPlayer.getColor()){
            return 0;
        }

        boolean evaulated[][] = new boolean[Board.SIZE][Board.SIZE];

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
    /**
     * Analyzes the line returned by getLine and returns a score based on the patterns found.
     *
     * @param line         The line of players to be analyzed.
     * @param currentPlayer The player whose perspective the analysis is done from.
     * @param evaluated    A 2D boolean array representing the cells that have already been evaluated.
     * @return The score based on the patterns found in the line.
     */
    //analyze the line returned by getLine, and returns a score
    private static int analyzeLine(Player[] line, Player currentPlayer, boolean[][] evaluated){
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
        //can be deleted because this is checked at evaluated point
        if(line[mid] == null || line[mid].getColor() != currentPlayer.getColor()){
            return 0;
        }

        //check left side of the array from middle
        for(int i=4; i>=0; i--){
            if(line[i] == null && gapCount == 0){
                gapCount++;
            } else if(line[i] == null && gapCount >= 1){
                gapCount++;
                break;
            }else if(line[i].getColor() != currentPlayer.getColor() && blockedCount == 0){  //one end blocked
                blockedCount++;
                break;
            }else if(line[i].getColor() == currentPlayer.getColor()){
                pieceCount++;
            }else{
                System.out.println("ERROR: unknown situation at analyzeLine()");
            }
        }

        //check right side of the array from middle
        for(int i=5; i<9; i++){     //needs to start at 5 because 4 is counted in above loop
            if(line[i] == null && gapCount == 0){  //problem with using ==
                gapCount++;
            }else if(line[i] == null && gapCount >= 1){
                gapCount++;
                break;
            }else if(line[i].getColor() != currentPlayer.getColor() && blockedCount == 0){
                blockedCount++;
            }else if(line[i].getColor() != currentPlayer.getColor() && blockedCount >= 1 ) {  //two end blocked, use less
                break;
            }else if(line[i].getColor() == currentPlayer.getColor()){
                pieceCount++;
            }else{
                System.out.println("ERROR: unknown situation at analyzeLine()");
            }
        }

        if(blockedCount >= 2){
            return 0;
        }else if(pieceCount == 5 && blockedCount == 1){
            return BLOCKED_FIVE;
        }else if(pieceCount == 4 && blockedCount == 1){
            return BLOCKED_FOUR;
        }else if(pieceCount == 3 && blockedCount == 1){
            return BLOCKED_THREE;
        }else if(pieceCount == 2 && blockedCount == 1){
            return BLOCKED_TWO;
        }else if(pieceCount == 1 && blockedCount == 1) {
            return BLOCKED_ONE;
        }else if(pieceCount == 5) {
            return FIVE;
        }else if(pieceCount == 4){
            return FOUR;
        }else if(pieceCount == 3){
            return THREE;
        }else if(pieceCount == 2){
            return TWO;
        }else if(pieceCount == 1) {
            return ONE;
        }

        return 0;
    }

    /**
     * Returns a line (array of size 9) given the location and direction on the game board.
     *
     * @param board     The game board.
     * @param x         The x-coordinate of the starting point.
     * @param y         The y-coordinate of the starting point.
     * @param DIRECTION The direction in which the line is formed (HORIZONTAL, VERTICAL, DIAGONAL_SLASH, DIAGONAL_BACKSLASH).
     * @return An array of players representing the line formed in the specified direction.
     */
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

    /**
     * Checks if a given point is out of bounds on the game board.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @return {@code true} if the point is out of bounds, {@code false} otherwise.
     */
    public static boolean checkOutOfBoard(int x, int y){
        return x < 0 || y < 0 || x >= Board.SIZE || y >= Board.SIZE;
    }
}
