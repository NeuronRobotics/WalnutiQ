package model.MARK_II.parameters;

import mnist.tools.MnistManager;
import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.MARK_II.generalAlgorithm.ColumnPosition;
import model.MARK_II.region.Region;
import model.MARK_II.generalAlgorithm.SpatialPooler;
import model.MARK_II.connectTypes.SensorCellsToRegionRectangleConnect;
import model.MARK_II.sensory.Retina;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Set;

/**
 * Why: There are about a dozen parameters that are very important to how the
 * neurons in the model interact with each other. We need to find the best value
 * for these parameters to allow the brain algorithms to work efficiently.
 *  
 * What: This class contains many different ways a partial brain model can be
 * constructed.
 *  
 * How: A optimization algorithm will call 1 of the methods below over and over
 * until it has found parameters that produce the best score.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version Apr 21, 2014
 */
public class FindOptimalParametersForSDR {

    /**
     * Builds a simple 1 OldRetina to 1 Region model with given parameters, runs
     * the spatial pooling algorithm once, computes a score based on the output
     * of the spatial pooling algorithm.
     *
     * @param percentMinimumOverlapScore
     * @param desiredLocalActivity
     * @param desiredPercentageOfActiveColumns
     * @param locationOfFileWithFileNameToSaveScore
     * @return The SDR score.
     * @throws IOException
     */
    public static double printToFileSDRScoreFor1RetinaTo1RegionModelFor1Digit(
            double percentMinimumOverlapScore, double desiredLocalActivity,
            double desiredPercentageOfActiveColumns,
            String locationOfFileWithFileNameToSaveScore) throws IOException {
        Retina retina = new Retina(66, 66);
        Region region = new Region("Region", 8, 8, 1,
                percentMinimumOverlapScore, (int) desiredLocalActivity);

        AbstractSensorCellsToRegionConnect retinaToRegion = new SensorCellsToRegionRectangleConnect();
        retinaToRegion.connect(retina.getVisionCells(), region.getColumns(), 0, 0);

        SpatialPooler spatialPooler = new SpatialPooler(region);
        spatialPooler.setLearningState(true);

        retina.seeBMPImage("2.bmp");

        spatialPooler.performPooling(); // 11 active columns
        Set<ColumnPosition> columnActivityAfterSeeingImage2 = spatialPooler
                .getActiveColumnPositions();
        // = (6,5)(6, 3)(6, 2)(5, 3)(3, 5)(2, 2)(1, 3)(1, 2)(2, 5)(1, 5)(4, 4)

        // -----------------------compute SDR score----------------------------
        int totalNumberOfColumnsInRegion = region.getNumberOfRowsAlongRegionYAxis()
                * region.getNumberOfColumnsAlongRegionXAxis();
        SDRScoreCalculator sdrScoreCalculator = new SDRScoreCalculator(
                columnActivityAfterSeeingImage2,
                desiredPercentageOfActiveColumns, totalNumberOfColumnsInRegion);

        double SDRScore = sdrScoreCalculator.computeSDRScore();

        NumberFormat formatter = new DecimalFormat("0.################E0");

        // print SDRScore to file
        try {
            BufferedWriter out2 = new BufferedWriter(new FileWriter(
                    locationOfFileWithFileNameToSaveScore));
            out2.write(formatter.format(SDRScore));
            out2.close();
        } catch (IOException e) {

        }

        return SDRScore;
    }

    /**
     * Builds a simple 1 OldRetina to 1 Region model with given parameters, runs
     * the spatial pooling algorithm once, computes a score based on the output
     * of the spatial pooling algorithm.
     *
     * @param percentMinimumOverlapScore
     * @param desiredLocalActivity
     * @param desiredPercentageOfActiveColumns
     * @param locationOfFileWithFileNameToSaveScore
     * @return The average SDR score.
     * @throws IOException
     */
    public static double printToFileAverageSDRScoreFor1RetinaTo1RegionModelForAllDigitsInMNIST(
            double percentMinimumOverlapScore, double desiredLocalActivity,
            double desiredPercentageOfActiveColumns,
            String locationOfFileWithFileNameToSaveScore) throws IOException {
        MnistManager mnistManager = new MnistManager(
                "./images/digits/MNIST/t10k-images.idx3-ubyte",
                "./images/digits/MNIST/t10k-labels.idx1-ubyte");

        // all images in MNIST dataset are 28 x 28 pixels
        Retina retina = new Retina(28, 28);
        Region region = new Region("Region", 8, 8, 1,
                percentMinimumOverlapScore, (int) desiredLocalActivity);

        AbstractSensorCellsToRegionConnect retinaToRegion = new SensorCellsToRegionRectangleConnect();
        retinaToRegion.connect(retina.getVisionCells(), region.getColumns(), 0, 0);

        SpatialPooler spatialPooler = new SpatialPooler(region);
        spatialPooler.setLearningState(true);

        int numberOfImagesToSee = 1000;
        double totalSDRScore = 0.0;
        for (int i = 1; i < (numberOfImagesToSee + 1); i++) {
            mnistManager.setCurrent(i);
            int[][] image = mnistManager.readImage();

            retina.see2DIntArray(image);
            spatialPooler.performPooling();
            Set<ColumnPosition> columnActivityAfterSeeingCurrentMNISTImage = spatialPooler
                    .getActiveColumnPositions();

            // compute SDR score
            int totalNumberOfColumnsInRegion = region.getNumberOfRowsAlongRegionYAxis()
                    * region.getNumberOfColumnsAlongRegionXAxis();
            SDRScoreCalculator sdrScoreCalculator = new SDRScoreCalculator(
                    columnActivityAfterSeeingCurrentMNISTImage,
                    desiredPercentageOfActiveColumns,
                    totalNumberOfColumnsInRegion);

            double SDRScore = sdrScoreCalculator.computeSDRScore();
            totalSDRScore += SDRScore;
        }

        double averageSDRScore = totalSDRScore / numberOfImagesToSee;

        NumberFormat formatter = new DecimalFormat("0.################E0");

        // print averageSDRScore to file
        try {
            BufferedWriter out2 = new BufferedWriter(new FileWriter(
                    locationOfFileWithFileNameToSaveScore));
            out2.write(formatter.format(averageSDRScore));
            out2.close();
        } catch (IOException e) {

        }

        return averageSDRScore;
    }
}
