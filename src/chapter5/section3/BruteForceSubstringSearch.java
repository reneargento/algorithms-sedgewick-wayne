package chapter5.section3;

import chapter1.section3.Queue;

/**
 * Created by Rene Argento on 24/02/18.
 */
// Runs in O(N * M), but the typical running time is N + M
public class BruteForceSubstringSearch implements SubstringSearch {

    protected String pattern;
    protected int patternLength;

    public BruteForceSubstringSearch(String pattern) {
        this.pattern = pattern;
        patternLength = pattern.length();
    }

    // Search for pattern in text.
    // Returns the index of the first occurrence of the pattern string in the text string or textLength if no such match.
    public int search(String text) {
        int textLength = text.length();

        for (int textIndex = 0; textIndex <= textLength - patternLength; textIndex++) {
            int patternIndex;

            for (patternIndex = 0; patternIndex < patternLength; patternIndex++) {
                if (text.charAt(textIndex + patternIndex) != pattern.charAt(patternIndex)) {
                    break;
                }
            }

            if (patternIndex == patternLength) {
                return textIndex;  // found
            }
        }

        return textLength;        // not found
    }

    // Alternate implementation
    public int search2(String text) {
        int textLength = text.length();

        int textIndex;
        int patternIndex;

        for (textIndex = 0, patternIndex = 0; textIndex < textLength && patternIndex < patternLength; textIndex++) {

            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                patternIndex++;
            } else {
                textIndex -= patternIndex;
                patternIndex = 0;
            }
        }

        if (patternIndex == patternLength) {
            return textIndex - patternLength;  // found
        } else {
            return textLength;                 // not found
        }
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

        for (int textIndex = textStartIndex; textIndex <= textLength - patternLength; textIndex++) {
            int patternIndex;

            for (patternIndex = 0; patternIndex < patternLength; patternIndex++) {
                if (text.charAt(textIndex + patternIndex) != pattern.charAt(patternIndex)) {
                    break;
                }
            }

            if (patternIndex == patternLength) {
                return textIndex;  // found
            }
        }

        return textLength;        // not found
    }

}
