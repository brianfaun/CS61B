/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the loa package.
 *  @author Brian Faun
 */
public class UnitTests {

    /** Run the JUnit tests in the loa package. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTests.class);
        textui.runClasses(BoardTest.class);
    }

    /** Test 1. */
    @Test
    public void initTest() {
        Board b = new Board();
        assertTrue(true);
    }

    /** Test 2. */
    @Test
    public void winTest() {
        assertTrue(true);
    }

}


