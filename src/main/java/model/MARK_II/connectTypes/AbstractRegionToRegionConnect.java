package model.MARK_II.connectTypes;

import model.MARK_II.region.Column;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 7, 2013
 */
public abstract class AbstractRegionToRegionConnect {

    public abstract void connect(Column[][] childRegionColumns, Column[][] parentRegionColumns,
                                 int numberOfColumnsToOverlapAlongXAxisOfRegion,
                                 int numberOfColumnsToOverlapAlongYAxisOfRegion);

    void checkParameters(Column[][] childRegionColumns, Column[][] parentRegionColumns,
                         int numberOfColumnsToOverlapAlongXAxisOfRegion,
                         int numberOfColumnsToOverlapAlongYAxisOfRegion) {
        if (parentRegionColumns == null) {
            throw new IllegalArgumentException(
                    "parentRegionColumns in connect method cannot be null");
        } else if (childRegionColumns == null) {
            throw new IllegalArgumentException(
                    "childRegionColumns in connect method cannot be null");
        } else if (childRegionColumns.length <= parentRegionColumns.length
                || childRegionColumns[0].length <= parentRegionColumns[0].length) {
            throw new IllegalArgumentException(
                    "childRegionColumns in connect method cannot be smaller in X or Y "
                            + "dimensions than the parentRegionColumns");
        } else if (numberOfColumnsToOverlapAlongXAxisOfRegion < 0) {
            throw new IllegalArgumentException(
                    "numberOfColumnsToOverlapAlongXAxisOfRegion in connect method cannot be < 0");
        } else if (numberOfColumnsToOverlapAlongYAxisOfRegion < 0) {
            throw new IllegalArgumentException(
                    "numberOfColumnsToOverlapAlongYAxisOfRegion in connect method cannot be < 0");
        }
    }
}
