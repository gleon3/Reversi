package reversi.model.ai;

import reversi.model.Cell;
import reversi.model.GameField;
import reversi.model.GameState;
import reversi.model.Player;

/**
 * Assessment class that rates the {@link GameState} by counting the number of disks
 * on four corners of the GameField.
 * Corner allows a player to build disks around them and provides stability to
 * the player's disks.
 *
 * <p>The best possible score can be achieved by {@link Player#WHITE} having
 * as many disks as possible on the corners,
 * while at the same time {@link Player#BLACK} is having as few disks
 * on the corners as possible.
 */
public class CornerAssessor implements StateAssessor {

  @Override
  public double computeValue(GameState state, Player minPlayer, int depth) {
    assert minPlayer == Player.BLACK; // minPlayer is the human player

    int aiCorner = 0;
    int humanCorner = 0;

    Cell[] cornerCells = {
      new Cell(0, 0),
      new Cell(0, GameField.SIZE - 1),
      new Cell(GameField.SIZE - 1, 0),
      new Cell(GameField.SIZE - 1, GameField.SIZE - 1)
    };

    for (Cell cornerCell : cornerCells) {
      if (state.getField().get(cornerCell).isPresent()
              && state.getField().get(cornerCell).get().getPlayer() == Player.WHITE) {
        aiCorner++;
      }
      if (state.getField().get(cornerCell).isPresent()
              && state.getField().get(cornerCell).get().getPlayer() == Player.BLACK) {
        humanCorner++;
      }
    }

    return 50 * (aiCorner - humanCorner);
  }
}
