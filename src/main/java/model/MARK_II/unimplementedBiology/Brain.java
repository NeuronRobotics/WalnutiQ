package model.MARK_II.unimplementedBiology;

import model.MARK_II.Neocortex;

/**
 * Adult human brains contain about 10^11 neurons with 10^3 synapses each for a
 * total of about 10^14 synapses in the adult human brain.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 5, 2013
 */
public class Brain {
    private Cerebrum cerebrum;
    private Thalamus thalamus;

    // private Hypothalamus hypothalamus; = regulates basic needs, fighting,
    // fleeing, feeding, and mating

    // private Hindbrain hindbrain;
    // Medulla Oblongata = breathing, muscle tone and blood pressure
    // Pons = involved in sleep and arousal
    // Cerebellum = coordination and timing of voluntary movements,
    // sense of equilibrium, language, attention, ...

    // private Midbrain midbrain; = eye movements, visual and auditory reflexes

    // private ReticularFormation reticularFormation; = modulates muscle
    // reflexes, breathing, pain perception, sleep, arousal

    public Brain(Neocortex neocortex, LGN LGN) {
        this.cerebrum = new Cerebrum(neocortex);
        this.thalamus = new Thalamus(LGN);
        // this.hypothalamus = null;
        // this.hindbrain = null;
        // this.midbrain = null;
        // this.reticularFormation = null;
    }

    public Cerebrum getCerebrum() {
        return this.cerebrum;
    }

    public Thalamus getThalamus() {
        return this.thalamus;
    }
}
