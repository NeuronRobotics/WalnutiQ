package model.MARK_II.region;

import model.MARK_II.sensory.VisionCell;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 26, 2014
 */
public class DistalSegmentTest extends junit.framework.TestCase {
    private DistalSegment distalSegment;

    public void setUp() {
        this.distalSegment = new DistalSegment();
    }

    public void test_nextTimeStep() {
        // add 1 active Synapse to make distalSegment currently active
        VisionCell activeVisionCell_1 = new VisionCell();
        activeVisionCell_1.setActiveState(true);
        this.distalSegment.addSynapse(new Synapse<Cell>(activeVisionCell_1,
                0.2, 0, 0));

        this.distalSegment.setSequenceState(true);

        assertTrue(this.distalSegment.getActiveState());
        assertFalse(this.distalSegment.getPreviousActiveState());
        assertTrue(this.distalSegment
                .getSequenceStatePredictsFeedFowardInputOnNextStep());

        this.distalSegment.nextTimeStep();
        // retina or temporal pooling algorithm will cause distalSegment
        // current active state to change

        assertTrue(this.distalSegment.getPreviousActiveState());
        assertFalse(this.distalSegment
                .getSequenceStatePredictsFeedFowardInputOnNextStep());
    }

    public void test_getNumberOfPreviousActiveSynapses() {
        // add 1 currently active Synapse
        VisionCell activeVisionCell_1 = new VisionCell();
        activeVisionCell_1.setActiveState(true);
        this.distalSegment.addSynapse(new Synapse<Cell>(activeVisionCell_1,
                0.2, 0, 0));

        // add 1 previously active Synapse
        VisionCell activeVisionCell_2 = new VisionCell();
        activeVisionCell_2.setPreviousActiveState(true);
        this.distalSegment.addSynapse(new Synapse<Cell>(activeVisionCell_2,
                0.2, 0, 1));

        assertEquals(1, this.distalSegment.getNumberOfPreviousActiveSynapses());
    }
}
