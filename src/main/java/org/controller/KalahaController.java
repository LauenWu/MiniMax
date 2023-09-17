package org.controller;

import org.games.KalahaGame;
import org.solver.MiniMaxSolver;

public class KalahaController extends Controller<KalahaGame> {

    public KalahaController() {
        super(new KalahaGame(0), 5);
    }

    @Override
    public void humanMove(int move) {
        game.singleMove(move);
    }

    @Override
    public void computerMove(int move) {
        MiniMaxSolver.Result result = solver.minimiser(game.getCurrentState());
        game.makeMove(result.bestMoveId());
    }
}
