package chapter6.suffixarrays;

import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 17/09/18.
 */
public class Exercise34_LongestRepeatedSubstrings {

    // Finds the longest substring that is repeated k or more times.
    // Runtime complexity: O(N * k)
    public String longestRepeatedSubstring(String string, int k) {
        if (string == null) {
            throw new IllegalArgumentException("String cannot be null");
        }

        if (string.length() == 0) {
            return "";
        }

        if (k == 0) {
            return string;
        }

        int longestSubstringSuffixIndex = 0;
        int maxLength = 0;

        SuffixArrayLinearTime suffixArray = new SuffixArrayLinearTime(string);
        int[] lcp = KasaiAlgorithm.buildLCPArray(suffixArray.getSuffixes(), string);

        for (int i = 0; i < string.length(); i++) {
            int currentIndex = i;
            int currentMinLength = lcp[i];

            while (currentIndex <= i + k - 1 && currentIndex < string.length()) {
                if (lcp[currentIndex] < currentMinLength) {
                    currentMinLength = lcp[currentIndex];
                }

                currentIndex++;
            }

            if (currentIndex == i + k && currentMinLength > maxLength) {
                maxLength = currentMinLength;
                longestSubstringSuffixIndex = i;
            }
        }

        return suffixArray.select(longestSubstringSuffixIndex).substring(0, maxLength);
    }

    // Finds all repeated substrings of size equal or higher to minimumLength.
    // Runtime complexity of O(N^2)
    public List<String> getAllRepeatedSubstringsOfMinimumLength(String string, int minimumLength) {
        if (string == null) {
            throw new IllegalArgumentException("String cannot be null");
        }

        if (string.length() == 0) {
            return new ArrayList<>();
        }

        SuffixArrayLinearTime suffixArray = new SuffixArrayLinearTime(string);
        int[] lcp = KasaiAlgorithm.buildLCPArray(suffixArray.getSuffixes(), string);

        Set<String> substrings = new HashSet<>();

        for (int i = 0; i < lcp.length; i++) {
            // Small optimization to avoid processing the same substrings
            if (i > 0 && lcp[i] == lcp[i - 1]) {
                continue;
            }

            if (lcp[i] >= minimumLength) {
                substrings.add(suffixArray.select(i).substring(0, lcp[i]));
            }
        }

        List<String> substringsList = new ArrayList<>(substrings);
        Collections.sort(substringsList);
        return substringsList;
    }

    public static void main(String[] args) {
        Exercise34_LongestRepeatedSubstrings longestRepeatedSubstrings = new Exercise34_LongestRepeatedSubstrings();

        StdOut.println("*** Longest substrings repeated K or more times ***");

        String string1 = "suffix arrays are efficient data structures";
        StdOut.println("\nString 1: " + string1);

        longestRepeatedSubstrings.testLongestRepeatedSubstring(string1, 0, "suffix arrays are efficient data structures");
        longestRepeatedSubstrings.testLongestRepeatedSubstring(string1, 1, "ar");
        longestRepeatedSubstrings.testLongestRepeatedSubstring(string1, 2, "");

        String string2 = "test0123 and 456test0 andabtest0 and999test0";
        StdOut.println("String 2: " + string2);

        longestRepeatedSubstrings.testLongestRepeatedSubstring(string2, 0, "test0123 and 456test0 andabtest0 and999test0");
        longestRepeatedSubstrings.testLongestRepeatedSubstring(string2, 1, "test0 and");
        longestRepeatedSubstrings.testLongestRepeatedSubstring(string2, 2, "test0");
        longestRepeatedSubstrings.testLongestRepeatedSubstring(string2, 3, "test0");
        longestRepeatedSubstrings.testLongestRepeatedSubstring(string2, 4, "t");
        longestRepeatedSubstrings.testLongestRepeatedSubstring(string2, 5, "t");
        longestRepeatedSubstrings.testLongestRepeatedSubstring(string2, 8, "");

        StdOut.println("*** Repeated substrings of size >= L ***");
        StdOut.println("\nString 1: " + string1);

        longestRepeatedSubstrings.testRepeatedSubstringsOfMinimumLength(string1, 1, " ,  ar, a, ar, c, e, f, ffi, fi, i, r, re, s, t, u");
        longestRepeatedSubstrings.testRepeatedSubstringsOfMinimumLength(string1, 2, " ar, ar, ffi, fi, re");
        longestRepeatedSubstrings.testRepeatedSubstringsOfMinimumLength(string1, 3, " ar, ffi");

        StdOut.println("String 2: " + string2);

        longestRepeatedSubstrings.testRepeatedSubstringsOfMinimumLength(string2, 1, " ,  and, 0, 0 and, 9, 99, a, and, d, est0, est0 and, nd, st0, st0 and, t, t0, t0 and, test0, test0 and");
        longestRepeatedSubstrings.testRepeatedSubstringsOfMinimumLength(string2, 3, " and, 0 and, and, est0, est0 and, st0, st0 and, t0 and, test0, test0 and");
        longestRepeatedSubstrings.testRepeatedSubstringsOfMinimumLength(string2, 5, "0 and, est0 and, st0 and, t0 and, test0, test0 and");
        longestRepeatedSubstrings.testRepeatedSubstringsOfMinimumLength(string2, 7, "est0 and, st0 and, test0 and");
    }

    private void testLongestRepeatedSubstring(String string, int repetitions, String expectedResult) {
        String longestRepeatedSubstring = longestRepeatedSubstring(string, repetitions);
        StdOut.println("Longest substring repeated " + repetitions + " or more times: " + longestRepeatedSubstring);
        StdOut.println("Expected: " + expectedResult);
        StdOut.println();
    }

    private void testRepeatedSubstringsOfMinimumLength(String string, int minimumLength, String expectedResult) {
        List<String> substrings = getAllRepeatedSubstringsOfMinimumLength(string, minimumLength);

        StringJoiner substringList = new StringJoiner(", ");
        for (String substring : substrings) {
            substringList.add(substring);
        }
        StdOut.println("Repeated substrings of length " + minimumLength + " or more: " + substringList);
        StdOut.println("Expected: " + expectedResult);
        StdOut.println();
    }

}
