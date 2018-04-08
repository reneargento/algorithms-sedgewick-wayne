package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 9/30/16.
 */
// http://stackoverflow.com/questions/12238241/find-local-minima-in-an-array
// http://courses.csail.mit.edu/6.006/spring11/lectures/lec02.pdf
public class Exercise18_LocalMinimum {

    private static int NOT_FOUND = -99999;

    public static void main(String[] args) {
        int[] array1 = {10, -9, 20, 25, 21, 40, 50, -20};
        int[] array2 = {-4, -3, 9, 4, 10, 2, 20};
        int[] array3 = {5, -3, -5, -6, -7, -8};
        int[] array4 = {5};
        int[] array5 = {10, 20};
        int[] array6 = {7, 20, 30};

        int localMinimum1 = localMinimum(array1);
        int localMinimum2 = localMinimum(array2);
        int localMinimum3 = localMinimum(array3);
        int localMinimum4 = localMinimum(array4);
        int localMinimum5 = localMinimum(array5);
        int localMinimum6 = localMinimum(array6);

        StdOut.println("Local Minimum: " + localMinimum1 + " Expected: -9 or -20");
        StdOut.println("Local Minimum: " + localMinimum2 + " Expected: 4 or -4 or 2");
        StdOut.println("Local Minimum: " + localMinimum3 + " Expected: -8");
        StdOut.println("Local Minimum: " + localMinimum4 + " Expected: 5");
        StdOut.println("Local Minimum: " + localMinimum5 + " Expected: 10");
        StdOut.println("Local Minimum: " + localMinimum6 + " Expected: 7");
    }

    // O(lg n)
    private static int localMinimum(int[] array) {

        int low = 0;
        int high = array.length - 1;

        // N = 1
        if (array.length == 1) {
            return array[0];
        }

        // N = 2
        if (array.length == 2) {
            if (array[0] < array[1]) {
                return array[0];
            } else {
                return array[1];
            }
        }

        while(low <= high) {
            int middle = low + (high - low) / 2;

            //Corner case
            if (middle == 0) {
                if (array[middle] < array[middle+1]) {
                    return array[middle];
                } else {
                    return NOT_FOUND;
                }
            }

            //Corner case
            if (middle == array.length - 1) {
                if (array[middle] < array[middle - 1]) {
                    return array[middle];
                } else {
                    return NOT_FOUND;
                }
            }

            if (array[middle - 1] > array[middle] && array[middle + 1] > array[middle]) {
                return array[middle];
            } else if (array[middle - 1] < array[middle]) {
                high = middle - 1;
            } else if (array[middle + 1] < array[middle]) {
                low = middle + 1;
            }
        }

        return NOT_FOUND;
    }

}
