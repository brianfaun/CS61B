package signpost;

import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

import static signpost.Place.pl;
import signpost.Model.Sq;
import static signpost.Utils.msg;
import static signpost.Utils.tr;
import static signpost.Utils.assertSetEquals;

/** Tests of the Model class.
 *  @author P. N. Hilfinger
 *  @author Will Wang
 */
public class ModelTests {

    /** Check that MODEL is a valid representation of the partial solution in
     *  SOLUTION, and that the fixed numbers in it are precisely FIXED.
     *  SOLUTION may contain 0's, indicating squares that are not yet
     *  sequenced. */
    static void checkNumbers(int[][] solution, Model model,
                             Collection<Integer> fixed) {
        assertEquals("Wrong model width", solution.length, model.width());
        assertEquals("Wrong model height", solution[0].length, model.height());
        assertEquals("Wrong model size", solution.length * solution[0].length,
                     model.size());
        HashSet<Integer> actualFixed = new HashSet<>();
        for (int x = 0; x < model.width(); x += 1) {
            for (int y = 0; y < model.height(); y += 1) {
                Sq sq = model.get(x, y);
                assertEquals(msg("Wrong at (%d, %d)", x, y),
                             solution[x][y], sq.sequenceNum());
                if (sq.hasFixedNum()) {
                    actualFixed.add(sq.sequenceNum());
                }
            }
        }
        assertEquals("Fixed positions differ", new HashSet<Integer>(fixed),
                     actualFixed);
    }

    /** Check that the arrow directions in MODEL agree with DIRS.  The
     *  direction of the arrow at (x, y) in MODEL should be DIRS[x][y]. */
    static void checkArrows(int[][] dirs, Model model) {
        for (int x = 0; x < model.width(); x += 1) {
            for (int y = 0; y < model.height(); y += 1) {
                assertEquals(msg("Arrows differ at (%d, %d)", x, y),
                             dirs[x][y], model.get(x, y).direction());
            }
        }
    }

    /** Check that the model.Sq has the correct attributes. */
    static void checkSquare(Sq sq, Sq head, Sq predecessor, Sq successor,
                            int seqNum, int group) {
        assertEquals("Sq has incorrect group head.", head, sq.head());
        assertEquals("Sq has incorrect predecessor.",
                     predecessor, sq.predecessor());
        assertEquals("Sq has incorrect successor.", successor, sq.successor());
        assertEquals("Sq has incorrect sequence number.",
                     seqNum, sq.sequenceNum());
        assertEquals("Sq has incorrect group number.", group, sq.group());
    }

    @Test
    public void initTest1() {
        Model model = new Model(tr(SOLN1));
        checkNumbers(tr(BOARD1), model, asList(1, 16));
    }

    @Test
    public void initTest2() {
        Model model = new Model(SOLN2);
        checkNumbers(BOARD2, model, asList(1, 20));
    }

    @Test
    public void initTest3() {
        int[][] soln = tr(SOLN2);
        Model model = new Model(soln);
        model.solve();
        for (int x = 0; x < model.width(); x += 1) {
            YLoop:
            for (int y = 0; y < model.height(); y += 1) {
                for (Sq sq : model) {
                    if (x == sq.x && y == sq.y) {
                        assertEquals(msg("Wrong number at (%d, %d)", x, y),
                                     soln[x][y], sq.sequenceNum());
                        continue YLoop;
                    }
                }
                fail(msg("Did not find square at (%d, %d)", x, y));
            }
        }
    }

    @Test
    public void allPlacesTest() {
        Model model = new Model(tr(SOLN2));
        for (Sq sq : model) {
            assertEquals(msg("Wrong square at (%d, %d)", sq.x, sq.y),
                         sq, model.get(sq.x, sq.y));
            assertEquals(msg("Wrong square at Place %s", sq.pl),
                         sq, model.get(sq.pl));
            assertEquals(msg("Wrong Place at (%d, %d)", sq.x, sq.y),
                         pl(sq.x, sq.y), sq.pl);
        }
    }

    @Test
    public void arrowTest1() {
        Model model = new Model(tr(SOLN1));
        checkArrows(tr(ARROWS1), model);
    }

    @Test
    public void copyTest() {
        Model model1 = new Model(tr(SOLN1));
        Model model2 = new Model(model1);
        checkNumbers(tr(BOARD1), model2, asList(1, 16));
        for (int x = 0; x < model1.width(); x += 1) {
            for (int y = 0; y < model1.height(); y += 1) {
                Sq sq1 = model1.get(x, y);
                Sq sq2 = model2.get(x, y);
                assertFalse("Sq should not be the same instance", sq1 == sq2);
                assertTrue("Sq should be equivalent objects", sq1.equals(sq2));
            }
        }

        HashMap<Sq, Sq> model1Sqs = new HashMap<Sq, Sq>();
        HashSet<Sq> model2Sqs = new HashSet<Sq>();
        for (Sq sq : model1) {
            model1Sqs.put(sq, sq);
        }
        for (Sq sq : model2) {
            assertFalse("Sq should not be the same instance",
                    sq == model1Sqs.get(sq));
            model2Sqs.add(sq);
        }
        assertSetEquals("Model iterators should have equivalent Sqs",
                model1Sqs.keySet(), model2Sqs);
    }

    @Test
    public void solvedTest1() {
        Model model = new Model(tr(SOLN1));
        assertFalse("Model not solved yet.", model.solved());
        model.solve();
        assertTrue("Model should be solved.", model.solved());
        checkNumbers(tr(SOLN1), model, asList(1, 16));
    }

    @Test
    public void autoConnectTest1() {
        Model model = new Model(tr(new int[][] { { 1, 2 } }));
        model.autoconnect();
        assertTrue("Trivial puzzle should be solved at birth.", model.solved());
    }

    @Test
    public void arrowDirectionTest() {
        Model model = new Model(SOLN1);
        assertEquals("What is the value of _solution[x][y]?", 0, 0);
    }


    /* In sqConnectTest and sqDisconnectTest, we disregard the solution
       board passed into Model and instead instantiate our own squares.
       This avoids depending on a working Model constructor.
       The squares are placed on a 3x3 board shown below, where each
       tuple represents (square, direction).

          (s5, S ) (      ) (s3, SW)
          (s6, S ) (s4, S ) (      )
          (s1, NE) (      ) (s2, N )
     */

    @Test
    public void sqConnectTest() {
        Model model = new Model(tr(SOLN1));
        Sq s1 = model.new Sq(0, 0, 0, false, 1, -1);
        Sq s2 = model.new Sq(2, 0, 0, false, 8, -1);
        Sq s3 = model.new Sq(2, 2, 0, false, 5, -1);
        Sq s4 = model.new Sq(1, 1, 4, true, 5, 0);
        Sq s5 = model.new Sq(0, 2, 8, true, 4, 0);
        Sq s6 = model.new Sq(0, 1, 1, true, 4, 0);

        assertFalse("A square is not connectable to itself.", s1.connect(s1));
        checkSquare(s1, s1, null, null, 0, -1);

        assertFalse("Squares must be one queen move away and in the "
                    + "correct direction.", s1.connect(s2));
        checkSquare(s1, s1, null, null, 0, -1);
        checkSquare(s2, s2, null, null, 0, -1);

        assertTrue("These squares should be connectable.", s1.connect(s3));
        checkSquare(s1, s1, null, s3, 0, 1);
        checkSquare(s3, s1, s1, null, 0, 1);

        assertFalse("Unnumbered in same group can't connect", s3.connect(s1));
        checkSquare(s1, s1, null, s3, 0, 1);
        checkSquare(s3, s1, s1, null, 0, 1);

        assertFalse("Next square cannot already have a predecessor.",
                    s2.connect(s3));
        checkSquare(s2, s2, null, null, 0, -1);
        checkSquare(s3, s1, s1, null, 0, 1);

        assertFalse("Current square can't have successor.", s1.connect(s4));
        checkSquare(s1, s1, null, s3, 0, 1);
        checkSquare(s4, s4, null, null, 4, 0);

        assertTrue("These squares should be connectable.", s3.connect(s4));
        checkSquare(s3, s1, s1, s4, 3, 0);
        checkSquare(s4, s1, s3, null, 4, 0);
        checkSquare(s1, s1, null, s3, 2, 0);

        assertFalse("Non-sequential numbered squares", s5.connect(s1));
        checkSquare(s1, s1, null, s3, 2, 0);
        checkSquare(s5, s5, null, null, 8, 0);

        assertTrue("These squares should be connectable.", s6.connect(s1));
        checkSquare(s1, s6, s6, s3, 2, 0);
        checkSquare(s6, s6, null, s1, 1, 0);
    }

    /* We disregard the solution board passed into Model and instead
       instantiate our own squares. This bypasses the dependency
       on a working Model constructor. The squares are placed on
       a 3x3 board shown below, where each tuple represents
       (square, direction).

          (s3, E ) (s4, N ) (s9, N )
          (s2, N ) (      ) (s8, N )
          (s1, N ) (s6, E ) (s7, N )

       This test requires that you pass sqConnectTest.
     */

    @Test
    public void sqDisconnectTest() {
        Model model = new Model(tr(SOLN1));
        Sq s1 = model.new Sq(0, 0, 1, true, 8, 0);
        Sq s2 = model.new Sq(0, 1, 0, false, 8, -1);
        Sq s3 = model.new Sq(0, 2, 0, false, 2, -1);
        Sq s4 = model.new Sq(1, 2, 0, false, 8, -1);
        Sq s6 = model.new Sq(1, 0, 0, false, 2, -1);
        Sq s7 = model.new Sq(2, 0, 0, false, 8, -1);
        Sq s8 = model.new Sq(2, 1, 0, false, 8, -1);
        Sq s9 = model.new Sq(2, 2, 9, true, 8, 0);

        assertTrue("These squares should be connectable.", s1.connect(s2));
        assertTrue("These squares should be connectable.", s2.connect(s3));
        assertTrue("These squares should be connectable.", s3.connect(s4));
        checkSquare(s1, s1, null, s2, 1, 0);
        checkSquare(s2, s1, s1, s3, 2, 0);
        checkSquare(s3, s1, s2, s4, 3, 0);
        checkSquare(s4, s1, s3, null, 4, 0);

        assertTrue("These squares should be connectable.", s8.connect(s9));
        assertTrue("These squares should be connectable.", s7.connect(s8));
        assertTrue("These squares should be connectable.", s6.connect(s7));
        checkSquare(s6, s6, null, s7, 6, 0);
        checkSquare(s7, s6, s6, s8, 7, 0);
        checkSquare(s8, s6, s7, s9, 8, 0);
        checkSquare(s9, s6, s8, null, 9, 0);

        s2.disconnect();
        checkSquare(s1, s1, null, s2, 1, 0);
        checkSquare(s2, s1, s1, null, 2, 0);
        checkSquare(s3, s3, null, s4, 0, 1);
        checkSquare(s4, s3, s3, null, 0, 1);

        s1.disconnect();
        checkSquare(s1, s1, null, null, 1, 0);
        checkSquare(s2, s2, null, null, 0, -1);

        s4.disconnect();
        checkSquare(s3, s3, null, s4, 0, 1);
        checkSquare(s4, s3, s3, null, 0, 1);

        s3.disconnect();
        checkSquare(s3, s3, null, null, 0, -1);
        checkSquare(s4, s4, null, null, 0, -1);

        s7.disconnect();
        checkSquare(s6, s6, null, s7, 0, 1);
        checkSquare(s7, s6, s6, null, 0, 1);
        checkSquare(s8, s8, null, s9, 8, 0);
        checkSquare(s9, s8, s8, null, 9, 0);

        s8.disconnect();
        checkSquare(s8, s8, null, null, 0, -1);
        checkSquare(s9, s9, null, null, 9, 0);
    }

    /* The following array data is written to look on the page like
     * the arrangement of data on the screen, with the first row
     * corresponding to the top row of the puzzle board, etc.  They are
     * transposed by tr into the actual data, in which the first array
     * dimension indexes columns, and the second indexes rows from bottom to
     * top. */

    private static final int[][] SOLN1 = {
        { 1, 13, 3, 2 },
        { 12, 4, 8, 15 },
        { 5, 9, 7, 14 },
        { 11, 6, 10, 16 }
    };

    private static final int[][] ARROWS1 = {
        { 2, 3, 5, 6 },
        { 1, 5, 5, 4 },
        { 3, 3, 8, 8 },
        { 8, 1, 6, 0 }
    };

    private static final int[][] BOARD1 = {
        { 1, 0, 0, 0 },
        { 0, 0, 0, 0 },
        { 0, 0, 0, 0 },
        { 0, 0, 0, 16 } };

    private static final int[][] SOLN2 = {
        { 1, 2, 17, 16, 3 },
        { 9, 7, 15, 6, 8 },
        { 12, 11, 18, 5, 4 },
        { 10, 13, 14, 19, 20 }
    };

    private static final int[][] BOARD2 = {
        { 1, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 20} };

}
