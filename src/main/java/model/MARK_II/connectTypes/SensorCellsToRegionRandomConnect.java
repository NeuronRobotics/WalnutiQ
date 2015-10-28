package model.MARK_II.connectTypes;

import model.MARK_II.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version July 18, 2013
 */
public class SensorCellsToRegionRandomConnect extends
        AbstractSensorCellsToRegionConnect {

    /**
     * In order to explain overlapping Synapses more concisely I will be using
     * the abbreviated variable names in the below formula:
     * numberOfColumnsToOverlapAlongXAxisOfSensorCells = C_X
     * numberOfColumnsToOverlapAlongYAxisOfSensorCells = C_Y sensorCells.length
     * = SC_X sensorCells[0].length = SC_Y regionXAxisLength = R_X
     * regionYAxisLength = R_Y
     *  
     * [(C_X + SC_X) * (C_Y + SC_Y)] / (R_X * R_Y) = # of Synapses created to
     * connect a SensorCellLayer to a leaf Region Column.
     */
    @Override
    public void connect(SensorCell[][] sensorCells, Column[][] regionColumns,
                        int numberOfSynapsesToOverlapAlongXAxisOfSensorCells,
                        int numberOfSynapsesToOverlapAlongYAxisOfSensorCells) {

        super.checkParameters(sensorCells, regionColumns,
                numberOfSynapsesToOverlapAlongXAxisOfSensorCells,
                numberOfSynapsesToOverlapAlongYAxisOfSensorCells);

        int regionRowLength = regionColumns.length; // 8
        int regionColumnLength = regionColumns[0].length; // 8

        int sensorCellsRowLength = sensorCells.length; // 66
        int sensorCellsColumnLength = sensorCells[0].length; // 66

        List<Point> allSynapsePositions = new ArrayList<Point>(
                sensorCellsRowLength * sensorCellsColumnLength);
        // generate all possible Synapse positions
        for (int row = 0; row < sensorCellsRowLength; row++) {
            for (int column = 0; column < sensorCellsColumnLength; column++) {
                allSynapsePositions.add(new Point(row, column));
            }
        }
        Collections.shuffle(allSynapsePositions);

        // ((2+66) * (2+66))/(8*8) = 68*68/64 = 72.25 > 66
        int numberOfSynapsesToAddToColumn = ((numberOfSynapsesToOverlapAlongXAxisOfSensorCells + sensorCellsRowLength) * (numberOfSynapsesToOverlapAlongYAxisOfSensorCells + sensorCellsColumnLength))
                / (regionRowLength * regionColumnLength);

        for (int columnRowPosition = 0; columnRowPosition < regionRowLength; columnRowPosition++) {
            for (int columnColumnPosition = 0; columnColumnPosition < regionColumnLength; columnColumnPosition++) {
                Column column = regionColumns[columnRowPosition][columnColumnPosition];

                // add first numberOfSynapsesToAddToColumn to this Column
                for (int i = 0; i < numberOfSynapsesToAddToColumn; i++) {
                    int randomSynapseXPosition = allSynapsePositions.get(i).x;
                    int randomSynapseYPosition = allSynapsePositions.get(i).y;

                    column.getProximalSegment()
                            .addSynapse(
                                    new Synapse<Cell>(
                                            sensorCells[randomSynapseXPosition][randomSynapseYPosition],
                                            randomSynapseXPosition,
                                            randomSynapseYPosition));
                }
            }
        }
    }
}
