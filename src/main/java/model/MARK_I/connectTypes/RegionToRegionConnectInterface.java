package main.java.model.MARK_I.connectTypes;

import main.java.model.MARK_I.Region;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 7, 2013
 */
public interface RegionToRegionConnectInterface {
    public abstract void connect(Region childRegion, Region parentRegion,
	    int numberOfColumnsToOverlapAlongXAxisOfRegion,
	    int numberOfColumnsToOverlapAlongYAxisOfRegion);
}
