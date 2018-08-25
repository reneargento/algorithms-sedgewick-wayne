package chapter6.suffixarrays;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/08/18.
 */
public class LongestRepeatedSubstring {

    public static void main(String[] args) {
        String text = StdIn.readAll().replaceAll("\\s+", " ");
        int length = text.length();

        SuffixArray suffixArray = new SuffixArray(text);
        String longestRepeatedSubstring = "";

        for (int i = 1; i < length; i++) {
            int currentLength = suffixArray.longestCommonPrefix(i);

            if (currentLength > longestRepeatedSubstring.length()) {
                longestRepeatedSubstring = text.substring(suffixArray.index(i), suffixArray.index(i) + currentLength);
            }
        }
        StdOut.println("'" + longestRepeatedSubstring + "'");
    }

}
