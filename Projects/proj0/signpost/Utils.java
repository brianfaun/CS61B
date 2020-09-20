package signpost;

import static org.junit.Assert.*;
import static java.lang.System.arraycopy;
import java.util.Collection;
import java.util.HashSet;

/** Various utility methods.
 *  @author P. N. Hilfinger
 */
class Utils {

    /** Returns String.format(FORMAT, ARGS...). */
    static String msg(String format, Object... args) {
        return String.format(format, args);
    }

    /** Copy contents of SRC into DEST.  SRC and DEST must both be
     *  rectangular, with identical dimensions. */
    static void deepCopy(int[][] src, int[][] dest) {
        assert src.length == dest.length && src[0].length == dest[0].length;
        for (int i = 0; i < src.length; i += 1) {
            arraycopy(src[i], 0, dest[i], 0, src[i].length);
        }
    }

    /** Check that the set of T's in EXPECTED is the same as that in ACTUAL.
     *  Use MSG as the error message if the check fails. */
    static <T> void assertSetEquals(String msg,
                                     Collection<T> expected,
                                     Collection<T> actual) {
        assertNotNull(msg, actual);
        assertEquals(msg, new HashSet<T>(expected), new HashSet<T>(actual));
    }

    /** Return an IllegalArgumentException whose message is formed from
     *  MSGFORMAT and ARGS as for String.format. */
    static IllegalArgumentException badArgs(String msgFormat, Object... args) {
        return new IllegalArgumentException(String.format(msgFormat, args));
    }

    /** Return integer denoted by NUMERAL. */
    static int toInt(String numeral) {
        return Integer.parseInt(numeral);
    }

    /** Return long integer denoted by NUMERAL. */
    static long toLong(String numeral) {
        return Long.parseLong(numeral);
    }

    /** Given H x W array A, return a W x H array in which the columns of
     *  A, each reversed, are the rows of the result.  That is, returns B
     *  so that B[x][y] is A[H - y - 1][x].  This is a convenience method
     *  that allows our test arrays to be arranged on the page to look as
     *  they do when displayed. */
    static int[][] tr(int[][] A) {
        int[][] B = new int[A[0].length][A.length];
        for (int x = 0; x < A[0].length; x += 1) {
            for (int y = 0; y < A.length; y += 1) {
                B[x][y] = A[A.length - y - 1][x];
            }
        }
        return B;
    }

    /** Returns a Model whose solution is SOLN, whose initially fixed numbers
     *  are FIXED, and which also has initial connections given by
     *  CONNECT.  CONNECT is a 2k array denoting k connections:
     *  { u0, v0, u1, v1, ... }, where ui, vi indicates that the square
     *  containing ui in SOLN should be connected to the square containing
     *  Vi in SOLN. */
    static Model setUp(int[][] soln, int[] fixed, int[] connect) {
        Model result = new Model(soln);
        for (int n : fixed) {
            result.get(result.solnNumToPlace(n)).setFixedNum(n);
        }
        for (int i = 0; i < connect.length; i += 2) {
            result.solnNumToSq(connect[i])
                .connect(result.solnNumToSq(connect[i + 1]));
        }
        return result;
    }
}

