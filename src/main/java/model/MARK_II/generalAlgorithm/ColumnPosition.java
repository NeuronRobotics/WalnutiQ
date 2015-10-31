package model.MARK_II.generalAlgorithm;

/**
 * Represents the position of a Column within a Region.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 18, 2013
 */
public class ColumnPosition {
    private int row; // row position within Region
    private int column; // column position within Region

    public ColumnPosition(int row, int column) {
        if (row < 0) {
            throw new IllegalArgumentException(
                    "row in ColumnPosition class constructor cannot be < 0");
        } else if (column < 0) {
            throw new IllegalArgumentException(
                    "column in ColumnPosition class constructor cannot be < 0");
        }
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n=======================");
        stringBuilder.append("\n--ColumnPosition Info--");
        stringBuilder.append("\n(row, column): ");
        stringBuilder.append("(" + this.row + ", " + this.column + ")");
        stringBuilder.append("\n=======================");
        String visionCellInformation = stringBuilder.toString();
        return visionCellInformation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.row;
        result = prime * result + this.column;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        ColumnPosition otherColumnPosition = (ColumnPosition) obj;
        if (this.row != otherColumnPosition.row)
            return false;
        if (this.column != otherColumnPosition.column)
            return false;
        return true;
    }
}
