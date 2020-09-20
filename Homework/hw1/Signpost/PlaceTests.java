import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/** Tests of the Place class.
 *  @author Will Wang
 */
public class PlaceTests {

    private void checkSuccessors(Place[][] expected, Place.PlaceList[] actual) {
        for (int dir = 8; dir >= 0; dir -= 1) {
            Utils.assertSetEquals(Utils.msg("Mismatch sucessors at direction %d", dir),
                    Arrays.asList(expected[dir]), actual[dir]);
        }
    }

    @Test
    public void successorCellsTest() {
        Place.PlaceList[][][] sucessors = Place.successorCells(WIDTH, HEIGHT);
        checkSuccessors(EXPECTED, sucessors[PL.x][PL.y]);
    }

    private static final int WIDTH = 4;
    private static final int HEIGHT = 4;

    private static final Place PL = Place.pl(1, 1);

    private static final Place[][] EXPECTED = {
        { Place.pl(0, 0), Place.pl(0, 1), Place.pl(0, 2), Place.pl(1, 2),
          Place.pl(1, 3), Place.pl(2, 2), Place.pl(3, 3), Place.pl(2, 1),
          Place.pl(3, 1), Place.pl(2, 0), Place.pl(1, 0)},
        { Place.pl(2, 2), Place.pl(3, 3) },
        { Place.pl(2, 1), Place.pl(3, 1) },
        { Place.pl(2, 0) },
        { Place.pl(1, 0) },
        { Place.pl(0, 0) },
        { Place.pl(0, 1) },
        { Place.pl(0, 2) },
        { Place.pl(1, 2), Place.pl(1, 3) },
    };

    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(PlaceTests.class));
    }
}
