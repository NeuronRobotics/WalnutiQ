package model.MARK_II.connectTypes;

import model.MARK_II.region.Column;
import model.MARK_II.region.Neuron;
import model.MARK_II.region.Synapse;
import model.MARK_II.region.Cell;

import java.awt.Point;

/**
 * @author Nathan Waggoner(nwagg14@vt.edu)
 * @author Quinn Liu(quinnliu@vt.edu)
 * @version 10/16/14
 */
public class RegionToRegionRectangleConnect extends
        AbstractRegionToRegionConnect {
    @Override
    public void connect(Column[][] bottomLayer, Column[][] topLayer,
                        int numberOfColumnsToOverlapAlongNumberOfRows,
                        int numberOfColumnsToOverlapAlongNumberOfColumns) {
        int rowBinitial, rowBfinal, colBinitial, colBfinal;

        int topRowLength = topLayer.length;
        int topColLength = topLayer[0].length;
        int botRowLength = bottomLayer.length;
        int botColLength = bottomLayer[0].length;

        RectangleConnect rectangleConnect = new RectangleConnect();

        for(int rowT = 0; rowT < topRowLength; rowT++){
            Point rowReceptiveField = rectangleConnect.updateReceptiveFieldDimensionLengthWithOverlap(topRowLength, botRowLength, rowT, numberOfColumnsToOverlapAlongNumberOfRows);
            rowBinitial = (int) rowReceptiveField.getX();
            rowBfinal = (int) rowReceptiveField.getY();

            for(int colT = 0; colT < topColLength; colT++) {
                Point colReceptiveField = rectangleConnect.updateReceptiveFieldDimensionLengthWithOverlap(topColLength, botColLength, colT, numberOfColumnsToOverlapAlongNumberOfColumns);
                colBinitial = (int) colReceptiveField.getX();
                colBfinal = (int) colReceptiveField.getY();

                // actually add synapses from bottom layer receptive field to top layer column
                Column topColumn = topLayer[rowT][colT];
                for (int rowB = rowBinitial; rowB <= rowBfinal; rowB++){
                    for (int colB = colBinitial; colB <= colBfinal; colB++) {
                        for(Neuron neuron : bottomLayer[rowB][colB].getNeurons()){
                            topColumn.getProximalSegment().addSynapse(new Synapse<Cell>(neuron, rowB, colB));
                        }
                    }
                }
            }
        }
    }
}
