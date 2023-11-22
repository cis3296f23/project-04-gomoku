package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;

public class AI {
    private Evaluator evaluator;
    private Board board;
    private Player aiPlayer;
    private Player humanPlayer;

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

        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                // Check if the cell is empty
                if (board.isEmpty(x, y)) {
                    // Make a temporary move
                    board.setCell(x, y, aiPlayer);

                    // Evaluate this move
                    int score = evaluator.evaluateBoard(aiPlayer);

                    // Undo the move
                    board.setCell(x, y, null);

                    // Update the best move if this move is better than the current best
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Move(x, y);
                    }
                }
            }
        }

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
