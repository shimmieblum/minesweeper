package Game;

import GameSetup.Board;

public class Stats {
    private int minesRemaining;

    public Stats (Board board) {
        minesRemaining = board.getMineNumber();
    }

    public void setMinesRemaining(int minesRemaining) {
        this.minesRemaining = minesRemaining;
    }

    public void decrementMinesRemaining() {
        minesRemaining--;
    }

    public int getMinesRemaining() {
        return minesRemaining;
    }

    public String getStatsString() {
        String statsString = "mines remaining: " + minesRemaining;
        return statsString;
    }
}
