package model.MARK_II.parameters;

import junit.framework.TestCase;
import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.MARK_II.generalAlgorithm.ColumnPosition;
import model.MARK_II.region.Region;
import model.MARK_II.generalAlgorithm.SpatialPooler;
import model.MARK_II.connectTypes.SensorCellsToRegionRectangleConnect;
import model.MARK_II.sensory.Retina;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version Apr 16, 2014
 */
public class SDRScoreCalculatorTest extends TestCase {
    private Set<ColumnPosition> columnActivityAfterSeeingImage2;
    private Set<ColumnPosition> columnActivityWithBadSDRScore;
    private Set<ColumnPosition> columnActivityWithMediumSDRScore;
    private Set<ColumnPosition> columnActivityWithGoodSDRScore;

    private double desiredPercentageOfActiveColumns;
    private int totalNumberOfColumnsInRegion;

    private SDRScoreCalculator sdrScoreCalculator;

    public void setUp() throws IOException {
        this.setUpColumnActivityAfterSeeingImage2();
        this.setUpColumnActivityWithBadSDRScore();
        this.setUpColumnActivityWithMediumSDRScore();
        this.setUpColumnActivityWithGoodSDRScore();

        // set up SDRScoreCalculator
        this.desiredPercentageOfActiveColumns = 10.0;
        this.totalNumberOfColumnsInRegion = 64;

        this.sdrScoreCalculator = new SDRScoreCalculator(
                columnActivityAfterSeeingImage2,
                this.desiredPercentageOfActiveColumns,
                totalNumberOfColumnsInRegion);
    }

    private void setUpColumnActivityAfterSeeingImage2() throws IOException {
        Retina retina = new Retina(66, 66);
        Region region = new Region("Region", 8, 8, 1, 50, 3);

        AbstractSensorCellsToRegionConnect retinaToRegion = new SensorCellsToRegionRectangleConnect();
        retinaToRegion.connect(retina.getVisionCells(), region.getColumns(), 0, 0);

        SpatialPooler spatialPooler = new SpatialPooler(region);
        spatialPooler.setLearningState(true);

        retina.seeBMPImage("2.bmp");

        spatialPooler.performPooling(); // 11 active columns
        this.columnActivityAfterSeeingImage2 = spatialPooler
                .getActiveColumnPositions();
        // = (6,5)(6, 3)(6, 2)(5, 3)(3, 5)(2, 2)(1, 3)(1, 2)(2, 5)(1, 5)(4, 4)
    }

    private void setUpColumnActivityWithBadSDRScore() {
        // imagine a region that is 8 by 8 columns
        ColumnPosition columnPosition00 = new ColumnPosition(0, 0);
        ColumnPosition columnPosition01 = new ColumnPosition(0, 1);
        ColumnPosition columnPosition10 = new ColumnPosition(1, 0);
        ColumnPosition columnPosition11 = new ColumnPosition(1, 1);
        ColumnPosition columnPosition02 = new ColumnPosition(0, 2);
        ColumnPosition columnPosition20 = new ColumnPosition(2, 0);
        this.columnActivityWithBadSDRScore = new HashSet<ColumnPosition>();
        this.columnActivityWithBadSDRScore.add(columnPosition00);
        this.columnActivityWithBadSDRScore.add(columnPosition01);
        this.columnActivityWithBadSDRScore.add(columnPosition10);
        this.columnActivityWithBadSDRScore.add(columnPosition11);
        this.columnActivityWithBadSDRScore.add(columnPosition02);
        this.columnActivityWithBadSDRScore.add(columnPosition20);
    }

    private void setUpColumnActivityWithMediumSDRScore() {
        // imagine a region that is 8 by 8 columns
        ColumnPosition columnPosition00 = new ColumnPosition(0, 0);
        ColumnPosition columnPosition01 = new ColumnPosition(0, 1);
        ColumnPosition columnPosition10 = new ColumnPosition(1, 1);
        ColumnPosition columnPosition11 = new ColumnPosition(2, 2);
        ColumnPosition columnPosition02 = new ColumnPosition(3, 3);
        ColumnPosition columnPosition20 = new ColumnPosition(4, 4);
        this.columnActivityWithMediumSDRScore = new HashSet<ColumnPosition>();
        this.columnActivityWithMediumSDRScore.add(columnPosition00);
        this.columnActivityWithMediumSDRScore.add(columnPosition01);
        this.columnActivityWithMediumSDRScore.add(columnPosition10);
        this.columnActivityWithMediumSDRScore.add(columnPosition11);
        this.columnActivityWithMediumSDRScore.add(columnPosition02);
        this.columnActivityWithMediumSDRScore.add(columnPosition20);
    }

    private void setUpColumnActivityWithGoodSDRScore() {
        // imagine a region that is 8 by 8 columns
        ColumnPosition columnPosition00 = new ColumnPosition(0, 0);
        ColumnPosition columnPosition01 = new ColumnPosition(0, 7);
        ColumnPosition columnPosition10 = new ColumnPosition(7, 0);
        ColumnPosition columnPosition11 = new ColumnPosition(2, 2);
        ColumnPosition columnPosition02 = new ColumnPosition(5, 5);
        ColumnPosition columnPosition20 = new ColumnPosition(7, 7);
        this.columnActivityWithGoodSDRScore = new HashSet<ColumnPosition>();
        this.columnActivityWithGoodSDRScore.add(columnPosition00);
        this.columnActivityWithGoodSDRScore.add(columnPosition01);
        this.columnActivityWithGoodSDRScore.add(columnPosition10);
        this.columnActivityWithGoodSDRScore.add(columnPosition11);
        this.columnActivityWithGoodSDRScore.add(columnPosition02);
        this.columnActivityWithGoodSDRScore.add(columnPosition20);
    }

    public void test_computeSparsityScore() {
        // setup with column activity after seeing image 2
        assertEquals(-1.08, this.sdrScoreCalculator.computeSparsityScore(),
                0.01);

        this.sdrScoreCalculator.updateParameters(
                this.columnActivityWithBadSDRScore,
                this.desiredPercentageOfActiveColumns,
                this.totalNumberOfColumnsInRegion);

        assertEquals(-1.0, this.sdrScoreCalculator.computeSparsityScore(), 0.01);

        this.sdrScoreCalculator.updateParameters(
                this.columnActivityWithMediumSDRScore,
                this.desiredPercentageOfActiveColumns,
                this.totalNumberOfColumnsInRegion);

        assertEquals(-1.20, this.sdrScoreCalculator.computeSparsityScore(),
                0.01);

        this.sdrScoreCalculator.updateParameters(
                this.columnActivityWithGoodSDRScore,
                this.desiredPercentageOfActiveColumns,
                this.totalNumberOfColumnsInRegion);

        assertEquals(-3.68, this.sdrScoreCalculator.computeSparsityScore(),
                0.01);
    }

    public void test_computeNumberOfActiveColumnsScore() {
        assertEquals(-0.25,
                this.sdrScoreCalculator.computeNumberOfActiveColumnsScore(),
                0.01);

        this.sdrScoreCalculator.updateParameters(
                this.columnActivityWithBadSDRScore, // this set has the exact
                // number of desired columns
                this.desiredPercentageOfActiveColumns,
                this.totalNumberOfColumnsInRegion);

        assertEquals(-10.0,
                this.sdrScoreCalculator.computeNumberOfActiveColumnsScore(),
                0.01);
    }

    public void test_computeSDRScore() {
        assertEquals(-1.33, this.sdrScoreCalculator.computeSDRScore(), 0.01);
    }
}
