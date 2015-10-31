package model.MARK_II.region;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 25, 2014
 */
public class DistalSegment extends Segment {
    /**
     * same as sequenceSegment
     */
    private boolean predictsFeedForwardInputOnNextTimeStep;

    private boolean wasActive;

    public DistalSegment() {
        super();
        this.predictsFeedForwardInputOnNextTimeStep = false;
        this.wasActive = false;
    }

    public void nextTimeStep() {
        this.predictsFeedForwardInputOnNextTimeStep = false;

        this.wasActive = super.isActive;
        //super.isActive = false; Although it may be obvious to put
        // this line here it is incorrect becuase Segment recalculates
        // it's activeState based on current state of it's Synapses
    }

    public boolean getSequenceStatePredictsFeedFowardInputOnNextStep() {
        return this.predictsFeedForwardInputOnNextTimeStep;
    }

    public void setSequenceState(boolean predictsFeedForwardInputOnNextTimeStep) {
        this.predictsFeedForwardInputOnNextTimeStep = predictsFeedForwardInputOnNextTimeStep;
    }

    public void setPreviousActiveState(boolean wasActive) {
        this.wasActive = wasActive;
    }

    public boolean getPreviousActiveState() {
        return this.wasActive;
    }

    public int getNumberOfPreviousActiveSynapses() {
        int numberOfPreviousActiveSynapses = 0;
        for (Synapse synapse : this.synapses) {
            if (synapse.isConnected()
                    && synapse.getConnectedCell().getPreviousActiveState()) {
                numberOfPreviousActiveSynapses++;
            }
        }
        return numberOfPreviousActiveSynapses;
    }
}
