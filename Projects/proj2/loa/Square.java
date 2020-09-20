/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.regex.Pattern;

import static loa.Utils.*;

/** Represents a position on a LOA board.  Positions are indexed from
 *  from (0, 0) (lower-left corner) to (BOARD_SIZE - 1, BOARD_SIZE - 1)
 *  (upper-right). Squares are immutable and unique: there is precisely
 *  one square created for each distinct position.  Clients create squares
 *  using the factory method sq, not the constructor.  Because there is a
 *  unique Square object for each position, you can freely use the
 *  cheap == operator (rather than the .equals method) to compare Squares,
 *  and the program does not waste time creating the same square over
 *  and over again.
 *  @author P. N. Hilfinger
 */
final class Square {

    /** The total number of possible rows or columns. */
    static final int BOARD_SIZE = 8;

    /** The total number of possible squares. */
    static final int NUM_SQUARES = BOARD_SIZE * BOARD_SIZE;

    /** The regular expression for a square designation (e.g.,
     *  a3). For convenience, it is in parentheses to make it a
     *  group.  This subpattern may be incorporated into
     *  other patterns that contain square designations (such as
     *  patterns for moves) using SQ.pattern(). */
    static final Pattern SQ = Pattern.compile("([a-h][1-8])");

    /** Return my row position, where 0 is the bottom row. */
    int row() {
        return _row;
    }

    /** Return my column position, where 0 is the leftmost column. */
    int col() {
        return _col;
    }

    /** Return distance (number of squares) to OTHER. */
    int distance(Square other) {
        return Math.max(Math.abs(_row - other._row),
                        Math.abs(_col - other._col));
    }

    /** Return true iff THIS - TO is a valid move. */
    boolean isValidMove(Square to) {
        return this != to
            && (_row == to._row || _col == to._col
                || _row + _col == to._row + to._col
                || _row - _col == to._row - to._col);
    }

    /** Definitions of directions.  DIR[k] = (dcol, drow)
     *  means that to going one step from (col, row) in direction k,
     *  brings us to (col + dcol, row + drow). 0-7 cc, 0 is north */
    private static final int[][] DIR = {
        { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 },
        { -1, -1 }, { -1, 0 }, { -1, 1 }
    };

    /** The increments (delta column, delta row) from one Square to the
     *  next indexed by direction. */
    private static final int[]
        DC = {  0,  1,  1,  1,  0, -1, -1, -1 },
        DR = {  1,  1,  0, -1, -1, -1,  0,  1 };

    /** Returns the delta in columns.
     * @param direction direction from FROM to TO.
     * @return delta columns
     */
    int getDC(int direction) {
        return DC[direction];
    }

    /** Returns the delta in rows.
     * @param direction direction from FROM to TO.
     * @return delta row
     */
    int getDR(int direction) {
        return DR[direction];
    }

    /** Mapping of (dc + 1, dr + 1) to direction, where (dc, dr) are the
     *  column and row displacements of an adjacent square. */
    private static final int[][] DISP_TO_DIR = {
        { 5, 6, 7 }, { 4, -1, 0 }, { 3, 2, 1 }
    };

    /** Return the Square that is STEPS>0 squares away from me in direction
     *  DIR, or null if there is no such square.
     *  DIR = 0 for north, 1 for north-east, 2 for east, etc., up to
     *  7 for north-west. If DIR has another value, return null. */
    Square moveDest(int dir, int steps) {
        if (dir < 0 || dir > 7 || steps <= 0) {
            return null;
        }

        int c = col() + DC[dir] * steps,
            r = row() + DR[dir] * steps;
        if (exists(c, r)) {
            return sq(c, r);
        } else {
            return null;
        }
    }

    /** Return the direction (an int as defined in the documentation
     *  for moveDest) of the move THIS-TO. */
    int direction(Square to) {
        assert isValidMove(to);
        int dc = col() > to.col() ? 0 : col() == to.col() ? 1 : 2,
            dr = row() > to.row() ? 0 : row() == to.row() ? 1 : 2;
        return DISP_TO_DIR[dc][dr];
    }


    /** Return an array of all Squares adjacent to SQ. */
    Square[] adjacent() {
        return ADJACENT[index()];
    }

    @Override
    public String toString() {
        return _str;
    }

    /** Return true iff COL ROW is a legal square. */
    static boolean exists(int col, int row) {
        /* A useful trick: since char is an unsigned type, the value of
         * (char) x < V is the same as x >= 0 && x < V. */
        return (char) row < BOARD_SIZE && (char) col < BOARD_SIZE;
    }

    /** Return the (unique) Square denoting COL ROW. */
    static Square sq(int col, int row) {
        if (!exists(row, col)) {
            error(1, "row or column out of bounds");
        }
        return SQUARES[col][row];
    }

    /** Return the (unique) Square denoting the position in POSN, in the
     *  standard text format for a square (e.g. a4). Return null if POSN
     *  does not denote a valid square designation. */
    static Square sq(String posn) {
        if (SQ.matcher(posn).matches()) {
            return sq(posn.charAt(0) - 'a', posn.charAt(1) - '1');
        }
        return null;
    }

    /** The Square (COL, ROW). */
    private Square(int col, int row) {
        _row = row;
        _col = col;
        _str = String.format("%c%d", (char) ('a' + _col), 1 + _row);
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    /** Return a unique number between 0 and NUM_SQUARES-1, inclusive,
     *  for this Square. All distinct squares have distinct index values.
     */
    int index() {
        return (_row << 3) + _col;
    }

    @Override
    public int hashCode() {
        return index();
    }

    /** The cache of all created squares, by row and column. */
    private static final Square[][] SQUARES =
        new Square[BOARD_SIZE][BOARD_SIZE];

    /** A list of all Squares on a board. */
    static final Square[] ALL_SQUARES = new Square[BOARD_SIZE * BOARD_SIZE];

    static {
        for (int c = 0; c < BOARD_SIZE; c += 1) {
            for (int r = 0; r < BOARD_SIZE; r += 1) {
                Square sq = new Square(c, r);
                ALL_SQUARES[sq.index()] = SQUARES[c][r] = sq;
            }
        }
    }

    /** A mapping of Square index s.index() to arrays of Squares adjacent to
     *  Square s. */
    private static final Square[][] ADJACENT = new Square[ALL_SQUARES.length][];

    static {
        for (Square sq : ALL_SQUARES) {
            int cl = Math.max(0, sq.col() - 1),
                cr = Math.min(sq.col() + 1, BOARD_SIZE - 1),
                rl = Math.max(0, sq.row() - 1),
                rr = Math.min(sq.row() + 1, BOARD_SIZE - 1);
            ADJACENT[sq.index()] =
                new Square[(cr - cl + 1) * (rr - rl + 1) - 1];
            for (int k = 0, r = rl; r <= rr; r += 1) {
                for (int c = cl; c <= cr; c += 1) {
                    if (r == sq.row() && c == sq.col()) {
                        continue;
                    }
                    ADJACENT[sq.index()][k] = sq(c, r);
                    k += 1;
                }
            }
        }
    }

    /** My row and column. */
    private final int _row, _col;

    /** My String denotation. */
    private final String _str;

}
