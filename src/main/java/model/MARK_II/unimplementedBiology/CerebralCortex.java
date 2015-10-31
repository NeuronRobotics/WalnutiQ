package model.MARK_II.unimplementedBiology;

import model.MARK_II.Neocortex;


/**
 * A layered sheet of neurons. About 1/8th inch thick with approximately 30 billion
 * neurons. Each neuron with about 10,000 synapses.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 5, 2013
 */
public class CerebralCortex {
    private Neocortex neocortex;
    // private Paleocortex paleocortex;
    // private Archicortex archicortex;

    public CerebralCortex(Neocortex neocortex) {
        this.neocortex = neocortex;
        // this.paleocortex = null;
        // this.archicortex = null;
    }

    public Neocortex getNeocortex() {
        return this.neocortex;
    }
}
