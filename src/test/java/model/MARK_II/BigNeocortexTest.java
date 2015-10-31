//package model.MARK_II;
//
//import junit.framework.TestCase;
//import model.MARK_II.region.Region;
//import model.MARK_II.util.BigClassUtil;
//import model.MARK_II.connectTypes.RegionToRegionRectangleConnect;
//import model.MARK_II.region.Layer5Region;
//import model.MARK_II.util.Rectangle;
//
//import java.awt.*;
//import java.io.File;
//import java.io.IOException;
//
///**
// * @author Q Liu (quinnliu@vt.edu)
// * @version 6/9/2015
// */
//public class BigNeocortexTest extends TestCase {
//    private int maxSizeOfARegionInMB;
//    private String path;
//
//    private String[] regionParameterListInOrder;
//    private String pathAndFolderName;
//
//    private BigNeocortex bigNeocortex;
//
//    public void setUp() throws IOException {
//        this.maxSizeOfARegionInMB = 350;
//        this.path = "./src/test/java/model/MARK_II/";
//
//        // pass it an array of all Region names
//        // Example List:
//        // index_0 = root, 60, 60, 4, 20, 3
//        // index_1 = A   , 60, 60, 4, 20, 3
//
//        // NOTE: new region every 6 elements
//        this.regionParameterListInOrder = new String[] {"root", "60", "60", "2", "20", "3",
//                                               "A", "60", "60", "2", "20", "3"};
//
//        // NOTE: new connection pattern every 7 elements
//        String[] regionConnectionParameterListInOrder = new String[]{
//                "0", "0", "30", "60", "A", "2", "2",
//                "change to region A"};
//
//        this.pathAndFolderName = "" +
//                "./src/test/java/model/MARK_II/BigNeocortexTest__0";
//        this.bigNeocortex = new BigNeocortex(this.maxSizeOfARegionInMB,
//                this.regionParameterListInOrder, new
//                RegionToRegionRectangleConnect(),
//                regionConnectionParameterListInOrder,
//                this.pathAndFolderName);
//    }
//
//    public void test_connectAllRegions() throws IOException {
//        Region root = this.bigNeocortex.getRegion("root");
//        // now each Synapse should be attached to a Neuron
//        assertNotNull(root.getColumn(0, 0).getProximalSegment().getSynapse(0,
//                0).getCell());
//        assertEquals("A", this.bigNeocortex.getCurrentRegion().getBiologicalName());
//
//        // delete folder created by constructor
//        File firstPath = new File(this.pathAndFolderName);
//        BigClassUtil.deleteFolder(firstPath);
//    }
//
//    public void test_instantiateAndSaveAllUnconnectedRegions() throws IOException {
//        // make sure root.json and A.json are in MARK_II/BigNeocortexTest__0
//        File path__0 = new File(this.pathAndFolderName);
//        assertTrue(BigClassUtil.isFileInList("root.json", path__0
//                .listFiles()));
//        assertTrue(BigClassUtil.isFileInList("A.json", path__0.listFiles
//                ()));
//
//        this.bigNeocortex.createUniqueFolderToSaveBigNeocortex(this.pathAndFolderName);
//        this.bigNeocortex.instantiateAndSaveAllUnconnectedRegions(this.regionParameterListInOrder);
//
//        // make sure root.json and A.json are in MARK_II/BigNeocortexTest__1
//        File path__1 = new File(this.path + "BigNeocortexTest__1");
//        assertTrue(BigClassUtil.isFileInList("root.json", path__1
//                .listFiles()));
//        assertTrue(BigClassUtil.isFileInList("A.json", path__1.listFiles()));
//
//        BigClassUtil.deleteFolder(path__0);
//        BigClassUtil.deleteFolder(path__1);
//    }
//
//    public void test_instantiateAndSaveLayer5Region() throws IOException {
//        String[] regionParameterListInOrder = new String[] {
//                "Layer 5 M", "60", "60", "4", "20", "3",
//                "A", "60", "60", "4", "20", "3"};
//
//        // NOTE: new connection pattern every 7 elements
//        String[] regionConnectionParameterListInOrder = new String[]{
//                "0", "0", "30", "60", "A", "4", "4"};
//
//        this.bigNeocortex = new BigNeocortex(this.maxSizeOfARegionInMB,
//                regionParameterListInOrder, new
//                RegionToRegionRectangleConnect(),
//                regionConnectionParameterListInOrder,
//                this.pathAndFolderName);this
//
//        assertEquals(Layer5Region.class, this.bigNeocortex.getRegion("Layer 5 M")
//                .getClass());
//
//        File path__1 = new File(this.path + "BigNeocortexTest__1");
//        BigClassUtil.deleteFolder(path__1);
//
//        // delete folder created by constructor
//        File firstPath = new File(this.pathAndFolderName);
//        BigClassUtil.deleteFolder(firstPath);
//    }
//
//    public void test_createUniqueFolderToSaveBigNeocortex() {
//        File path = new File(this.path);
//        // currently there is no folder by that name in the folder MARK_II
//        assertFalse(BigClassUtil.isFolderInList
//                ("test_createUniqueFolderToSaveBigNeocortex", path.listFiles()));
//
//        String pathAndFolderName = this.path +
//                "test_createUniqueFolderToSaveBigNeocortex";
//        this.bigNeocortex.createUniqueFolderToSaveBigNeocortex(pathAndFolderName);
//        assertTrue(BigClassUtil.isFolderInList
//                ("test_createUniqueFolderToSaveBigNeocortex", path.listFiles()));
//
//        assertFalse(BigClassUtil.isFolderInList
//                ("test_createUniqueFolderToSaveBigNeocortex__0", path.listFiles()));
//        this.bigNeocortex.createUniqueFolderToSaveBigNeocortex(pathAndFolderName);
//
//        assertTrue(BigClassUtil.isFolderInList
//                ("test_createUniqueFolderToSaveBigNeocortex__0", path.listFiles()));
//
//        assertFalse(BigClassUtil.isFolderInList
//                ("test_createUniqueFolderToSaveBigNeocortex__1", path.listFiles()));
//        this.bigNeocortex.createUniqueFolderToSaveBigNeocortex(pathAndFolderName);
//        assertTrue(BigClassUtil.isFolderInList
//                ("test_createUniqueFolderToSaveBigNeocortex__1", path.listFiles()));
//
//        assertFalse(BigClassUtil.isFolderInList
//                ("test_createUniqueFolderToSaveBigNeocortex__2", path.listFiles()));
//        this.bigNeocortex.createUniqueFolderToSaveBigNeocortex(pathAndFolderName);
//        assertTrue(BigClassUtil.isFolderInList
//                ("test_createUniqueFolderToSaveBigNeocortex__2", path.listFiles()));
//
//        File folder = new File(pathAndFolderName);
//        BigClassUtil.deleteFolder(folder);
//        File folder__0 = new File(pathAndFolderName + "__0");
//        BigClassUtil.deleteFolder(folder__0);
//        File folder__1 = new File(pathAndFolderName + "__1");
//        BigClassUtil.deleteFolder(folder__1);
//        File folder__2 = new File(pathAndFolderName + "__2");
//        BigClassUtil.deleteFolder(folder__2);
//
//        // delete folder created by constructor
//        File firstPath = new File(this.pathAndFolderName);
//        BigClassUtil.deleteFolder(firstPath);
//    }
//
//    public void test_getRegion() throws IOException {
//        Region A = this.bigNeocortex.getRegion("A");
//        assertEquals("A", A.getBiologicalName());
//
//        assertNull(this.bigNeocortex.getRegion("B"));
//
//        // delete folder created by constructor
//        File firstPath = new File(this.pathAndFolderName);
//        BigClassUtil.deleteFolder(firstPath);
//    }
//
//    public void test_addToCurrentRegion() throws IOException {
//        Region A_BeforeAddingB = this.bigNeocortex.getRegion("A");
//        Rectangle connectionLocation = new Rectangle(new Point(0, 0), new Point(9, 9));
//        Region B = new Region("B", 10, 10, 4, 20, 3);
//        this.bigNeocortex.addToCurrentRegion(connectionLocation, B, 0, 0);
//
//        Region A_AfterAddingB = this.bigNeocortex.getRegion("A");
//
//        assertNotSame(A_BeforeAddingB, A_AfterAddingB);
//
//        // delete folder created by constructor
//        File firstPath = new File(this.pathAndFolderName);
//        BigClassUtil.deleteFolder(firstPath);
//    }
//}
