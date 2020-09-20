package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Brian Faun
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */
    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    /*
    @Test
    public void checkCycle() {
        Permutation perm = new Permutation("(BCDA)", new Alphabet("ABCD"));
        checkPerm("test1", "ABCD", "BCDA");
        //Permutation perm = new Permutation("(AD)(BC)", new Alphabet("ABCD"));
        checkPerm("test2", "BDCA", "CABD");
    }

     */

    @Test
    public void testOverIndex() {
        Permutation p = new Permutation("(ABCDEFGHIJKLMNOPQRSTUVXYZ)", UPPER);
        assertEquals(2, p.permute(27));
        assertEquals(4, p.permute(29));
        assertEquals(3, p.invert(30));
        assertEquals(4, p.invert(31));
    }

    @Test
    public void checkPermute() {
        Permutation p = new Permutation("(ABC)(ED)", UPPER);
        assertEquals(1, p.permute(0));
        assertEquals(0, p.permute(2));
        assertEquals('A', p.permute('C'));
        assertEquals('C', p.permute('B'));
        assertEquals(6, p.permute(6));
    }

    @Test
    public void checkInvert() {
        Permutation p = new Permutation("(ABC)(ED)", UPPER);
        assertEquals(1, p.invert(2));
        assertEquals(2, p.invert(0));
        assertEquals('A', p.invert('B'));
        assertEquals('C', p.invert('A'));
        assertEquals(6, p.permute(6));
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        p.invert('F');
        p.invert(5);
        p.permute('F');
        p.permute(5);
    }
}
