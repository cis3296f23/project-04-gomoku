package com.gomoku.project04gomoku.app.logic;

/**
 * The Runnable interface should be implemented by any class whose instances are intend to be a player in the Gomoku game. The class must define both the pieces' color and player type.
 */
public interface Player {
    // Enums can be nested inside the interface
    enum PlayerColor {
        BLACK, WHITE, NONE;  // Representing player colors and empty state
    }

    enum PlayerType {
        HUMAN, COMPUTER,EMPTY;  // Representing types of players
    }

    PlayerColor getColor();
    PlayerType getType();
}
