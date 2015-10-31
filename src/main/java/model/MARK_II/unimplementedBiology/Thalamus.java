package model.MARK_II.unimplementedBiology;

/**
 * "Relay station" for all sensory information(except smell) which
 * is conveyed to the cortex.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 5, 2013
 */
public class Thalamus {
    private model.MARK_II.unimplementedBiology.LGN LGN;

    public Thalamus(LGN LGN) {
        this.LGN = LGN;
    }

    public LGN getLGN() {
        return this.LGN;
    }
}
