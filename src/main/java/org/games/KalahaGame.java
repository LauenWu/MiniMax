package org.games;

import org.state.KalahaState;

public class KalahaGame implements Game<KalahaState> {
    private KalahaState currentState;

    public KalahaGame(int currentPlayer) {
        this.currentState = new KalahaState(currentPlayer);
    }

    @Override
    public String toString() {
        return currentState.toString();
    }

    @Override
    public void makeMove(int move) {
        if (currentState.isLeaf()) {
            throw new RuntimeException("Can't make move on leaf node");
        }

        currentState = currentState.getChildren().get(move);
        System.out.println("made moves: " + currentState.getMoves());
    }

    @Override
    public int getCurrentPlayer() {
        return currentState.getPlayer();
    }

    @Override
    public boolean gameOver() {
        return currentState.isLeaf();
    }

    @Override
    public KalahaState getCurrentState() {
        return this.currentState;
    }

    public void singleMove(int field) {
        if (currentState.isLeaf()) {
            throw new RuntimeException("Can't make move. Game is Over.");
        }
        currentState = currentState.manualMove(field);
    }
}
