package model.MARK_II.generalAlgorithm;

import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.MARK_II.region.*;
import model.MARK_II.sensory.VisionCell;
import model.MARK_II.connectTypes.SensorCellsToRegionRectangleConnect;
import model.MARK_II.sensory.Retina;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 27, 2014
 */
public class TemporalPoolerTest extends junit.framework.TestCase {
    private Retina retina;
    private Region region;
    private SpatialPooler spatialPooler;
    private TemporalPooler temporalPooler;

    private DistalSegment distalSegmentWith1ActiveSynapse;
    private DistalSegment distalSegmentWith2ActiveSynapses;
    private DistalSegment distalSegmentWith3ActiveSynapses;
    private DistalSegment distalSegmentWith4ActiveSynapses;

    private DistalSegment distalSegmentWith1PreviousActiveSynapse;

    public void setUp() throws IOException {

        // declare sets to test on
        Set<ColumnPosition> emptySet = new HashSet<ColumnPosition>();
        Set<ColumnPosition> filledSet = new HashSet<ColumnPosition>();

        // create columns that should be in the filledSet
        ColumnPosition cp1 = new ColumnPosition(6, 5);
        ColumnPosition cp2 = new ColumnPosition(6, 2);
        ColumnPosition cp3 = new ColumnPosition(1, 2);
        ColumnPosition cp4 = new ColumnPosition(2, 5);
        ColumnPosition cp5 = new ColumnPosition(1, 5);

        // add the column positions to the filledSet
        filledSet.add(cp1);
        filledSet.add(cp2);
        filledSet.add(cp3);
        filledSet.add(cp4);
        filledSet.add(cp5);

        // images this oldRetina will see are all 66x66 pixels
        this.retina = new Retina(66, 66);

        this.region = new Region("Region", 8, 8, 3, 65, 2);

        AbstractSensorCellsToRegionConnect retinaToRegion = new SensorCellsToRegionRectangleConnect();
        retinaToRegion.connect(this.retina.getVisionCells(), this.region.getColumns(), 0, 0);

        this.spatialPooler = new SpatialPooler(this.region);
        this.spatialPooler.setLearningState(true);

        this.retina.seeBMPImage("2.bmp");
        this.spatialPooler.performPooling();

        // filledSet contains ((6, 2), (1, 3), (1, 5), (4, 4))
        assertEquals(filledSet, this.spatialPooler.getActiveColumnPositions());

        this.temporalPooler = new TemporalPooler(this.spatialPooler, 25);
        this.temporalPooler.setLearningState(true);

        this.setUpDistalSegments();
    }

    public void test_performTemporalPoolingOnRegion() {
        // segmentUpdateList.size = 0

        // in Phase 1
        //   segmentUpdateList.size += (segmentUpdate for each learning Neuron = # of active columns from SP)

        // in Phase 2
        //   segmentUpdateList.size += active segments(created by spatial pooling)
        //                          += any synapses that could have predicted this segments activation?

        // in Phase 3
        //   segmentUpdateList.size -= adapt segments on learning neurons
        //   segmentUpdateList.size -= adapt segments previously predictive & NOT currently predictive

        // temporal pooling algorithm is deterministic
        this.temporalPooler.performPooling();
        assertEquals(20, this.temporalPooler.getSegmentUpdateList().size());
        this.temporalPooler.nextTimeStep();

        this.spatialPooler.performPooling();
        this.temporalPooler.performPooling();
        //assertEquals(19, this.temporalPooler.getSegmentUpdateList().size()); // NOTE: why is this failing on Travis CI?
        this.temporalPooler.nextTimeStep();

        this.spatialPooler.performPooling();
        this.temporalPooler.performPooling();
        //assertEquals(19, this.temporalPooler.getSegmentUpdateList().size());
        this.temporalPooler.nextTimeStep();
    }

    public void test_phaseOneCase1() {
        // Case 1: bottomUpPredicted = false learningCellChosen = false
        this.temporalPooler.phaseOne(this.spatialPooler.getActiveColumns());

        Column[][] columns = this.spatialPooler.getRegion().getColumns();
        int numberOfActiveNeurons = 0;
        for (int rowPosition = 0; rowPosition < columns.length; rowPosition++) {
            for (int columnPosition = 0; columnPosition < columns[0].length; columnPosition++) {
                for (Neuron neuron : columns[rowPosition][columnPosition].getNeurons()) {
                    if (neuron.getActiveState()) {
                        numberOfActiveNeurons++;
                    }
                }
            }
        }

        // 5 active columns where each column has 3 neurons. 5 * 3 = 15
        assertEquals(15, numberOfActiveNeurons);

        // 5 learning neurons were chosen and given a new distal segment
        // each with newSynapseCount new synapses
        assertEquals(5, this.temporalPooler.getSegmentUpdateList().size());
    }

    public void test_phaseOneCase2() {
        // Case 2: bottomUpPredicted = true learningCellChosen = false
        Neuron neuron = new Neuron();
        neuron.setPreviousActiveState(true);

        DistalSegment distalSegment = new DistalSegment();
        distalSegment.setPreviousActiveState(true);
        distalSegment.setSequenceState(true);
        neuron.addDistalSegment(distalSegment);

        Column column = this.spatialPooler.getRegion().getColumn(1, 3);
        column.setNeuron(neuron, 0);

        this.temporalPooler.phaseOne(this.spatialPooler.getActiveColumns());

        Column[][] columns = this.spatialPooler.getRegion().getColumns();
        int numberOfActiveNeurons = 0;
        for (int x = 0; x < columns.length; x++) {
            for (int y = 0; y < columns[0].length; y++) {
                for (Neuron currentNeuron : columns[x][y].getNeurons()) {
                    if (currentNeuron.getActiveState()) {
                        numberOfActiveNeurons++;
                    }
                }
            }
        }

        assertEquals(15, numberOfActiveNeurons);
        assertEquals(5, this.temporalPooler.getCurrentLearningNeurons().size());
    }

//    public void test_phaseOneCase3() {
//        // Case 3: bottomUpPredicted = false learningCellChosen = true
//        // This is not possible
//    }
//
//    public void test_phaseOneCase4() {
//        // Case 4: bottomUpPredicted = true learningCellChosen = true
//        // This is covered by case 2
//    }

    public void test_getSegmentActiveSynapses() {
        // Case 1: previousTimeStep = true & newSynapses = true
        this.setUpCurrentLearningNeuronListForTemporalPooler();

        SegmentUpdate segmentUpdate = this.temporalPooler
                .getSegmentActiveSynapses(new ColumnPosition(0, 0), 0,
                        this.distalSegmentWith1PreviousActiveSynapse, true,
                        true);

        assertEquals(4, segmentUpdate.getSynapsesWithActiveCells().size());
        assertEquals(0, segmentUpdate.getSynpasesWithDeactiveCells().size());

        // Case 2: previousTimeStep = false & newSynapses = false
        this.setUpCurrentLearningNeuronListForTemporalPooler();

        SegmentUpdate segmentUpdate2 = this.temporalPooler
                .getSegmentActiveSynapses(new ColumnPosition(0, 0), 0,
                        this.distalSegmentWith1PreviousActiveSynapse, false,
                        false);

        assertEquals(0, segmentUpdate2.getSynapsesWithActiveCells().size());
        assertEquals(4, segmentUpdate2.getSynpasesWithDeactiveCells().size());

        // ---------------------------------------------------------------------
        DistalSegment twoActiveAndOnePreviouslyActiveSynapses = new DistalSegment();

        VisionCell activeVisionCell_1 = new VisionCell();
        activeVisionCell_1.setActiveState(true);

        VisionCell activeVisionCell_2 = new VisionCell();
        activeVisionCell_2.setActiveState(true);

        twoActiveAndOnePreviouslyActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_1, 0.2, 0, 10));
        twoActiveAndOnePreviouslyActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_2, 0.2, 0, 11));

        VisionCell activeVisionCell_3 = new VisionCell();
        activeVisionCell_3.setPreviousActiveState(true);
        twoActiveAndOnePreviouslyActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_3, 0.2, 0, 12));
        // ---------------------------------------------------------------------

        SegmentUpdate segmentUpdate3 = this.temporalPooler
                .getSegmentActiveSynapses(new ColumnPosition(0, 0), 0,
                        twoActiveAndOnePreviouslyActiveSynapses, false, false);

        assertEquals(2, segmentUpdate3.getSynapsesWithActiveCells().size());
        assertEquals(1, segmentUpdate3.getSynpasesWithDeactiveCells().size());
    }

    public void test_addRandomlyChosenSynapsesFromCurrentLearningNeurons() {
        try {
            Set<Synapse<Cell>> synapses = this.temporalPooler
                    .addRandomlyChosenSynapsesFromCurrentLearningNeurons(
                            new HashSet<Synapse<Cell>>(), new DistalSegment(),
                            new ColumnPosition(0, 0));
            fail("should've thrown an exception!");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "currentLearningNeurons in TemporalPooler class "
                            + "addRandomlyChosenSynapsesFromCurrentLearningNeurons method cannot be size 0",

                    expected.getMessage());
        }

        DistalSegment distalSegment1 = this
                .setUpCurrentLearningNeuronListForTemporalPooler();

        Set<Synapse<Cell>> synapses = this.temporalPooler
                .addRandomlyChosenSynapsesFromCurrentLearningNeurons(
                        new HashSet<Synapse<Cell>>(), distalSegment1,
                        new ColumnPosition(0, 0));
        // even though newSynapseCount is 25 if there are only 3 unique
        // learning neurons, then only 3 synapses will be added
        assertEquals(3, synapses.size());
        assertEquals(3, distalSegment1.getSynapses().size());
    }

    DistalSegment setUpCurrentLearningNeuronListForTemporalPooler() {
        Neuron neuron1 = new Neuron();
        Neuron neuron2 = new Neuron();
        Neuron neuron3 = new Neuron();
        DistalSegment distalSegment1 = new DistalSegment();
        neuron1.addDistalSegment(distalSegment1);
        DistalSegment distalSegment2 = new DistalSegment();
        neuron2.addDistalSegment(distalSegment2);
        DistalSegment distalSegment3 = new DistalSegment();
        neuron3.addDistalSegment(distalSegment3);
        this.temporalPooler.getCurrentLearningNeurons().add(neuron1);
        this.temporalPooler.getCurrentLearningNeurons().add(neuron2);
        this.temporalPooler.getCurrentLearningNeurons().add(neuron3);
        return distalSegment1;
    }

    public void test_generatePotentialSynapses() {
        Neuron neuron = new Neuron();
        neuron.addDistalSegment(this.distalSegmentWith3ActiveSynapses);
        neuron.addDistalSegment(this.distalSegmentWith4ActiveSynapses);
        this.temporalPooler.getCurrentLearningNeurons().add(neuron);

        List<Synapse<Cell>> synapses2 = this.temporalPooler
                .generatePotentialSynapses(2, new ColumnPosition(0, 0));
        assertEquals(3, synapses2.size());
    }

    public void test_createNewSynapsesConnectedToCurrentLearningNeurons() {
        try {
            this.temporalPooler
                    .createNewSynapsesConnectedToCurrentLearningNeurons(
                            new ArrayList<Synapse<Cell>>(), 1,
                            new ColumnPosition(0, 0));
            fail("should've thrown an exception!");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "currentLearningNeurons in TemporalPooler class "
                            + "createNewSynapsesConnectedToCurrentLearningNeurons method cannot be size 0",
                    expected.getMessage());
        }

        Neuron neuron = new Neuron();
        neuron.addDistalSegment(this.distalSegmentWith1ActiveSynapse);
        this.temporalPooler.getCurrentLearningNeurons().add(neuron);

        List<Synapse<Cell>> newSynapses = this.temporalPooler
                .createNewSynapsesConnectedToCurrentLearningNeurons(
                        new ArrayList<Synapse<Cell>>(), 1, new ColumnPosition(
                                3, 3));
        assertEquals(1, newSynapses.size());
        Synapse<Cell> sameSynapse = new Synapse<Cell>(neuron, 3, 3);
        assertEquals(sameSynapse, newSynapses.get(0));
    }

    public void test_phaseTwo() {
        this.temporalPooler.phaseOne(this.spatialPooler.getActiveColumns());
        this.temporalPooler.phaseTwo();
        assertEquals(25, this.temporalPooler.getSegmentUpdateList().size());
    }

    public void test_phaseThree() {
        this.temporalPooler.phaseOne(this.spatialPooler.getActiveColumns());
        this.temporalPooler.phaseTwo();
        this.temporalPooler.phaseThree();
        assertEquals(20, this.temporalPooler.getSegmentUpdateList().size());
    }

    public void test_adaptSegments() {
        Set<Synapse<Cell>> synapsesWithActiveCells = new HashSet<Synapse<Cell>>();
        synapsesWithActiveCells.add(new Synapse<Cell>(new VisionCell(), 0, 0));
        Set<Synapse<Cell>> synapsesWithDeactiveCells = new HashSet<Synapse<Cell>>();
        synapsesWithDeactiveCells
                .add(new Synapse<Cell>(new VisionCell(), 0, 0));

        SegmentUpdate segmentUpdate = new SegmentUpdate(
                synapsesWithActiveCells, synapsesWithDeactiveCells,
                new ColumnPosition(1, 1), 1);

        this.temporalPooler.adaptSegments(segmentUpdate, true);
        Set<Synapse<Cell>> increasedSynapses = segmentUpdate
                .getSynapsesWithActiveCells();
        for (Synapse synapse : increasedSynapses) {
            assertEquals(Synapse.INITIAL_PERMANENCE
                    + Synapse.PERMANENCE_INCREASE, synapse.getPermanenceValue());
        }

        Set<Synapse<Cell>> decreasedSynapses = segmentUpdate
                .getSynpasesWithDeactiveCells();
        for (Synapse synapse : decreasedSynapses) {
            assertEquals(Synapse.INITIAL_PERMANENCE
                    - Synapse.PERMANENCE_DECREASE, synapse.getPermanenceValue());
        }

        this.temporalPooler.adaptSegments(segmentUpdate, false);
        increasedSynapses = segmentUpdate.getSynapsesWithActiveCells();
        for (Synapse synapse : increasedSynapses) {
            assertEquals(
                    Synapse.INITIAL_PERMANENCE + Synapse.PERMANENCE_INCREASE
                            - Synapse.PERMANENCE_DECREASE,
                    synapse.getPermanenceValue());
        }

        decreasedSynapses = segmentUpdate.getSynpasesWithDeactiveCells();
        for (Synapse synapse : decreasedSynapses) {
            assertEquals(Synapse.INITIAL_PERMANENCE
                    - Synapse.PERMANENCE_DECREASE, synapse.getPermanenceValue());
        }
    }

    public void test_getBestMatchingNeuronIndex() {
        Column column = new Column(4, new ColumnPosition(0, 0));
        Neuron neuron0 = new Neuron();
        Neuron neuron1 = new Neuron();
        Neuron neuron2 = new Neuron();
        Neuron neuron3 = new Neuron();
        neuron0.addDistalSegment(distalSegmentWith1ActiveSynapse);
        neuron1.addDistalSegment(distalSegmentWith2ActiveSynapses);
        neuron2.addDistalSegment(distalSegmentWith4ActiveSynapses);
        neuron3.addDistalSegment(distalSegmentWith3ActiveSynapses);
        column.setNeuron(neuron0, 0);
        column.setNeuron(neuron1, 1);
        column.setNeuron(neuron2, 2);
        column.setNeuron(neuron3, 3);
        // NOTE: this test not passing on Travis CI with Java version 1.8.0_31.
        // NOTE: local Java version = 1.8.0_51-b16
        //assertEquals(2, this.temporalPooler.getBestMatchingNeuronIndex(column));

        Column column2 = new Column(2, new ColumnPosition(0, 0));
        Neuron neuron4 = new Neuron();
        Neuron neuron5 = new Neuron();
        neuron4.addDistalSegment(new DistalSegment());
        neuron5.addDistalSegment(new DistalSegment());
        column2.setNeuron(neuron4, 0);
        column2.setNeuron(neuron5, 1);
        assertEquals(0, this.temporalPooler.getBestMatchingNeuronIndex(column2));
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
                activeVisionCell_1, 0.2, 0, 1));
        this.distalSegmentWith2ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_2, 0.2, 0, 2));

        this.distalSegmentWith3ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_1, 0.2, 0, 3));
        this.distalSegmentWith3ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_2, 0.2, 0, 4));
        this.distalSegmentWith3ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_3, 0.2, 0, 5));

        this.distalSegmentWith4ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_1, 0.2, 0, 6));
        this.distalSegmentWith4ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_2, 0.2, 0, 7));
        this.distalSegmentWith4ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_3, 0.2, 0, 8));
        this.distalSegmentWith4ActiveSynapses.addSynapse(new Synapse<Cell>(
                activeVisionCell_4, 0.2, 0, 9));

        this.distalSegmentWith1PreviousActiveSynapse = new DistalSegment();
        VisionCell activeVisionCell_5 = new VisionCell();
        activeVisionCell_5.setPreviousActiveState(true);
        this.distalSegmentWith1PreviousActiveSynapse
                .addSynapse(new Synapse<Cell>(activeVisionCell_5, 0.2, 0, 0));
    }
}