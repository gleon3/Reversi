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

  private int moveCounter;

  /**
   * Constructs a new <code>GameState</code>. The state begins in a clear state, which means that
   * 1.) no disks are on the board initially, 2.) player black is set as the beginning player, and
   * 3.) the game has its {@link Phase} set to a running state.
   */
  GameState(GameField field) {
    currentPhase = Phase.RUNNING;
    currentPlayer = Player.BLACK;
    gameField = field;
    winner = null;
    moveCounter = 0;
  }

  private GameState(GameState stateToClone) {
    currentPhase = stateToClone.currentPhase;
    currentPlayer = stateToClone.currentPlayer;
    gameField = new GameField(stateToClone.gameField);
    winner = stateToClone.winner;
    moveCounter = stateToClone.moveCounter;
  }

  synchronized GameState makeCopy() {
    return new GameState(this);
  }

  void increaseMoveCounter() {
    moveCounter++;
  }

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
