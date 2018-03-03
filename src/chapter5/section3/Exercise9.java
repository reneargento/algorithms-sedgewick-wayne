package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 25/02/18.
 */
public class Exercise9 {

    public class BoyerMooreSearchAll extends BoyerMoore {

        BoyerMooreSearchAll(String pattern) {
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
    }

    public static void main(String[] args) {
        Exercise9 exercise9 = new Exercise9();

        String text = "abcdrenetestreneabdreneabcdd";

        String pattern1 = "rene";
        BoyerMooreSearchAll boyerMooreSearchAll1 = exercise9.new BoyerMooreSearchAll(pattern1);
        int count1 = boyerMooreSearchAll1.count(text);
        StdOut.println("Count 1: " + count1 + " Expected: 3");

        StdOut.println("Occurrences");
        boyerMooreSearchAll1.searchAll(text);
        StdOut.println("Expected: 4, 12, 19\n");

        String pattern2 = "abcd";
        BoyerMooreSearchAll boyerMooreSearchAll2 = exercise9.new BoyerMooreSearchAll(pattern2);
        int count2 = boyerMooreSearchAll2.count(text);
        StdOut.println("Count 2: " + count2 + " Expected: 2");

        StdOut.println("Occurrences");
        boyerMooreSearchAll2.searchAll(text);
        StdOut.println("Expected: 0, 23\n");

        String pattern3 = "d";
        BoyerMooreSearchAll boyerMooreSearchAll3 = exercise9.new BoyerMooreSearchAll(pattern3);
        int count3 = boyerMooreSearchAll3.count(text);
        StdOut.println("Count 3: " + count3 + " Expected: 4");

        StdOut.println("Occurrences");
        boyerMooreSearchAll3.searchAll(text);
        StdOut.println("Expected: 3, 18, 26, 27\n");

        String pattern4 = "zzz";
        BoyerMooreSearchAll boyerMooreSearchAll4 = exercise9.new BoyerMooreSearchAll(pattern4);
        int count4 = boyerMooreSearchAll4.count(text);
        StdOut.println("Count 4: " + count4 + " Expected: 0");

        StdOut.println("Occurrences");
        boyerMooreSearchAll4.searchAll(text);
        StdOut.println("Expected: No occurrences");
    }

}
