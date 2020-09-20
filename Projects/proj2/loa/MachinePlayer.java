/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static loa.Piece.*;
import static loa.Square.ALL_SQUARES;
import static loa.Square.sq;

/** An automated Player.
 *  @author Brian Faun
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        int tact = alpha;
        if (depth != 0 && !board.gameOver()) {
            if (sense == 1) {
                tact = alpha;
            }
            for (int i = 0; i < board.legalMoves().size(); i++) {
                Move amove = board.legalMoves().get(i);
                board.makeMove(amove);
                int bmove = findMove(board, depth - 1, false,
                        -1 * sense, alpha, beta);
                if (tact > bmove && sense != 1 || tact < bmove && sense == 1) {
                    if (saveMove) {
                        _foundMove = amove;
                    }
                    tact = bmove;
                }
                if (sense == 1) {
                    alpha = max(alpha, tact);
                } else {
                    beta = min(beta, tact);
                }
                board.retract();
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            return nextMove(board);
        }
        return tact;
    }

    /** Heuristic helper for findMove().
     * @param b Board
     * @return total
     */
    private int nextMove(Board b) {
        if (b.gameOver()) {
            if (b.winner() == EMP) {
                return 0;
            } else if (b.winner() == WP) {
                return WINNING_VALUE;
            } else {
                return -1 * WINNING_VALUE;
            }
        }
        int total = 0, whiterow = 0, blackrow = 0, whitecol = 0, blackcol = 0;
        int wtotal = 0, btotal = 0;
        for (int i = 0; i < ALL_SQUARES.length; i++) {
            Square sq = ALL_SQUARES[i];
            if (b.get(sq) == BP) {
                blackcol += sq.col();
                blackrow += sq.row();
                wtotal += 1;
            }
            if (b.get(sq) == WP) {
                whitecol += sq.col();
                whiterow += sq.row();
                btotal += 1;
            }
        }
        for (int i = 0; i < ALL_SQUARES.length; i++) {
            Square s = ALL_SQUARES[i];
            if (b.get(s) == BP) {
                total += s.distance(sq(blackcol / btotal, blackrow / btotal));
            }
            if (b.get(s) == WP) {
                total -= s.distance(sq(whitecol / wtotal, whiterow / wtotal));
            }
        }
        return total;
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 1;
    }

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
