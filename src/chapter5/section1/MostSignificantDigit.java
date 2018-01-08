package chapter5.section1;

import util.ArrayUtil;

/**
 * Created by Rene Argento on 07/01/18.
 */
// O(N * w), where N is the number of strings in the array and w is the strings' average length.
// Has an average running time of N when sorting random Strings.
public class MostSignificantDigit {

    private static int alphabetSize = 256; // Extended ASCII characters; radix
    private static final int CUTOFF_FOR_SMALL_SUBARRAYS = 15;
    private static String[] auxArray;

    public static void msdSort(String[] array) {
        auxArray = new String[array.length];
        sort(array, 0, array.length - 1, 0);
    }

    private static void sort(String[] array, int low, int high, int digit) {
        // Sort from array[low] to array[high], starting at the digitTh character
        if (low + CUTOFF_FOR_SMALL_SUBARRAYS >= high) {
            InsertionSort.sort(array, low, high, digit);
            return;
        }

        // Compute frequency counts
        int[] count = new int[alphabetSize + 2];
        for(int i = low; i <= high; i++) {
            int digitIndex = charAt(array[i], digit) + 2;
            count[digitIndex]++;
        }

        // Transform counts to indices
        for(int r = 0; r < alphabetSize + 1; r++) {
            count[r + 1] += count[r];
        }

        // Distribute
        for(int i = low; i <= high; i++) {
            int digitIndex = charAt(array[i], digit) + 1;
            int indexInAuxArray = count[digitIndex]++;
            auxArray[indexInAuxArray] = array[i];
        }

        // Copy back
        for(int i = low; i <= high; i++) {
            array[i] = auxArray[i - low];
        }

        // Recursively sort for each character value
        for(int r = 0; r < alphabetSize; r++) {
            sort(array, low + count[r], low + count[r + 1] - 1,digit + 1);
        }
    }

    private static int charAt(String string, int digit) {
        if (digit < string.length()) {
            return string.charAt(digit);
        } else {
            return -1;
        }
    }

    // Insertion sort for Strings whose first digit characters are equal
    public static class InsertionSort {

        public static void sort(String[] array, int low, int high, int digit) {
            // Sort from array[low] to array[high], starting at the digitTh character
            for(int i = low; i <= high; i++) {
                for(int j = i; j > low && less(array[j], array[j - 1], digit); j--) {
                    ArrayUtil.exchange(array, j, j - 1);
                }
            }
        }

        private static boolean less(String string1, String string2, int digit) {
            for(int i = digit; i < Math.min(string1.length(), string2.length()); i++) {
                if (string1.charAt(i) < string2.charAt(i)) {
                    return true;
                } else if (string1.charAt(i) > string2.charAt(i)) {
                    return false;
                }
            }

            return string1.length() < string2.length();
        }
    }

}
