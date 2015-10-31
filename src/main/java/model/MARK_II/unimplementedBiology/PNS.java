package model.MARK_II.unimplementedBiology;

import model.MARK_II.sensory.Retina;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 5, 2013
 */
public class PNS {
    private SNS SNS;

    /*
     * Works below the level of consciousness controlling
     * heart rate, digestion, respiratory rate, ...
     */
    // private AutonomicNervousSystem ANS;

    public PNS(Retina retine) {
        this.SNS = new SNS(retine);
        // this.ANS = null;
    }

    public SNS getSNS() {
        return this.SNS;
    }
}
