package model.MARK_II.connectTypes;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

import model.MARK_II.region.*;
import model.MARK_II.util.Point3D;

/**
 * @author Nathan Waggoner(nwagg14@vt.edu)
 * @author Quinn Liu(quinnliu@vt.edu)
 * @version 12/18/14
 */
public class RegionToRegionRectangleConnectTest extends TestCase{

    private RegionToRegionRectangleConnect connectType;

    private Region topRegion;
    private Region bottomRegion;
    private static final int bottomRegionColumnLength = 2;

    Set<Point3D> expectedPositionsForColumnAt00;
    Set<Point3D> expectedPositionsForColumnAt01;
    Set<Point3D> expectedPositionsForColumnAt10;
    Set<Point3D> expectedPositionsForColumnAt11;

    public void setUp(){
        this.connectType = new RegionToRegionRectangleConnect();

        this.topRegion = new Region("topRegion", 2, 2, 1, 50, 2);
        this.bottomRegion = new Region("bottomRegion", 4, 8, bottomRegionColumnLength, 50, 2);

        this.setUpExpectedPositionsForColumnsWithOverlap();
    }

    public void test_connect() {
        AbstractRegionToRegionConnect regionToRegion = new RegionToRegionRectangleConnect();
        regionToRegion.connect(this.bottomRegion.getColumns(), this.topRegion.getColumns(), 1, 2);
        int topRowLength = topRegion.getNumberOfRowsAlongRegionYAxis();
        int topColLength = topRegion.getNumberOfColumnsAlongRegionXAxis();

        Set<Point3D> actualConnectedNeuronPositions = new HashSet<Point3D>();
        for(int rowT = 0; rowT < topRowLength; rowT++) {
            for (int colT = 0; colT < topColLength; colT++) {
                Segment proximalSegment = topRegion.getColumn(rowT, colT).getProximalSegment();
                //System.out.println("Column at (row, column) = (" + rowT + ", " + colT + ")");
                for(Synapse<Cell> synapse : proximalSegment.getSynapses()) {
                    for (int i = 0; i < bottomRegionColumnLength; i++) {
                        Point3D neuronPosition = new Point3D(synapse.getCellColumn(), synapse.getCellRow(), i);
                        actualConnectedNeuronPositions.add(neuronPosition);
                        //System.out.println("added neuronPosition (r, c, i) = ("
                        //        + synapse.getCellColumn() + ", " + synapse.getCellRow() + ", " + i + ")");
                    }
                }

                if (rowT == 0 && colT == 0) {
                    assertEquals(this.expectedPositionsForColumnAt00, actualConnectedNeuronPositions);
                } else if (rowT == 0 && colT == 1) {
                    assertEquals(this.expectedPositionsForColumnAt01, actualConnectedNeuronPositions);
                } else if (rowT == 1 && colT == 0) {
                    assertEquals(this.expectedPositionsForColumnAt10, actualConnectedNeuronPositions);
                } else { // rowT == 1 && colT == 1
                    assertEquals(this.expectedPositionsForColumnAt11, actualConnectedNeuronPositions);
                }
                actualConnectedNeuronPositions.clear();
            }
        }
    }

    private void setUpExpectedPositionsForColumnsWithOverlap() {
        expectedPositionsForColumnAt00 = new HashSet<Point3D>();
        for(int r = 0; r < 2 + 1; r++) {
            for(int c = 0; c < 4 + 2; c++) {
                for (int z = 0; z < bottomRegionColumnLength; z++) {
                    expectedPositionsForColumnAt00.add(new Point3D(r, c, z));
                }
            }
        }

        expectedPositionsForColumnAt01 = new HashSet<Point3D>();
        for(int r = 0; r < 2 + 1; r++) {
            for(int c = 4 - 2; c < 8; c++) {
                for (int z = 0; z < bottomRegionColumnLength; z++) {
                    expectedPositionsForColumnAt01.add(new Point3D(r, c, z));
                }
            }
        }

        expectedPositionsForColumnAt10 = new HashSet<Point3D>();
        for(int r = 2 - 1; r < 4; r++) {
            for(int c = 0; c < 4 + 2; c++) {
                for (int z = 0; z < bottomRegionColumnLength; z++) {
                    expectedPositionsForColumnAt10.add(new Point3D(r, c, z));
                }
            }
        }

        expectedPositionsForColumnAt11 = new HashSet<Point3D>();
        for(int r = 2 - 1; r < 4; r++) {
            for(int c = 4 - 2; c < 8; c++) {
                for (int z = 0; z < bottomRegionColumnLength; z++) {
                    expectedPositionsForColumnAt11.add(new Point3D(r, c, z));
                }
            }
        }
    }

    public void test_ConnectWithNoOverlap() {
        Region parentRegion = new Region("parentRegion", 8, 8, 4, 20, 3);
        Region childRegion = new Region("childRegion", 64, 64, 4, 20, 3);

        AbstractRegionToRegionConnect regionToRegion = new RegionToRegionRectangleConnect();
        regionToRegion.connect(childRegion.getColumns(), parentRegion.getColumns(), 0, 0);

        Column[][] columns = parentRegion.getColumns();
        for (int parentRegionRow = 0; parentRegionRow < parentRegion
                .getNumberOfRowsAlongRegionYAxis(); parentRegionRow++) {
            for (int parentRegionColumn = 0; parentRegionColumn < parentRegion
                    .getNumberOfColumnsAlongRegionXAxis(); parentRegionColumn++) {
                assertEquals(64, columns[parentRegionRow][parentRegionColumn]
                        .getProximalSegment().getSynapses().size());
            }
        }
    }

    public void test_ConnectWithLittleOverlap() {
        Region parentRegion = new Region("parentRegion", 8, 8, 4, 20, 3);
        Region childRegion = new Region("childRegion", 64, 64, 4, 20, 3);

        AbstractRegionToRegionConnect regionToRegion = new RegionToRegionRectangleConnect();
        regionToRegion.connect(childRegion.getColumns(), parentRegion.getColumns(), 2, 2);

        int numberOfColumnsWith100Synapses = 0;
        int numberOfColumnsWith120Synapses = 0;
        int numberOfColumnsWith144Synapses = 0;

        Column[][] columns = parentRegion.getColumns();
        for (int parentRegionRow = 0; parentRegionRow < parentRegion
                .getNumberOfRowsAlongRegionYAxis(); parentRegionRow++) {
            for (int parentRegionColumn = 0; parentRegionColumn < parentRegion
                    .getNumberOfColumnsAlongRegionXAxis(); parentRegionColumn++) {
                int numberOfSynapses = columns[parentRegionRow][parentRegionColumn]
                        .getProximalSegment().getSynapses().size();

                if (numberOfSynapses == 100) {
                    numberOfColumnsWith100Synapses++;
                } else if (numberOfSynapses == 120) {
                    numberOfColumnsWith120Synapses++;
                } else if (numberOfSynapses == 144) {
                    numberOfColumnsWith144Synapses++;
                }
            }
        }

        assertEquals(4, numberOfColumnsWith100Synapses); // 4 corners with no overlap
        assertEquals(24, numberOfColumnsWith120Synapses); // 6 on each of 4 sides = 24
        assertEquals(36, numberOfColumnsWith144Synapses); // inner 6 by 6 columns = 36
        // counted every column
        assertEquals(64, numberOfColumnsWith100Synapses +
                numberOfColumnsWith120Synapses + numberOfColumnsWith144Synapses);
    }
}
