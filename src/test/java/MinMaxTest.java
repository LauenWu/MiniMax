import org.solver.MiniMaxSolver;
import org.state.GameState;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinMaxTest {
    public static GameStateImpl state(int score) {
        return new GameStateImpl(score, null);
    }

    public static GameStateImpl state(GameState... children) {
        return new GameStateImpl(0, List.of(children));
    }

    private static class GameStateImpl implements GameState {
        private int score;
        private List<GameState> children;

        private GameStateImpl(int score, List<GameState> children) {
            this.score = score;
            this.children = children;
        }

        @Override
        public List<GameState> getChildren() {
            return this.children;
        }

        @Override
        public boolean isLeaf() {
            return this.children == null;
        }

        @Override
        public int evaluate() {
            return this.score;
        }
    }

    @Test
    public void t1() {
        GameState gameState = state(
                state(
                        state(
                                state(5),
                                state(2)
                        ),
                        state(1)
                ),
                state(
                        state(3),
                        state(
                                state(2),
                                state(4)
                        )
                )
        );

        MiniMaxSolver alg = new MiniMaxSolver(10);
        MiniMaxSolver.Result s = alg.minimiser(gameState);
        assertEquals(0, s.bestMoveId());
        assertEquals(2, s.bestScore());

        alg = new MiniMaxSolver(10);
        s = alg.maximiser(gameState);
        assertEquals(1, s.bestMoveId());
        assertEquals(3, s.bestScore());
    }

    @Test
    public void t2() {
        GameState gameState = state(
                state(
                        state(
                                state(-1),
                                state(3)
                        ),
                        state(
                                state(5),
                                state(1)
                        )
                ),
                state(
                        state(
                                state(-6),
                                state(-4)
                        ),
                        state(
                                state(0),
                                state(9)
                        )
                )
        );

        MiniMaxSolver alg = new MiniMaxSolver(10);
        MiniMaxSolver.Result s = alg.minimiser(gameState);
        assertEquals(1, s.bestMoveId());
        assertEquals(0, s.bestScore());

        alg = new MiniMaxSolver(10);
        s = alg.maximiser(gameState);
        assertEquals(0, s.bestMoveId());
        assertEquals(3, s.bestScore());
    }

    @Test
    public void t3() {
        GameState gameState = state(
                state(
                        state(
                                state(
                                        state(8),
                                        state(5)
                                ),
                                state(
                                        state(6),
                                        state(-4)
                                )
                        ),
                        state(
                                state(
                                        state(3),
                                        state(8)
                                ),
                                state(
                                        state(4),
                                        state(-6)
                                )
                        )
                ),
                state(
                        state(
                                state(
                                        state(1),
                                        state(-1000)
                                ),
                                state(
                                        state(5),
                                        state(2)
                                )
                        ),
                        state(-1001)
                )
        );

        MiniMaxSolver alg = new MiniMaxSolver(10);
        MiniMaxSolver.Result s = alg.maximiser(gameState);
        assertEquals(0, s.bestMoveId());
        assertEquals(3, s.bestScore());
    }
}
