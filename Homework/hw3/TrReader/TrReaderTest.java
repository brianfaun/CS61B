import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Cursory test of the TrReader class.
 *  @author Josh Hug
 *  @author P. N. Hilfinger
 */
public class TrReaderTest {

    /** This test reads in the source code for itself.
     * Then feeds it into TrReader. If it works, you get the
     * source for this test, but scrambled.
     *
     * NOTE: If you're testing in IntelliJ, you might need to change the
     * parameter to the FileReader to "TrReader/TrReaderTest.java".
     *
     * NOTE: Remember to change the parameter back to "TrReaderTest.java" when
     * you are ready to submit; otherwise, make check will not work correctly.
     *
     * */
    @Test
    public void testSource() throws IOException {
        Reader r = makeStringReader(new FileReader("TrReaderTest.java"), 4096);

        TrReader trR = new TrReader(r, "import jav.", "josh hug___");
        char[] cbuf = new char[250];

        assertEquals(250, trR.read(cbuf));
        String result = new String(cbuf);
        assertEquals(TRANSLATION.substring(0, 250), result);
    }

    /** Return a StringReader that contains the contents delivered by R,
     *  up to MAXSIZE characters.  All end-of-line sequences in the
     *  characters read are canonicalized to '\n' (this has an effect only
     *  on Windows). */
    private Reader makeStringReader(Reader r, int maxSize) throws IOException {
        char[] buf = new char[maxSize];
        r.read(buf);
        String result = new String(buf);
        return new StringReader(result.replace("\r\n", "\n"));
    }

    public static void main(String[] args) {
        System.exit(textui.runClasses(TrReaderTest.class));
    }

    static final String TRANSLATION =
        "josh hug____jh_FjleRe_de ;\n"
        + "josh hug____jh_Re_de ;\n"
        + "josh hug____jh_Sh jngRe_de ;\n"
        + "josh hug____jh_IOExceshjhn;\n"
        + "\n"
        + "josh huh g_gunjh_Tesh;\n"
        + "josh hush_hjcuh g_gunjh_Asse h_*;\n"
        + "\n"
        + "josh huucb_gunjh_hexhuj;\n"
        + "\n"
        + "/**uCu sh yuheshuhfuhheuT Re_de ucl_ss_\n"
        + "u*uu@_uhhh uJhshuHjosh hug____jh_FjleRe_de ;\n"
        + "josh hug____jh_Re_de ;\n"
        + "josh hug____jh_Sh jngRe_de ;\n"
        + "josh hug____jh_IOExceshjhn;\n"
        + "\n"
        + "josh huh g_gunjh_Tesh;\n"
        + "josh hush_hjcuh g_gunjh_Asse h_*;\n"
        + "\n"
        + "josh huucb_gunjh_hexhuj;\n"
        + "\n"
        + "/**uCu sh yuheshuhfuhheuT Re_de ucl_ss_\n"
        + "u*uu@_uhhh uJhshuH";
}
