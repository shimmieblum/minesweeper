package Testing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import GameSetup.Board;
import GameSetup.GameType;
import GameSetup.Location;

class BoardTest {
    Board board;

    @Before
    void setUp() {
        board = new Board(GameType.BEGINNER);
    }

    @Test
    void getMineNumber() {

    }

    @Test
    void setMines() {
        List<Location> locations = new ArrayList<>();
        for (int x = 0; x < board.getNoRows(); x++) {
            for (int y = 0; y < board.getNoColumns(); y ++) {
                locations.add(board.getLocation(x,y));
            }
        }

        Stream<Location> stream = locations.stream().filter(s -> s.isMine());
        int mines = (int)stream.count();
        assertEquals(mines, board.getMineNumber());
    }


    @Test
    void getAdjacentLocations() {
        List<Location> locations11 = board.getAdjacentLocations(board.getLocation(1,1));
        List<Location> locations00 = board.getAdjacentLocations(board.getLocation(0,0));
        List<Location> location03 = board.getAdjacentLocations(board.getLocation(0,3));
        assertEquals (8, locations11.size());
        assertEquals(3, locations00.size());
        assertEquals(5, location03.size());
    }
}