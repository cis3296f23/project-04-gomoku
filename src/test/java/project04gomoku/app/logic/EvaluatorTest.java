//package project04gomoku.app.logic;
//
//import com.gomoku.project04gomoku.app.logic.*;
//import com.gomoku.project04gomoku.app.models.Board;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//public class EvaluatorTest {
//    private Board board;
//    private Evaluator evaluator;
//    private Player blackPlayer;
//    private Player whitePlayer;
//    private Player Empty;
//
//
//    @Before
//    public void setUp() {
//        board = new Board();
//        evaluator = new Evaluator(board);
//        blackPlayer = new HumanPlayer(Player.PlayerColor.BLACK);
//        whitePlayer = new HumanPlayer(Player.PlayerColor.WHITE);
//        Empty = new EmptyPlayer(Player.PlayerColor.NONE);
//    }
//
//    @Test
//    public void testFiveInRow() {
//        // Set up a five-in-a-row pattern
//        for (int i = 0; i < 5; i++) {
//            board.setCell(i, 0, blackPlayer);
//            System.out.println("x = " + i);
//        }
//        int score = evaluator.evaluateBoard(blackPlayer);
//        assertEquals("Score should reflect five in a row", Evaluator.getFiveInRow(), score);
//    }
//
//    @Test
//    public void testOpenFour() {
//        // Set up an open four pattern
//        board.setCell(0, 0, Empty); // empty space
//        for (int i = 1; i <= 4; i++) {
//            board.setCell(i, 0, blackPlayer); // four consecutive black pieces
//            System.out.println("x = " + i);
//        }
//        board.setCell(5, 0, Empty); // empty space
//
//        int score = evaluator.evaluateBoard(blackPlayer);
//        assertEquals("Score should reflect open four", Evaluator.getOpenFour(), score);
//    }
//
//    @Test
//    public void testHalfOpenFour() {
//        // Set up a half-open four pattern
//        board.setCell(0, 0, Empty); // empty space
//        for (int i = 1; i <= 4; i++) {
//            board.setCell(i, 0, blackPlayer); // four consecutive black pieces
//        }
//        board.setCell(5, 0, whitePlayer); // blocked by white player
//
//        int score = evaluator.evaluateBoard(blackPlayer);
//        assertEquals("Score should reflect half-open Four", Evaluator.getHalfOpenFour(), score);
//    }
//
//    @Test
//    public void testOpenThree() {
//        // Set up an open three pattern
//        board.setCell(0, 0, Empty); // empty space
//        for (int i = 1; i <= 3; i++) {
//            board.setCell(i, 0, blackPlayer); // three consecutive black pieces
//            System.out.println("x = " + i);
//        }
//        board.setCell(4, 0, Empty); // empty space
//
//        int score = evaluator.evaluateBoard(blackPlayer);
//        assertEquals("Score should reflect open three", Evaluator.getOpenThree(), score);
//    }
//
//    @Test
//    public void testHalfOpenThree() {
//        // Set up a half-open three pattern
//        board.setCell(0, 0, Empty); // empty space
//        for (int i = 1; i <= 3; i++) {
//            board.setCell(i, 0, blackPlayer); // three consecutive black pieces
//            System.out.println("x = " + i);
//        }
//        board.setCell(4, 0, whitePlayer); // blocked by white player
//
//        int score = evaluator.evaluateBoard(blackPlayer);
//        assertEquals("Score should reflect half-open three", Evaluator.getHalfOpenThree(), score);
//    }
//
//    @Test
//    public void testOpenTwo() {
//        // Set up an open three pattern
//        board.setCell(0, 0, Empty); // empty space
//        for (int i = 1; i <= 2; i++) {
//            board.setCell(i, 0, blackPlayer); // three consecutive black pieces
//            System.out.println("x = " + i);
//        }
//        board.setCell(3, 0, Empty); // empty space
//
//        int score = evaluator.evaluateBoard(blackPlayer);
//        assertEquals("Score should reflect open three", Evaluator.getOpenTwo(), score);
//    }
//
//    @Test
//    public void testHalfOpenTwo() {
//        // Set up a half-open three pattern
//        board.setCell(0, 0, Empty); // empty space
//        for (int i = 1; i <= 2; i++) {
//            board.setCell(i, 0, blackPlayer); // three consecutive black pieces
//            System.out.println("x = " + i);
//        }
//        board.setCell(3, 0, whitePlayer); // blocked by white player
//
//        int score = evaluator.evaluateBoard(blackPlayer);
//        assertEquals("Score should reflect half-open three", Evaluator.getHalfOpenTwo(), score);
//    }
//
///*    @Test
//    public void testSplitThree() {
//        // Set up a split three pattern
//        board.setCell(0, 0, blackPlayer);
//        board.setCell(1, 0, null); // gap
//        board.setCell(2, 0, blackPlayer);
//        board.setCell(3, 0, blackPlayer);
//        board.setCell(4, 0, null); // outside the pattern
//
//        int score = evaluator.evaluateBoard(blackPlayer);
//        assertEquals("Score should reflect split three", Evaluator.getSplitThree(), score);
//    }*/
//
//   /* @Test
//    public void testSplitTwo() {
//        // Set up a split two pattern
//        board.setCell(0, 0, blackPlayer);
//        board.setCell(1, 0, null); // gap
//        board.setCell(2, 0, blackPlayer);
//        board.setCell(3, 0, null); // outside the pattern
//
//        int score = evaluator.evaluateBoard(blackPlayer);
//        assertEquals("Score should reflect split two", Evaluator.getSplitTwo(), score);
//    }*/
//
//    @Test
//    public void testEvaluateBoardForAll() {
//        // Set up a board state that should result in a specific score
//        // Example: Placing a sequence of pieces on the board
//        board.setCell(0, 0, blackPlayer);
//        board.setCell(1, 0, blackPlayer);
//        board.setCell(2, 0, blackPlayer);
//
//        // Calculate the total score for the black player
//        int totalScore = evaluator.evaluateBoardForAll(blackPlayer);
//
//        // Assert that the total score is as expected
//        int expectedScore = 600;
//        assertEquals("Total score should match the expected value for the given board state", expectedScore, totalScore);
//    }
//
//    @Test
//    public void testEmptyBoard() {
//        int totalScore = evaluator.evaluateBoardForAll(blackPlayer);
//        assertEquals("Total score should be zero for an empty board", 0, totalScore);
//    }
//}
