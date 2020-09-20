import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(CompoundInterest.numYears(2021),1);
        assertEquals(CompoundInterest.numYears(2023),3);
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValue(10.0,12.0,2022),12.544, tolerance);
        assertEquals(CompoundInterest.futureValue(1000000, 0, 2060),1000000, tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValueReal(10, 12, 2022, 3),11.8026496, tolerance);
        assertEquals(CompoundInterest.futureValueReal(1000000, 0, 2060, 3),295712.29, tolerance);
    }

    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.totalSavings(5000, 2022, 10), 16550, tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.2;
        assertEquals(15572, CompoundInterest.totalSavingsReal(5000, 2022, 10, 3), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
