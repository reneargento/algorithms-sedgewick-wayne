package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 25/02/18.
 */
public class Exercise8 {

    public class KnuthMorrisPrattSearchAll extends KnuthMorrisPratt {

        KnuthMorrisPrattSearchAll(String pattern) {
            super(pattern);
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

        // Prints all the occurrences of pattern in the text
        public void searchAll(String text) {
            int occurrenceIndex = searchFromIndex(text, 0);

            if (occurrenceIndex == text.length()) {
                StdOut.println("No occurrences");
                return;
            }

            while (occurrenceIndex != text.length()) {
                StdOut.println("Pattern found at index " + occurrenceIndex);
                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }
        }

        // Searches for the pattern in the text starting at specified index
        protected int searchFromIndex(String text, int textStartIndex) {
            int textIndex;
            int patternIndex;
            int textLength = text.length();
            int patternLength = pattern.length();

            for (textIndex = textStartIndex, patternIndex = 0; textIndex < textLength && patternIndex < patternLength;
                 textIndex++) {
                patternIndex = dfa[text.charAt(textIndex)][patternIndex];
            }
            if (patternIndex == patternLength) {
                return textIndex - patternLength; // found
            } else {
                return textLength;                // not found
            }
        }
    }

    public static void main(String[] args) {
        Exercise8 exercise8 = new Exercise8();

        String text = "abcdrenetestreneabdreneabcdd";

        String pattern1 = "rene";
        KnuthMorrisPrattSearchAll knuthMorrisPrattSearchAll1 = exercise8.new KnuthMorrisPrattSearchAll(pattern1);
        int count1 = knuthMorrisPrattSearchAll1.count(text);
        StdOut.println("Count 1: " + count1 + " Expected: 3");

        StdOut.println("Occurrences");
        knuthMorrisPrattSearchAll1.searchAll(text);
        StdOut.println("Expected: 4, 12, 19\n");

        String pattern2 = "abcd";
        KnuthMorrisPrattSearchAll knuthMorrisPrattSearchAll2 = exercise8.new KnuthMorrisPrattSearchAll(pattern2);
        int count2 = knuthMorrisPrattSearchAll2.count(text);
        StdOut.println("Count 2: " + count2 + " Expected: 2");

        StdOut.println("Occurrences");
        knuthMorrisPrattSearchAll2.searchAll(text);
        StdOut.println("Expected: 0, 23\n");

        String pattern3 = "d";
        KnuthMorrisPrattSearchAll knuthMorrisPrattSearchAll3 = exercise8.new KnuthMorrisPrattSearchAll(pattern3);
        int count3 = knuthMorrisPrattSearchAll3.count(text);
        StdOut.println("Count 3: " + count3 + " Expected: 4");

        StdOut.println("Occurrences");
        knuthMorrisPrattSearchAll3.searchAll(text);
        StdOut.println("Expected: 3, 18, 26, 27\n");

        String pattern4 = "zzz";
        KnuthMorrisPrattSearchAll knuthMorrisPrattSearchAll4 = exercise8.new KnuthMorrisPrattSearchAll(pattern4);
        int count4 = knuthMorrisPrattSearchAll4.count(text);
        StdOut.println("Count 4: " + count4 + " Expected: 0");

        StdOut.println("Occurrences");
        knuthMorrisPrattSearchAll4.searchAll(text);
        StdOut.println("Expected: No occurrences");
    }

}
