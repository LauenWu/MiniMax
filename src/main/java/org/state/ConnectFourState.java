package org.state;

import java.util.ArrayList;
import java.util.List;

public class ConnectFourState implements GameState {
    private final static int VICTORY = 500;
    private final static int[] scores = {0,0,15,30,VICTORY,VICTORY,VICTORY,VICTORY};
    private final static byte ROWS = 6;
    private final static byte COLS = 7;
    private final int player; // must be -1 or 1
    private final boolean gameOver;

    public ConnectFourState(int player) {
        this.occupied = new byte[COLS];
        this.moves = new byte[0];
        this.player = player;
        this.score = 0;
        this.gameOver = false;
    }

    public int getPlayer() {
        return this.player;
    }

    public ConnectFourState(byte[] moves, byte move, int player) {
        this.occupied = new byte[COLS];
        this.moves = new byte[moves.length + 1];
        System.arraycopy(moves, 0, this.moves, 0, moves.length);
        this.moves[moves.length] = move;
        this.player = player;

        byte[][] fields = buildFields(occupied);

        this.score = calcScore(fields, occupied[move], move);
        if (this.score == VICTORY || isFull()) {
            this.gameOver = true;
        } else {
            this.gameOver = false;
        }
    }

    private final byte[] occupied; // for each col, contains the number of occupied rows
    private final byte[] moves; // moves that led to the current state
    private List<ConnectFourState> children;
    private final int score;

    private byte getLatestMove() {
        return this.moves[this.moves.length - 1];
    }

    @Override
    public List<ConnectFourState> getChildren() {
        if (this.children != null) {
            return this.children;
        }
        this.children = new ArrayList<>();

        for (byte col = 0; col < COLS; col++) {
            if (occupied[col] < ROWS) {
                this.children.add(new ConnectFourState(this.moves, col, -this.player));
            }
        }
        return this.children;
    }

    @Override
    public boolean isLeaf() {
        return this.gameOver;
    }

    @Override
    public int evaluate() {
        return this.score;
    }

    @Override
    public String toString() {
        byte[][] fields = buildFields(new byte[COLS]);
        StringBuilder sb = new StringBuilder();
        for (byte row = ROWS; row > 0; row--) {
            for (byte col = 0; col < COLS; col++) {
                byte value = fields[row][col];
                if (value == 0) {
                    sb.append("  ");
                } else if (value == -1) {
                    sb.append("o ");
                } else {
                    sb.append("x ");
                }
            }
        }
        sb.append("--------------");
        return sb.toString();
    }

    private int calcScore(byte[][] field, byte zeile, byte spalte){
        int score = horizontal(field, zeile, spalte);
        if(score >= VICTORY) return VICTORY;

        score += diag1(field, zeile, spalte);
        if(score >= VICTORY) return VICTORY;

        score += diag2(field, zeile, spalte);
        if(score >= VICTORY) return VICTORY;

        score += runter(field, zeile, spalte);
        if(score >= VICTORY) return VICTORY;

        return player * score;
    }

    private int horizontal(byte[][] field, byte zeile, byte spalte) {
        int maxPunkte = 0;
        int start = 0;
        boolean stop = false;
        int j = 0;
        while(!stop && j < 3) {
            try {
                j ++;
                byte feld = field[zeile][spalte-j];
                if(feld == player) {
                    start = j;
                }else if(feld != 0) {
                    throw new RuntimeException();
                }
            } catch(RuntimeException e) {
                stop = true;
            }
        }

        stop = false;

        while(!stop) {
            int i = 0;
            int punkte = 0;
            while(!stop && i < 4) {
                try {
                    byte feld = field[zeile][spalte-start+i];
                    if(feld == player) {
                        punkte ++;
                    } else if(feld != 0) {
                        throw new RuntimeException();
                    }
                    i++;
                } catch(RuntimeException e) {
                    stop = true;
                }
            }
            if(punkte > maxPunkte) {
                maxPunkte = punkte;
            }
            start++;
        }
        return scores[maxPunkte]+10;
    }

    private int diag1(byte[][] field, byte zeile, byte spalte) {
        int maxPunkte = 0;
        int start = 0;
        boolean stop = false;
        int j = 0;
        while(!stop && j < 3) {
            try {
                j ++;
                byte feld = field[zeile-j][spalte-j];
                if(feld == player) {
                    start = j;
                }else if(feld != 0) {
                    throw new RuntimeException();
                }
            } catch(RuntimeException e) {
                stop = true;
            }
        }

        stop = false;

        while(!stop) {
            int i = 0;
            int punkte = 0;
            while(!stop && i < 4) {
                try {
                    byte feld = field[zeile-start+i][spalte-start+i];
                    if(feld == player) {
                        punkte ++;
                    } else if(feld != 0) {
                        throw new RuntimeException();
                    }
                    i++;
                } catch(RuntimeException e) {
                    stop = true;
                }
            }
            if(punkte > maxPunkte) {
                maxPunkte = punkte;
            }
            start++;
        }
        return scores[maxPunkte];
    }

    private int diag2(byte[][] field, byte zeile, byte spalte) {
        int maxPunkte = 0;
        int start = 0;
        boolean stop = false;
        int j = 0;
        while(!stop && j < 3) {
            try {
                j ++;
                byte feld = field[zeile+j][spalte-j];
                if(feld == player) {
                    start = j;
                }else if(feld != 0) {
                    throw new RuntimeException();
                }
            } catch(RuntimeException e) {
                stop = true;
            }
        }

        stop = false;

        while(!stop) {
            int i = 0;
            int punkte = 0;
            while(!stop && i < 4) {
                try {
                    byte feld = field[zeile+start-i][spalte-start+i];
                    if(feld == player) {
                        punkte ++;
                    } else if(feld != 0) {
                        throw new RuntimeException();
                    }
                    i++;
                } catch(RuntimeException e) {
                    stop = true;
                }
            }
            if(punkte > maxPunkte) {
                maxPunkte = punkte;
            }
            start++;
        }
        return scores[maxPunkte];
    }

    private int runter(byte[][] field, byte zeile, byte spalte) {
        int punkte = 0;

        for(int i = 0; i < 4; i++) {
            try {
                byte feld = field[zeile--][spalte];
                if(feld == player) {
                    punkte++;
                } else {
                    break;
                }
            } catch(RuntimeException e) {
                break;
            }
        }

        return scores[punkte];
    }

    private boolean isFull() {
        for (byte col = 0; col < COLS; col++) {
            if (this.occupied[col] < ROWS) {
                return false;
            }
        }
        return true;
    }

    private byte[][] buildFields(byte[] occupied) {
        int player;
        if (this.moves.length % 2 == 0) {
            player = this.player;
        } else {
            player = -this.player;
        }
        byte[][] fields = new byte[ROWS][COLS];
        for (byte m : moves) {
            fields[occupied[m]++][m] = (byte) player;
            player = -player;
        }
        return fields;
    }
}
