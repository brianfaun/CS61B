package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /** FIXME
     */
    @Test
    public void catenateTests(){
        int[] a = new int[]{1,2,3,4};
        int[] b = new int[]{5,6,9,8};
        int[] result = Arrays.catenate(a, b);
        int[] expected = new int[]{1,2,3,4,5,6,9,8};
        assertArrayEquals(expected, result);

        int[] c = new int[]{1,3,5,7,89};
        int[] d = new int[]{4,5,6,77,9};
        int[] result2 = Arrays.catenate(c, d);
        int[] expected2 = new int[]{1,3,5,7,89,4,5,6,77,9};
        assertArrayEquals(expected2, result2);
    }

    @Test
    public void removeTests(){
        int[] a = new int[]{1,2,3,4};
        int[] result = Arrays.remove(a, 1, 2);
        int[] expected = new int[]{1, 4};
        assertArrayEquals(expected, result);

        int[] b = new int[]{5,6,9,8,34,23};
        int[] result2 = Arrays.remove(b, 2, 3);
        int[] expected2 = new int[]{5, 6, 23};
    }

    @Test
    public void naturalRunsTest(){
        int[] a = new int[]{1, 3, 7, 5, 4, 6, 9, 10};
        int[][] result = new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}};
        int[][] expected = Arrays.naturalRuns(a);
        assertArrayEquals(expected, result);
    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
