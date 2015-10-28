package model.MARK_II;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulates a cortical pyramidal cell.
 *  
 * A real Neuron has many different states 1) 3 spikes per second 2) 10+ spikes
 * per second 3) 0 spikes per second Support: https://db.tt/QFqA4Dta
 *  
 * However this Neuron model only models 2 different states:
 * 1) active Neuron = any kind of spiking activity for a real Neuron
 * (implemented in Cell.java class)
 *  
 * 2) predictive Neuron = depolarized real Neuron
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @author Michael Cogswell (cogswell@vt.edu)
 * @version July 22, 2013
 */
public class Neuron extends Cell {
    private boolean isPredicting;
    private boolean wasPredicting;

    private List<DistalSegment> distalSegments;

    public Neuron() {
        super();
        this.isPredicting = false;
        this.wasPredicting = false;

        this.distalSegments = new ArrayList<DistalSegment>();
    }

    public void nextTimeStep() {
        this.wasActive = super.isActive;
        super.isActive = false;

        this.wasPredicting = this.isPredicting;
        this.isPredicting = false;
    }

    public boolean getPredictingState() {
        return this.isPredicting;
    }

    public void setPredictingState(boolean predictingState) {
        this.isPredicting = predictingState;
    }

    public boolean getPreviousPredictingState() {
        return this.wasPredicting;
    }

    public void setPreviousPredictingState(boolean previousPredictingState) {
        this.wasPredicting = previousPredictingState;
    }

    /**
     * For the given column c cell i, return a segment index such that
     * segmentActive(s,t, state) is true. If multiple segments are active,
     * sequence segments are given preference. Otherwise, segments with most
     * activity are given preference.
     *  
     * Return a DistalSegment that was active in the previous time step. If more
     * than one DistalSegment was active, sequence segments are considered. If
     * more than one sequence segment was active, the segment with the most
     * activity is returned.
     */
    public DistalSegment getBestPreviousActiveSegment() {
        List<DistalSegment> previousActiveSegments = new ArrayList<DistalSegment>();
        List<DistalSegment> previousActiveSequenceSegment = new ArrayList<DistalSegment>();

        for (DistalSegment distalSegment : this.distalSegments) {
            if (distalSegment.getPreviousActiveState()
                    && distalSegment
                    .getSequenceStatePredictsFeedFowardInputOnNextStep()) {
                previousActiveSegments.add(distalSegment);
                previousActiveSequenceSegment.add(distalSegment);
            } else if (distalSegment.getPreviousActiveState()) {
                previousActiveSegments.add(distalSegment);
            }
        }

        if (previousActiveSegments.size() == 0) {
            return this.getPreviousSegmentWithMostActivity(this.distalSegments);

        } else if (previousActiveSegments.size() == 1) {
            return previousActiveSegments.get(0);

        } else { // previousActiveSegments.size() > 1

            if (previousActiveSequenceSegment.size() == 0) {
                return this
                        .getPreviousSegmentWithMostActivity(this.distalSegments);
            } else if (previousActiveSequenceSegment.size() == 1) {
                return previousActiveSequenceSegment.get(0);
            } else { // previousActiveSequenceSegments.size() > 1
                return this
                        .getPreviousSegmentWithMostActivity(previousActiveSequenceSegment);
            }
        }
    }

    DistalSegment getPreviousSegmentWithMostActivity(
            List<DistalSegment> whichSegmentsToCheck) {
        if (whichSegmentsToCheck.size() == 0) {
            DistalSegment newDistalSegment = new DistalSegment();
            this.addDistalSegment(newDistalSegment);
            return newDistalSegment;
        }
        DistalSegment mostActiveDistalSegment = this.distalSegments.get(0);

        int maxPreviousActiveSynapses = 0;
        for (DistalSegment distalSegment : whichSegmentsToCheck) {
            int previousActiveSynapses = distalSegment
                    .getNumberOfPreviousActiveSynapses();
            if (previousActiveSynapses > maxPreviousActiveSynapses) {
                maxPreviousActiveSynapses = previousActiveSynapses;
                mostActiveDistalSegment = distalSegment;
            }
        }
        if (maxPreviousActiveSynapses == 0) {
            // there were no previously active distal segments
            DistalSegment newDistalSegment = new DistalSegment();
            this.addDistalSegment(newDistalSegment);
            return newDistalSegment;
        }
        return mostActiveDistalSegment;
    }

    public DistalSegment getBestActiveSegment() {
        if (this.distalSegments.size() == 0) {
            DistalSegment newDistalSegment = new DistalSegment();
            this.addDistalSegment(newDistalSegment);
            return newDistalSegment;
        }

        DistalSegment mostActiveDistalSegment = this.distalSegments.get(0);

        int maxActiveSynapses = 0;
        for (DistalSegment distalSegment : this.distalSegments) {
            int activeSynapses = distalSegment.getNumberOfActiveSynapses();
            if (activeSynapses > maxActiveSynapses) {
                maxActiveSynapses = activeSynapses;
                mostActiveDistalSegment = distalSegment;
            }
        }

        return mostActiveDistalSegment;
    }

    public List<DistalSegment> getDistalSegments() {
        return this.distalSegments;
    }

    public void addDistalSegment(DistalSegment distalSegment) {
        if (distalSegment == null) {
            throw new IllegalArgumentException(
                    "distalSegment in neuron class method addDistalSegment cannot be null");
        }
        this.distalSegments.add(distalSegment);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Neuron other = (Neuron) obj;
        if (distalSegments == null) {
            if (other.distalSegments != null)
                return false;
        } else if (!distalSegments.equals(other.distalSegments))
            return false;
        if (isPredicting != other.isPredicting)
            return false;
        if (wasPredicting != other.wasPredicting)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n===========================");
        stringBuilder.append("\n--------Neuron Info--------");
        stringBuilder.append("\n           isActive: ");
        stringBuilder.append(this.isActive);
        stringBuilder.append("\n          wasActive: ");
        stringBuilder.append(this.wasActive);
        stringBuilder.append("\n       isPredicting: ");
        stringBuilder.append("\n===========================");
        String NeuronInformation = stringBuilder.toString();
        return NeuronInformation;
    }
}
