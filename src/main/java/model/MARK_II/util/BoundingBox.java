package model.MARK_II.util;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 30, 2014
 */
public class BoundingBox {
    private double width, height, depth;

    public BoundingBox(double width, double height, double depth) {
        if (width < 0 || height < 0 || depth < 0) {
            throw new IllegalArgumentException("In class BoundingBox constructor method one of the input parameters" +
                " are invalid because it is less than 0. Currently width = " + width + ", height = " + height +
                ", and depth = " + depth);
        }
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getDepth() {
        return this.depth;
    }
}
