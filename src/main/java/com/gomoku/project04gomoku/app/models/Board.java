package com.gomoku.project04gomoku.app.models;

import com.gomoku.project04gomoku.app.logic.Game.Player;

public class Board {
    public static final int SIZE = 15;  // Size of the board (15x15)
    private Player[][] board; // Two-dimensional array to represent the board

    public Board() {
        board = new Player[SIZE][SIZE];
        reset(); // Sets all positions to Player.NONE
    }

    // Returns the player occupying the cell (x,y), or Player.NONE if it is empty
    public Player getCell(int x, int y) {
        return board[x][y];
    }

    // Sets the cell at position (x,y) to the specified player
    public void setCell(int x, int y, Player player) {
        board[x][y] = player;
    }

    // Checks if the cell at position (x,y) is empty
    public boolean isEmpty(int x, int y) {
        return board[x][y] == Player.NONE;
    }

    // Resets the board to an empty state
    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = Player.NONE;
            }
        }
    }

    // Checks if the board is completely filled with no empty cells
    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == Player.NONE) {
                    return false; // Found an empty cell, so the board is not full
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
        while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y] == player) {
            count++; // Found a piece belonging to the player
            x += dx; // Move in the direction of (dx, dy)
            y += dy;
        }
        return count; // Return the total count of consecutive pieces
    }
}
