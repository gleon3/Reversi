package reversi.model;

import static java.util.Objects.requireNonNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of a reversi class that handles the logic for a reversi game. It is the
 * entry-class to the internal structure of the game-model and provides all methods necessary for an
 * user-interface to playing a game successfully.
 */
public class Reversi implements Model {

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

    Player currentPlayer = state.getCurrentPlayer();

    if (!GameField.isWithinBounds(to)) {
      return false;
    }

    // get all possible moves for the current player
    Set<Cell> possibleMoves = getPossibleMovesForPlayer(currentPlayer);
    if (!possibleMoves.contains(to)) {
      return false;
    }

    // clone the state and record it
    stateHistory.push(state.makeCopy());

    state.getField().set(to, new Disk(currentPlayer));
    flipDisks(to, currentPlayer);

    Player nextPlayer = Player.getOpponentOf(currentPlayer);
    if (!checkForWinningCondition()) {
      state.setCurrentPlayer(nextPlayer);
    }

    getState().increaseMoveCounter();
    currentPlayer.setDiskCount(currentPlayer.getDiskCount() - 1);
    notifyListeners();

    return true;
  }

  /**
   * Checks whether a winning condition is fulfilled. In that case, the {@link Phase phase} and the
   * {@link Player winning player} are set appropriately.
   *
   * @return <code>true</code> if the game is over, <code>false</code> otherwise
   */
  private boolean checkForWinningCondition() {
    if (state.getCurrentPhase() != Phase.RUNNING) {
      return true;
    }

    if (!canExecuteMove(Player.BLACK) || !canExecuteMove(Player.WHITE)) {
      // one of the players is not able to make a move anymore, so the game is over and the player
      // with more disks wins the game.
      Set<Cell> blackDisks = state.getAllCellsOfPlayer(Player.BLACK);
      Set<Cell> whiteDisks = state.getAllCellsOfPlayer(Player.WHITE);

      if (whiteDisks.size() > blackDisks.size()) {
        setGameFinished(Optional.of(Player.WHITE));
      } else if (blackDisks.size() > whiteDisks.size()) {
        setGameFinished(Optional.of(Player.BLACK));
      } else {
        setGameFinished(Optional.empty());
      }
      return true;
    }

    return false;
  }

  @Override
  public synchronized void undoMove() {
    state = stateHistory.pop();
    notifyListeners();
  }

  // TODO: javadoc
  private void flipDisks(Cell cell, Player player) {
    Set<List<Cell>> straightLineDisks = disksHorizontal(cell);
    straightLineDisks.addAll(disksHorizontal(cell));
    straightLineDisks.addAll(disksVertical(cell));
    straightLineDisks.addAll(disksDiagonal(cell));

    List<Cell> disksToFlip = new ArrayList<>();

    for (List<Cell> list : straightLineDisks) {
      if (!list.isEmpty() && checkStraightLineInterrupted(list, player)) {
        disksToFlip.addAll(cutListAt(player, list));
      }
    }

    for (Cell c : disksToFlip) {
      state.getField().set(c, new Disk(player));
    }
  }

  // TODO: javadoc
  private List<Cell> cutListAt(Player player, List<Cell> list) {
    List<Cell> newList = new ArrayList<>();

    for (Cell cell : list) {
      if (state.getField().get(cell).isPresent()
          && state.getField().get(cell).get().getPlayer().equals(player)) {
        break;
      } else {
        newList.add(cell);
      }
    }
    return newList;
  }

  // TODO: javadoc
  private boolean checkStraightLineInterrupted(List<Cell> list, Player player) {

    if (state.getField().get(list.get(0)).isEmpty()
        || state.getField().get(list.get(0)).get().getPlayer().equals(player)) {
      return false;
    }

    for (Cell cell : list) {
      if (state.getField().get(cell).isEmpty()) {
        return false;
      } else {
        if (state.getField().get(cell).get().getPlayer().equals(player)) {
          return true;
        }
      }
    }
    return false;
  }

  // TODO: javadoc
  private Set<List<Cell>> disksDiagonal(Cell to) {
    List<Cell> disksUpperRight = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell cell = new Cell(to.getColumn() + i, to.getRow() + i);

      if (!GameField.isWithinBounds(cell)) {
        break;
      }

      disksUpperRight.add(cell);
    }

    List<Cell> disksUpperLeft = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell cell = new Cell(to.getColumn() - i, to.getRow() + i);

      if (!GameField.isWithinBounds(cell)) {
        break;
      }

      disksUpperLeft.add(cell);
    }

    List<Cell> disksLowerRight = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell cell = new Cell(to.getColumn() + i, to.getRow() - i);

      if (!GameField.isWithinBounds(cell)) {
        break;
      }

      disksLowerRight.add(cell);
    }

    List<Cell> disksLowerLeft = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell cell = new Cell(to.getColumn() - i, to.getRow() - i);

      if (!GameField.isWithinBounds(cell)) {
        break;
      }

      disksLowerLeft.add(cell);
    }

    Set<List<Cell>> disksDiagonal = new HashSet<>();

    disksDiagonal.add(disksUpperRight);
    disksDiagonal.add(disksUpperLeft);
    disksDiagonal.add(disksLowerRight);
    disksDiagonal.add(disksLowerLeft);

    return disksDiagonal;
  }

  // TODO: javadoc
  private Set<List<Cell>> disksVertical(Cell to) {
    Set<List<Cell>> disksVertical = new HashSet<>();

    List<Cell> disksForward = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell cell = new Cell(to.getColumn(), to.getRow() + i);

      if (!GameField.isWithinBounds(cell)) {
        break;
      }

      disksForward.add(cell);
    }

    List<Cell> disksBehind = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell cell = new Cell(to.getColumn(), to.getRow() - i);

      if (!GameField.isWithinBounds(cell)) {
        break;
      }

      disksBehind.add(cell);
    }

    disksVertical.add(disksForward);
    disksVertical.add(disksBehind);

    return disksVertical;
  }

  // TODO: javadoc
  private Set<List<Cell>> disksHorizontal(Cell to) {
    Set<List<Cell>> disksHorizontal = new HashSet<>();

    List<Cell> disksRight = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell cell = new Cell(to.getColumn() + i, to.getRow());

      if (!GameField.isWithinBounds(cell)) {
        break;
      }

      disksRight.add(cell);
    }

    List<Cell> disksLeft = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell cell = new Cell(to.getColumn() - i, to.getRow());

      if (!GameField.isWithinBounds(cell)) {
        break;
      }

      disksLeft.add(cell);
    }

    disksHorizontal.add(disksRight);
    disksHorizontal.add(disksLeft);

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
    Set<Cell> possibleMoves = getPossibleMovesForPlayer(player);
    return !possibleMoves.isEmpty();
  }

  @Override
  public synchronized Set<Cell> getPossibleMovesForPlayer(Player player) {
    // TODO
    if (player != Player.WHITE && player != Player.BLACK) {
      throw new IllegalArgumentException("Unhandled player: " + player);
    }

    Set<Cell> possibleMoves = new HashSet<>();

    if (player.getDiskCount() <= 0) {
      return possibleMoves;
    }

    Set<Cell> cells;

    boolean firstFourMoves = false;

    if (player.getDiskCount() > Player.DISK_COUNT_START - 2) {
      // cells = middle 4 cells empty
      cells = state.getField().getMiddleFourEmptyCells();
      firstFourMoves = true;
    } else {
      cells = state.getField().getEmptyCells();
      // cells = empty
    }

    // für jede cell in cells
    for (Cell cell : cells) {
      if (firstFourMoves) {
        possibleMoves.add(cell);
      } else {
        boolean hasAdjacentOppositeDisk = false;
        boolean pathInterrupted = false;

        // 2.
        Cell forward = new Cell(cell.getColumn(), cell.getRow() + 1);
        Cell backward = new Cell(cell.getColumn(), cell.getRow() - 1);
        Cell right = new Cell(cell.getColumn() + 1, cell.getRow());
        Cell left = new Cell(cell.getColumn() - 1, cell.getRow());
        Cell diagonallyForwardRight = new Cell(cell.getColumn() + 1, cell.getRow() + 1);
        Cell diagonallyForwardLeft = new Cell(cell.getColumn() - 1, cell.getRow() + 1);
        Cell diagonallyBackwardRight = new Cell(cell.getColumn() + 1, cell.getRow() - 1);
        Cell diagonallyBackwardLeft = new Cell(cell.getColumn() - 1, cell.getRow() - 1);

        if ((GameField.isWithinBounds(forward)
                && state.getField().get(forward).isPresent()
                && state
                    .getField()
                    .get(forward)
                    .get()
                    .getPlayer()
                    .equals(Player.getOpponentOf(player)))
            || (GameField.isWithinBounds(backward)
                && state.getField().get(backward).isPresent()
                && state
                    .getField()
                    .get(backward)
                    .get()
                    .getPlayer()
                    .equals(Player.getOpponentOf(player)))
            || (GameField.isWithinBounds(right)
                && state.getField().get(right).isPresent()
                && state
                    .getField()
                    .get(right)
                    .get()
                    .getPlayer()
                    .equals(Player.getOpponentOf(player)))
            || (GameField.isWithinBounds(left)
                && state.getField().get(left).isPresent()
                && state
                    .getField()
                    .get(left)
                    .get()
                    .getPlayer()
                    .equals(Player.getOpponentOf(player)))
            || (GameField.isWithinBounds(diagonallyForwardRight)
                && state.getField().get(diagonallyForwardRight).isPresent()
                && state
                    .getField()
                    .get(diagonallyForwardRight)
                    .get()
                    .getPlayer()
                    .equals(Player.getOpponentOf(player)))
            || (GameField.isWithinBounds(diagonallyForwardLeft)
                && state.getField().get(diagonallyForwardLeft).isPresent()
                && state
                    .getField()
                    .get(diagonallyForwardLeft)
                    .get()
                    .getPlayer()
                    .equals(Player.getOpponentOf(player)))
            || (GameField.isWithinBounds(diagonallyBackwardRight)
                && state.getField().get(diagonallyBackwardRight).isPresent()
                && state
                    .getField()
                    .get(diagonallyBackwardRight)
                    .get()
                    .getPlayer()
                    .equals(Player.getOpponentOf(player)))
            || (GameField.isWithinBounds(diagonallyBackwardLeft)
                && state.getField().get(diagonallyBackwardLeft).isPresent()
                && state
                    .getField()
                    .get(diagonallyBackwardLeft)
                    .get()
                    .getPlayer()
                    .equals(Player.getOpponentOf(player)))) {
          // ... for backward, right, left, ...
          // 2. is true
          hasAdjacentOppositeDisk = true;
        }

        Set<List<Cell>> straightLineDisks = disksHorizontal(cell);
        straightLineDisks.addAll(disksHorizontal(cell));
        straightLineDisks.addAll(disksVertical(cell));
        straightLineDisks.addAll(disksDiagonal(cell));

        for (List<Cell> list : straightLineDisks) {
          if (!list.isEmpty() && checkStraightLineInterrupted(list, player)) {
            pathInterrupted = true;
            break;
          }
        }

        if (hasAdjacentOppositeDisk && pathInterrupted) {
          possibleMoves.add(cell);
        }
      }
    }

    return possibleMoves;
  }
}
