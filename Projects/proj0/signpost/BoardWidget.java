package signpost;

import ucb.gui2.Pad;

import java.util.concurrent.ArrayBlockingQueue;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import static java.awt.RenderingHints.*;

import static signpost.Place.pl;
import signpost.Model.Sq;

/** A widget that displays a Signpost puzzle.
 *  @author Brian Faun
 */
class BoardWidget extends Pad {

    /* Parameters controlling sizes, speeds, colors, and fonts. */

    /** Pause in milliseconds before each turn of arrows to signal solved
     *  puzzle. */
    static final long ARROW_BUMP_INTERVAL = 120;

    /** Colors of squares, arrows, and grid lines. */
    static final Color
        BACKGROUND_COLOR = new Color(220, 220, 220),
        GRID_LINE_COLOR = Color.black,
        ARROW_COLOR = Color.black,
        NUMBERED_SQUARE_COLOR = Color.white,
        CONNECTED_COLOR = new Color(180, 180, 180),
        NUM_COLOR = Color.black,
        FIXED_NUM_COLOR = Color.blue;

    /** Basic cell background RGB color values (from Simon Tatham). */
    static final int[] BG_BASE_RGB = {
        0xffffff,
        0xffa07a,
        0x98fb98,
        0x7fffd4,
        0x9370db,
        0xffa500,
        0x87cefa,
        0xffff00,
    };

    /** Background colors used by runs of unnumbered squares. */
    static final Color[] BG_COLORS = new Color[BG_BASE_RGB.length * 3];

    static {
        int p = BG_BASE_RGB.length;
        for (int i = 0; i < BG_COLORS.length; i += 1) {
            if (i < p) {
                BG_COLORS[i] = new Color(BG_BASE_RGB[i]);
            } else {
                BG_COLORS[i] =
                    new Color((BG_COLORS[i - p].getRGB()
                               + BG_COLORS[i - p + 1].getRGB()) / 2);
            }
        }
    }

    /** Bar width separating squares and other dimensions (pixels). */
    static final int
        CELL_SIDE = 50,
        GRID_LINE_WIDTH = 1,
        PADDING = CELL_SIDE / 2,
        TEXT_OFFSET = 4,
        OFFSET = 2,
        DOT_SIZE = 7;

    /** Separation between cell centers and half separation between cell
     *  centers. */
    static final int
        CELL_SEP = CELL_SIDE + GRID_LINE_WIDTH,
        HALF_SEP = CELL_SEP / 2;

    /** Strokes for ordinary grid lines and those that are parts of
     *  boundaries. */
    static final BasicStroke
        GRIDLINE_STROKE = new BasicStroke(GRID_LINE_WIDTH);

    /** Font for square numbers. */
    static final Font NUM_FONT = new Font("Dejavu Serif", Font.BOLD, 18);

    /** Font for square group-sequence numbers (e.g., "a+1"). */
    static final Font GROUP_TEXT_FONT = new Font("Dejavu Serif", Font.BOLD, 12);

    /** Font for dots. */
    static final Font DOT_FONT = new Font("SansSerif", Font.BOLD, 36);

    /** Arrow vertex coordinates: x in first row, y in second. */
    static final int [][] ARROW = {
        {  0,  7,  7, 14, 14, 21, 10,  },
        { 10, 10,  0, 0,  10, 10, 21,  }
    };

    /** Star vertex coordinates. */
    static final int[][] STAR = {
        { 22, 15, 18, 11, 4, 7, 0, 8, 11, 14 },
        { 8, 13, 21, 16, 21, 13, 8, 8, 0, 8 }
    };

    /** Amount of rotation between arrow positions. */
    static final double PI_4 = 0.25 * Math.PI;

    /** A graphical representation of a Signpost board that sends commands
     *  derived from mouse clicks to COMMANDS. */
    BoardWidget(ArrayBlockingQueue<String> commands) {
        _commands = commands;
        setMouseHandler("press", this::mousePressed);
        setMouseHandler("release", this::mouseReleased);
    }

    /** Set the size of the board to WIDTH x HEIGHT. */
    public void setSize(int width, int height) {
        synchronized (me) {
            _width = width; _height = height;
            _boardWidth = width * CELL_SEP + 3 * GRID_LINE_WIDTH;
            _boardHeight = height * CELL_SEP + 3 * GRID_LINE_WIDTH;
            setPreferredSize(_boardWidth, _boardHeight);
        }
        repaint();
    }

    /** Draw the grid lines on G. */
    private void drawGrid(Graphics2D g) {
        g.setColor(GRID_LINE_COLOR);
        g.setStroke(GRIDLINE_STROKE);
        for (int k = 0; k <= _width; k += 1) {
            g.drawLine(cx(k), cy(0), cx(k), cy(_height));
        }
        for (int k = 0; k <= _height; k += 1) {
            g.drawLine(cx(0), cy(k), cx(_width), cy(k));
        }
    }

    /** Return the appropriate color for arrow in SQ. */
    private Color arrowColor(Sq sq) {
        return sq.successor() == null ? ARROW_COLOR : CONNECTED_COLOR;
    }

    /** Return the appropriate color for numeral in SQ. */
    private Color numberColor(Sq sq) {
        return sq.hasFixedNum() ? FIXED_NUM_COLOR
            : sq.successor() == null || sq.predecessor() == null ? NUM_COLOR
            : CONNECTED_COLOR;
    }

    /** Draw star in SQ on G. */
    private void drawStar(Graphics2D g, Sq sq) {
        g.setColor(arrowColor(sq));
        int px = cx(sq.x), py = cy(sq.y);
        int[] x = new int[STAR[0].length], y = new int[STAR[0].length];
        for (int i = 0; i < x.length; i += 1) {
            x[i] = px + STAR[0][i] + CELL_SIDE / 2 + 2;
            y[i] = py + STAR[1][i] - CELL_SIDE / 2 - 2;
        }
        g.fillPolygon(x, y, x.length);
    }

    /** Draw arrow in SQ on G. */
    private void drawArrow(Graphics2D g, Sq sq) {
        if (sq.direction() == 0) {
            drawStar(g, sq);
            return;
        }
        g.setColor(arrowColor(sq));
        int px = cx(sq.x), py = cy(sq.y);
        int[] x = new int[ARROW[0].length], y = new int[ARROW[0].length];
        for (int i = 0; i < x.length; i += 1) {
            x[i] = px + CELL_SIDE / 2 + 2 + ARROW[0][i];
            y[i] = py - CELL_SIDE / 2 + 2 + ARROW[1][i];
        }
        AffineTransform init = g.getTransform();
        int dir = (sq.direction() + _dirBump - 1) % 8 + 1;
        g.rotate((dir - 4) * PI_4,
                 px + 3 * CELL_SIDE / 4, py - CELL_SIDE / 4);
        g.fillPolygon(x, y, x.length);
        g.setTransform(init);
    }

    /** Draw SQ on G. */
    private void drawSquare(Graphics2D g, Sq sq) {
        int px = cx(sq.x), py = cy(sq.y);
        if (sq.group() >= 0) {
            g.setColor(groupColor(sq.group()));
            g.fillRect(px + GRID_LINE_WIDTH, py + GRID_LINE_WIDTH - CELL_SIDE,
                       CELL_SIDE - GRID_LINE_WIDTH,
                       CELL_SIDE - GRID_LINE_WIDTH);
        }
        drawArrow(g, sq);
        if (sq.predecessor() == null && sq.sequenceNum() != 1) {
            g.setColor(ARROW_COLOR);
            g.fillOval(px + 3 * TEXT_OFFSET, py - 4 * TEXT_OFFSET,
                       DOT_SIZE, DOT_SIZE);
        }
        if (sq.sequenceNum() != 0) {
            g.setColor(numberColor(sq));
            g.setFont(NUM_FONT);
        } else if (sq.group() > 0) {
            g.setColor(NUM_COLOR);
            g.setFont(GROUP_TEXT_FONT);
        } else {
            return;
        }
        g.drawString(sq.seqText(),
                     px + TEXT_OFFSET, py - CELL_SIDE / 2 - 2 * TEXT_OFFSET);
    }

    /** Give a visual signal that the puzzle is solved. */
    private void signalSolved() {
        while (true) {
            try {
                Thread.sleep(ARROW_BUMP_INTERVAL);
            } catch (InterruptedException excp) {
                /* Ignore InterruptedException. */
            }
            synchronized (this) {
                _dirBump = (_dirBump + 1) % 8;
                repaint();
                if (_dirBump == 0) {
                    break;
                }
            }
        }
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, _boardWidth, _boardHeight);

        drawGrid(g);
        int n = 2;

        for (Sq sq : _model) {
            drawSquare(g, sq);
        }
    }

    /** Handle mouse pressed event E, recording the starting square of a
     *  connection, if appropriate. */
    private synchronized void mousePressed(String unused, MouseEvent e) {
        int x = x(e), y = y(e);
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (_model.isCell(x, y)) {
            _connStart = pl(x, y);
        } else {
            _connStart = null;
        }
    }

    /** Handle mouse released event E, reporting a connection,
     *  if appropriate. */
    private synchronized void mouseReleased(String unused, MouseEvent e) {
        int x = x(e), y = y(e);
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        Place endPlace;
        if (_connStart != null) {
            if (_model.isCell(x, y)) {
                Sq sq0 = _model.get(_connStart), sq1 = _model.get(x, y);
                _commands.offer(String.format("CONN %d %d %d %d",
                                              sq0.x, sq0.y, x, y));
            } else {
                _commands.offer(String.format("BRK %d %d",
                                              _connStart.x, _connStart.y));
            }
        }
        _connStart = null;
    }

    /** Return the column index of the square on which EVENT occurred. */
    private int x(MouseEvent event) {
        return (int) Math.floorDiv(event.getX() - OFFSET, CELL_SEP);
    }

    /** Return the row index of the square on which EVENT occurred. */
    private int y(MouseEvent event) {
        return _height - 1
            - (int) Math.floorDiv(event.getY() - OFFSET, CELL_SEP);
    }

    /** Return the color associated with group N. */
    private Color groupColor(int n) {
        if (n == 0) {
            return NUMBERED_SQUARE_COLOR;
        } else {
            return BG_COLORS[(n - 1) % (BG_COLORS.length - 1) + 1];
        }
    }

    /** Revise the displayed board according to MODEL. */
    void update(Model model) {
        synchronized (this) {
            _model = new Model(model);
            _dirBump = 0;
        }

        repaint();
        if (_model.solved()) {
            signalSolved();
        }
    }

    /** Return pixel coordinates of vertical board coordinate Y relative
     *  to window. */
    private int cy(int y) {
        return OFFSET + (_height - y) * CELL_SEP;
    }

    /** Return pixel coordinates of horizontal board coordinate X relative
     *  to window. */
    private int cx(int x) {
        return OFFSET + x * CELL_SEP;
    }

    /** Number of height and of columns. */
    private int _height, _width;

    /** Queue on which to post commands (from mouse clicks). */
    private ArrayBlockingQueue<String> _commands;

    /** Current model being displayed. */
    private Model _model;
    /** Length (in pixels) of the side of the board. */
    private int _boardWidth, _boardHeight;
    /** Place where mouse action started. */
    private Place _connStart;
    /** Amount to add to direction value for each displayed arrow (used for
     *  special effect signaling completion. */
    private int _dirBump;
}
