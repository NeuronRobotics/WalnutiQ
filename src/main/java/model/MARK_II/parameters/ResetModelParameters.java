package model.MARK_II.parameters;

import model.MARK_II.region.Column;
import model.MARK_II.region.Segment;
import model.MARK_II.generalAlgorithm.SpatialPooler;
import model.MARK_II.region.Synapse;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 30, 2013
 */
public class ResetModelParameters {
    public static void reset(double PERMANENCE_INCREASE,
                             double PERMANENCE_DECREASE, double MINIMAL_CONNECTED_PERMANENCE,
                             double INITIAL_PERMANENCE,
                             double PERCENT_ACTIVE_SYNAPSES_THRESHOLD,
                             double EXPONENTIAL_MOVING_AVERAGE_AlPHA,
                             double MINIMUM_COLUMN_FIRING_RATE) {
        Synapse.PERMANENCE_INCREASE = PERMANENCE_INCREASE;
        Synapse.PERMANENCE_DECREASE = PERMANENCE_DECREASE;
        Synapse.MINIMAL_CONNECTED_PERMANENCE = MINIMAL_CONNECTED_PERMANENCE;
        Synapse.INITIAL_PERMANENCE = INITIAL_PERMANENCE;

        Segment.PERCENT_ACTIVE_SYNAPSES_THRESHOLD = PERCENT_ACTIVE_SYNAPSES_THRESHOLD;
        Column.EXPONENTIAL_MOVING_AVERAGE_AlPHA = (float) EXPONENTIAL_MOVING_AVERAGE_AlPHA;
        SpatialPooler.MINIMUM_COLUMN_FIRING_RATE = (float) MINIMUM_COLUMN_FIRING_RATE;
    }
}
