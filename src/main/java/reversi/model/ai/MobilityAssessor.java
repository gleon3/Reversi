package reversi.model.ai;

import reversi.model.GameState;
import reversi.model.Player;
import reversi.model.Reversi;

/**
 * Assessment class that rates the {@link GameState} by counting the number of the possible moves of
 * the player.
 *
 * <p>The best possible score can be achieved by {@link Player#WHITE} having
 * as many possible moves as possible , while at the same time {@link Player#BLACK} is having as
 * few possible moves as possible.
 */
public class MobilityAssessor implements StateAssessor {

  @Override
  public double computeValue(GameState state, Player minPlayer, int depth) {

    assert minPlayer == Player.BLACK; // minPlayer is the human player

    Reversi reversi = new Reversi(state);

    int numOfAiMoves = reversi.getPossibleMovesForPlayer(Player.WHITE).size();
    int numOfHumanMoves = reversi.getPossibleMovesForPlayer(Player.BLACK).size();

    return (float) 100 * (numOfAiMoves - numOfHumanMoves) / (numOfAiMoves + numOfHumanMoves);

  }
}
