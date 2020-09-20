package signpost;

import java.util.Scanner;
import java.util.NoSuchElementException;

import static signpost.Utils.*;

/** A type of InputSource that receives commands from a Scanner. This kind
 *  of source is intended for testing.
 *  @author P. N. Hilfinger
 */
class TestSource implements CommandSource, PuzzleSource {

    /** Provides commands and puzzles from SOURCE.  */
    TestSource(Scanner source) {
        source.useDelimiter("[ \t\n\r(,)]+");
        _source = source;
    }

    /** Returns a command string read from my source. At EOF, returns QUIT.
     *  Allows comment lines starting with "#", which are discarded. */
    @Override
    public String getCommand() {
        while (_source.hasNext()) {
            String line = _source.nextLine().trim().toUpperCase();
            if (!line.startsWith("#")) {
                return line;
            }
        }
        return "QUIT";
    }

    /** Initialize MODEL to a puzzle.  Throws IllegalStateException if there is
     *  no valid puzzle available, or if ALLOWFREEENDS is false and the
     *  puzzle does not have the default starting and ending squares in the
     *  upper-left and lower-right corners. */
    @Override
    public Model getPuzzle(int width, int height, boolean allowFreeEnds) {
        try {
            while (_source.hasNext(".*#.*")) {
                _source.next();
                _source.nextLine();
            }

            if (_source.hasNext("AUTOPUZZLE")) {
                _source.next();
                return _randomPuzzler.getPuzzle(width, height, allowFreeEnds);
            }

            _source.next("PUZZLE");
            int w = _source.nextInt(), h = _source.nextInt();
            if (w != width || h != height) {
                throw badArgs("wrong puzzle size");
            }
            int[][] soln = new int[w][h];
            for (int y = h - 1; y >= 0; y -= 1) {
                for (int x = 0; x < w; x += 1) {
                    soln[x][y] = _source.nextInt();
                }
            }
            if (!allowFreeEnds) {
                if (soln[0][h - 1] != 1 || soln[w - 1][0] != w * h) {
                    throw new NoSuchElementException();
                }
            }
            Model model = new Model(soln);
            model.restart();
            while (_source.hasNextInt()) {
                int n = _source.nextInt();
                for (int x = 0; x < w; x += 1) {
                    for (int y = 0; y < h; y += 1) {
                        if (soln[x][y] == n) {
                            model.get(x, y).setFixedNum(n);
                        }
                    }
                }
            }
            _source.next("ENDPUZZLE");
            return model;
        } catch (NoSuchElementException excp) {
            throw new IllegalStateException("missing or malformed puzzle");
        }
    }

    @Override
    public void setSeed(long seed) {
        _randomPuzzler.setSeed(seed);
    }

    /** Input source. */
    private Scanner _source;
    /** Source for random puzzles. */
    private PuzzleGenerator _randomPuzzler = new PuzzleGenerator(0);
}
