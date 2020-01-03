package reversi.model;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReversiTest {

  private Reversi reversi;

  @BeforeEach
  void setUp() {
    reversi = new Reversi();
  }

  @Test
  void testNew_Game() {
    // arrange
    reversi.newGame();

    // act
    Player currentPlayer = reversi.getState().getCurrentPlayer();
    Phase currentPhase = reversi.getState().getCurrentPhase();

    try {
      reversi.getState().getWinner();
      Assertions.fail("No exception on #get");

    } catch (IllegalStateException e) {
      // expected
    }

    GameField gameField = reversi.getState().getField();

    // assert
    Assertions.assertEquals(currentPlayer, Player.BLACK);
    Assertions.assertEquals(currentPhase, Phase.RUNNING);
    Assertions.assertTrue(gameField.getCellsOccupiedWithDisks().isEmpty());
  }

  @Test
  void testValid_Move() {
    // arrange
    reversi.newGame();

    // act
    Player playerBeforeMove = reversi.getState().getCurrentPlayer();
    int diskCountBeforeMove = reversi.getState().getDiskCount(playerBeforeMove);
    Cell cell = new Cell(4, 4);
    boolean move = reversi.move(cell);

    // assert
    Assertions.assertTrue(move);
    Assertions.assertEquals(
            reversi.getState().getDiskCount(playerBeforeMove), diskCountBeforeMove - 1);
    Assertions.assertEquals(
            reversi.getState().getCurrentPlayer(), Player.getOpponentOf(playerBeforeMove));
    Assertions.assertTrue(
        reversi.getState().getField().get(cell).isPresent()
            && reversi.getState().getField().get(cell).get().getPlayer().equals(playerBeforeMove));
  }

  @Test
  void testInvalid_Move() {
    // arrange
    reversi.newGame();

    // act
    Player playerBeforeMove = reversi.getState().getCurrentPlayer();
    int diskCountBeforeMove = reversi.getState().getDiskCount(playerBeforeMove);
    Cell cell = new Cell(0, 0);
    reversi.move(cell);
    Player playerAfterMove = reversi.getState().getCurrentPlayer();
    int diskCountAfterMove = reversi.getState().getDiskCount(playerBeforeMove);

    // assert
    Assertions.assertEquals(playerAfterMove, playerBeforeMove);
    Assertions.assertEquals(diskCountAfterMove, diskCountBeforeMove);
    Assertions.assertTrue(reversi.getState().getField().get(cell).isEmpty());
  }

  @Test
  void testFirst_Moves() {
    reversi.newGame();

    boolean move1 = reversi.move(new Cell(3, 3));
    boolean move2 = reversi.move(new Cell(3, 4));
    boolean move3 = reversi.move(new Cell(4, 4));
    boolean move4 = reversi.move(new Cell(4, 3));
    int blackDiskCount4 = reversi.getState().getAllCellsOfPlayer(Player.BLACK).size();
    int whiteDiskCount4 = reversi.getState().getAllCellsOfPlayer(Player.WHITE).size();

    boolean move5 = reversi.move(new Cell(3, 5));
    int blackDiskCount5 = reversi.getState().getAllCellsOfPlayer(Player.BLACK).size();
    int whiteDiskCount5 = reversi.getState().getAllCellsOfPlayer(Player.WHITE).size();
    boolean move6 = reversi.move(new Cell(2, 3));
    int blackDiskCount6 = reversi.getState().getAllCellsOfPlayer(Player.BLACK).size();
    int whiteDiskCount6 = reversi.getState().getAllCellsOfPlayer(Player.WHITE).size();

    // check board was set up correctly
    Assertions.assertTrue(move1);
    Assertions.assertTrue(move2);
    Assertions.assertTrue(move3);
    Assertions.assertTrue(move4);
    Assertions.assertEquals(blackDiskCount4, 2);
    Assertions.assertEquals(whiteDiskCount4, 2);

    // check that the next moves (move 5, move 6) worked as intended
    Assertions.assertTrue(move5);
    Assertions.assertEquals(blackDiskCount5, 4);
    Assertions.assertEquals(whiteDiskCount5, 1);
    Assertions.assertTrue(move6);
    Assertions.assertEquals(blackDiskCount6, 3);
    Assertions.assertEquals(whiteDiskCount6, 3);
  }

  @Test
  void testGet_PossibleMoves() {
    reversi.newGame();

    int numberOfPossibleMoves0 =
            reversi.getPossibleMovesForPlayer(reversi.getState().getCurrentPlayer()).size();
    Assertions.assertEquals(numberOfPossibleMoves0, 4);
    reversi.move(new Cell(3, 3));
    int numberOfPossibleMoves1 =
            reversi.getPossibleMovesForPlayer(reversi.getState().getCurrentPlayer()).size();
    Assertions.assertEquals(numberOfPossibleMoves1, 3);
    reversi.move(new Cell(3, 4));
    int numberOfPossibleMoves2 =
            reversi.getPossibleMovesForPlayer(reversi.getState().getCurrentPlayer()).size();
    Assertions.assertEquals(numberOfPossibleMoves2, 2);
    reversi.move(new Cell(4, 4));
    int numberOfPossibleMoves3 =
            reversi.getPossibleMovesForPlayer(reversi.getState().getCurrentPlayer()).size();
    Assertions.assertEquals(numberOfPossibleMoves3, 1);
    reversi.move(new Cell(4, 3));
    int numberOfPossibleMoves4 =
            reversi.getPossibleMovesForPlayer(reversi.getState().getCurrentPlayer()).size();
    Assertions.assertEquals(numberOfPossibleMoves4, 4);
    reversi.move(new Cell(3, 5));
    int numberOfPossibleMoves5 =
            reversi.getPossibleMovesForPlayer(reversi.getState().getCurrentPlayer()).size();
    Assertions.assertEquals(numberOfPossibleMoves5, 3);
  }

  @Test
  void testFlips() {
    // set up board
    reversi.newGame();
    reversi.getState().setCurrentPlayer(Player.WHITE);
    reversi.getState().getField().set(new Cell(0, 0), new Disk(Player.WHITE));
    reversi.getState().getField().set(new Cell(1, 1), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(7, 1), new Disk(Player.WHITE));
    reversi.getState().getField().set(new Cell(2, 2), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(6, 2), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(3, 3), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(5, 3), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(0, 4), new Disk(Player.WHITE));
    reversi.getState().getField().set(new Cell(1, 4), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(2, 4), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(3, 4), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(5, 4), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(6, 4), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(7, 4), new Disk(Player.WHITE));
    reversi.getState().getField().set(new Cell(3, 5), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(5, 5), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(2, 6), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(6, 6), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(1, 7), new Disk(Player.WHITE));
    reversi.getState().getField().set(new Cell(7, 7), new Disk(Player.WHITE));

    int diskAmountBlackStart = reversi.getState().getAllCellsOfPlayer(Player.BLACK).size();
    int diskAmountWhiteStart = reversi.getState().getAllCellsOfPlayer(Player.WHITE).size();

    // move to cell(4, 4) which should cause every black disk to flip
    boolean move = reversi.move(new Cell(4, 4));

    int diskAmountBlackAfter = reversi.getState().getAllCellsOfPlayer(Player.BLACK).size();
    int diskAmountWhiteAfter = reversi.getState().getAllCellsOfPlayer(Player.WHITE).size();

    Assertions.assertEquals(diskAmountBlackStart, 14);
    Assertions.assertEquals(diskAmountWhiteStart, 6);
    Assertions.assertTrue(move);
    Assertions.assertEquals(diskAmountBlackAfter, 0);
    Assertions.assertEquals(diskAmountWhiteAfter, 21);
  }

  @Test
  void testEnd_Game1() {
    // set up board
    reversi.newGame();
    reversi.getState().setCurrentPlayer(Player.BLACK);
    reversi.getState().setDiskCount(Player.BLACK, 20);
    reversi.getState().setDiskCount(Player.WHITE, 20);
    for (int row = 0; row < GameField.SIZE; row++) {
      for (int col = 0; col < GameField.SIZE; col++) {
        if (!(row == 0 && col == 0)) {
          if ((row + col) % 2 == 0) {
            reversi.getState().getField().set(new Cell(col, row), new Disk(Player.BLACK));
          } else {
            reversi.getState().getField().set(new Cell(col, row), new Disk(Player.WHITE));
          }
        }
      }
    }

    int diskAmountStart = reversi.getState().getField().getCellsOccupiedWithDisks().size();

    boolean move = reversi.move(new Cell(0, 0));
    Phase newPhase = reversi.getState().getCurrentPhase();

    Assertions.assertEquals(diskAmountStart, 63);

    Assertions.assertTrue(move);
    Assertions.assertEquals(newPhase, Phase.FINISHED);

    Optional<Player> winner = reversi.getState().getWinner();
    Assertions.assertEquals(winner, Optional.of(Player.BLACK));
  }

  @Test
  void testEnd_Game2() {
    // set up board
    reversi.newGame();
    reversi.getState().setCurrentPlayer(Player.BLACK);
    reversi.getState().setDiskCount(Player.BLACK, 1);
    reversi.getState().setDiskCount(Player.WHITE, 3);
    reversi.getState().getField().set(new Cell(3, 3), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(4, 4), new Disk(Player.BLACK));
    reversi.getState().getField().set(new Cell(3, 4), new Disk(Player.WHITE));
    reversi.getState().getField().set(new Cell(4, 3), new Disk(Player.WHITE));

    boolean move = reversi.move(new Cell(5, 3));
    Phase newPhase = reversi.getState().getCurrentPhase();

    Assertions.assertTrue(move);
    Assertions.assertEquals(reversi.getState().getDiskCount(Player.BLACK), 0);
    Assertions.assertEquals(newPhase, Phase.FINISHED);

    Optional<Player> winner = reversi.getState().getWinner();

    Assertions.assertEquals(winner, Optional.of(Player.BLACK));
  }
}
