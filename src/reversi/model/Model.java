package reversi.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Set;

/**
 * The main interface of the reversi reversi.model. It provides all necessary methods for accessing and
 * manipulating the data such that a game can be played successfully.
 *
 * <p>When something changes in the reversi.model, the reversi.model notifies its observers by firing a {@link
 * PropertyChangeEvent change-event}.
 */
public interface Model {


    String STATE_CHANGED = "State changed";
    String NEW_MOVE = "New move";

    /**
     * Add a {@link PropertyChangeListener} to the reversi.model that will be notified about the changes made
     * to the reversi board.
     *
     * @param pcl the view that implements the listener.
     */
    void addPropertyChangeListener(PropertyChangeListener pcl);

    /**
     * Remove a listener from the reversi.model, which will then no longer be notified about any events in the
     * reversi.model.
     *
     * @param pcl the view that then no longer receives notifications from the reversi.model.
     */
    void removePropertyChangeListener(PropertyChangeListener pcl);

    /**
     * Sets a new game up. This includes clearing the board, and allowing {@link Player#BLACK} to make the first move.
     *
     * @throws IOException if any IOException occurs while starting the new game
     */
    void newGame() throws IOException;

    /**
     * Move disk to a cell and deal with the consequences. Moving a disk only works if
     * their is currently a game running, if the given cell doesn't contains a disk, and
     * if moving to the cell is a valid reversi move. After the move, it will be the turn of the
     * next player, unless he has to miss a turn because he can't move any disk or the game is over.
     *
     * @param to cell to move disk to
     * @return <code>true</code> if the move was successful, <code>false</code> otherwise
     */
    boolean move(Cell to);

    /**
     * Undo the last move if possible. In case of success, this will also inform the observers of the
     * reversi.model, so that they may take proper actions afterwards.
     */
    void undoMove();

    /**
     * Return the {@link GameState} specific to this class.
     *
     * @return The <code>GameState</code>-object.
     */
    GameState getState();

    /**
     * Computes all possible moves for a player.
     *
     * @param player The {@link Player player} to compute all possible moves for.
     * @return A set of cells with all possible moves for the player.
     */
    Set<Cell> getPossibleMovesForPlayer(Player player);
}
