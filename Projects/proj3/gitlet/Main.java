package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Brian Faun
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        // FILL THIS IN
        Driver gitlet = new Driver();
        if (args.length == 0) {
            System.err.println("Please enter a command.");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "init":
                gitlet.init();
                return;
            case "add":
                gitlet.add(args[1]);
                return;
            case "commit":
                try {
                    gitlet.commit(args[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Please enter a commit message.");
                }
                return;
            case "checkout":
                if (args.length == 3) {
                    gitlet.fileCheckout(args[2]);
                } else if (args.length == 4) {
                    if (args[2].equals("--")) {
                        gitlet.checkout(args[1], args[3]);
                    } else {
                        System.out.println("Incorrect operands."); return;
                    }
                } else if (args.length == 2) {
                    gitlet.branchCheckout(args[1]);
                }
                return;
            case "log":
                gitlet.log(); return;

    }
}
