package Game;

import GameSetup.Board;
import GameSetup.GameType;
import GameSetup.Location;

import java.util.Iterator;

/**
 * the workings of the game, ie what happens when a location is right clicked, or left clicked is
 * coded here.
 *
 * When a location is right clicked, there are two possibilities:
 * 1) it is a mine in which case the game is over and all the mines are revealed.
 * 2) it isn't a mine, in which case
 *      i) the Location revealed and
 *      ii) there is a cascade in which all its adjacent locations are revealed. subsequently, all the Locations
 *      adjacent to Locations with an adjacent mine number of 0 (ie, they have no mines adjacent to them) are revealed,
 *      ad infinitum.
 *
 * when a location is left clicked it is flagged.
 */

public class GameEngine {
    private Board board;
    private Stats stats;
    private boolean gameOver;

    public GameEngine(GameType difficulty) {
        board = new Board(difficulty);
        stats = new Stats(board);
        gameOver = false;
    }

    /**
     * @return the board the game is working with.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Is the game over or not
     * @return true if the game is over. else false
     */
    public boolean gameOver() {
        return gameOver;
    }

    /**
     * Location is right clicked. Either set gameOver if the location is a mine, or reveal all the Locations
     * that need to be revealed if it isn't
     * @param x x coordinate of clicked location
     * @param y y coordinate
     */
    public void leftClick(int x, int y) {

        Location location = board.getLocation(x,y);
        if (location.isMine()) {
            gameOver = true;
        }
        else {
            revealLocations(location);
        }
    }

    /**
     * toggle the flag of the location when left clicked
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void rightClick(int x, int y) {
        Location location = board.getLocation(x,y);
        location.toggleFlag();
        location.setChanged(true);
    }

    /**
     * reveal a location and then if it has no adjacent mines, reveal its adjacent locations and cascade.
     * @param location the location to be revealed
     */
    private void revealLocations(Location location) {
        if (!location.isRevealed() && !location.isFlagged()) {
            location.reveal();
            location.setChanged(true);
            if (location.getAdjacentMines() == 0) {
                Iterator<Location> adjacentLocationIt = board.getAdjacentLocations(location).iterator();
                while (adjacentLocationIt.hasNext()) {
                    Location location1 = adjacentLocationIt.next();
                    revealLocations(location1);
                }
            }
        }
    }

    /**
     * @return string with all the stats.
     */
    public String getStatsString() {
        return stats.getStatsString();
    }

}