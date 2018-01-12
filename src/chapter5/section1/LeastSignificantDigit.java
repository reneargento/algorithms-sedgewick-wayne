package chapter5.section1;

/**
 * Created by Rene Argento on 07/01/18.
 */
// O(N * W), where N is the number of strings in the array and W is the strings' length.
// All strings must have equal length.
// Also known as Radix sort.
public class LeastSignificantDigit {

    public static void lsdSort(String[] array, int stringsLength) {

        int alphabetSize = 256; // Extended ASCII characters

        String[] auxArray = new String[array.length];

        for(int digit = stringsLength - 1; digit >= 0; digit--) {
            // Sort by key-indexed counting on digitTh char

            // Compute frequency counts
            int count[] = new int[alphabetSize + 1];
            for(int i = 0; i < array.length; i++) {
                int digitIndex = array[i].charAt(digit);
                count[digitIndex + 1]++;
            }

            // Transform counts to indices
            for(int r = 0; r < alphabetSize; r++) {
                count[r + 1] += count[r];
            }

            // Distribute
            for(int i = 0; i < array.length; i++) {
                int digitIndex = array[i].charAt(digit);
                int indexInAuxArray = count[digitIndex]++;
                auxArray[indexInAuxArray] = array[i];
            }

            // Copy back
            for(int i = 0; i < array.length; i++) {
                array[i] = auxArray[i];
            }
        }
    }

}
