package reversi.model.ai;

import java.util.Set;
import reversi.model.Cell;
import reversi.model.GameState;
import reversi.model.Player;

/**
 * Assessment class that rates the {@link GameState} by calculating static position values regarding
 * the current postions of the player's disks
 *
 * <p>The best possible score can be achieved by {@link Player#WHITE} having
 * as more position values as possible , while at the same time {@link Player#BLACK} is having as
 * less postion values as possible.
 */
public class WeightAssessor implements StateAssessor {
  int[][] staticWeights = {
          {20, -3, 11, 8, 8, 11, -3, 20},
          {-3, -7, -4, 1, 1, -4, -7, -3},
          {11, -4, 2, 2, 2, 2, -4, 11},
          {8, 1, 2, -3, -3, 2, 1, 8},
          {8, 1, 2, -3, -3, 2, 1, 8},
          {11, -4, 2, 2, 2, 2, -4, 11},
          {-3, -7, -4, 1, 1, -4, -7, -3},
          {20, -3, 11, 8, 8, 11, -3, 20}};

  @Override
  public double computeValue(GameState state, Player minPlayer, int depth) {

    assert minPlayer == Player.BLACK; // minPlayer is the human player

    Set<Cell> aiCells = state.getAllCellsOfPlayer(Player.WHITE); // ai player
    Set<Cell> humanCells = state.getAllCellsOfPlayer(Player.BLACK); // human player

    int positionValues = 0;

    for (Cell cell : aiCells) {
      positionValues += staticWeights[cell.getColumn()][cell.getRow()];
    }

    for (Cell cell : humanCells) {
      positionValues -= staticWeights[cell.getColumn()][cell.getRow()];
    }

    return positionValues;
  }
}
