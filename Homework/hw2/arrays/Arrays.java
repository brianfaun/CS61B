package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        int aSize = A.length;
        int bSize = B.length;
        int[] result = new int[aSize + bSize];
        for (int i = 0; i < aSize; i++){
            result[i] = A[i];
        }
        for (int j = 0; j < bSize; j++){
            result[aSize+j] = B[j];
        }
        return result;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        int aLength = A.length;
        int rLength  = aLength - len;
        int[] rArray = new int[rLength];
        for (int i = 0; i < start; i++){
            rArray[i] = A[i];
        }
        for (int j = 0; j+start-1 < rLength-1; j++){
            rArray[j+start] = A[j+start+len];
        }
        return rArray;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        /* *Replace this body with the solution. */
        int aLength = A.length;
        int sublists = 1;
        for (int i = 0; i < aLength-1; i++){
            if (A[i] >= A[i+1]){
                sublists += 1;
            }
        }
        int[][] natural = new int[sublists][];
        int length = 0;
        for (int j = 0; j < aLength-1; j++){
            if (A[j] >= A[j+1]){
                natural[j] = Utils.subarray(A, j, length);
                length = 0;
            }
            length += 1;
        }
        return natural;
    }
}
