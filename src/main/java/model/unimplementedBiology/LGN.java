package model.unimplementedBiology;

import model.MARK_II.Region;

/**
 * Input into LGN: activity of Cells within OldRetina.
 *  
 * Output of LGN: activity of Neurons from this Region.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 5, 2013
 */
public class LGN {
    private Region region;

    public LGN(Region region) {
        // how should desiredLocalActivity be calculated
        this.region = region;
    }

    public Region getRegion() {
        return this.region;
    }
}
