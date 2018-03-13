package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 17/10/16.
 */
//Based on http://stackoverflow.com/questions/19372930/given-a-bitonic-array-and-element-x-in-the-array-find-the-index-of-x-in-2logn
public class Exercise20_BitonicSearch_2lgN {

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

        return bitonicSearch(array, value, 0, array.length - 1);
    }

    private static int bitonicSearch(int[] array, int value, int low, int high) {

        if (low > high) {
            return -1;
        }

        int middle = low + (high - low) / 2;

        //Case 1: Element in the middle is the value searched
        if (array[middle] == value) {
            return middle;
        } else if (array[middle] < value
                && ((middle > 0 && array[middle - 1] > array[middle])
                || (middle < array.length - 1 && array[middle] > array[middle + 1]))) {
            //Case 2: Middle element is smaller than value searched, and max value is on the left
            return bitonicSearch(array, value, low, middle - 1);
        } else if (array[middle] < value
                && ((middle > 0 && array[middle - 1] < array[middle])
                || (middle < array.length - 1 && array[middle] < array[middle + 1]))) {
            //Case 3: Middle element is smaller than value searched, and max value is on the right
            return bitonicSearch(array, value, middle + 1, high);
        } else {
            //Case 4: Middle element is bigger than the value searched
            //We do an ascending binary search on the left half and a descending binary search on the right half
            int leftHalfSearch = ascendingBinarySearch(array, value, low, middle);
            if (leftHalfSearch != -1) {
                return leftHalfSearch;
            }

            int rightHalfSearch = descendingBinarySearch(array, value, middle + 1, high);
            return rightHalfSearch;
        }
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

}
