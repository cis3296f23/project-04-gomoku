package project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.logic.*;
import com.gomoku.project04gomoku.app.models.Board;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EvaluatorTest {
    private Board board;
    private Evaluator evaluator;
    private Player blackPlayer;
    private Player whitePlayer;


    @Before
    public void setUp() {
        board = new Board();
        evaluator = new Evaluator(board);
        blackPlayer = new HumanPlayer(Player.PlayerColor.BLACK);
        whitePlayer = new HumanPlayer(Player.PlayerColor.WHITE);
    }

    @Test
    public void testFiveInRow() {
        // Set up a five-in-a-row pattern
        for (int i = 0; i < 5; i++) {
            board.setCell(i, 0, blackPlayer);
            System.out.println("x = " + i);
        }
        int score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect five in a row", Evaluator.getFiveInRowScore(), score);
    }

    @Test
    public void testOpenFour() {
        // Set up an open four pattern
        board.setCell(0, 0, null); // empty space
        for (int i = 1; i <= 4; i++) {
            board.setCell(i, 0, blackPlayer); // four consecutive black pieces
            System.out.println("x = " + i);
        }
        board.setCell(5, 0, null); // empty space

        int score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect open four", Evaluator.getOpenFourScore(), score);
    }

}
