package chapter5.section1;

import util.ArrayUtil;

/**
 * Created by Rene Argento on 07/01/18.
 */
// O(N * w * logR), where N is the number of strings in the array, w is the strings' average length and R is the alphabet size.
// Has an average running time of N when sorting Strings with long prefix matches.
public class ThreeWayStringQuickSort {

    public static void threeWayStringQuickSort(String[] array) {
        threeWayStringQuickSort(array, 0, array.length - 1, 0);
    }

    private static void threeWayStringQuickSort(String[] array, int low, int high, int digit) {
        if (low >= high) {
            return;
        }

        int lowerThan = low;
        int greaterThan = high;

        int pivot = charAt(array[low], digit);

        int index = low + 1;

        while (index <= greaterThan) {
            int currentChar = charAt(array[index], digit);

            if (currentChar < pivot) {
                ArrayUtil.exchange(array, lowerThan++, index++);
            } else if (currentChar > pivot) {
                ArrayUtil.exchange(array, index, greaterThan--);
            } else {
                index++;
            }
        }

        // Now array[low..lowerThan - 1] < pivot = array[lowerThan..greaterThan] < array[greaterThan + 1..high]
        threeWayStringQuickSort(array, low, lowerThan - 1, digit);
        if (pivot >= 0) {
            threeWayStringQuickSort(array, lowerThan, greaterThan, digit + 1);
        }
        threeWayStringQuickSort(array, greaterThan + 1, high, digit);
    }

    private static int charAt(String string, int digit) {
        if (digit < string.length()) {
            return string.charAt(digit);
        } else {
            return -1;
        }
    }

}
