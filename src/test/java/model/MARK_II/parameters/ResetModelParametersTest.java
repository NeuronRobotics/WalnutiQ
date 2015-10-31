package model.MARK_II.parameters;

import model.MARK_II.region.Cell;
import model.MARK_II.region.Synapse;
import model.MARK_II.sensory.VisionCell;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 30, 2013
 */
public class ResetModelParametersTest extends junit.framework.TestCase {

    private Synapse synapse;

    public void setUp() {
        // NOTE: when first testing this class I used preset values for MARK I
        //       model without realizing I would want to change them in the
        //       future. The following are the preset values:
        Synapse.INITIAL_PERMANENCE = 0.3;
        Synapse.MINIMAL_CONNECTED_PERMANENCE = 0.2;
        Synapse.PERMANENCE_INCREASE = 0.02;
        Synapse.PERMANENCE_DECREASE = 0.005;

        this.synapse = new Synapse<Cell>(new VisionCell(), 0, 0);
    }

    public void test_reset() {
        assertEquals(0.3, this.synapse.getPermanenceValue());
        this.synapse.increasePermanence();
        assertEquals(0.32, this.synapse.getPermanenceValue());
        ResetModelParameters.reset(0.3, 0.005, 0.2, 0.3, 0.2, 0.005, 0.01);
        this.synapse.increasePermanence();
        assertEquals(0.62, this.synapse.getPermanenceValue());
    }
}
