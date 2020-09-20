package gitlet;
import java.io.*;
import java.util.*;

/** Driver for gitlet.
 * @author Brian Faun
 */
public class Driver {

    /** New Driver. */
    public Driver() {

    }

    /** New Gitlet. */
    public void init() {
        File git = new File(".gitlet");
        if (git.mkdir()) {

        } else {
            System.out.println("A gitlet version-control "
                    + "system already exists in the current directory.");
        }
    }
}
