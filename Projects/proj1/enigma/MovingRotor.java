package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Brian Faun
 */
class MovingRotor extends Rotor {
    /** Position of notches. */
    private String _notches;

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            char a = _notches.charAt(i);
            if (alphabet().toChar(setting()) == a) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        int next = permutation().wrap(setting() + 1);
        super.set(next);
    }
}
