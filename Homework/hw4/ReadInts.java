import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 *  @author Josh Hug and Paul Hilfinger
 */

public class ReadInts {

    /** Takes a String INPUT and prints out all the integers available
     *  from the scanner each on its own line. Throws an exception
     *  if INPUT contains anything other than valid decimal numerals
     *  separated by whitespace, or if any numeral represents a number
     *  out of the range of type int. */
    public static void printInts(String input) {
        Scanner s = new Scanner(input);
        while (s.hasNext()) {
            System.out.println(s.nextInt());
        }
    }

    /** Takes a string INPUT consisting of integer literals separated
     *  by whitespace, and returns all a list of all the ints represented
     *  by those numerals in the order they appear in the string. */
    public static List<Integer> readInts(String input) {
        /* The notations ArrayList<Integer> and List<Integer> indicate
         * list types all of whose items are of type Integer. */
        ArrayList<Integer> L = new ArrayList<Integer>();

        Scanner s = new Scanner(input);
        while (s.hasNext()) {
            int nextInt = s.nextInt();
            L.add(nextInt);
        }
        return L;
    }

    /** Read String INPUT into List but skip any non-integer tokens.
     *  Does not throw an InputMismatchException, even if
     *  the string has non-integer tokens. Returns this list of ints
     *  as a List<Integer>.

     *  Use the hasNext(), next(), nextInt() and hasNextInt() methods
     *  on Scanners. */
    public static List<Integer> smartReadInts(String input) {
        ArrayList<Integer> L = new ArrayList<Integer>();
        Scanner s = new Scanner(input);
        while (s.hasNext()) {
            if (s.hasNextInt()) {
                int nextInt = s.nextInt();
                L.add(nextInt);
            } else {
                s.next();
            }
        }
        return L;
    }
}
