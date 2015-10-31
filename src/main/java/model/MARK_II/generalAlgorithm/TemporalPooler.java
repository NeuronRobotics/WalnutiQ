package model.MARK_II.generalAlgorithm;

import com.google.gson.Gson;
import model.MARK_II.region.*;
import model.MARK_II.util.FileInputOutput;

import java.io.IOException;
import java.util.*;

/**
 * Idea behind temporal pooling: SDRs that occur adjacent in time probably have
 * a common underlying cause. Several times a second your eyes fixate on a
 * different part of the image causing a complete change in input. Despite this
 * changing input your perception is stable. Somewhere in higher region there
 * must be neurons that remain active.
 *
 * Input into TemporalPooler: activeColumns of a Region at time t computed by
 * SpatialPooler
 *
 * Output from TemporalPooler: boolean OR of the current active and predictive
 * state for each neuron in the set of activeColumns of a Region.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 27, 2014
 */
public class TemporalPooler extends Pooler {
    private SpatialPooler spatialPooler;

    private final int newSynapseCount;
    private List<Neuron> currentLearningNeurons;
    private SegmentUpdateList segmentUpdateList;
    private Set<ColumnPosition> predictiveColumnsAtTForTPlus1;
    private Set<ColumnPosition> predictiveColumnsAtTMinus1;

    public TemporalPooler(SpatialPooler spatialPooler, int newSynapseCount) {
        this.spatialPooler = spatialPooler;
        super.region = spatialPooler.getRegion();
        this.segmentUpdateList = new SegmentUpdateList();

        this.newSynapseCount = newSynapseCount;

        this.currentLearningNeurons = new ArrayList<Neuron>();
        this.predictiveColumnsAtTForTPlus1 = new HashSet<ColumnPosition>();
        this.predictiveColumnsAtTMinus1 = new HashSet<ColumnPosition>();
    }

    public void performPooling() {
        Set<Column> activeColumns = this.spatialPooler.getActiveColumns();
        if (super.getLearningState()) {
            this.phaseOne(activeColumns);
            this.phaseTwo();
            this.phaseThree();
        } else {
            this.computeActiveStateOfAllNeuronsInActiveColumn(activeColumns);
            this.computePredictiveStateOfAllNeurons();
        }
    }

    public SpatialPooler getSpatialPooler() {
        return this.spatialPooler;
    }

    public void nextTimeStep() {
        Column[][] columns = super.region.getColumns();
        for (int row = 0; row < super.region.getNumberOfRowsAlongRegionYAxis(); row++) {
            for (int column = 0; column < super.region.getNumberOfColumnsAlongRegionXAxis(); column++) {
                for (Neuron neuron : columns[row][column].getNeurons()) {
                    neuron.nextTimeStep();

                    for (DistalSegment distalSegment : neuron
                            .getDistalSegments()) {
                        distalSegment.nextTimeStep();
                    }
                }
            }
        }
        this.spatialPooler.getAlgorithmStatistics().nextTimeStep();
        this.currentLearningNeurons.clear();
        this.segmentUpdateList.clear();
        this.predictiveColumnsAtTMinus1.addAll(this.predictiveColumnsAtTForTPlus1);
        this.predictiveColumnsAtTForTPlus1.clear();
    }

    /**
     * Compute the activeState for each Neuron in activeColumns. Then in each
     * active Column a learning Neuron is chosen.
     */
    void phaseOne(Set<Column> activeColumns) {
        /// for c in activeColumns(t)
        for (Column column : activeColumns) {
            /// buPredicted = false
            boolean bottomUpPredicted = false;
            /// lcChosen = false
            boolean learningCellChosen = false;

            Neuron[] neurons = column.getNeurons();
            /// for i = 0 to cellsPerColumn - 1
            for (int i = 0; i < neurons.length; i++) {
                /// predictiveState(c, i, t-1) == true then
                if (neurons[i].getPreviousActiveState() == true) {
                    /// s = getActiveSegment(c, i, t-1, activeState)
                    DistalSegment bestSegment = neurons[i]
                            .getBestPreviousActiveSegment(this.spatialPooler.getAlgorithmStatistics());

                    /// if s.sequenceSegment == true then
                    if (bestSegment != null
                            && bestSegment
                            .getSequenceStatePredictsFeedFowardInputOnNextStep()) {
                        /// buPredicted = true
                        bottomUpPredicted = true;
                        /// activeState(c, i, t) = 1
                        neurons[i].setActiveState(true);

                        /// if segmentActive(s, t-1, learnState) then
                        if (bestSegment.getPreviousActiveState()) {
                            /// lcChosen = true
                            learningCellChosen = true;
                            /// learnState(c, i, t) = 1
                            column.setLearningNeuronPosition(i);
                            this.currentLearningNeurons.add(neurons[i]);
                        }
                    }
                }
            }
            /// if buPredicted == false then
            if (bottomUpPredicted == false) {
                /// for i = 0 to cellsPerColumn - 1
                for (Neuron neuron : column.getNeurons()) {
                    /// activeState(c, i, t) = 1
                    neuron.setActiveState(true);
                }
            }

            /// if lcChosen == false then
            if (learningCellChosen == false) {
                /// l,s = getBestMatchingCell(c, t-1)
                int bestNeuronIndex = this.getBestMatchingNeuronIndex(column);
                /// learnState(c, i, t) = 1
                column.setLearningNeuronPosition(bestNeuronIndex);
                this.currentLearningNeurons.add(column
                        .getNeuron(bestNeuronIndex));

                DistalSegment segment = neurons[bestNeuronIndex]
                        .getBestPreviousActiveSegment(this.spatialPooler.getAlgorithmStatistics());
                /// sUpdate = getSegmentActiveSynapses(c, i, s, t-1, true)
                SegmentUpdate segmentUpdate = this.getSegmentActiveSynapses(
                        column.getCurrentPosition(), bestNeuronIndex, segment,
                        true, true);
                /// sUpdate.sequenceSegment = true
                segmentUpdate.setSequenceState(true);
                segment.setSequenceState(true);
                this.spatialPooler.getAlgorithmStatistics().getTP_sequenceSegmentsHistoryAndAdd(1);

                /// segmentUpdateList.add(sUpdate)
                this.segmentUpdateList.add(segmentUpdate);
            }
        }
        this.spatialPooler.getAlgorithmStatistics().getTP_learningNeuronsHistoryAndAdd(this.currentLearningNeurons.size());
    }

    /**
     * @param newSynapses Actually adding new Synapses to given segment object
     * @return A segmentUpdate data structure containing a list of proposed
     * changes to segment. Let activeSynapses be the list of active
     * synapses where the originating cells have their activeState
     * output = 1 at time step t. (This list is empty if s = -1 since
     * the segment doesn't exist.) newSynapses is an optional argument
     * that defaults to false. If newSynapses is true, then
     * newSynapseCount - count(activeSynapses) synapses are added to
     * activeSynapses. These synapses are randomly chosen from the set
     * of cells that have learnState output = 1 at time step t.
     */
    SegmentUpdate getSegmentActiveSynapses(ColumnPosition columnPosition,
                                           int neuronIndex, Segment segment, boolean previousTimeStep,
                                           boolean newSynapses) {
        Set<Synapse<Cell>> activeSynapses = new HashSet<Synapse<Cell>>();
        Set<Synapse<Cell>> deactiveSynapses = new HashSet<Synapse<Cell>>();

        for (Synapse<Cell> synapse : segment.getSynapses()) {

            if (previousTimeStep) {
                if (synapse.getCell().getPreviousActiveState()) {
                    activeSynapses.add(synapse);
                } else {
                    deactiveSynapses.add(synapse);
                }
            } else {
                if (synapse.getCell().getActiveState()) {
                    activeSynapses.add(synapse);
                } else {
                    deactiveSynapses.add(synapse);
                }
            }
        }

        if (newSynapses) {
            activeSynapses = this
                    .addRandomlyChosenSynapsesFromCurrentLearningNeurons(
                            activeSynapses, segment, columnPosition);
        }

        return new SegmentUpdate(activeSynapses, deactiveSynapses,
                columnPosition, neuronIndex);
    }

    Set<Synapse<Cell>> addRandomlyChosenSynapsesFromCurrentLearningNeurons(
            Set<Synapse<Cell>> activeSynapses, Segment segment,
            ColumnPosition columnPosition) {
        if (this.currentLearningNeurons.size() == 0) {
            throw new IllegalStateException(
                    "currentLearningNeurons in TemporalPooler class "
                            + "addRandomlyChosenSynapsesFromCurrentLearningNeurons"
                            + " method cannot be size 0");
        }

        int numberOfSynapsesToAdd = this.newSynapseCount
                - activeSynapses.size();

        List<Synapse<Cell>> potentialSynapsesToAdd = this
                .generatePotentialSynapses(numberOfSynapsesToAdd,
                        columnPosition);

        for (int i = 0; i < numberOfSynapsesToAdd; i++) {
            activeSynapses.add(potentialSynapsesToAdd.get(i));
            segment.addSynapse(potentialSynapsesToAdd.get(i));
        }

        return activeSynapses;
    }

    /**
     * This method must never return an emtpy list.
     */
    List<Synapse<Cell>> generatePotentialSynapses(int numberOfSynapsesToAdd,
                                                  ColumnPosition columnPosition) {
        List<Synapse<Cell>> potentialSynapsesToAdd = new ArrayList<Synapse<Cell>>();
        for (Neuron neuron : this.currentLearningNeurons) {
            // it is okay if initally no learning neurons have any distal
            // segments
            for (DistalSegment distalSegment : neuron.getDistalSegments()) {
                if (potentialSynapsesToAdd.size() >= numberOfSynapsesToAdd) {
                    break;
                } else {
                    potentialSynapsesToAdd.addAll(distalSegment.getSynapses());
                }
            }
            // it is possible potentialSynapsesToAdd.size() is still <
            // numberOfSynapsesToAdd
            if (potentialSynapsesToAdd.size() >= numberOfSynapsesToAdd) {
                break;
            }
        }

        // it is possible potentialSynapsesToAdd.size() is still <
        // numberOfSynapsesToAdd and this is a problem if it is empty
        // because then a neuron's segments will never have any new Synapses
        if (numberOfSynapsesToAdd > potentialSynapsesToAdd.size()) {
            potentialSynapsesToAdd = this
                    .createNewSynapsesConnectedToCurrentLearningNeurons(
                            potentialSynapsesToAdd, numberOfSynapsesToAdd,
                            columnPosition);
        }
        return potentialSynapsesToAdd;
    }

    List<Neuron> getCurrentLearningNeurons() {
        return this.currentLearningNeurons;
    }

    List<Synapse<Cell>> createNewSynapsesConnectedToCurrentLearningNeurons(
            List<Synapse<Cell>> potentialSynapsesToAdd,
            int numberOfSynapsesToAdd, ColumnPosition columnPosition) {

        int remainingNumberOfSynapsesToAdd = numberOfSynapsesToAdd
                - potentialSynapsesToAdd.size();

        this.spatialPooler.getAlgorithmStatistics().getTP_synapsesHistoryAndAdd(remainingNumberOfSynapsesToAdd);

        int numberOfLearningNeurons = this.currentLearningNeurons.size();
        if (numberOfLearningNeurons == 0) {
            throw new IllegalStateException(
                    "currentLearningNeurons in TemporalPooler class "
                            + "createNewSynapsesConnectedToCurrentLearningNeurons"
                            + " method cannot be size 0");
        }

        int learningNeuronIndex = 0;
        for (int i = 0; i < remainingNumberOfSynapsesToAdd; i++) {
            Synapse<Cell> newSynapse = new Synapse<Cell>(
                    this.currentLearningNeurons.get(learningNeuronIndex),
                    columnPosition.getRow(), columnPosition.getColumn());
            potentialSynapsesToAdd.add(newSynapse);

            if ((learningNeuronIndex + 1) < numberOfLearningNeurons) {
                learningNeuronIndex++;
            } else { // wrap around and so as many different learning neurons
                // are used
                learningNeuronIndex = 0;
            }
        }
        return potentialSynapsesToAdd;
    }

    /**
     * Calculated the predictive state for each Neuron. A Neuron's
     * predictiveState will be true if 1 or more distal segments becomes active.
     */
    void phaseTwo() {
        /// for c, i in cells
        Column[][] columns = this.region.getColumns();
        for (int rowIndex = 0; rowIndex < columns.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columns[0].length; columnIndex++) {
                Column column = columns[rowIndex][columnIndex];
                Neuron[] neurons = column.getNeurons();
                for (int i = 0; i < neurons.length; i++) {
                    // we must compute the best segment here because
                    // if we compute it where it is commented out below
                    // then we would be iterating over the neuron's list
                    // of segments again
                    Segment predictingSegment = neurons[i]
                            .getBestPreviousActiveSegment(this.spatialPooler
                                    .getAlgorithmStatistics());

                    /// for s in segments(c, i)
                    for (Segment segment : neurons[i].getDistalSegments()) {
                        // NOTE: segment may become active during the spatial pooling
                        // between temporal pooling iterations
                        /// if segmentActive(s, t, activeState) then
                        if (segment.getActiveState()) {
                            /// predictiveState(c, i, t) = 1
                            neurons[i].setPredictingState(true);
                            this.spatialPooler.getAlgorithmStatistics().getTP_activeDistalSegmentsHistoryAndAdd(1);
                            this.predictiveColumnsAtTForTPlus1.add(column
                                    .getCurrentPosition());

                            /// activeUpdate = getSegmentActiveSynapses(c, i, s, t, false)
                            SegmentUpdate activeUpdate = this
                                    .getSegmentActiveSynapses(
                                            column.getCurrentPosition(), i,
                                            segment, false, false);
                            /// segmentUpdateList.add(activeUpdate)
                            this.segmentUpdateList.add(activeUpdate);
                            // Segment predictingSegment = neurons[i]
                            // .getBestPreviousActiveSegment();

                            /// predSegment = getBestMatchingSegment(c, i, t-1)
                            /// predUpdate = getSegmentActiveSynapses(c, i, predSegment, t-1, true)
                            SegmentUpdate predictionUpdate = this
                                    .getSegmentActiveSynapses(
                                            column.getCurrentPosition(), i,
                                            predictingSegment, true, true);
                            /// segmentUpdateList.add(predUpdate)
                            this.segmentUpdateList.add(predictionUpdate);
                        }
                    }
                }
            }
        }
        this.spatialPooler.getAlgorithmStatistics()
                .getTP_predictionScoreHistoryAndAdd(super.algorithmStatistics
                        .computePredictionScore(this.spatialPooler.getActiveColumnPositions(), this.predictiveColumnsAtTForTPlus1));
    }

    /**
     * Carries out learning. Segment updates that have been queued up are
     * actually implemented once we get feed-forward input and a Neuron is
     * chosen as a learning Neuron. Otherwise, if the Neuron ever stops
     * predicting for any reason, we negatively reinforce the Segments.
     */
    void phaseThree() {
        /// for c, i in cells
        Column[][] columns = this.region.getColumns();
        for (int rowIndex = 0; rowIndex < columns.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columns[0].length; columnIndex++) {
                Column column = columns[rowIndex][columnIndex];
                ColumnPosition c = column.getCurrentPosition();
                Neuron[] neurons = column.getNeurons();
                for (int i = 0; i < neurons.length; i++) {
                    /// if learnState(s, i, t) == 1 then
                    if (i == column.getLearningNeuronPosition()) {
                        /// adaptSegments(segmentUpdateList(c, i), true)
                        this.adaptSegments(
                                this.segmentUpdateList.getSegmentUpdate(c, i), true);
                        /// segmentUpdateList(c, i).delete()
                        this.segmentUpdateList.deleteSegmentUpdate(c, i);

                        /// else if predictiveState(c, i, t) == 0 and predictiveState(c, i, t-1)==1 then
                    } else if (neurons[i].getPredictingState() == false
                            && neurons[i].getPreviousPredictingState() == true) {
                        /// adaptSegments(segmentUpdateList(c, i), false)
                        this.adaptSegments(
                                this.segmentUpdateList.getSegmentUpdate(c, i),
                                false);
                        /// segmentUpdateList(c, i).delete()
                        this.segmentUpdateList.deleteSegmentUpdate(c, i);
                    }
                }
            }
        }
    }

    /**
     * Iterates through the Synapses of a SegmentUpdate and reinforces each
     * Synapse. If positiveReinforcement is true then Synapses on the list get
     * their permanenceValues incremented by permanenceIncrease. All other
     * Synapses get their permanenceValue decremented by permanenceDecrease. If
     * positiveReinforcement is false, then Synapses on the list get their
     * permanenceValues decremented by permanenceDecrease. Finally, any Synapses
     * in SegmentUpdate that do not yet exist get added with a permanenceValue
     * of initialPermanence.
     */
    void adaptSegments(SegmentUpdate segmentUpdate,
                       boolean positiveReinforcement) {
        if (segmentUpdate == null) {
            // the neuron being iterated over does not have any segments to
            // update so skip
            return;
        }
        Set<Synapse<Cell>> synapsesWithActiveCells = segmentUpdate
                .getSynapsesWithActiveCells();
        Set<Synapse<Cell>> synapsesWithDeactiveCells = segmentUpdate
                .getSynpasesWithDeactiveCells();

        if (positiveReinforcement) {
            for (Synapse<Cell> synapse : synapsesWithActiveCells) {
                synapse.increasePermanence();
            }
            for (Synapse<Cell> synapse : synapsesWithDeactiveCells) {
                synapse.decreasePermanence();
            }
        } else {
            for (Synapse<Cell> synapse : synapsesWithActiveCells) {
                synapse.decreasePermanence();
            }
        }
    }

    /**
     * @return The index of the Neuron with the Segment with the greatest number
     * of active Synapses. If no best matching Segment is found, return the
     * Neuron with the least number of active Synapses.
     */
    int getBestMatchingNeuronIndex(Column column) {
        int greatestNumberOfActiveSynapses = 0;
        int bestMatchingNeuronIndex = 0;

        int leastNumberOfSegments = -1;
        int neuronWithLeastSegmentsIndex = -1;
        boolean setNumberOfSegments = false;

        Neuron[] neurons = column.getNeurons();

        for (int i = 0; i < neurons.length; i++) {
            int numberOfSegments = neurons[i].getDistalSegments().size();
            if (!setNumberOfSegments) {
                // following code should be only run the first time
                leastNumberOfSegments = numberOfSegments;
                neuronWithLeastSegmentsIndex = i;
                setNumberOfSegments = true;
            }

            Segment bestSegment = neurons[i].getBestActiveSegment(this.spatialPooler.getAlgorithmStatistics());
            int numberOfActiveSynapses = bestSegment.getNumberOfActiveSynapses();

            if (numberOfActiveSynapses > greatestNumberOfActiveSynapses) {
                greatestNumberOfActiveSynapses = numberOfActiveSynapses;
                bestMatchingNeuronIndex = i;
            }

            // In the case all Neuron's Segments have 0 active Synapses we
            // need to return Neuron with least Segments.
            if (numberOfSegments < leastNumberOfSegments) {
                leastNumberOfSegments = numberOfSegments;
                neuronWithLeastSegmentsIndex = i;
            }
        }

        if (greatestNumberOfActiveSynapses == 0) {
            // All Segments have 0 active Synapses so we nned to return Neuron
            // with least Segments.
            return neuronWithLeastSegmentsIndex;
        }

        return bestMatchingNeuronIndex;
    }

    SegmentUpdateList getSegmentUpdateList() {
        return this.segmentUpdateList;
    }

    int getNewSynapseCount() {
        return this.newSynapseCount;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n==========================================");
        stringBuilder.append("\n-------TemporalPooler Information---------");
        stringBuilder.append("\n     biological region name: ");
        stringBuilder.append(this.region.getBiologicalName());
        stringBuilder.append("\n     segmentUpdateList size: ");
        stringBuilder.append(this.segmentUpdateList.size());
        stringBuilder.append("\n            newSynapseCount: ");
        stringBuilder.append(this.newSynapseCount);
        stringBuilder.append("\ncurrentLearningNeurons size: ");
        stringBuilder.append(this.currentLearningNeurons.size());
        stringBuilder.append("\n================================");
        String temporalPoolerInformation = stringBuilder.toString();
        return temporalPoolerInformation;
    }

    void computeActiveStateOfAllNeuronsInActiveColumn(Set<Column> activeColumns) {
        for (Column column : activeColumns) {

            boolean bottomUpPredicted = false;

            for (Neuron neuron : column.getNeurons()) {

                if (neuron.getPreviousActiveState() == true) {
                    DistalSegment bestSegment = neuron
                            .getBestPreviousActiveSegment(this.spatialPooler.getAlgorithmStatistics());

                    // Question: when is segment ever set to be sequence segment?
                    // Answer:
                    if (bestSegment != null
                            && bestSegment
                            .getSequenceStatePredictsFeedFowardInputOnNextStep()) {
                        bottomUpPredicted = true;
                        neuron.setActiveState(true);
                    }
                }
            }

            if (bottomUpPredicted == false) {
                for (Neuron neuron : column.getNeurons()) {
                    neuron.setActiveState(true);
                }
            }
        }
    }

    void computePredictiveStateOfAllNeurons() {
        Column[][] columns = this.region.getColumns();
        for (int rowIndex = 0; rowIndex < columns.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columns[0].length; columnIndex++) {
                Column column = columns[rowIndex][columnIndex];
                for (Neuron neuron : column.getNeurons()) {
                    for (Segment segment : neuron.getDistalSegments()) {
                        if (segment.getActiveState()) {
                            neuron.setPredictingState(true);
                            this.spatialPooler.getAlgorithmStatistics().getTP_activeDistalSegmentsHistoryAndAdd(1);
                            this.predictiveColumnsAtTForTPlus1.add(column
                                    .getCurrentPosition());
                        }
                    }
                }
            }
        }
        this.spatialPooler.getAlgorithmStatistics()
                .getTP_predictionScoreHistoryAndAdd(super.algorithmStatistics
                        .computePredictionScore(this.spatialPooler.getActiveColumnPositions(), this.predictiveColumnsAtTMinus1));
    }

    public int getNumberOfCurrentLearningNeurons() {
        return this.currentLearningNeurons.size();
    }

    /**
     * Save AlgorithmStatistics object into a .JSON file for the current Region.
     */
    public void saveCurrentRegionAlgorithmStatistics(String pathAndFolderNameWithoutEndingBacklash) throws IOException {
        Gson gson = new Gson();
        String algorithmStatisticsInJSON = gson.toJson(this.spatialPooler.getAlgorithmStatistics());
        String finalPathAndFile = pathAndFolderNameWithoutEndingBacklash +
                "/region_" + region.getBiologicalName()
                + "_statistics.json";
        FileInputOutput.saveObjectToTextFile(algorithmStatisticsInJSON,
                finalPathAndFile);
    }
}
