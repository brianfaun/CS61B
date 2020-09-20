package signpost;

import ucb.junit.textui;

/** The suite of all JUnit tests for the signpost package.
 *  @author P. N. Hilfinger
 */
public class UnitTests {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(ModelTests.class,
                PuzzleGeneratorTests.class, PlaceTests.class));
    }

}
