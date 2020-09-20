package enigma;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Brian Faun
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named JNAME. */
    private Scanner getInput(String jname) {
        try {
            return new Scanner(new File(jname));
        } catch (IOException excp) {
            throw error("could not open %s", jname);
        }
    }

    /** Return a PrintStream writing to the file named KNAME. */
    private PrintStream getOutput(String kname) {
        try {
            return new PrintStream(new File(kname));
        } catch (IOException excp) {
            throw error("could not open %s", kname);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine mach = readConfig();
        String setting = _input.nextLine();
        String message = "";
        setUp(mach, setting);
        while (_input.hasNextLine()) {
            String next = _input.nextLine();
            if (next.contains("*")) {
                setUp(mach, next);
            } else {
                message = mach.convert(next.replaceAll(" ", ""));
                printMessageLine(message);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String a = _config.next();
            if (a.contains("(") || a.contains(")") || a.contains("*")) {
                throw new EnigmaException("Wrong config format");
            }
            _alphabet = new Alphabet(a);
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            _allRotors = new ArrayList<>();
            while (_config.hasNext()) {
                _allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String rname = _config.next();
            String type = _config.next();
            String cycles = "";
            while (_config.hasNext("\\(.*\\)")) {
                cycles += _config.next();
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (type.charAt(0) == 'M') {
                String notches = "";
                for (int i = 1; i < type.length(); i++) {
                    notches += type.charAt(i);
                }
                return new MovingRotor(rname, perm, notches);
            } else if (type.charAt(0) == 'N') {
                return new FixedRotor(rname, perm);
            } else if (type.charAt(0) == 'R') {
                return new Reflector(rname, perm);
            } else {
                throw new EnigmaException("no rotor");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] setArr = settings.split(" ");
        boolean reflects = false;
        if (!setArr[0].equals("*")) {
            throw new EnigmaException("illegal symbol start");
        }
        for (int i = 0; i < _allRotors.size(); i++) {
            if (_allRotors.get(i).reflecting()) {
                if (_allRotors.get(i).name().equals(setArr[1])) {
                    reflects = true;
                    break;
                }
            }
        }
        if (M.numRotors() + 2 > setArr.length) {
            throw new EnigmaException("wrong number of rotors");
        }
        if (!reflects) {
            throw new EnigmaException("wrong, reflector");
        }
        String set = setArr[M.numRotors() + 1];
        if (set.length() != M.numRotors() - 1) {
            throw new EnigmaException("wrong setting format 1");
        }
        for (int i = 0; i < set.length(); i++) {
            if (!_alphabet.contains(set.charAt(i))) {
                throw new EnigmaException("wrong setting format 2");
            }
        }
        String[] rotors = new String[M.numRotors()];
        String cycles = "";
        if (setArr[0].equals("*")) {
            for (int i = 0; i < rotors.length; i++) {
                rotors[i] = setArr[i + 1];
            }
            for (int i = M.numRotors() + 2; i < setArr.length; i++) {
                cycles += setArr[i];
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            M.setPlugboard(perm);
            M.insertRotors(rotors);
            M.setRotors(set);
        } else {
            throw new EnigmaException("wrong setting format 3");
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        msg = msg.replace(" ", "");
        String result = "";
        while (msg.length() >= 6) {
            result += msg.substring(0, 5) + " ";
            msg = msg.substring(5);
        }
        result += msg;
        _output.println(result);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** An ArrayList containing all rotors that can be used. */
    private ArrayList<Rotor> _allRotors = new ArrayList<>();
}
