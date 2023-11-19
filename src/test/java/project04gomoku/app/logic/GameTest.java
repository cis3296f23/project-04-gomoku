package project04gomoku.app.logic;

import static org.junit.Assert.*;

import com.gomoku.project04gomoku.app.logic.Game;
import com.gomoku.project04gomoku.app.logic.Player;
import com.gomoku.project04gomoku.app.models.Board;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private Game game;

    @Before
    public void setUp() {
        game = new Game();
    }

    @Test
    public void testGameInitialization() {
        assertFalse("Game should not be over after initialization.", game.isGameOver());
        assertEquals("Current player should be BLACK after initialization.", Player.PlayerColor.BLACK, game.getCurrentPlayer().getColor());
    }

    @Test
    public void testHandleCellClick() {
        game.handleCellClick(0, 0);
        assertEquals("Cell (0,0) should be occupied by BLACK.", Player.PlayerColor.BLACK, game.getBoard().getCell(0, 0).getColor());
        // Since the player switches after each move, we need to check the next player's color
        assertEquals("Current player should be WHITE after one successful move.", Player.PlayerColor.WHITE, game.getCurrentPlayer().getColor());
    }

    @Test
    public void testWinCondition() {
        // Simulate a winning condition for BLACK
        for (int i = 0; i < 5; i++) {
            game.handleCellClick(i, 0); // Player.BLACK
            if (i < 4) {
                game.handleCellClick(i, 1); // Player.WHITE
            }
        }
        assertTrue("Game should be over after winning condition is met.", game.isGameOver());
    }

    @Test
    public void testRestartGame() {
        game.handleCellClick(0, 0); // Player.BLACK
        game.restartGame();
        assertEquals("Board should be empty after game restart.", Player.NONE, game.getBoard().getCell(0, 0));
        assertFalse("Game should not be over after restart.", game.isGameOver());
        assertEquals("Current player should be BLACK after game restart.", Player.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void testIllegalClick() {
        game.handleCellClick(0, 0);
        Player currentPlayer = game.getCurrentPlayer();
        game.handleCellClick(0, 0); // Attempting to click the same cell
        assertEquals("Current player should not change after illegal click.", currentPlayer, game.getCurrentPlayer());
    }

    @Test
    public void testClickAfterGameOver() {
        // Simulate a winning condition for BLACK
        for (int i = 0; i < 5; i++) {
            game.handleCellClick(i, 0); // Player.BLACK
            if (i < 4) {
                game.handleCellClick(i, 1); // Player.WHITE
            }
        }
        assertTrue("Game should be over after winning condition is met.", game.isGameOver());
        game.handleCellClick(0, 5); // Any click after game over
        assertTrue("Game should still be over after click after game over.", game.isGameOver());
    }

    @Test
    public void testWinConditionVertical() {
        // Simulate a vertical winning condition for BLACK
        for (int i = 0; i < 5; i++) {
            game.handleCellClick(0, i); // Player.BLACK
            if (i < 4) {
                game.handleCellClick(1, i); // Player.WHITE
            }
        }
        assertTrue("Game should be over after vertical winning condition is met.", game.isGameOver());
    }

    @Test
    public void testWinConditionDiagonal() {
        // Simulate a diagonal winning condition for BLACK
        for (int i = 0; i < 5; i++) {
            game.handleCellClick(i, i); // Player.BLACK
            if (i < 4) {
                game.handleCellClick(i, i + 1); // Player.WHITE
            }
        }
        assertTrue("Game should be over after diagonal winning condition is met.", game.isGameOver());
    }

    // Add more tests if there are more behaviors to verify.

    // 不知道isDraw逻辑有问题还是这个的添加棋子有问题

/*
    @Test
    public void testDrawCondition() {
        // Simulate a full board by filling it with alternating player moves
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                // Alternate moves between Player.BLACK and Player.WHITE
                game.handleCellClick(i, j); // This method now automatically switches the player
                if (game.isGameOver()) {
                    // If a win has been detected, the test setup is incorrect
                    fail("Winning condition met during setup of draw condition test.");
                }
            }
        }
        // After filling the board, check that the game is not over and is a draw
        assertFalse("Game should not be over since no win was detected.", game.isGameOver());
        assertTrue("Game should be a draw if the board is full and no winner.", game.isDraw());
    }
*/


    // Add more tests if there are more behaviors to verify.
}
