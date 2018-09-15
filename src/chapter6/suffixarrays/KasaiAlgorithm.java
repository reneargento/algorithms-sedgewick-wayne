package chapter6.suffixarrays;

/**
 * Created by Rene Argento on 14/09/18.
 */
// Builds a LCP[] array from a Suffix array in O(N)
// Based on https://www.geeksforgeeks.org/%C2%AD%C2%ADkasais-algorithm-for-construction-of-lcp-array-from-suffix-array/
public class KasaiAlgorithm {

    public static int[] buildLCPArray(int[] suffixArray, String text) {
        int length = suffixArray.length;
        int[] lcp = new int[length];

        // Auxiliary array to store the inverse of suffix array elements.
        // For example, if suffixArray[0] is 5, the inverseSuffixArray[5] is 0.
        int[] inverseSuffixArray = new int[length];

        for (int i = 0; i < length; i++) {
            inverseSuffixArray[suffixArray[i]] = i;
        }

        int previousLCPLength = 0;

        for (int i = 0; i < length; i++) {
            // If the current suffix is at length-1, then we don’t have a next substring to consider.
            if (inverseSuffixArray[i] == length - 1) {
                previousLCPLength = 0;
                continue;
            }

            int nextSuffixIndex = suffixArray[inverseSuffixArray[i] + 1];

            // Directly start matching from previousLCPLength'th index as at least previousLCPLength - 1
            // characters will match.
            while (i + previousLCPLength < length && nextSuffixIndex + previousLCPLength < length
                    && text.charAt(i + previousLCPLength) == text.charAt(nextSuffixIndex + previousLCPLength)) {
                previousLCPLength++;
            }

            lcp[inverseSuffixArray[i]] = previousLCPLength;

            // Delete start character
            if (previousLCPLength > 0) {
                previousLCPLength--;
            }
        }

        return lcp;
    }

}
