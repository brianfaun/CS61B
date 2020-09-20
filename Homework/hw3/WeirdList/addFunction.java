/**
 * Implements IntUnaryFunction Interface.
 * @author Brian Faun
 */
public class addFunction implements IntUnaryFunction {
    private int add;

    public addFunction(int n){
        add = n;
    }

    public int apply(int x) {
        return add + x;
    }
}
