package com.gomoku.project04gomoku.app.models;

import com.gomoku.project04gomoku.app.logic.Player;

import java.util.Objects;
import java.util.Stack;

/**
 * Hold the 2D array representing the game board of Gomoku including functionalities that check and change the board.
 */
public class Board {
    /**
     * The SIZE x SIZE dimension of the board.
     */
    public static final int SIZE = 15;
    /**
     * Actual 2D array representing the board.
     */
    private Player[][] board; // Two-dimensional array to represent the board
    private Stack<Move> moveHistory;

    /**
     * Initialize the board with default SIZE x SIZE dimension and reset all cell to empty state.
     */
    public Board() {
        board = new Player[SIZE][SIZE];
        moveHistory = new Stack<>();
        reset(); // Sets all positions to null (empty)
    }

    /**
     * Gets the player possessed the specified cell (x,y).
     * @param x the x-axis value of the cell
     * @param y the y-axis value of the cell
     * @return the player occupied the cell (x,y), or null if no player possess it.
     */
    public Player getCell(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return null;  // Outside the board bounds
        }
        return board[x][y];
    }

    /**
     * Assigns the owner of the cell (x,y).
     * @param x the x-axis value of the cell
     * @param y the y-axis value of the cell
     * @param player the owner to be set.
     */
    public void setCell(int x, int y, Player player) {
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
            board[x][y] = player;
            if (player != null) {
                recordMove(x, y, player); // Record the move
            }
        }
    }

    /**
     * Check if the specified cell (x,y) is empty.
     * @param x the x-axis value of the cell
     * @param y the y-axis value of the cell
     * @return true if the cell is empty, or false otherwise
     */
    public boolean isEmpty(int x, int y) {
        return getCell(x, y) == null;
    }

    /**
     * Empty all player possessions inside the board to null.
     */
    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = null;  // Reset each cell to null (empty)
            }
        }
    }

    /**
     * Checks if the board is full.
     * @return true if no empty cells leftover, or false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isEmpty(i, j)) {
                    return false;  // Found an empty cell, so the board is not full
                }
            }
        }
        return true;
    }

    /**
     * Count for combo pieces of a player starting from the cell (x,y) and moving in direction specified by (dx, dy).
     * @param x the x-axis value of the cell
     * @param y the y-axis value of the cell
     * @param dx the x-axis direction of traversal
     * @param dy the y-axis direction of traversal
     * @param player the owner of the pieces to look for
     * @return the total count of the consecutive pieces in the specified direction
     */
    // Counts the number of consecutive pieces of the specified player starting
    // from position (x,y) and moving in direction specified by (dx,dy)
    public int checkLine(int x, int y, int dx, int dy, Player player) {
        int count = 0;
        x += dx;
        y += dy;
        while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && getCell(x, y) == player) {
            count++; // Found a piece belonging to the player
            x += dx; // Move in the direction of (dx, dy)
            y += dy;
        }
        return count; // Return the total count of consecutive pieces
    }

    // Records a move to the move history
    private void recordMove(int x, int y, Player player) {
        moveHistory.push(new Move(x, y, player));
    }

    // Undoes the last move and returns it
    public Move undoMove() {
        if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.pop();
            // Reset the cell to empty
            board[lastMove.x][lastMove.y] = null;
            return lastMove;
        }
        return null;
    }

    // Gets the move history
    public Stack<Move> getMoveHistory() {
        return moveHistory;
    }

    public Move getMoveAt(int index) {
        if (index >= 0 && index < moveHistory.size()) {
            return moveHistory.get(index);
        }
        return null;
    }

    // Inner class to represent a move
    public static class Move {
        public int x, y;
        public Player player;

        public Move(int x, int y, Player player) {
            this.x = x;
            this.y = y;
            this.player = player;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Move move = (Move) o;
            return x == move.x && y == move.y && Objects.equals(player, move.player);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, player);
        }
    }
}
