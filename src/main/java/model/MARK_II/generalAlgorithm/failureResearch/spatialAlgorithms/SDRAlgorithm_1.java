package model.MARK_II.generalAlgorithm.failureResearch.spatialAlgorithms;

import model.MARK_II.generalAlgorithm.Pooler;
import model.MARK_II.region.Column;
import model.MARK_II.region.Region;

/**
 * This is an experimental SDR algorithm.
 *
 * The core idea of this algorithm is that it finds a FIXED percentage of the
 * most active columns and this is the Region's SDR. It also strengthens the
 * connections of synapses to active input cells and weakens the connections
 * of synapses to inactive input cells.
 *
 * This class solidfies these ideas into a deterministic SDR generation
 * algorithm that works okay. Understanding how this algorithm compares
 * to SDRAlgorithm_2(with inhibition) will allow us to understand when it is
 * appropriate or not appropriate to use inhibition in SDRAlgorithms.
 *
 * @author Q Liu (quinnliu@vt.edu)
 * @version 10/17/2015
 */
public class SDRAlgorithm_1 extends Pooler {
    private final double ACTIVITY_PERCENTAGE;

    public SDRAlgorithm_1(int numberOfTimesToRunAlgorithm, Region region, double activityPercentage) {
        super(numberOfTimesToRunAlgorithm);
        super.region = region;
        this.ACTIVITY_PERCENTAGE = activityPercentage;
    }

    /**
     * Call this method to run SDRAlgorithm_1 once on a Region.
     */
    public void run() {
        // Step 1) compute columns with most input activity @ t = 0
        //         using ACTIVITY_PERCENTAGE
        Column[][] columns = this.region.getColumns();
        for (int row = 0; row < columns.length; row++) {
            for (int column = 0; column < columns[0].length; column++) {
                //this.computeColumnOverlapScore(columns[row][column]);
            }
        }
        // TODO:
    }
}
