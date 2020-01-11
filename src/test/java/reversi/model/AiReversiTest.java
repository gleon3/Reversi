package reversi.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AiReversiTest {

  private AiReversi aiReversi;

  @BeforeEach
  void setUp() {
    aiReversi = new AiReversi();
  }

  @Test
  void testStartMove() {

    Cell humanCell = new Cell(3, 3);
    aiReversi.move(humanCell);
    Cell aiCell = new Cell(3, 4);
    Assertions.assertTrue(aiReversi.getState().getField().isCellOfPlayer(Player.WHITE, aiCell));

    humanCell = new Cell(4, 4);
    aiReversi.move(humanCell);
    aiCell = new Cell(4, 3);
    Assertions.assertTrue(aiReversi.getState().getField().isCellOfPlayer(Player.WHITE, aiCell));
  }

  @Test
  void testMove() {

    aiReversi.move(new Cell(3, 3));
    aiReversi.move(new Cell(4, 4));

    Cell humanCell = new Cell(2, 4);
    aiReversi.move(humanCell);
    Cell aiCell = new Cell(4, 5);
    Assertions.assertTrue(aiReversi.getState().getField().isCellOfPlayer(Player.WHITE, aiCell));

    humanCell = new Cell(5, 4);
    aiReversi.move(humanCell);
    aiCell = new Cell(2, 3);
    Assertions.assertTrue(aiReversi.getState().getField().isCellOfPlayer(Player.WHITE, aiCell));

    humanCell = new Cell(2, 2);
    aiReversi.move(humanCell);
    aiCell = new Cell(1, 3);
    Assertions.assertTrue(aiReversi.getState().getField().isCellOfPlayer(Player.WHITE, aiCell));

    humanCell = new Cell(4, 2);
    aiReversi.move(humanCell);
    aiCell = new Cell(1, 4);
    Assertions.assertTrue(aiReversi.getState().getField().isCellOfPlayer(Player.WHITE, aiCell));

    humanCell = new Cell(0, 4);
    aiReversi.move(humanCell);
    aiCell = new Cell(0, 5);
    Assertions.assertTrue(aiReversi.getState().getField().isCellOfPlayer(Player.WHITE, aiCell));
  }
}
