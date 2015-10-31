package model.MARK_II.region;

import model.MARK_II.util.BoundingBox;
import model.MARK_II.util.Point3D;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version July 5, 2014
 */
public class Layer5Region extends Region {

    public Layer5Region(String biologicalName, int numberOfColumnsAlongRowsDimension,
                        int numberOfColumnsAlongColumnsDimension, int cellsPerColumn,
                        double percentMinimumOverlapScore, int desiredLocalActivity) {
        super(biologicalName, numberOfColumnsAlongRowsDimension, numberOfColumnsAlongColumnsDimension, cellsPerColumn,
                percentMinimumOverlapScore, desiredLocalActivity);
    }

    public Point3D getMotorOutput(BoundingBox boundingBox) {
        // NOTE: x = width = super.columns[0].length AND y = height = super.columns.length
        // split the Region x & y axis halfway along the x axis & only consider x less than this midpoint when
        // calculating averageRetinaX and averageRetinaY
        double regionWidth = super.columns[0].length / 2;
        double regionHeight = super.columns.length;

        double averageRegionXMultipliedByThisNumberEqualsNewBoundingBoxXPosition = boundingBox.getWidth() / regionWidth;
        double averageRegionYMultipliedByThisNumberEqualsNewBoundingBoxYPosition = boundingBox.getHeight() / regionHeight;
        double averageRegionZMultipliedByThisNumberEqualsNewBoundingBoxZPosition = boundingBox.getDepth() / regionHeight;

        double averageRegionX = 0;
        double averageRegionY = 0;
        int numberOfActiveAndPredictiveNeuronsForXY = 0;

        double averageRegionZ = 0;
        int numberOfActiveAndPredictiveNeuronsForZ = 0;


        for (int row = 0; row < super.columns.length; row++) {
            for (int column = 0; column < super.columns[0].length; column++) {
                for (Neuron neuron : super.columns[row][column].getNeurons()) {
                    if (neuron.getActiveState() == true || neuron.getPredictingState() == true) {
                        if (column < regionWidth) {
                            averageRegionX += column;
                            averageRegionY += row;
                            numberOfActiveAndPredictiveNeuronsForXY++;
                        } else {
                            // column >= regionWidth so use row variable to calculate averageRetinaZ
                            averageRegionZ += row;
                            numberOfActiveAndPredictiveNeuronsForZ++;
                        }
                    }
                }
            }
        }

        double newBoundingBoxXPosition = 0.0;
        double newBoundingBoxYPosition = 0.0;
        double newBoundingBoxZPosition = 0.0;

        if (averageRegionX != 0.0) {
            averageRegionX = averageRegionX / numberOfActiveAndPredictiveNeuronsForXY;
            newBoundingBoxXPosition = averageRegionX * averageRegionXMultipliedByThisNumberEqualsNewBoundingBoxXPosition;
        } else {
            // do not manipulate variable newBoundingBoxXPosition
            // this may be why people always look up when they are thinking hard since there is very
            // little neuronal activity in the region of the neocortex that activates the eye muscles causing
            // the eyes to move to the same default location...
        }

        if (averageRegionY != 0.0) {
            averageRegionY = averageRegionY / numberOfActiveAndPredictiveNeuronsForXY;
            newBoundingBoxYPosition = averageRegionY * averageRegionYMultipliedByThisNumberEqualsNewBoundingBoxYPosition;
        } else {
            // do not manipulate variable newBoundingBoxYPosition
        }

        if (averageRegionZ != 0.0) {
            averageRegionZ = averageRegionZ / numberOfActiveAndPredictiveNeuronsForZ;
            newBoundingBoxZPosition = averageRegionZ * averageRegionZMultipliedByThisNumberEqualsNewBoundingBoxZPosition;
        } else {
            // do not manipulate variable newBoundingBoxZPosition
        }

        return new Point3D(newBoundingBoxXPosition, newBoundingBoxYPosition, newBoundingBoxZPosition);
    }
}
