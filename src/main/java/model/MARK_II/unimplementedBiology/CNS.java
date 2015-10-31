package model.MARK_II.unimplementedBiology;

import model.MARK_II.Neocortex;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 5, 2013
 */
public class CNS {
    private Brain brain;

    /**
     * Things the spinal cord does for you:
     * 1) Local feedback loops control reflexes.
     * 2) Descending motor control signals from the
     * brain activae spinal motor neurons.
     * 3) Ascending sensory axons convey sensory
     * information (from muscles and skin) back to the brain.
     */
    // private SpinalCord spinalCord;
    public CNS(Neocortex neocortex, LGN LGN) {
        this.brain = new Brain(neocortex, LGN);
        // this.spinalCord = null;
    }

    public Brain getBrain() {
        return this.brain;
    }
}
