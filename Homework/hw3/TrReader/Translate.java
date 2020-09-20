import java.io.IOException;
import java.io.StringReader;

/** String translation.
 *  @author Brian Faun
 */
public class Translate {
    /** This method should return the String S, but with all characters that
     *  occur in FROM changed to the corresponding characters in TO.
     *  FROM and TO must have the same length.
     *  NOTE: You must use your TrReader to achieve this. */
    static String translate(String S, String from, String to) {
        /* NOTE: The try {...} catch is a technicality to keep Java happy. */
        char[] buffer = new char[S.length()];
        try {
            StringReader sReader = new StringReader(S);
            TrReader translator = new TrReader(sReader, from, to);
            int c = translator.read(buffer, 0, S.length());
            String str = new String(buffer);
            return str;
        } catch (IOException e) {
            return null;
        }
    }

    /** Ad-hoc Tests.
     * @param args arguments
     */
    public static void main(String [] args) {
        System.out.println(translate("My Name Is Brian", "MNIB", "mnib"));
        System.out.println(translate("chipmunks", "chipmunk", "flamingo"));

    }
}
