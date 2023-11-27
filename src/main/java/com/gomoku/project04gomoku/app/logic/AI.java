package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;

import static com.gomoku.project04gomoku.app.logic.Evaluator.FIVE_IN_ROW;

public class AI {
    private final Evaluator evaluator;
    private final Board board;
    private final Player aiPlayer;
    private final Player humanPlayer;

    public AI(Board board, Player aiPlayer, Player humanPlayer) {
        this.board = board;
        this.aiPlayer = aiPlayer;
        this.humanPlayer = humanPlayer;
        this.evaluator = new Evaluator(board);
    }

    // Method to find the best move for the AI
    public Move findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = new Move(-1, -1);

        // Iterate through all possible moves
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                if (board.isEmpty(x, y)) {
                    // Simulate AI move
                    board.setCell(x, y, aiPlayer);
                    int scoreAI = evaluator.evaluateBoard(aiPlayer);

                    // Undo AI move and simulate opponent's move
                    board.setCell(x, y, humanPlayer);
                    int scoreHuman = evaluator.evaluateBoard(humanPlayer);

                    // Reset the cell to its original state
                    board.setCell(x, y, null);

                    // Output current position and corresponding scores
                    System.out.println("Position: (" + x + ", " + y + ") - AI Score: " + scoreAI + ", Human Score: " + scoreHuman);

                    // Consider a defensive play
                    int score = scoreAI - scoreHuman;
                    if (scoreHuman > FIVE_IN_ROW / 2) {
                        score += scoreHuman;  // Boost the opponent's score if they are close to winning
                    }

                    // Select the best move
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Move(x, y);
                    }
                }
            }
        }

        System.out.println("Best Move: (" + bestMove.x + ", " + bestMove.y + ") with Score: " + bestScore);
        return bestMove;
    }

    // Inner class to represent a move
    public static class Move {
        public int x, y;

        public Move(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
