package model.MARK_II.connectTypes;

import model.MARK_II.region.Column;
import model.MARK_II.region.Neuron;
import model.MARK_II.region.Synapse;
import model.MARK_II.region.Cell;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 19th, 2014
 */
public class RegionToRegionRandomConnect extends AbstractRegionToRegionConnect {

    @Override
    public void connect(Column[][] childRegionColumns, Column[][] parentRegionColumns,
                        int numberOfColumnsToOverlapAlongXAxisOfRegion,
                        int numberOfColumnsToOverlapAlongYAxisOfRegion) {

        super.checkParameters(childRegionColumns, parentRegionColumns,
                numberOfColumnsToOverlapAlongXAxisOfRegion,
                numberOfColumnsToOverlapAlongYAxisOfRegion);

        int parentRegionXAxisLength = parentRegionColumns.length; // = 8
        int parentRegionYAxisLength = parentRegionColumns[0].length; // = 8

        int childRegionXAxisLength = childRegionColumns.length; // = 66
        int childRegionYAxisLength = childRegionColumns[0].length; // = 66

        List<Point> allSynapsePositions = new ArrayList<Point>(
                childRegionXAxisLength * childRegionYAxisLength);
        // generate all possible Synapse positions
        for (int x = 0; x < childRegionXAxisLength; x++) {
            for (int y = 0; y < childRegionYAxisLength; y++) {
                allSynapsePositions.add(new Point(x, y));
            }
        }
        Collections.shuffle(allSynapsePositions);

        // ((2+66) * (2+66))/(8*8) = 68*68/64 = 72.25 > 66
        int numberOfSynapsesToAddToColumn = ((numberOfColumnsToOverlapAlongXAxisOfRegion + childRegionXAxisLength) * (numberOfColumnsToOverlapAlongYAxisOfRegion + childRegionYAxisLength))
                / (parentRegionXAxisLength * parentRegionYAxisLength);

        for (int parentColumnX = 0; parentColumnX < parentRegionXAxisLength; parentColumnX++) {
            for (int parentColumnY = 0; parentColumnY < parentRegionYAxisLength; parentColumnY++) {

                // add numberOfSynapsesForEachColumn Synapses to each Column
                // randomly
                Column parentColumn = parentRegionColumns[parentColumnX][parentColumnY];
                for (int i = 0; i < numberOfSynapsesToAddToColumn; i++) {
                    int randomSynapseXPosition = allSynapsePositions.get(i).x;
                    int randomSynapseYPosition = allSynapsePositions.get(i).y;

                    for (Neuron childColumnXYNeuron : childRegionColumns[randomSynapseXPosition][randomSynapseYPosition]
                            .getNeurons()) {
                        parentColumn.getProximalSegment().addSynapse(
                                new Synapse<Cell>(childColumnXYNeuron,
                                        randomSynapseXPosition,
                                        randomSynapseYPosition));
                    }
                }
            }
        }
    }
}
