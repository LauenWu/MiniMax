package org.state;

import java.util.List;

public interface GameState {
    /**
     * @return List of child game states that can be reached from the current state within one move.
     * The order of states has to be consistent between calls.
     */
    List<? extends GameState> getChildren();

    /**
     * @return true if the current state does not have any children.
     */
    boolean isLeaf();

    /**
     * Returns an integer that evaluates the score of the game state. The aim of the maximising player is to increase
     * this score and the aim of the minimising player is to decrease it.
     * @return score of the current game state.
     */
    int evaluate();
}
