package chapter6.suffixarrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 26/08/18.
 */
// Based on https://algs4.cs.princeton.edu/63suffix/SuffixArrayX.java.html
// This implementation uses '\0' as a sentinel and assumes that the character '\0' does not appear in the text.
public class Exercise30 {

    public class SuffixArrayNoInnerClass {

        private char[] textChars;
        private int[] indexes; // indexes of suffixes' first characters
        private int textLength;

        public SuffixArrayNoInnerClass(String text) {
            textLength = text.length();
            indexes = new int[textLength];

            // Add an extra char to avoid an index out of bounds exception when comparing characters during sort
            text += '\0';
            textChars = text.toCharArray();

            for (int index = 0; index < textLength; index++) {
                indexes[index] = index;
            }
            new ThreeWayStringQuickSortSuffixArray().threeWayStringQuickSort(indexes, textChars);
        }

        public int index(int i) {
            if (i < 0 || i >= textLength) {
                throw new IllegalArgumentException("Index must be between 0 and " + (textLength - 1));
            }
            return indexes[i];
        }

        public int length() {
            return textLength;
        }

        public String select(int i) {
            if (i < 0 || i >= textLength) {
                throw new IllegalArgumentException("Index must be between 0 and " + (textLength - 1));
            }

            return new String(textChars, indexes[i], textLength - indexes[i]);
        }

        public int longestCommonPrefix(int i) {
            if (i < 1 || i >= textLength) {
                throw new IllegalArgumentException("Index must be between 1 and " + (textLength - 1));
            }

            return longestCommonPrefix(indexes[i], indexes[i - 1]);
        }

        private int longestCommonPrefix(int index1, int index2) {
            int longestCommonPrefix = 0;

            while (index1 < textLength && index2 < textLength) {
                if (textChars[index1] != textChars[index2]) {
                    return longestCommonPrefix;
                }

                index1++;
                index2++;
                longestCommonPrefix++;
            }

            return longestCommonPrefix;
        }

        public int rank(String key) {
            int low = 0;
            int high = textLength - 1;

            while (low <= high) {
                int middle = low + (high - low) / 2;

                int compare = compare(key, indexes[middle]);

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

        private int compare(String key, int suffixIndex) {
            int keyIndex = 0;
            int suffixLength = textLength - suffixIndex;

            while (keyIndex < key.length() && suffixIndex < textLength) {
                if (key.charAt(keyIndex) < textChars[suffixIndex]) {
                    return -1;
                }
                if (key.charAt(keyIndex) > textChars[suffixIndex]) {
                    return 1;
                }

                keyIndex++;
                suffixIndex++;
            }

            return key.length() - suffixLength;
        }

    }

    // O(N * w * logR), where N is the number of strings in the array, w is the strings' average length and R is
    // the alphabet size.
    // For suffix arrays this is equal to O(N * N/2 * log256) = O(N^2) character compares.
    // Has an average running time of N when sorting Strings with long prefix matches.
    public class ThreeWayStringQuickSortSuffixArray {

        public void threeWayStringQuickSort(int[] indexes, char[] text) {
            threeWayStringQuickSort(indexes, text, 0, indexes.length - 1, 0);
        }

        private void threeWayStringQuickSort(int[] indexes, char[] text, int low, int high, int digit) {
            if (low >= high) {
                return;
            }

            int lowerThan = low;
            int greaterThan = high;

            char pivot = text[indexes[low] + digit];

            int index = low + 1;

            while (index <= greaterThan) {
                char currentChar = text[indexes[index] + digit];

                if (currentChar < pivot) {
                    exchange(indexes, lowerThan++, index++);
                } else if (currentChar > pivot) {
                    exchange(indexes, index, greaterThan--);
                } else {
                    index++;
                }
            }

            // Now array[low..lowerThan - 1] < pivot = array[lowerThan..greaterThan] < array[greaterThan + 1..high]
            threeWayStringQuickSort(indexes, text, low, lowerThan - 1, digit);
            if (pivot > 0) {
                threeWayStringQuickSort(indexes, text, lowerThan, greaterThan, digit + 1);
            }
            threeWayStringQuickSort(indexes, text, greaterThan + 1, high, digit);
        }

        private void exchange(int[] indexes, int index1, int index2) {
            int aux = indexes[index1];
            indexes[index1] = indexes[index2];
            indexes[index2] = aux;
        }
    }

    public static void main(String[] args) {
        String text = "barcelona";
        SuffixArrayNoInnerClass suffixArrayNoInnerClass = new Exercise30().new SuffixArrayNoInnerClass(text);

        StdOut.println("  i index lcp rank select");
        StdOut.println("------------------------------");

        for (int i = 0; i < text.length(); i++) {
            int index = suffixArrayNoInnerClass.index(i);
            String suffix = suffixArrayNoInnerClass.select(i);
            int rank = suffixArrayNoInnerClass.rank(suffix);

            if (i == 0) {
                StdOut.printf("%3d %5d %3s %4d %s\n", i, index, "-", rank, suffix);
            } else {
                int longestCommonPrefix = suffixArrayNoInnerClass.longestCommonPrefix(i);
                StdOut.printf("%3d %5d %3d %4d %s\n", i, index, longestCommonPrefix, rank, suffix);
            }
        }

        StdOut.println("\nLength: " + suffixArrayNoInnerClass.length() + " Expected: 9");

        StdOut.println("\n*** Index tests ***");

        int index1 = suffixArrayNoInnerClass.index(0);
        StdOut.println("Index 0: " + index1 + " Expected: 8");

        int index2 = suffixArrayNoInnerClass.index(1);
        StdOut.println("Index 1: " + index2 + " Expected: 1");

        int index3 = suffixArrayNoInnerClass.index(6);
        StdOut.println("Index 6: " + index3 + " Expected: 7");

        int index4 = suffixArrayNoInnerClass.index(8);
        StdOut.println("Index 8: " + index4 + " Expected: 2");

        StdOut.println("\n*** Select tests ***");

        String select1 = suffixArrayNoInnerClass.select(0);
        StdOut.println("Select 0: " + select1 + " Expected: a");

        String select2 = suffixArrayNoInnerClass.select(2);
        StdOut.println("Select 2: " + select2 + " Expected: barcelona");

        String select3 = suffixArrayNoInnerClass.select(8);
        StdOut.println("Select 8: " + select3 + " Expected: rcelona");

        StdOut.println("\n*** Longest common prefix tests ***");

        int lcp1 = suffixArrayNoInnerClass.longestCommonPrefix(1);
        StdOut.println("LCP 1: " + lcp1 + " Expected: 1");

        int lcp2 = suffixArrayNoInnerClass.longestCommonPrefix(2);
        StdOut.println("LCP 2: " + lcp2 + " Expected: 0");

        StdOut.println("\n*** Rank tests ***");

        int rank1 = suffixArrayNoInnerClass.rank("algorithms");
        StdOut.println("Rank algorithms: " + rank1 + " Expected: 1");

        int rank2 = suffixArrayNoInnerClass.rank("fenwick");
        StdOut.println("Rank fenwick: " + rank2 + " Expected: 5");

        int rank3 = suffixArrayNoInnerClass.rank("rene argento");
        StdOut.println("Rank rene argento: " + rank3 + " Expected: 9");
    }

}
