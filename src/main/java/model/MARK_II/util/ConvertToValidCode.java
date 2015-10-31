package model.MARK_II.util;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version June 29, 2014
 */
public class ConvertToValidCode {
    /**
     * @param int2DArray must contain \n to represent the next line
     */
    public void printsToConsoleConvertedJavaForInt2DArray(String int2DArray) {
        System.out.println("\n= new int[][] {");
        System.out.print("{ ");
        int int2DArrayLength = int2DArray.length();
        for (int i = 0; i < int2DArrayLength; i++) {
            char currentChar = int2DArray.charAt(i);
            char nextChar = int2DArray.charAt(i + 1);

            if (nextChar == 'E') {
                System.out.print(currentChar);
                System.out.print(" }\n};");
                return;
            } else if (nextChar == '\n') {
                System.out.print(currentChar);
                System.out.print(" },\n{ ");
            } else {
                // skip \n char
                if (currentChar != '\n') {
                    System.out.print(currentChar + ",");
                }
            }
        }
    }
}
