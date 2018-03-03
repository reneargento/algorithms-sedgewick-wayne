package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/02/18.
 */
public class Exercise7 {

    public class BruteForceSubstringSearchAll extends BruteForceSubstringSearch {

        BruteForceSubstringSearchAll(String pattern) {
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

    public static void main(String[] args) {
        Exercise7 exercise7 = new Exercise7();

        String text = "abcdrenetestreneabdreneabcdd";

        String pattern1 = "rene";
        BruteForceSubstringSearchAll bruteForceSubstringSearchAll1 = exercise7.new BruteForceSubstringSearchAll(pattern1);
        int count1 = bruteForceSubstringSearchAll1.count(text);
        StdOut.println("Count 1: " + count1 + " Expected: 3");

        StdOut.println("Occurrences");
        bruteForceSubstringSearchAll1.searchAll(text);
        StdOut.println("Expected: 4, 12, 19\n");

        String pattern2 = "abcd";
        BruteForceSubstringSearchAll bruteForceSubstringSearchAll2 = exercise7.new BruteForceSubstringSearchAll(pattern2);
        int count2 = bruteForceSubstringSearchAll2.count(text);
        StdOut.println("Count 2: " + count2 + " Expected: 2");

        StdOut.println("Occurrences");
        bruteForceSubstringSearchAll2.searchAll(text);
        StdOut.println("Expected: 0, 23\n");

        String pattern3 = "d";
        BruteForceSubstringSearchAll bruteForceSubstringSearchAll3 = exercise7.new BruteForceSubstringSearchAll(pattern3);
        int count3 = bruteForceSubstringSearchAll3.count(text);
        StdOut.println("Count 3: " + count3 + " Expected: 4");

        StdOut.println("Occurrences");
        bruteForceSubstringSearchAll3.searchAll(text);
        StdOut.println("Expected: 3, 18, 26, 27\n");

        String pattern4 = "zzz";
        BruteForceSubstringSearchAll bruteForceSubstringSearchAll4 = exercise7.new BruteForceSubstringSearchAll(pattern4);
        int count4 = bruteForceSubstringSearchAll4.count(text);
        StdOut.println("Count 4: " + count4 + " Expected: 0");

        StdOut.println("Occurrences");
        bruteForceSubstringSearchAll4.searchAll(text);
        StdOut.println("Expected: No occurrences");
    }

}
