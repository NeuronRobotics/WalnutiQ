package model.MARK_II.region;

import junit.framework.TestCase;
import model.MARK_II.sensory.VisionCell;

/**
 * SpatialPooler ProximalSegments
 * 1) Segments should add new Synapses only during instantiation
 * 2) Segments can:
 * - increase permanences of all Synapses on itself
 * - decrease permanences of all Synapses on itself
 * - increase permanences of all active Synapses on itself
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version July 22, 2013
 */
public class SegmentTest extends TestCase {
    private Segment proximalSegment;

    public void setUp() {
        this.proximalSegment = new ProximalSegment();
    }

    public void test_getActiveState() {
        // a Segment with 10 Synapses will have an activation threshold of 2
        // Synapses
        // if the PERCENT_ACTIVE_SYNAPSES_THRESHOLD is 0.2

        // add 1 active Synapse
        VisionCell activeVisionCell_1 = new VisionCell();
        activeVisionCell_1.setActiveState(true);
        this.proximalSegment.addSynapse(new Synapse<Cell>(activeVisionCell_1,
                0.2, 0, 0));

        // add 9 inactive Synapses
        VisionCell inActiveVisionCell = null;
        for (int i = 1; i < 10; i++) {
            inActiveVisionCell = new VisionCell();
            this.proximalSegment.addSynapse(new Synapse<Cell>(
                    inActiveVisionCell, 0.2, i, 0));
        }
        assertEquals(10, this.proximalSegment.getSynapses().size());

        // Case 1: number of active Synapses within Segment < activation
        // threshold
        assertFalse(this.proximalSegment.getActiveState());

        // Case 2: number of active Synapses within Segment = activation
        // threshold
        Synapse<Cell> synapseToRemove_1 = new Synapse<Cell>(inActiveVisionCell,
                0.2, 1, 0);
        assertTrue(this.proximalSegment.removeSynapse(synapseToRemove_1));

        VisionCell activeVisionCell_2 = new VisionCell();
        activeVisionCell_2.setActiveState(true);
        this.proximalSegment.getSynapses().add(
                new Synapse<Cell>(activeVisionCell_2, 0.2, 10, 0));
        assertFalse(this.proximalSegment.getActiveState());

        // Case 3: number of active Synapses within Segment > activation
        // threshold
        Synapse<Cell> synapseToRemove_2 = new Synapse<Cell>(inActiveVisionCell,
                0.2, 2, 0);
        assertTrue(this.proximalSegment.removeSynapse(synapseToRemove_2));
        VisionCell activeVisionCell_3 = new VisionCell();
        activeVisionCell_3.setActiveState(true);
        this.proximalSegment.getSynapses().add(
                new Synapse<Cell>(activeVisionCell_3, 0.2, 11, 0));
        assertTrue(this.proximalSegment.getActiveState());
    }

    public void test_updateSynapsePermanences() {
        // add 1 active Synapse
        VisionCell activeVisionCell = new VisionCell();
        activeVisionCell.setActiveState(true);
        this.proximalSegment.addSynapse(new Synapse<Cell>(activeVisionCell,
                0.2, 0, 1));

        // add 1 inactive Synapse
        VisionCell inActiveVisionCell = new VisionCell();
        this.proximalSegment.addSynapse(new Synapse<Cell>(inActiveVisionCell,
                0.2, 1, 1));

        this.proximalSegment
                .updateSynapsePermanences(Segment.SynapseUpdateState.INCREASE_ACTIVE);
        assertEquals(0.2 + Synapse.PERMANENCE_INCREASE, this.proximalSegment
                .getSynapse(0, 1).getPermanenceValue(), 0.0001);
        assertEquals(0.2, this.proximalSegment.getSynapse(1, 1)
                .getPermanenceValue(), 0.0001);

        this.proximalSegment
                .updateSynapsePermanences(Segment.SynapseUpdateState.INCREASE_ALL);
        assertEquals(0.2 + 2 * Synapse.PERMANENCE_INCREASE,
                this.proximalSegment.getSynapse(0, 1).getPermanenceValue(),
                0.0001);
        assertEquals(0.2 + Synapse.PERMANENCE_INCREASE, this.proximalSegment
                .getSynapse(1, 1).getPermanenceValue(), 0.0001);

        this.proximalSegment
                .updateSynapsePermanences(Segment.SynapseUpdateState.DECREASE_ALL);
        assertEquals(0.2 + 2 * Synapse.PERMANENCE_INCREASE
                        - Synapse.PERMANENCE_DECREASE,
                this.proximalSegment.getSynapse(0, 1).getPermanenceValue(),
                0.0001);
        assertEquals(0.2 + Synapse.PERMANENCE_INCREASE
                        - Synapse.PERMANENCE_DECREASE,
                this.proximalSegment.getSynapse(1, 1).getPermanenceValue(),
                0.0001);
    }

    public void test_addSynapse() {
        // make sure a Segment can hold 2 Synapses that
        // exactly the same
        VisionCell visionCell_11 = new VisionCell();
        Synapse synapse_1 = new Synapse<Cell>(visionCell_11, 1, 1);
        Synapse synapse_2 = new Synapse<Cell>(visionCell_11, 1, 1);

        this.proximalSegment.addSynapse(synapse_1);
        this.proximalSegment.addSynapse(synapse_2);
        assertEquals(1, this.proximalSegment.getSynapses().size());
    }

    public void test_getNumberOfPreviousActiveSynapses() {
        // case 1: a Cell was previously active and Synapse is now connected
        VisionCell visionCell_1 = new VisionCell();
        visionCell_1.setActiveState(true);
        visionCell_1.setPreviousActiveState(true);
        Synapse<Cell> synapse_1 = new Synapse<Cell>(visionCell_1, 0.3, 0, 0);

        // case 2: a Cell was previously active and Synapse is now NOT connected
        VisionCell visionCell_2 = new VisionCell();
        visionCell_2.setActiveState(true);
        visionCell_2.setPreviousActiveState(true);
        Synapse<Cell> synapse_2 = new Synapse<Cell>(visionCell_2, 0.1, 0, 1);

        // case 3: a Cell was previously NOT active and Synapse is now connected
        VisionCell visionCell_3 = new VisionCell();
        visionCell_3.setActiveState(true);
        visionCell_3.setPreviousActiveState(false);
        Synapse<Cell> synapse_3 = new Synapse<Cell>(visionCell_3, 0.3, 0, 2);

        // case 4: a Cell was previously NOT active and Synapse is now NOT
        // connected
        VisionCell visionCell_4 = new VisionCell();
        visionCell_4.setActiveState(true);
        visionCell_4.setPreviousActiveState(false);
        Synapse<Cell> synapse_4 = new Synapse<Cell>(visionCell_4, 0.1, 0, 2);
    }
}
