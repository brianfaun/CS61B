package signpost;

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;

import ucb.util.CommandArgs;

/** The main class for the Signpost puzzle.
 *  @author P. N. Hilfinger
 */
public class Main {

    /** The main program.  ARGS may contain the options --seed=NUM,
     *  (random seed); --log (record commands, clicks);
     *  --testing (take puzzles and commands from standard input);
     *  --setup (take puzzles from standard input and commands from GUI);
     *  and --no-display. */
    public static void main(String... args) {

        CommandArgs options =
            new CommandArgs("--seed=(\\d+) --log --setup --testing "
                            + "--no-display --=(.*)",
                            args);
        if (!options.ok()) {
            System.err.println("Usage: java signpost.Main [ --seed=NUM ] "
                               + "[ --setup ] "
                               + "[ --log ] [ --testing ] [ --no-display ]"
                               + " [ INPUT ]");
            System.exit(1);
        }

        if (options.contains("--")) {
            String inpFile = options.getFirst("--");
            try {
                System.setIn(new FileInputStream(inpFile));
            } catch (IOException excp) {
                System.err.printf("Could not open %s%n", inpFile);
                System.exit(1);
            }
        }

        Controller puzzler = getController(options);

        try {
            while (puzzler.solving()) {
                puzzler.solvePuzzle();
            }
        } catch (IllegalStateException excp) {
            System.err.printf("Internal error: %s%n", excp.getMessage());
            System.exit(1);
        }

        if (options.contains("--no-display") || options.contains("--testing")) {
            System.exit(0);
        }

    }

    /** Return an appropriate Controller as indicated by OPTIONS. */
    private static Controller getController(CommandArgs options) {
        GUI gui;
        CommandSource cmds;
        PuzzleSource puzzles;

        if (options.contains("--no-display")) {
            gui = null;
        } else {
            gui = new GUI("Signpost 61B");
        }

        if (gui == null && !options.contains("--testing")) {
            System.err.println("Error: no input source.");
            System.exit(1);
            return null;
        } else if (options.contains("--testing")) {
            TestSource src = new TestSource(new Scanner(System.in));
            cmds = src;
            puzzles = src;
        } else if (options.contains("--setup")) {
            cmds = new GUISource(gui);
            puzzles = new TestSource(new Scanner(System.in));
        } else {
            cmds = new GUISource(gui);
            long seed;
            if (options.contains("--seed")) {
                seed = options.getLong("--seed");
            } else {
                seed = (long) (Math.random() * SEED_RANGE);
            }
            puzzles = new PuzzleGenerator(seed);
        }

        return new Controller(gui, cmds, puzzles,
                              options.contains("--log"),
                              options.contains("--testing"));
    }

    /** Maximum default seed. */
    private static final double SEED_RANGE = 1e12;
}
