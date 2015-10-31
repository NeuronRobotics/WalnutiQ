package model.MARK_II.generalAlgorithm.failedResearch.temporalAlgorithms;

import junit.framework.TestCase;
import model.MARK_II.generalAlgorithm.SpatialPooler;
import model.MARK_II.generalAlgorithm.failureResearch.temporalAlgorithms.PredictionAlgorithm_1;
import model.MARK_II.region.Region;

/**
 * @author Q Liu (quinnliu@vt.edu)
 * @version 9/20/2015
 */
public class PredictionAlgorithm_1Test extends TestCase {
    private PredictionAlgorithm_1 predictionAlgorithm_1;

    public void setUp() {
        SpatialPooler spatialPooler = new SpatialPooler(new Region("root", 6, 8, 4, 20.0, 3));
        this.predictionAlgorithm_1 = new PredictionAlgorithm_1(spatialPooler);
    }

    public void test_runOnce() {
        //this.predictionAlgorithm_1.runOnce();
    }
}
