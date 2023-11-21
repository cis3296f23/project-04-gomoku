package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;

public class Game {
    private Board board;
    private Player currentPlayer;
    private Player blackPlayer;
    private Player whitePlayer;
    private boolean gameOver;
    private boolean isPvPMode;  // To distinguish between PvP and PvE modes


    public Game() {
        // Initialize
        board = new Board();
        // Default to PvP mode
        setupGameMode(true);
        gameOver = false;
    }

    public void setupGameMode(boolean isPvP) {
        this.isPvPMode = isPvP;
        blackPlayer = new HumanPlayer(Player.PlayerColor.BLACK);

        if (isPvP) {
            whitePlayer = new HumanPlayer(Player.PlayerColor.WHITE);
        } else {
            whitePlayer = new ComputerPlayer(Player.PlayerColor.WHITE);
        }

        restartGame();
    }

    public void startGame() {
        board.reset(); // Clear the board
        currentPlayer = blackPlayer; // Start with the black player
        gameOver = false;
    }

    public void restartGame() {
        board.reset();
        currentPlayer = blackPlayer;
        gameOver = false;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {return currentPlayer;}

    public void handleCellClick(int x, int y) {
        // Only handle the click if the game is not over and the cell is empty
        if (!gameOver && board.isEmpty(x, y)) {
            // Set the cell to the current player's enum
            board.setCell(x, y, currentPlayer);

            if (checkWin(x, y)) {
                gameOver = true;
                return;
            }

            if (board.isFull()) {
                gameOver = true;
                return;
            }

            // Switch to the next player
            currentPlayer = (currentPlayer == blackPlayer) ? whitePlayer : blackPlayer;

            // If the game is in PvE mode and it's now the computer's turn, handle the computer move
            if (!isPvPMode && currentPlayer.getType() == Player.PlayerType.COMPUTER) {
                // TODO: Implement computer move logic
            }
        }
    }

    public boolean checkWin(int x, int y) {
        Player player = board.getCell(x, y);
        if (player == null) {
            return false; // If the cell is empty, it can't be part of a win
        }

        // Check lines emanating from the last move for a win
        return board.checkLine(x, y, 1, 0, player) + board.checkLine(x, y, -1, 0, player) >= 4 ||
                board.checkLine(x, y, 0, 1, player) + board.checkLine(x, y, 0, -1, player) >= 4 ||
                board.checkLine(x, y, 1, 1, player) + board.checkLine(x, y, -1, -1, player) >= 4 ||
                board.checkLine(x, y, 1, -1, player) + board.checkLine(x, y, -1, 1, player) >= 4;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isDraw() {
        return !gameOver && board.isFull();
    }
}
