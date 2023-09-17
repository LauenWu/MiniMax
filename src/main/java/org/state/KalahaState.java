package org.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KalahaState implements GameState {
    private int[] fields;
    private int player;
    private List<KalahaState> children;
    private Boolean gameOver;
    private List<Integer> moves; // moves that led from parent to this state
    private static final byte FIELD_NUM = 7; // number of fields per player including Kalahas
    private static final byte TOTAL_FIELD_NUM = 2*FIELD_NUM;

    // the index of the Kalaha fields of the corresponing player
    private static final int[] KALAHA = new int[]{FIELD_NUM-1, 2*FIELD_NUM-1};
    private static final int[] OPPOSITE_FIELDS = new int[]{12,11,10,9,8,7,13,5,4,3,2,1,0,6};
    private static final int[] STARTS = {0,FIELD_NUM};
    private static final int[] ENDS = {FIELD_NUM-1,TOTAL_FIELD_NUM-1};

    public KalahaState(int currentPlayer) {
        this.player = currentPlayer;
        this.fields = new int[TOTAL_FIELD_NUM];
        for (int i : playerFields(0, false)) {
            this.fields[i] = 4;
        }
        for (int i : playerFields(1, false)) {
            this.fields[i] = 4;
        }
    }

    public List<Integer> getMoves() {
        return this.moves;
    }

    public int[] getFields() {
        return this.fields;
    }

    public void setFields(int[] fields) {
        this.fields = fields;
    }

    public int getPlayer() {
        return this.player;
    }

    private boolean checkEmptySide(int player) {
        for (int field : playerFields(player, false)) {
            if (this.fields[field] != 0) return false;
        }
        return true;
    }

    /**
     * Empties all fields of a player's side and put the stones into his Kalaha
     * @param player Identifies the player. Must be 0 or 1.
     */
    private void flushSide(int player) {
        int sum = 0;
        for (int field : playerFields(player, false)) {
            sum += this.fields[field];
            this.fields[field] = 0;
        }

        fields[KALAHA[player]] += sum;
    }

    @Override
    public List<KalahaState> getChildren() {
        if (this.children != null) {
            return this.children;
        } else {
            this.children = new ArrayList<>();
        }
        for (int move : playerFields(this.player, false)) {
            if (this.fields[move] <= 0) {
                continue;
            }

            KalahaState child = simulateMove(move);
            if (child.player == this.player) {
                child.getChildren().forEach(c -> c.getMoves().add(0, child.getMoves().get(0)));
                this.children.addAll(child.getChildren());
            } else {
                // intermediate moves are not considered children. To change that remove the else to always add child
                this.children.add(child);
            }
        }
        return this.children;
    }

    @Override
    public boolean isLeaf() {
        if (this.gameOver != null) {
            return this.gameOver;
        }

        if (checkEmptySide(this.player)) {
            this.gameOver = true;
            return true;
        }

        if (checkEmptySide(otherPlayer())) {
            this.gameOver = true;
            return true;
        }

        this.gameOver = this.getChildren().isEmpty();
        return this.gameOver;
    }

    @Override
    public int evaluate() {
        return this.fields[KALAHA[0]] - this.fields[KALAHA[1]];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("+--+--+--+--+--+--+--+--+\n|  |");
        for (int field : playerFields(1, true)) {
            sb.append(paddedFieldString(this.fields[field]));
            sb.append("|");
        }
        sb.append("  |");
        if (this.player == 1) sb.append("<");
        sb.append("\n|");
        sb.append(paddedFieldString(this.fields[KALAHA[1]]));
        sb.append("+--+--+--+--+--+--+");
        sb.append(paddedFieldString(this.fields[KALAHA[0]]));
        sb.append("|\n");
        sb.append("|  |");
        for (int field : playerFields(0, false)) {
            sb.append(paddedFieldString(this.fields[field]));
            sb.append("|");
        }
        sb.append("  |");
        if (this.player == 0) sb.append("<");
        sb.append("\n+--+--+--+--+--+--+--+--+\n\n");
        return sb.toString();
    }

    private String paddedFieldString(int score) {
        String scoreString;
        if (score < 10) {
            scoreString = " ";
            if (score == 0) return scoreString + " ";
            return scoreString + score;
        }
        return String.valueOf(score);
    }

    private Iterable<Integer> playerFields(int player, boolean reversed) {
        int start, end;
        if (reversed) {
            start = ENDS[player] - 1;
            end = STARTS[player] - 1;
        } else {
            start = STARTS[player];
            end = ENDS[player];
        }

        return () -> new Iterator<>() {
            private int i = start;

            @Override
            public boolean hasNext() {
                return reversed ? i > end : i < end;
            }

            @Override
            public Integer next() {
                return reversed ? i-- : i++;
            }
        };
    }

    public KalahaState manualMove(int move) {
        if(!isOwnField(move)) {
            throw new RuntimeException("Move is on opponent's field");
        }
        return simulateMove(move);
    }

    private KalahaState simulateMove(int move) {
        KalahaState child = new KalahaState(otherPlayer());
        child.fields = copyFields();
        child.moves = new ArrayList<>();
        child.moves.add(move);

        int take = child.fields[move];
        child.fields[move] = 0;
        if (take <= 0) {
            throw new RuntimeException("took " + take + " stones from field " + move);
        }

        int field_pointer = move;
        while (take > 0) {
            field_pointer = (field_pointer + 1) % TOTAL_FIELD_NUM;
            if (field_pointer == KALAHA[child.player]) {
                // can't put stones on the other player's Kalaha
                continue;
            }

            child.fields[field_pointer]++;
            take--;
        }

        boolean onKalaha = field_pointer == KALAHA[this.player];

        // check if last move was on an empty field
        if (!onKalaha && child.fields[field_pointer] == 1) {
            // check if last move was on own field
            if (field_pointer >= STARTS[this.player] && field_pointer < ENDS[this.player]) {
                take = child.fields[OPPOSITE_FIELDS[field_pointer]];
                child.fields[OPPOSITE_FIELDS[field_pointer]] = 0;
                child.fields[KALAHA[this.player]] += take;
            }
        }

        if (child.checkEmptySide(this.player)) {
            child.flushSide(child.player);
            child.gameOver = true;
        }

        if (child.checkEmptySide(child.player)) {
            child.flushSide(this.player);
            child.gameOver = true;
        }

        boolean thisWon = child.fields[KALAHA[this.player]] > 24;
        boolean otherWon = child.fields[KALAHA[child.player]] > 24;
        if (thisWon || otherWon) {
            // if a player has more than half the stones, he won.
            child.flushSide(this.player);
            child.flushSide(child.player);

            if (thisWon) {
                child.fields[KALAHA[this.player]] = 48;
                child.fields[KALAHA[child.player]] = 0;
            } else {
                child.fields[KALAHA[child.player]] = 48;
                child.fields[KALAHA[this.player]] = 0;
            }

            child.gameOver = true;
        } else if (onKalaha) {
            // check if last move was on own Kalaha. In that case, the current player can move again
            child.player = this.player;
        }

        return child;
    }

    private int otherPlayer() {
        return 1 - this.player;
    }

    private int[] copyFields() {
        int[] copyState = new int[this.fields.length];
        System.arraycopy(this.fields, 0, copyState, 0, this.fields.length);
        return copyState;
    }

    private boolean isOwnField(int field) {
        return field >= STARTS[this.player] && field < ENDS[this.player];
    }
}
