package model.MARK_II.parameters;

import model.MARK_II.Region;
import model.MARK_II.SpatialPooler;
import model.MARK_II.TemporalPooler;
import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.MARK_II.connectTypes.SensorCellsToRegionRectangleConnect;
import model.Retina;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version Apr 30, 2014
 */
public class FindOptimalParametersForSPandTP {

    /**
     * Ranges for each variable
     *
     * @param percentMinimumOverlapScore            0 - 100
     * @param desiredLocalActivity                  0 - (region width / 4)
     * @param desiredPercentageOfActiveColumns      0 - 100
     * @param newSynapseCount                       1 - 1000
     * @param numberOfIterations                    1000 - infinity :)
     * @param PERMANENCE_INCREASE                   0.001 - 0.999
     * @param PERMANENCE_DECREASE                   0.001 - 0.999
     * @param MINIMAL_CONNECTED_PERMANENCE          0.1 - 0.9
     * @param INITIAL_PERMANENCE                    Greater than MINIMAL_CONNECTED_PERMANENCE 0.11 - 0.91
     * @param PERCENT_ACTIVE_SYNAPSES_THRESHOLD     0.1 - 0.9
     * @param EXPONENTIAL_MOVING_AVERAGE_AlPHA      0.001 - 0.9
     * @param MINIMUM_COLUMN_FIRING_RATE            0.01 - 0.9
     * @param locationOfFileWithFileNameToSaveScore
     * @return SPandTPScore
     * @throws IOException
     */
    public static double printToFileSPandTPScoreFor1RetinaTo1RegionModelFor1Digit(
            double percentMinimumOverlapScore, double desiredLocalActivity,
            double desiredPercentageOfActiveColumns, double newSynapseCount,
            double numberOfIterations, double PERMANENCE_INCREASE,
            double PERMANENCE_DECREASE, double MINIMAL_CONNECTED_PERMANENCE,
            double INITIAL_PERMANENCE,
            double PERCENT_ACTIVE_SYNAPSES_THRESHOLD,
            double EXPONENTIAL_MOVING_AVERAGE_AlPHA,
            double MINIMUM_COLUMN_FIRING_RATE,
            String locationOfFileWithFileNameToSaveScore) throws IOException {

        ResetModelParameters.reset(PERMANENCE_INCREASE, PERMANENCE_DECREASE,
                MINIMAL_CONNECTED_PERMANENCE, INITIAL_PERMANENCE,
                PERCENT_ACTIVE_SYNAPSES_THRESHOLD,
                EXPONENTIAL_MOVING_AVERAGE_AlPHA, MINIMUM_COLUMN_FIRING_RATE);

        Retina retina = new Retina(66, 66);
        Region region = new Region("Region", 8, 8, 4,
                percentMinimumOverlapScore, (int) desiredLocalActivity);

        AbstractSensorCellsToRegionConnect retinaToRegion = new SensorCellsToRegionRectangleConnect();
        retinaToRegion.connect(retina.getVisionCells(), region.getColumns(), 0, 0);

        SpatialPooler spatialPooler = new SpatialPooler(region);
        spatialPooler.setLearningState(true);
        TemporalPooler temporalPooler = new TemporalPooler(spatialPooler,
                (int) newSynapseCount);
        temporalPooler.setLearningState(true);

        retina.seeBMPImage("2.bmp");

        int totalNumberOfSequenceSegments = 0;
        int totalNumberOfLearningNeurons = 0;
        for (int i = 0; i < (int) numberOfIterations; i++) {
            spatialPooler.performPooling();
            temporalPooler.performPooling();

            temporalPooler.getLearningAlgorithmStatistics().updateModelLearningMetrics(temporalPooler.getRegion());
            totalNumberOfSequenceSegments += temporalPooler.getLearningAlgorithmStatistics()
                    .getTotalNumberOfSequenceSegmentsInCurrentTimeStep();
            totalNumberOfLearningNeurons += temporalPooler
                    .getNumberOfCurrentLearningNeurons();
            temporalPooler.nextTimeStep();
        }

        // --------------------compute SPandTP score----------------------------

        double SPandTPScore = -(totalNumberOfSequenceSegments + totalNumberOfLearningNeurons)
                / numberOfIterations;

        NumberFormat formatter = new DecimalFormat("0.################E0");

        // print SPandTPScore to file
        try {
            BufferedWriter out2 = new BufferedWriter(new FileWriter(
                    locationOfFileWithFileNameToSaveScore));
            out2.write(formatter.format(SPandTPScore));
            out2.close();
        } catch (IOException e) {

        }

        return SPandTPScore;
    }

    public static double printToFileSPandTPScoreFor1RetinaTo9RegionModelFor5Digits() {
        double SPandTPscore = 0.0;

        // construct model
        //SaccadingRetina retina = null;
        //OldImageViewer oldImageViewer = null;

        // spatialPooler.performSpatialPooling();
        // temporalPooler.performSpatialPooling();

        // exact shift in current retina position and zoom level from
        // region representing parietal lobe

        //retina.setDistanceBetweenImageAndRetina(1);
        Point2D retinaLocation = new Point();
        retinaLocation.setLocation(2.0, 2.0);
        //retina.setPosition(retinaLocation);

        // oldImageViewer.updateRetinaWithSeenPartOfImageBasedOnCurrentPosition();

        // spatialPooler.performSpatialPooling();
        // temporalPooler.performSpatialPooling();
        // ... again

        // show image 0
        // call spatial & temporal pooling

        // show image 1
        // call spatial & temporal pooling

        // show image 2
        // call spatial & temporal pooling

        // show image 3
        // call spatial & temporal pooling

        // show image 4
        // call spatial & temporal pooling

        // show image 5
        // call spatial & temporal pooling

        // good SPandTPScore when showing same 0-6 images over and over again
        // 1) the more sequence segments there are
        // 2) the more predicting neurons there are
        // 3) the less newSynapses there are NOT a good metric as it is a
        // input parameter

        return SPandTPscore;
    }
}
