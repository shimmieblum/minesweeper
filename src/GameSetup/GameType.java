package GameSetup;

import javafx.util.Pair;

public enum GameType {
    BEGINNER(8, 10), INTERMEDIATE(16,40 ), EXPERT(24,99), JOKE(1,0);

    private Pair<Integer, Integer> boardDimensions;
    private int mines;

    GameType(int size, int mines ) {
        boardDimensions = new Pair<>(size, size);
        this.mines = mines;
    }



    public int getMines() {
        return mines;
    }

    public Pair<Integer, Integer> getDimensions() {
        return boardDimensions;
    }

    public int getRows() { return boardDimensions.getKey(); }

    public int getColumns() { return boardDimensions.getValue(); }
}
