package org.games;

import org.state.ConnectFourState;
import org.state.GameState;

public class ConnectFourGame implements Game<ConnectFourState> {

    private ConnectFourState currentState;

    public ConnectFourGame(int currentPlayer) {
        this.currentState = new ConnectFourState(currentPlayer);
    }
    @Override
    public void makeMove(int move) {
        this.currentState = this.currentState.getChildren().get(move);
    }

    public void humanMove(int field) {
        // TODO: finish implementations
//        if (currentState.isLeaf()) {
//            throw new RuntimeException("Can't make move. Game is Over.");
//        }
//        for (ConnectFourState child : currentState.getChildren()) {
//            child.
//            currentState = currentState.(field);
//        }
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
    public ConnectFourState getCurrentState() {
        return this.currentState;
    }
}
