package model.MARK_II.region;

import junit.framework.TestCase;
import model.MARK_II.sensory.VisionCell;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 8, 2013
 */
public class SynapseTest extends TestCase {
    private VisionCell visionCell_1;
    private VisionCell visionCell_NULL;

    private Synapse<Cell> synapse_1;
    private Synapse<Cell> synapse_2;
    private Synapse<Cell> synapse_3;
    private Synapse<Cell> synapse_4;

    public void setUp() {
        this.visionCell_1 = new VisionCell();
        this.visionCell_NULL = null;

        this.synapse_1 = new Synapse<Cell>(this.visionCell_1, 1, 1);
        this.synapse_2 = new Synapse<Cell>(this.visionCell_1, 0.19, 1, 1);
    }

    public void test_SynapseStaticFields() {
        Synapse<Cell> synapse = new Synapse<Cell>(this.visionCell_1, 1, 1);
        assertEquals(Synapse.INITIAL_PERMANENCE, synapse.getPermanenceValue());
        synapse.increasePermanence();
        double newPermanence = Synapse.INITIAL_PERMANENCE + Synapse.PERMANENCE_INCREASE;
        assertEquals(newPermanence, synapse.getPermanenceValue(), 0.0001);

        Synapse.PERMANENCE_INCREASE = 0.2;
        synapse.increasePermanence();
        assertEquals(newPermanence + Synapse.PERMANENCE_INCREASE, synapse.getPermanenceValue(), 0.0001);
    }

    public void test_Synapse() {
        assertEquals(Synapse.INITIAL_PERMANENCE,
                this.synapse_1.getPermanenceValue(), 0.001);

        assertEquals(0.19, this.synapse_2.getPermanenceValue(), 0.001);

        try {
            this.synapse_3 = new Synapse<Cell>(this.visionCell_NULL, 0.21, 1, 1);
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals("cell in Synapse class constructor cannot be null",
                    expected.getMessage());
        }

        try {
            this.synapse_4 = new Synapse<Cell>(this.visionCell_1, 0.21, -1, 1);
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals(
                    "cellColumn and cellRow in Synapse class constructor must be > 0",
                    expected.getMessage());
        }

        try {
            this.synapse_4 = new Synapse<Cell>(this.visionCell_1, -0.1, 1, 1);
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals(
                    "initialPermanence in Synapse class constructor must be between 0 and 1",
                    expected.getMessage());
        }

        try {
            this.synapse_4 = new Synapse<Cell>(this.visionCell_1, 1.1, 1, 1);
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals(
                    "initialPermanence in Synapse class constructor must be between 0 and 1",
                    expected.getMessage());
        }
    }

    public void test_isConnected() {
        assertTrue(this.synapse_1.isConnected());
        assertFalse(this.synapse_2.isConnected());
    }

    public void test_increasePermanence() {
        this.synapse_1.increasePermanence();
        assertEquals(Synapse.INITIAL_PERMANENCE + Synapse.PERMANENCE_INCREASE,
                synapse_1.getPermanenceValue(), 0.0001);

        this.synapse_1.setPermanenceValue(0.999);
        this.synapse_1.increasePermanence();
        assertEquals(1.0, this.synapse_1.getPermanenceValue(), 0.0001);
    }

    public void test_decreasePermanence() {
        this.synapse_1.decreasePermanence();
        assertEquals(Synapse.INITIAL_PERMANENCE - Synapse.PERMANENCE_DECREASE,
                synapse_1.getPermanenceValue(), 0.0001);

        this.synapse_1.setPermanenceValue(0.001);
        this.synapse_1.decreasePermanence();
        assertEquals(0.0, this.synapse_1.getPermanenceValue(), 0.0001);
    }

    public void test_equals() {
        assertFalse(this.synapse_1.equals(this.synapse_2));

        this.synapse_2.setPermanenceValue(Synapse.INITIAL_PERMANENCE);
        assertTrue(this.synapse_1.equals(this.synapse_2));

        Synapse<Cell> synapse_3 = new Synapse<Cell>(new Neuron(), 1, 1);
        Synapse<Neuron> synapse_4 = new Synapse<Neuron>(new Neuron(), 1, 1);
        assertFalse(this.synapse_1.equals(synapse_3));
        assertFalse(this.synapse_1.equals(synapse_4));
    }
}
