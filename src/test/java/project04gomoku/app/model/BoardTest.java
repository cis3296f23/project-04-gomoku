package project04gomoku.app.model;

import static org.junit.Assert.*;

import com.gomoku.project04gomoku.app.logic.HumanPlayer;
import com.gomoku.project04gomoku.app.models.Board;
import com.gomoku.project04gomoku.app.logic.Player;
import com.gomoku.project04gomoku.app.models.Board.Move;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {

    private Board board;
    private Player blackPlayer;
    private Player whitePlayer;

    @Before
    public void setUp() {
        board = new Board();
        blackPlayer = new HumanPlayer(Player.PlayerColor.BLACK);
        whitePlayer = new HumanPlayer(Player.PlayerColor.WHITE);
    }

    @Test
    public void testBoardInitialization() {
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                assertNull("Board should be initialized with null at cell (" + i + "," + j + ").", board.getCell(i, j));
            }
        }
    }

    @Test
    public void testSetCell() {
        board.setCell(0, 0, blackPlayer);
        assertEquals("Cell (0,0) should be set to BLACK.", blackPlayer, board.getCell(0, 0));
    }

    @Test
    public void testReset() {
        board.setCell(0, 0, blackPlayer);
        board.reset();
        assertNull("Board should be cleared after reset.", board.getCell(0, 0));
    }


    @Test
    public void testIsEmpty() {
        assertTrue("Cell should be empty initially.", board.isEmpty(0, 0));
        board.setCell(0, 0, blackPlayer);
        assertFalse("Cell should not be empty after being set.", board.isEmpty(0, 0));
    }

    @Test
    public void testIsFull() {
        // Initially, the board should not be full
        assertFalse("Board should not be full initially.", board.isFull());

        // Fill the board
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                board.setCell(i, j, blackPlayer);
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
                board.setCell(i, j, blackPlayer);
            }
        }
        board.reset();

        // The board should not be full after reset
        assertFalse("Board should not be full after reset.", board.isFull());
    }

    // Test the record functionality
    @Test
    public void testMoveRecording() {
        // Make a move
        board.setCell(0, 0, blackPlayer);
        assertFalse("Move history should not be empty after a move", board.getMoveHistory().isEmpty());

        Move lastMove = board.getMoveHistory().peek();
        assertNotNull("Last move should not be null", lastMove);
        assertEquals("Last move player should be BLACK", blackPlayer, lastMove.player);
        assertEquals("Last move X coordinate should be 0", 0, lastMove.x);
        assertEquals("Last move Y coordinate should be 0", 0, lastMove.y);
    }


    // Add more tests if there are more behaviors to verify.
}
