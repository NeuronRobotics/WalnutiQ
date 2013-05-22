package model;

import model.MARK_II.Neocortex;

import model.MARK_II.ConnectionType;
import model.MARK_II.Region;

/**
 * The Hindbrain
 *   - Medulla Oblongata = controls breathing, muscle tone and blood
 *                         pressure.
 *   - Pons = connected to the cerebellum & involved in sleep and
 *            arousal.
 *   - Cerebellum = coordination and timing of voluntary movements, sense
 *                  of equilibrium, language, attention, ...
 *
 *   - midbrain = controls eye movements, visual and auditory reflexes
 *   - reticular formation = modulates muscle reflexes, breathing & pain
 *                           perception. Also regulates sleep, wakefulness &
 *                           arousal. Near center of brain
 *   - Thalamus = "Relay station" for all sensory information(except smell)
 *                which is conveyed to the cortex. The thalamus also regulates
 *                sleep/wakefulness.
 *   - Hypothalamus = regulates basic needs(fighting, fleeing, feeding, and mating)
 *
 * Cerebrum
 *   - Cerebral cortex = layered sheet of neurons(30 billion neurons with
 *                       10,000 synapses each makes 300 trillion connections)
 *   - Neocortex = is the outer layer of the cerebral hemispheres. Made up of 6
 *                 layers, labelled I to VI(with VI being the innermost).
 *                 I = feedback from higher cortical layers
 *                 II+III = output to higher cortical layers
 *                 IV = input to neocortex from thalamus
 *                 V =
 *                 VI = output of neocortex back to thalamus
 *   - basal ganglia
 *   - hippocampus
 *   - amygdala
 */
public class Brain {
    private Cerebrum cerebrum;
    private Thalamus thalamus;
    private Hindbrain hindbrain;
    private Midbrain midbrain;
    private ReticularFormation reticularFormation;
    private Hypothalamus hypothalamus;

    public Brain(Region rootRegion,
	    ConnectionType neocortexRegionToNeocortexRegion,
	    int numberOfVisionCellsAlongXAxis, int numberOfVisionCellsAlongYAxis) {
	this.cerebrum = new Cerebrum(rootRegion, neocortexRegionToNeocortexRegion);
	this.thalamus = new Thalamus(numberOfVisionCellsAlongXAxis,
		numberOfVisionCellsAlongYAxis);
	this.hindbrain = null;
	this.midbrain = null;
	this.reticularFormation = null;
	this.hypothalamus = null;

	// The retina is connected to the LGN in the NervousSystem constructor.
	// The following connects the LGN to V1 region in neocortex using OverlapConnectFunctor.
	LateralGeniculateNucleus LGN = this.thalamus.getLGN();
	Neocortex neocortex = this.cerebrum.getCerebralCortex().getNeocortex();

    }

    public Cerebrum getCerebrum() {
	return this.cerebrum;
    }

    public Thalamus getThalamus() {
	return this.thalamus;
    }
}