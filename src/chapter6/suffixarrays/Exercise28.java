package chapter6.suffixarrays;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 25/08/18.
 */
public class Exercise28 {

    public class SuffixArray {

        private class Suffix implements Comparable<Suffix> {
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

            public int compareTo(Suffix that) {
                if (this == that) {
                    return 0;
                }

                int minLength = Math.min(this.length(), that.length());

                for (int i = 0; i < minLength; i++) {
                    if (this.charAt(i) < that.charAt(i)) {
                        return -1;
                    }
                    if (this.charAt(i) > that.charAt(i)) {
                        return 1;
                    }
                }

                return this.length() - that.length();
            }
        }

        private Suffix[] suffixes;

        public SuffixArray(String text) {
            int length = text.length();
            suffixes = new Suffix[length];

            for (int index = 0; index < length; index++) {
                suffixes[index] = new Suffix(text, index);
            }
            Arrays.sort(suffixes);
        }

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

    }

    public static void main(String[] args) {
        String text = "barcelona";
        SuffixArray suffixArray = new Exercise28().new SuffixArray(text);

        StdOut.println("  i index lcp rank select");
        StdOut.println("------------------------------");

        for (int i = 0; i < text.length(); i++) {
            int index = suffixArray.index(i);
            String suffix = "\"" + text.substring(index) + "\"";
            assert text.substring(index).equals(suffixArray.select(i));

            int rank = suffixArray.rank(text.substring(index));

            if (i == 0) {
                StdOut.printf("%3d %5d %3s %4d %s\n", i, index, "-", rank, suffix);
            } else {
                int longestCommonPrefix = suffixArray.longestCommonPrefix(i);
                StdOut.printf("%3d %5d %3d %4d %s\n", i, index, longestCommonPrefix, rank, suffix);
            }
        }

        StdOut.println("\n*** Longest common prefix tests ***");

        int lcp1 = suffixArray.longestCommonPrefix(1);
        StdOut.println("LCP 1: " + lcp1 + " Expected: 1");

        int lcp2 = suffixArray.longestCommonPrefix(2);
        StdOut.println("LCP 2: " + lcp2 + " Expected: 0");

        StdOut.println("\n*** Rank tests ***");

        int rank1 = suffixArray.rank("algorithms");
        StdOut.println("Rank algorithms: " + rank1 + " Expected: 1");

        int rank2 = suffixArray.rank("fenwick");
        StdOut.println("Rank fenwick: " + rank2 + " Expected: 5");

        int rank3 = suffixArray.rank("rene argento");
        StdOut.println("Rank rene argento: " + rank3 + " Expected: 9");
    }

}
