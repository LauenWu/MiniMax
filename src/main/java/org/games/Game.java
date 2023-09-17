package org.games;

import org.state.GameState;

public interface Game<S extends GameState> {
    String toString();

    /**
     * Transitioins to one of the child states.
     * @param move The passed integer corresponds to the index of the child state in <code>getChildren()</code>
     *             the move transitions to.
     * @throws RuntimeException When <code>isLeaf()</code> returns true
     */
    void makeMove(int move);

    /**
     * @return Either 0 or 1 depending on which player's turn it is.
     */
    int getCurrentPlayer();

    boolean gameOver();

    S getCurrentState();
}
