package model.MARK_II.generalAlgorithm;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Q Liu (quinnliu@vt.edu)
 * @version 7/9/2015
 */
public class AlgorithmStatistics {
    public static final int DEFAULT_NUMBER_OF_ALGORITHM_RUNS = 1000;

    // NOTE: each element in each of the following lists represent TOTALS
    //       for 1 time step of running the general learning algorithm.

    // NOTE: Most important statistics at top
    // spatial pooler (SP) statistics:
    private int[] SP_activeSynapsesHistory,
            SP_activeColumnsHistory;
    private double[] SP_inhibitionRadiusHistory;
    // Consider adding SDRStatistic probabilityOfFalsePositive later since
    // y-axis would be a different scale

    // temporal pooler (TP) statistics:
    private int[] TP_synapsesHistory,
            TP_distalSegmentsHistory,
    /**
     * An active distal segment on a Neuron means it has become
     * predictive.
     */
    TP_activeDistalSegmentsHistory,
    // since it is dependent on all Synpase permanence values
    TP_sequenceSegmentsHistory,
            TP_learningNeuronsHistory;
    // distribution of new synapses somehow?
    private double[] TP_predictionScoreHistory;

    private int arraySize;

    private int currentTimeStep;

    public AlgorithmStatistics(int arraySize) {
        this.arraySize = arraySize;
        this.currentTimeStep = 0;
        this.clearAllData();
    }

    public AlgorithmStatistics() {
        this(10);
        this.arraySize = 10;
    }

    public int getCurrentTimeStep() {
        return this.currentTimeStep;
    }

    public void clearAllData() {
        this.SP_activeSynapsesHistory = new int[arraySize];
        this.SP_activeColumnsHistory = new int[arraySize];
        this.SP_inhibitionRadiusHistory = new double[arraySize];

        this.TP_synapsesHistory = new int[arraySize];
        this.TP_distalSegmentsHistory = new int[arraySize];
        this.TP_activeDistalSegmentsHistory = new int[arraySize];
        this.TP_sequenceSegmentsHistory = new int[arraySize];
        this.TP_learningNeuronsHistory = new int[arraySize];
        this.TP_predictionScoreHistory = new double[arraySize];
    }

    public void nextTimeStep() {
        this.currentTimeStep++;
        if (this.currentTimeStep == this.arraySize) {
            this.currentTimeStep--;
            // so user can continue to call getter methods to retrieve arrays
            // without unknowingly adding to currentTimeStep
        }
        if (this.currentTimeStep > this.arraySize) {
            throw new IllegalStateException("you can NOT call nextTimeStep() more" +
                    "times than the array size of AlgorithmStatistics");
        }
    }

    public int[] getSP_activeSynapsesHistoryAndAdd(int valueToAdd) {
        int newValue = this.SP_activeSynapsesHistory[this.currentTimeStep] +
                valueToAdd;
        this.SP_activeSynapsesHistory[this.currentTimeStep] = newValue;
        return this.SP_activeSynapsesHistory;
    }

    public int[] getSP_activeColumnsHistoryAndAdd(int valueToAdd) {
        int newValue = this.SP_activeColumnsHistory[this.currentTimeStep] + valueToAdd;
        this.SP_activeColumnsHistory[this.currentTimeStep] = newValue;
        return this.SP_activeColumnsHistory;
    }

    public double[] getSP_inhibitionRadiusHistoryAndAdd(double valueToAdd) {
        double newValue = this.SP_inhibitionRadiusHistory[this.currentTimeStep] + valueToAdd;
        this.SP_inhibitionRadiusHistory[this.currentTimeStep] = newValue;
        return this.SP_inhibitionRadiusHistory;
    }

    public int[] getTP_synapsesHistoryAndAdd(int valueToAdd) {
        int newValue = this.TP_synapsesHistory[this.currentTimeStep] + valueToAdd;
        this.TP_synapsesHistory[this.currentTimeStep] = newValue;
        return this.TP_synapsesHistory;
    }

    public int[] getTP_distalSegmentsHistoryAndAdd(int valueToAdd) {
        int newValue = this.TP_distalSegmentsHistory[this.currentTimeStep] + valueToAdd;
        this.TP_distalSegmentsHistory[this.currentTimeStep] = newValue;
        return this.TP_distalSegmentsHistory;
    }

    public int[] getTP_activeDistalSegmentsHistoryAndAdd(int valueToAdd) {
        int newValue = this.TP_activeDistalSegmentsHistory[this.currentTimeStep] + valueToAdd;
        this.TP_activeDistalSegmentsHistory[this.currentTimeStep] = newValue;
        return this.TP_activeDistalSegmentsHistory;
    }

    public int[] getTP_sequenceSegmentsHistoryAndAdd(int valueToAdd) {
        int newValue = this.TP_sequenceSegmentsHistory[this.currentTimeStep] + valueToAdd;
        this.TP_sequenceSegmentsHistory[this.currentTimeStep] = newValue;
        return this.TP_sequenceSegmentsHistory;
    }

    public int[] getTP_learningNeuronsHistoryAndAdd(int valueToAdd) {
        int newValue = this.TP_learningNeuronsHistory[this.currentTimeStep] + valueToAdd;
        this.TP_learningNeuronsHistory[this.currentTimeStep] = newValue;
        return this.TP_learningNeuronsHistory;
    }

    public double[] getTP_predictionScoreHistoryAndAdd(double valueToAdd) {
        double newValue = this.TP_predictionScoreHistory[this.currentTimeStep] + valueToAdd;
        this.TP_predictionScoreHistory[this.currentTimeStep] = newValue;
        return this.TP_predictionScoreHistory;
    }

    /**
     * For more information please visit: https://github.com/WalnutiQ/WalnutiQ/issues/168
     */
    public double computePredictionScore(Set<ColumnPosition> activeColumnPositions, Set<ColumnPosition> predictiveColumnsAtTMinus1) {
        Set<ColumnPosition> activeColumns = activeColumnPositions;
        Set<ColumnPosition> activeAtTAndPredictiveAtTMinus1ColumnIntersection =
                new HashSet<>(activeColumns);

        activeAtTAndPredictiveAtTMinus1ColumnIntersection.retainAll(predictiveColumnsAtTMinus1);

        int numerator = activeAtTAndPredictiveAtTMinus1ColumnIntersection.size();
        int denominator = activeColumns.size();
        if (denominator == 0) {
            System.out.println("WARNING: current time step " + this.getCurrentTimeStep() + " of prediction score had" +
                    "0 active columns @ t and " + predictiveColumnsAtTMinus1.size()
                    + " predictive columns @ t-1");
            return 0;
        } else {
            return ((double) numerator) / ((double) denominator);
        }
    }
}