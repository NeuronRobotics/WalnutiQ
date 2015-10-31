package model.MARK_II.experiments.experiment_1;

import model.MARK_II.BigNeocortex;
import model.MARK_II.Neocortex;
import model.MARK_II.NervousSystem;
import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.MARK_II.connectTypes.RegionToRegionRectangleConnect;
import model.MARK_II.connectTypes.SensorCellsToRegionRectangleConnect;
import model.MARK_II.BigNervousSystem;
import model.MARK_II.generalAlgorithm.SpatialPooler;
import model.MARK_II.generalAlgorithm.TemporalPooler;
import model.MARK_II.region.Layer5Region;
import model.MARK_II.region.Region;
import model.MARK_II.sensory.BigRetina;
import model.MARK_II.sensory.ImageViewer;
import model.MARK_II.util.Point3D;

import java.awt.*;
import java.io.IOException;

/**
 * @author Q Liu (quinnliu@vt.edu)
 * @date 6/15/2015.
 */
public class Experiment_1 {

    private static String pathToExperiment_1_folder = "./src/test/java/model/experiments/vision/MARK_II" +
            "/experiment_1/";
    private static BigNervousSystem bigNervousSystem;
    /**
     * For saving the Java NervousSystem object as a JSON file later on.
     */
    private static SpatialPooler spatialPooler;
    private static TemporalPooler temporalPooler;

    public static void main(String[] args) throws IOException {
        System.out.println("Running Experiment_1.main() ...");

        bigNervousSystem = buildNervousSystem();

        BigNeocortex bigNeocortex = bigNervousSystem.getBigNeocortex();

        ImageViewer imageViewer = new ImageViewer("imageOfHumanFace1000x1000pixels.bmp",
                bigNervousSystem.getBigRetina().getSavedRetinaFromDisk());
        // NOTE: retina saved to disk will not be the most up to date state
        // the current retina as the new bits it sees will not be saved

        runForreal(bigNeocortex, imageViewer);

        System.out.println("Finished Experiment_1.main()");
    }

    private static BigNervousSystem buildNervousSystem() throws IOException {
        // ===================== Build Nervous System ==========================
        int maxSizeOfARegionInMB = 1024 + 512;
        // pass it an array of all Region names
        // Example List:
        // index_0 = root, 60, 60, 4, 20, 3
        // index_1 = A   , 60, 60, 4, 20, 3
        // ...

        String fourNeurons = "4"; // = neocortex layer 3 with 4 neurons per column
        String oneNeuron = "1"; // = neocortex layer 4 with 1 neuron per column
        String PMO = "20"; // = percent minimum overlap
        String DLA = "3"; // = desired local activity

        // NOTE: new region every 6 elements with keyword "Layer 5"
        // to create a Layer5Region.java class object
        String[] regionParameterListInOrder = {
                "root", "60", "60", fourNeurons, PMO, DLA,
                "A"   , "60", "60", fourNeurons, PMO, DLA,
                "B"   , "60", "60", fourNeurons, PMO, DLA,
                "C"   , "125", "125", oneNeuron, PMO, DLA,
                "D"   , "125", "125", oneNeuron, PMO, DLA,
                "E"   , "125", "125", fourNeurons, PMO, DLA,
                "F"   , "125", "125", fourNeurons, PMO, DLA,
                "G"   , "125", "125", fourNeurons, PMO, DLA,
                "H"   , "125", "125", fourNeurons, PMO, DLA,
                "layer 5 M"   , "125", "125", oneNeuron, PMO, DLA,
                "I"   , "250", "250", oneNeuron, PMO, DLA,
                "J"   , "250", "250", oneNeuron, PMO, DLA,
                "K"   , "250", "250", oneNeuron, PMO, DLA,
                "L"   , "250", "250", oneNeuron, PMO, DLA
        };

        // NOTE: new connection pattern every 7 elements or
        // change with 1 element with keywords "change to region REGION_NAME"
        String[] regionConnectionParameterListInOrder = {
                "0", "0", "30", "60", "A", "4", "4",
                "30", "0", "60", "60", "B", "4", "4",
                "change to region A",
                "0", "0", "60", "60", "C", "4", "4",
                "change to region B",
                "0", "0", "60", "60", "D", "4", "4",
                "change to region C",
                "0", "0", "63", "125", "E", "4", "4",
                "63", "0", "125", "125", "F", "4", "4",
                "change to region D",
                "0", "0", "63", "125", "G", "4", "4",
                "63", "0", "125", "125", "H", "4", "4",
                "change to region E",
                "0", "0", "125", "125", "I", "4", "4",
                "change to region F",
                "0", "0", "125", "125", "J", "4", "4",
                "change to region G",
                "0", "0", "125", "125", "K", "4", "4",
                "change to region H",
                "0", "0", "125", "125", "L", "4", "4",
                // connecting layer 5 region M
                "change to region I",
                "0", "0", "125", "125", "M", "4", "4",
                "change to region J",
                "0", "0", "125", "125", "M", "4", "4",
                "change to region K",
                "0", "0", "125", "125", "M", "4", "4",
                "change to region L",
                "0", "0", "125", "125", "M", "4", "4",
                "change to region M",
                "0", "0", "125", "125", "F", "4", "4"
        };

        String whereToSaveBigNeocortex = pathToExperiment_1_folder + "Experiment_1_Neocortex__0";
        BigNeocortex bigNeocortex = new BigNeocortex(maxSizeOfARegionInMB,
                regionParameterListInOrder, new RegionToRegionRectangleConnect(),
                regionConnectionParameterListInOrder, whereToSaveBigNeocortex);

        Dimension retinaDimension = new Dimension(1000, 1000);
        AbstractSensorCellsToRegionConnect opticNerve = new SensorCellsToRegionRectangleConnect();
        String[] retinaConnectionParameterListInOrder = {"0", "0", "500", "500", "I", "0", "0",
                "500", "0", "1000", "500", "J", "0", "0",
                "0", "500", "500", "1000", "K", "0", "0",
                "500", "500", "1000", "1000", "L", "0", "0"};

        String pathAndRetinaFileName = pathToExperiment_1_folder + "retina__0.json";
        BigNervousSystem bigNervousSystem = new BigNervousSystem(1024 + 512, bigNeocortex,
                retinaDimension, opticNerve, retinaConnectionParameterListInOrder,
                pathAndRetinaFileName);

        return bigNervousSystem;
    }

    public static void runForreal(BigNeocortex neocortex, ImageViewer imageViewer) throws IOException {
        spatialPooler = new SpatialPooler(neocortex.getRegion("I"));
        spatialPooler.setLearningState(true);

        temporalPooler = new TemporalPooler(spatialPooler, 25);
        temporalPooler.setLearningState(true);

        // initialize view
        imageViewer.saccadeRetinaToNewPositionAndGetWhatItSees(new Point3D(500, 500, 500));

        String[] secondLayer3RegionNames = {"A", "B"};
        String[] secondLayer4RegionNames = {"C", "D"};
        String[] firstLayer3RegionNames = {"E", "F", "G", "H"};
        String[] firstLayer4RegionNames = {"I", "J", "K", "L"};

        final int NUMBER_OF_TIMES_TO_RUN_LEARNING_ALGORITHM = 1;
        for (int i = 0; i < NUMBER_OF_TIMES_TO_RUN_LEARNING_ALGORITHM; i++) {

            for(String regionName : firstLayer4RegionNames){
                Region currRegion = neocortex.getRegion(regionName);
                spatialPooler.changeRegion(currRegion);
                spatialPooler.performPooling();
            }
            for(String regionName : firstLayer3RegionNames){
                Region currRegion = neocortex.getRegion(regionName);
                spatialPooler.changeRegion(currRegion);
                spatialPooler.performPooling();

                temporalPooler.performPooling();
                temporalPooler.nextTimeStep();
            }

            Layer5Region layer5Region = (Layer5Region) neocortex.getRegion("M");
            Point3D nextRetinaPosition = layer5Region
                    .getMotorOutput(imageViewer.getBoxRetinaIsStuckIn());

            imageViewer.saccadeRetinaToNewPositionAndGetWhatItSees(nextRetinaPosition);

            for(String regionName : secondLayer4RegionNames){
                Region currRegion = neocortex.getRegion(regionName);
                spatialPooler.changeRegion(currRegion);
                spatialPooler.performPooling();
            }
            for(String regionName : secondLayer3RegionNames){
                Region currRegion = neocortex.getRegion(regionName);
                spatialPooler.changeRegion(currRegion);
                spatialPooler.performPooling();

                temporalPooler.performPooling();
                temporalPooler.nextTimeStep();
            }

            spatialPooler.changeRegion(neocortex.getRegion("root"));
            temporalPooler.performPooling();
            temporalPooler.nextTimeStep();
        }
    }
}
