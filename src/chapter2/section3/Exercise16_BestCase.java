package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 05/03/17.
 */
//Based on http://algs4.cs.princeton.edu/23quicksort/QuickBest.java.html
public class Exercise16_BestCase {

    public static void main(String[] args) {

        int arraySize = Integer.parseInt(args[0]);

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        int[] array = bestCaseArray(arraySize);

        StdOut.print("Best-case array: ");

        for (int i = 0; i < arraySize; i++) {
            StdOut.print(alphabet.charAt(array[i]));
        }

        StdOut.println();
    }

    private static int[] bestCaseArray(int arraySize) {
        int[] bestCaseArray = new int[arraySize];

        for(int i = 0; i < arraySize; i++) {
            bestCaseArray[i] = i;
        }

        bestCaseArray(bestCaseArray, 0, arraySize - 1);
        return bestCaseArray;
    }

    private static void bestCaseArray(int[] array, int low, int high) {

        if (low >= high) {
            return;
        }

        int middle = low + (high - low) / 2;

        bestCaseArray(array, low, middle - 1);
        bestCaseArray(array, middle + 1, high);

        int temp = array[low];
        array[low] = array[middle];
        array[middle] = temp;
    }

}
