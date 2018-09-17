package chapter6.suffixarrays;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 25/08/18.
 */
public class Exercise27 {

    public class CircularSuffixArray {

        private class CircularSuffix implements Comparable<CircularSuffix> {
            private final String text; // reference to text string
            private final int index;   // index of circular suffix's first character

            private CircularSuffix(String text, int index) {
                this.text = text;
                this.index = index;
            }

            private int length() {
                return text.length();
            }

            private char charAt(int charIndex) {
                int adjustedIndex = (index + charIndex) % text.length();
                return text.charAt(adjustedIndex);
            }

            public String toString() {
                String firstPart = text.substring(index);
                String secondPart = text.substring(0, index);
                return firstPart + secondPart;
            }

            public int compareTo(CircularSuffix otherCircularSuffix) {

                for (int i = 0; i < length(); i++) {
                    if (charAt(i) < otherCircularSuffix.charAt(i)) {
                        return -1;
                    } else if (charAt(i) > otherCircularSuffix.charAt(i)) {
                        return 1;
                    }
                }

                return 0;
            }
        }

        private CircularSuffix[] circularSuffixes;

        public CircularSuffixArray(String text) {
            int length = text.length();
            circularSuffixes = new CircularSuffix[length];

            for (int index = 0; index < length; index++) {
                circularSuffixes[index] = new CircularSuffix(text, index);
            }
            Arrays.sort(circularSuffixes);
        }

        public int index(int i) {
            if (i < 0 || i >= circularSuffixes.length) {
                throw new IllegalArgumentException("Index must be between 0 and " + (circularSuffixes.length - 1));
            }
            return circularSuffixes[i].index;
        }

        public int length() {
            return circularSuffixes.length;
        }

        public String select(int i) {
            if (i < 0 || i >= circularSuffixes.length) {
                throw new IllegalArgumentException("Index must be between 0 and " + (circularSuffixes.length - 1));
            }
            return circularSuffixes[i].toString();
        }

        public int longestCommonPrefix(int i) {
            if (i < 1 || i >= circularSuffixes.length) {
                throw new IllegalArgumentException("Index must be between 1 and " + (circularSuffixes.length - 1));
            }

            return longestCommonPrefix(circularSuffixes[i], circularSuffixes[i - 1]);
        }

        private int longestCommonPrefix(CircularSuffix circularSuffix1, CircularSuffix circularSuffix2) {
            int length = circularSuffix1.length();

            for (int i = 0; i < length; i++) {
                if (circularSuffix1.charAt(i) != circularSuffix2.charAt(i)) {
                    return i;
                }
            }

            return length;
        }

        public int rank(String key) {
            int low = 0;
            int high = circularSuffixes.length - 1;

            while (low <= high) {
                int middle = low + (high - low) / 2;

                int compare = compare(key, circularSuffixes[middle]);

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

        private int compare(String key, CircularSuffix circularSuffix) {
            int minLength = Math.min(key.length(), circularSuffix.length());

            for (int i = 0; i < minLength; i++) {
                if (key.charAt(i) < circularSuffix.charAt(i)) {
                    return -1;
                }
                if (key.charAt(i) > circularSuffix.charAt(i)) {
                    return 1;
                }
            }

            return key.length() - circularSuffix.length();
        }

    }

    public static void main(String[] args) {
        String text = "barcelona";
        CircularSuffixArray circularSuffixArray = new Exercise27().new CircularSuffixArray(text);

        StdOut.println("  i index lcp rank select");
        StdOut.println("------------------------------");

        for (int i = 0; i < text.length(); i++) {
            int index = circularSuffixArray.index(i);
            String suffix = circularSuffixArray.select(i);
            int rank = circularSuffixArray.rank(suffix);

            if (i == 0) {
                StdOut.printf("%3d %5d %3s %4d %s\n", i, index, "-", rank, suffix);
            } else {
                int longestCommonPrefix = circularSuffixArray.longestCommonPrefix(i);
                StdOut.printf("%3d %5d %3d %4d %s\n", i, index, longestCommonPrefix, rank, suffix);
            }
        }

        StdOut.println("\nLength: " + circularSuffixArray.length() + " Expected: 9");

        StdOut.println("\n*** Index tests ***");

        int index1 = circularSuffixArray.index(0);
        StdOut.println("Index 0: " + index1 + " Expected: 8");

        int index2 = circularSuffixArray.index(1);
        StdOut.println("Index 1: " + index2 + " Expected: 1");

        int index3 = circularSuffixArray.index(6);
        StdOut.println("Index 6: " + index3 + " Expected: 7");

        int index4 = circularSuffixArray.index(8);
        StdOut.println("Index 8: " + index4 + " Expected: 2");

        StdOut.println("\n*** Select tests ***");

        String select1 = circularSuffixArray.select(0);
        StdOut.println("Select 0: " + select1 + " Expected: abarcelon");

        String select2 = circularSuffixArray.select(2);
        StdOut.println("Select 2: " + select2 + " Expected: barcelona");

        String select3 = circularSuffixArray.select(8);
        StdOut.println("Select 8: " + select3 + " Expected: rcelonaba");

        StdOut.println("\n*** Longest common prefix tests ***");

        int lcp1 = circularSuffixArray.longestCommonPrefix(1);
        StdOut.println("LCP 1: " + lcp1 + " Expected: 1");

        int lcp2 = circularSuffixArray.longestCommonPrefix(2);
        StdOut.println("LCP 2: " + lcp2 + " Expected: 0");

        StdOut.println("\n*** Rank tests ***");

        int rank1 = circularSuffixArray.rank("algorithms");
        StdOut.println("Rank algorithms: " + rank1 + " Expected: 1");

        int rank2 = circularSuffixArray.rank("fenwick");
        StdOut.println("Rank fenwick: " + rank2 + " Expected: 5");

        int rank3 = circularSuffixArray.rank("rene argento");
        StdOut.println("Rank rene argento: " + rank3 + " Expected: 9");
    }

}
