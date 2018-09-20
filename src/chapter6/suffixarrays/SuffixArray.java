package chapter6.suffixarrays;

import java.util.Arrays;

/**
 * Created by Rene Argento on 23/08/18.
 */
// Running time: O(N * w * logR), where w is the suffixes average length and R is the alphabet size.
// This is equal to O(N * N/2 * log256) = O(N^2) character compares.
// The worst case happens when sorting the suffixes of a string consisting of N copies of the same character.
// On average it runs in 2N ln N character compares.
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

}
