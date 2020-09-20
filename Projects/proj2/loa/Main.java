/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;

import ucb.util.CommandArgs;

import java.util.List;

import static loa.Piece.*;
import static loa.Utils.*;

/** Main class of the Lines of Action program.
 * @author P. N. Hilfinger and
 */
public class Main {

    /** Version designator. */
    public static final String VERSION = "2.0";
    /** Name of resource containing usage message. */
    private static final String USAGE = "loa/Usage.txt";

    /** Number of seconds in one minute. */
    static final double MIN = 60.0;

    /** The main Lines of Action.  ARGS are as described in the
     *  file Usage.
     */
    public static void main(String... args) {
        CommandArgs options =
            new CommandArgs("--debug=(\\d+){0,1} --display{0,1} --strict{0,1} "
                            + "--log={0,1} --=(.*){0,2}",
                            args);

        if (!options.ok()) {
            usage();
        }

        setStrict(options.contains("--strict"));

        if (options.contains("--debug")) {
            setMessageLevel(options.getInt("--debug"));
        }

        List<String> files = options.get("--");
        if (!files.isEmpty()) {
            try {
                if (!files.get(0).equals("-")) {
                    System.setIn(new FileInputStream(files.get(0)));
                }
                if (files.size() > 1) {
                    FileOutputStream out = new FileOutputStream(files.get(1));
                    System.setOut(new PrintStream(out, true));
                }
            } catch (IOException excp) {
                System.err.printf("Could not open file: %s%n",
                                  excp.getMessage());
                System.exit(1);
            }
        } else if (options.contains("--display")) {
            try {
                System.in.close();
                System.err.println("Display option not implemented.");
                System.exit(2);
            } catch (IOException dummy) {
                /* Ignore IOException. */
            }
        }

        Game game = getGame(options);
        if (!options.contains("--display")) {
            System.out.printf("Lines of Action.  Version %s.%n"
                              + "Type ? for help.%n", VERSION);
        }
        game.play();
    }

    /** Return an appropriate Controller as indicated by OPTIONS. */
    private static Game getGame(CommandArgs options) {
        Player manualPlayer;
        GUI gui;
        PrintStream log;
        View view;
        Reporter reporter;

        if (options.contains("--display")) {
            gui = new GUI("LOA 61B");
            reporter = gui;
            gui.display(true);
            view = gui;
            manualPlayer = new GUIPlayer(gui);
        } else {
            gui = null;
            reporter = new TextReporter();
            view = new NullView();
            manualPlayer = new HumanPlayer();
        }

        log = null;
        if (options.contains("--log")) {
            try {
                log = new PrintStream(options.getFirst("--log"));
            } catch (IOException excp) {
                error(1, "Could not open log file");
            }
        }

        return new Game(view, log, reporter, manualPlayer,
                        new MachinePlayer(), options.contains("--strict"));
    }

    /** Print brief description of the command-line format. */
    static void usage() {
        printResource(USAGE);
        System.exit(1);
    }

    /** Print the contents of the resource named NAME on the standard error. */
    static void printResource(String name) {
        try {
            InputStream resourceStream =
                Main.class.getClassLoader().getResourceAsStream(name);
            BufferedReader str =
                new BufferedReader(new InputStreamReader(resourceStream));
            for (String s = str.readLine(); s != null; s = str.readLine())  {
                System.err.println(s);
            }
            str.close();
        } catch (IOException excp) {
            System.out.println("No help found.");
        }
    }

}
