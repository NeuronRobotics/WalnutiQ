package model.MARK_II.generalAlgorithm;

import model.MARK_II.region.Cell;
import model.MARK_II.region.Column;
import model.MARK_II.region.Region;
import model.MARK_II.region.Synapse;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Provides implementation for running the spatial learning algorithm on a
 * Region. This class provides methods that simulate brain activity within
 * Regions of the Neocortex.
 *
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version July 29, 2013
 */
public class SpatialPooler extends Pooler {
    private Set<Column> activeColumns;
    private Set<ColumnPosition> activeColumnPositions;

    public static float MINIMUM_COLUMN_FIRING_RATE = 0.01f;

    public SpatialPooler(Region region, int numberOfTimesToRunAlgorithm) {
        super(numberOfTimesToRunAlgorithm);

        if (region == null) {
            throw new IllegalArgumentException(
                    "region in SpatialPooler class constructor cannot be null");
        }
        super.region = region;
        this.activeColumns = new HashSet<>();
        this.activeColumnPositions = new HashSet<>();
    }

    public SpatialPooler(Region region) {
        this(region, AlgorithmStatistics.DEFAULT_NUMBER_OF_ALGORITHM_RUNS);
    }

    /**
     * This method recomputes all Column states within this functional Region.
     * Through local inhibition, only a sparse set of Columns become active to
     * represent the current 2D array of sensory data or a lower Region's
     * output.
     *
     * @return A sparse set of active Columns within this Region.
     */
    public Set<Column> performPooling() {
        /// for c in columns
        Column[][] columns = this.region.getColumns();
        for (int row = 0; row < columns.length; row++) {
            for (int column = 0; column < columns[0].length; column++) {
                this.computeColumnOverlapScore(columns[row][column]);
            }
        }

        // a sparse set of Columns become active after local inhibition
        this.computeActiveColumnsOfRegion();

        // simulate learning by boosting specific Synapses
        this.regionLearnOneTimeStep();

        return this.activeColumns;
    }

    /**
     * If only the column positions computed by spatial pooling are needed use
     * this method to return a set of just the column positions that were active
     * in the most recent iteration of spatial pooling. For example instead of
     * using:
     *
     * Set Column columnActivity =
     * spatialPooler.performPooling();
     *
     * Now use:
     *
     * spatialPooler.performPooling();
     * Set ColumnPosition columnActivity = this.spatialPooler.getActiveColumnPositions();
     */
    public Set<ColumnPosition> getActiveColumnPositions() {
        return this.activeColumnPositions;
    }

    public Set<Column> getActiveColumns() {
        return this.activeColumns;
    }

    /**
     * The overlapScore for each Column is the number of Synapses connected to
     * Cells with active inputs multiplied by that Columns's boostValue. If a
     * Column's overlapScore is below minOverlap, that Column's overlapScore is
     * set to 0.
     */
    void computeColumnOverlapScore(Column column) {
        if (column == null) {
            throw new IllegalArgumentException(
                    "the Column in SpatialPooler method computeColumnOverlapScore cannot be null");
        }

        /// overlap(c) = 0
        int newOverlapScore = column.getProximalSegment()
            /// for s in connectedSynapses(c)
            ///     overlap(c) = overlap(c) + input(t, s.sourceInput)
            .getNumberOfActiveSynapses();

        super.algorithmStatistics.getSP_activeSynapsesHistoryAndAdd(newOverlapScore);

        // compute minimumOverlapScore assuming all proximalSegments are
        // connected to the same number of synapses
        Column[][] columns = this.region.getColumns();
        int regionMinimumOverlapScore = this.region.getMinimumOverlapScore();

        /// if overlap(c) < minOverlap then
        if (newOverlapScore < regionMinimumOverlapScore) {
            /// overlap(c) = 0
            newOverlapScore = 0;
        } else {
            /// overlap(c) = overlap(c) * boost(c)
            newOverlapScore = (int) (newOverlapScore * column.getBoostValue());
        }
        column.setOverlapScore(newOverlapScore);
    }

    /**
     * This method is called by performPooling and computes the
     * activeColumns(t) = the list of Columns that win due to the bottom-up input
     * at time t.
     */
    void computeActiveColumnsOfRegion() {
        // remove old active columns from last time spatial pooling was called
        this.activeColumns.clear();
        this.activeColumnPositions.clear();

        Column[][] columns = this.region.getColumns();
        /// for c in columns
        for (int row = 0; row < columns.length; row++) {
            for (int column = 0; column < columns[0].length; column++) {
                columns[row][column].setActiveState(false);
                this.updateNeighborColumns(row, column);

                // necessary for calculating kthScoreOfColumns
                List<ColumnPosition> neighborColumnPositions = new ArrayList<ColumnPosition>();
                neighborColumnPositions = columns[row][column].getNeighborColumns();
                List<Column> neighborColumns = new ArrayList<Column>();
                for (ColumnPosition columnPosition : neighborColumnPositions) {
                    neighborColumns
                            .add(columns[columnPosition.getRow()][columnPosition
                                    .getColumn()]);
                }

                /// minLocalActivity = kthScore(neighbors(c), desiredLocalActivity)
                int minimumLocalOverlapScore = this.kthScoreOfColumns(
                        neighborColumns, this.region.getDesiredLocalActivity());

                // more than (this.region.desiredLocalActivity) number of
                // columns can become active since it is applied to each
                // Column object's neighborColumns

                /// if overlap(c) > 0 and overlap(c) >= minLocalActivity then
                if (columns[row][column].getOverlapScore() > 0
                        && columns[row][column].getOverlapScore() >= minimumLocalOverlapScore) {
                    /// activeColumns(t).append(c)
                    columns[row][column].setActiveState(true);

                    this.activeColumns.add(columns[row][column]);
                    this.activeColumnPositions.add(new ColumnPosition(row, column));
                }
            }
        }
        super.algorithmStatistics.getSP_activeColumnsHistoryAndAdd(this.activeColumnPositions.size());
    }

    /**
     * This method models spike-timing dependent plasticity. This is also known
     * as Hebb's Rule. The inhibitionRadius for the Region is also computed and
     * updated here.
     */
    void regionLearnOneTimeStep() {
        this.modelLongTermPotentiationAndDepression();

        this.boostSynapsesBasedOnActiveAndOverlapDutyCycle();

        /// inhibitionRadius = averageReceptiveFieldSize()
        double inhibitionRadius = averageReceptiveFieldSizeOfRegion();
        this.region
                .setInhibitionRadius((int) inhibitionRadius);

        super.algorithmStatistics.getSP_inhibitionRadiusHistoryAndAdd(inhibitionRadius);
    }

    void modelLongTermPotentiationAndDepression() {
        Column[][] columns = this.region.getColumns();

        if (super.getLearningState()) {
            /// for c in activeColumns(t)
            for (int x = 0; x < columns.length; x++) {
                for (int y = 0; y < columns[0].length; y++) {
                    if (columns[x][y].getActiveState()) {
                        // increase and decrease of proximal segment synapses
                        // based on each Synapses's activeState
                        Set<Synapse<Cell>> synapses = columns[x][y]
                                .getProximalSegment().getSynapses();

                        /// for s in potentialSynapses(c)
                        for (Synapse<Cell> synapse : synapses) {
                            /// if active(s) then
                            if (synapse.getConnectedCell() != null
                                    && synapse.getConnectedCell()
                                    .getActiveState()) {
                                // model long term potentiation
                                /// s.permanence += permanenceInc
                                /// s.permanence = min(1.0, s.permanence)
                                synapse.increasePermanence();
                            } else {
                                // model long term depression
                                /// s.permanence -= permanenceDec
                                /// s.permanence = max(0.0, s.permanence)
                                synapse.decreasePermanence();
                            }
                        }
                    }
                }
            }
        }
    }

    void boostSynapsesBasedOnActiveAndOverlapDutyCycle() {
        Column[][] columns = this.region.getColumns();

        /// for c in columns
        for (int row = 0; row < columns.length; row++) {
            for (int column = 0; column < columns[0].length; column++) {
                if (columns[row][column].getActiveState()) {
                    // increase and decrease of proximal Segment Synapses based
                    // on each Synapses's activeState
                    // columns[row][column].performBoosting();

                    // 2 methods to help a Column's proximal Segment
                    // Synapses learn connections:
                    //
                    // 1) If activeDutyCycle(measures winning rate) is too low.
                    // The overall boost value of the Columns is increased.
                    //
                    // 2) If overlapDutyCycle(measures connected Synapses with
                    // inputs) is too low, the permanence values of the
                    // Column's Synapses are boosted.

                    // neighborColumns are already up to date.
                    List<ColumnPosition> neighborColumnPositions = columns[row][column]
                            .getNeighborColumns();

                    List<Column> neighborColumns = new ArrayList<Column>();
                    for (ColumnPosition columnPosition : neighborColumnPositions) {
                        // add the Column object to neighborColumns
                        neighborColumns
                                .add(columns[columnPosition.getRow()][columnPosition
                                        .getColumn()]);
                    }

                    float maximumActiveDutyCycle = this.region
                            .maximumActiveDutyCycle(neighborColumns);
                    if (maximumActiveDutyCycle == 0) {
                        maximumActiveDutyCycle = 0.1f;
                    }

                    // neighborColumns are no longer necessary for calculations
                    // in this time step
                    columns[row][column].clearNeighborColumns();

                    // minDutyCycle represents the minimum desired firing rate
                    // for a Column(number of times it becomes active over some
                    // number of iterations).
                    // If a Column's firing rate falls below this value, it will
                    // be boosted.
                    /// minDutyCycle(c) = 0.01 * maxDutyCycle(neighbors(c))
                    float minimumActiveDutyCycle = this.MINIMUM_COLUMN_FIRING_RATE
                            * maximumActiveDutyCycle;

                    // 1) boost if activeDutyCycle is too low
                    /// activeDutyCycle(c) = updateActiveDutyCycle(c)
                    columns[row][column].updateActiveDutyCycle();

                    /// boost(c) = boostFunction(activeDutyCycle(c), minDutyCycle(c))
                    columns[row][column].setBoostValue(columns[row][column]
                            .boostFunction(minimumActiveDutyCycle));

                    // 2) boost if overlapDutyCycle is too low
                    /// overlapDutyCycle(c) = updateOverlapDutyCycle(c)
                    this.updateOverlapDutyCycle(row, column);

                    /// if overlapDutyCycle(c) < minDutyCycle(c) then
                    if (columns[row][column].getOverlapDutyCycle() < minimumActiveDutyCycle
                            && this.getLearningState()) {
                        /// increasePermanences(c, 0.1*connectedPerm)
                        columns[row][column]
                                .increaseProximalSegmentSynapsePermanences(1);
                    }
                }
            }
        }
    }

    /**
     * Adds all Columns within inhitionRadius of the parameter Column to the
     * neighborColumns field within the parameter Column.
     *
     * @param columnRowPosition    position of Column within Region along y-axis
     * @param columnColumnPosition position of Column within Region along x-axis
     */
    void updateNeighborColumns(int columnRowPosition, int columnColumnPosition) {
        if (columnRowPosition < 0 || columnRowPosition > this.region.getNumberOfRowsAlongRegionYAxis()
                || columnColumnPosition < 0
                || columnColumnPosition > this.region.getNumberOfColumnsAlongRegionXAxis()) {
            throw new IllegalArgumentException(
                    "the Column being updated by the updateNeighborColumns method"
                            + "in SpatialPooler class does not exist within the Region");
        }
        int localInhibitionRadius = this.region.getInhibitionRadius();
        assert (localInhibitionRadius >= 0);

        // forced inhibition of adjacent Columns
        int xInitial = Math.max(0, columnRowPosition - localInhibitionRadius);
        int yInitial = Math.max(0, columnColumnPosition - localInhibitionRadius);

        // System.out.println("xInitial, yInitial: " + xInitial + ", " +
        // yInitial);
        int xFinal = Math.min(this.region.getNumberOfRowsAlongRegionYAxis(), columnRowPosition
                + localInhibitionRadius);
        int yFinal = Math.min(this.region.getNumberOfColumnsAlongRegionXAxis(), columnColumnPosition
                + localInhibitionRadius);

        // to allow double for loop to reach end portion of this.allColumns
        xFinal = Math.min(this.region.getNumberOfRowsAlongRegionYAxis(), xFinal + 1);
        yFinal = Math.min(this.region.getNumberOfColumnsAlongRegionXAxis(), yFinal + 1);
        // System.out.println("xFinal, yFinal: " + xFinal + ", " + yFinal);

        Column[][] columns = this.region.getColumns();

        if (columns[columnRowPosition][columnColumnPosition].getNeighborColumns().size() != 0) {
            // remove neighbors of Column computed with old inhibitionRadius
            columns[columnRowPosition][columnColumnPosition].clearNeighborColumns();
        }

        for (int columnIndex = xInitial; columnIndex < xFinal; columnIndex++) {
            for (int rowIndex = yInitial; rowIndex < yFinal; rowIndex++) {
                if (columnIndex == columnRowPosition && rowIndex == columnColumnPosition) {
                } else {
                    Column newColumn = columns[columnIndex][rowIndex];
                    if (newColumn != null) {
                        columns[columnRowPosition][columnColumnPosition].addNeighborColumns(new ColumnPosition(columnIndex, rowIndex));
                    }
                }
            }
        }
    }

    /**
     * @param neighborColumns
     * @param desiredLocalActivity
     * @return the kth highest overlapScore value of a Column object within the
     * neighborColumns list.
     */
    int kthScoreOfColumns(List<Column> neighborColumns,
                          int desiredLocalActivity) {
        if (neighborColumns == null) {
            throw new IllegalArgumentException(
                    "neighborColumns in SpatialPooler method kthScoreOfColumns cannot be null");
        }
        // TreeSet data structures' elements are automatically sorted.
        Set<Integer> overlapScores = new TreeSet<Integer>();
        for (Column column : neighborColumns) {
            overlapScores.add(column.getOverlapScore());
        }

        // if invalid or no local activity is desired, it is changed so that the
        // highest overlapScore is returned.
        if (desiredLocalActivity <= 0) {
            throw new IllegalStateException(
                    "desiredLocalActivity cannot be <= 0");
        }

        // k is the index of the overlapScore to be returned. The overlapScore
        // is the score at position k(counting from the top) of all
        // overlapScores when arranged from smallest to greatest.
        int k = Math.max(0, overlapScores.size() - desiredLocalActivity);
        if (overlapScores.size() > k) {
            return (Integer) overlapScores.toArray()[k];
        } else {
            return 0;
        }
    }

    /**
     * Returns the radius of the average connected receptive field size of all
     * the Columns. The connected receptive field size of a Column includes only
     * the Column's connected Synapses.
     *
     * @return The average connected receptive field size.
     */
    double averageReceptiveFieldSizeOfRegion() {
        double regionAverageReceptiveField = 0.0;

        // for each column
        Column[][] columns = this.region.getColumns();
        for (int x = 0; x < columns.length; x++) {
            for (int y = 0; y < columns[0].length; y++) {

                // get the set of connected synapses
                Set<Synapse<Cell>> connectedSynapes = columns[x][y]
                        .getProximalSegment().getConnectedSynapses();

                Dimension bottomLayerDimensions = this.region
                        .getBottomLayerXYAxisLength();

                // get the column position relative to the input layer
                double rowRatio = bottomLayerDimensions.width
                        / this.region.getNumberOfRowsAlongRegionYAxis();
                double columnX = rowRatio / 2 + x * rowRatio;
                double columnRatio = bottomLayerDimensions.height
                        / this.region.getNumberOfColumnsAlongRegionXAxis();
                double columnY = columnRatio / 2 + y * columnRatio;

                double totalSynapseDistanceFromOriginColumn = 0.0;
                // iterates over every connected Synapses and sums the
                // distances from it's origin Column to determine the
                // average receptive field for this Column
                for (Synapse<?> connectedSynapse : connectedSynapes) {

                    double dx = Math.abs(columnX
                            - connectedSynapse.getCellColumn());
                    double dy = Math.abs(columnY
                            - connectedSynapse.getCellRow());
                    double connectedSynapseDistance = Math.sqrt(dx * dx + dy
                            * dy);
                    totalSynapseDistanceFromOriginColumn += connectedSynapseDistance;
                }

                double columnAverageReceptiveField = totalSynapseDistanceFromOriginColumn
                        / connectedSynapes.size();
                regionAverageReceptiveField += columnAverageReceptiveField;
            }
        }

        regionAverageReceptiveField /= this.region.getNumberOfColumns();

        return regionAverageReceptiveField;
    }

    /**
     * Compute a moving average of how often this Column has overlap greater
     * than minimumDutyOverlap. Exponential Moving Average(EMA): St = a * Yt +
     * (1 - a) * St - 1.
     */
    void updateOverlapDutyCycle(int columnRowPosition, int columnColumnPosition) {
        if (columnRowPosition < 0 || columnRowPosition > this.region.getNumberOfRowsAlongRegionYAxis()
                || columnColumnPosition < 0
                || columnColumnPosition > this.region.getNumberOfColumnsAlongRegionXAxis()) {
            throw new IllegalArgumentException(
                    "the Column being updated by the updateOverlapDutyCycle method"
                            + "in SpatialPooler does not exist within the Region");
        }
        // Note whenever updateOverlapDutyCycle() is called, the
        // overlapDutyCycle
        // is always decremented less and less but only incremented if the
        // Column's
        // overlapScore was greater than the Region's minimumOverlapScore.
        // Furthermore, the increment applied to overlapDutyCycle is a constant
        // representing the maximum decrement of overlapDutyCycle from initial
        // value 1. Because of this a Column's overlapDutyCycle has a upper
        // bound of 1.
        Column[][] columns = this.region.getColumns();
        float newOverlapDutyCycle = (1.0f - Column.EXPONENTIAL_MOVING_AVERAGE_AlPHA)
                * columns[columnRowPosition][columnColumnPosition].getOverlapDutyCycle();

        if (columns[columnRowPosition][columnColumnPosition].getOverlapScore() > this.region
                .getMinimumOverlapScore()) {
            newOverlapDutyCycle += Column.EXPONENTIAL_MOVING_AVERAGE_AlPHA;
        }
        columns[columnRowPosition][columnColumnPosition]
                .setOverlapDutyCycle(newOverlapDutyCycle);
    }

    public Region getRegion() {
        return this.region;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n===============================");
        stringBuilder.append("\n---SpatialPooler Information---");
        stringBuilder.append("\n     biological region name: ");
        stringBuilder.append(this.region.getBiologicalName());
        stringBuilder.append("\n# of activeColumns produced: ");
        stringBuilder.append(this.activeColumnPositions.size());
        stringBuilder.append("\n===============================");
        String spatialPoolerInformation = stringBuilder.toString();
        return spatialPoolerInformation;
    }
}