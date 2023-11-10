package project04gomoku.app.model;

import static org.junit.Assert.*;

import com.gomoku.project04gomoku.app.models.Board;
import com.gomoku.project04gomoku.app.logic.Game.Player;
import org.junit.Before;
import org.junit.Test;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testBoardInitialization() {
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                assertEquals("Board should be initialized with NONE at cell (" + i + "," + j + ").", Player.NONE, board.getCell(i, j));
            }
        }
    }

    @Test
    public void testSetCell() {
        board.setCell(0, 0, Player.BLACK);
        assertEquals("Cell (0,0) should be set to BLACK.", Player.BLACK, board.getCell(0, 0));
    }

    @Test
    public void testReset() {
        board.setCell(0, 0, Player.BLACK);
        board.reset();
        assertEquals("Board should be cleared after reset.", Player.NONE, board.getCell(0, 0));
    }

    @Test
    public void testIsEmpty() {
        assertTrue("Cell should be empty initially.", board.isEmpty(0, 0));
        board.setCell(0, 0, Player.BLACK);
        assertFalse("Cell should not be empty after being set.", board.isEmpty(0, 0));
    }

    @Test
    public void testIsFull() {
        // Initially, the board should not be full
        assertFalse("Board should not be full initially.", board.isFull());

        // Fill the board
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                board.setCell(i, j, Player.BLACK);
            }
        }

        // Now the board should be full
        assertTrue("Board should be full when all cells are set.", board.isFull());
    }

    @Test
    public void testIsFullAfterReset() {
        // Fill the board and then reset
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                board.setCell(i, j, Player.BLACK);
            }
        }
        board.reset();

        // The board should not be full after reset
        assertFalse("Board should not be full after reset.", board.isFull());
    }

    // Add more tests if there are more behaviors to verify.
}
