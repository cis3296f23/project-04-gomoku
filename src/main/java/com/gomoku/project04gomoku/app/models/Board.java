package com.gomoku.project04gomoku.app.models;

public class Board {
    public static final int SIZE = 15;
    private int[][] board;

    public Board() {
        board = new int[SIZE][SIZE];
    }

    public int getCell(int x, int y) {
        return board[x][y];
    }

    public void setCell(int x, int y, int player) {
        board[x][y] = player;
    }

    public boolean isEmpty(int x, int y) {
        return board[x][y] == 0;
    }

    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 0;
            }
        }
    }

    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
