/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author Brian Faun
 */
public class Nybbles {

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 7;

    public static void main (String [] args) {
        for (int i = 0; i < 16; i++) {
            System.out.print(i % 8);
        }
        System.out.println();


        int[] a = new int[8];
        for (int i = 0; i < 8; i++) {
            a[i] = i;
            System.out.println();
        }
    }

    /** Return an array of size N. 
    * DON'T CHANGE THIS.*/
    public Nybbles(int N) {
        _data = new int[(N + 7) / 8];
        _n = N;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }

    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
            return (_data[k / 8] << 4 * (7 - k % 8)) >> 28; // REPLACE WITH SOLUTION
        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
            _data[k / 8] = (_data[k / 8] | (val & 0xF) << 4 * (k % 8));
        }
    }

    /** DON'T CHANGE OR ADD TO THESE.*/
    /** Size of current array (in nybbles). */
    private int _n;
    /** The array data, packed 8 nybbles to an int. */
    private int[] _data;
}
