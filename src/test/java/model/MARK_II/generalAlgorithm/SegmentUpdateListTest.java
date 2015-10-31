package model.MARK_II.generalAlgorithm;

import model.MARK_II.region.Cell;
import model.MARK_II.region.Synapse;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 25, 2014
 */
public class SegmentUpdateListTest extends junit.framework.TestCase {
    private SegmentUpdateList segmentUpdateList;

    private Set<Synapse<Cell>> synapsesWithActiveCells;
    private Set<Synapse<Cell>> synapsesWithDeactiveCells;

    private SegmentUpdate segmentUpdate1;
    private SegmentUpdate segmentUpdate2;

    public void setUp() {
        this.segmentUpdateList = new SegmentUpdateList();

        this.synapsesWithActiveCells = new HashSet<Synapse<Cell>>();
        this.synapsesWithDeactiveCells = new HashSet<Synapse<Cell>>();
        this.segmentUpdate1 = new SegmentUpdate(synapsesWithActiveCells,
                synapsesWithDeactiveCells, new ColumnPosition(1, 1), 1);

        this.segmentUpdate2 = new SegmentUpdate(synapsesWithActiveCells,
                synapsesWithDeactiveCells, new ColumnPosition(2, 2), 2);

        this.segmentUpdateList.add(this.segmentUpdate1);
        this.segmentUpdateList.add(this.segmentUpdate2);
    }

    public void test_getSegmentUpdate() {
        assertEquals(this.segmentUpdate1,
                this.segmentUpdateList.getSegmentUpdate(
                        new ColumnPosition(1, 1), 1));

        assertNull(this.segmentUpdateList.getSegmentUpdate(new ColumnPosition(
                2, 1), 2));
    }

    public void test_deleteSegmentUpdate() {
        assertTrue(this.segmentUpdateList.deleteSegmentUpdate(
                new ColumnPosition(1, 1), 1));

        assertFalse(this.segmentUpdateList.deleteSegmentUpdate(
                new ColumnPosition(1, 2), 1));
    }
}
