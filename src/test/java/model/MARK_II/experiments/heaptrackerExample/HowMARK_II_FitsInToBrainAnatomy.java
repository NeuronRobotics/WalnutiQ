package model.MARK_II.experiments.heaptrackerExample;

import model.MARK_II.Neocortex;
import model.MARK_II.NervousSystem;
import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.MARK_II.connectTypes.RegionToRegionRectangleConnect;
import model.MARK_II.connectTypes.SensorCellsToRegionRectangleConnect;
import model.MARK_II.region.Layer5Region;
import model.MARK_II.region.Region;
import model.MARK_II.sensory.Retina;
import model.MARK_II.util.HeapTracker;
import model.MARK_II.util.Rectangle;

import java.awt.*;
import java.io.IOException;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @author Nathan Waggoner(nwagg14@vt.edu)
 * @version 5/26/2015
 */
public class HowMARK_II_FitsInToBrainAnatomy {

    private static HeapTracker heapTracker;

    public static void main(String[] args) throws IOException {
        System.out.println("Running HowMARK_II_FitsIntoBrainAnatomy.main() ...");

        heapTracker = new HeapTracker(true);

        NervousSystem partialNervousSystem = buildNervousSystem();

        // save all heap size data into a file
        //heapTracker.printAllHeapDataToFile("./src/test/java/model/experiments/vision/MARK_II/heaptrackerExample/heapSizeLogData_HowMARK_II_FitsInToBrainAnatomy.txt");

        System.out.println("Finished HowMARK_II_FitsIntoBrainAnatomy.main()");
    }

    /**
     * The following is a BIRDS EYE VIEW of a connected partial nervous system with a 3D drawing here:
     * https://github.com/WalnutiQ/WalnutiQ/issues/107
     *
     * LEGEND:
     * root, A, B, C, ... Z are Region names
     * M = parietal lobe region meaning it's neuron activity directly causes the Retina to move to it's new
     * position within the box Retina is stuck in.
     *
     *             root (runs just temporal pooling algorithm)
     *           /      \
     *          A        B (higher layer 3 runs spatial & temporal pooling)
     *          |        |
     *          C        D (higher Layer 4 runs spatial pooling)
     *         / \      / \
     * +<---- F   E    G   H (Layer 3 runs spatial pooling & temporal pooling)
     * |      |   |    |   |
     * M----->J   I    K   L (Layer 4 runs spatial pooling)
     * |      |   |    |   |
     * |       \  |    |  /
     * |  +-----\-|----|-/-----+
     * |  |      \|    |/      |
     * +--------> Retina       | <= box Retina is stuck in
     *    |                    |
     *    ImageRetinaIsLookingAt
     */
    private static NervousSystem buildNervousSystem() throws IOException {
        int fourNeurons = 4; // = neocortex layer 3 with 4 neurons per column
        int oneNeuron = 1; // = neocortex layer 4 with 1 neuron per column
        double PMO = 20; // = percent minimum overlap
        int DLA = 3; // = desired local activity

        // region
        heapTracker.updateHeapData();
        Region root = new Region("root", 60, 60, fourNeurons, PMO, DLA);
        heapTracker.updateHeapData();
        Region A = new Region("A", 60, 60, fourNeurons, PMO, DLA);
        Region B = new Region("B", 60, 60, fourNeurons, PMO, DLA);
        Region C = new Region("C", 125, 125, oneNeuron, PMO, DLA);
        Region D = new Region("D", 125, 125, oneNeuron, PMO, DLA);
        Region E = new Region("E", 125, 125, fourNeurons, PMO, DLA);
        Region F = new Region("F", 125, 125, fourNeurons, PMO, DLA);
        Region G = new Region("G", 125, 125, fourNeurons, PMO, DLA);
        Region H = new Region("H", 125, 125, fourNeurons, PMO, DLA);
        Region M = new Layer5Region("M", 125, 125, oneNeuron, PMO, DLA);
        Region I = new Region("I", 250, 250, oneNeuron, PMO, DLA);
        Region J = new Region("J", 250, 250, oneNeuron, PMO, DLA);
        Region K = new Region("K", 250, 250, oneNeuron, PMO, DLA);
        Region L = new Region("L", 250, 250, oneNeuron, PMO, DLA);
        heapTracker.updateHeapData();

        // connecting all region together
        Neocortex neocortex = new Neocortex(root, new RegionToRegionRectangleConnect());
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(30, 60)), A, 4, 4);
        neocortex.addToCurrentRegion(new Rectangle(new Point(30, 0), new
                Point(60, 60)), B, 4, 4);
        heapTracker.updateHeapData();

        neocortex.changeCurrentRegionTo("A");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(60, 60)), C, 4, 4);
        heapTracker.updateHeapData();

        neocortex.changeCurrentRegionTo("B");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(60, 60)), D, 4, 4);
        heapTracker.updateHeapData();

        neocortex.changeCurrentRegionTo("C");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(63, 125)), E, 4, 4);
        neocortex.addToCurrentRegion(new Rectangle(new Point(63, 0), new
                Point(125, 125)), F, 4, 4);
        heapTracker.updateHeapData();

        neocortex.changeCurrentRegionTo("D");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(63, 125)), G, 4, 4);
        neocortex.addToCurrentRegion(new Rectangle(new Point(63, 0), new Point(125, 125)), H, 4, 4);
        heapTracker.updateHeapData();

        neocortex.changeCurrentRegionTo("E");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(125, 125)), I, 4, 4);
        heapTracker.updateHeapData();

        neocortex.changeCurrentRegionTo("F");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(125, 125)), J, 4, 4);
        heapTracker.updateHeapData();

        neocortex.changeCurrentRegionTo("G");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(125, 125)), K, 4, 4);

        neocortex.changeCurrentRegionTo("H");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(125, 125)), L, 4, 4);
        heapTracker.updateHeapData();

        heapTracker.printAllHeapDataToFile("./src/test/java/model/experiments/vision/MARK_II/heaptrackerExample/heapSizeLogData_HowMARK_II_FitsInToBrainAnatomy.txt");
        // NOTE: comment out below code in this method to successfully run this
        // method on a computer with 1GB of heap size set.

        // connecting layer 5 region M
        neocortex.changeCurrentRegionTo("I");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(125, 125)), M, 4, 4);
        heapTracker.updateHeapData();
        neocortex.changeCurrentRegionTo("J");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(125, 125)), M, 4, 4);
        heapTracker.updateHeapData();
        neocortex.changeCurrentRegionTo("K");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(125, 125)), M, 4, 4);
        heapTracker.updateHeapData();
        neocortex.changeCurrentRegionTo("L");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(125, 125)), M, 4, 4);
        heapTracker.updateHeapData();

        neocortex.changeCurrentRegionTo("M");
        neocortex.addToCurrentRegion(new Rectangle(new Point(0, 0), new Point(125, 125)), F, 4, 4);
        heapTracker.updateHeapData();

        // NOTE: I, J, K, & L are connected to different parts of the same Retina
        Retina retina = new Retina(1000, 1000);
        heapTracker.updateHeapData();

        AbstractSensorCellsToRegionConnect opticNerve = new
                SensorCellsToRegionRectangleConnect();
        // now we can overlap
        opticNerve.connect(retina.getVisionCells(new Rectangle(new Point(0,
                0), new Point(500, 500))), I.getColumns(), 8, 8); //
                // .getVisionCells(topLeftPoint, bottomRightPoint)
        heapTracker.updateHeapData();
        opticNerve.connect(retina.getVisionCells(new Rectangle(new Point(500,
                0), new Point(1000, 500))), J.getColumns(), 8, 8);
        heapTracker.updateHeapData();
        opticNerve.connect(retina.getVisionCells(new Rectangle(new Point(0, 500), new Point(500, 1000))), K.getColumns(), 8, 8);
        heapTracker.updateHeapData();
        opticNerve.connect(retina.getVisionCells(new Rectangle(new Point(500, 500), new Point(1000, 1000))), L.getColumns(), 8, 8);
        heapTracker.updateHeapData();

        NervousSystem nervousSystem = new NervousSystem(neocortex, null, retina); // no LGN with circle surround input for now
        heapTracker.updateHeapData();

        return nervousSystem;
        //return null;
    }
}