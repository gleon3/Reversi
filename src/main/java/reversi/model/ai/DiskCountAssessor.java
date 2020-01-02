package reversi.model.ai;

import java.util.Set;
import reversi.model.Cell;
import reversi.model.GameState;
import reversi.model.Player;

/**
 * Assessment class that rates the {@link GameState} by evaluating the number of disks on the GameField.
 *
 * <p>The best possible score can be achieved by {@link Player#WHITE} having
 * as much disks as possible on the field, while at the same time {@link Player#BLACK} is having as
 * few disks as possible.
 */
public class DiskCountAssessor implements StateAssessor {

  @Override
  public double computeValue(GameState state, Player minPlayer, int depth) {
    assert minPlayer == Player.BLACK; // minPlayer is the human player

    Set<Cell> aiCells = state.getAllCellsOfPlayer(Player.WHITE); // ai player
    Set<Cell> humanCells = state.getAllCellsOfPlayer(Player.BLACK); // human player

    return aiCells.size() - humanCells.size();
  }
}
