package com.gomoku.project04gomoku.app.logic;
/**
 * The `ComputerPlayer` class represents a AI player in the Gomoku game.
 * It implements the `Player` interface, defining the color and player type of the computer player.
 */
public class ComputerPlayer implements Player {
    /**
     * Color of the player piece
     */
    private PlayerColor color;
    /**
     * Constructs a new `ComputerPlayer` with the specified color.
     *
     * @param color The color of the player's pieces (BLACK or WHITE).
     */
    public ComputerPlayer(PlayerColor color) {
        this.color = color;
    }
    /**
     * Gets the color of the player's pieces.
     *
     * @return The color of the player's pieces (BLACK or WHITE).
     */
    @Override
    public PlayerColor getColor() {
        return color;
    }
    /**
     * Gets the type of the player.
     *
     * @return The type of the player (HUMAN).
     */
    @Override
    public PlayerType getType() {
        return PlayerType.COMPUTER;
    }
}
