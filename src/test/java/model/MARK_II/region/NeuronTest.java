package model.MARK_II.region;

import model.MARK_II.generalAlgorithm.AlgorithmStatistics;
import model.MARK_II.sensory.VisionCell;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 26, 2014
 */
public class NeuronTest extends junit.framework.TestCase {
    private Neuron neuron;

    private DistalSegment distalSegmentWith1ActiveSynapse;
    private DistalSegment distalSegmentWith2ActiveSynapses;
    private DistalSegment distalSegmentWith3ActiveSynapses;
    private DistalSegment distalSegmentWith4ActiveSynapses;

    private DistalSegment currentlyActiveDistalSegment;
    private DistalSegment previousActiveDistalSegment;

    public void setUp() {
        this.neuron = new Neuron();

        this.setUpDistalSegments();

        this.currentlyActiveDistalSegment = new DistalSegment();
        this.previousActiveDistalSegment = new DistalSegment();
        this.previousActiveDistalSegment.setPreviousActiveState(true);
    }

    public void test_nextTimeStep() {
        this.neuron.setActiveState(true);
        this.neuron.setPredictingState(true);

        assertFalse(this.neuron.getPreviousActiveState());
        assertFalse(this.neuron.getPreviousPredictingState());

        this.neuron.nextTimeStep();

        assertTrue(this.neuron.getPreviousActiveState());
        assertTrue(this.neuron.getPreviousPredictingState());

        assertFalse(this.neuron.getActiveState());
        assertFalse(this.neuron.getPredictingState());
    }

    public void test_getBestPreviousActiveSegment() {
        // previousActiveSegments size = 0 & sequenceSegments size = 0
        assertNotNull(this.neuron.getBestPreviousActiveSegment(new AlgorithmStatistics()));
        assertEquals(1, this.neuron.getDistalSegments().size());

        // previousActiveSegments size = 0 & sequenceSegments size = 0
        this.neuron.addDistalSegment(this.distalSegmentWith1ActiveSynapse);
        this.neuron.addDistalSegment(this.distalSegmentWith2ActiveSynapses);
        assertNotNull(this.neuron.getBestPreviousActiveSegment(new AlgorithmStatistics()));
        assertEquals(4, this.neuron.getDistalSegments().size());

        // previousActiveSegments size = 1 & sequenceSegments size = 0
        this.neuron.addDistalSegment(this.previousActiveDistalSegment);
        assertEquals(this.previousActiveDistalSegment,
                this.neuron.getBestPreviousActiveSegment(new AlgorithmStatistics()));

        // previousActiveSegments size = 2 & sequenceSegments size = 0
        VisionCell activeVisionCell_1 = new VisionCell();
        activeVisionCell_1.setPreviousActiveState(true);
        Synapse<Cell> previousActiveSynapse_1 = new Synapse<Cell>(
                activeVisionCell_1, 0.3, 0, 0);

        DistalSegment distalSegment_3 = new DistalSegment();
        distalSegment_3.addSynapse(previousActiveSynapse_1);
        distalSegment_3.setPreviousActiveState(true);
        this.neuron.addDistalSegment(distalSegment_3);
        assertEquals(distalSegment_3,
                this.neuron.getBestPreviousActiveSegment(new AlgorithmStatistics()));

        // previousActiveSegments size = 3 & sequenceSegments size = 1
        DistalSegment distalSegment_4 = new DistalSegment();
        distalSegment_4.setPreviousActiveState(true);
        distalSegment_4.setSequenceState(true);
        this.neuron.addDistalSegment(distalSegment_4);
        assertEquals(distalSegment_4,
                this.neuron.getBestPreviousActiveSegment(new AlgorithmStatistics()));

        // previousActiveSegments size = 3 & sequenceSegments size = 2
        DistalSegment distalSegment_5 = new DistalSegment();
        distalSegment_5.addSynapse(previousActiveSynapse_1);
        distalSegment_5.setPreviousActiveState(true);
        distalSegment_5.setSequenceState(true);
        this.neuron.addDistalSegment(distalSegment_5);
        assertEquals(distalSegment_5,
                this.neuron.getBestPreviousActiveSegment(new AlgorithmStatistics()));
    }

    public void test_getBestActiveSegment() {
        this.neuron.addDistalSegment(this.distalSegmentWith1ActiveSynapse);
        this.neuron.addDistalSegment(this.distalSegmentWith2ActiveSynapses);
        this.neuron.addDistalSegment(this.distalSegmentWith3ActiveSynapses);
        this.neuron.addDistalSegment(this.distalSegmentWith4ActiveSynapses);

        assertEquals(1,
                this.distalSegmentWith1ActiveSynapse
                        .getNumberOfActiveSynapses());
        assertEquals(2,
                this.distalSegmentWith2ActiveSynapses
                        .getNumberOfActiveSynapses());
        assertEquals(3,
                this.distalSegmentWith3ActiveSynapses
                        .getNumberOfActiveSynapses());
        assertEquals(4,
                this.distalSegmentWith4ActiveSynapses
                        .getNumberOfActiveSynapses());

        assertEquals(this.distalSegmentWith4ActiveSynapses,
                this.neuron.getBestActiveSegment(null));
    }

    void setUpDistalSegments() {
        this.distalSegmentWith1ActiveSynapse = new DistalSegment();
        this.distalSegmentWith2ActiveSynapses = new DistalSegment();
        this.distalSegmentWith3ActiveSynapses = new DistalSegment();
        this.distalSegmentWith4ActiveSynapses = new DistalSegment();

        // 4 currently active Synapse
        VisionCell activeVisionCell_1 = new VisionCell();
        activeVisionCell_1.setActiveState(true);

        VisionCell activeVisionCell_2 = new VisionCell();
        activeVisionCell_2.setActiveState(true);

        VisionCell activeVisionCell_3 = new VisionCell();
        activeVisionCell_3.setActiveState(true);

        VisionCell activeVisionCell_4 = new VisionCell();
        activeVisionCell_4.setActiveState(true);

        this.distalSegmentWith1ActiveSynapse.addSynapse(new Synapse<Cell>(
                activeVisionCell_1, 0.2, 0, 0));

        this.distalSegmentWith2ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_1, 0.2, 0, 0));
        this.distalSegmentWith2ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_2, 0.2, 0, 1));

        this.distalSegmentWith3ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_1, 0.2, 0, 0));
        this.distalSegmentWith3ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_2, 0.2, 0, 1));
        this.distalSegmentWith3ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_3, 0.2, 0, 2));

        this.distalSegmentWith4ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_1, 0.2, 0, 0));
        this.distalSegmentWith4ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_2, 0.2, 0, 1));
        this.distalSegmentWith4ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_3, 0.2, 0, 2));
        this.distalSegmentWith4ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_4, 0.2, 0, 3));
    }
}
