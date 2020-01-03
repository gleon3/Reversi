package reversi.model;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameFieldTest {

  private GameField field;

  @BeforeEach
  void setUp() {
    field = new GameField();
  }

  @Test
  void testIsWithinBounds_firstCell() {
    // arrange
    Cell zeroCell = new Cell(0, 0);

    // act
    boolean actual = GameField.isWithinBounds(zeroCell);

    // assert
    Assertions.assertTrue(actual, "Game field claims " + zeroCell + " is out ouf bounds");
  }

  @Test
  void testGet_negativeRow() {
    final Cell cellWithNegativeRow = new Cell(1, -1);

    try {
      field.get(cellWithNegativeRow);
      Assertions.fail("No exception on #get");

    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  void testGet_emptyCell() {
    final Cell validCell = new Cell(1, 1);

    Optional<Disk> pawnOnField = field.get(validCell);

    Assertions.assertEquals(Optional.empty(), pawnOnField);
  }
}
