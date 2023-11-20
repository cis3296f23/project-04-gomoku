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
        }
        int score = evaluator.evaluateBoard(blackPlayer);
        assertEquals("Score should reflect five in a row", Evaluator.getFiveInRowScore(), score);
    }


}
