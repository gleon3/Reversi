package reversi.model.ai;

import static reversi.model.Player.getOpponentOf;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import reversi.model.Cell;
import reversi.model.GameState;
import reversi.model.Model;
import reversi.model.Phase;
import reversi.model.Player;
import reversi.model.Reversi;

/**
 * An implementation of the minimax algorithm. It allows to compute a move for the reversi game, in
 * which the potential loss in a worst-case-scenario is minimized. This means for the ai-player
 * ({@link Player#WHITE}) that it takes the best possible move out of all potential moves that the
 * opponent ({@link Player#BLACK} has forced upon the ai-player.
 */
public class MinimaxAlgorithm {

  private static final int LOOK_AHEAD = 3;

  private final StateAssessor assessor;

  private Model reversi;
  private Cell savedCell;
  private Player maxPlayer; // the AI player
  private Player minPlayer; // the human player

  /**
   * Creates a new instance of the {@link MinimaxAlgorithm}, in which the assessment of each {@link
   * GameState} is predefined by a given set of {@link ReversiAssessor rules}.
   */
  public MinimaxAlgorithm() {
    assessor = new ReversiAssessor();
  }

  /**
   * Computes a possible move for the current {@link GameState}, which is done by an underlying
   * minimax-algorithm. This method works only if the phase of the game is not yet set to {@link
   * Phase#FINISHED finished}.
   *
   * @param gameState The current {@link GameState} of the reversi application.
   * @return A {@link Cell} that contains the computed target-cell.
   */
  public Cell determineCell(GameState gameState) {
    if (gameState.getCurrentPhase() == Phase.FINISHED) {
      return null;
    }

    // create a new reversi instance with a reference to this gameState,
    // so that we get access to methods like move(), undoMove(), etc.
    reversi = new Reversi(gameState);

    savedCell = null;
    maxPlayer = gameState.getCurrentPlayer();
    minPlayer = getOpponentOf(maxPlayer);
    max(maxPlayer, 0);

    return savedCell;
  }

  private double max(Player player, int currentDepth) {
    double currentStateScore = assessor.computeValue(reversi.getState(), minPlayer, currentDepth);

    if (reversi.getState().getCurrentPhase() == Phase.FINISHED || currentDepth >= LOOK_AHEAD) {
      return currentStateScore;
    }

    if (reversi.getState().getCurrentPlayer() != player) {
      // player skipped
      return max(getOpponentOf(player), currentDepth + 1) + currentStateScore;
    }

    double maxValue = -Double.MAX_VALUE;
    Cell currentBestCell = null;

    Set<Cell> targetCells = new TreeSet<>(Comparator.naturalOrder());
    targetCells.addAll(reversi.getPossibleMovesForPlayer(player));

    for (Cell targetCell : targetCells) {
      reversi.move(targetCell);

      double value = min(getOpponentOf(player), currentDepth + 1) + currentStateScore;

      reversi.undoMove();

      if (Double.compare(value, maxValue) > 0) {
        maxValue = value;
        currentBestCell = targetCell;
      }
    }

    if (currentDepth == 0) {
      savedCell = currentBestCell;
    }

    return maxValue;
  }

  private double min(Player player, int currentDepth) {
    double currentStateScore = assessor.computeValue(reversi.getState(), minPlayer, currentDepth);

    if (reversi.getState().getCurrentPhase() == Phase.FINISHED || currentDepth >= LOOK_AHEAD) {
      return currentStateScore;
    }

    if (reversi.getState().getCurrentPlayer() != player) {
      // player skipped
      return min(getOpponentOf(player), currentDepth + 1) + currentStateScore;
    }

    double minValue = Double.MAX_VALUE;
    Cell currentBestCell = null;

    Set<Cell> targetCells = new TreeSet<>(Comparator.naturalOrder());
    targetCells.addAll(reversi.getPossibleMovesForPlayer(player));

    for (Cell targetCell : targetCells) {
      reversi.move(targetCell);

      double value = max(getOpponentOf(player), currentDepth + 1) + currentStateScore;

      reversi.undoMove();

      if (Double.compare(value, minValue) < 0) {
        minValue = value;
        currentBestCell = targetCell;
      }
    }

    if (currentDepth == 0) {
      savedCell = currentBestCell;
    }

    return minValue;
  }

}
