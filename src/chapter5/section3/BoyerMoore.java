package chapter5.section3;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/02/18.
 */
// Runs in O(N * M), but the typical running time is N / M
// Extra space: R
// Requires backup in the input text
public class BoyerMoore implements SubstringSearch {

    protected int[] right;
    protected String pattern;

    public BoyerMoore(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Invalid pattern");
        }

        this.pattern = pattern;
        int alphabetSize = 256;

        right = new int[alphabetSize];

        for (int currentChar = 0; currentChar < alphabetSize; currentChar++) {
            right[currentChar] = -1; // -1 for chars not in pattern
        }

        for (int patternIndex = 0; patternIndex < pattern.length(); patternIndex++)  {
            right[pattern.charAt(patternIndex)] = patternIndex; // rightmost position for chars in pattern
        }
    }

    // Search for pattern in the text.
    // Returns the index of the first occurrence of the pattern string in the text string or textLength if no such match.
    public int search(String text) {
        int textLength = text.length();
        int patternLength = pattern.length();

        int skip;

        for (int textIndex = 0; textIndex <= textLength - patternLength; textIndex += skip) {
            // Does the pattern match the text at position textIndex?
            skip = 0;

            for (int patternIndex = patternLength - 1; patternIndex >= 0; patternIndex--) {
                if (pattern.charAt(patternIndex) != text.charAt(textIndex + patternIndex)) {
                    skip = Math.max(1, patternIndex - right[text.charAt(textIndex + patternIndex)]);
                    break;
                }
            }
            if (skip == 0) {
                return textIndex; // found
            }
        }

        return textLength;        // not found
    }

    // Count the occurrences of pattern in the text
    public int count(String text) {
        int count = 0;

        int occurrenceIndex = searchFromIndex(text, 0);

        while (occurrenceIndex != text.length()) {
            count++;
            occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
        }

        return count;
    }

    // Finds all the occurrences of pattern in the text
    public Iterable<Integer> findAll(String text) {
        Queue<Integer> offsets = new Queue<>();

        int occurrenceIndex = searchFromIndex(text, 0);

        while (occurrenceIndex != text.length()) {
            offsets.enqueue(occurrenceIndex);
            occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
        }

        return offsets;
    }

    // Searches for the pattern in the text starting at specified index.
    // Most of the code is copied from search() method instead of making search() call this method with textStartIndex 0
    // in order to keep the methods separated for educational purposes.
    protected int searchFromIndex(String text, int textStartIndex) {
        int textLength = text.length();
        int patternLength = pattern.length();

        int skip;

        for (int textIndex = textStartIndex; textIndex <= textLength - patternLength; textIndex += skip) {
            // Does the pattern match the text at position textIndex?
            skip = 0;

            for (int patternIndex = patternLength - 1; patternIndex >= 0; patternIndex--) {
                if (pattern.charAt(patternIndex) != text.charAt(textIndex + patternIndex)) {
                    skip = Math.max(1, patternIndex - right[text.charAt(textIndex + patternIndex)]);
                    break;
                }
            }
            if (skip == 0) {
                return textIndex; // found
            }
        }

        return textLength;        // not found
    }

    // Parameters example: AACAA AABRAACADABRAACAADABRA
    public static void main(String[] args) {
        String pattern = args[0];
        String text = args[1];

        BoyerMoore boyerMoore = new BoyerMoore(pattern);
        StdOut.println("text:    " + text);

        int offset = boyerMoore.search(text);
        StdOut.print("pattern: ");
        for (int i = 0; i < offset; i++) {
            StdOut.print(" ");
        }
        StdOut.println(pattern);
    }

}
