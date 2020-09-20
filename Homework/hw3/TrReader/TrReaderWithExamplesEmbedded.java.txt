import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader. */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(0) to TO.charAt(0), etc., leaving other characters
     *  unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        // FILL IN
    }

    /** Reads characters into a portion of an array */
    public int read(char[] cbuf, int off, int len) {
        // Information goes from the reader supplied
        // gets translated, and stored in cbuf[off] through
        // cbuf[off + len]

        // say off were 2, and len were 10
        // e.g. c[2] = translated 0th character
        // c[3] = translated 1st character

    }
    from = 'cbF'
    to = 'CRS'
    cat (from the outside world)

    cbuf: ['F','H','I','J','O'] : Some stuff is here. you overwrite it, who cares about it.

    we call read with cbuf, off = 2, len = 2, someone types cat
    cbuf: ['F', 'H', 'C', 'a']


    // FILL IN
    // NOTE: Until you fill in the right methods, the compiler will
    //       reject this file, saying that you must declare TrReader
    //     abstract.  Don't do that; define the right methods instead!
}


