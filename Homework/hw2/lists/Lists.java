package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
 */
class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        /* *Replace this body with the solution. */

        IntListList natural = new IntListList(L, null);
        IntListList tracer = natural;

        while (L.tail != null) {
            if (L.tail.head > L.head){
                L = L.tail;
            } else {
                tracer.tail = new IntListList(L.tail, null);
                tracer = tracer.tail;


                L.tail = null;
                L = tracer.head;
            }
        }
        return natural;
    }
}
 /*
        IntList tracer = L;
        int length = Utils.length(L);
        int[] LArray = new int[length];
        for (int i = 0; i < length-1; i++){
            LArray[i] = tracer.head;
            tracer = tracer.tail;
        }

        int sublists = 1;
        for (int i =0; i< LArray.length-1; i++){
            if(LArray[i] >= LArray[i+1]){
                sublists += 1;
            }
        }
        int[][] natural = new int[sublists][];
        for (int i = 0; i < LArray.length-1; i++){
            natural[i] = Utils.subarray(LArray, i-1, i);
        }

        return IntListList.list(natural);

         */
 /*
        int length = Utils.length(L);
        int[] intArray = new int[length];
        int[][] finalArray;
        for(int i =0; i < length-1; i++){
            intArray[i] = L.head;
            L = L.tail;
        }

        for (int i=0; i < length-1; i++){
            int sublists = 0;
            if (intArray[i] < intArray[i+1]){
                sublists += 1;
            }
            int[] addArray = new int[size];
        }

        return Utils.toList(final);

         */
/*
     natural.tail = new
                num = tracker.head;
                tracker = tracker.tail;
                adder.head = num;
                adder.tail = null;
            } else{                             // extend num to adder
                if (adder.tail == null) {       // only for the start, where tail is null
                    adder.tail = new IntList(tracker.head, null);
                } else{
                    adder.tail = new IntList(adder.tail.head, null);
                }
                num = tracker.head;
                tracker = tracker.tail;
 */