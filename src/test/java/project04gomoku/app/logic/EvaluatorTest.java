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
}
