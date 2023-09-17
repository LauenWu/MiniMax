package org.solver;

import org.state.GameState;

public class MiniMaxSolver {
    private int level;

    public record Result(int bestScore, int bestMoveId) {}

    public MiniMaxSolver(int level) {
        this.level = level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Result minimiser(GameState state) {
        if (state.isLeaf()) {
            return new Result(state.evaluate(), 0);
        }
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int moveId = 0;
        int bestMoveId = moveId;
        int bestScore = Integer.MAX_VALUE;
        for (GameState child : state.getChildren()) {
            int score = maximiser(child, this.level-1, alpha, beta);
            if (score < bestScore) {
                bestScore = score;
                bestMoveId = moveId;
            }
            beta = Math.min(beta, score);
            if (beta <= alpha) break;
            moveId++;
        }
        return new Result(bestScore, bestMoveId);
    }

    public Result maximiser(GameState game) {
        if (game.isLeaf()) {
            return new Result(game.evaluate(), 0);
        }
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int moveId = 0;
        int bestMoveId = moveId;
        int bestScore = Integer.MIN_VALUE;
        for (GameState child : game.getChildren()) {
            int score = minimiser(child, this.level-1, alpha, beta);
            if (score > bestScore) {
                bestScore = score;
                bestMoveId = moveId;
            }
            alpha = Math.max(alpha, score);
            if (beta <= alpha) break;
            moveId++;
        }
        return new Result(bestScore, bestMoveId);
    }

    public int minimiser(GameState game, int level, int alpha, int beta) {
        if (game.isLeaf() || level == 0) {
            return game.evaluate();
        }
        int bestScore = Integer.MAX_VALUE;
        for (GameState child : game.getChildren()) {
            int score = maximiser(child, level-1, alpha, beta);
            bestScore = Math.min(score, bestScore);
            beta = Math.min(beta, score);
            if (beta <= alpha) break;
        }
        return bestScore;
    }

    public int maximiser(GameState game, int level, int alpha, int beta) {
        if (game.isLeaf() || level == 0) {
            return game.evaluate();
        }
        int bestScore = Integer.MIN_VALUE;
        for (GameState child : game.getChildren()) {
            int score = minimiser(child, level-1, alpha, beta);
            bestScore = Math.max(score, bestScore);
            alpha = Math.max(alpha, score);
            if (beta <= alpha) break;
        }
        return bestScore;
    }
}
