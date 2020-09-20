import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {
    int[][] tester = { {1, 1, 14, 1},
                       {4, 4, 4 , 4},
                       {2, 2, 2 , 2} };
    int[] tester2 = {17, 16, 8};

    @Test
    public void testMaxValue() {
        //TODO: Your code here
        assertEquals(14, MultiArr.maxValue(tester));
    }

    @Test
    public void testAllRowSums(){
        //TODO: Your code here!
        assertArrayEquals(tester2, MultiArr.allRowSums(tester));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
