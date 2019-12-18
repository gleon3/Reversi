package reversi.model;

import static java.util.Objects.requireNonNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of a reversi class that handles the logic for a reversi game.
 * It is the entry-class to the internal structure of the game-model and provides all methods
 * necessary for an user-interface to playing a game successfully.
 */
public class Reversi implements Model {

    private static final int DIRECTION_UPWARD = 1;
    private static final int DIRECTION_DOWNWARD = -1;

    private static final int EXPECTED_HISTORY_LENGTH = 60;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final Deque<GameState> stateHistory = new ArrayDeque<>(EXPECTED_HISTORY_LENGTH);

    private GameState state;

    /**
     * Initialize a new Reversi-Game in which everything is set up in its initial position. The game is
     * ready to be played immediately after.
     */
    public Reversi() {
        newGame();
    }

    /**
     * Creates a new reversi object in which the passed {@link GameState}-object is taken as new
     * instance of this class.
     *
     * @param gameState A non-null {@link GameState}-object.
     */
    public Reversi(GameState gameState) {
        state = requireNonNull(gameState);
    }


    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener pcl) {
        requireNonNull(pcl);
        support.addPropertyChangeListener(pcl);
    }

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener pcl) {
        requireNonNull(pcl);
        support.removePropertyChangeListener(pcl);
    }

    /**
     * Invokes the firing of an event, such that any attached observer (i.e., {@link
     * PropertyChangeListener}) is notified that a change happened to this model.
     */
    protected void notifyListeners() {
        notifyListeners(true);
        //TODO
    }

    private void notifyListeners(boolean wasActiveChange) {
        support.firePropertyChange(STATE_CHANGED, null, this);
        if (wasActiveChange) {
            support.firePropertyChange(NEW_MOVE, null, this);
        }
        //TODO
    }

    @Override
    public synchronized GameState getState() {
        return state;
    }

    /**
     * Sets the game state to the given value. Can be used to override the existing game state. After
     * updating the game state, listeners will be notified about the change.
     *
     * @param state the new game state
     */
    synchronized void setState(GameState state) {
        this.state = state;
        notifyListeners(false);
    }

    @Override
    public synchronized void newGame() {
        state = new GameState(new GameField());

        notifyListeners();
    }

    @Override
    public synchronized boolean move(Cell to) {
        if (state.getCurrentPhase() != Phase.RUNNING) {
            return false;
        }

        //TODO
        getState().increaseMoveCounter();
        notifyListeners();

        return true;
    }

    @Override
    public synchronized void undoMove() {
        state = stateHistory.pop();
        notifyListeners();
    }

    /**
     * Ends the game by setting its current phase to {@link Phase#FINISHED}. The winner is also set
     * depending on the given input.
     *
     * <p>Afterwards, an event is fired in which all observers are notified about the changed phase.
     *
     * @param winner An {@link Optional} containing either the winning player, or empty otherwise (if
     *     the game ended in a draw).
     */
    private void setGameFinished(Optional<Player> winner) {
        state.setCurrentPhase(Phase.FINISHED);
        state.setWinner(winner);

        notifyListeners();
    }

    /**
     * Checks a player if he is able to execute a move.
     *
     * @param player The player to be checked.
     * @return <code>true</code> if player is able to move, <code>false</code> otherwise.
     */
    private boolean canExecuteMove(Player player) {
        Set<Cell> cells = state.getAllCellsOfPlayer(player);
        for (Cell cell : cells) {
            Set<Cell> possibleMoves = getPossibleMovesForPlayer(player);
            if (!possibleMoves.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized Set<Cell> getPossibleMovesForPlayer(Player player) {
        //TODO
        return new HashSet<>();
    }
}
