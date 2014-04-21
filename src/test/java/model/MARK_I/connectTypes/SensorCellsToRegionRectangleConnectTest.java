package test.java.model.MARK_I.connectTypes;

import main.java.model.MARK_I.Column;
import main.java.model.MARK_I.VisionCell;
import main.java.model.MARK_I.SensorCell;
import main.java.model.MARK_I.Region;
import main.java.model.MARK_I.connectTypes.SensorCellsToRegionRectangleConnect;
import main.java.model.MARK_I.connectTypes.AbstractSensorCellsToRegionConnect;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 13, 2013
 */
public class SensorCellsToRegionRectangleConnectTest extends
	junit.framework.TestCase {

    private AbstractSensorCellsToRegionConnect connectType;

    public void setUp() {
	this.connectType = new SensorCellsToRegionRectangleConnect();
    }

    public void test_connect() {
	Region leafRegion = new Region("leafRegion", 8, 8, 4, 20, 3);
	SensorCell[][] sensorCells = new VisionCell[66][66];
	for (int x = 0; x < sensorCells.length; x++) {
	    for (int y = 0; y < sensorCells[0].length; y++) {
		sensorCells[x][y] = new VisionCell();
	    }
	}

	this.connectType.connect(sensorCells, leafRegion, 2, 2);

	Column[][] columns = leafRegion.getColumns();
	for (int parentColumnX = 0; parentColumnX < leafRegion.getXAxisLength(); parentColumnX++) {
	    for (int parentColumnY = 0; parentColumnY < leafRegion
		    .getYAxisLength(); parentColumnY++) {
		assertEquals(100, columns[parentColumnX][parentColumnY]
			.getProximalSegment().getSynapses().size());
	    }
	}
    }
}