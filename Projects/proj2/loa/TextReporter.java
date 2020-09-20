/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

/** A Reporter that uses the standard output for messaeges.
 *  @author P. N. Hilfinger
 */
class TextReporter implements Reporter {

    @Override
    public void reportError(String fmt, Object... args) {
        System.err.printf(fmt, args);
        System.err.println();
    }

    @Override
    public void reportNote(String fmt, Object... args) {
        System.out.printf("* " + fmt, args);
        System.out.println();
    }

    @Override
    public void reportMove(Move move) {
        System.out.printf("* %s%n", move);
    }
}
