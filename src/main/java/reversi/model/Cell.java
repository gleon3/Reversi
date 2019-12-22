package reversi.model;

import java.util.Objects;

/** Cell class that contains all necessary information for being represented on a 2D-board. */
public class Cell implements Comparable<Cell> {

    private final int column;
    private final int row;

    /**
     * Create a new Cell with coordinates given in a numeric format.
     *
     * @param column The x-value of the cell.
     * @param row The y-value of the cell.
     */
    public Cell(int column, int row) {
        this.column = column;
        this.row = row;
    }

    /**
     * Returns the column of this cell as integer index. Column values range from 0 to 7 and describe
     * the reversi columns from A to H, respectively.
     *
     * @return the column value of this cell, as integer index
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the row of this cell as integer index. Row values range from 0 to 7 and describe the
     * reversi rows from 1 to 8, respectively.
     *
     * @return the row of this cell, as integer index
     */
    public int getRow() {
        return row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cell)) {
            return false;
        }

        Cell other = (Cell) obj;
        return Objects.equals(column, other.column) && Objects.equals(row, other.row);
    }

    @Override
    public String toString() {
        return column + "," + row;
    }

    @Override
    public int compareTo(Cell other) {
        int colDiff = column - other.getColumn();
        if (colDiff == 0) {
            return row - other.getRow();
        }
        return colDiff;
    }
}
