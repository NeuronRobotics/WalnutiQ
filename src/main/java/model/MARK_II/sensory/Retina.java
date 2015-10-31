package model.MARK_II.sensory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Input to Retina: images of different possible formats.
 *
 * Output of Retina: activity of Cells within VisionCells.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version Feb 3, 2014
 */
public class Retina {
    protected VisionCell[][] visionCells;

    public Retina(int numberOfVisionCellsAlongYAxis, int numberOfVisionCellsAlongXAxis) {
        this.visionCells = new VisionCell[numberOfVisionCellsAlongYAxis][numberOfVisionCellsAlongXAxis];

        for (int row = 0; row < numberOfVisionCellsAlongYAxis; row++) {
            for (int column = 0; column < numberOfVisionCellsAlongXAxis; column++) {
                this.visionCells[row][column] = new VisionCell();
            }
        }
    }

    public VisionCell[][] getVisionCells() {
        return this.visionCells;
    }

    /**
     * NOTE: When creating the input rectangle object remember that the point
     * parameters are (x, y) coordinates and not (row, column). For example:
     *
     * Rectangle rectangle = new Rectangle(new Point(x1_or_column1, y1_or_row1),
     *                                    new Point(x2_or_column2, y2_or_row2));
     *
     * @param rectangle The rectangle shape of VisionCells you want to
     *                  retrieve from the retina.
     * @return Partial 2D array of VisionCells in retina based on input rectangle
     *         dimensions.
     */
    public VisionCell[][] getVisionCells(model.MARK_II.util.Rectangle rectangle) {
        int rectangleWidth = rectangle.getWidth();
        int rectangleHeight = rectangle.getHeight();
        int largestColumnIndex = (int) rectangle.getBottomRightCorner().getX();
        int largestRowIndex = (int) rectangle.getBottomRightCorner().getY();
        if (rectangleWidth > this.visionCells[0].length ||
                rectangleHeight > this.visionCells.length ||
                largestColumnIndex > this.visionCells[0].length ||
                largestRowIndex > this.visionCells.length) {
            throw new IllegalArgumentException("In class Retina method " +
                    "getVisionCells the input parameter Rectangle" +
                    "is larger than the VisionCell[][] 2D array");
        }
        VisionCell[][] partialVisionCells = new VisionCell[rectangleHeight][rectangleWidth];
        int oldRowInitial = (int) rectangle.getTopLeftCorner().getY();
        int oldColumnInitial = (int) rectangle.getTopLeftCorner().getX();
        for (int row = 0; row < rectangleHeight; row++) {
            oldColumnInitial = (int) rectangle.getTopLeftCorner().getX();
            for (int column = 0; column < rectangleWidth; column++) {
                partialVisionCells[row][column] = this.visionCells[oldRowInitial][oldColumnInitial];
                oldColumnInitial++;
            }
            oldRowInitial++;
        }
        return partialVisionCells;
    }

    /**
     * Update the state of the OldRetina with the given .bmp file name.
     *
     * @param BMPFileName
     * @throws IOException
     */
    public void seeBMPImage(String BMPFileName) throws IOException {

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResource(BMPFileName));
        } catch (IOException e) {
            // nothing
        }

        int numberOfRows = this.visionCells.length;
        int numberOfColumns = this.visionCells[0].length;

        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {

                int RGBcolor = image.getRGB(column, row);

                if (RGBcolor == Color.BLACK.getRGB()) {
                    this.visionCells[row][column]
                            .setActiveState(true);
                } else {
                    this.visionCells[row][column]
                            .setActiveState(false);
                }
            }
        }
    }

    /**
     * @param image Any element in the 2D array with a value of zero will mean the
     *              corresponding VisionCell 2D array element will have
     *              activeState set to false. Any other number sets activeState to
     *              true.
     */
    public void see2DIntArray(int[][] image) {
        int numberOfRows = this.visionCells.length;
        int numberOfColumns = this.visionCells[0].length;
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                if (row >= image.length || column >= image[0].length) {
                    this.visionCells[row][column]
                            .setActiveState(false);
                } else if (image[row][column] != 0) {
                    this.visionCells[row][column]
                            .setActiveState(true);
                } else {
                    this.visionCells[row][column]
                            .setActiveState(false);
                }
            }
        }
    }
}
