package model.MARK_II.experiments.heaptrackerExample;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Q Liu (quinnliu@vt.edu)
 * @date 4/1/2015
 */
public class HeaptrackerDataToJavascriptArray {
    public static void main(String []av) throws Exception {
        HeaptrackerDataToJavascriptArray
                .printToConsole("" +
                        "./src/test/java/model/experiments/vision/MARK_II/" +
                        "heaptrackerExample/" +
                        "heapSizeLogData_HowMARK_II_FitsInToBrainAnatomy.txt");
    }

    /**
     * The purpose is to print to console the contents of the heaptrace.dat file
     * into a Javascript array of the following format:
     *
     * Example input heaptrace.dat:
     *
     * 0.190750 229960
     * 0.194708 429976
     * time (seconds) heap size (bytes)
     *
     * The first column is seconds and the second column is MB used by heap.
     *
     * The output will look like the following with the seconds column multiplied
     * by 1000 to represent milliseconds.
     *
     * var dataset = [
     *              [190.75, 229960], [194.71, 429976]
    *                ];
     *
     * @param pathAndFileName
     * @throws IOException
     */
    public static void printToConsole(String pathAndFileName) throws IOException {
        DecimalFormat df1 = new DecimalFormat("#.00");
        File file = new File(pathAndFileName);
        List<String> linesWithCommas = Files.readAllLines(file.toPath(),
                StandardCharsets.UTF_8);

        String javascriptArray = "var dataset = [\n\t\t\t";

        //int numberOfValuesEachLine = 3;
        int currentElement = 1;
        for (String line : linesWithCommas) {
            String[] rowValues = line.split(" ");

            double rowValueInSeconds = Double.valueOf(rowValues[0]);
            double rowValueInMilliseconds = rowValueInSeconds * 1000;

            //Convert the heap size into megabytes
            double rowValueInBytes = Double.valueOf(rowValues[1]);
            double rowValueInMegaBytes = rowValueInBytes / 1000000;

            javascriptArray += "[" + df1.format(rowValueInMilliseconds) + "," + df1.format(rowValueInMegaBytes) + "]";

            if (currentElement < linesWithCommas.size()) {
                javascriptArray += ", ";
                currentElement++;
            } // else don't add comma

//            if (numberOfValuesEachLine == 1) {
//                javascriptArray += "\n\t\t\t";
//                numberOfValuesEachLine = 3;
//            } else {
//                numberOfValuesEachLine--;
//            }
        }

        javascriptArray += "\n\t\t\t  ];";
        System.out.print(javascriptArray);
    }
}
