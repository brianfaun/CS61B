package signpost;

/** Describes a source of Signpost puzzles.
 *  @author P. N. Hilfinger
 */
interface PuzzleSource {

    /** Returns a WIDTH x HEIGHT Model containing a puzzle. Unless
     *  ALLOWFREEENDS, the upper-left square will be numbered 1 and the
     *  lower-right will be numbered with the number of cells in the model. */
    Model getPuzzle(int width, int height, boolean allowFreeEnds);

    /** Reseed the random number generator with SEED. */
    void setSeed(long seed);

}
