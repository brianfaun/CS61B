
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author Brian Faun
 */
import java.util.Arrays;
public class UnionFind {
    public static void main (String[] args) {
        UnionFind test = new UnionFind(10);
        System.out.println(Arrays.toString(test.parents));
        System.out.println(Arrays.toString(test.size));
    }

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        parents = new int[N + 1];
        size = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            parents[i] = i;
        }
        for (int i = 1; i <= N; i++) {
            size[i] = 1;
        }
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        if (parents[v] == v) {
            return v;
        } else {
            return find(parents[v]);
        }
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        if (samePartition(u, v)) {
            return find(u);
        }
        int rootu = find(u);
        int rootv = find(v);
        if (size[rootu] > size[rootv]) {
            parents[rootv] = rootu;
            size[rootu] += size[rootv];
            return rootu;
        } else if (size[rootv] > size[rootu] || size[rootv] == size[rootu]) {
            parents[rootu] = rootv;
            size[rootv] += size[rootu];
            return rootv;
        }
        return 9999;
    }

    // FIXME
    /** Parents of set of contiguous integers. */
    int[] parents;

    /** Size of each set of contiguous integers. */
    int[] size;
}
