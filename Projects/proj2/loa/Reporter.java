/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

/** An object that reports errors and other notifications to the user.
 *  @author P. N. Hilfinger
 */
interface Reporter {

    /** Report an error as indicated by FORMAT and ARGS, which have
     *  the same meaning as in String.format. */
    void reportError(String format, Object... args);

    /** Display a message indicated by FORMAT and ARGS, which have
     *  the same meaning as in String.format. */
    void reportNote(String format, Object... args);

    /** Display MOVE as needed. */
    void reportMove(Move move);

}

