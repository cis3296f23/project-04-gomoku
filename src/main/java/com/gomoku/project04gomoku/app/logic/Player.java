package com.gomoku.project04gomoku.app.logic;

/**
 * The Runnable interface should be implemented by any class whose instances are intend to be a player in the Gomoku game. The class must define both the pieces' color and player type.
 */
public interface Player {
    /**
     * Enum representing the color of the player's pieces.
     * It includes BLACK, WHITE, and NONE (empty state).
     */
    enum PlayerColor {
        /**
         * Represents the black color for player's pieces.
         */
        BLACK,

        /**
         * Represents the white color for player's pieces.
         */
        WHITE,
        /**
         * Represents the absence of a player's piece (empty state).
         */
        NONE;
    }
    /**
     * Enum representing the type of the player.
     * It includes HUMAN and COMPUTER.
     */
    enum PlayerType {
        /**
         * Represents a human player type.
         */
        HUMAN,
        /**
         * Represents a computer player type.
         */
        COMPUTER;
    }
    /**
     * Gets the color of the player's pieces.
     *
     * @return The color of the player's pieces (BLACK, WHITE, or NONE).
     */
    PlayerColor getColor();

    /**
     * Gets the type of the player.
     *
     * @return The type of the player (HUMAN or COMPUTER).
     */
    PlayerType getType();

}
