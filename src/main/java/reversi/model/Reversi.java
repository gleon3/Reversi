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
 * Implementation of a reversi class that handles the logic for a reversi game. It is the
 * entry-class to the internal structure of the game-model and provides all methods necessary for an
 * user-interface to playing a game successfully.
 */
public class Reversi implements Model {

  private static final int DIRECTION_UPWARD = 1;
  private static final int DIRECTION_DOWNWARD = -1;

  private static final int EXPECTED_HISTORY_LENGTH = 60;

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private final Deque<GameState> stateHistory = new ArrayDeque<>(EXPECTED_HISTORY_LENGTH);

  private GameState state;

  /**
   * Initialize a new Reversi-Game in which everything is set up in its initial position. The game
   * is ready to be played immediately after.
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
    // TODO
  }

  private void notifyListeners(boolean wasActiveChange) {
    support.firePropertyChange(STATE_CHANGED, null, this);
    if (wasActiveChange) {
      support.firePropertyChange(NEW_MOVE, null, this);
    }
    // TODO
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

    // TODO
    /* if diskCount of currentPlayer > 30
        if to is within middle 4 cells && the cell is empty
           execute move
           getState().increaseMoveCounter();
           notifyListeners();
           return true;
      else
        return false

     else
       if state.getField().get(to).isEmpty (1.)
           if isWithinBound && isPresent && state.getField().get(new Cell(to.getColumn+1, to.getRow)).get().isOppositeColor
           oder isWithinBound && isPresent && state.getField().get(new Cell(to.getColumn-1,to.getRow)).get().isOppositeColor
           oder isWithinBound && isPresent && state.getField().get(new Cell(to.getColumn,to.getRow+1)).isOppositeColor
           oder isWithinBound && isPresent && state.getField().get(new Cell(to.getColumn,to.getRow-1)).isOppositeColor (2.)
               sets definieren;
               if (checkStraightLine(sets))(3.)

               we have multiple sets of cells in different directions, cutSetAt(currentPlayer) for all of
               these sets, then add sets together to new set, this is the set of disks that have to get flipped
               flip these disks

               execute move
               getState().increaseMoveCounter();
               notifyListeners();
               return true;
    */
    return true;
  }

  @Override
  public synchronized void undoMove() {
    state = stateHistory.pop();
    notifyListeners();
  }

  // TODO: javadoc
  private void flipDisks(Set<Cell> set) {
    for (Cell cell : set) {
      state.getField().set(cell, new Disk(Player.getOpponentOf(state.getCurrentPlayer())));
    }
  }

  // TODO: javadoc
  private Set<Cell> cutSetAt(Player player, Set<Cell> set) {
    Set newSet = new HashSet<Cell>();

    for (Cell cell : set) {
      if (state.getField().get(cell).isPresent()
          && state.getField().get(cell).get().getPlayer().equals(player)) {
        break;
      } else {
        newSet.add(cell);
      }
    }
    return newSet;
  }

  // TODO: javadoc
  private boolean checkStraightLineUninterrupted(Set<Cell> set) {
    for (Cell cell : set) {
      if (state.getField().get(cell).isEmpty()) {
        return false;
      } else {
        if (state.getField().get(cell).get().getPlayer().equals(state.getCurrentPlayer())) {
          break;
        }
      }
    }
    return true;
  }

  // TODO: javadoc
  private Set<Cell> disksDiagonal(Cell to) {
    Set disksDiagonal = new HashSet<Set<Set<Cell>>>();

    Optional<Disk> pawnUpperRight =
        state.getField().get(new Cell(to.getColumn() + 1, to.getRow() + 1));
    Optional<Disk> pawnUpperLeft =
        state.getField().get(new Cell(to.getColumn() - 1, to.getRow() + 1));
    Optional<Disk> pawnLowerRight =
        state.getField().get(new Cell(to.getColumn() + 1, to.getRow() - 1));
    Optional<Disk> pawnLowerLeft =
        state.getField().get(new Cell(to.getColumn() - 1, to.getRow() - 1));

    if (pawnUpperRight.isPresent()
        && pawnUpperRight
            .get()
            .getPlayer()
            .equals(Player.getOpponentOf(state.getCurrentPlayer()))) {
      Set disksUpperRight = new HashSet<Cell>();

      Cell upperRight = new Cell(to.getColumn() + 1, to.getRow() + 1);

      int upperRightStart = Math.max(upperRight.getColumn(), upperRight.getRow());

      for (int i = upperRightStart; i < GameField.SIZE; i++) {
        disksUpperRight.add(state.getField().get(new Cell(to.getColumn() + i, to.getRow() + i)));
      }

      disksDiagonal.add(disksUpperRight);
    }

    if (pawnUpperLeft.isPresent()
        && pawnUpperLeft.get().getPlayer().equals(Player.getOpponentOf(state.getCurrentPlayer()))) {
      Set disksUpperLeft = new HashSet<Cell>();

      for (int i = 0; i < GameField.SIZE - 1; i++) {
        disksUpperLeft.add(state.getField().get(new Cell(to.getColumn() - i, to.getRow() + i)));

        if ((to.getColumn() - i) <= 0 || (to.getRow() + i) >= GameField.SIZE - 1) {
          break;
        }
      }

      disksDiagonal.add(disksUpperLeft);
    }

    if (pawnLowerRight.isPresent()
        && pawnLowerRight
            .get()
            .getPlayer()
            .equals(Player.getOpponentOf(state.getCurrentPlayer()))) {
      Set disksLowerRight = new HashSet<Cell>();

      for (int i = 0; i < GameField.SIZE - 1; i++) {
        disksLowerRight.add(state.getField().get(new Cell(to.getColumn() + i, to.getRow() - i)));

        if ((to.getColumn() + i) >= GameField.SIZE || (to.getRow() - i) <= 0) {
          break;
        }
      }

      disksDiagonal.add(disksLowerRight);
    }

    if (pawnLowerLeft.isPresent()
        && pawnLowerLeft.get().getPlayer().equals(Player.getOpponentOf(state.getCurrentPlayer()))) {
      Set disksLowerLeft = new HashSet<Cell>();

      Cell lowerLeft = new Cell(to.getColumn() - 1, to.getRow() - 1);

      int lowerLeftStart = Math.min(lowerLeft.getColumn(), lowerLeft.getRow());

      for (int i = lowerLeftStart; i >= 0; i--) {
        disksLowerLeft.add(state.getField().get(new Cell(to.getColumn() - i, to.getRow() - i)));
      }

      disksDiagonal.add(disksLowerLeft);
    }

    return disksDiagonal;
  }

  // TODO: javadoc
  private Set<Cell> disksVertical(Cell to) {
    Set disksVertical = new HashSet<Set<Set<Cell>>>();

    Optional<Disk> pawnForward = state.getField().get(new Cell(to.getColumn(), to.getRow() + 1));
    Optional<Disk> pawnBehind = state.getField().get(new Cell(to.getColumn(), to.getRow() - 1));

    if (pawnForward.isPresent()
        && pawnForward.get().getPlayer().equals(Player.getOpponentOf(state.getCurrentPlayer()))) {
      Set disksForward = new HashSet<Cell>();

      for (int i = to.getRow() + 1; i < GameField.SIZE - 1; i++) {
        disksForward.add(state.getField().get(new Cell(i, to.getRow())));
      }

      disksVertical.add(disksForward);
    }

    if (pawnBehind.isPresent()
        && pawnBehind.get().getPlayer().equals(Player.getOpponentOf(state.getCurrentPlayer()))) {
      Set disksVerticalBehind = new HashSet<Cell>();

      for (int i = to.getRow() - 1; i >= 0; i--) {
        disksVerticalBehind.add(state.getField().get(new Cell(i, to.getRow())));
      }

      disksVertical.add(disksVerticalBehind);
    }

    return disksVertical;
  }

  // TODO: javadoc
  private Set<Set<Cell>> disksHorizontal(Cell to) {
    Set disksHorizontal = new HashSet<Set<Set<Cell>>>();

    Optional<Disk> pawnRight = state.getField().get(new Cell(to.getColumn() + 1, to.getRow()));
    Optional<Disk> pawnLeft = state.getField().get(new Cell(to.getColumn() - 1, to.getRow()));

    if (pawnRight.isPresent()
        && pawnRight.get().getPlayer().equals(Player.getOpponentOf(state.getCurrentPlayer()))) {
      Set disksRight = new HashSet<Cell>();

      for (int i = to.getColumn() + 1; i < GameField.SIZE - 1; i++) {
        disksRight.add(state.getField().get(new Cell(i, to.getRow())));
      }

      disksHorizontal.add(disksRight);
    }

    if (pawnLeft.isPresent()
        && pawnLeft.get().getPlayer().equals(Player.getOpponentOf(state.getCurrentPlayer()))) {
      Set disksLeft = new HashSet<Cell>();

      for (int i = to.getColumn() - 1; i >= 0; i--) {
        disksLeft.add(state.getField().get(new Cell(i, to.getRow())));
      }

      disksHorizontal.add(disksLeft);
    }

    return disksHorizontal;
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
    // TODO
    return new HashSet<>();
  }
}
