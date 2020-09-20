import java.util.*;

/**
 * An implementation of a Non-deterministic Finite Automaton (NFA).
 *
 *
 * For this implementation, our legal symbols are every Java character with the
 * exception with {*, +, (, ), |} which are reserved for operations/grouping.
 * We could extend our code to allow for these symbols by escaping them, which
 * is when you add a special character (usually '\') to devoid any special
 * meaning the proceeding character might have. Thus '/*' would be interpreted
 * as the * character while '*' would be interpreted as the Kleene Star
 * operator. Again, our implementation will not do this.
 *
 * We've chosen to not include every escape character for simplicity's sake, but
 * we do have 2 in our implementation:
 *
 *   \d - matches any digit (0-9)
 *   \l - matches any lowercase letter (a-z)
 *
 *
 * Note that '\l' is not a real escape pattern in the standard regular
 * expression implementations, it's just something we've chosen to add. '\d' is,
 * however, standard and that should work as you've learned from lecture.
 *
 * @author Brian Faun
 *
 *
 * */
public class NFA {

    /** A char representing the empty character epsilon. */
    private static final char EPSILON = Character.MIN_VALUE;

    /**
     * Used for keeping track of which operation to use when creating an NFA
     * from a pattern. */
    private static final String OR = "OR";

    /**
     *  Used for keeping track of which operation to use when creating an NFA
     *  from a pattern. */
    private static final String CONCAT = "CONCAT";

    /** To shorted useful patterns (digits and lowercase letters). */
    private static final Map<Character, String> ESCAPED_TO_PATTERN =
            new HashMap<Character, String>();

    static {
        String lowerLetterPattern = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s"
                + "|t|u|v|w|x|y|z)";
        String singleDigitPattern = "(0|1|2|3|4|5|6|7|8|9)";

        ESCAPED_TO_PATTERN.put('l', lowerLetterPattern);
        ESCAPED_TO_PATTERN.put('d', singleDigitPattern);
    }

    /** The starting State of this NFA. */
    private State _startState;

    /** The accepting State of this NFA. */
    private State _acceptState;

    /** For a String representation. */
    private String _pattern;


    /** The internal States in an NFA. */
    // TODO: Read this inner class, then you may delete this comment
    private class State {

        /**
         * Creates a new State that this State points to with edge label C.
         * Returns the newly created State. */
        public State addNeighbor(Character c) {
            State neighbor = new State();

            addEdge(c, neighbor);
            return neighbor;
        }

        /** Adds an edge from this State to NEIGHBOR with edge label C. */
        public void addEdge(Character c, State neighbor) {
            if (!_edges.containsKey(c)) {
                _edges.put(c, new HashSet<State>());
            }
            _edges.get(c).add(neighbor);
        }

        /**
         * Returns a Set of all the States that can be reached from this
         * State by taking an edge with label C.
         *
         * If C is EPSILON, the  it returns all the valid States that
         * can be reached using  only EPSILON edges (this may span
         * multiple consecutive EPSILON edges).
         *c
         * If this State has no outgoing edges with label C, then
         * return an empty Set. */
        public Set<State> successors(char c) {
            // TODO: Implement this method
            Set<State> succ = new HashSet<>();
            if (!_edges.containsKey(c)) {
                return new HashSet<>();
            } else if (c == EPSILON) {
                succ.addAll(_edges.get(c));
                for (State s : _edges.get(c)) {
                    succ.addAll(s.successors(EPSILON));
                }
                return succ;
            } else {
                return _edges.get(c);
            }
        }

        /**
         * @return: Whether or not this State is an accepting State.
         * */
        public boolean isAccepting() {
            return _accepting;
        }

        /** Modifies this State to be accepting. */
        public void makeAccepting() {
            _accepting = true;
        }

        /** Modifies this State to not be accepting. */
        public void makeNotAccepting() {
            _accepting = false;
        }

        /** A State may have many outgoing edges with the same edge label. */
        private Map<Character, Set<State>> _edges = new HashMap<Character, Set<State>>();

        /** Whether or not this State is an accepting state. */
        private boolean _accepting;
    }

    /**
     * @param s the String to check parentheses in
     * @param j the index of some begin parentheses
     * @return the index of the corresponding closing parentheses or -1
     *         if none is found. */
    private static int getClosingIndex(String s, int j) {

        assert s.charAt(j) == '(' : "The jth char of s must be (";

        int numOpenSeen = 1;
        int n = s.length();
        for (int i = j + 1; i < n; i += 1) {

            if (numOpenSeen == 0) {
                return i - 1;
            }
            char c = s.charAt(i);

            switch (c) {
            case '(':
                numOpenSeen += 1;
                break;
            case ')':
                numOpenSeen -= 1;
                break;
            default:
                continue;
            }
        }
        return numOpenSeen == 0 ? n - 1 : -1;
    }

    /**
     * Destructively applies the Kleene Plus operation to this NFA according to
     *  construction defined above. */
    private void applyPlus() {
        State newStart = new State();
        State newAccept = new State();

        newStart.addEdge(EPSILON, _startState);

        _acceptState.addEdge(EPSILON, _startState);
        _acceptState.addEdge(EPSILON, newAccept);

        _acceptState.makeNotAccepting();
        newAccept.makeAccepting();

        _startState = newStart;
        _acceptState = newAccept;
    }

    /**
     * Destructively applies the Kleene Star operation to this NFA according
     *  to construction defined above. */
    private void applyStar() {
        State newStart = new State();
        State newAccept = new State();

        newStart.addEdge(EPSILON, _startState);
        newStart.addEdge(EPSILON, newAccept);

        _acceptState.addEdge(EPSILON, _startState);
        _acceptState.addEdge(EPSILON, newAccept);

        _acceptState.makeNotAccepting();
        newAccept.makeAccepting();

        _startState = newStart;
        _acceptState = newAccept;
    }

    /** Returns the NFA constructed by taking the Or of the NFAs S and T. */
    private static NFA or(NFA s, NFA t) {

        /** As is the case at the beginning of the pattern. */
        if (s == null) {
            return t;
        }

        State sStart = s._startState;
        State sAccept = s._acceptState;

        State tStart = t._startState;
        State tAccept = t._acceptState;

        NFA joined = new NFA();

        State start = joined._startState;
        State accept = joined._acceptState;

        start.addEdge(EPSILON, sStart);
        start.addEdge(EPSILON, tStart);

        sAccept.addEdge(EPSILON, accept);
        tAccept.addEdge(EPSILON, accept);

        sAccept.makeNotAccepting();
        tAccept.makeNotAccepting();

        return joined;
    }

    /**
     * Returns the NFA constructed by taking the Concatenation of the NFAs S
     * and T. */
    private static NFA concat(NFA s, NFA t) {

        /** As is the case at the beginning of the pattern. */
        if (s == null) {
            return t;
        }

        NFA joined = new NFA();

        joined._startState = s._startState;
        s._acceptState.addEdge(EPSILON, t._startState);
        joined._acceptState = t._acceptState;

        s._acceptState.makeNotAccepting();

        return joined;
    }

    /**
     * @param type either OR or CONCAT, denoting the type of operation to
     *             apply
     * @param s the NFA on the left of the join
     * @param t the NFA on the right of the join
     *
     * @return the join of type TYPE of S and T (destructively). */
    private static NFA apply(String type, NFA s, NFA t) {
        switch (type) {
        case OR:
            return or(s, t);
        case CONCAT:
            return concat(s, t);
        default:
            return null;
        }
    }

    /** A dummy constructor that may be useful when joining two NFAs. */
    public NFA() {
        _startState = new State();
        _acceptState = new State();
        _acceptState.makeAccepting();
    }

    /** A simple NFA that matches only a single character C. */
    public NFA(char c) {
        _startState = new State();
        _acceptState = _startState.addNeighbor(c);
        _acceptState.makeAccepting();
        _pattern = String.valueOf(c);
    }


    /** Sets the _pattern to PATTERN. */
    private void setPattern(String pattern) {
        _pattern = pattern;
    }

    /**
     * @param pattern the pattern to create an NFA from
     *
     * @return the NFA created from the pattern using Thompson's
     *         construction (see above for an explanation). */
    public static NFA fromPattern(String pattern) {
        int n = pattern.length();
        assert n != 0 : "pattern may not be empty";

        if (n == 1) {
            return new NFA(pattern.charAt(0));
        }
        int i = 0;
        char c;
        NFA nfa = null;
        NFA subNFA = null;
        String operationType = CONCAT;

        while (i < n) {
            c = pattern.charAt(i);

            switch (c) {
            case '(':
                int endIndex = getClosingIndex(pattern, i);
                assert endIndex != -1 : "missing closing parentheses";
                String subPattern = pattern.substring(i + 1, endIndex);
                subNFA = fromPattern(subPattern);
                i = endIndex + 1;
                break;
            case '|':
                operationType = OR;
                i += 1;
                continue;
            case '\\':
                assert i + 1 < n : "missing escape character ('l' or 'd')";
                i += 1;
                c = pattern.charAt(i);
                assert c == 'l' || c == 'd' : "illegal escape sequence \\" + c;

                subNFA = NFA.fromPattern(ESCAPED_TO_PATTERN.get(c));
                i += 1;
                break;
            default:
                assert c != ')' : "missing opening parentheses";
                assert c != '+' && c != '*' : "malformed pattern";
                subNFA = new NFA(c);
                i += 1;
            }

            if (i < n && pattern.charAt(i) == '*') {
                subNFA.applyStar();
                i += 1;
            } else if (i < n && pattern.charAt(i) == '+') {
                subNFA.applyPlus();
                i += 1;
            }
            nfa = apply(operationType, nfa, subNFA);
            operationType = CONCAT;
        }
        nfa.setPattern(pattern);
        return nfa;
    }

    /**
     * @param s the query String
     * @return whether or not the string S is accepted by this NFA. */
    public boolean matches(String s) {
        // TODO: write the matching algorithm
        Set<State> S = _startState.successors(EPSILON);
        S.add(_startState);
        Set<State> SS = new HashSet<>();
        int index = 0;
        while (index < s.length()) {
            char c = s.charAt(index);
            for (State state : S) {
                SS.addAll(state.successors(c));
            }
            S.clear();
            for (State state : SS) {
                S.add(state);
                S.addAll(state.successors(EPSILON));
            }
            SS.clear();
            index++;
        }
        if (S.contains(_acceptState)) {
            return true;
        }
        return false;
    }

    /** Returns the pattern used to make this NFA. */
    @Override
    public String toString() {
        return _pattern;
    }
}
