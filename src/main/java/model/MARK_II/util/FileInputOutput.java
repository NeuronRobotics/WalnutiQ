package model.MARK_II.util;

import java.io.*;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 26, 2013
 */
public class FileInputOutput {

    public static void saveObjectToTextFile(String string,
                                            String textFileName) throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File(textFileName)));
            bw.write(string);
        } finally {
            try {
                bw.close();
            } catch (Exception e) {

            }
        }
    }

    public static String openObjectInTextFile(String textFileName)
            throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(textFileName)));
            return br.readLine();
        } finally {
            try {
                br.close();
            } catch (Exception e) {

            }
        }
    }
}
