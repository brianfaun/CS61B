package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Brian Faun
 */
class Permutation {

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Cycle of this permutation. */
    private String _cycles;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        boolean contains = false;
        for (int i = 0; i < _alphabet.size(); i++) {
            for (int j = 0; j < _cycles.length(); j++) {
                if (_alphabet.toChar(i) == _cycles.charAt(j)) {
                    contains = true;
                }
            }
            if (!contains) {
                addCycle(String.valueOf(_alphabet.toChar(i)));
            }
            contains = false;
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String adder = "(" + cycle + ")";
        _cycles += adder;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char a = _alphabet.toChar(wrap(p));
        char perm = '-';
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i) == a && _cycles.charAt(i + 1) != ')') {
                perm = _cycles.charAt(i + 1);
            } else if (_cycles.charAt(i) == a && _cycles.charAt(i + 1) == ')') {
                for (int j = 0; j < _cycles.length(); j++) {
                    if (_cycles.charAt(i - j) == '(') {
                        perm = _cycles.charAt(i - j + 1);
                        break;
                    }
                }
            }
        }
        return _alphabet.toInt(perm);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char a = _alphabet.toChar(wrap(c));
        char perm = '-';
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i) == a && _cycles.charAt(i - 1) != '(') {
                perm = _cycles.charAt(i - 1);
            } else if (_cycles.charAt(i) == a && _cycles.charAt(i - 1) == '(') {
                for (int j = 0; j < _cycles.length(); j++) {
                    if (_cycles.charAt(i + j) == ')') {
                        perm = _cycles.charAt(i + j - 1);
                        break;
                    }
                }
            }
        }
        return _alphabet.toInt(perm);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw new EnigmaException("Not in alphabet!");
        }
        int a = _alphabet.toInt(p);
        int b = permute(a);
        return _alphabet.toChar(b);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw new EnigmaException("Not in alphabet!");
        }
        int a = _alphabet.toInt(c);
        int b = invert(a);
        return _alphabet.toChar(b);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i) == '(' && _cycles.charAt(i + 2) == ')') {
                return false;
            }
        }
        return true;
    }
}
