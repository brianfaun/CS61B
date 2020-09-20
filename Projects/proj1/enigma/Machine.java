package enigma;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Brian Faun
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet a, int numRotors, int pawls, Collection<Rotor> allRotors) {
        _alphabet = a;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors.toArray();
        _rotors = new Rotor[_numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (int j = 0; j < _allRotors.length; j++) {
                if ((rotors[i].toString()).equals((
                        ((Rotor) _allRotors[j]).name()))) {
                    _rotors[i] = (Rotor) _allRotors[j];
                }
            }
        }
        if (_rotors.length != rotors.length) {
            throw new EnigmaException("Misnamed rotors");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numRotors - 1) {
            throw new EnigmaException("mismatched string length.");
        }
        for (int i = 1; i < _rotors.length; i++) {
            if (!_alphabet.contains(setting.charAt(i - 1))) {
                throw new EnigmaException(" string not in alphabet");
            }
            _rotors[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] a = new boolean[numRotors()];
        for (int i = 0; i < numRotors(); i++) {
            if (i == numRotors() - 1) {
                a[i] = true;
            } else if (!_rotors[i].rotates()) {
                a[i] = false;
            } else if (_rotors[i + 1].atNotch()) {
                a[i] = true;
                a[i + 1] = true;
            }
        }
        for (int i = 0; i < numRotors(); i++) {
            if (a[i]) {
                _rotors[i].advance();
            }
        }
        int result = _plugboard.permute(c);
        for (int i = _rotors.length - 1; i >= 0; i--) {
            result = _rotors[i].convertForward(result);
        }
        for (int i = 1; i < _rotors.length; i++) {
            result = _rotors[i].convertBackward(result);
        }
        result = _plugboard.permute(result);
        return result;
    }
    /** return rotor array. */
    public Rotor[] getRotors() {
        return _rotors;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            char c = _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
            result += c;
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors. */
    private int _numRotors;

    /** Number of pawls. */
    private int _pawls;

    /** All available rotors. */
    private Object[] _allRotors;

    /** List of currently used rotors. */
    private Rotor[] _rotors;

    /** Plugboard permutation. */
    private Permutation _plugboard;
}
