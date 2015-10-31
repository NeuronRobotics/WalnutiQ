package model.MARK_II;

import model.MARK_II.sensory.Retina;
import model.MARK_II.unimplementedBiology.CNS;
import model.MARK_II.unimplementedBiology.LGN;
import model.MARK_II.unimplementedBiology.PNS;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 5, 2013
 */
public class NervousSystem {
    private model.MARK_II.unimplementedBiology.CNS CNS;
    private model.MARK_II.unimplementedBiology.PNS PNS;

    public NervousSystem(Neocortex neocortex, LGN lgn, Retina retina) {
        this.CNS = new CNS(neocortex, lgn);
        this.PNS = new PNS(retina);
    }

    public CNS getCNS() {
        return this.CNS;
    }

    public PNS getPNS() {
        return this.PNS;
    }
}
