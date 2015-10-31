package model.MARK_II.util;

import com.google.gson.Gson;
import junit.framework.TestCase;
import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.MARK_II.connectTypes.SensorCellsToRegionRectangleConnect;
import model.MARK_II.generalAlgorithm.SpatialPooler;
import model.MARK_II.region.Column;
import model.MARK_II.region.Region;
import model.MARK_II.sensory.Retina;

import java.io.IOException;
import java.util.Set;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version Feb 1, 2014
 */
public class FileInputOutputTest extends TestCase {
    private Gson gson;
    private String path;

    public void setUp() {
        this.gson = new Gson();
        this.path = "./src/test/java/model/MARK_II/util/";
    }

    public void test_saveRegionObject() throws IOException {
        Region LGNRegion = new Region("LGN", 8, 8, 1, 50, 3);

        Retina retina = new Retina(66, 66);

        AbstractSensorCellsToRegionConnect retinaToLGN = new SensorCellsToRegionRectangleConnect();
        retinaToLGN.connect(retina.getVisionCells(), LGNRegion.getColumns(), 0, 0);

        // run spatial pooling on a image
        SpatialPooler spatialPooler = new SpatialPooler(LGNRegion);
        spatialPooler.setLearningState(true);

        retina.seeBMPImage("2.bmp");
        Set<Column> LGNNeuronActivity = spatialPooler
                .performPooling();

        assertEquals(10, LGNNeuronActivity.size());

        Gson gson2 = new Gson();
        Region trainedLGNRegion = spatialPooler.getRegion();
        String regionObject = gson2.toJson(trainedLGNRegion);

        FileInputOutput.saveObjectToTextFile(regionObject,
                this.path + "test_saveRegionObject.txt");
    }

    public void test_openRegionObject() throws IOException {
        String regionAsString = FileInputOutput
                .openObjectInTextFile(this.path + "test_saveRegionObject.txt");

        Gson gson2 = new Gson();
        Region trainedLGNRegion = gson2.fromJson(regionAsString, Region.class);
        assertEquals("LGN", trainedLGNRegion.getBiologicalName());
    }
}
