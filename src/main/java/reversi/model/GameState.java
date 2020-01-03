package reversi.model;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of a data structure class that contains all necessary attributes in order to
 * successfully play a game of reversi.
 */
public class GameState {

  private Phase currentPhase;
  private Player currentPlayer;
  private GameField gameField;
  private Player winner;
  private int diskCountBlack;
  private int diskCountWhite;

  private int moveCounter;

  /**
   * Constructs a new <code>GameState</code>. The state begins in a clear state, which means that
   * 1.) no disks are on the board initially, 2.) player black is set as the beginning player, and
   * 3.) the game has its {@link Phase} set to a running state.
   *
   * @param field the field to start the <code>GameState</code> with.
   */
  GameState(GameField field) {
    diskCountWhite = Player.DISK_COUNT_START;
    diskCountBlack = Player.DISK_COUNT_START;
    currentPhase = Phase.RUNNING;
    currentPlayer = Player.BLACK;
    gameField = field;
    winner = null;
    moveCounter = 0;
  }

  /**
   * Constructs a new <code>GameState</code>, that is cloned from another <code>GameState</code>.
   *
   * @param stateToClone the <code>GameState</code> to clone.
   */
  private GameState(GameState stateToClone) {
    diskCountWhite = stateToClone.diskCountWhite;
    diskCountBlack = stateToClone.diskCountBlack;
    currentPhase = stateToClone.currentPhase;
    currentPlayer = stateToClone.currentPlayer;
    gameField = new GameField(stateToClone.gameField);
    winner = stateToClone.winner;
    moveCounter = stateToClone.moveCounter;
  }

  /**
   * Returns a copy of the current game state.
   *
   * @return the copy of the game state.
   */
  synchronized GameState makeCopy() {
    return new GameState(this);
  }

  /**
   * Return the current amount of disks the player has left.
   *
   * @param player The player to get the amount of disks for.
   * @return the amount of disks.
   */
  int getDiskCount(Player player) {
    if (player.equals(Player.WHITE)) {
      return diskCountWhite;
    } else if (player.equals(Player.BLACK)) {
      return diskCountBlack;
    } else {
      throw new AssertionError("Unhandled Player!");
    }
  }

  /**
   * Set the amount of disks the player holds.
   *
   * @param player The player to set the amount of disks for.
   * @param diskCount The amount of disks the player holds now.
   */
  void setDiskCount(Player player, int diskCount) {
    if (player.equals(Player.WHITE)) {
      diskCountWhite = diskCount;
    } else if (player.equals(Player.BLACK)) {
      diskCountBlack = diskCount;
    } else {
      throw new AssertionError("Unhandled Player!");
    }
  }

  /** Increases the current move counter. */
  void increaseMoveCounter() {
    moveCounter++;
  }

  /**
   * Return the current move counter of the game.
   *
   * @return the current move counter.
   */
  public int getMoveCounter() {
    return moveCounter;
  }

  /**
   * Return the current phase of the game.
   *
   * @return the current phase.
   */
  public synchronized Phase getCurrentPhase() {
    return currentPhase;
  }

  /**
   * Overwrites the current {@link Phase phase} of the game with the new one.
   *
   * @param phase The new phase.
   */
  synchronized void setCurrentPhase(Phase phase) {
    currentPhase = requireNonNull(phase);
  }

  /**
   * Returns the {@link GameField gamefield} that stores the data for each cell of the
   * reversi-board.
   *
   * @return the current gamefield.
   */
  public synchronized GameField getField() {
    return gameField;
  }

  /**
   * Return the player that is currently allowed to make a move.
   *
   * @return the current player
   */
  public synchronized Player getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Return the winner of the current game. This method may only be called if the current game is
   * finished.
   *
   * @return {@link Optional#empty()} if the game's a draw. Otherwise an optional that contains the
   *     winner
   */
  public synchronized Optional<Player> getWinner() {
    if (currentPhase != Phase.FINISHED) {
      throw new IllegalStateException(
          String.format(
              "Expected current phase to be %s, but instead it is %s",
              Phase.FINISHED, currentPhase));
    }
    return Optional.ofNullable(winner);
  }

  /**
   * Set a winner to the current game. If a game ends in a draw, then no winner should be set here.
   *
   * @param winner An {@link Optional} containing either the winning {@link Player}, or no player if
   *     the game ends in a draw.
   */
  synchronized void setWinner(Optional<Player> winner) {
    this.winner = winner.orElse(null);
  }

  /**
   * Set the active player. For example, if {@link Player#BLACK} was previously active, the new
   * player might be set to {@link Player#WHITE}, and vice versa.
   *
   * @param newPlayer The player that may make his move now.
   */
  synchronized void setCurrentPlayer(Player newPlayer) {
    currentPlayer = newPlayer;
  }

  /**
   * Return all {@link Cell cells} of the current reversi board that belong to the requested player.
   *
   * @param player The player whose cells are to be retrieved.
   * @return A set of cells on which the player has currently his disks upon.
   */
  public synchronized Set<Cell> getAllCellsOfPlayer(Player player) {
    requireNonNull(player);
    return gameField.getCellsOccupiedWithDisks().entrySet().stream()
        .filter(x -> player == x.getValue())
        .map(Map.Entry::getKey)
        .collect(Collectors.toCollection(HashSet::new));
  }
}
