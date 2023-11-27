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


    // Test the undo functionality
    @Test
    public void testUndoMove() {
        // Make a move and then undo it
        board.setCell(0, 0, blackPlayer);
        Move undoneMove = board.undoMove();

        assertNotNull("Undone move should not be null", undoneMove);
        assertEquals("Undone move player should be BLACK", blackPlayer, undoneMove.player);
        assertEquals("Undone move X coordinate should be 0", 0, undoneMove.x);
        assertEquals("Undone move Y coordinate should be 0", 0, undoneMove.y);

        // Check if the move was really removed from the history
        assertTrue("Move history should be empty after undo", board.getMoveHistory().isEmpty());

        // Check if the cell is empty again
        assertTrue("Cell should be empty after undo", board.isEmpty(0, 0));
    }

    @Test
    public void testMultipleMovesRecording() {
        // Make multiple moves
        board.setCell(0, 0, blackPlayer);
        board.setCell(1, 1, whitePlayer);

        // Check if the move history has recorded both moves
        assertEquals("Move history should have two moves", 2, board.getMoveHistory().size());
        assertEquals("First move should be by BLACK player at (0,0)",
                new Board.Move(0, 0, blackPlayer), board.getMoveHistory().get(0));
        assertEquals("Second move should be by WHITE player at (1,1)",
                new Board.Move(1, 1, whitePlayer), board.getMoveHistory().get(1));
    }

    @Test
    public void testUndoMultipleMoves() {
        // Make multiple moves
        board.setCell(0, 0, blackPlayer);
        board.setCell(1, 1, whitePlayer);

        // Undo the moves
        Board.Move lastMove = board.undoMove();
        assertEquals("Undo move should be WHITE player at (1,1)",
                new Board.Move(1, 1, whitePlayer), lastMove);
        assertTrue("Cell (1,1) should be empty after undo", board.isEmpty(1, 1));

        lastMove = board.undoMove();
        assertEquals("Undo move should be BLACK player at (0,0)",
                new Board.Move(0, 0, blackPlayer), lastMove);
        assertTrue("Cell (0,0) should be empty after undo", board.isEmpty(0, 0));

        assertTrue("Move history should be empty after undoing all moves", board.getMoveHistory().isEmpty());
    }

    @Test
    public void testMoveHistoryAfterMultipleMovesAndUndo() {
        // Make and undo multiple moves
        board.setCell(0, 0, blackPlayer);
        board.setCell(1, 1, whitePlayer);
        board.undoMove();
        board.undoMove();

        // Check if the move history is empty after undoing
        assertTrue("Move history should be empty after undoing all moves", board.getMoveHistory().isEmpty());
    }
    // Add more tests if there are more behaviors to verify.
}
