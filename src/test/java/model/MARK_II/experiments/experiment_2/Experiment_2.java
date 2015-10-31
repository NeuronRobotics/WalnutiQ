package model.MARK_II.experiments.experiment_2;

import junit.framework.TestCase;
import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.MARK_II.connectTypes.SensorCellsToRegionRectangleConnect;
import model.MARK_II.generalAlgorithm.ColumnPosition;
import model.MARK_II.generalAlgorithm.SpatialPooler;
import model.MARK_II.generalAlgorithm.TemporalPooler;
import model.MARK_II.region.Column;
import model.MARK_II.region.Region;
import model.MARK_II.region.Segment;
import model.MARK_II.region.Synapse;
import model.MARK_II.sensory.Retina;
import model.MARK_II.util.RegionConsoleViewer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

/**
 * @author Q Liu (quinnliu@vt.edu)
 * @date 7/7/2015.
 */
public class Experiment_2 extends TestCase {
    private static String pathToExperiment_2_folder = "./src/test/java/model" +
            "/MARK_II/experiments/experiment_2";

    private Retina retina;
    private Region region;
    private SpatialPooler spatialPooler;
    private TemporalPooler temporalPooler;

    public void setUp() {
        // NOTE: the following parameters are borrowed from numenta/nupic TP example
        // here: https://github.com/numenta/nupic/blob/master/examples/tp/hello_tp.py

        //    NUPIC                                     WalnutiQ
        // ==============================================================
        // numberOfCols =    50                         6*8 = 48
        int cellsPerColumn =   2; //                    rest all the same
        double initialPerm =   0.5;
        double connectedPerm = 0.5;
        int minThreshold =    10;
        int newSynapseCount = 10;
        double permanenceInc =   0.1;
        double permanenceDec =   0.0;

        Synapse.INITIAL_PERMANENCE = initialPerm;
        Synapse.MINIMAL_CONNECTED_PERMANENCE = connectedPerm;
        Synapse.PERMANENCE_INCREASE = permanenceInc;
        Synapse.PERMANENCE_DECREASE = permanenceDec;

        Segment.PERCENT_ACTIVE_SYNAPSES_THRESHOLD = 0.2;

        // Step 1: create Temporal Pooler instance with appropriate parameters
        this.retina = new Retina(10, 10);
        this.region = new Region("root", 6, 8, cellsPerColumn, minThreshold, 3);
        region.setInhibitionRadius(3);

        AbstractSensorCellsToRegionConnect retinaToRegion = new SensorCellsToRegionRectangleConnect();
        retinaToRegion.connect(this.retina.getVisionCells(), this.region.getColumns(),
                0, 0);

        this.spatialPooler = new SpatialPooler(this.region, 51);
        this.spatialPooler.setLearningState(true);

        this.temporalPooler = new TemporalPooler(this.spatialPooler, newSynapseCount);
        this.temporalPooler.setLearningState(true);

        // Step 2: create input images to feed to the temporal pooler. Here we
        // create a simple sequence of 5 images of letters: A -> B -> C -> D -> E
        // NOTE: images are in folder WalnutiQ/images/model/MARK_II/experiments/experiment_2
    }

    public void test_CLA() throws IOException {
        // Step 3: send this simple sequence to the temporal pooler for learning
        // we repeat the sequence 10 times

        for (int i = 0; i < 10; i++) {
            this.retina.seeBMPImage("A.bmp");
            runCLA();

            this.retina.seeBMPImage("B.bmp");
            runCLA();

            this.retina.seeBMPImage("C.bmp");
            runCLA();

            this.retina.seeBMPImage("D.bmp");
            runCLA();

            this.retina.seeBMPImage("E.bmp");
            runCLA();
        }

        this.retina.seeBMPImage("A.bmp");
        runCLA_noNextTimeStep();

        this.temporalPooler.saveCurrentRegionAlgorithmStatistics
                (pathToExperiment_2_folder);

        System.out.println("Expect to see active columns for 'A' ");
        RegionConsoleViewer.printDoubleCharArray(RegionConsoleViewer
                .getColumnActiveStatesCharArray(this.region));

        char[][] columnActiveStates = RegionConsoleViewer
                .getColumnActiveStatesCharArray(this.region);
        assertEquals("aaiiiiii\n" +
                        "aaiiiiii\n" +
                        "aaaiiiii\n" +
                        "iiiiiiii\n" +
                        "iiiiiiii\n" +
                        "iiiiiiii",
                RegionConsoleViewer.doubleCharArrayAsString(columnActiveStates));

        System.out.println("\n\nExpect to see predictive columns for 'B'");
        RegionConsoleViewer.printDoubleCharArray(RegionConsoleViewer
                .getColumnPredictiveStatesCharArray(this.region));

        // so above assertions can be made with current time step
        // columns with predicting neurons
        this.temporalPooler.nextTimeStep();

//        this.retina.seeBMPImage("B.bmp");
//        runCLA_noNextTimeStep();
//        System.out.println("Expect to see active columns for 'B' ");
//        RegionConsoleViewer.printDoubleCharArray(RegionConsoleViewer
//                .getColumnActiveStatesCharArray(this.region));
//
//        System.out.println("\n\nExpect to see predictive columns for 'C'");
//        RegionConsoleViewer.printDoubleCharArray(RegionConsoleViewer
//                .getColumnPredictiveStatesCharArray(this.region));
//        this.temporalPooler.nextTimeStep();

        // NOTE: repeat for remaining letters.
    }

    public void runCLA() {
        this.spatialPooler.performPooling();

        this.temporalPooler.performPooling();
        this.temporalPooler.nextTimeStep();
    }

    public void runCLA_noNextTimeStep() {
        this.spatialPooler.performPooling();
        this.temporalPooler.performPooling();
    }

    void printColumnPositions(Set<ColumnPosition> columnPositions, String type) {
        System.out.print(type + " column positions = ");
        for (ColumnPosition cp : columnPositions) {
            System.out.print("(" + cp.getRow() + "," + cp.getColumn() + "), ");
        }
        System.out.print("\n");
    }
}