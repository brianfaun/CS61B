import org.junit.Test;
import ucb.junit.textui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Tests for the NFA construction.
 * We test only legal patterns.
 *
 * The anatomy of these tests are defined below.
 *
 * @author ONK
 */
public class NFATests {

    /** For testing directories */
    private static final String CWD = System.getProperty("user.dir");

    /** Filter out all but testing (.in) files. */
    private static final FilenameFilter PLAIN_FILES =
            new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile() && name.endsWith(".in");
            }
        };


    public static void main(String[] unused) {
        textui.runClasses(NFATests.class);
    }

    /**
     * Returns a list of the names of all plain files in the directory DIR, in
     * lexicographic order as Java Strings.  Returns null if DIR does
     * not denote a directory. */
    private List<String> inputFilenamesIn(File dir) {
        String[] files = dir.list(PLAIN_FILES);
        if (files == null) {
            return null;
        } else {
            Arrays.sort(files);
            return Arrays.asList(files);
        }
    }

    /** Runs all the tests (.in files) in the given directory */
    private void testDirectory(String dirString) {
        int numPassed = 0;
        int numTests = 0;
        File dir = new File(CWD, dirString);

        System.out.println(
                String.format("\nTESTING DIRECTORY: %s\n", dirString));
        for (String filePath : inputFilenamesIn(dir)) {
            System.out.println(String.format("TESTING FILE: %s", filePath));
            numTests += 1;
            try {
                testFile(new File(dir, filePath));
                System.out.println("PASSED");
                numPassed += 1;
            } catch (AssertionError e) {
                System.out.println("FAILED");
                System.out.println(e.getMessage());

            }
        }
        double percent = ((double) numPassed) / numTests;

        assert percent == 1.0 : "some tests failed";
        System.out.println("ALL PASSED\n");
    }

    /**
     * Here is the anatomy of a testing file:
     *
     * The first line is the pattern
     * The next line begins with either a * or a ** and then has a space
     * separated list of Strings that should match the pattern. If this line
     * begins with a **, then the empty String is included (else not).
     *
     * The next (and final) line is identical to the previous line, but instead
     * is a list of Strings that should NOT match the pattern.
     *
     * Throws an AssertionError if things match when they shouldn't or
     * don't match when they should. */
    private void testFile(File file) {
        try {
            String filePath = file.getPath();
            Scanner scanner = new Scanner(new File(filePath));
            String pattern = scanner.next();
            NFA nfa = NFA.fromPattern(pattern);
            String singleOrDoubleStar;
            boolean isSingleOrDouble;

            List<String> matches = new LinkedList<>();
            List<String> notMatches = new LinkedList<>();


            singleOrDoubleStar = scanner.next();
            isSingleOrDouble = singleOrDoubleStar.equals("*")
                    | singleOrDoubleStar.equals("**");

            assert isSingleOrDouble : "matches must begin with * or **";


            if (singleOrDoubleStar.equals("**")) {
                matches.add("");
            }


            while (!scanner.hasNext("\\*") && !scanner.hasNext("\\*\\*")) {
                matches.add(scanner.next());
            }

            singleOrDoubleStar = scanner.next();
            isSingleOrDouble = singleOrDoubleStar.equals("*")
                    | singleOrDoubleStar.equals("**");

            assert isSingleOrDouble : "not matches must begin with * or **";


            if (singleOrDoubleStar.equals("**")) {
                notMatches.add("");
            }

            while (scanner.hasNext()) {
                notMatches.add(scanner.next());
            }

            assertMatches(nfa, matches, notMatches);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given an NFA, a list of Strings that should match (MATCHES), and a list
     * of Strings that shouldn't match (NOTMATCHES), asserts that every String
     * in MATCHES matches and every String in NOTMATCHES doesn't match. */
    private void assertMatches(NFA nfa,
                               List<String> matches, List<String> notMatches) {

        String errString;
        String errMsg;

        for (String s : matches) {
            errString = s.length() == 0 ? "<empty string>" : s;
            errMsg = String.format("expected match for nfa %s and string %s",
                    nfa, errString);

            assertTrue(errMsg, nfa.matches(s));
        }
        for (String s : notMatches) {
            errString = s.length() == 0 ? "<empty string>" : s;
            errMsg = String.format("expected no match for nfa %s and string %s",
                    nfa, errString);

            assertFalse(errMsg, nfa.matches(s));
        }
    }

    @Test
    public void testSimple() {
        testDirectory("testing/simple");
    }

    @Test
    public void testMedium() {
        testDirectory("testing/medium");
    }

    @Test
    public void testHard() {
        testDirectory("testing/hard");
    }
}
