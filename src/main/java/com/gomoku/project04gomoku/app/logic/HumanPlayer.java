package com.gomoku.project04gomoku.app.logic;

public class HumanPlayer implements Player {
    private PlayerColor color;

    public HumanPlayer(PlayerColor color) {
        this.color = color;
    }

    @Override
    public PlayerColor getColor() {
        return color;
    }

    @Override
    public PlayerType getType() {
        return PlayerType.HUMAN;
    }
}