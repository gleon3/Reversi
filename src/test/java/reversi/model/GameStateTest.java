package reversi.model;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GameStateTest {

  @Test
  void testGetAllCellsOfPlayer_emptyField() {
    GameField fieldMock = Mockito.mock(GameField.class);
    Mockito.when(fieldMock.get(any())).thenReturn(Optional.empty());

    GameState state = new GameState(fieldMock);

    Set<Cell> cellsWhite = state.getAllCellsOfPlayer(Player.WHITE);
    Set<Cell> cellsBlack = state.getAllCellsOfPlayer(Player.BLACK);

    Assertions.assertTrue(cellsWhite.isEmpty());
    Assertions.assertTrue(cellsBlack.isEmpty());
  }
}
