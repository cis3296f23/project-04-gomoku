package com.gomoku.project04gomoku.app.logic;

public interface Player {
    // Enums can be nested inside the interface
    enum PlayerColor {
        BLACK, WHITE, NONE;  // Representing player colors and empty state
    }

    enum PlayerType {
        HUMAN, COMPUTER;  // Representing types of players
    }

    PlayerColor getColor();
    PlayerType getType();
}
