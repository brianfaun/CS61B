import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

/** An (X, Y) position on a Signpost puzzle board.  We require that
 *  X, Y >= 0.  Each Place object is unique; no other has the same x and y
 *  values.  As a result, "==" may be used for comparisons.
 *  @author P. N. Hilfinger
 */
class Place {

    /** Convenience list-of-Place class.  (Defining this allows one to create
     *  arrays of lists without compiler warnings.) */
    static class PlaceList extends ArrayList<Place> {
        /** Initialize empty PlaceList. */
        PlaceList() {
        }

        /** Initialze PlaceList from a copy of INIT. */
        PlaceList(List<Place> init) {
            super(init);
        }
    }

    /** The position (X0, Y0), where X0, Y0 >= 0. */
    private Place(int x0, int y0) {
        x = x0; y = y0;
    }

    /** Return the position (X, Y).  This is a factory method that
     *  creates a new Place only if needed by caching those that are
     *  created. */
    static Place pl(int x, int y) {
        assert x >= 0 && y >= 0;
        int s = max(x, y);
        if (s >= _places.length) {
            Place[][] newPlaces = new Place[s + 1][s + 1];
            for (int i = 0; i < _places.length; i += 1) {
                System.arraycopy(_places[i], 0, newPlaces[i], 0,
                                 _places.length);
            }
            _places = newPlaces;
        }
        if (_places[x][y] == null) {
            _places[x][y] = new Place(x, y);
        }
        return _places[x][y];
    }

    /** Returns the direction from (X0, Y0) to (X1, Y1), if we are a queen
     *  move apart.  If not, returns 0. The direction returned (if not 0)
     *  will be an integer 1 <= dir <= 8 corresponding to the definitions
     *  in Model.java */
    static int dirOf(int x0, int y0, int x1, int y1) {
        int dx = x1 < x0 ? -1 : x0 == x1 ? 0 : 1;
        int dy = y1 < y0 ? -1 : y0 == y1 ? 0 : 1;
        if (dx == 0 && dy == 0) {
            return 0;
        }
        if (dx != 0 && dy != 0 && Math.abs(x0 - x1) != Math.abs(y0 - y1)) {
            return 0;
        }

        return dx > 0 ? 2 - dy : dx == 0 ? 6 + 2 * dy : 6 + dy;
    }

    /** Returns the direction from me to PLACE, if we are a queen
     *  move apart.  If not, returns 0. */
    int dirOf(Place place) {
        return dirOf(x, y, place.x, place.y);
    }

    /** If (x1, y1) is the adjacent square in  direction DIR from me, returns
     *  x1 - x. */
    static int dx(int dir) {
        return DX[dir];
    }

    /** If (x1, y1) is the adjacent square in  direction DIR from me, returns
     *  y1 - y. */
    static int dy(int dir) {
        return DY[dir];
    }

    /** Return an array, M, such that M[x][y][dir] is a list of Places that are
     *  one queen move away from square (x, y) in direction dir on a
     *  WIDTH x HEIGHT board.  Additionally, M[x][y][0] is a list of all Places
     *  that are a queen move away from (x, y) in any direction (the union of
     *  the lists of queen moves in directions 1-8).
     *
     *  FIXME: There are two errors intentionally introduced into this code.
     *  Find and fix it. First, just take some time stepping through the code
     *  to understand  what it is doing. Try drawing out what the 3D array
     *  looks like. */
    static PlaceList[][][] successorCells(int width, int height) {
        PlaceList[][][] M = new PlaceList[width][height][9];
        for (int x0 = 0; x0 < width; x0 += 1) {
            for (int y0 = 0; y0 < height; y0 += 1) {
                PlaceList[] places0 = M[x0][y0];
                for (int dir = 0; dir <= 8; dir += 1) {
                    places0[dir] = new PlaceList();
                }
                for (int x1 = 0; x1 < width; x1 += 1) {
                    for (int y1 = 0; y1 < height; y1 += 1) {
                        int dir = dirOf(x0, y0, x1, y1);
                        Place p = pl(x1, y1);
                        if (dir != 0) {
                            places0[dir].add(p);
                            places0[0].add(p);
                        }
                    }
                }
            }
        }
        return M;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Place)) {
            return false;
        }
        Place other = (Place) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return (x << 16) + y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    /** X displacement of adjacent squares, indexed by direction. */
    static final int[] DX = { 0, 1, 1, 1, 0, -1, -1, -1, 0 };

    /** Y displacement of adjacent squares, indexed by direction. */
    static final int[] DY = { 0, 1, 0, -1, -1, -1, 0, 1, 1 };

    /** Coordinates of this Place. */
    protected final int x, y;

    /** Places already generated. */
    private static Place[][] _places = new Place[10][10];


}
