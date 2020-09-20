/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

/** An updateable view of a LOA board.
 *  @author P. N. Hilfinger */
interface View {

    /** Update the current view according to the game on CONTROLLER. */
    void update(Game controller);

}
