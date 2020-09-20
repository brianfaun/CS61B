import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/** HW #7, Sorting ranges.
 *  @author Brian Faun
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        intervals.sort(Comparator.comparingInt(arr -> arr[0]));

        int total = 0;
        int a = Integer.MAX_VALUE, end = Integer.MIN_VALUE;

        for (int i = 0; i < intervals.size(); i++) {
            if (intervals.get(i)[0] > end) {
                total += end - a;
                a = intervals.get(i)[0];
                end = intervals.get(i)[1];
            } else if (intervals.get(i)[0] <= end) {
                if (intervals.get(i)[1] > end) {
                    end = intervals.get(i)[1];
                }
            }
        }
        total += end - a;
        return total - 1;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Test intervals. */
    static final int[][] test1= {
            {19, 39},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int correct1 = 32;



    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
        assertEquals(correct1, coveredLength(Arrays.asList(test1)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
