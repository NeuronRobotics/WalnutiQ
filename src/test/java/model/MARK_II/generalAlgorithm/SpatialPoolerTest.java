package model.MARK_II.generalAlgorithm;

import junit.framework.TestCase;
import model.MARK_II.connectTypes.AbstractRegionToRegionConnect;
import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.MARK_II.connectTypes.RegionToRegionRectangleConnect;
import model.MARK_II.connectTypes.SensorCellsToRegionRectangleConnect;
import model.MARK_II.util.RegionConsoleViewer;
import model.MARK_II.sensory.Retina;
import model.MARK_II.region.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version July 29, 2013
 */
public class SpatialPoolerTest extends TestCase {

    private Region parentRegion;
    private SpatialPooler spatialPooler;

    public void setUp() {
        // NOTE: when first testing this class I used preset values for MARK I
        //       model without realizing I would want to change them in the
        //       future. The following are the preset values:
        Synapse.INITIAL_PERMANENCE = 0.3;
        Synapse.MINIMAL_CONNECTED_PERMANENCE = 0.2;
        Synapse.PERMANENCE_INCREASE = 0.02;
        Synapse.PERMANENCE_DECREASE = 0.005;

        this.parentRegion = new Region("parentRegion", 8, 8, 4, 20, 3);
        Region childRegion = new Region("childRegion", 66, 66, 4, 20, 3);
        AbstractRegionToRegionConnect connectType = new RegionToRegionRectangleConnect();
        connectType.connect(childRegion.getColumns(), this.parentRegion.getColumns(), 2, 2);

        this.spatialPooler = new SpatialPooler(this.parentRegion);
        this.spatialPooler.setLearningState(true);
    }

    public void test_performSpatialPoolingOnRegion() throws IOException {
        Region region = new Region("region", 8, 8, 4, 50, 1);
        this.spatialPooler.changeRegion(region);

        Retina retina = new Retina(66, 66);

        AbstractSensorCellsToRegionConnect connectType2 = new SensorCellsToRegionRectangleConnect();
        connectType2.connect(retina.getVisionCells(), region.getColumns(), 2, 2);

        retina.seeBMPImage("2.bmp");
        this.spatialPooler.performPooling();
        Set<ColumnPosition> columnActivityAfterSeeingImage2 = this.spatialPooler
                .getActiveColumnPositions();

        assertEquals(4, columnActivityAfterSeeingImage2.size());
    }

    public void test_computeColumnOverlapScore() {
        // when # of active Synapses on Column
        // proximal Segment < regionMinimumOverlapScore
        // Column new overlapScore = 0
        Column[][] columns = this.parentRegion.getColumns();
        assertEquals(0, columns[0][0].getProximalSegment()
                .getNumberOfActiveSynapses());
        assertEquals(121, columns[0][0].getProximalSegment().getSynapses()
                .size());

        columns[0][0].setOverlapScore(69);
        assertEquals(69, columns[0][0].getOverlapScore());

        this.spatialPooler.computeColumnOverlapScore(columns[0][0]);
        assertEquals(0, columns[0][0].getOverlapScore());

        // when # of active Synapses on Column
        // proximal Segment > regionMinimumOverlapScore
        // Column new overlapScore = # of active Synapses * Column boostValue

        // the current regionMinimumOverlapScore is 24 active Synapses out of
        // 100 total Synapses

        // to test if method is working correctly add 24 synapses
        Set<Synapse<Cell>> oneHundredInactiveSynapses = columns[0][0]
                .getProximalSegment().getSynapses();
        int i = 0;
        for (Synapse inactiveSynapse : oneHundredInactiveSynapses) {
            if (i < 24) {
                Cell cell = (Cell) inactiveSynapse.getConnectedCell();
                cell.setActiveState(true);
                i++;
            } else {
                break;
            }
        }
        // now the overlapScore will be 24
        columns[0][0].setBoostValue(1.5f); // now since the boostValue is 1.5

        // the new overlapScore will be 24 * 1.5 ~= 36
        this.spatialPooler.computeColumnOverlapScore(columns[0][0]);

        assertEquals(36, columns[0][0].getOverlapScore());
    }

    public void test_computeActiveColumnsOfRegion() {
        Column[][] columns = this.parentRegion.getColumns();
        columns[0][0].setOverlapScore(10);
        columns[0][1].setOverlapScore(11);
        columns[0][2].setOverlapScore(12);
        columns[0][3].setOverlapScore(13);
        columns[0][4].setOverlapScore(14);
        columns[1][0].setOverlapScore(15);
        columns[1][1].setOverlapScore(2);
        columns[1][2].setOverlapScore(3);
        columns[1][3].setOverlapScore(4);
        columns[1][4].setOverlapScore(16);
        columns[2][0].setOverlapScore(17);
        columns[2][1].setOverlapScore(5);
        columns[2][2].setOverlapScore(1);
        columns[2][3].setOverlapScore(6);
        columns[2][4].setOverlapScore(18);
        columns[3][0].setOverlapScore(19);
        columns[3][1].setOverlapScore(7);
        columns[3][2].setOverlapScore(8);
        columns[3][3].setOverlapScore(9);
        columns[3][4].setOverlapScore(20);
        columns[4][0].setOverlapScore(21);
        columns[4][1].setOverlapScore(22);
        columns[4][2].setOverlapScore(23);
        columns[4][3].setOverlapScore(24);
        columns[4][4].setOverlapScore(25);
        // all other Column overlapScores are 0

        this.parentRegion.setInhibitionRadius(2);

        this.spatialPooler.computeActiveColumnsOfRegion();

        char[][] columnActiveStates = RegionConsoleViewer
                .getColumnActiveStatesCharArray(this.parentRegion);

        // Question: how can 5 columns along row 5 all be active next to each
        // other?

        // Answer: the desired local activity is applied to each column thus you
        // can have more active columns within inhibitionRadius(2) than
        // desiredLocalActivity(3)
        assertEquals("iiiiaiii\naiiiaiii\niiiiiiii\niiiiiiii\naaaaaiii\n"
                        + "iiiiiiii\niiiiiiii\niiiiiiii",
                RegionConsoleViewer.doubleCharArrayAsString(columnActiveStates));
    }

    public void test_regionLearnOneTimeStep() {
        assertEquals(1, this.parentRegion.getInhibitionRadius());
        this.spatialPooler.regionLearnOneTimeStep();
        assertEquals(4, this.parentRegion.getInhibitionRadius());
    }

    public void test_modelLongTermPotentiationAndDepression() {
        Column[][] columns = this.parentRegion.getColumns();
        Segment proximalSegment_00 = columns[0][0].getProximalSegment();
        Synapse<Cell> synapse_00 = proximalSegment_00.getSynapse(0, 0);

        // An "and" truth table of column.updatePermanences()
        // X-Axis: Column activeState
        // Y-Axis: Synapse/InputCell activeState
        // -----true false
        // true .32 .295
        // false.300 .300
        columns[0][0].setActiveState(true);
        Cell cell_00 = (Cell) synapse_00.getConnectedCell();
        cell_00.setActiveState(true);
        assertEquals(0.3f, synapse_00.getPermanenceValue(), 0.001);
        this.spatialPooler.modelLongTermPotentiationAndDepression();

        assertEquals(0.32f, synapse_00.getPermanenceValue(), 0.001);

        // now the Synapse permanenceValue is decreased
        columns[0][0].setActiveState(true);
        cell_00.setActiveState(false);
        assertEquals(0.32f, synapse_00.getPermanenceValue(), 0.001);
        this.spatialPooler.modelLongTermPotentiationAndDepression();
        assertEquals(0.315f, synapse_00.getPermanenceValue(), 0.001);

        // when Column activeState is false a Synapse's permanenceValue does
        // not change whether the Synapse is active itself or not
        columns[0][0].setActiveState(false);
        cell_00.setActiveState(true);
        assertEquals(0.315f, synapse_00.getPermanenceValue(), 0.001);
        this.spatialPooler.modelLongTermPotentiationAndDepression();
        assertEquals(0.315f, synapse_00.getPermanenceValue(), 0.001);

        columns[0][0].setActiveState(false);
        cell_00.setActiveState(false);
        assertEquals(0.315f, synapse_00.getPermanenceValue(), 0.001);
        this.spatialPooler.modelLongTermPotentiationAndDepression();
        assertEquals(0.315f, synapse_00.getPermanenceValue(), 0.001);
    }

    public void test_updateNeighborColumns() {
        // test on Column at position (0, 0) of Region
        this.parentRegion.setInhibitionRadius(0);
        this.spatialPooler.updateNeighborColumns(0, 0);
        Column[][] columns = this.parentRegion.getColumns();
        List<ColumnPosition> neighborColumns1 = columns[0][0]
                .getNeighborColumns();
        assertEquals(0, neighborColumns1.size());

        this.parentRegion.setInhibitionRadius(1);
        this.spatialPooler.updateNeighborColumns(0, 0);
        List<ColumnPosition> neighborColumns2 = columns[0][0]
                .getNeighborColumns();
        assertEquals(3, neighborColumns2.size());

        this.parentRegion.setInhibitionRadius(2);
        this.spatialPooler.updateNeighborColumns(0, 0);
        List<ColumnPosition> neighborColumns3 = columns[0][0]
                .getNeighborColumns();
        assertEquals(8, neighborColumns3.size());

        this.parentRegion.setInhibitionRadius(3);
        this.spatialPooler.updateNeighborColumns(0, 0);
        List<ColumnPosition> neighborColumns4 = columns[0][0]
                .getNeighborColumns();
        assertEquals(15, neighborColumns4.size());

        // test on Column at position (3, 3) of Region
        this.parentRegion.setInhibitionRadius(0);
        this.spatialPooler.updateNeighborColumns(3, 3);
        List<ColumnPosition> neighborColumns5 = columns[3][3]
                .getNeighborColumns();
        assertEquals(0, neighborColumns5.size());

        this.parentRegion.setInhibitionRadius(1);
        this.spatialPooler.updateNeighborColumns(3, 3);
        List<ColumnPosition> neighborColumns6 = columns[3][3]
                .getNeighborColumns();
        assertEquals(8, neighborColumns6.size());

        this.parentRegion.setInhibitionRadius(2);
        this.spatialPooler.updateNeighborColumns(3, 3);
        List<ColumnPosition> neighborColumns7 = columns[3][3]
                .getNeighborColumns();
        assertEquals(24, neighborColumns7.size());

        this.parentRegion.setInhibitionRadius(3);
        this.spatialPooler.updateNeighborColumns(3, 3);
        List<ColumnPosition> neighborColumns8 = columns[3][3]
                .getNeighborColumns();
        assertEquals(48, neighborColumns8.size());
    }

    public void test_kthScoreOfColumns() {
        // change overlapScores of columns for testing
        // the correct returning kth score
        Column[][] columns = this.parentRegion.getColumns();
        columns[1][3].setOverlapScore(24);
        columns[2][3].setOverlapScore(1);
        columns[2][5].setOverlapScore(26);
        columns[3][1].setOverlapScore(20);
        columns[3][4].setOverlapScore(5);
        columns[3][6].setOverlapScore(30);
        columns[4][3].setOverlapScore(10);

        List<Column> columnsList = new ArrayList<Column>();
        columnsList.add(columns[1][3]);
        columnsList.add(columns[2][3]);
        columnsList.add(columns[2][5]);
        columnsList.add(columns[3][1]);
        columnsList.add(columns[3][4]);
        columnsList.add(columns[3][6]);
        columnsList.add(columns[4][3]);

        assertEquals(30, this.spatialPooler.kthScoreOfColumns(columnsList, 1));
        assertEquals(26, this.spatialPooler.kthScoreOfColumns(columnsList, 2));
        assertEquals(24, this.spatialPooler.kthScoreOfColumns(columnsList, 3));
        assertEquals(20, this.spatialPooler.kthScoreOfColumns(columnsList, 4));
        assertEquals(10, this.spatialPooler.kthScoreOfColumns(columnsList, 5));
        assertEquals(5, this.spatialPooler.kthScoreOfColumns(columnsList, 6));
        assertEquals(1, this.spatialPooler.kthScoreOfColumns(columnsList, 7));
    }

    public void test_averageReceptiveFieldSizeOfRegion() {
        assertEquals(4.733,
                this.spatialPooler.averageReceptiveFieldSizeOfRegion(), 0.001);
        // Outputting a reasonable value.

        // Now really check by building a very small Region with one Column
        // with 1 connected Synapse & test. Then add another connected Synapse
        // & test.
        Region parentRegion2 = new Region("parentRegion2", 1, 1, 1, 50, 1);
        Region childRegion2 = new Region("childRegion2", 5, 5, 1, 50, 3);
        AbstractRegionToRegionConnect connectType = new RegionToRegionRectangleConnect();
        connectType.connect(childRegion2.getColumns(), parentRegion2.getColumns(), 0, 0);
        // parentRegion2 has 1 Column with 25 synapses.

        Column[][] columns = parentRegion2.getColumns();
        Set<Synapse<Cell>> synapses = columns[0][0].getProximalSegment()
                .getSynapses();
        assertEquals(25, synapses.size());

        // force all 25 synapses to no longer be connected
        for (Synapse<Cell> synapse : synapses) {
            synapse.setPermanenceValue(0.1);
            assertFalse(synapse.isConnected());
        }

        // force 1 specific synapse at (0, 0) to become connected
        Synapse<Cell> synapseAt00 = columns[0][0].getProximalSegment()
                .getSynapse(0, 0);
        synapseAt00.setPermanenceValue(0.3);
        assertTrue(synapseAt00.isConnected());

        this.spatialPooler = new SpatialPooler(parentRegion2);
        assertEquals(3.53,
                this.spatialPooler.averageReceptiveFieldSizeOfRegion(), 0.01);

        // force 1 specific synapse at (4, 4) to become connected
        Synapse<Cell> synapseAt44 = columns[0][0].getProximalSegment()
                .getSynapse(3, 3);
        synapseAt44.setPermanenceValue(0.3);
        assertTrue(synapseAt44.isConnected());

        // as expected since synapsAt33 is closer to where the column is at
        // around (2.5, 2.5) the average receptive field will become smaller
        assertEquals(2.12,
                this.spatialPooler.averageReceptiveFieldSizeOfRegion(), 0.01);
    }

    public void test_updateOverlapDutyCycle() {
        Column[][] columns = this.parentRegion.getColumns();

        this.spatialPooler.updateOverlapDutyCycle(0, 0);
        assertEquals(0.995f, columns[0][0].getOverlapDutyCycle(), 0.0001);

        columns[0][0].setOverlapDutyCycle(0.5f);
        this.spatialPooler.updateOverlapDutyCycle(0, 0);
        assertEquals(0.4975f, columns[0][0].getOverlapDutyCycle(), 0.0001);
    }
}