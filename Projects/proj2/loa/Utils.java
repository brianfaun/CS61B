/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

/** Miscellaneous utilties.
 *  @author P. N. Hilfinger */

class Utils {

    /** Report an error and exit program with EXIT as the
     *  exit code if _strict is false; otherwise exit with code 2.
     *  FORMAT is the message format (as for printf), and ARGS any
     *  additional arguments. */
    static void error(int exit, String format, Object... args) {
        error(format, args);
        System.exit(exit);
    }

    /** Report an error.  If _strict, then exit (code 2).  Otherwise,
     *  simply return. FORMAT is the message format (as for printf),
     *  and ARGS any additional arguments. */
    static void error(String format, Object... args) {
        System.err.print("Error: ");
        System.err.printf(format, args);
        if (_strict) {
            System.exit(2);
        }
    }

    /** Set "strict" mode to STRICT, which causes any error to exit the
     *  program. */
    static void setStrict(boolean strict) {
        _strict = strict;
    }

    /** Set the message level for this package to LEVEL.  The debug() routine
     *  (below) will print any message with a positive level that is <= LEVEL.
     *  Initially, the level is 0. */
    public static void setMessageLevel(int level) {
        _messageLevel = level;
    }

    /** Returns the current message level, as set by setMessageLevel. */
    public static int getMessageLevel() {
        return _messageLevel;
    }

    /** Print a message on the standard error if LEVEL is positive and <= the
     *  current message level. FORMAT and ARGS are as for the .printf
     *  methods. */
    public static void debug(int level, String format, Object... args) {
        if (level > 0 && level <= _messageLevel) {
            System.err.printf(format, args);
            System.err.println();
        }
    }

    /** The current package-wide message level. */
    private static int _messageLevel = 0;

    /** Setting of "strict" mode, in which erroneous inputs cause the program
     *  to error out with exit code 2. */
    private static boolean _strict;

}
