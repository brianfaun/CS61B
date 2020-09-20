/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

/** A Player that takes input from a GUI.
 *  @author P. N. Hilfinger
 */
class GUIPlayer extends Player implements Reporter {

    /** A new GUIPlayer that takes moves and commands from GUI.  */
    GUIPlayer(GUI gui) {
        this(null, null, gui);
    }

    /** A new GUIPlayer playing PIECE under control of CONTROLLER, taking
     *  moves and commands from GUI. */
    GUIPlayer(Piece piece, Game controller, GUI gui) {
        super(piece, controller);
        _gui = gui;
    }

    @Override
    Player create(Piece piece, Game game) {
        return new GUIPlayer(piece, game, _gui);
    }

    @Override
    boolean isManual() {
        return true;
    }

    @Override
    String getMove() {
        while (true) {
            String command;
            command = getGame().readLine(false);
            if (command == null) {
                command = _gui.readCommand();
            }
            Move move = Move.mv(command);
            if (move == null || getBoard().isLegal(move)) {
                return command;
            }
        }
    }

    @Override
    public void reportError(String fmt, Object... args) {
        _gui.reportError(fmt, args);
    }

    @Override
    public void reportNote(String fmt, Object... args) {
        _gui.reportNote(fmt, args);
    }

    @Override
    public void reportMove(Move unused) {
    }

    /** The GUI I use for input. */
    private GUI _gui;
}
