import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Brian Faun
 */
public class TrReader extends Reader {
    /**
     * Class variables.
     * @variable _str str
     */
    private Reader _str;
    /**
     * @variable _from from
     * @variable _to  to
     */
    private String _from, _to;

    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length.
     *
     * @param str string
     * @param from from-string
     * @param to to-string
     */
    public TrReader(Reader str, String from, String to) {
        assert from.length() == to.length();
        _str = str;
        _from = from;
        _to = to;
    }

    /**
     * Reads from inputted characters.
     * @param in character in
     * @return  in
     * @throws IOException
     */
    public char read(char in) throws IOException {
        for (int i = 0; i < _from.length(); i++) {
            if (in == _from.charAt(i)) {
                return _to.charAt(i);
            }
        }
        return in;
    }

    /**
     * Reads into CBUF from index OFF for LEN characters.
     * @param cbuf buffer array
     * @param off offset
     * @param len length
     * @return count
     * @throws IOException
     */
    public int read(char[] cbuf, int off, int len) throws IOException {
        int count = _str.read(cbuf, off, len);
        for (int i = off; i < off + len; i++) {
            cbuf[i] = read(cbuf[i]);
        }
        return count;
    }

    /**
     * Closes the stream. Generates IOException if there are further
     * attempts to read from stream.
     *
     * @exception IOException
     */
    public void close() throws IOException {
        _str.close();
    }
}

