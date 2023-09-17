import org.games.KalahaGame;
import org.solver.MiniMaxSolver;
import org.state.KalahaState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KalahaTest {
    @Test
    public void t1() {
        KalahaGame k = new KalahaGame(0);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 4| 4| 4| 4| 4| 4|  |
                        |  +--+--+--+--+--+--+  |
                        |  | 4| 4| 4| 4| 4| 4|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(2);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 4| 4| 4| 4| 4| 4|  |
                        |  +--+--+--+--+--+--+ 1|
                        |  | 4| 4|  | 5| 5| 5|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );
        assertEquals(0, k.getCurrentPlayer());

        assertThrows(
                RuntimeException.class,
                () -> k.singleMove(7)
        );
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 4| 4| 4| 4| 4| 4|  |
                        |  +--+--+--+--+--+--+ 1|
                        |  | 4| 4|  | 5| 5| 5|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        assertThrows(
                RuntimeException.class,
                () -> k.singleMove(10)
        );
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 4| 4| 4| 4| 4| 4|  |
                        |  +--+--+--+--+--+--+ 1|
                        |  | 4| 4|  | 5| 5| 5|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.makeMove(1);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 4| 4| 4| 4| 4| 4|  |<
                        |  +--+--+--+--+--+--+ 1|
                        |  | 4|  | 1| 6| 6| 6|  |
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(11);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 5|  | 4| 4| 4| 4|  |
                        | 1+--+--+--+--+--+--+ 1|
                        |  | 5| 1| 1| 6| 6| 6|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(0);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 5|  | 4| 4| 4| 4|  |<
                        | 1+--+--+--+--+--+--+ 1|
                        |  |  | 2| 2| 7| 7| 7|  |
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        for (int i = 0; i < 7; i++) {
            final int move = i;
            assertThrows(RuntimeException.class, () -> k.singleMove(move));
        }

        k.singleMove(7);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 5| 1| 5| 5| 5|  |  |
                        | 3+--+--+--+--+--+--+ 1|
                        |  |  |  | 2| 7| 7| 7|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(3);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 5| 1| 6| 6| 6| 1|  |<
                        | 3+--+--+--+--+--+--+ 2|
                        |  |  |  | 2|  | 8| 8|  |
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(12);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  |  | 1| 6| 6| 6| 1|  |
                        | 4+--+--+--+--+--+--+ 2|
                        |  | 1| 1| 3| 1| 8| 8|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.makeMove(5);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1| 2| 7| 7| 7| 2|  |<
                        | 4+--+--+--+--+--+--+ 3|
                        |  | 2| 1| 3| 1| 8|  |  |
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(11);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 2|  | 7| 7| 7| 2|  |<
                        | 5+--+--+--+--+--+--+ 3|
                        |  | 2| 1| 3| 1| 8|  |  |
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(7);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 2|  | 7| 8| 8|  |  |
                        | 5+--+--+--+--+--+--+ 3|
                        |  | 2| 1| 3| 1| 8|  |  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(1);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 2|  | 7| 8| 8|  |  |<
                        | 5+--+--+--+--+--+--+ 3|
                        |  | 2|  | 4| 1| 8|  |  |
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(12);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  |  |  | 7| 8| 8|  |  |
                        | 6+--+--+--+--+--+--+ 3|
                        |  | 3|  | 4| 1| 8|  |  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(2);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  |  |  | 7| 8| 8|  |  |
                        | 6+--+--+--+--+--+--+ 4|
                        |  | 3|  |  | 2| 9| 1|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(5);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  |  |  | 7| 8| 8|  |  |
                        | 6+--+--+--+--+--+--+ 5|
                        |  | 3|  |  | 2| 9|  |  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(4);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1| 1| 8| 9| 9| 1|  |<
                        | 6+--+--+--+--+--+--+ 6|
                        |  | 4|  |  | 2|  | 1|  |
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(7);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1| 1| 8| 9|10|  |  |
                        | 6+--+--+--+--+--+--+ 6|
                        |  | 4|  |  | 2|  | 1|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(0);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1| 1| 8| 9|  |  |  |<
                        | 6+--+--+--+--+--+--+16|
                        |  |  | 1| 1| 3| 1| 1|  |
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(12);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  |  | 1| 8| 9|  |  |  |<
                        | 7+--+--+--+--+--+--+16|
                        |  |  | 1| 1| 3| 1| 1|  |
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(11);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1|  | 8| 9|  |  |  |
                        | 7+--+--+--+--+--+--+16|
                        |  |  | 1| 1| 3| 1| 1|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(5);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1|  | 8| 9|  |  |  |
                        | 7+--+--+--+--+--+--+17|
                        |  |  | 1| 1| 3| 1|  |  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(3);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1|  | 8| 9|  |  |  |
                        | 7+--+--+--+--+--+--+18|
                        |  |  | 1| 1|  | 2| 1|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(5);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1|  | 8| 9|  |  |  |
                        | 7+--+--+--+--+--+--+19|
                        |  |  | 1| 1|  | 2|  |  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(4);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1|  | 8| 9|  |  |  |
                        | 7+--+--+--+--+--+--+20|
                        |  |  | 1| 1|  |  | 1|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(5);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1|  | 8| 9|  |  |  |
                        | 7+--+--+--+--+--+--+21|
                        |  |  | 1| 1|  |  |  |  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );

        k.singleMove(2);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  |  |  |  |  |  |  |  |<
                        |  +--+--+--+--+--+--+48|
                        |  |  |  |  |  |  |  |  |
                        +--+--+--+--+--+--+--+--+

                        """,
                k.toString()
        );
        assertThrows(
                RuntimeException.class,
                () -> k.singleMove(12)
        );
    }

    @Test
    public void t12() {
        KalahaState state = new KalahaState(0);
        state.setFields(new int[]{0,2,2,9,9,8,2,4,4,0,0,0,1,7});

        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1|  |  |  | 4| 4|  |
                        | 7+--+--+--+--+--+--+ 2|
                        |  |  | 2| 2| 9| 9| 8|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                state.toString()
        );
        state.manualMove(5);
    }

    @Test
    public void t2() {
        MiniMaxSolver solver = new MiniMaxSolver(5);
        KalahaGame game = new KalahaGame(0);

        game.singleMove(2);
        game.singleMove(1);

        game.makeMove(solver.minimiser(game.getCurrentState()).bestMoveId());

        game.singleMove(0);

        game.makeMove(solver.minimiser(game.getCurrentState()).bestMoveId());

        game.singleMove(2);
        game.singleMove(1);

        game.makeMove(solver.minimiser(game.getCurrentState()).bestMoveId());

        game.singleMove(0);

        game.makeMove(solver.minimiser(game.getCurrentState()).bestMoveId());

        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 1|  |  |  | 4| 4|  |
                        | 7+--+--+--+--+--+--+ 2|
                        |  |  | 2| 2| 9| 9| 8|  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                game.toString()
        );

        game.singleMove(5);
        assertEquals(
                """
                        +--+--+--+--+--+--+--+--+
                        |  | 2| 1| 1| 1| 5| 5|  |
                        | 7+--+--+--+--+--+--+ 3|
                        |  | 1| 2| 2| 9| 9|  |  |<
                        +--+--+--+--+--+--+--+--+

                        """,
                game.toString()
        );
    }
}
