package model.MARK_II.connectTypes;

import junit.framework.TestCase;
import model.MARK_II.region.Region;
import model.MARK_II.region.Column;
import model.MARK_II.sensory.SensorCell;
import model.MARK_II.sensory.VisionCell;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version 12/18/2014
 */
public class SensorCellsToRegionRectangleConnectTest extends TestCase {

    private AbstractSensorCellsToRegionConnect connectType;

    public void setUp() {
        this.connectType = new SensorCellsToRegionRectangleConnect();
    }

    public void test_connect() {
        Region leafRegion = new Region("leafRegion", 8, 8, 4, 20, 3);
        SensorCell[][] sensorCells = new VisionCell[64][64];
        for (int row = 0; row < sensorCells.length; row++) {
            for (int column = 0; column < sensorCells[0].length; column++) {
                sensorCells[row][column] = new VisionCell();
            }
        }

        this.connectType.connect(sensorCells, leafRegion.getColumns(), 0, 0);

        Column[][] columns = leafRegion.getColumns();
        for (int parentColumnRowPosition = 0; parentColumnRowPosition < leafRegion.getNumberOfRowsAlongRegionYAxis(); parentColumnRowPosition++) {
            for (int parentColumnColumnPosition = 0; parentColumnColumnPosition < leafRegion
                    .getNumberOfColumnsAlongRegionXAxis(); parentColumnColumnPosition++) {
                assertEquals(64, columns[parentColumnRowPosition][parentColumnColumnPosition]
                        .getProximalSegment().getSynapses().size());
            }
        }
    }
}
