package model.MARK_II.generalAlgorithm;

import junit.framework.TestCase;

/**
 * @author Q Liu (quinnliu@vt.edu)
 * @version 7/9/2015
 */
public class AlgorithmStatisticsTest extends TestCase {
    private AlgorithmStatistics algorithmStatistics;

    public void setUp() {
        this.algorithmStatistics = new AlgorithmStatistics(3);
    }

    public void test_addingHistory() {
        for (int i = 0; i < 3; i++) {
            this.algorithmStatistics.getSP_activeSynapsesHistoryAndAdd(i);
            if (i == 1) {
                this.algorithmStatistics.getSP_activeSynapsesHistoryAndAdd(10);
            }
            this.algorithmStatistics.nextTimeStep();
        }

        int[] expected = new int[] {0, 11, 2};

        int[] actual = this.algorithmStatistics.getSP_activeSynapsesHistoryAndAdd(0);
        for (int i = 0; i < 3; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
