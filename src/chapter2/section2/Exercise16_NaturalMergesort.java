package chapter2.section2;

import util.ArrayGenerator;

/**
 * Created by Rene Argento on 14/02/17.
 */
// Thanks to svezyridis (https://github.com/svezyridis) for finding an improvement to the natural mergesort algorithm.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/101

// Running time: O(n lg IS)
// Array size = n
// Number of maximal increasing sequences in the array = IS
@SuppressWarnings("unchecked")
public class Exercise16_NaturalMergesort {

    public static void main(String[] args) {
        Comparable[] array = ArrayGenerator.generateRandomArray(1000);
        naturalMergesort(array);
    }

    private static void naturalMergesort(Comparable[] array) {
        if (array == null || array.length == 1) {
            return;
        }
        Comparable[] aux = new Comparable[array.length];

        int low = 0;
        int middle;
        int high;

        while (true) {
            middle = findSortedSubArray(array, low);
            if (middle == array.length - 1) {
                if (low == 0) // Array is sorted
                    break;
                else {
                    low = 0;
                    continue;
                }
            }
            high = findSortedSubArray(array, middle + 1);
            BottomUpMergeSort.merge(array, aux, low, middle, high);
            low = (high == array.length - 1) ? 0 : high + 1;
        }
    }

    private static int findSortedSubArray(Comparable[] array, int start) {
        for(int i = start + 1; i < array.length; i++) {
            if (array[i].compareTo(array[i - 1]) < 0) {
                return i - 1;
            }
        }
        return array.length - 1;
    }
}
