/**
 * Implements IntUnaryFunction Interface.
 * @author Brian Faun
 */
public class sumFunction implements IntUnaryFunction{
    static int total = 0;

    public int apply(int x) {
        return total += x;
    }
}
