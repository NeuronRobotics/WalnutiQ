package model.MARK_II.experiments.experiment_0;

import junit.framework.TestCase;
import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.MARK_II.generalAlgorithm.ColumnPosition;
import model.MARK_II.generalAlgorithm.SpatialPooler;
import model.MARK_II.region.Region;
import model.MARK_II.connectTypes.SensorCellsToRegionRectangleConnect;
import model.MARK_II.sensory.Retina;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

/**
 * -------------------------------Purpose---------------------------------------
 * To show the spatial pooling learning algorithm is good at producing the same
 * output of neural activity even when the input is very noisy.
 *
 * ------------------------------Experiment-------------------------------------
 * Run the spatial pooling algorithm on 3 different bitmap images. The 3 images
 * are both of the same thing but 1 of the images has no noise, 1 image has some
 * noise, and 1 image has a lot of noise.
 *
 * ------------------------------Conclusion-------------------------------------
 * The spatial pooling algorithm does simple local computations on it's input to
 * remove noise very efficiently up to a specific threshold that can vary
 * between locations in the input.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version August 1, 2014
 */
public class NoiseInvarianceExperiment extends TestCase {
    private Retina retina;
    private Region region;
    private SpatialPooler spatialPooler;

    public void setUp() {
        // images this oldRetina will see are all 66x66 pixels
        this.retina = new Retina(66, 66);

        this.region = new Region("Region name", 8, 8, 1, 65, 2);
        this.region.setInhibitionRadius(3);

        AbstractSensorCellsToRegionConnect retinaToRegion = new SensorCellsToRegionRectangleConnect();
        retinaToRegion.connect(this.retina.getVisionCells(), this.region.getColumns(), 0, 0);

        this.spatialPooler = new SpatialPooler(this.region);
        this.spatialPooler.setLearningState(true);
    }

    public void test_NoiseInvarianceExperiment() throws IOException {

        // create columns that should be in the sets
        ColumnPosition cp1 = new ColumnPosition(6, 5);
        ColumnPosition cp2 = new ColumnPosition(6, 2);
        ColumnPosition cp3 = new ColumnPosition(2, 5);
        ColumnPosition cp4 = new ColumnPosition(1, 5);

        // create a set to test against the first two images
        Set<ColumnPosition> set1 = new HashSet<ColumnPosition>();
        set1.add(cp1);
        set1.add(cp2);
        set1.add(cp3);
        set1.add(cp4);

        // set to use for the final image
        Set<ColumnPosition> set2 = new HashSet<ColumnPosition>();
        set2.add(cp3);
        set2.add(cp4);

        // View all three images of digit 2 @ https://github.com/WalnutiQ/WalnutiQ#noise-invariance-experiment
        this.retina.seeBMPImage("2.bmp");
        this.spatialPooler.performPooling();
        // set1 = ((6, 5), (6, 2), (2, 5), (1, 5))
        TestCase.assertEquals(set1, this.spatialPooler.getActiveColumnPositions());

        this.retina.seeBMPImage("2_with_some_noise.bmp");
        this.spatialPooler.performPooling();
        // set1 = ((6, 5), (6, 2), (2, 5), (1, 5))
        TestCase.assertEquals(set1, this.spatialPooler.getActiveColumnPositions());

        this.retina.seeBMPImage("2_with_a_lot_of_noise.bmp");
        this.spatialPooler.performPooling();
        // when there is a lot of noise notice how the active columns are no longer the same?
        // set2 = ((2, 5), (1, 5))
        TestCase.assertEquals(set2, this.spatialPooler.getActiveColumnPositions());
    }
}
