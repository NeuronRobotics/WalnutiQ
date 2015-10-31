package model.MARK_II.region;

/**
 * Input to ProximalSegment: activity of active Synapses connected to this Segment.
 *  
 * Output from ProximalSegment: whether or not this Segment is currently active.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version July 22, 2013
 */
public class ProximalSegment extends Segment {

    public ProximalSegment() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n====================================");
        stringBuilder.append("\n-------ProximalSegment Info---------");
        stringBuilder.append("\n                active state: ");
        stringBuilder.append(this.getActiveState());
        stringBuilder.append("\n       previous active state: ");
        stringBuilder.append("\n    number of total Synapses: ");
        stringBuilder.append(this.synapses.size());
        stringBuilder.append("\nminimum activation threshold: ");
        stringBuilder
                .append((int) (this.synapses.size() * PERCENT_ACTIVE_SYNAPSES_THRESHOLD));
        stringBuilder.append("\n   number of active Synapses: ");
        stringBuilder.append(this.getNumberOfActiveSynapses());
        stringBuilder.append("\n=====================================");
        String columnInformation = stringBuilder.toString();
        return columnInformation;
    }
}
