package model.MARK_II.generalAlgorithm;

import model.MARK_II.region.Cell;
import model.MARK_II.region.Synapse;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 25, 2014
 */
class SegmentUpdate {
    private Set<Synapse<Cell>> synapsesWithActiveCells;
    private Set<Synapse<Cell>> synapsesWithDeactiveCells;

    /**
     * same as sequenceSegment
     */
    private boolean predictsFeedForwardInputOnNextTimeStep;

    private ColumnPosition neuronColumnPosition;
    private int neuronIndex;

    private Set<Synapse<Cell>> synapsesNonexistentInModel;

    public SegmentUpdate(Set<Synapse<Cell>> synapsesWithActiveCells,
                         Set<Synapse<Cell>> synapsesWithDeactiveCells,
                         ColumnPosition neuronColumnPosition, int neuronIndex) {
        this.synapsesWithActiveCells = synapsesWithActiveCells;
        this.synapsesWithDeactiveCells = synapsesWithDeactiveCells;
        this.predictsFeedForwardInputOnNextTimeStep = false;

        this.neuronColumnPosition = neuronColumnPosition;
        this.neuronIndex = neuronIndex;

        this.synapsesNonexistentInModel = new HashSet<Synapse<Cell>>();
    }

    public ColumnPosition getNeuronColumnPosition() {
        return this.neuronColumnPosition;
    }

    public int getNeuronIndex() {
        return this.neuronIndex;
    }

    public Set<Synapse<Cell>> getSynapsesWithActiveCells() {
        return this.synapsesWithActiveCells;
    }

    public Set<Synapse<Cell>> getSynpasesWithDeactiveCells() {
        return this.synapsesWithDeactiveCells;
    }

    public void setSequenceState(boolean predictsFeedForwardInputOnNextTimeStep) {
        this.predictsFeedForwardInputOnNextTimeStep = predictsFeedForwardInputOnNextTimeStep;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n==========================================");
        stringBuilder.append("\n------------SegmentUpdate Info------------");
        stringBuilder.append("\n          synapsesWithActiveCells size: ");
        stringBuilder.append(this.synapsesWithActiveCells.size());
        stringBuilder.append("\n        synapsesWithDeactiveCells size: ");
        stringBuilder.append(this.synapsesWithDeactiveCells.size());
        stringBuilder.append("\npredictsFeedForwardInputOnNextTimeStep: ");
        stringBuilder.append(this.predictsFeedForwardInputOnNextTimeStep);
        stringBuilder.append("\n    neuronColumnPosition (row, column): (");
        stringBuilder.append(this.neuronColumnPosition.getRow() + ", " + this.neuronColumnPosition.getColumn() + ")");
        stringBuilder.append("\n                           neuronIndex: ");
        stringBuilder.append(this.neuronIndex);
        stringBuilder.append("\n       synapsesNonexistentInModel size: ");
        stringBuilder.append(this.synapsesNonexistentInModel.size());
        stringBuilder.append("\n==========================================");
        String NeuronInformation = stringBuilder.toString();
        return NeuronInformation;
    }
}
