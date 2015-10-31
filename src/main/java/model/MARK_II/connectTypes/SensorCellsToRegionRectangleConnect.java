package model.MARK_II.connectTypes;

import model.MARK_II.region.Column;
import model.MARK_II.region.Synapse;
import model.MARK_II.sensory.SensorCell;
import model.MARK_II.region.Cell;

import java.awt.*;

/**
 * @author Nathan Waggoner(nwagg14@vt.edu)
 * @author Quinn Liu(quinnliu@vt.edu)
 * @version 11/5/14
 */
public class SensorCellsToRegionRectangleConnect extends
            AbstractSensorCellsToRegionConnect {
    @Override
    public void connect(SensorCell[][] sensorCells, Column[][] regionColumns,
                        int numberOfColumnsToOverlapAlongXAxisOfSensorCells,
                        int numberOfColumnsToOverlapAlongYAxisOfSensorCells) {

        int rowBinitial, rowBfinal, colBinitial, colBfinal;

        int topRowLength = regionColumns.length;
        int topColLength = regionColumns[0].length;
        int botRowLength = sensorCells.length;
        int botColLength = sensorCells[0].length;

        RectangleConnect rectangleConnect = new RectangleConnect();

        for(int rowT = 0; rowT < topRowLength; rowT++){
            Point rowReceptiveField = rectangleConnect.updateReceptiveFieldDimensionLengthWithOverlap(topRowLength, botRowLength, rowT, numberOfColumnsToOverlapAlongYAxisOfSensorCells);
            rowBinitial = (int) rowReceptiveField.getX();
            rowBfinal = (int) rowReceptiveField.getY();

            for(int colT = 0; colT < topColLength; colT++) {
                Point colReceptiveField = rectangleConnect.updateReceptiveFieldDimensionLengthWithOverlap(topColLength, botColLength, colT, numberOfColumnsToOverlapAlongXAxisOfSensorCells);
                colBinitial = (int) colReceptiveField.getX();
                colBfinal = (int) colReceptiveField.getY();

                // actually add synapses from bottom layer receptive field to top layer column
                Column topColumn = regionColumns[rowT][colT];
                for (int rowB = rowBinitial; rowB <= rowBfinal; rowB++){
                    for (int colB = colBinitial; colB <= colBfinal; colB++) {
                        topColumn.getProximalSegment().addSynapse(new Synapse<Cell>(sensorCells[rowB][colB], rowB, colB));
                    }
                }
            }
        }
    }
}