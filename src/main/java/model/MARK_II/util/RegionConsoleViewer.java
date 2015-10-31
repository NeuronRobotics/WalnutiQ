package model.MARK_II.util;

import model.MARK_II.region.*;

import java.awt.*;
import java.util.Set;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 18, 2013
 */
public class RegionConsoleViewer {
    /**
     * Returns a 2-D array of chars representing each Column's activeState
     * within a Region inside of a SpatialPooler object. 'a' represents an
     * active Column while 'i' represents an inactive Column for the current
     * time step.
     *
     * @param region
     * @return A 2-D char array of Columns' active states.
     */
    public static char[][] getColumnActiveStatesCharArray(Region region) {
        char[][] columnActiveStates = new char[region.getNumberOfRowsAlongRegionYAxis()][region
                .getNumberOfColumnsAlongRegionXAxis()];
        Column[][] columns = region.getColumns();
        for (int row = 0; row < columnActiveStates.length; row++) {
            for (int column = 0; column < columnActiveStates[row].length; column++) {
                if (columns[row][column].getActiveState()) {
                    // 'a' represents an active Column at a specific time step
                    columnActiveStates[row][column] = 'a';
                } else {
                    // 'i' represents an inactive Column at a specific time step
                    columnActiveStates[row][column] = 'i';
                }
            }
        }
        return columnActiveStates;
    }

    /**
     * Returns a 2-D array of chars representing each Column's activeState
     * within a Region inside of a SpatialPooler object. 'a' represents an
     * active Column while 'i' represents an inactive Column for the current
     * time step.
     *
     * @param region
     * @return A 2-D char array of Columns' predictive states.
     */
    public static char[][] getColumnPredictiveStatesCharArray(Region region) {
        char[][] columnPredictiveStates = new char[region.getNumberOfRowsAlongRegionYAxis()][region
                .getNumberOfColumnsAlongRegionXAxis()];
        Column[][] columns = region.getColumns();
        for (int row = 0; row < columnPredictiveStates.length; row++) {
            for (int column = 0; column < columnPredictiveStates[row].length; column++) {
                Neuron[] neurons = columns[row][column].getNeurons();
                for (Neuron neuron : neurons) {
                    if (neuron.getPredictingState()) {
                        // 'p' represents an Column with a Neuron that has predictive state on
                        columnPredictiveStates[row][column] = 'p';
                        break;
                    } else {
                        // 'n' represents an Column with a Neuron that does NOTE havehas predictive state on
                        columnPredictiveStates[row][column] = 'n';
                    }
                }
            }
        }
        return columnPredictiveStates;
    }

    /**
     * Returns a 2-D array of integers representing each Column's overlapScore
     * within a Region inside of a SpatialPooler object.
     *
     * @param region
     * @return A 2-D integer array of Columns' overlapScores.
     */
    public static int[][] getColumnOverlapScoresIntArray(Region region) {
        int[][] columnOverlapScores = new int[region.getNumberOfRowsAlongRegionYAxis()][region
                .getNumberOfColumnsAlongRegionXAxis()];
        Column[][] columns = region.getColumns();
        for (int row = 0; row < columnOverlapScores.length; row++) {
            for (int column = 0; column < columnOverlapScores[row].length; column++) {
                columnOverlapScores[row][column] = columns[row][column].getOverlapScore();
            }
        }
        return columnOverlapScores;
    }

    /**
     * 0 means the SensorCell is disconnected from the Synapse at that location.
     * Any number 2-9 represents the permanenceValue of the Synapse at that
     * location rounded down in the tenth decimal place.
     *  
     * For example, a Synapse with a permanenceValue of 0.36 will be represented
     * as a 3 in the int[][] array being returned.
     */
    public static int[][] getSynapsePermanencesIntArray(Region region) {
        Dimension bottomLayerDimensions = region.getBottomLayerXYAxisLength();
        int[][] synapsePermanences = new int[bottomLayerDimensions.width][bottomLayerDimensions.height];

        Column[][] columns = region.getColumns();
        for (int row = 0; row < columns.length; row++) {
            for (int column = 0; column < columns[row].length; column++) {
                Set<Synapse<Cell>> synapses = columns[row][column]
                        .getProximalSegment().getSynapses();

                for (Synapse<Cell> synapse : synapses) {
                    if (synapse.getPermanenceValue() < 0.2) {
                        synapsePermanences[synapse.getCellColumn()][synapse
                                .getCellRow()] = 0;
                    } else {
                        // permanenceTimesTen = round((0.9999 - 0.05555) * 10)
                        int permanenceTimesTen = (int) Math.round(((synapse
                                .getPermanenceValue() - 0.055555) * 10));
                        synapsePermanences[synapse.getCellColumn()][synapse
                                .getCellRow()] = permanenceTimesTen;
                    }
                }
            }
        }
        return synapsePermanences;
    }

    /**
     * Prints a byte 2-D array to the console.
     *
     * @param doubleByteArray The 2-D byte array to be printed.
     */
    public static void printDoubleByteArray(byte[][] doubleByteArray) {
        for (int row = 0; row < doubleByteArray.length; row++) {
            System.out.println();
            for (int column = 0; column < doubleByteArray[row].length; column++) {
                System.out.print(doubleByteArray[row][column]);
            }
        }
    }

    /**
     * Prints a int 2-D array to the console.
     *
     * @param doubleIntArray The 2-D int array to be printed.
     */
    public static void printDoubleIntArray(int[][] doubleIntArray) {
        for (int row = 0; row < doubleIntArray.length; row++) {
            System.out.println();
            for (int column = 0; column < doubleIntArray[row].length; column++) {
                System.out.printf("%1d", doubleIntArray[row][column]);
            }
        }
    }

    /**
     * Returns a char 2-D array to the console.
     *
     * @param doubleCharArray The 2-D char array to be printed.
     * @return The 2-D char array as a String.
     */
    public static String doubleCharArrayAsString(char[][] doubleCharArray) {
        String doubleCharArrayAsString = "";
        for (int row = 0; row < doubleCharArray.length; row++) {
            boolean isAtBeginning = (row == 0);
            boolean isAtEnd = (row == doubleCharArray.length);
            if (isAtBeginning || isAtEnd) {
                // don't add anything
            } else {
                doubleCharArrayAsString += "\n";
            }

            for (int column = 0; column < doubleCharArray[row].length; column++) {
                doubleCharArrayAsString += doubleCharArray[row][column];
            }
        }
        return doubleCharArrayAsString;
    }

    public static void printDoubleCharArray(char[][] doubleCharArray) {
        System.out.print(doubleCharArrayAsString(doubleCharArray));
    }
}
