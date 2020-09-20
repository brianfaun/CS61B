package signpost;

import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.concurrent.ArrayBlockingQueue;

import java.awt.Dimension;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/** The GUI controller for a Signpost board and buttons.
 *  @author P. N. Hilfinger
 */
class GUI extends TopLevel implements View {

    /** Minimum size of board in pixels. */
    private static final int MIN_SIZE = 500;

    /** Size of pane used to contain help text. */
    static final Dimension TEXT_BOX_SIZE = new Dimension(500, 700);

    /** Resource name of "About" message. */
    static final String ABOUT_TEXT = "signpost/About.html";

    /** Resource name of Signpost help text. */
    static final String HELP_TEXT = "signpost/Help.html";

    /** A new window with given TITLE providing a view of MODEL. */
    GUI(String title) {
        super(title, true);
        addMenuButton("Game->New", this::newGame);
        addMenuButton("Game->Restart", this::restartGame);
        addMenuButton("Game->Solve", this::showSolution);
        addSeparator("Game");
        addMenuButton("Game->Undo", this::undo);
        addMenuButton("Game->Redo", this::redo);
        addSeparator("Game");
        addMenuButton("Type->Set Size", (s) -> newSize(false));
        addMenuButton("Type->Set Size (free ends)", (s) -> newSize(true));
        addMenuButton("Type->Seed", this::newSeed);
        addMenuButton("Game->Quit", this::quit);
        addMenuButton("Help->About", (s) -> displayText("About", ABOUT_TEXT));
        addMenuButton("Help->Signpost", (s) -> displayText("Signpost Help",
                                                           HELP_TEXT));
    }

    /** Response to "Quit" button click. */
    private void quit(String dummy) {
        _pendingCommands.offer("QUIT");
    }

    /** Response to "New Game" button click. */
    private void newGame(String dummy) {
        _pendingCommands.offer("NEW");
    }

    /** Response to "Undo" button click. */
    private void undo(String dummy) {
        _pendingCommands.offer("UNDO");
    }

    /** Response to "Redo" button click. */
    private void redo(String dummy) {
        _pendingCommands.offer("REDO");
    }

    /** Response to "New Game" button click. */
    private void restartGame(String dummy) {
        _pendingCommands.offer("RESTART");
    }

    /** Response to "Solve" button click. */
    private void showSolution(String dummy) {
        _pendingCommands.offer("SOLVE");
    }

    /** Display text in resource named TEXTRESOURCE in a new window titled
     *  TITLE. */
    private void displayText(String title, String textResource) {
        /* Implementation note: It would have been more convenient to avoid
         * having to read the resource and simply use dispPane.setPage on the
         * resource's URL.  However, we wanted to use this application with
         * a nonstandard ClassLoader, and arranging for straight Java to
         * understand non-standard URLS that access such a ClassLoader turns
         * out to be a bit more trouble than it's worth. */
        JFrame frame = new JFrame(title);
        JEditorPane dispPane = new JEditorPane();
        dispPane.setEditable(false);
        dispPane.setContentType("text/html");
        InputStream resource =
            GUI.class.getClassLoader().getResourceAsStream(textResource);
        StringWriter text = new StringWriter();
        try {
            while (true) {
                int c = resource.read();
                if (c < 0) {
                    dispPane.setText(text.toString());
                    break;
                }
                text.write(c);
            }
        } catch (IOException e) {
            return;
        }
        JScrollPane scroller = new JScrollPane(dispPane);
        scroller.setVerticalScrollBarPolicy(scroller.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setPreferredSize(TEXT_BOX_SIZE);
        frame.add(scroller);
        frame.pack();
        frame.setVisible(true);
    }

    /** Pattern describing the 'size' command's arguments. */
    private static final Pattern SIZE_PATN =
        Pattern.compile("\\s*(\\d{1,2})\\s*[xX]\\s*(\\d{1,2})\\s*$");

    /** Pattern describing the 'seed' command's arguments. */
    private static final Pattern SEED_PATN =
        Pattern.compile("\\s*(-?\\d{1,18})\\s*$");

    /** Response to "Set size" button clicks. FREE indicates that free
     *  ends are allowed. */
    private void newSize(boolean free) {
        String response =
            getTextInput("Enter new size (<width>x<height>).",
                         "New size",  "plain",
                         String.format("%dx%d", _width, _height));
        if (response != null) {
            Matcher mat = SIZE_PATN.matcher(response);
            if (mat.matches()) {
                int width = Integer.parseInt(mat.group(1)),
                    height = Integer.parseInt(mat.group(2));
                if (width >= 1 && height >= 1) {
                    _pendingCommands.offer(String.format("TYPE %d %d%s",
                                                         width, height,
                                                         free ? " free" : ""));
                }
            } else {
                showMessage("Bad board size chosen.", "Error", "error");
            }
        }
    }

    /** Response to "Seed" button click. */
    private void newSeed(String dummy) {
        String response =
            getTextInput("Enter new random seed.", "New seed",  "plain", "");
        if (response != null) {
            Matcher mat = SEED_PATN.matcher(response);
            if (mat.matches()) {
                _pendingCommands.offer(String.format("SEED %s", mat.group(1)));
            } else {
                showMessage("Enter an integral seed value.", "Error", "error");
            }
        }
    }

    /** Return the next command from our widget, waiting for it as necessary.
     *  Press/release pairs are reported as "CONN" or "BRK" commands.
     *  Menu-button clicks result in the messages "QUIT", "NEW", "UNDO",
     *  "REDO", "RESTART", "SEED", "SOLVE", or "TYPE". */
    String readCommand() {
        try {
            return _pendingCommands.take();
        } catch (InterruptedException excp) {
            throw new Error("unexpected interrupt");
        }
    }

    @Override
    public void update(Model model) {
        if (_widget == null) {
            _widget = new BoardWidget(_pendingCommands);
            _widget.setSize(model.width(), model.height());
            _width = model.width();
            _height = model.height();
            int pad = _widget.PADDING;
            add(_widget,
                new LayoutSpec("y", 0,
                               "ileft", pad, "iright", pad,
                               "itop", pad, "ibottom", pad,
                               "height", "REMAINDER",
                               "width", "REMAINDER"));
        } else if (model.height() != _height || model.width() != _width) {
            _width = model.width();
            _height = model.height();
            _widget.setSize(_width, _height);
        }
        display(true);
        _widget.update(model);
    }

    /** The board widget. */
    private BoardWidget _widget;
    /** The current size of the model. */
    private int _width, _height;

    /** Queue of pending key presses. */
    private ArrayBlockingQueue<String> _pendingCommands =
        new ArrayBlockingQueue<>(5);

}
