package signpost;

import java.util.ArrayList;

import static signpost.Utils.*;
import static signpost.Place.*;
import signpost.Model.Sq;

/** The input/output and GUI controller for play of a Signpost puzzle.
 *  @author P. N. Hilfinger. */
public class Controller {

    /** The default number of squares on a side of the board. */
    static final int DEFAULT_SIZE = 4;

    /** Controller for a game represented by MODEL, using COMMANDS as the
     *  the source of commands, and PUZZLES to supply puzzles.  If LOGGING,
     *  prints commands received on standard output.  If TESTING, prints
     *  the board when possibly changed.  If VIEW is non-null, update it
     *  at appropriate points when the model changes. */
    public Controller(View view,
                      CommandSource commands, PuzzleSource puzzles,
                      boolean logging, boolean testing) {
        _view = view;
        _commands = commands;
        _puzzles = puzzles;
        _logging = logging;
        _testing = testing;
        _solving = true;
        _width = _height = DEFAULT_SIZE;
    }

    /** Return true iff we have not received a Quit command. */
    boolean solving() {
        return _solving;
    }

    /** Clear the board and solve one puzzle, until receiving a quit,
     *  new-game, or board-type change request.  Update the viewer with
     *  each visible modification to the model. */
    void solvePuzzle() {
        _model = _puzzles.getPuzzle(_width, _height, _allowFreeEnds);
        initUndo();
        logPuzzle();
        logBoard();
        while (_solving) {
            if (_view != null) {
                _view.update(_model);
            }

            String cmnd = _commands.getCommand();
            if (_logging) {
                System.out.println(cmnd);
            }
            String[] parts = cmnd.split("\\s+");
            switch (parts[0]) {
            case "QUIT": case "Q":
                _solving = false;
                return;
            case "NEW":
                return;
            case "TYPE":
                setType(toInt(parts[1]), toInt(parts[2]),
                        parts.length > 3 && parts[3].equals("FREE"));
                return;
            case "SEED":
                _puzzles.setSeed(toLong(parts[1]));
                break;
            case "CONN": case "C":
                connect(toInt(parts[1]), toInt(parts[2]),
                        toInt(parts[3]), toInt(parts[4]));
                break;
            case "BRK": case "B":
                disconnect(toInt(parts[1]), toInt(parts[2]));
                break;
            case "RESTART":
                restart();
                break;
            case "UNDO": case "U":
                undo();
                break;
            case "REDO": case "R":
                redo();
                break;
            case "SOLVE":
                solve();
                break;
            case "":
                break;
            default:
                System.err.printf("Bad command: '%s'%n", cmnd);
                break;
            }
        }
    }

    /** Connect (X0, Y0) to (X1, Y1).  Has no effect if (X0, Y0) is connected
     *  already, something is already connected to (X1, Y1), or the connection
     *  is not allowed. */
    private void connect(int x0, int y0, int x1, int y1) {
        Sq sq0 = _model.get(x0, y0), sq1 = _model.get(x1, y1);
        if (sq0.connectable(sq1)) {
            sq0.connect(sq1);
            _model.autoconnect();
            saveForUndo();
        }
        logBoard();
    }

    /** Disconnect (X, Y) from its successor and predecessor, if connected.
     *  Otherwise has no effect. */
    private void disconnect(int x, int y) {
        Sq sq = _model.get(x, y),
            next = sq.successor(),
            prev = sq.predecessor();
        int unconnected0 = _model.unconnected();
        if (next != null) {
            sq.disconnect();
        }
        if (prev != null) {
            prev.disconnect();
        }
        _model.autoconnect();
        if (unconnected0 != _model.unconnected()) {
            saveForUndo();
        }
        logBoard();
    }

    /** Restart current puzzle. */
    private void restart() {
        _model.restart();
        _model.autoconnect();
        initUndo();
        logBoard();
    }

    /** Set current puzzle bpard to show a solution. */
    private void solve() {
        _model.solve();
        logBoard();
    }

    /** Set current puzzle type to WIDTH x HEIGHT and with free ends iff FREE.
     */
    private void setType(int width, int height, boolean free) {
        _width = width;
        _height = height;
        _allowFreeEnds = free;
    }

    /** Back up one move, if possible.  Does nothing otherwise. */
    private void undo() {
        if (_undoIndex > 0) {
            _undoIndex -= 1;
            _model = new Model(_undoStack.get(_undoIndex));
        }
        logBoard();
    }

    /** Redo one move, if possible.  Does nothing otherwise. */
    private void redo() {
        if (_undoIndex + 1 < _undoStack.size()) {
            _undoIndex += 1;
            _model = new Model(_undoStack.get(_undoIndex));
        }
        logBoard();
    }

    /** Initialize _undoStack to contain just current model. */
    private void initUndo() {
        _undoStack.clear();
        _undoStack.add(new Model(_model));
        _undoIndex = 0;
    }

    /** Save current board position for possible undo. */
    private void saveForUndo() {
        _undoStack.subList(_undoIndex + 1, _undoStack.size()).clear();
        _undoStack.add(new Model(_model));
        _undoIndex += 1;
    }

    /** If testing, print the contents of the board. */
    private void logBoard() {
        if (_testing) {
            System.out.printf("B[ %dx%d%s%n%s]%n",
                              _model.width(), _model.height(),
                              _model.solved() ? " (SOLVED)" : "", _model);
        }
    }

    /** If logging, print a representation of the puzzle suitable for input
     *  from a TestSource. */
    private void logPuzzle() {
        if (_logging) {
            System.out.printf("PUZZLE%n%d %d%n",
                              _model.width(), _model.height());
            int[][] soln = _model.solution();
            for (int y = _model.height() - 1; y >= 0; y -= 1) {
                for (int x = 0; x < _model.width(); x += 1) {
                    System.out.printf("%d ", soln[x][y]);
                }
                System.out.println();
            }
            for (int y = _model.height() - 1; y >= 0; y -= 1) {
                for (int x = 0; x < _model.width(); x += 1) {
                    Sq sq = _model.get(x, y);
                    if (sq.hasFixedNum()) {
                        System.out.printf("%d ", soln[x][y]);
                    }
                }
            }
            System.out.printf("%nENDPUZZLE%n");
        }
    }

    /** The board. */
    private Model _model;

    /** The sequence of board states, used to implement undo/redo operations.
     *  Item #_undoIndex is always a copy of the current model.  */
    private ArrayList<Model> _undoStack = new ArrayList<>();

    /** Current position in the undoStack of a copy of the current model.
     *  Lower indices are previous models, accessible by undoing, and
     *  higher indices are models accessible by redoing. */
    private int _undoIndex;

    /** Our view of _model. */
    private View _view;

    /** Puzzle dimensions. */
    private int _width, _height;

    /** Input source from standard input. */
    private CommandSource _commands;

    /** Input source from standard input. */
    private PuzzleSource _puzzles;

    /** True while user is still working on a puzzle. */
    private boolean _solving;

    /** True iff we are logging commands on standard output. */
    private boolean _logging;

    /** True iff we are testing the program and printing board contents. */
    private boolean _testing;

    /** True iff we allow generated puzzles to have free ends. */
    private boolean _allowFreeEnds;

}
