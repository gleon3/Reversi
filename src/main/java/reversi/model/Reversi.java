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

import reversi.model.network.Game;

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
  protected void notifyListeners(boolean wasActiveChange) {
    support.firePropertyChange(STATE_CHANGED, null, this);
    if (wasActiveChange) {
      support.firePropertyChange(NEW_MOVE, null, this);
    }
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
  public synchronized void setState(GameState state) {
    this.state = state;
    notifyListeners(false);
  }

  @Override
  public synchronized void newGame() {
    newGame(true);
  }

  synchronized void newGame(Boolean notifyClient) {
    state = new GameState(new GameField());

    notifyListeners(notifyClient);
  }

  @Override
  public void startLobby() {}

  @Override
  public void stopGame() {
    state.setCurrentPhase(Phase.FINISHED);

    notifyListeners(false);
  }

  @Override
  public void startGame(Player player) {
    // do nothing
  }

  @Override
  public void joinGame(int gameID, Player player) {
    // do nothing
  }

  @Override
  public void leaveLobby() {
    // do nothing
  }

  @Override
  public List<Game> getOpenGames() {
    return new ArrayList<>();
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
    state.setDiskCount(currentPlayer, state.getDiskCount(currentPlayer) - 1);

    Player nextPlayer = Player.getOpponentOf(currentPlayer);
    if (!checkForWinningCondition()) {
      state.setCurrentPlayer(nextPlayer);
    }

    getState().increaseMoveCounter();
    notifyListeners(true);

    return true;
  }

  /**
   * Checks whether a winning condition is fulfilled. In that case, the {@link Phase phase} and the
   * {@link Player winning player} are set appropriately.
   *
   * @return <code>true</code> if the game is over, <code>false</code> otherwise.
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
    notifyListeners(true);
  }

  /**
   * According to the reversi rules after a move to a given cell by a given player, change the
   * player for disks of opponent.
   *
   * @param cell The cell the player moved to.
   * @param player The player that moved.
   */
  private void flipDisks(Cell cell, Player player) {
    Set<List<Cell>> straightLineCells = new HashSet<>();
    straightLineCells.addAll(cellsHorizontalOf(cell));
    straightLineCells.addAll(cellsVerticalOf(cell));
    straightLineCells.addAll(cellsDiagonalOf(cell));

    List<Cell> disksToFlip = new ArrayList<>();

    for (List<Cell> list : straightLineCells) {
      if (!list.isEmpty() && checkFullyInterrupted(list, player)) {
        disksToFlip.addAll(cutListAt(player, list));
      }
    }

    for (Cell c : disksToFlip) {
      state.getField().set(c, new Disk(player));
    }
  }

  /**
   * Cut a list of cells at the first instance of a given player (including the cell with the disk
   * of the player on).
   *
   * @param player The player to cut the list at.
   * @param list The list of cells to cut.
   * @return the given list but cut at the first instance of the given player.
   */
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

  /**
   * Check whether a list of cells is fully interrupted by disks of opponent of given player before
   * the first disk of the given player.
   *
   * @param list The list to check for.
   * @param player The player to check for.
   * @return @return <code>true</code> if the list is fully interrupted before the first disk of
   *     player, <code>false</code> otherwise.
   */
  private boolean checkFullyInterrupted(List<Cell> list, Player player) {

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

  /**
   * Return a set of lists of cells that that hold cells diagonal of the given cell(upper-right,
   * upper-left, lower-left, lower-right of cell) ordered from closest to given cell to furthest to
   * given cell.
   *
   * @param cell The cell to build the set for.
   * @return A set of lists of cells that are diagonal of given cell.
   */
  private Set<List<Cell>> cellsDiagonalOf(Cell cell) {
    List<Cell> disksUpperRight = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell c = new Cell(cell.getColumn() + i, cell.getRow() + i);

      if (!GameField.isWithinBounds(c)) {
        break;
      }

      disksUpperRight.add(c);
    }

    List<Cell> disksUpperLeft = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell c = new Cell(cell.getColumn() - i, cell.getRow() + i);

      if (!GameField.isWithinBounds(c)) {
        break;
      }

      disksUpperLeft.add(c);
    }

    List<Cell> disksLowerRight = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell c = new Cell(cell.getColumn() + i, cell.getRow() - i);

      if (!GameField.isWithinBounds(c)) {
        break;
      }

      disksLowerRight.add(c);
    }

    List<Cell> disksLowerLeft = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell c = new Cell(cell.getColumn() - i, cell.getRow() - i);

      if (!GameField.isWithinBounds(c)) {
        break;
      }

      disksLowerLeft.add(c);
    }

    Set<List<Cell>> disksDiagonal = new HashSet<>();

    disksDiagonal.add(disksUpperRight);
    disksDiagonal.add(disksUpperLeft);
    disksDiagonal.add(disksLowerRight);
    disksDiagonal.add(disksLowerLeft);

    return disksDiagonal;
  }

  /**
   * Return a set of lists of cells that that hold cells vertical of the given cell(forward, behind
   * of cell) ordered from closest to given cell to furthest to given cell.
   *
   * @param cell The cell to build the set for.
   * @return A set of lists of cells that are vertical of given cell.
   */
  private Set<List<Cell>> cellsVerticalOf(Cell cell) {
    Set<List<Cell>> disksVertical = new HashSet<>();

    List<Cell> disksForward = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell c = new Cell(cell.getColumn(), cell.getRow() + i);

      if (!GameField.isWithinBounds(c)) {
        break;
      }

      disksForward.add(c);
    }

    List<Cell> disksBehind = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell c = new Cell(cell.getColumn(), cell.getRow() - i);

      if (!GameField.isWithinBounds(c)) {
        break;
      }

      disksBehind.add(c);
    }

    disksVertical.add(disksForward);
    disksVertical.add(disksBehind);

    return disksVertical;
  }

  /**
   * Return a set of lists of cells that that hold cells horizontal of the given cell(right, left of
   * cell) ordered from closest to given cell to furthest to given cell.
   *
   * @param cell The cell to build the set for.
   * @return A set of lists of cells that are horizontal of given cell.
   */
  private Set<List<Cell>> cellsHorizontalOf(Cell cell) {
    Set<List<Cell>> disksHorizontal = new HashSet<>();

    List<Cell> disksRight = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell c = new Cell(cell.getColumn() + i, cell.getRow());

      if (!GameField.isWithinBounds(c)) {
        break;
      }

      disksRight.add(c);
    }

    List<Cell> disksLeft = new ArrayList<>();

    for (int i = 1; i < GameField.SIZE; i++) {
      Cell c = new Cell(cell.getColumn() - i, cell.getRow());

      if (!GameField.isWithinBounds(c)) {
        break;
      }

      disksLeft.add(c);
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

    // notifyListeners(true);
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
    if (player != Player.WHITE && player != Player.BLACK) {
      throw new IllegalArgumentException("Unhandled player: " + player);
    }

    // if the player doesn't have any disks left then return an empty set as the player can't make
    // any more moves
    if (state.getDiskCount(player) <= 0) {
      return new HashSet<>();
    }

    // in the first four moves the middle four empty cells are possible moves
    if (state.getDiskCount(player) > Player.DISK_COUNT_START - 2) {
      return state.getField().getMiddleFourEmptyCells();
    }

    Set<Cell> possibleMoves = new HashSet<>();

    // a disk should only be able to be set on empty cells, so go through all of the empty cells of
    // the board and check whether a move to the cell fulfills the other reversi rules
    for (Cell cell : state.getField().getEmptyCells()) {
      // a disk should only be able to be placed on a cell that has an opposite disk on one of the
      // adjacent cells
      boolean hasAdjacentOppositeDisk = false;
      // a disk should only be able to be set on a cell that has at least one straight path to a
      // disk of player that is fully interrupted by disks of opponent
      boolean pathInterrupted = false;

      Cell forward = new Cell(cell.getColumn(), cell.getRow() + 1);
      Cell backward = new Cell(cell.getColumn(), cell.getRow() - 1);
      Cell right = new Cell(cell.getColumn() + 1, cell.getRow());
      Cell left = new Cell(cell.getColumn() - 1, cell.getRow());
      Cell diagonallyForwardRight = new Cell(cell.getColumn() + 1, cell.getRow() + 1);
      Cell diagonallyForwardLeft = new Cell(cell.getColumn() - 1, cell.getRow() + 1);
      Cell diagonallyBackwardRight = new Cell(cell.getColumn() + 1, cell.getRow() - 1);
      Cell diagonallyBackwardLeft = new Cell(cell.getColumn() - 1, cell.getRow() - 1);

      // checks the current empty cell if there is a disk of the opposite player on one of the
      // adjacent cells, if there is set hasAdjacentOppositeDisk to true
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
              && state.getField().get(right).get().getPlayer().equals(Player.getOpponentOf(player)))
          || (GameField.isWithinBounds(left)
              && state.getField().get(left).isPresent()
              && state.getField().get(left).get().getPlayer().equals(Player.getOpponentOf(player)))
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
        hasAdjacentOppositeDisk = true;
      }

      Set<List<Cell>> straightLineCells = new HashSet<>();
      straightLineCells.addAll(cellsHorizontalOf(cell));
      straightLineCells.addAll(cellsVerticalOf(cell));
      straightLineCells.addAll(cellsDiagonalOf(cell));

      // go through every straight line (list of cells) and check the straight line condition
      // if one fulfills it, then set pathInterrupted to true and stop the for loop and continue
      for (List<Cell> list : straightLineCells) {
        if (!list.isEmpty() && checkFullyInterrupted(list, player)) {
          pathInterrupted = true;
          break;
        }
      }

      // if an empty cell fulfills both of the tested requirements then it is a possible move
      if (hasAdjacentOppositeDisk && pathInterrupted) {
        possibleMoves.add(cell);
      }
    }

    return possibleMoves;
  }
}
