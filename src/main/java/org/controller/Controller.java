package org.controller;

import org.games.Game;
import org.solver.MiniMaxSolver;

import java.util.Scanner;

public abstract class Controller<G extends Game> {
    protected final G game;
    protected final MiniMaxSolver solver;
    protected final Scanner scanner;
    protected final int beginningPlayer;

    // 0 is always the human player
    public Controller(G game, int level) {
        this.game = game;
        this.beginningPlayer = game.getCurrentPlayer();
        this.solver = new MiniMaxSolver(level);
        this.scanner = new Scanner(System.in);
    }

    public void play() {
        System.out.print(game);
        while (!game.gameOver()) {
            if (beginningPlayer == 0) {
                humanMove();
                if (game.gameOver()) {
                    return;
                }
                computerMove();
            } else {
                computerMove();
                if (game.gameOver()) {
                    return;
                }
                humanMove();
            }
        }
        System.out.println("Game Over");
    }

    private void humanMove() {
        while(game.getCurrentPlayer() == 0) {
            System.out.print("Input your move: ");
            String input = scanner.nextLine(); // blocking call
            humanMove(Integer.parseInt(input));
            System.out.print(game);
            if (game.gameOver()) {
                return;
            }
        }
    }

    private void computerMove() {
        MiniMaxSolver.Result result;
        if (beginningPlayer == 0) {
            result = solver.minimiser(game.getCurrentState());
        } else {
            result = solver.maximiser(game.getCurrentState());
        }
        computerMove(result.bestMoveId());
        System.out.print(game);
    }

    abstract void humanMove(int move);
    abstract void computerMove(int move);

    public void setLevel(int level) {
        this.solver.setLevel(level);
    }
}
