package chapter6.suffixarrays;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 02/09/18.
 */

// Builds a suffix array in O(N)
// Based on J. Kärkkäinen and P. Sanders' "Simple linear work suffix array construction" article.
// In: Proceedings of the 30th International Conference on Automata, Languages and Programming, Springer, 2003
// Also based on https://github.com/carrotsearch/jsuffixarrays/blob/master/src/main/java/org/jsuffixarrays/Skew.java
public class SuffixArrayLinearTime {

    private class Suffix {
        private final String text; // reference to text string
        private final int index;   // index of suffix's first character

        private Suffix(String text, int index) {
            this.text = text;
            this.index = index;
        }

        private int length() {
            return text.length() - index;
        }

        private char charAt(int charIndex) {
            return text.charAt(index + charIndex);
        }

        public String toString() {
            return text.substring(index);
        }
    }

    private Suffix[] suffixes;
    private static final int ALPHABET_SIZE = 256;

    public SuffixArrayLinearTime(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        int[] input = new int[text.length() + 3];
        int length = text.length();
        suffixes = new Suffix[length];

        if (length == 1) {
            suffixes[0] = new Suffix(text, 0);
            return;
        }

        for (int i = 0; i < text.length(); i++) {
            input[i] = text.charAt(i);
        }

        int[] suffixArray = new int[length + 3];
        buildSuffixArray(input, suffixArray, length, ALPHABET_SIZE, new int[ALPHABET_SIZE + 2]);

        for (int index = 0; index < length; index++) {
            suffixes[index] = new Suffix(text, suffixArray[index]);
        }
    }

    // Lexicographic order for pairs
    private boolean lexicographicallyLessPair(int a1, int a2, int b1, int b2) {
        return (a1 < b1 || (a1 == b1 && a2 <= b2));
    }

    // Lexicographic order for triples
    private boolean lexicographicallyLessTriple(int a1, int a2, int a3, int b1, int b2, int b3) {
        return (a1 < b1 || (a1 == b1 && lexicographicallyLessPair(a2, a3, b2, b3)));
    }

    // Stably sort indexes from source[0..size-1] to destination[0..size-1] with keys in 0..k from values[].
    // A constant offset is added to indexes from source.
    private void radixPass(int[] source, int[] destination, int[] values, int offset, int size, int k, int[] count) {
        // Reset count array
        Arrays.fill(count, 0, k + 1, 0);

        // Count occurrences
        for (int i = 0; i < size; i++) {
            int valueIndex = offset + source[i];
            count[values[valueIndex]]++;
        }

        // Compute prefix sums
        int prefixSum = 0;
        for (int i = 0; i <= k; i++) {
            int currentCount = count[i];
            count[i] = prefixSum;
            prefixSum += currentCount;
        }

        // Sort
        for (int i = 0; i < size; i++) {
            int valueIndex = offset + source[i];
            int countIndex = values[valueIndex];
            int destinationIndex = count[countIndex];
            destination[destinationIndex] = source[i];

            count[countIndex]++;
        }
    }

    // Find the suffix array suffixArray of values[0..size-1] in {1..k}^size
    // Require values[size] = values[size + 1] = values[size + 2] = 0; size >= 2
    private int[] buildSuffixArray(int[] values, int[] suffixArray, int size, int k, int[] count) {
        int n0 = (size + 2) / 3;
        int n1 = (size + 1) / 3;
        int n2 = size / 3;
        int n02 = n0 + n2;

        // Sample suffixes
        int[] s12 = new int[n02 + 3];
        s12[n02] = 0;
        s12[n02 + 1] = 0;
        s12[n02 + 2] = 0;

        int[] suffixArray12 = new int[n02 + 3];
        suffixArray12[n02] = 0;
        suffixArray12[n02 + 1] = 0;
        suffixArray12[n02 + 2] = 0;

        // Nonsample suffixes
        int[] s0 = new int[n0];
        int[] suffixArray0 = new int[n0];

        // Step 0: Construct sample
        // Generate positions of module 1 and module 2 suffixes
        // The "+ (n0 - n1)" adds a dummy module 1 suffix if size % 3 == 1
        for (int i = 0, j = 0; i < size + (n0 - n1); i++) {
            if (i % 3 != 0) {
                s12[j++] = i;
            }
        }

        // Step 1: Sort sample suffixes
        // LSD radix sort the module 1 and module 2 triples
        count = ensureSize(count, k + 1);
        radixPass(s12, suffixArray12, values, +2, n02, k, count);
        radixPass(suffixArray12, s12, values, +1, n02, k, count);
        radixPass(s12, suffixArray12, values, 0, n02, k, count);

        // Find lexicographically names of triples and write them to correct places in s12
        int name = 0;
        int c0 = -1;
        int c1 = -1;
        int c2 = -1;

        for (int i = 0; i < n02; i++) {
            if (values[suffixArray12[i]] != c0
                    || values[suffixArray12[i] + 1] != c1
                    || values[suffixArray12[i] + 2] != c2) {
                name++;
                c0 = values[suffixArray12[i]];
                c1 = values[suffixArray12[i] + 1];
                c2 = values[suffixArray12[i] + 2];
            }

            if (suffixArray12[i] % 3 == 1) {
                // Left half
                s12[suffixArray12[i] / 3] = name;
            } else {
                // Right half
                s12[suffixArray12[i] / 3 + n0] = name;
            }
        }

        // Recurse if names are not yet unique
        if (name < n02) {
            count = buildSuffixArray(s12, suffixArray12, n02, name, count);

            // Store unique names in s12 using the suffix array
            for (int i = 0; i < n02; i++) {
                s12[suffixArray12[i]] = i + 1;
            }
        } else {
            // Generate the suffix array of s12 directly
            for (int i = 0; i < n02; i++) {
                suffixArray12[s12[i] - 1] = i;
            }
        }

        // Step 2: Sort nonsample suffixes
        // Stably sort the module 0 suffixes from suffixArray12 by their first character
        for (int i = 0, j = 0; i < n02; i++) {
            if (suffixArray12[i] < n0) {
                s0[j++] = 3 * suffixArray12[i];
            }
        }
        radixPass(s0, suffixArray0, values, 0, n0, k, count);

        // Step 3: Merge
        // Merge sorted suffixArray0 suffixes and sorted suffixArray12 suffixes
        int p = 0;
        int t = n0 - n1;
        for (int index = 0; index < size; index++) {
            // Position of current offset 12 suffix
            int i = getSuffixArrayPosition(suffixArray12, t, n0);
            // Position of current offset 0 suffix
            int j = suffixArray0[p];

            if (suffixArray12[t] < n0 ? // Different compares for module 1 and module 2 suffixes
                    lexicographicallyLessPair(values[i], s12[suffixArray12[t] + n0], values[j], s12[j / 3]) :
                    lexicographicallyLessTriple(values[i], values[i + 1], s12[suffixArray12[t] - n0 + 1],
                            values[j], values[j + 1], s12[j / 3 + n0])) {
                // Suffix from suffixArray12 is smaller
                suffixArray[index] = i;
                t++;

                if (t == n02) {
                    // Done - only suffixArray0 suffixes left
                    for (index++; p < n0; p++, index++) {
                        suffixArray[index] = suffixArray0[p];
                    }
                }
            } else {
                // Suffix from suffixArray0 is smaller
                suffixArray[index] = j;
                p++;

                if (p == n0) {
                    // Done - only suffixArray12 suffixes left
                    for (index++; t < n02; t++, index++) {
                        suffixArray[index] = getSuffixArrayPosition(suffixArray12, t, n0);
                    }
                }
            }
        }

        return count;
    }

    private int[] ensureSize(int[] array, int length) {
        if (array.length < length) {
            array = new int[length];
        }

        return array;
    }

    private int getSuffixArrayPosition(int[] suffixArray12, int t, int n0) {
        return suffixArray12[t] < n0 ? suffixArray12[t] * 3 + 1 : (suffixArray12[t] - n0) * 3 + 2;
    }

    // Other methods

    public int index(int i) {
        if (i < 0 || i >= suffixes.length) {
            throw new IllegalArgumentException("Index must be between 0 and " + (suffixes.length - 1));
        }
        return suffixes[i].index;
    }

    public int length() {
        return suffixes.length;
    }

    public String select(int i) {
        if (i < 0 || i >= suffixes.length) {
            throw new IllegalArgumentException("Index must be between 0 and " + (suffixes.length - 1));
        }
        return suffixes[i].toString();
    }

    public int longestCommonPrefix(int i) {
        if (i < 1 || i >= suffixes.length) {
            throw new IllegalArgumentException("Index must be between 1 and " + (suffixes.length - 1));
        }

        return longestCommonPrefix(suffixes[i], suffixes[i - 1]);
    }

    private int longestCommonPrefix(Suffix suffix1, Suffix suffix2) {
        int minLength = Math.min(suffix1.length(), suffix2.length());

        for (int i = 0; i < minLength; i++) {
            if (suffix1.charAt(i) != suffix2.charAt(i)) {
                return i;
            }
        }

        return minLength;
    }

    public int rank(String key) {
        int low = 0;
        int high = suffixes.length - 1;

        while (low <= high) {
            int middle = low + (high - low) / 2;

            int compare = compare(key, suffixes[middle]);

            if (compare < 0) {
                high = middle - 1;
            } else if (compare > 0) {
                low = middle + 1;
            } else {
                return middle;
            }
        }

        return low;
    }

    private int compare(String key, Suffix suffix) {
        int minLength = Math.min(key.length(), suffix.length());

        for (int i = 0; i < minLength; i++) {
            if (key.charAt(i) < suffix.charAt(i)) {
                return -1;
            }
            if (key.charAt(i) > suffix.charAt(i)) {
                return 1;
            }
        }

        return key.length() - suffix.length();
    }

    public int[] getSuffixes() {
        int[] suffixesArray = new int[suffixes.length];

        for (int i = 0; i < suffixes.length; i++) {
            suffixesArray[i] = suffixes[i].index;
        }

        return suffixesArray;
    }

    // Direct access to a suffix character in O(1).
    // This method is useful when we need to check specific characters of the suffix in constant time (instead of
    // getting the entire suffix first with select() in O(N)).
    public char getSuffixCharAt(int suffixIndex, int charIndex) {
        if (suffixIndex < 0 || suffixIndex >= suffixes.length) {
            throw new IllegalArgumentException("Suffix index must be between 0 and " + (suffixes.length - 1));
        }

        return suffixes[suffixIndex].charAt(charIndex);
    }

    public static void main(String[] args) {
        String text = "barcelona";
        SuffixArrayLinearTime suffixArrayLinearTime = new SuffixArrayLinearTime(text);

        StdOut.println("  i index lcp rank select");
        StdOut.println("------------------------------");

        for (int i = 0; i < text.length(); i++) {
            int index = suffixArrayLinearTime.index(i);
            String suffix = suffixArrayLinearTime.select(i);
            int rank = suffixArrayLinearTime.rank(suffix);

            if (i == 0) {
                StdOut.printf("%3d %5d %3s %4d %s\n", i, index, "-", rank, suffix);
            } else {
                int longestCommonPrefix = suffixArrayLinearTime.longestCommonPrefix(i);
                StdOut.printf("%3d %5d %3d %4d %s\n", i, index, longestCommonPrefix, rank, suffix);
            }
        }

        StdOut.println("\nLength: " + suffixArrayLinearTime.length() + " Expected: 9");

        StdOut.println("\n*** Index tests ***");

        int index1 = suffixArrayLinearTime.index(0);
        StdOut.println("Index 0: " + index1 + " Expected: 8");

        int index2 = suffixArrayLinearTime.index(1);
        StdOut.println("Index 1: " + index2 + " Expected: 1");

        int index3 = suffixArrayLinearTime.index(6);
        StdOut.println("Index 6: " + index3 + " Expected: 7");

        int index4 = suffixArrayLinearTime.index(8);
        StdOut.println("Index 8: " + index4 + " Expected: 2");

        StdOut.println("\n*** Select tests ***");

        String select1 = suffixArrayLinearTime.select(0);
        StdOut.println("Select 0: " + select1 + " Expected: a");

        String select2 = suffixArrayLinearTime.select(2);
        StdOut.println("Select 2: " + select2 + " Expected: barcelona");

        String select3 = suffixArrayLinearTime.select(8);
        StdOut.println("Select 8: " + select3 + " Expected: rcelona");

        StdOut.println("\n*** Longest common prefix tests ***");

        int lcp1 = suffixArrayLinearTime.longestCommonPrefix(1);
        StdOut.println("LCP 1: " + lcp1 + " Expected: 1");

        int lcp2 = suffixArrayLinearTime.longestCommonPrefix(2);
        StdOut.println("LCP 2: " + lcp2 + " Expected: 0");

        StdOut.println("\n*** Rank tests ***");

        int rank1 = suffixArrayLinearTime.rank("algorithms");
        StdOut.println("Rank algorithms: " + rank1 + " Expected: 1");

        int rank2 = suffixArrayLinearTime.rank("fenwick");
        StdOut.println("Rank fenwick: " + rank2 + " Expected: 5");

        int rank3 = suffixArrayLinearTime.rank("rene argento");
        StdOut.println("Rank rene argento: " + rank3 + " Expected: 9");
    }

}
