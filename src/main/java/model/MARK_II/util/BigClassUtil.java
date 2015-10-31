package model.MARK_II.util;

import java.io.File;

/**
 * @author Q Liu (quinnliu@vt.edu)
 * @version  6/11/2015.
 */
public class BigClassUtil {

    public static String extractFolderName(String pathAndFolderName) {
        // example: String pathAndFolderName = "
        // ./src/test/java/model/experiments/vision/MARK_II/FolderName";
        String[] parts = pathAndFolderName.split("/");
        String folderName = parts[parts.length - 1];
        return folderName;
    }

    public static String extractPath(String pathAndFolderName) {
        int lengthOfFolderName = extractFolderName(pathAndFolderName).length();
        return pathAndFolderName.substring(0, pathAndFolderName.length() -
                lengthOfFolderName);
    }

    public static boolean isFolderInList(String folderName, File[] listOfFilesAndFolders) {
        for (int i = 0; i < listOfFilesAndFolders.length; i++) {

            if (listOfFilesAndFolders[i].isDirectory() &&
                    folderName.equals(listOfFilesAndFolders[i].getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFileInList(String fileName, File[] listOfFilesAndFolders) {
        for (int i = 0; i < listOfFilesAndFolders.length; i++) {

            if (listOfFilesAndFolders[i].isFile() &&
                    fileName.equals(listOfFilesAndFolders[i].getName())) {
                return true;
            }
        }
        return false;
    }

    public static void deleteFolder(File file){
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteFolder(f);
            }
        }
        file.delete();
    }
}
