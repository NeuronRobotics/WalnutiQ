package model.MARK_II.region;

import junit.framework.TestCase;
import model.MARK_II.generalAlgorithm.ColumnPosition;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version August 3, 2013
 */
public class ColumnTest extends TestCase {
    private Column column;

    public void setUp() {
        this.column = new Column(4, new ColumnPosition(0, 0));
    }

    public void test_Column() {
        try {
            Column column = new Column(0, new ColumnPosition(0, 0));
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals(
                    "numberOfCells in Column class constructor cannot be less than 1",
                    expected.getMessage());
        }

        assertFalse(this.column.getActiveState());

        Neuron[] neurons = this.column.getNeurons();
        for (Neuron neuron : neurons) {
            assertNotNull(neuron);
        }

        assertNotNull(this.column.getProximalSegment());

        assertEquals(0, this.column.getNeighborColumns().size());

        assertEquals(0, this.column.getOverlapScore());

        assertEquals(1.0, this.column.getBoostValue(), 0.01);

        assertEquals(1.0, this.column.getActiveDutyCycle(), 0.01);

        assertEquals(1.0, this.column.getOverlapDutyCycle(), 0.01);
    }

    public void test_updateActiveDutyCycle() {
        this.column.setActiveDutyCycle(0.995f);
        this.column.updateActiveDutyCycle();
        assertEquals(0.99002f, this.column.getActiveDutyCycle(), 0.00001);

        this.column.updateActiveDutyCycle();
        assertEquals(0.98507f, this.column.getActiveDutyCycle(), 0.00001);

        // Note this column's activeDutyCycle is reduced less and less with each
        // update
        this.column.updateActiveDutyCycle();
        assertEquals(0.98014f, this.column.getActiveDutyCycle(), 0.00001);

        // Note whenever updateActiveDutyCycle() is called, the activeDutyCycle
        // is always decremented less and less but only incremented if the
        // Column
        // was active. Furthermore, the increment applied to activeDutyCycle
        // when the Column is active is a constant representing the maximum
        // decrement of activeDutyCycle from initial value 1. Because of this
        // a Column's activeDutyCycle has a upper bound of 1.
        this.column.setActiveState(true);
        this.column.updateActiveDutyCycle();
        assertEquals(0.98024f, this.column.getActiveDutyCycle(), 0.0001);

        this.column.setActiveState(true);
        this.column.updateActiveDutyCycle();
        assertEquals(0.98034f, this.column.getActiveDutyCycle(), 0.0001);

        this.column.updateActiveDutyCycle();
        assertEquals(0.98044f, this.column.getActiveDutyCycle(), 0.0001);

        // a column's activeDutyCycle approaches 0.5
        this.column.setActiveDutyCycle(1.0f);
        for (int i = 0; i < 1000; i++) {
            // decrease activeDutyCycle
            this.column.setActiveState(false);
            this.column.updateActiveDutyCycle();

            // increase activeDutyCycle
            this.column.setActiveState(true);
            this.column.updateActiveDutyCycle();
        }
        assertEquals(0.50, this.column.getActiveDutyCycle(), 0.01);
    }

    public void test_boostFunction() {
        try {
            this.column.boostFunction(-0.1f);
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals(
                    "minimumDutyCycle in Column class boostFunction method cannot be <= 0",
                    expected.getMessage());
        }

        // if this.activeDutyCycle > minimumDutyCycle
        // the column's activeDutyCycle will not change and is returned
        this.column.setActiveDutyCycle(0.69f);
        assertEquals(0.69, this.column.boostFunction(0.6f), 0.001);

        this.column.setActiveDutyCycle(0.6f);
        assertEquals(1.149, this.column.boostFunction(0.69f), 0.001);
    }
}
