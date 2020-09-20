/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import static loa.Piece.*;

/** Represents a player.  Extensions of this class do the actual playing.
 *  @author P. N. Hilfinger
 */
public abstract class Player {

    /** A player that plays the SIDE pieces in GAME. */
    Player(Piece side, Game game) {
        _side = side;
        _game = game;
    }

    /** Return my next move from the current position in getBoard(), assuming
     *  that side() == getBoard.turn(). Assumes the game has not ended. */
    abstract String getMove();

    /** Return which side I'm playing. */
    Piece side() {
        return _side;
    }

    /** Return the board I am using. */
    Board getBoard() {
        return _game.getBoard();
    }

    /** Return the game I am playing. */
    Game getGame() {
        return _game;
    }

    /** Return true iff I am a manual (human or non-automated) player. */
    abstract boolean isManual();

    /** A factory method that returns a Player with my concrete type that
     *  plays the side indicated by PIECE in a game controlled by
     *  GAME.  This typically will call the constructor for the class
     *  it appears in. */
    abstract Player create(Piece piece, Game game);

    /** This player's side. */
    private final Piece _side;
    /** The game this player is part of. */
    private Game _game;

}
