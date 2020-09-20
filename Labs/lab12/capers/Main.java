package capers;

import java.io.File;
import java.io.IOException;

/** Canine Capers: A Gitlet Prelude.
 * @author Sean Dooher
*/
public class Main {
    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = new File(".capers");

   /** Story text file. */
    static final File STORY = Utils.join(CAPERS_FOLDER, "story.txt");

    /**
     * Runs one of three commands:
     * story [text] -- Appends "text" + a newline to a story file in the
     *                 .capers directory. Additionally, prints out the
     *                 current story.
     *
     * dog [name] [breed] [age] -- Persistently creates a dog with
     *                             the specified parameters; should also print
     *                             the dog's toString(). Assume dog names are
     *                             unique.
     *
     * birthday [name] -- Advances a dog's age persistently
     *                    and prints out a celebratory message.
     *
     * All persistent data should be stored in a ".capers"
     * directory in the current working directory.
     *
     * Recommended structure (you do not have to follow):
     *
     * *YOU SHOULD NOT CREATE THESE MANUALLY,
     *  YOUR PROGRAM SHOULD CREATE THESE FOLDERS/FILES*
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     *
     * @param args arguments from the command line
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            exitWithError("Must have at least one argument");
        }
        setupPersistence();
        switch (args[0]) {
            case "story":
                writeStory(args);
                break;
            case "dog":
                makeDog(args);
                break;
            case "birthday":
                celebrateBirthday(args);
                break;
            default:
                exitWithError(String.format("Unknown command: %s", args[0]));
        }
        return;
    }

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     *
     */
    public static void setupPersistence() {
        CAPERS_FOLDER.mkdir();
        Dog.DOG_FOLDER.mkdir();
        try {
            STORY.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param args Array in format: {'story', text}
     */
    public static void writeStory(String[] args) {
        validateNumArgs("story", args, 2);
        Utils.writeContents(STORY, Utils.readContentsAsString(STORY) + args[1] + "\n");
        System.out.println(Utils.readContentsAsString(STORY));
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     * @param args Array in format: {'story', name, breed, age}
     */
    public static void makeDog(String[] args) {
        validateNumArgs("dog", args, 4);
        Dog _dog = new Dog (args[1], args[2], Integer.parseInt(args[3]));
        _dog.saveDog();
        System.out.println(_dog.toString());
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param args Array in format: {'birthday', name}
     */
    public static void celebrateBirthday(String[] args) {
        validateNumArgs("birthday", args, 2);
        Dog _dog = Dog.fromFile(args[1]);
        _dog.haveBirthday();
        _dog.saveDog();
    }

    /**
     * Prints out MESSAGE and exits with error code -1.
     * Note:
     *     The functionality for erroring/exit codes is different within Gitlet
     *     so DO NOT use this as a reference.
     *     Refer to the spec for more information.
     * @param message message to print
     */
    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(-1);
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
