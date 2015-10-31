package model.MARK_II.util;

import junit.framework.TestCase;

import java.awt.*;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version July 13, 2014
 */
public class RectangleTest extends TestCase {
    private Rectangle rectangle;

    public void setUp() {
        this.rectangle = new Rectangle(new Point(1, 0), new Point(5, 10));
    }

    public void test_getWidth() {
        assertEquals(4, this.rectangle.getWidth());
    }

    public void test_getHeight() {
        assertEquals(10, this.rectangle.getHeight());
    }

    public void test_setTopLeftCorner() {
        this.rectangle.setTopLeftCorner(new Point(4, 9));
        assertEquals(4, (int) this.rectangle.getTopLeftCorner().getX());
        assertEquals(9, (int) this.rectangle.getTopLeftCorner().getY());

        try {
            this.rectangle.setTopLeftCorner(new Point(-1, 0));
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals("In class Rectangle isNewTopLeftCornerValid method the input point must be >= 0",
                    expected.getMessage());
        }

        try {
            this.rectangle.setTopLeftCorner(new Point(6, 10));
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals("In class Rectangle isNewTopLeftCornerValid method the input point is not to the top left of the current bottom right point",
                    expected.getMessage());
        }
    }

    public void test_setBottomRightCorner() {
        this.rectangle.setBottomRightCorner(new Point(2, 1));
        assertEquals(2, (int) this.rectangle.getBottomRightCorner().getX());
        assertEquals(1, (int) this.rectangle.getBottomRightCorner().getY());

        try {
            this.rectangle.setBottomRightCorner(new Point(-1, 3));
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals("In class Rectangle isNewBottomRightCornerValid method the input point must be >= 0",
                    expected.getMessage());
        }

        try {
            this.rectangle.setBottomRightCorner(new Point(1, 1));
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals("In class Rectangle isNewBottomRightCornerValid method the input point is not to the bottom right of the current top left point",
                    expected.getMessage());
        }
    }
}
