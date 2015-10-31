package model.MARK_II.sensory;

import com.google.gson.Gson;
import model.MARK_II.util.FileInputOutput;
import model.MARK_II.util.Rectangle;

import java.io.IOException;

/**
 * NOTE: Refer to Retina.java for summary of how a Retina is represented.
 * <p/>
 * PROBLEM: When a user wants to create a NervousSystem object that is too large
 * for the Java Heap.
 * <p/>
 * SOLUTION: This class provides the same public API as Retina.java but will
 * be loaded into the heap only when needed.
 *
 * @author Q Liu (quinnliu@vt.edu)
 * @version 6/15/2015.
 */
public class BigRetina {
    private String pathAndRetinaFileName; // where BigRetina is saved as JSON file
    private Gson gson;

    public BigRetina(int numberOfVisionCellsAlongYAxis, int numberOfVisionCellsAlongXAxis,
                     String pathAndRetinaFileName) throws IOException {
        Retina retina = new Retina(numberOfVisionCellsAlongYAxis, numberOfVisionCellsAlongXAxis);

        this.pathAndRetinaFileName = pathAndRetinaFileName;
        this.gson = new Gson();
        this.saveRetinaToDisk(retina);
    }

    public Retina getSavedRetinaFromDisk() throws IOException {
        String retinaAsJSON = FileInputOutput.openObjectInTextFile(this.pathAndRetinaFileName);
        return this.gson.fromJson(retinaAsJSON, Retina.class);
    }

    public void saveRetinaToDisk(Retina retina) throws IOException {
        String retinaAsJSON = this.gson.toJson(retina);
        FileInputOutput.saveObjectToTextFile(retinaAsJSON, this.pathAndRetinaFileName);
    }

    public VisionCell[][] getVisionCells() throws IOException {
        return this.getSavedRetinaFromDisk().getVisionCells();
    }

    public VisionCell[][] getVisionCells(Rectangle rectangle) throws IOException {
        return this.getSavedRetinaFromDisk().getVisionCells(rectangle);
    }

    public void seeBMPImage(String BMPFileName) throws IOException {
        Retina retina = this.getSavedRetinaFromDisk();
        retina.seeBMPImage(BMPFileName);
        this.saveRetinaToDisk(retina);
    }

    public void see2DIntArray(int[][] image) throws IOException {
        Retina retina = this.getSavedRetinaFromDisk();
        retina.see2DIntArray(image);
        this.saveRetinaToDisk(retina);
    }
}
