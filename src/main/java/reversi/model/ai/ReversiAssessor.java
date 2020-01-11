package reversi.model.ai;

import java.util.Arrays;
import java.util.List;
import reversi.model.GameState;
import reversi.model.Player;

/**
 * A composite assessor class that allows to compute the score for a given {@link
 * GameState}-instance. The class consists of several other assessor classes that do the actual work
 * of rating the various parts in the state.
 *
 * @see DiskCountAssessor
 * @see CornerAssessor
 * @see MobilityAssessor
 * @see WeightAssessor
 * @see WinVelocityAssessor
 */
public class ReversiAssessor implements StateAssessor {

  private static final List<StateAssessor> ASSESSORS =
          Arrays.asList(
                  new DiskCountAssessor(),
                  new CornerAssessor(),
                  new MobilityAssessor(),
                  new WeightAssessor(),
                  new WinVelocityAssessor()
          );

  @Override
  public double computeValue(GameState state, Player minPlayer, int depth) {
    double result = 0;
    for (StateAssessor assessor : ASSESSORS) {
      result += assessor.computeValue(state, minPlayer, depth);
    }
    return result;
  }
}
