package model.MARK_II.generalAlgorithm;

import model.MARK_II.region.Region;

/**
 * Abstract class extended by algorithm classes.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version 9/7/2015
 */
public abstract class Pooler {
    protected Region region;
    private boolean learningState;

    protected AlgorithmStatistics algorithmStatistics;

    public Pooler(int numberOfTimesToRunAlgorithm) {
        this.algorithmStatistics = new AlgorithmStatistics(numberOfTimesToRunAlgorithm);
    }

    public Pooler() {
        this.algorithmStatistics = new AlgorithmStatistics(AlgorithmStatistics.DEFAULT_NUMBER_OF_ALGORITHM_RUNS);
    }

    public void changeRegion(Region newRegion) {
        if (newRegion == null) {
            throw new IllegalArgumentException(
                    "newRegion in Pooler class changeRegion method cannot be null");
        }
        this.learningState = false;
        this.region = newRegion;
    }

    public boolean getLearningState() {
        return this.learningState;
    }

    public void setLearningState(boolean learningState) {
        this.learningState = learningState;
    }

    public Region getRegion() {
        return this.region;
    }

    public AlgorithmStatistics getAlgorithmStatistics() {
        return this.algorithmStatistics;
    }
}
