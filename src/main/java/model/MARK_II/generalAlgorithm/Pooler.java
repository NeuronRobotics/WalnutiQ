package model.MARK_II.generalAlgorithm;

import model.MARK_II.region.Region;

/**
 * Abstract class extended by algorithm classes.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 26, 2013
 */
public abstract class Pooler {
    protected Region region;
    private boolean learningState;

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
}