package model.MARK_II.region;

import model.MARK_II.generalAlgorithm.ColumnPosition;
import model.MARK_II.util.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Node within Neocortex tree. A Region object is an undirected graph of Neuron
 * nodes.
 *
 * Input to Region: activity of Cells within a SensorCellLayer or lower Region.
 * For example, VisionCellLayer, AudioCellLayer, etc.
 *
 * Output from Region: activity of Cells within this Region created by one or
 * more of the Pooler generalAlgorithm.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version July 29, 2013
 */
public class Region {
    private String biologicalName;
    private List<Region> children;

    protected Column[][] columns;

    private double percentMinimumOverlapScore;

    // Example: if = 10, a column will be a winner if its overlapScore is > than
    // the overlapScore of the 10th highest column within its inhibitionRadius
    private int desiredLocalActivity;

    private int inhibitionRadius;

    public  Region(String biologicalName, int numberOfColumnsAlongRowsDimension,
                  int numberOfColumnsAlongColumnsDimension, int cellsPerColumn,
                  double percentMinimumOverlapScore, int desiredLocalActivity) {

        this.checkParameters(biologicalName, numberOfColumnsAlongRowsDimension,
                numberOfColumnsAlongColumnsDimension, cellsPerColumn,
                percentMinimumOverlapScore, desiredLocalActivity);

        this.biologicalName = biologicalName;
        this.children = new ArrayList<Region>();
        this.columns = new Column[numberOfColumnsAlongRowsDimension][numberOfColumnsAlongColumnsDimension];

        for (int row = 0; row < numberOfColumnsAlongRowsDimension; row++) {
            for (int column = 0; column < numberOfColumnsAlongColumnsDimension; column++) {
                this.columns[row][column] = new Column(cellsPerColumn,
                        new ColumnPosition(row, column));
            }
        }

        this.percentMinimumOverlapScore = percentMinimumOverlapScore;
        this.desiredLocalActivity = desiredLocalActivity;
        // NOTE: inhibitionRadius isn't set until regionLearnOneTimeStep()
        // but it is needed in computeColumnOverlapScore() of
        // performPooling(). Therefore the first time
        // inhibitionRadius used in computeColumnOverlapScore() it won't
        // really have a computed value.
        this.inhibitionRadius = 1;
    }

    private void checkParameters(String biologicalName,
                                 int numberOfColumnsAlongXAxis, int numberOfColumnsAlongYAxis,
                                 int cellsPerColumn, double percentMinimumOverlapScore,
                                 int desiredLocalActivity) {
        if (biologicalName == null) {
            throw new IllegalArgumentException(
                    "biologicalName in Region constructor cannot be null");
        } else if (numberOfColumnsAlongXAxis < 1) {
            throw new IllegalArgumentException(
                    "numberOfColumnsAlongXAxis in Region constructor cannot be less than 1");
        } else if (numberOfColumnsAlongYAxis < 1) {
            throw new IllegalArgumentException(
                    "numberOfColumnsAlongYAxis in Region constructor cannot be less than 1");
        } else if (cellsPerColumn < 1) {
            throw new IllegalArgumentException(
                    "cellsPerColumn in Region constructor cannot be less than 1");
        } else if (percentMinimumOverlapScore < 0
                || percentMinimumOverlapScore > 100) {
            throw new IllegalArgumentException(
                    "percentMinimumOverlapScore in Region constructor must be between 0 and 100");
        } else if (desiredLocalActivity < 0
                || desiredLocalActivity > (numberOfColumnsAlongXAxis * numberOfColumnsAlongYAxis)) {
            throw new IllegalArgumentException(
                    "desiredLocalActivity in Region constructor must be between 0 and the total number of columns within a region");
        }
    }

    public void addChildRegion(Region region) {
        if (region == null) {
            throw new IllegalArgumentException(
                    "region in Region method addChildRegion cannot be null");
        }
        this.children.add(region);
    }

    public List<Region> getChildRegions() {
        return this.children;
    }

    public Column[][] getColumns() {
        return this.columns;
    }

    public Column getColumn(int row, int column) {
        if (row < 0 || column < 0 || row >= this.columns.length
                || column >= this.columns[0].length) {
            throw new IllegalArgumentException("row & column in Region class method "
                    + "getColumn(int row, int column) are invalid");
        }
        return this.columns[row][column];
    }

    public String getBiologicalName() {
        return this.biologicalName;
    }

    public int getMinimumOverlapScore() {
        // this assumes all proximal Segments will have the same number of
        // Synapses

        return (int) (this.percentMinimumOverlapScore / 100 * columns[0][0]
                .getProximalSegment().getSynapses().size());
    }

    public int getDesiredLocalActivity() {
        return this.desiredLocalActivity;
    }

    public int getInhibitionRadius() {
        return this.inhibitionRadius;
    }

    public void setInhibitionRadius(int inhibitionRadius) {
        if (inhibitionRadius < 0 || inhibitionRadius > this.getNumberOfRowsAlongRegionYAxis()
                || inhibitionRadius > this.getNumberOfColumnsAlongRegionXAxis()) {
            throw new IllegalArgumentException(
                    "inhibition in Region class setInhibitionRadius method must "
                            + "be >= 0 and < the number of columns along boths sides of the region");
        }
        this.inhibitionRadius = inhibitionRadius;
    }

    void setPercentMinimumOverlapScore(double percentMinimumOverlapScore) {
        if (percentMinimumOverlapScore < 0 || percentMinimumOverlapScore > 100) {
            throw new IllegalArgumentException(
                    "percentMinimumOverlapScore in Region class "
                            + "setPercentMinimumOverlapScore method must be >= 0 and <= 100");
        }
        this.percentMinimumOverlapScore = percentMinimumOverlapScore;
    }

    public int getNumberOfRowsAlongRegionYAxis() {
        return this.columns.length;
    }

    public int getNumberOfColumnsAlongRegionXAxis() {
        return this.columns[0].length;
    }

    public int getNumberOfColumns() {
        return this.getNumberOfRowsAlongRegionYAxis() * this.getNumberOfColumnsAlongRegionXAxis();
    }

    public Dimension getBottomLayerXYAxisLength() {
        // get largest Synapse position of largest Column position
        Column columnWithLargestIndex = this.columns[this.columns.length - 1][this.columns[0].length - 1];

        // now find input layer x and y axis lengths whether the input layer
        // is a SensorCellLayer or a Region
        Set<Synapse<Cell>> synapses = columnWithLargestIndex
                .getProximalSegment().getSynapses();
        int greatestSynapseXIndex = 0;
        int greatestSynapseYIndex = 0;
        for (Synapse synapse : synapses) {
            if (synapse.getCellColumn() > greatestSynapseXIndex) {
                greatestSynapseXIndex = synapse.getCellColumn();
            }
            if (synapse.getCellRow() > greatestSynapseYIndex) {
                greatestSynapseYIndex = synapse.getCellRow();
            }
        }
        // you + 1 to each dimension because in the array the index begins at 0
        // instead of 1
        return new Dimension(greatestSynapseXIndex + 1,
                greatestSynapseYIndex + 1);
    }

    /**
     * Returns the maximum activeDutyCycle within a given ArrayList of Column
     * objects.
     *
     * @param neighborColumns A list of Column objects.
     * @return The maximum acitveDutyCycle of a Column object.
     */
    public float maximumActiveDutyCycle(List<Column> neighborColumns) {
        if (neighborColumns == null) {
            throw new IllegalArgumentException(
                    "neighborColumns in Column class method "
                            + "maximumActiveDutyCycle cannot be null");
        }
        float maximumActiveDutyCycle = 0.0f;
        for (Column column : neighborColumns) {
            if (column.getActiveDutyCycle() > maximumActiveDutyCycle) {
                maximumActiveDutyCycle = column.getActiveDutyCycle();
            }
        }
        return maximumActiveDutyCycle;
    }

    /**
     * @param rectangle Rectangle part of parent region to connect inclusively
     *                  (including first last and in between) to.
     * @return Partial 2D array of Columns in parent Region based on input rectangle
     *         dimensions.
     */
    public Column[][] getColumns(Rectangle rectangle) {
        int rectangleWidth = rectangle.getWidth();
        int rectangleHeight = rectangle.getHeight();
        int largestColumnIndex = (int) rectangle.getBottomRightCorner().getX();
        int largestRowIndex = (int) rectangle.getBottomRightCorner().getY();
        if (rectangleWidth > this.columns[0].length ||
                rectangleHeight > this.columns.length ||
                largestColumnIndex > this.columns[0].length ||
                largestRowIndex > this.columns.length) {
            throw new IllegalArgumentException("In class Region method " +
                "getColumns the input parameter Rectangle is larger than the" +
                    "Column[][] 2D array");
        }
        Column[][] partialColumns = new Column[rectangleHeight][rectangleWidth];
        int oldRowInitial = (int) rectangle.getTopLeftCorner().getY();
        int oldColumnInitial = (int) rectangle.getTopLeftCorner().getX();
        for (int row = 0; row < rectangleHeight; row++) {
            oldColumnInitial = (int) rectangle.getTopLeftCorner().getX();
            for (int column = 0; column < rectangleWidth; column++) {
                partialColumns[row][column] = this.columns[oldRowInitial][oldColumnInitial];
                oldColumnInitial++;
            }
            oldRowInitial++;
        }
        return partialColumns;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n==================================");
        stringBuilder.append("\n-----------Region Info------------");
        stringBuilder.append("\n       name of this region: ");
        stringBuilder.append(this.biologicalName);
        stringBuilder.append("\n     child region(s) names: ");
        for (Region region : this.children) {
            stringBuilder.append(region.biologicalName + ", ");
        }
        // do not show BinaryCellConnections
        stringBuilder.append("\n   # of Columns along Rows: ");
        stringBuilder.append(this.columns.length);
        stringBuilder.append("\n# of Columns along Columns: ");
        stringBuilder.append(this.columns[0].length);
        stringBuilder.append("\n 	           # of layers: ");
        stringBuilder.append(this.columns[0][0].getNeurons().length);
        stringBuilder.append("\npercentMinimumOverlapScore: ");
        stringBuilder.append(this.percentMinimumOverlapScore);
        stringBuilder.append(" %");
        stringBuilder.append("\n      desiredLocalActivity: ");
        stringBuilder.append(this.desiredLocalActivity);
        stringBuilder.append("\n          inhibitionRadius: ");
        stringBuilder.append(this.inhibitionRadius);
        stringBuilder.append("\n===================================");
        String regionInformation = stringBuilder.toString();
        return regionInformation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((biologicalName == null) ? 0 : biologicalName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Region other = (Region) obj;
        if (biologicalName == null) {
            if (other.biologicalName != null)
                return false;
        } else if (!biologicalName.equals(other.biologicalName))
            return false;
        return true;
    }
}
