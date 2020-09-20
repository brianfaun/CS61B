package signpost;

import static java.util.Arrays.asList;

import org.junit.Test;
import static org.junit.Assert.*;

import static signpost.Utils.tr;
import static signpost.Utils.setUp;
import static signpost.PuzzleGenerator.*;
import static signpost.ModelTests.checkNumbers;

/** Tests of the Model class.
 *  @author P. N. Hilfinger
 */
public class PuzzleGeneratorTests {

    /** Check that SOLN is a valid WIDTH x HEIGHT puzzle that has fixed ends
     *  unless ALLOWLOOSE. */
    private void checkPuzzle(int[][] soln, int w, int h, boolean aL) {
        assertTrue("Bad size", soln.length == w && soln[0].length == h);
        int last = w * h;
        for (int x0 = 0; x0 < w; x0 += 1) {
            for (int y0 = 0; y0 < w; y0 += 1) {
                int v = soln[x0][y0];
                if (v == last) {
                    continue;
                }
                assertTrue("Value out of range", v >= 1 && v <= last);
                int c;
                for (int x1 = c = 0; x1 < w; x1 += 1) {
                    for (int y1 = 0; y1 < h; y1 += 1) {
                        if (soln[x1][y1] == v + 1) {
                            assertTrue("Values not in line",
                                       x0 == x1 || y0 == y1
                                       || Math.abs(x0 - x1)
                                       == Math.abs(y0 - y1));
                            c += 1;
                        }
                    }
                }
                assertEquals("Duplicate or unconnected values", 1, c);
            }
        }
        if (!aL) {
            assertTrue("End points incorrect",
                       soln[0][h - 1] == 1 && soln[w - 1][0] == last);
        }
    }

    @Test
    public void puzzleTest() {
        PuzzleGenerator puzzler = new PuzzleGenerator(314159);
        Model model;
        model = puzzler.getPuzzle(5, 5, false);
        checkPuzzle(model.solution(), 5, 5, false);
        model = puzzler.getPuzzle(4, 6, false);
        checkPuzzle(model.solution(), 4, 6, false);
        model = puzzler.getPuzzle(5, 5, true);
        checkPuzzle(model.solution(), 5, 5, true);
    }

    @Test
    public void uniquePuzzleTest() {
        PuzzleGenerator puzzler = new PuzzleGenerator(314159);
        Model model;
        model = puzzler.getPuzzle(1, 2, false);
        checkPuzzle(model.solution(), 1, 2, false);
        model = puzzler.getPuzzle(1, 3, false);
        checkPuzzle(model.solution(), 1, 3, false);
    }

    @Test
    public void uniqueSuccessorTest() {
        Model M = setUp(tr(SOLN1), SOLN1_NUMBERS, CONNECT1);
        assertEquals("Unique connection to edge Sq", M.get(2, 6),
                     findUniqueSuccessor(M, M.get(3, 5)));
        assertEquals("Unique connection through connected Sq", M.get(5, 0),
                     findUniqueSuccessor(M, M.get(3, 2)));
        assertEquals("Ambiguous connection", null,
                     findUniqueSuccessor(M, M.get(3, 4)));
        assertEquals("Successive numbered squares", M.get(3, 1),
                     findUniqueSuccessor(M, M.get(2, 0)));
        assertEquals("Unique connection to numbered Sq", M.get(1, 1),
                     findUniqueSuccessor(M, M.get(3, 3)));
        assertEquals("Unique connection to numbered Sq", M.get(1, 1),
                     findUniqueSuccessor(M, M.get(3, 3)));
        assertEquals("Unique connection of numbered to unnumbered Sq",
                     M.get(4, 2),
                     findUniqueSuccessor(M, M.get(6, 4)));
    }

    @Test
    public void uniquePredecessorTest() {
        Model M = setUp(tr(SOLN2), SOLN2_NUMBERS, CONNECT2);
        assertEquals("Unique predecessor", M.get(5, 6),
                     findUniquePredecessor(M, M.get(1, 6)));
        assertEquals("Predecessor not unique", null,
                     findUniquePredecessor(M, M.get(3, 3)));
    }

    @Test
    public void extendSimpleTest1() {
        Model M = setUp(tr(SOLN3), new int[] { 1, 16 }, new int[] {});
        assertTrue("Extend simple on ambiguous puzzle.", extendSimple(M));
        checkNumbers(tr(PARTIAL_SOLN3), M, asList(1, 16));
    }

    @Test
    public void extendSimpleTest2() {
        Model M = setUp(tr(SOLN3), new int[] { 1, 9, 16 }, new int[] {});
        assertTrue("Extend simple on unambiguous puzzle.", extendSimple(M));
        checkNumbers(tr(SOLN3), M, asList(1, 9, 16));
    }

    static final int[][] SOLN1 = {
        {  3,  9, 29,  4, 49,  8,  5 },
        { 47, 12, 46, 28, 48,  6, 27 },
        { 15, 13, 16, 40, 41, 39, 24 },
        { 14, 44, 45, 36, 26,  7, 23 },
        {  2, 31,  1, 32, 25, 10, 30 },
        { 21, 37, 20, 35, 19, 11, 22 },
        { 42, 38, 34, 18, 43, 33, 17 }
    };
    static final int[] SOLN1_NUMBERS = {
        9, 49, 27, 15, 40, 24, 44, 1, 30, 37, 35, 34
    };
    static final int[] CONNECT1 = { 18, 19, 41, 42, 7, 8, 8, 9 };

    static final int[][] SOLN2 = {
        { 23, 48, 46, 18, 19, 47, 45 },
        { 42, 49, 4, 32, 8, 35, 36 },
        { 21, 6, 33, 11, 7, 15, 34 },
        { 5, 20, 9, 28, 1, 39, 38 },
        { 22, 10, 27, 41, 29, 30, 26 },
        { 43, 44, 16, 17, 13, 14, 12 },
        { 24, 2, 3, 31, 25, 40, 37 }
    };

    static final int[] SOLN2_NUMBERS = {
        23, 46, 18, 47, 49, 32, 11, 34, 1, 44, 17, 2, 25
    };

    static final int[] CONNECT2 = { 45, 46 };

    /** An ambiguous puzzle.  The arrows corresponding to SOLN3, where the
     *  only fixed numbers are 1 and 16 has multiple solutions. */
    static final int[][] SOLN3 = {
        { 1, 4, 13, 2 },
        { 8, 5, 3, 14 },
        { 6, 7, 12, 11 },
        { 9, 15, 10, 16 }
    };

    /** This should be the result of extendSimple on the puzzle SOLN3,
     *  SOLN3_NUMBERS. */
    static final int[][] PARTIAL_SOLN3 = {
        { 1, 4, 0, 2 },
        { 0, 5, 3, 0 },
        { 6, 0, 0, 0 },
        { 0, 0, 0, 16 }
    };
}
