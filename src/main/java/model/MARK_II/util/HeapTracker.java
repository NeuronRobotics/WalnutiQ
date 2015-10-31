package model.MARK_II.util;

import java.io.IOException;

/**
 * @author Q Liu (quinnliu@vt.edu)
 * @date 6/9/2015.
 */
public class HeapTracker {
    private String allHeapData;
    private long START_TIME;
    private boolean printDataToConsole;

    public HeapTracker(boolean printDataToConsole) {
        this.allHeapData = new String();
        this.START_TIME = System.currentTimeMillis();
        this.printDataToConsole = printDataToConsole;

        if (this.printDataToConsole) {
            long heapMaxSizeInMB = Runtime.getRuntime().maxMemory() / 1000000;
            System.out.println("heapMaxSize = " + heapMaxSizeInMB + " MB");
        }
    }

    public void updateHeapData() {
        if (this.printDataToConsole) {
            System.out.println("usedHeapSize = " + this.getUsedHeapInBytes() / 1000000 + " MB");
        }

        double currentRunTimeInMilliseconds = System.currentTimeMillis() - this.START_TIME;
        double currentRunTimeInSeconds = currentRunTimeInMilliseconds / 1000;
        if (this.printDataToConsole) {
            System.out.println("currentRunTimeInSeconds = " + currentRunTimeInSeconds);
        }

        String addToFile = Double.toString(currentRunTimeInSeconds) + " " + this.getUsedHeapInBytes() + "\n";
        this.allHeapData += addToFile;
    }

    public String getAllHeapData() {
        return this.allHeapData;
    }

    public void printAllHeapDataToFile(String pathAndFileName) throws IOException {
        FileInputOutput.saveObjectToTextFile(this.allHeapData, pathAndFileName);
    }

    public long getUsedHeapInBytes() {
        long heapFreeSizeInBytes = Runtime.getRuntime().freeMemory();
        long heapSizeInBytes = Runtime.getRuntime().totalMemory();
        long usedHeapSizeInBytes = heapSizeInBytes - heapFreeSizeInBytes;
        return usedHeapSizeInBytes;
    }

    public long getHeapMaxSizeInBytes() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * @param percentage a number between 0 and 1 representing percentage.
     * @return True if used_heap/max_heap > percentage; otherwise return false.
     */
    public boolean isUsedHeapPercentageOver(double percentage) {
        if (percentage < 0 || percentage > 1) {
            throw new IllegalArgumentException("percentage must be between 0 and 1");
        }
        double used = (double) this.getUsedHeapInBytes();
        double max = (double) this.getHeapMaxSizeInBytes();
        double usedOverMax = used/max;
        return usedOverMax > percentage;
    }
}
