package reversi.model;

import reversi.model.ai.MinimaxAlgorithm;

/**
 * Extension of a {@link Reversi}-class, in which a human player plays against an ai-player. The logic
 * used for computing the moves of the ai-player is done by an implementation of a {@link
 * MinimaxAlgorithm minimax}-algorithm.
 *
 * @see Reversi
 */
public class AiReversi extends Reversi {

  private final MinimaxAlgorithm minMax;
  private final Player aiPlayer;

  /**
   * Initializes an ai-reversi, in which everything is set up such that a human player can play
   * against an ai. The human player always draws the {@link Player#BLACK black} disks and makes the
   * first move in the game.
   */
  public AiReversi() {
    super();
    minMax = new MinimaxAlgorithm();
    aiPlayer = Player.WHITE;
  }

  @Override
  public boolean move(Cell to) {
    if (!super.move(to)) {
      return false;
    }

    checkNextTurn();
    return true;
  }

  /**
   * Checks the next turn by determining whether it's the ai player's turn. Should that be the case,
   * the ai immediately computes and executes a move afterwards.
   */
  private void checkNextTurn() {
    if (isAiPlayersTurn()) {
      makeAiMove();
    }
  }

  /**
   * Computes the best possible move for an ai-player and executes it afterwards.
   *
   * <p>This method requires the current phase to be {@link Phase#RUNNING} in order to be executed
   * successfully.
   */
  void makeAiMove() {
    if (getState().getCurrentPhase() != Phase.RUNNING) {
      return;
    }

    Cell cell = minMax.determineCell(getState().makeCopy());
    move(cell);
  }

  /**
   * Checks whether it's currently the ai-players turn.
   *
   * @return <code>true</code> if the ai is the current active player, <code>false</code> otherwise.
   */
  private boolean isAiPlayersTurn() {
    return getState().getCurrentPlayer() == aiPlayer;
  }
}
