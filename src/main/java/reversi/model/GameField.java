package reversi.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A class whose sole responsibility is the management of disks on the reversi board. As such it
 * provides the data structure that allows to check and manipulate each entry on the board
 * accordingly.
 */
public class GameField {

  public static final int SIZE = 8;

  private final Disk[][] field;

  GameField() {
    field = new Disk[SIZE][SIZE];
  }

  GameField(GameField gameField) {
    field = cloneField(gameField.field);
  }

  private Disk[][] cloneField(Disk[][] field) {
    Disk[][] clonedField = new Disk[SIZE][SIZE];

    for (int i = 0; i < SIZE; i++) {
      clonedField[i] = field[i].clone();
    }
    return clonedField;
  }

  /**
   * Return an {@link Optional} that may contain a {@link Disk}, depending on if there is one
   * positioned on the cell.
   *
   * @param cell The cell to be checked.
   * @return An {@link Optional optional} containing the respective disk in case of success.
   */
  public Optional<Disk> get(Cell cell) {
    throwErrorWhenOutOfBounds(cell);
    return Optional.ofNullable(field[cell.getColumn()][cell.getRow()]);
  }

  /**
   * Returns all {@link Cell cells} that are currently occupied by a disk.
   *
   * @return A map with all cells that have a disk on them.
   */
  public Map<Cell, Player> getCellsOccupiedWithDisks() {
    Map<Cell, Player> map = new HashMap<>();

    for (int column = 0; column < SIZE; column++) {
      for (int row = 0; row < SIZE; row++) {
        if (field[column][row] != null) {
          map.put(new Cell(column, row), field[column][row].getPlayer());
        }
      }
    }

    return map;
  }

  /**
   * Returns all {@link Cell cells} that are currently empty.
   *
   * @return A set with all cells that have no disk on them.
   */
  Set<Cell> getEmptyCells() {
    Set<Cell> set = new HashSet<>();

    for (int column = 0; column < SIZE; column++) {
      for (int row = 0; row < SIZE; row++) {
        if (field[column][row] == null) {
          set.add(new Cell(column, row));
        }
      }
    }

    return set;
  }

  /**
   * Returns the middle four {@link Cell cells} that are currently empty.
   *
   * @return A set with all cells that are empty and withhin the mid.
   */
  Set<Cell> getMiddleFourEmptyCells() {
    Set<Cell> set = new HashSet<>();

    for (int column = 3; column < 5; column++) {
      for (int row = 3; row < 5; row++) {
        if (field[column][row] == null) {
          set.add(new Cell(column, row));
        }
      }
    }

    return set;
  }

  /**
   * Set a disk on the given cell. Any disks already on that cell will be overridden.
   *
   * @param cell cell to set disk on
   * @param newValue new value (disk) to set on the cell
   * @throws IllegalArgumentException if given cell is out of field bounds
   */
  void set(Cell cell, Disk newValue) {
    throwErrorWhenOutOfBounds(cell);
    Objects.requireNonNull(newValue);

    field[cell.getColumn()][cell.getRow()] = newValue; // may override an existing disk
  }

  /**
   * Remove disk from the given cell. This method only has to work if there is a disk on the cell.
   *
   * @param cell cell to remove any disk from
   * @return the disk that was removed
   * @throws IllegalArgumentException if given cell is out of field bounds
   */
  Disk remove(Cell cell) {
    throwErrorWhenOutOfBounds(cell);
    int col = cell.getColumn();
    int row = cell.getRow();
    if (field[col][row] == null) {
      throw new IllegalArgumentException("There's no disk to delete for cell " + cell);
    }

    Disk removed = field[col][row];
    field[col][row] = null;
    return removed;
  }

  /**
   * Checks a {@link Cell cell} whether it is occupied by a Disk and in case of success, whether it
   * belongs to the respective {@link Player player}.
   *
   * @param player The player to be checked.
   * @param cell The cell that can contain a disk.
   * @return <code>true</code> if the player has a disk on the cell, <code>false</code> otherwise.
   */
  boolean isCellOfPlayer(Player player, Cell cell) {
    Optional<Disk> diskOpt = get(cell);
    return diskOpt.isPresent() && diskOpt.get().getPlayer() == player;
  }

  /**
   * Checks a cell for its bounds and throws an exception in case of failure.
   *
   * @param cell The cell to be checked.
   */
  private void throwErrorWhenOutOfBounds(Cell cell) {
    if (!isWithinBounds(cell)) {
      throw new IllegalArgumentException("Coordinates of cell are out of bounds: " + cell);
    }
  }

  /**
   * Checks a {@link Cell} if its column- and row-value is within the bounds. The valid range is
   * from 0 to 7 for each.
   *
   * @param cell The cell to be checked
   * @return <code>true</code> if within the bounds, <code>false</code> otherwise.
   */
  public static boolean isWithinBounds(Cell cell) {
    return cell.getColumn() >= 0
        && cell.getColumn() < SIZE
        && cell.getRow() >= 0
        && cell.getRow() < SIZE;
  }
}
