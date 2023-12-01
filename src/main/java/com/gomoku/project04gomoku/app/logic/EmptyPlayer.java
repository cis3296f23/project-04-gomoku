package com.gomoku.project04gomoku.app.logic;

public class EmptyPlayer implements Player{
    private Player.PlayerColor color;

    public EmptyPlayer(Player.PlayerColor color) {
        this.color = color;
    }

    @Override
    public Player.PlayerColor getColor() {
        return color;
    }

    @Override
    public Player.PlayerType getType() {
        return PlayerType.EMPTY;
    }
}
