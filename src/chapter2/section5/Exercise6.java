package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 09/04/17.
 */
public class Exercise6 {

    public static void main(String[] args) {
        Comparable[] array = {1, 9, 2, 8, 3, 7, 4, 6, 5, 0};

        Exercise6 exercise6 = new Exercise6();
        Comparable selectedItem1 = exercise6.recursiveSelect(array, 4);
        Comparable selectedItem2 = exercise6.recursiveSelect(array, 1);
        Comparable selectedItem3 = exercise6.recursiveSelect(array, 0);
        Comparable selectedItem4 = exercise6.recursiveSelect(array, 9);

        StdOut.println("Element at index 4: " + selectedItem1 + " Expected: 4");
        StdOut.println("Element at index 1: " + selectedItem2 + " Expected: 1");
        StdOut.println("Element at index 0: " + selectedItem3 + " Expected: 0");
        StdOut.println("Element at index 9: " + selectedItem4 + " Expected: 9");
    }

    private Comparable recursiveSelect(Comparable[] array, int index) {
        if (index >= array.length) {
            throw new IllegalArgumentException("Index must be smaller than array size");
        }

        StdRandom.shuffle(array);

        return recursiveSelect(array, index, 0, array.length - 1);
    }

    private Comparable recursiveSelect(Comparable[] array, int index, int low, int high) {
        if (low == high) {
            return low;
        }

        int pivotIndex = partition(array, low, high);
        if (pivotIndex == index) {
            return array[index];
        } else {
            if (pivotIndex < index) {
                return recursiveSelect(array, index, pivotIndex + 1, high);
            } else {
                return recursiveSelect(array, index, low, pivotIndex - 1);
            }
        }
    }

    private int partition(Comparable[] array, int low, int high) {
        Comparable pivot = array[low];

        int i = low;
        int j = high + 1;

        while (true) {
            while(ArrayUtil.less(array[++i], pivot)) {
                if (i == high) {
                    break;
                }
            }

            while (ArrayUtil.more(array[--j], pivot)) {
                if (j == low) {
                    break;
                }
            }

            if (i >= j) {
                break;
            }

            ArrayUtil.exchange(array, i, j);
        }

        ArrayUtil.exchange(array, low, j);
        return j;
    }

}
