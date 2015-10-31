package model.MARK_II.region;

import model.MARK_II.generalAlgorithm.ColumnPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A data structure that represents a single Column of Neurons within a Region
 * object. A Column contains a ProximalSegment and properties that determine how
 * the Cells within this Column will be updated.
 *  
 * Input to Column: # of active Synapses from this Column's ProximalSegment.
 *  
 * Output from Column: if this Column is active or not during spatial pooling.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version July 29, 2013
 */
public class Column {
    // also stored as number of active Synapses. This variable is created so
    // the active Columns do not have to be recomputed during the same iteration
    // of spatial pooling
    private boolean isActive;

    private final Neuron[] neurons;

    private final Segment proximalSegment;

    private List<ColumnPosition> neighborColumnPositions;

    private int overlapScore;

    /**
     * value computed for a Column during learning. Always >= 1. Used to
     * increase the overlapScore for inactive Columns.
     */
    private float boostValue;

    /**
     * A sliding average representing how many times this Column has been active
     * after inhibition (e.g. over the past 1000 iterations of spatial learning)
     */
    private float activeDutyCycle;

    /**
     * A sliding average representing how many times this Column has had
     * significant overlapScore(greater than MINIMUM_OVERLAP)
     */
    private float overlapDutyCycle;

    /**
     * Used to compute the activeDutyCycle and overlapDutyCycle by decreasing it
     * exponentially less as the boostValue approached 0 and minimumOverlapScore
     * respectively.
     */
    public static float EXPONENTIAL_MOVING_AVERAGE_AlPHA = 0.005f;

    private ColumnPosition currentPosition;
    private int learningNeuronPosition;

    public Column(int numberOfCells, ColumnPosition currentPosition) {
        if (numberOfCells < 1) {
            throw new IllegalArgumentException(
                    "numberOfCells in Column class constructor cannot be less than 1");
        } else {
            this.isActive = false;
            this.neurons = new Neuron[numberOfCells];
            for (int i = 0; i < numberOfCells; i++) {
                this.neurons[i] = new Neuron();
            }
            this.proximalSegment = new ProximalSegment();
            this.neighborColumnPositions = new ArrayList<ColumnPosition>();
            this.overlapScore = 0;
            this.boostValue = 1.0f;
            this.activeDutyCycle = 1.0f; // Must be greater than 0, or will stay
            // 0
            this.overlapDutyCycle = 1.0f; // Must be greater than 0, or will
            // stay 0

            this.currentPosition = currentPosition;
        }
    }

    public ColumnPosition getCurrentPosition() {
        return this.currentPosition;
    }

    /**
     * Increases the permenanceValue of every Synapse object in a Column's
     * proximal Segment by "scaleFactor" number of times.
     *
     * @param scaleFactor Number of times to increasePermanences by PERMANENCE_INCREASE.
     */
    public void increaseProximalSegmentSynapsePermanences(int scaleFactor) {
        if (scaleFactor < 0) {
            throw new IllegalArgumentException("scaleFactor in Column class "
                    + "increaseProximalSegmentSynapsePermanences method"
                    + "cannot be less than 0");
        } else {
            for (int i = 0; i < scaleFactor; i++) {
                this.proximalSegment
                        .updateSynapsePermanences(Segment.SynapseUpdateState.INCREASE_ALL);
            }
        }
    }

    /**
     * Compute a moving average of how often this Column has been active after
     * inhibition. Exponential Moving Average(EMA): St = a * Yt + (1 - a) * St -
     * 1
     */
    public void updateActiveDutyCycle() {
        // Note whenever updateActiveDutyCycle() is called, the activeDutyCycle
        // is always decremented less and less but only incremented if the
        // Column
        // was active. Furthermore, the increment applied to activeDutyCycle
        // when the Column is active is a constant representing the maximum
        // decrement of activeDutyCycle from initial value 1. Because of this
        // a Column's activeDutyCycle has a upper bound of 1.
        float newActiveDutyCycle = (1.0f - EXPONENTIAL_MOVING_AVERAGE_AlPHA)
                * this.getActiveDutyCycle();
        if (this.getActiveState()) {
            newActiveDutyCycle += EXPONENTIAL_MOVING_AVERAGE_AlPHA;
        }
        this.activeDutyCycle = newActiveDutyCycle;
    }

    /**
     * Computes the boostValue of a Column during learning.
     *
     * @param minimumDutyCycle Represents the minimum desired firing rate for a Cell. If a
     *                         Cell's firing rate falls below this value, it will be boosted.
     * @return The boostValue of a Column.
     */
    public float boostFunction(float minimumDutyCycle) {
        if (minimumDutyCycle <= 0) {
            throw new IllegalArgumentException(
                    "minimumDutyCycle in Column class boostFunction method cannot be <= 0");
        }
        if (this.getActiveDutyCycle() <= minimumDutyCycle) {
            // the boostValue increases linearly once the Column's
            // activeDutyCycle becomes less than the minimumDutyCycle
            return (minimumDutyCycle / this.getActiveDutyCycle());
        } else
        // this.activeDutyCycle > minimumDutyCycle
        {
            return this.getActiveDutyCycle();
        }
    }

    /**
     * Reinitializes the activeState, overlapScore, neighborColumns, and
     * boostValue fields of this Column object to be recomputed.
     */
    public void nextTimeStep() {
        this.overlapScore = 0;
        this.clearNeighborColumns();
        this.boostValue = 1.0f;
    }

    // -------------------Getters and Setters----------------------
    public void setActiveState(boolean activeState) {
        this.isActive = activeState;
    }

    public boolean getActiveState() {
        return this.isActive;
    }

    public Neuron[] getNeurons() {
        return this.neurons;
    }

    public Neuron getNeuron(int neuronIndex) {
        if (neuronIndex < 0 || neuronIndex >= this.neurons.length) {
            throw new IllegalArgumentException(
                    "neuronIndex in Column class method "
                            + "getNeuron is invalid");
        }
        return this.neurons[neuronIndex];
    }

    public void setNeuron(Neuron neuron, int neuronIndex) {
        this.neurons[neuronIndex] = neuron;
    }

    public List<ColumnPosition> getNeighborColumns() {
        return Collections.unmodifiableList(this.neighborColumnPositions);
    }

    public void clearNeighborColumns() {
        this.neighborColumnPositions.clear();
    }

    public void addNeighborColumns(ColumnPosition columnPosition) {
        this.neighborColumnPositions.add(columnPosition);
    }

    public void setNeighborColumns(List<ColumnPosition> neighborColumns) {
        if (neighborColumns == null) {
            throw new IllegalArgumentException(
                    "neighborColumns in Column class setNeighborColumns method cannot be null");
        } else if (neighborColumns.size() == 0) {
            throw new IllegalArgumentException(
                    "neighborColumns size in Column class setNeighborColumns method must be > 0");
        }
        this.neighborColumnPositions = neighborColumns;
    }

    public int getOverlapScore() {
        return this.overlapScore;
    }

    public void setOverlapScore(int overlapScore) {
        if (overlapScore < 0) {
            throw new IllegalArgumentException(
                    "overlapScore in Column class setOverlapScore method must be >= 0.");
        } else if (overlapScore > this.proximalSegment.getSynapses().size()) {
            throw new IllegalArgumentException(
                    "overlapScore in Column class setOverlapScore method must be <= "
                            + "the total number of Synapses in this column's proximal Segment.");
        }
        this.overlapScore = overlapScore;
    }

    public float getBoostValue() {
        return this.boostValue;
    }

    public void setBoostValue(float boostValue) {
        if (boostValue < 0) {
            throw new IllegalArgumentException(
                    "boostValue in Column class setBoostValue method must >= 0");
        }
        this.boostValue = boostValue;
    }

    public float getActiveDutyCycle() {
        return this.activeDutyCycle;
    }

    public void setActiveDutyCycle(float activeDutyCycle) {
        if (activeDutyCycle < 0 || activeDutyCycle > 1) {
            throw new IllegalArgumentException(
                    "activeDutyCycle in Column class setActiveDutyCycle method must >= 0 & <= 1");
        }
        this.activeDutyCycle = activeDutyCycle;
    }

    public float getOverlapDutyCycle() {
        return this.overlapDutyCycle;
    }

    public void setOverlapDutyCycle(float overlapDutyCycle) {
        if (overlapDutyCycle < 0 || overlapDutyCycle > 1) {
            throw new IllegalArgumentException(
                    "overlapDutyCycle in Column class setOverlapDutyCycle method must >= 0 & <= 1");
        }
        this.overlapDutyCycle = overlapDutyCycle;
    }

    public Segment getProximalSegment() {
        return this.proximalSegment;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n====================================");
        stringBuilder.append("\n------------Column Info-------------");
        stringBuilder.append("\n          number of neurons: ");
        stringBuilder.append(this.neurons.length);
        stringBuilder.append("\nproximalSegment activeState: ");
        stringBuilder.append(this.proximalSegment.getActiveState());
        stringBuilder.append("\n  number of neighborColumns: ");
        stringBuilder.append(this.neighborColumnPositions.size());
        stringBuilder.append("\n               overlapScore: ");
        stringBuilder.append(this.overlapScore);
        stringBuilder.append("\n                 boostValue: ");
        stringBuilder.append(this.boostValue);
        stringBuilder.append("\n            activeDutyCycle: ");
        stringBuilder.append(this.activeDutyCycle);
        stringBuilder.append("\n           overlapDutyCycle: ");
        stringBuilder.append(this.overlapDutyCycle);
        stringBuilder.append("\n exponential moving average: ");
        stringBuilder.append(Column.EXPONENTIAL_MOVING_AVERAGE_AlPHA);
        stringBuilder.append("\n=====================================");
        String columnInformation = stringBuilder.toString();
        return columnInformation;
    }

    public void setLearningNeuronPosition(int learningNeuronPosition) {
        this.learningNeuronPosition = learningNeuronPosition;
    }

    public int getLearningNeuronPosition() {
        return this.learningNeuronPosition;
    }
}
