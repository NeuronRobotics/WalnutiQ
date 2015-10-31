package model.MARK_II.util;

import junit.framework.TestCase;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version April 21, 2014
 */
public class RegionConsoleViewerTest extends TestCase {
    private char[][] doubleCharArray;

    public void setUp() {
        this.doubleCharArray = new char[][]{{'y', '0'}, {'o', '1'},
                {'l', '2'}, {'o', '3'}};
    }

    public void test_printDoubleCharArray() {
        int numberOfRows = this.doubleCharArray.length;
        int numberOfColumns = this.doubleCharArray[0].length;
        assertEquals(4, numberOfRows);
        assertEquals(2, numberOfColumns);
        assertEquals("y0\n" + "o1\n" + "l2\n" + "o3",
                RegionConsoleViewer.doubleCharArrayAsString(this.doubleCharArray));
    }
}
