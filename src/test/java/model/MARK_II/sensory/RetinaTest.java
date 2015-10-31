package model.MARK_II.sensory;

import junit.framework.TestCase;
import model.MARK_II.util.Rectangle;

import java.awt.*;
import java.io.IOException;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version Feb 9, 2014
 */
public class RetinaTest extends TestCase {
    private Retina retina;

    public void setUp() {
        this.retina = new Retina(5, 15); // 5 rows 15 columns
    }

    public void test_getVisionCells() throws IOException {
        this.retina.seeBMPImage("Array2DTest.bmp");

        try {
            model.MARK_II.util.Rectangle partialRetinaWanted1 = new Rectangle(new Point(0, 0), new Point(16, 2));
            VisionCell[][] partialVisionCells1 = this.retina.getVisionCells(partialRetinaWanted1);
            fail("should've thrown an exception!");
        } catch (IllegalArgumentException expected) {
            assertEquals("In class Retina method getVisionCells the input " +
                            "parameter Rectangleis larger than the " +
                            "VisionCell[][] 2D array",
                    expected.getMessage());
        }

        Rectangle partialRetinaWanted2 = new Rectangle(new Point(0, 0), new Point(7, 2));
        VisionCell[][] partialVisionCells2 = this.retina.getVisionCells(partialRetinaWanted2);
        int numberOfRows = partialVisionCells2.length;
        int numberOfColumns = partialVisionCells2[0].length;
        assertEquals(2, numberOfRows);
        assertEquals(7, numberOfColumns);
        TestCase.assertTrue(partialVisionCells2[1][3].getActiveState());
        TestCase.assertFalse(partialVisionCells2[0][3].getActiveState());

        this.retina.seeBMPImage("Array2DTest2.bmp");
        TestCase.assertFalse(partialVisionCells2[1][3].getActiveState());
        TestCase.assertTrue(partialVisionCells2[0][3].getActiveState());

        this.retina.seeBMPImage("Array2DTest3.bmp");
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 7; column++) {
                TestCase.assertTrue(partialVisionCells2[row][column]
                        .getActiveState());
            }
        }
    }

    public void test_seeBMPImage() throws IOException {
        /**
         * Array2DTest.bmp =
         * 000000000000000
         * 000100000000000
         * 000000000000000
         * 000000000000000
         * 000000000000000
         */
        this.retina.seeBMPImage("Array2DTest.bmp");

        VisionCell[][] visionCells = this.retina.getVisionCells();
        int numberOfRows = visionCells.length;
        int numberOfColumns = visionCells[0].length;
        assertEquals(5, numberOfRows);
        assertEquals(15, numberOfColumns);
        TestCase.assertTrue(visionCells[1][3].getActiveState());
        TestCase.assertFalse(visionCells[0][3].getActiveState());

        this.retina.seeBMPImage("Array2DTest2.bmp");
        VisionCell[][] visionCells2 = this.retina.getVisionCells();
        /**
         * Array2DTest2.bmp =
         * 000100000000000 NOTE: visionCells2[0][3] = 1 where before it = 0
         * 000000000000000
         * 000000000000000
         * 000000000000000
         * 000000000000000
         */
        int numberOfRows2 = visionCells2.length;
        int numberOfColumns2 = visionCells2[0].length;
        assertEquals(5, numberOfRows2);
        assertEquals(15, numberOfColumns2);
        TestCase.assertFalse(visionCells2[1][3].getActiveState());
        TestCase.assertTrue(visionCells2[0][3].getActiveState());
    }
}
