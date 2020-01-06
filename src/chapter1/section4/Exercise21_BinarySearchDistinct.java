package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 18/10/16.
 */
public class Exercise21_BinarySearchDistinct {

    public static void main(String[] args) {
        int[] array = {99, 8, 8, 1, 2, 4, 1, -3};

        Exercise21_BinarySearchDistinct exercise21_binarySearchDistinct = new Exercise21_BinarySearchDistinct(array);

        boolean containsElement1 = exercise21_binarySearchDistinct.contains(4);
        boolean containsElement2 = exercise21_binarySearchDistinct.contains(-3);
        boolean containsElement3 = exercise21_binarySearchDistinct.contains(99);
        boolean containsElement4 = exercise21_binarySearchDistinct.contains(0);

        StdOut.println("Contains element 4: " + containsElement1 + " Expected: true");
        StdOut.println("Contains element -3: " + containsElement2 + " Expected: true");
        StdOut.println("Contains element 99: " + containsElement3 + " Expected: true");
        StdOut.println("Contains element 0: " + containsElement4 + " Expected: false");
    }

    private int[] array;

    public Exercise21_BinarySearchDistinct(int[] keys) {
        Set<Integer> elementsSet = new HashSet<>();

        // Fill set
        for(int i = 0; i < keys.length; i++) {
            elementsSet.add(keys[i]);
        }

        array = new int[elementsSet.size()];
        int arrayIndex = 0;

        for(int key : elementsSet) {
            array[arrayIndex] = key;
            arrayIndex++;
        }

        Arrays.sort(array);
    }

    public boolean contains(int key) {
        return rank(key) != - 1;
    }

    private int rank(int key) {
        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int middle = low + (high - low) / 2;

            if (key < array[middle]) {
                high = middle - 1;
            } else if (key > array[middle]) {
                low = middle + 1;
            } else {
                return middle;
            }
        }

        return -1;
    }

}
