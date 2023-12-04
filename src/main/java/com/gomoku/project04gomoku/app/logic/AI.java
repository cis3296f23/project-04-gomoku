package com.gomoku.project04gomoku.app.logic;

import com.gomoku.project04gomoku.app.models.Board;

import javax.net.ssl.SSLContext;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class AI {
    //private final Evaluator evaluator;
    private final Board board;
    private final Player aiPlayer;
    private final Player humanPlayer;
    int depth;

    public AI(Board board, Player aiPlayer, Player humanPlayer, int depth) {
        this.board = board;
        this.aiPlayer = aiPlayer;
        this.humanPlayer = humanPlayer;
        this.depth = depth;
        //this.evaluator = new Evaluator(board);
    }

    // Method to find the best move for the AI
    public Move findBestMove() {
        //Evaluator.printBoard(board);  //DEBUG
        //System.out.println(""); //DEBUG
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = new Move(-1, -1);
        bestMove = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return bestMove;
    }

    public Move minimax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        // Gets available moves
        ArrayList<Move> emptyCells = getEmptyCells();
        // Sort moves based on a heuristic (e.g., proximity to the center)
        Collections.sort(emptyCells, Comparator.comparingInt(m -> Math.abs(m.x - 15/2) + Math.abs(m.y - 15/2)));
        // Terminate conditions
        if (depth == 0 || emptyCells.isEmpty()) {
            //int s = evaluator.evaluateBoard(board, aiPlayer) - evaluator.evaluateBoard(board, humanPlayer);
            int aiPoint = Evaluator.evaluateBoard(board, aiPlayer);
            int humanPoint = Evaluator.evaluateBoard(board, humanPlayer);
            int s = aiPoint - humanPoint;
            return new Move(-1, -1, s);
        }

        // Initialize best move
        Move bestMove = new Move(-1, -1);

        // for maximize move
        if (maximizingPlayer) {
            // Initialize the node to -inf
            bestMove.score = Integer.MIN_VALUE;
            // Iterate each empty cell
            for (Move m: emptyCells) {
                // Simulate landing a cell
                board.getBoard()[m.x][m.y] = aiPlayer;
                // Get the result at the end starting from the current cell.
                Move eval = minimax(board, depth-1, alpha, beta, false);
                // Undo the move
                board.getBoard()[m.x][m.y] = null;
                // Update the move if the current cell lead to a better result
                if (eval.score > bestMove.score) {
                    bestMove.x = m.x;
                    bestMove.y = m.y;
                    bestMove.score = eval.score;
                }

                // Find the highest score among the paths
                alpha = Math.max(alpha, eval.score);
                // a-b pruning, return immediately as the beta is already lower than the alpha
                // No need to go deeper sub-tree as the opponent will absolutely pick the returned move.
                // Which is lower than the other parallel branch
                if (beta <= alpha) {
                    break;
                }
            }
            return bestMove;
        }
        // for minimize move
        else {
            bestMove.score = Integer.MAX_VALUE;
            for (Move m: emptyCells) {
                // Simulate landing a cell
                board.getBoard()[m.x][m.y] = humanPlayer;
                // Get the result at the end starting from the current cell.
                Move eval = minimax(board, depth-1, alpha, beta, true);
                // Undo the move
                board.getBoard()[m.x][m.y] = null;

                if (eval.score < bestMove.score) {
                    bestMove.x = m.x;
                    bestMove.y = m.y;
                    bestMove.score = eval.score;
                }

                // Find the lowest score among the paths
                beta = Math.min(beta, eval.score);
                // a-b pruning, return immediately as the beta is already lower than the alpha
                // No need to go deeper sub-tree as the opponent will absolutely pick the returned move.
                // Which is lower than the other parallel branch.
                if (beta <= alpha) {
                    break;
                }
            }
            return bestMove;
        }
    }

    private ArrayList<Move> getEmptyCells() {
        ArrayList<Move> moves = new ArrayList<>();
        // for each row
        for (int row = 0; row < board.SIZE; row++) {
            // for each col
            for (int col = 0; col < board.SIZE; col++) {
                // Get null board[row][col]
                if (board.getCell(row, col) == null) {
                    moves.add(new Move(row, col));
                }
            }
        }
        return moves;
    }

    // Inner class to represent a move
    public class Move {
        public int x, y, score;

        public Move(int x, int y, int score) {
            this.x = x;
            this.y = y;
            this.score = score;
        }
        public Move(int x, int y) {
            this.x = x;
            this.y = y;
            this.score = 0;
        }
    }

}
