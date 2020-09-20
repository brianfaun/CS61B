import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0. 
 *  @author YOUR NAMES HERE
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here.  We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester 
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void maxTest() {
        // Change call to max to make this call yours.
        assertEquals(14, firstclass.max(new int[] { 0, -5, 2, 14, 10 }));
        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumTest() {
        // Change call to threeSum to make this call yours.
        assertTrue(firstclass.threeSum(new int[] { -6, 3, 10, 200 }));
        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumDistinctTest() {
        // Change call to threeSumDistinct to make this call yours.
        assertFalse(firstclass.threeSumDistinct(new int[] { -6, 3, 10, 200 }));
        // REPLACE THIS WITH MORE TESTS.
    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
