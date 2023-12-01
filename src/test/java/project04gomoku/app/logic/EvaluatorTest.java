package project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.logic.*;
import com.gomoku.project04gomoku.app.models.Board;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EvaluatorTest {
    private int score;
    private Board board;
    private Evaluator evaluator;
    private Player blackPlayer;
    private Player whitePlayer;


    @Before
    public void setUp() {
        score = 0;
        board = new Board();
        evaluator = new Evaluator(board);
        blackPlayer = new HumanPlayer(Player.PlayerColor.BLACK);
        whitePlayer = new HumanPlayer(Player.PlayerColor.WHITE);
    }

    @Test
    public void testFiveInRow() {
        // Vertical
        for (int i = 1; i <= 5; i++) {
            board.setCell(i, 0, blackPlayer);
        }
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect five in a row (vertical)", Evaluator.getFiveInRow(), score);

        board.reset();

        // Diagonal
        for (int i = 1; i <= 5; i++) {
            board.setCell(i, 0, blackPlayer);
        }
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect five in a row (diagonal)", Evaluator.getFiveInRow(), score);

        board.reset();

        // Horizontal
        for (int i = 1; i <= 5; i++) {
            board.setCell(0, i, blackPlayer);
        }
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect five in a row (horizontal)", Evaluator.getFiveInRow(), score);
    }

    @Test
    public void testOpenFour() {
        // Vertical
        for (int i = 1; i <= 4;i++) {
            board.setCell(i, 1, whitePlayer);

        }
        score = evaluator.evaluateBoard(whitePlayer);
        assertEquals("Score should reflect open four (vertical)", Evaluator.getOpenFour(), score);

        board.reset();

        // Diagonal
        board.setCell(1, 1, whitePlayer);
        board.setCell(2, 2, whitePlayer);
        board.setCell(3, 3, whitePlayer);
        board.setCell(4, 4, whitePlayer);

        score = evaluator.evaluateBoard(whitePlayer);
        assertEquals("Score should reflect open four (diagonal)", Evaluator.getOpenFour(), score);

        board.reset();

        // Horizontal
        board.setCell(0, 2, whitePlayer);
        board.setCell(0, 3, whitePlayer);
        board.setCell(0, 4, whitePlayer);
        board.setCell(0, 5, whitePlayer);

        score = evaluator.evaluateBoard(whitePlayer);
        assertEquals("Score should reflect open four (horizontally)", Evaluator.getOpenFour(), score);
    }

    @Test
    public void testHalfOpenFour() {
        // Vertical
        for (int i = 1; i <= 4; i++) {
            board.setCell(i, 0, blackPlayer); // four consecutive black pieces
        }
        board.setCell(5, 0, whitePlayer); // blocked by white player

        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect half-open Four (vertical)", Evaluator.getHalfOpenFour(), score);

        board.reset();

        // Diagonal
        for (int i = 1; i <= 4; i++) {
            board.setCell(i, i, blackPlayer); // four consecutive black pieces
        }
        board.setCell(5, 5, whitePlayer); // blocked by white player

        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect half-open Four (Diagonal)", Evaluator.getHalfOpenFour(), score);

        board.reset();

        // Horizontal
        for (int i = 1; i <= 4; i++) {
            board.setCell(0, i, blackPlayer); // four consecutive black pieces
        }
        board.setCell(0, 5, whitePlayer); // blocked by white player

        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect half-open Four (Horizontal)", Evaluator.getHalfOpenFour(), score);
    }

    @Test
    public void testOpenThree() {
        // Vertical
        for (int i = 1; i <= 3; i++) {
            board.setCell(i, 0, blackPlayer); // three consecutive black pieces}
        }
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect open three (vertical)", Evaluator.getOpenThree(), score);

        board.reset();

        // Diagonal
        for (int i = 1; i <= 3; i++) {
            board.setCell(i, i, blackPlayer); // three consecutive black pieces
        }
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect open three (diagonal)", Evaluator.getOpenThree(), score);

        board.reset();

        // Horizontal
        for (int i = 1; i <= 3; i++) {
            board.setCell(0, i, blackPlayer); // three consecutive black pieces
        }
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect open three (horizontal)", Evaluator.getOpenThree(), score);


    }

    @Test
    public void testHalfOpenThree() {
        // Vertical
        for (int i = 1; i <= 3; i++) {
            board.setCell(i, 0, blackPlayer); // three consecutive black pieces
        }
        board.setCell(4, 0, whitePlayer); // blocked by white player

        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect half-open three (vertical)", Evaluator.getHalfOpenThree(), score);

        board.reset();

        // Diagonal
        for (int i = 1; i <= 3; i++) {
            board.setCell(0, i, blackPlayer); // three consecutive black pieces
        }
        board.setCell(0, 4, whitePlayer); // blocked by white player

        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect half-open three (diagonal)", Evaluator.getHalfOpenThree(), score);

        board.reset();

        // Horizontal
        for (int i = 1; i <= 3; i++) {
            board.setCell(0, i, blackPlayer); // three consecutive black pieces
        }
        board.setCell(0, 4, whitePlayer); // blocked by white player

        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect half-open three (Horizontal)", Evaluator.getHalfOpenThree(), score);
    }

    @Test
    public void testOpenTwo() {
        // Vertical
        setLane(1,1,1,0, 2, blackPlayer);
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect open three (vertical)", Evaluator.getOpenTwo(), score);
        board.reset();

        // Diagonal
        setLane(1,1,1,1, 2, blackPlayer);
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect open three (diagonal)", Evaluator.getOpenTwo(), score);
        board.reset();

        // Horizontal
        setLane(0,1,0,1, 2, blackPlayer);
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect open three (Horizontal)", Evaluator.getOpenTwo(), score);
    }

    @Test
    public void testHalfOpenTwo() {
        // Vertical
        setLane(0,0,1,0,2,blackPlayer);
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect half-open two (Vertical)", Evaluator.getHalfOpenTwo(), score);
        board.reset();

        // Diagonal
        setLane(0,0,1,1,2,blackPlayer);
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect half-open two (Diagonal)", Evaluator.getHalfOpenTwo(), score);
        board.reset();

        // Horizontal
        setLane(0,0,0,1,2,blackPlayer);
        score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect half-open two (Horizontal)", Evaluator.getHalfOpenTwo(), score);
    }

/*    @Test
    public void testSplitThree() {
        // Set up a split three pattern
        board.setCell(0, 0, blackPlayer);
        board.setCell(1, 0, null); // gap
        board.setCell(2, 0, blackPlayer);
        board.setCell(3, 0, blackPlayer);
        board.setCell(4, 0, null); // outside the pattern

        int score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect split three", Evaluator.getSplitThree(), score);
    }*/

   /* @Test
    public void testSplitTwo() {
        // Set up a split two pattern
        board.setCell(0, 0, blackPlayer);
        board.setCell(1, 0, null); // gap
        board.setCell(2, 0, blackPlayer);
        board.setCell(3, 0, null); // outside the pattern

        int score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect split two", Evaluator.getSplitTwo(), score);
    }*/

    @Test
    public void testEvaluateBoardForAll() {
        // Set up a board state that should result in a specific score
        // Example: Placing a sequence of pieces on the board
        board.setCell(0, 0, blackPlayer);
        board.setCell(1, 0, blackPlayer);
        board.setCell(2, 0, blackPlayer);

        // Calculate the total score for the black player
        int totalScore = evaluator.evaluateBoardForAll(blackPlayer);

        // Assert that the total score is as expected
        int expectedScore = 600;
        assertEquals("Total score should match the expected value for the given board state", expectedScore, totalScore);
    }

    @Test
    public void testEmptyBoard() {
        score = evaluator.evaluateBoardForAll(blackPlayer);
        assertEquals("Total score should be zero for an empty board", 0, score);
    }

    private void setLane(int row, int col, int dr, int dc, int count, Player player) {
        for (int i = 0; i < count; i++) {
            board.setCell(row, col, player);
            row += dr;
            col += dc;
        }
    }
}
