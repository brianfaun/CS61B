/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import static loa.Board.*;
import static loa.Piece.*;
import static loa.Square.*;

/** A move in Lines of Action.  A move denotes a "from" and "to" square.
 *  It may also be used to indicate whether a piece is captured as a result
 *  of the move (this is relevant when Moves are used to record moves,
 *  allowing these moves to be undone.)
 *  @author P. N. Hilfinger */
final class Move {

    /* Implementation note: We create moves by means of static "factory
     * methods" all named mv, which in turn use the single (private)
     * constructor.  There is a unique Move for each combination of arguments.
     * As a result the default equality operation (same as ==) will
     * work. */

    /** Return a move denoted S.  When CAPTURE is true, inicates a move
     *  that results in a capture. Returns null if S is not a
     *  valid move. */
    static Move mv(String s, boolean capture) {
        s = s.trim();
        if (s.matches("[a-h][1-8]-[a-h][1-8]\\b.*")) {
            return mv(sq(s.substring(0, 2)), sq(s.substring(3, 5)),
                      capture);
        } else {
            return null;
        }
    }

    /** Return a move denoted MOVE with isCapture() false. */
    static Move mv(String move) {
        return mv(move, false);
    }

    /** Return a move from FROM to TO. If CAPTURE, indicates a move that
     *  captures a piece.  Returns null if FROM-TO is not a valid move. */
    static Move mv(Square from, Square to, boolean capture) {
        if (from == null || to == null) {
            return null;
        }
        return _moves[from.index()][to.index()][capture ? 1 : 0];
    }

    /** Return a move from FROM to TO with isCapture() false. */
    static Move mv(Square from, Square to) {
        return mv(from, to, false);
    }

    /** Return the Square moved from. */
    Square getFrom() {
        return _from;
    }

    /** Return the Square moved to. */
    Square getTo() {
        return _to;
    }

    /** Return true if this Move is a capture. */
    boolean isCapture() {
        return _capture;
    }

    /** Return the same Move as this, with isCapture() true. */
    Move captureMove() {
        return _captureMove;
    }

    /** Return the length of this move (number of squares moved). */
    int length() {
        return _from.distance(_to);
    }

    @Override
    public String toString() {
        return String.format("%s-%s", getFrom(), getTo());
    }

    /** Construct a Move from FROM to TO, capturing iff CAPTURE. */
    private Move(Square from, Square to, boolean capture) {
        assert from.isValidMove(to);
        _from = from; _to = to;
        _capture = capture;
        _captureMove = _capture ? this : new Move(from, to, true);
    }

    /** Starting and destination Squares. */
    private final Square _from, _to;
    /** True iff this Move records a capture. */
    private final boolean _capture;
    /** When this is not a capture move, the Move with the same getFrom()
     *  and getTo() as this, but with isCapture() true. */
    private final Move _captureMove;

    /** The set of all possible Moves, indexed by row and column of
     *  start, row and column of destination, and whether Move denotes
     *  a capture. */
    private static Move[][][] _moves = new Move[NUM_SQUARES][NUM_SQUARES][2];

    static {
        for (int c = 0; c < BOARD_SIZE; c += 1) {
            for (int r = 0; r < BOARD_SIZE; r += 1) {
                Square from = sq(c, r);
                int fromi = from.index();
                for (int dir = 0; dir < 8; dir += 1) {
                    for (Square to = from.moveDest(dir, 1); to != null;
                         to = to.moveDest(dir, 1)) {
                        int toi = to.index();
                        _moves[fromi][toi][0] = new Move(from, to, false);
                        _moves[fromi][toi][1]
                            = _moves[fromi][toi][0]._captureMove;
                    }
                }
            }
        }
    }

}
