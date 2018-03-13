package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/10/16.
 */
public class Exercise23_BinarySearchWithDuplicates {

    public static void main(String[] args) {

        int[] array = {-4, -1, 1, 2, 2, 2, 2, 2, 2, 2, 4, 5};

        Exercise23_BinarySearchWithDuplicates exercise23_binarySearchWithDuplicates = new Exercise23_BinarySearchWithDuplicates();

        //First rank tests
        int firstRankElement1 = exercise23_binarySearchWithDuplicates.firstRank(array, -4);
        int firstRankElement2 = exercise23_binarySearchWithDuplicates.firstRank(array, -8);
        int firstRankElement3 = exercise23_binarySearchWithDuplicates.firstRank(array, 5);
        int firstRankElement4 = exercise23_binarySearchWithDuplicates.firstRank(array, 2);


        StdOut.println("First index of element -4: " + firstRankElement1 + " Expected: 0");
        StdOut.println("First index of element -8: " + firstRankElement2 + " Expected: -1");
        StdOut.println("First index of element 5: " + firstRankElement3 + " Expected: 11");
        StdOut.println("First index of element 2: " + firstRankElement4 + " Expected: 3");

        //Last rank tests
        int firstRankElement5 = exercise23_binarySearchWithDuplicates.lastRank(array, -4);
        int firstRankElement6 = exercise23_binarySearchWithDuplicates.lastRank(array, -8);
        int firstRankElement7 = exercise23_binarySearchWithDuplicates.lastRank(array, 5);
        int firstRankElement8 = exercise23_binarySearchWithDuplicates.lastRank(array, 2);


        StdOut.println("First index of element -4: " + firstRankElement5 + " Expected: 0");
        StdOut.println("First index of element -8: " + firstRankElement6 + " Expected: -1");
        StdOut.println("First index of element 5: " + firstRankElement7 + " Expected: 11");
        StdOut.println("First index of element 2: " + firstRankElement8 + " Expected: 9");
    }

    private int firstRank(int[] array, int key) {
        int low = 0;
        int high = array.length - 1;

        return firstRank(array, key, low, high);
    }

    private int firstRank(int[] array, int key, int low, int high) {
        while (low <= high) {
            int middle = low + (high - low) / 2;

            if (key < array[middle]) {
                high = middle - 1;
            } else if (key > array[middle]) {
                low = middle + 1;
            } else {

                int newIndex = firstRank(array, key, low, middle - 1);
                if (newIndex != -1) {
                    return newIndex;
                } else {
                    return middle;
                }
            }
        }

        return -1;
    }

    private int lastRank(int[] array, int key) {
        int low = 0;
        int high = array.length - 1;

        return lastRank(array, key, low, high);
    }

    private int lastRank(int[] array, int key, int low, int high) {
        while (low <= high) {
            int middle = low + (high - low) / 2;

            if (key < array[middle]) {
                high = middle - 1;
            } else if (key > array[middle]) {
                low = middle + 1;
            } else {

                int newIndex = lastRank(array, key, middle + 1, high);
                if (newIndex != -1) {
                    return newIndex;
                } else {
                    return middle;
                }
            }
        }

        return -1;
    }

}
