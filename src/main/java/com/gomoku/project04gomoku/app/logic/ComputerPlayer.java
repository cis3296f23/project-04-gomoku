package com.gomoku.project04gomoku.app.logic;

public class ComputerPlayer implements Player {
    private PlayerColor color;

    public ComputerPlayer(PlayerColor color) {
        this.color = color;
    }

    @Override
    public PlayerColor getColor() {
        return color;
    }

    @Override
    public PlayerType getType() {
        return PlayerType.COMPUTER;
    }

    // Additional methods specific to ComputerPlayer can be added here
    @Override
    public String toString() {
        return "HumanPlayer{" +
                "color=" + color +
                ", type=" + getType() +
                '}';
    }
}