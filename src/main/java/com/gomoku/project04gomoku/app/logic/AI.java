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

        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                if (board.isEmpty(x, y)) {
                    // 模拟 AI 落子
                    board.setCell(x, y, aiPlayer);
                    int scoreAI = evaluator.evaluateBoard(aiPlayer);

                    // 模拟对手落子
                    board.setCell(x, y, humanPlayer);
                    int scoreHuman = evaluator.evaluateBoard(humanPlayer);

                    // 撤销落子
                    board.setCell(x, y, null);

                    // 输出当前位置和对应的分数
                    System.out.println("Position: (" + x + ", " + y + ") - AI Score: " + scoreAI + ", Human Score: " + scoreHuman);

                    // 考虑防守
                    int score = scoreAI - scoreHuman;
                    if (scoreHuman > FIVE_IN_ROW / 2) {
                        score += scoreHuman;  // 如果对手即将获胜，增加对手的分数以促使 AI 防守
                    }

                    // 选择最佳移动
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
