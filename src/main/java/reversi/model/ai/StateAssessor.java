package reversi.model.ai;

import reversi.model.GameState;
import reversi.model.Player;

/**
 * Assessment interface used to rate the current {@link GameState} of a running reversi
 * application. This rating can then be used for computing a best possible move, which could either
 * be an ai in a single-player game, or for helping out a human player in a single step of a game.
 */
public interface StateAssessor {

  static final double MODIFIER = 1.5;

  /**
   * Rates the current {@link GameState} and returns the computed score afterwards. Independent of
   * the current {@link Player}, it always computes the best possible value for {@link Player#WHITE}
   * (the ai-player), while for the {@link Player#BLACK} (the human player), the score is always
   * kept at a minimal value.
   *
   * @param state The current {@link GameState} that is to be rated.
   * @param minPlayer The player for which the lowest-possible score is computed.
   * @param depth The amount of moves already made for reaching this state.
   * @return a double-value that is the rating of the current state.
   */
  double computeValue(GameState state, Player minPlayer, int depth);
}
