package reversi.model.ai;

import reversi.model.GameState;
import reversi.model.Phase;
import reversi.model.Player;

/**
 * Assessment class that rates the current {@link GameState} by evaluating whether any of the
 * players have won the game.
 *
 * <p>If the game is not yet over, a neutral value of zero points is returned. Otherwise, a constant
 * value of {@link WinVelocityAssessor#WINNING_SCORE 5000} will be taken and divided by the amount
 * of steps necessary in order to reach this state. Depending on the player, the value is positive
 * for {@link Player#WHITE} and negative for {@link Player#BLACK}.
 */
class WinVelocityAssessor implements StateAssessor {

  private static final double WINNING_SCORE = 5000.0;

  @Override
  public double computeValue(GameState state, Player minPlayer, int depth) {
    if (state.getCurrentPhase() != Phase.FINISHED || state.getWinner().isEmpty()) {
      return 0;
    }

    Player winner = state.getWinner().get();
    if (winner == minPlayer) {
      return -MODIFIER * WINNING_SCORE / depth;
    } else {
      return WINNING_SCORE / depth;
    }
  }
}
