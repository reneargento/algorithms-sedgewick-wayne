package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 16/10/16.
 */
public class Exercise20_BitonicSearch_3lgN {

    public static void main(String[] args) {
        int[] array1 = {1, 2, 3, 4, -1, -2, -3};
        int[] array2 = {1, 5, 4, 3, 2, 0};
        int[] array3 = {2, 4, 8, 16, 32, 1};
        int[] array4 = {2, 4, 8, 16, 32};
        int[] array5 = {2, 1};
        int[] array6 = {9};

        int indexOfElement1 = bitonicSearch(array1, -1);
        int indexOfElement2 = bitonicSearch(array2, 5);
        int indexOfElement3 = bitonicSearch(array3, 2);
        int indexOfElement4 = bitonicSearch(array3, 99);
        int indexOfElement5 = bitonicSearch(array4, 32);
        int indexOfElement6 = bitonicSearch(array5, 1);
        int indexOfElement7 = bitonicSearch(array6, 9);

        StdOut.println("Index of element: " + indexOfElement1 + " Expected: 4");
        StdOut.println("Index of element: " + indexOfElement2 + " Expected: 1");
        StdOut.println("Index of element: " + indexOfElement3 + " Expected: 0");
        StdOut.println("Index of element: " + indexOfElement4 + " Expected: -1");
        StdOut.println("Index of element: " + indexOfElement5 + " Expected: 4");
        StdOut.println("Index of element: " + indexOfElement6 + " Expected: 1");
        StdOut.println("Index of element: " + indexOfElement7 + " Expected: 0");
    }

    private static int bitonicSearch(int[] array, int value) {

        if (array == null || array.length == 0) {
            return -1;
        }

        int tippingPoint = findTippingPoint(array, 0, array.length - 1);

        int low = 0;
        int high = tippingPoint;

        int increasingSequenceSearchIndex = ascendingBinarySearch(array, value, low, high);

        if (increasingSequenceSearchIndex != -1) {
            return increasingSequenceSearchIndex;
        }

        low = tippingPoint + 1;
        high = array.length - 1;

        int decreasingSequenceSearchIndex = descendingBinarySearch(array, value, low, high);
        return decreasingSequenceSearchIndex;
    }

    private static int ascendingBinarySearch(int[] array, int value, int low, int high) {

        while (low <= high) {
            int middle = low + (high - low) / 2;

            if (array[middle] < value) {
                low = middle + 1;
            } else if (array[middle] > value) {
                high = middle - 1;
            } else {
                return middle;
            }
        }

        return -1;
    }

    private static int descendingBinarySearch(int[] array, int value, int low, int high) {

        while (low <= high) {
            int middle = low + (high - low) / 2;

            if (array[middle] > value) {
                low = middle + 1;
            } else if (array[middle] < value) {
                high = middle - 1;
            } else {
                return middle;
            }
        }

        return -1;
    }

    private static int findTippingPoint(int[] array, int low, int high) {

        if (low > high) {
            return array.length - 1;
        }

        if (high == low) {
            return high;
        }

        int middle = low + (high - low) / 2;

        if (middle == 0) {
            if (array[middle] < array[middle + 1]) {
                return findTippingPoint(array, middle + 1, high);
            } else {
                return 0;
            }
        } else if (middle == array.length - 1) {
            return array.length - 1;
        }

        if (array[middle] < array[middle - 1] && array[middle] > array[middle + 1]) {
            high = middle - 1;
            return findTippingPoint(array, low, high);
        } else if (array[middle] > array[middle - 1] && array[middle] < array[middle + 1]) {
            low = middle + 1;
            return findTippingPoint(array, low, high);
        } else {
            return middle;
        }
    }
}
