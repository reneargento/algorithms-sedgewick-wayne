package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 9/29/16.
 */
// Thanks to ektasingh151 (https://github.com/ektasingh151) for suggesting a simpler solution:
// https://github.com/reneargento/algorithms-sedgewick-wayne/pull/149
public class Exercise12 {

    public static void main(String[] args) {
        int[] array1 = { 0, 1, 2, 2, 5, 6, 6, 8, 25, 25 };
        int[] array2 = { -2, 0, 1, 2, 2, 2, 3, 4, 5, 10, 20, 25, 25 };

        StdOut.print("Elements: ");
        printElementsThatAppearInBothArrays(array1, array2);
        StdOut.println("\nExpected: 0 1 2 5 25");
    }

    private static void printElementsThatAppearInBothArrays(int[] array1, int[] array2) {
        int array1Index = 0;
        int array2Index = 0;
        Integer recentMatchedValue = null;

        while (array1Index < array1.length && array2Index < array2.length) {
            if (array1[array1Index] < array2[array2Index]) {
                array1Index++;
            } else if (array2[array2Index] < array1[array1Index]) {
                array2Index++;
            } else {
                if(recentMatchedValue == null || recentMatchedValue != array1[array1Index]) {
                    StdOut.print(array1[array1Index] + " ");
                    recentMatchedValue = array1[array1Index];
                }
                array1Index++;
                array2Index++;
            }
        }
    }
}
