package model.MARK_II;

import model.MARK_II.connectTypes.RegionToRegionRectangleConnect;
import model.MARK_II.region.Region;
import model.MARK_II.util.Rectangle;

import java.awt.*;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version 12/24/2014
 */
public class NeocortexTest extends junit.framework.TestCase {
    private Neocortex neocortex;
    private final int L3NPM = 4; // = layer 3 neurons per column
    private final int L4NPM = 1; // = layer 4 neurons per column
    private double PMO = 20; // = percent minimum overlap
    private int DLA = 3; // = desired local activity
    private model.MARK_II.util.Rectangle connectionLocation;
    private Region A, B, C, D;

    public void setUp() {
        this.neocortex = new Neocortex(new Region("root", 10, 10, L3NPM, PMO, DLA), new RegionToRegionRectangleConnect());
    }

    public void test_getRegionWithoutCycle() {
        this.connectionLocation = new Rectangle(new Point(0, 0), new Point(9, 9));

        this.A = new Region("A", 10, 10, L4NPM, PMO, DLA);
        this.B = new Region("B", 10, 10, L4NPM, PMO, DLA);
        this.C = new Region("C", 10, 10, L4NPM, PMO, DLA);
        this.D = new Region("D", 10, 10, L4NPM, PMO, DLA);
        this.neocortex.addToCurrentRegion(connectionLocation, A, 0, 0);
        this.neocortex.changeCurrentRegionTo("A");
        this.neocortex.addToCurrentRegion(connectionLocation, B, 0, 0);
        this.neocortex.addToCurrentRegion(connectionLocation, C, 0, 0);
        this.neocortex.changeCurrentRegionTo("C");
        this.neocortex.addToCurrentRegion(connectionLocation, D, 0, 0);

        Region foundD = this.neocortex.getRegion("D");
        assertEquals(D, foundD);

        // when looking for a nonexistent region
        Region foundE = this.neocortex.getRegion("E");
        assertEquals(null, foundE);
    }

    public void test_getRegionWithCycle() {
        this.test_getRegionWithoutCycle();

        // now see if the recursive find works even when there is a cycle in
        // the neocortex undirected graph
        this.neocortex.changeCurrentRegionTo("D");
        this.neocortex.addToCurrentRegion(connectionLocation, A, 0, 0);
        Region E = new Region("E", 10, 10, L4NPM, PMO, DLA);
        this.neocortex.addToCurrentRegion(connectionLocation, E, 0, 0);
        Region F = new Region("F", 10, 10, L4NPM, PMO, DLA);
        Region G = new Region("G", 10, 10, L4NPM, PMO, DLA);
        this.neocortex.addToCurrentRegion(connectionLocation, C, 0, 0);
        this.neocortex.addToCurrentRegion(connectionLocation, F, 0, 0);
        this.neocortex.addToCurrentRegion(connectionLocation, G, 0, 0);

        // Current Neocortex undirected graph:
        //    A <-----
        //  B  -> C  |
        //     |  D --
        //     ---E
        //      F   G
        Region foundHInGraphWithCycle = this.neocortex.getRegion("H");
        assertEquals(null, foundHInGraphWithCycle);

        Region foundG = this.neocortex.getRegion("G");
        assertEquals(G, foundG);
    }
}