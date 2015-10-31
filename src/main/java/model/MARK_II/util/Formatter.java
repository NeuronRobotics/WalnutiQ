package model.MARK_II.util;

import model.MARK_II.generalAlgorithm.ColumnPosition;
import java.util.Set;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version August 1, 2014
 */
public class Formatter {
    /**
     * @return Return the list of active column positions in the following
     * format: ((0, 0), (1, 1), (2, 2), (2,0))
     */
    public static String format(Set<ColumnPosition> activeColumnPositions) {
        String listOfActiveColumns = "(";

        int numberOfActiveColumns = activeColumnPositions.size();
        for (ColumnPosition columnPosition : activeColumnPositions) {
            listOfActiveColumns += "(" + columnPosition.getRow() + ", " +
                    columnPosition.getColumn() + ")";
            --numberOfActiveColumns;

            if (numberOfActiveColumns == 0) {
                // this is the last active column so don't print ", "
            } else {
                listOfActiveColumns += ", ";
            }
        }

        listOfActiveColumns += ")";
        return listOfActiveColumns;
    }
}
