package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Brian Faun
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void checkCycle() {
        Permutation p1 = getNewPermutation("(BCDA)", getNewAlphabet("ABCD"));
        checkPerm("test1", "ABCD", "BCDA", p1, getNewAlphabet("ABCD"));

        Permutation p2 = getNewPermutation("(AD)(BC)", getNewAlphabet("ABCD"));
        checkPerm("test2", "BDCA", "CABD", p2, getNewAlphabet("ABCD"));

    }

    @Test public void selfPermute() {
        Permutation p = getNewPermutation("(BA)", getNewAlphabet("ABCD"));
        assertEquals('C', p.permute('C'));
        assertEquals('D', p.permute('D'));
        assertEquals('A', p.permute('B'));
        assertEquals(0, p.permute(1));
        assertEquals(1, p.permute(0));
    }

    @Test
    public void testSize() {
        Permutation p1 = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(4, p1.size());

        Permutation p2 = getNewPermutation("(BACD)", getNewAlphabet("ABCDEFG"));
        assertEquals(7, p2.size());

        Permutation p3 = getNewPermutation("", getNewAlphabet());
        assertEquals(26, p3.size());
    }

    @Test
    public void testPermute() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('A', p.permute('B'));
        assertEquals('D', p.permute('C'));
        assertEquals('B', p.permute('D'));
        assertEquals(2, p.permute(0));
        assertEquals(1, p.permute(3));
        checkPerm("test1", "ABCD", "CADB", p, getNewAlphabet("ABCD"));
    }

    @Test
    public void testInvertChar() {
        Permutation p1 = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('B', p1.invert('A'));
        assertEquals('D', p1.invert('B'));
        assertEquals('C', p1.invert('D'));

        Permutation p2 = getNewPermutation("(ABC)(ED)(GF)", getNewAlphabet("ABCDEFG"));
        assertEquals('A', p2.invert('B'));
        assertEquals('D', p2.invert('E'));
        assertEquals('G', p2.invert('F'));
        assertEquals(0, p2.invert(1));
        assertEquals(1, p2.invert(2));
    }

    @Test
    public void testAlphabet() {
        Alphabet alpha = getNewAlphabet("ABCD");
        Permutation p = getNewPermutation("", alpha);
        assertEquals(alpha, p.alphabet());
        Alphabet beta = getNewAlphabet();
        Permutation p2 = getNewPermutation("", beta);
        assertEquals(beta, p2.alphabet());

        Permutation p3 = getNewPermutation("(B)", getNewAlphabet("B"));
        assertEquals('B', p3.invert('B'));
        assertEquals(0, p3.invert(0));
        assertEquals('B', p3.permute('B'));
        assertEquals(0, p3.permute(0));
    }

    @Test
    public void testOverIndex() {
        Permutation p = getNewPermutation("(ABCDEFGHIJKLMNOPQRSTUVXYZ)", getNewAlphabet());
        assertEquals(2, p.permute(27));
        assertEquals(4, p.permute(29));
        assertEquals(3, p.invert(30));
        assertEquals(4, p.invert(31));
    }

    @Test
    public void testDerangement() {
        Permutation p1 = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertFalse(p1.derangement());
        Permutation p2 = getNewPermutation("(ABC)", getNewAlphabet("ABCD"));
        assertFalse(p2.derangement());
        Permutation p3 = getNewPermutation("(AB)", getNewAlphabet("AB"));
        assertTrue(p3.derangement());

    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.invert('F');
        p.invert(5);
        p.permute('F');
        p.permute(5);
    }


}
