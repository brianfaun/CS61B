/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static loa.Piece.*;
import static loa.Move.mv;
import static loa.Square.*;
import static loa.Main.*;
import static loa.Utils.*;

/** Represents one game of Lines of Action.
 *  @author Brian Faun
 */
class Game {

    /** Number of milliseconds in 1 second. */
    static final int MILLISEC = 1000;
    /** Name of help text resource. */
    static final String HELP_FILE = "loa/HelpText.txt";

    /** Controller for one or more games of LOA, using
     *  MANUALPLAYERTEMPLATE as an exemplar for manual players
     *  (see the Player.create method) and AUTOPLAYERTEMPLATE
     *  as an exemplar for automated players.  Reports
     *  board changes to VIEW at appropriate points.  Uses REPORTER
     *  to report moves, wins, and errors to user. If LOGFILE is
     *  non-null, copies all commands to it. If STRICT, exits the
     *  program with non-zero code on receiving an erroneous move from a
     *  player. */
    Game(View view, PrintStream logFile, Reporter reporter,
         Player manualPlayerTemplate, Player autoPlayerTemplate,
         boolean strict) {
        _view = view;
        _playing = false;
        _logFile = logFile;
        _input = new Scanner(System.in);
        _autoPlayerTemplate = autoPlayerTemplate;
        _manualPlayerTemplate = manualPlayerTemplate;
        _nonplayer = manualPlayerTemplate.create(EMP, this);
        _white = _autoPlayerTemplate.create(WP, this);
        _black = _manualPlayerTemplate.create(BP, this);
        _reporter = reporter;
        _strict = strict;
    }

    /** Return the current board. */
    Board getBoard() {
        return _board;
    }

    /** Quit the game. */
    private void quit() {
        System.exit(0);
    }

    /** Return a move or command from the standard input, after prompting if
     *  PROMPT. */
    String readLine(boolean prompt) {
        if (prompt) {
            prompt();
        }
        if (_input.hasNextLine()) {
            return _input.nextLine().trim();
        } else {
            return null;
        }
    }

    /** Print a prompt for a move. */
    private void prompt() {
        if (_playing) {
            System.out.print(_board.turn().abbrev().charAt(0));
        } else {
            System.out.print("-");
        }
        System.out.print("> ");
        System.out.flush();
    }

    /** Describes a command with up to three arguments. */
    private static final Pattern COMMAND_PATN =
        Pattern.compile("(#|\\S+)\\s*(\\S*)\\s*(\\S*)\\s*(\\S*).*");

    /** Process the command on LINE. */
    private void processCommand(String line) {
        line = line.trim();
        if (line.length() == 0) {
            return;
        }
        if (_logFile != null) {
            _logFile.println(line);
            _logFile.flush();
        }
        Matcher command = COMMAND_PATN.matcher(line);
        if (command.matches()) {
            switch (command.group(1).toLowerCase()) {
            case "#":
                break;
            case "new":
                _board.clear();
                _playing = true;
                break;
            case "dump":
                System.out.printf("%s%n", _board);
                break;
            case "manual":
                manualCommand(command.group(2).toLowerCase());
                break;
            case "auto":
                autoCommand(command.group(2).toLowerCase());
                break;
            case "quit":
                quit();
                break;
            case "seed":
                seedCommand(command.group(2));
                break;
            case "set":
                setCommand(command.group(2), command.group(3).toLowerCase(),
                           command.group(4).toLowerCase());
                break;
            case "limit":
                limitCommand(command.group(2));
                break;
            case "?": case "help":
                help();
                break;
            default:
                if (!processMove(line)) {
                    error("unknown command: %s%n", line);
                }
                break;
            }
        }
    }

    /** Return true iff white is a manual player. */
    boolean manualWhite() {
        return _white.isManual();
    }

    /** Return true iff black is a manual player. */
    boolean manualBlack() {
        return _black.isManual();
    }

    /** Report error by calling reportError(FORMAT, ARGS) on my reporter. */
    void reportError(String format, Object... args) {
        _reporter.reportError(format, args);
    }

    /** Report note by calling reportNote(FORMAT, ARGS) on my reporter. */
    void reportNote(String format, Object... args) {
        _reporter.reportNote(format, args);
    }

    /** Report move by calling reportMove(MOVE) on my reporter. */
    void reportMove(Move move) {
        _reporter.reportMove(move);
    }

    /** Set player PLAYER ("white" or "black") to be a manual player. */
    private void manualCommand(String player) {
        switch (player) {
        case "white":
            _white = _manualPlayerTemplate.create(WP, this);
            break;
        case "black":
            _black = _manualPlayerTemplate.create(BP, this);
            break;
        default:
            error("unknown player: %s%n", player);
        }
    }

    /** Set player PLAYER ("white" or "black") to be an automated player. */
    private void autoCommand(String player) {
        switch (player) {
        case "white":
            _white = _autoPlayerTemplate.create(WP, this);
            break;
        case "black":
            _black = _autoPlayerTemplate.create(BP, this);
            break;
        default:
            error("unknown player: %s%n", player);
        }
    }

    /** Seed random-number generator with SEED (as a long). */
    private void seedCommand(String seed) {
        try {
            _randomSource.setSeed(Long.parseLong(seed));
        } catch (NumberFormatException excp) {
            error("Invalid number: %s%n", seed);
        }
    }

    /** Set square S to CONTENT ('black', 'white', or '-'), and next player
        to move to NEXTPLAYER: 'black' or 'white'. */
    private void setCommand(String S, String content, String nextPlayer) {
        try {
            Piece p = Piece.playerValueOf(content);
            Piece next = Piece.playerValueOf(nextPlayer);
            if (next == EMP) {
                error("invalid next player: -");
            } else {
                _board.set(sq(S), p, next);
            }
        } catch (IllegalArgumentException excp) {
            error("invalid arguments to set: set %s %s %s%n", S, content,
                  nextPlayer);
        }
    }

    /** Set the corrent move limit according to the numeral in LIMIT.  LIMIT
     *  must be a valid numeral that is greater than the current number of
     *  moves by either player in the current game. */
    private void limitCommand(String limit) {
        try {
            _board.setMoveLimit(Integer.parseInt(limit));
        } catch (NumberFormatException excp) {
            throw new IllegalArgumentException("badly formed numeral");
        }
    }

    /** Perform the move designated by LINE, if a valid move.  Return
     *  true iff LINE has the syntax of a move. */
    private boolean processMove(String line) {
        Move move = mv(line);
        if (move == null) {
            return false;
        } else if (!_playing) {
            error("no game in progress%n");
        } else if (!_board.isLegal(move)) {
            error("illegal move: %s%n", line);
        } else {
            _board.makeMove(move);
        }
        return true;
    }

    /** Play this game, printing any results. */
    public void play() {
        _board = new Board();
        _playing = true;

        while (true) {
            try {
                String next;
                _view.update(this);
                if (_board.gameOver() && _playing) {
                    announceWinner();
                    _playing = false;
                }
                if (_playing) {
                    switch (_board.turn()) {
                    case WP:
                        next = _white.getMove();
                        break;
                    case BP:
                        next = _black.getMove();
                        break;
                    default:
                        throw new Error("Unreachable statement");
                    }
                } else {
                    next = _nonplayer.getMove();
                }
                if (next == null) {
                    return;
                } else {
                    processCommand(next);
                }
            } catch (IllegalArgumentException excp) {
                System.err.printf("Error: %s%n", excp.getMessage());
            }
        }
    }

    /** Print an announcement of the winner.  Requires that the game has been
     *  won. */
    private void announceWinner() {
        switch (_board.winner()) {
        case BP:
            _reporter.reportNote("Black wins.");
            break;
        case WP:
            _reporter.reportNote("White wins.");
            break;
        default:
            _reporter.reportNote("Tie game.");
            break;
        }
    }

    /** Return an integer r, 0 <= r < N, randomly chosen from a
     *  uniform distribution using the current random source. */
    int randInt(int n) {
        return _randomSource.nextInt(n);
    }

    /** Print a help message. */
    void help() {
        Main.printResource(HELP_FILE);
    }

    /** The official game board. */
    private Board _board;

    /** The current templates for manual and automated players. */
    private Player _autoPlayerTemplate, _manualPlayerTemplate;

    /** Player of white pieces. */
    private Player _white;
    /** Player of black pieces. */
    private Player _black;
    /** A player to handle commands when game not started. Does not
     *  actually generate moves. */
    private Player _nonplayer;

    /** A source of pseudo-random numbers, primed to deliver the same
     *  sequence in any Game with the same seed value. */
    private Random _randomSource = new Random();

    /** True if actually playing (game started and not stopped or finished).
     */
    private boolean _playing;

    /** The object that is displaying the current game. */
    private View _view;

    /** Log file, or null if absent. */
    private PrintStream _logFile;

    /** Input source. */
    private Scanner _input;

    /** Reporter for messages and errors. */
    private Reporter _reporter;

    /** If true, command errors cause termination with error exit
     *  code. */
    private boolean _strict;
}
