package model.MARK_II.region;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides the base implementation for a DistalSegment and ProximalSegment.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @author Michael Cogswell (cogswell@vt.edu)
 * @version July 22, 2013
 */
public class Segment {
    protected Set<Synapse<Cell>> synapses;

    protected boolean isActive;

    /**
     * Minimal percent of active Synapses out of total Synapses needed for a
     * Segment to become active.
     */
    public static double PERCENT_ACTIVE_SYNAPSES_THRESHOLD = 0.2;

    /**
     * Provides three enums to be used as parameters in the method
     * updateSynapsePermanences(enumParameter). These 3 enums describe the three
     * different ways that Synapse permanences on a Segment can be updated.
     *
     * @author Quinn Liu (quinnliu@vt.edu)
     * @author Michael Cogswell (cogswell@vt.edu)
     */
    public enum SynapseUpdateState {
        /**
         * Increase permanence of Synapses with an active Cell on one Segment.
         */
        INCREASE_ACTIVE,

        /**
         * Increase permanence of all Synapses on one Segment.
         */
        INCREASE_ALL,

        /**
         * Decrease permanence of all Synapses on one Segment.
         */
        DECREASE_ALL
    }

    public Segment() {
        this.synapses = new HashSet<Synapse<Cell>>();
        this.isActive = false;
    }

    /**
     * @return true if this Segment has more active Synapses than the minimal
     * number of active Synapses needed to activate this Segment based
     * on PERCENT_ACTIVE_SYNAPSES_THRESHOLD.
     */
    public boolean getActiveState() {
        int numberOfActiveSynapses = 0;
        for (Synapse<Cell> synapse : this.synapses) {
            Cell abstractCell = synapse.getConnectedCell();
            if (synapse.isConnected() && abstractCell.getActiveState()) {
                numberOfActiveSynapses++;
            }
        }

        int minimalNumberOfActiveSynapses = (int) (this.synapses.size() * PERCENT_ACTIVE_SYNAPSES_THRESHOLD);

        if (numberOfActiveSynapses > minimalNumberOfActiveSynapses) {
            this.isActive = true;
            return true;
        } else {
            this.isActive = false;
            return false;
        }
    }

    /**
     * @param updateState This enum parameter determines how permanence of all Synapses
     *                    on a Segment will be updated.
     */
    public void updateSynapsePermanences(SynapseUpdateState updateState) {
        if (updateState == null) {
            throw new IllegalArgumentException(
                    "updateState in Segment method updateSynapsePermanences cannot be null");
        }
        for (Synapse<Cell> synapse : this.synapses) {
            switch (updateState) {
                case INCREASE_ACTIVE:
                    if (synapse.isConnected()
                            && synapse.getConnectedCell().getActiveState()) {
                        synapse.increasePermanence();
                    }
                    break;
                case INCREASE_ALL:
                    synapse.increasePermanence();
                    break;
                case DECREASE_ALL:
                    synapse.decreasePermanence();
                    break;
            }
        }
    }

    public void addSynapse(Synapse<Cell> synapse) {
        if (synapse == null) {
            throw new IllegalArgumentException(
                    "Synapse in Segment class method addSynapse cannot be null");
        }
        this.synapses.add((Synapse<Cell>) synapse);
    }

    public Set<Synapse<Cell>> getSynapses() {
        return this.synapses;
    }

    public int getNumberOfActiveSynapses() {
        int numberOfActiveSynapses = 0;
        for (Synapse synapse : this.synapses) {
            if (synapse.isConnected() && synapse.getConnectedCell().getActiveState()) {
                numberOfActiveSynapses++;
            }
        }
        return numberOfActiveSynapses;
    }

    public boolean removeSynapse(Synapse synapseToRemove) {
        for (Synapse synapse : this.synapses) {
            if (synapseToRemove.getConnectedCell().getClass()
                    .equals(synapse.getConnectedCell().getClass())
                    && synapseToRemove.getPermanenceValue() == synapse
                    .getPermanenceValue()
                    && synapseToRemove.getCellColumn() == synapse
                    .getCellColumn()
                    && synapseToRemove.getCellRow() == synapse
                    .getCellRow()) {
                this.synapses.remove(synapse);
                return true;
            }
        }
        return false;
    }

    public Synapse getSynapse(int cellXPosition, int cellYPosition) {
        for (Synapse synapse : this.synapses) {
            if (synapse.getCellColumn() == cellXPosition
                    && synapse.getCellRow() == cellYPosition) {
                return synapse;
            }
        }
        return null;
    }

    public Set<Synapse<Cell>> getConnectedSynapses() {
        Set<Synapse<Cell>> connectedSynapes = new HashSet<Synapse<Cell>>();

        for (Synapse<Cell> synapse : this.synapses) {
            if (synapse.getConnectedCell() != null) {
                connectedSynapes.add(synapse);
            }
        }

        return connectedSynapes;
    }
}
