package GameSetup;

/**
 * a location in the game. contains its x & y coordinate, whether it contains a mine and is flagged.
 * it also contains an integer of how many mines are in adjacent locations.
 *
 * @author Shimmie Blum
 * @version 01
 */
public class Location {
    int xCoord;
    private int yCoord;
    private boolean isMine;
    // whether this square is hidden still or not.
    private boolean isRevealed;
    private boolean flagged;
    private int adjacentMines;
    private boolean changed;

    public Location(int x, int y) {
        xCoord = x;
        yCoord = y;
        isMine = false;
        isRevealed = false;
        flagged = false;
        adjacentMines = 0;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void reveal() {
        isRevealed = true;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setAsMine() {
        isMine = true;
    }

    public boolean isMine() {
        return isMine;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int value) {
        adjacentMines = value;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void toggleFlag() {
        flagged = !flagged;
    }

    /**
     * set the status of changed boolean so the game can be aware whether the
     * related label needs updating.
     * @param status true if this location has been changed, false if it hasn't.
     */
    public void setChanged(boolean status) {
        changed = status;
    }

    public boolean isChanged() {
        return changed;
    }
}
