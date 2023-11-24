package com.gomoku.project04gomoku.app.models;

import com.gomoku.project04gomoku.app.logic.Player;

import java.util.Objects;
import java.util.Stack;

public class Board {
    public static final int SIZE = 15;  // Size of the board (15x15)
    private Player[][] board; // Two-dimensional array to represent the board
    private Stack<Move> moveHistory;

    public Board() {
        board = new Player[SIZE][SIZE];
        moveHistory = new Stack<>();
        reset(); // Sets all positions to null (empty)
    }

    // Returns the player occupying the cell (x,y), or null if it is empty
    public Player getCell(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return null;  // Outside the board bounds
        }
        return board[x][y];
    }

    // Sets the cell at position (x,y) to the specified player
    public void setCell(int x, int y, Player player) {
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
            board[x][y] = player;
            if (player != null) {
                recordMove(x, y, player); // Record the move
            }
        }
    }

    // Checks if the cell at position (x,y) is empty
    public boolean isEmpty(int x, int y) {
        return getCell(x, y) == null;
    }

    // Resets the board to an empty state
    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = null;  // Reset each cell to null (empty)
            }
        }
        moveHistory.clear(); // Clear the move history
    }

    // Checks if the board is completely filled with no empty cells
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
