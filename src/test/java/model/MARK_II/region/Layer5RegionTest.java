package model.MARK_II.region;

import junit.framework.TestCase;
import model.MARK_II.util.BoundingBox;
import model.MARK_II.util.Point3D;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version July 5, 2014
 */
public class Layer5RegionTest extends TestCase {
    private Layer5Region layer5Region;

    public void setUp() {
        this.layer5Region = new Layer5Region("layer 5 region", 100, 200, 1, 20, 3);
    }

    public void test_getMotorOutputOnInactiveRegion() {
        assertEquals(new Point3D(0, 0, 0), this.layer5Region.getMotorOutput(new BoundingBox(66, 66, 33)));
    }

    public void test_shiftNewRetinaPositionToLeft() {
        this.layer5Region.getColumn(50, 1).getNeuron(0).setActiveState(true);

        Point3D newRetinaPosition = this.layer5Region.getMotorOutput(new BoundingBox(66, 66, 33));
        assertEquals(0.66, newRetinaPosition.getX(), 0.001);
        assertEquals(33, newRetinaPosition.getY(), 0.001);
        assertEquals(0, newRetinaPosition.getZ(), 0.001);
    }

    public void test_shiftNewRetinaPositionToRight() {
        this.layer5Region.getColumn(50, 99).getNeuron(0).setActiveState(true);

        Point3D newRetinaPosition = this.layer5Region.getMotorOutput(new BoundingBox(66, 66, 33));
        assertEquals(65.34, newRetinaPosition.getX(), 0.001);
        assertEquals(33, newRetinaPosition.getY(), 0.001);
        assertEquals(0, newRetinaPosition.getZ(), 0.001);
    }

    public void test_shiftNewRetinaPositionUp() {
        this.layer5Region.getColumn(1, 50).getNeuron(0).setActiveState(true);

        Point3D newRetinaPosition = this.layer5Region.getMotorOutput(new BoundingBox(66, 66, 33));
        assertEquals(33, newRetinaPosition.getX(), 0.001);
        assertEquals(0.66, newRetinaPosition.getY(), 0.001);
        assertEquals(0, newRetinaPosition.getZ(), 0.001);
    }

    public void test_shiftNewRetinaPositionDown() {
        this.layer5Region.getColumn(99, 50).getNeuron(0).setActiveState(true);

        Point3D newRetinaPosition = this.layer5Region.getMotorOutput(new BoundingBox(66, 66, 33));
        assertEquals(33, newRetinaPosition.getX(), 0.001);
        assertEquals(65.34, newRetinaPosition.getY(), 0.001);
        assertEquals(0, newRetinaPosition.getZ(), 0.001);
    }

    public void test_zoomInNewRetinaPosition() {
        this.layer5Region.getColumn(1, 150).getNeuron(0).setActiveState(true);

        Point3D newRetinaPosition = this.layer5Region.getMotorOutput(new BoundingBox(66, 66, 33));
        assertEquals(0, newRetinaPosition.getX(), 0.001);
        assertEquals(0, newRetinaPosition.getY(), 0.001);
        assertEquals(0.33, newRetinaPosition.getZ(), 0.001);

        // There should be no change along the newRetinaPositionZ when other neurons are active/predictive
        // since the X & Y positions are not used for calculating zooming in & zooming out.
        this.layer5Region.getColumn(1, 101).getNeuron(0).setActiveState(true);

        Point3D newRetinaPosition2 = this.layer5Region.getMotorOutput(new BoundingBox(66, 66, 33));
        assertEquals(0, newRetinaPosition2.getX(), 0.001);
        assertEquals(0, newRetinaPosition2.getY(), 0.001);
        assertEquals(0.33, newRetinaPosition2.getZ(), 0.001);
    }

    public void test_zoomOutNewRetinaPosition() {
        this.layer5Region.getColumn(99, 150).getNeuron(0).setActiveState(true);

        Point3D newRetinaPosition = this.layer5Region.getMotorOutput(new BoundingBox(66, 66, 33));
        assertEquals(0, newRetinaPosition.getX(), 0.001);
        assertEquals(0, newRetinaPosition.getY(), 0.001);
        assertEquals(32.67, newRetinaPosition.getZ(), 0.001);
    }
}