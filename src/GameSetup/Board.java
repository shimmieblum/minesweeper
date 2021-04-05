package GameSetup;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * contains 2D array of Locations which is the game board.
 * when a board is created the correct number of locations will be set as mines, and numbers of
 * adjacent mines will be set for each Location.
 */
public class Board {
    private int noColumns;
    private int noRows;
    private Location[][] grid;
    // the number of mines in this game
    private int mineNumber;
    private Random mineGenerator;


    public Board (GameType difficulty) {
        noColumns= difficulty.getColumns();
        noRows = difficulty.getRows();
        // create 2D array of locations
        grid = new Location[noColumns][noRows];
        // number of mines in the game
        mineNumber = difficulty.getMines();
        mineGenerator = new Random();

        createGrid();
    }

    public int getNoColumns() {
        return noColumns;
    }

    public int getNoRows() {
        return noRows;
    }

    public int getMineNumber() {
        return mineNumber;
    }

    /**
     * return the Location at specific coordinates on the board. if the board doesn't
     * contain those coordinates return null
     * @param x x coordinate of the location
     * @param y y coordinate of the location
     */
    public Location getLocation(int x, int y) {
        if (isOnBoard(x,y)) {
            return grid[x][y];
        }
        return null;
    }

    /**
     * create the grid of Locations
     */
    private void createGrid()  {
        for(int x = 0; x < noColumns; x++) {
            for (int y = 0; y < noRows; y++) {
                grid[x][y] = new Location(x,y);
            }
        }
        setMines();
        setAdjacentMines();
    }

    /**
     * set some of the Locations as mines. using thee mine number derived from the difficulty.
     */
    public void setMines() {
        int unplacedMines = mineNumber;
        while (unplacedMines > 0 ) {
            int x = mineGenerator.nextInt(noColumns);
            int y = mineGenerator.nextInt(noRows);
            Location location = grid[x][y];

            if (!location.isMine()) {
                location.setAsMine();
                unplacedMines--;
            }
        }
    }

    /**
     * set the number of adjacent mines for each location.
     */
    private void setAdjacentMines() {
        for (int x = 0; x < noColumns; x++) {
            for (int y = 0; y < noRows; y++) {
                Location location = grid[x][y];
                location.setAdjacentMines(checkAdjacentMines(location));
            }
        }
    }

    /**
     * check if a particular coordinate is on the board
     * @param x x coordinate
     * @param y y coordinate
     * @return true if it is on the board, if not, false.
     */
    public boolean isOnBoard(int x, int y) {
        if(x >=0 && x < noColumns && y >=0 && y < noRows) {
            return true;
        }
        return false;
    }

    /**
     * find and return the number of mines adjacent to a particular location
     * @param location the location
     * @return the number of mines
     */
    private int checkAdjacentMines(Location location){
        List<Location> adjacentLocations = getAdjacentLocations(location);
        int mines = 0;
        for(Location location1: adjacentLocations) {
            if(location1.isMine()) {
                mines++;
            }
        }
        return mines;
    }

    /**
     * create a list of all the locations adjacent to a particular location. if this location is on the edge of the grid or in a corner it will have fewer.
     * @param location the location
     * @return a list of adjacent locations.
     */
    public List<Location> getAdjacentLocations(Location location){
        List<Location> adjacentLocations = new LinkedList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                // you don't want to include the location itself!
                if(! (i == 0 && j == 0)){
                    int x = location.getXCoord() + i;
                    int y = location.getYCoord() + j;
                    if (isOnBoard(x,y)) {
                        adjacentLocations.add(getLocation(x,y));
                    }
                }
            }
        }
        return adjacentLocations;
    }

    public List<Location> getLocationList() {
        List<Location> locations = new LinkedList<>();
        for (int x = 0; x < noColumns; x++) {
            for (int y = 0; y < noRows; y++) {
                locations.add(grid[x][y]);
            }
        }
        return locations;
    }
}
