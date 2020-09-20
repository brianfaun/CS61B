package signpost;

/** Describes a source of input commands.  The possible text commands are as
 *  follows (parts of a command are separated by whitespace):
 *    - TYPE w h [FREE]: Replace the current board with one that is w cells
 *                wide and h cells high.  Then start a new puzzle, allowing
 *                free ends iff FREE is present.  Requires that w, h >= 3 and
 *                that they are properly formed numerals.
 *    - NEW:      Start a new puzzle with current parameters.
 *    - RESTART:  Clear all work on the current puzzle, returning to its initial
 *                state.
 *    - CONN X0 Y0 X1 Y1:
 *                Connect square (X0, Y0) to (X1, Y1).
 *    - BRK X0 Y0:
 *                Remove any connections to and from (X0, Y0).
 *    - UNDO:     Go back one move.
 *    - REDO:     Go forward one previously undone move.
 *    - SEED s:   Set a new random seed.
 *    - SOLVE:    Show sequence numbers of a solution.
 *    - QUIT:     Exit the program.
 *  @author P. N. Hilfinger
 */
interface CommandSource {

    /** Returns one command string, trimmed of preceding and following
     *  whitespace and converted to upper case. */
    String getCommand();

}
