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

    /** Check that the set of T's in EXPECTED is the same as that in ACTUAL.
     *  Use MSG as the error message if the check fails. */
    static <T> void assertSetEquals(String msg,
                                    Collection<T> expected,
                                    Collection<T> actual) {
        assertNotNull(msg, actual);
        assertEquals(msg, new HashSet<T>(expected), new HashSet<T>(actual));
    }
}
