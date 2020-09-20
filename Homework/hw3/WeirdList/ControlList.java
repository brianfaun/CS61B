import sun.applet.resources.MsgAppletViewer_it;

/**
 * Helper class for WeirdList.
 * @author Brian Faun
 */
public class ControlList extends WeirdList {

    /** Constructor. */
    public ControlList() {
        super(0, null);
    }

    /**
     * Base case for recursion
     * @param w WeirdList
     * @return b boolean
     */
    static boolean isEmpty(WeirdList w) {
        return w.equals(WeirdList.EMPTY);
    }

    /** Return 0. */
    public int length() {
        return 0;
    }

    /** Print nothing. */
    public String toString() {
        return "";
    }

    /**
     * Return null WeirdList
     * @param func func
     * @return EMPTY
     */
    public WeirdList map(IntUnaryFunction func) {
        return EMPTY;
    }
}
