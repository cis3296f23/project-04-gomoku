package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;

/**
 * The controller of the Gomoku responsible for tracking the game flow.
 */
public class Game {
    /**
     * A 15x15 board to be used as the game board.
     */
    private final Board board;
    /**
     * The current player available for next move.
     */
    private Player currentPlayer;
    /**
     * Black pieces player.
     */
    private Player blackPlayer;
    /**
     * White pieces player.
     */
    private Player whitePlayer;
    /**
     * State of the game. True when the game ended, or false if no wining conditions observed. Initialized to false.
     */
    private boolean gameOver;
    /**
     * State of the PvPMode. True if the mode is PvP, false otherwise.
     */
    private boolean isPvPMode;  // To distinguish between PvP and PvE modes

    /**
     * Initialized the board, mode, and state of the game.
     */
    public Game() {
        // Initialize
        board = new Board();
        // Default to PvP mode
        setupGameMode(true);
        gameOver = false;
    }

    /**
     * Setups the variables according to the game mode.
     * @param isPvP the state of mode of the game
     */
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

    /**
     * Starts the game with black player go first. All cell are emptied.
     */
    public void startGame() {
        board.reset(); // Clear the board
        currentPlayer = blackPlayer; // Start with the black player
        gameOver = false;
    }

    /**
     * Restarts the game and empties the board.
     */
    public void restartGame() {
        board.restart();
        currentPlayer = blackPlayer;
        gameOver = false;
    }

    public void clear() {
        board.reset();
        currentPlayer = blackPlayer;
        gameOver = false;
    }
    /**
     * Retrieves the board.
     * @return the active game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the current player.
     * @return the current Player object
     */
    public Player getCurrentPlayer() {return currentPlayer;}

    /**
     * Sets the cell (x,y) click event and set the owner to the currentPlayer.
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
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

        }
    }

    /**
     * Checks if a specified cell form a consecutive-five.
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return true if there is a consecutive five in a row, or false otherwise.
     */
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

    /**
     * Sets game state to the gameOver.
     * @param gameOver the true or false value representing a game state.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Checks if the game is over.
     * @return the gameOver state variable
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Checks if there is a tie in the game.
     * @return true if there is a draw between players, or false otherwise.
     */
    public boolean isDraw() {
        return !gameOver && board.isFull();
    }

    public boolean undoLastMove() {
        Board.Move lastMove = board.undoMove();
        if (lastMove != null) {
            currentPlayer = lastMove.player;
            return true;
        }
        return false;
    }

}
