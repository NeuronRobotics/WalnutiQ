package model.MARK_II.sensory;

/**
 * A data structure that represents a single VisionCell within the OldRetina.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 4, 2013
 */
public class VisionCell extends SensorCell {

    public VisionCell() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n===================");
        stringBuilder.append("\n--VisionCell Info--");
        stringBuilder.append("\n isActive: ");
        stringBuilder.append(super.getActiveState());
        stringBuilder.append("\nwasActive: ");
        stringBuilder.append(super.getPreviousActiveState());
        stringBuilder.append("\n===================");
        String visionCellInformation = stringBuilder.toString();
        return visionCellInformation;
    }
}
