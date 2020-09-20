/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import afu.org.checkerframework.checker.igj.qual.I;
import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BindInfo;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Brian Faun
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
        _subsetsInitialized = false;
        _winner = null;
        _winnerKnown = false;
        _moves.clear();
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                _board[sq(x, y).index()] = contents[y][x];
            }
        }
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            _board[i] = board._board[i];
        }
        _turn = board.turn();
        _subsetsInitialized = board._subsetsInitialized;
        _moveLimit = board._moveLimit;
        _moves.clear();
        _moves.addAll(board._moves);
        _whiteRegionSizes.addAll(board._whiteRegionSizes);
        _blackRegionSizes.addAll(board._blackRegionSizes);
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        if (next != null) {
            _turn = next;
        }
        _board[sq.index()] = v;
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. This function assumes that
     *  MOVE.isCapture() will return false.  If it saves the move for
     *  later retraction, makeMove itself uses MOVE.captureMove() to produce
     *  the capturing move. */
    void makeMove(Move move) {
        assert isLegal(move);
        _winner = null;
        _winnerKnown = false;
        _subsetsInitialized = false;
        Piece p1 = get(move.getTo());
        Piece p2 = get(move.getFrom()).opposite();
        if (p1.equals(p2)) {
            move = move.captureMove();
        }
        set(move.getTo(), get(move.getFrom()), get(move.getFrom()).opposite());
        set(move.getFrom(), EMP);
        _moves.add(move);
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        _subsetsInitialized = false;
        _winnerKnown = false;
        _winner = null;
        Move m = _moves.remove(movesMade() - 1);
        if (!m.isCapture()) {
            set(m.getFrom(), get(m.getTo()));
            set(m.getTo(), EMP, turn().opposite());
        } else {

            set(m.getFrom(), get(m.getTo()), turn().opposite());
            set(m.getTo(), turn().opposite());
        }
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        if (turn() != get(from) || blocked(from, to)) {
            return false;
        }
        if (!exists(to.col(), to.row()) || !from.isValidMove(to)) {
            return false;
        }
        int moves = 1, index = 0, i2 = 0;
        int crrMoves = from.distance(to);
        int dir = from.direction(to);
        while (from.moveDest(dir, ++index) != null) {
            Piece p1 = get(from.moveDest(dir, index));
            if (p1.equals(WP) || p1.equals(BP)) {
                moves++;
            }
        }
        int fromDir = (BOARD_SIZE + (dir - 4) % BOARD_SIZE) % BOARD_SIZE;
        while (from.moveDest(fromDir, ++i2) != null) {
            Piece p = get(from.moveDest(fromDir, i2));
            if (!p.equals(EMP)) {
                moves++;
            }
        }
        if (moves == crrMoves) {
            return true;
        }
        return false;
    }


    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        ArrayList<Move> legalmoves = new ArrayList<>();
        for (Square from : ALL_SQUARES) {
            if (get(from).equals(turn())) {
                for (Square to: ALL_SQUARES) {
                    if (isLegal(from, to)) {
                        if (get(to).equals(turn().opposite())) {
                            legalmoves.add(Move.mv(from, to, true));
                        } else {
                            legalmoves.add(Move.mv(from, to));
                        }
                    }
                }
            }
        }
        return legalmoves;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            boolean wcont = piecesContiguous(WP);
            boolean bcont = piecesContiguous(BP);
            if (wcont) {
                if (bcont) {
                    _winner = turn().opposite();
                } else {
                    _winner = WP;
                }
            } else if (bcont) {
                _winner = BP;
            } else if (_moveLimit <= movesMade()) {
                _winner = EMP;
            }
            _winnerKnown = true;
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        if (get(from).equals(get(to))) {
            return true;
        } else if (!from.isValidMove(to)) {
            return true;
        }
        Piece p = get(from).opposite();
        int dir = from.direction(to);
        int dist = from.distance(to);
        for (int i = 1; i < dist; i++) {
            if (p.equals(get(from.moveDest(dir, i)))) {
                return true;
            }
        }
        return false;
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        int size = 0;
        if ((visited[sq.row()][sq.col()]) || (get(sq) != p)) {
            return size;
        } else {
            visited[sq.row()][sq.col()] = true;
            size = 1;
        }
        for (Square s : sq.adjacent()) {
            size += numContig(s, visited, p);
        }
        return size;
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _subsetsInitialized = true;
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (Square sq : ALL_SQUARES) {
            if (!visited[sq.row()][sq.col()] && get(sq).equals(WP)) {
                _whiteRegionSizes.add(numContig(sq, visited, WP));
            }
            if (!visited[sq.row()][sq.col()] && get(sq).equals(BP)) {
                _blackRegionSizes.add(numContig(sq, visited, BP));
            }
        }
        _whiteRegionSizes.sort(Collections.reverseOrder());
        _blackRegionSizes.sort(Collections.reverseOrder());
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}
