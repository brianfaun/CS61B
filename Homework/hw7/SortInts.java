/** HW #7, Distribution counting for large numbers.
 *  @author Brian Faun
 */
public class SortInts {

    /** Sort A into ascending order.  Assumes that 0 <= A[i] < n*n for all
     *  i, and that the A[i] are distinct. */
    static void sort(long[] A) {
        long in;
        int prev;
        for (int i = 0; i < A.length; i++) {
            in = A[i];
            prev = i - 1;
            while (prev >= 0 && A[prev] > in) {
                A[prev + 1] = A[prev];
                prev--;
            }
            A[prev + 1] = in;
        }
    }
}



