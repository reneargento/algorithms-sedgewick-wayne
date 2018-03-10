package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 25/02/18.
 */
public class Exercise10 {

    public class RabinKarpSearchAll extends RabinKarp {

        RabinKarpSearchAll(String pattern, boolean isMonteCarloVersion) {
            super(pattern, isMonteCarloVersion);
        }

        // Count the occurrences of pattern in the text
        public int count(String text) {
            int count = 0;

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length()) {
                count++;

                if (occurrenceIndex + 1 >= text.length()) {
                    break;
                }

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

                if (occurrenceIndex + 1 >= text.length()) {
                    break;
                }

                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }
        }

        // Searches for the pattern in the text starting at specified index
        protected int searchFromIndex(String text, int textStartIndex) {
            String eligibleText = text.substring(textStartIndex);

            int textLength = eligibleText.length();

            if (textLength < patternLength) {
                return textStartIndex + textLength;  // no match
            }

            long textHash = hash(eligibleText);

            if (patternHash == textHash && check(eligibleText, 0)) {
                return textStartIndex;  // match
            }

            for (int textIndex = patternLength; textIndex < textLength; textIndex++) {
                // Remove leading character, add trailing character, check for match
                textHash = (textHash + largePrimeNumber - rm * eligibleText.charAt(textIndex - patternLength) % largePrimeNumber)
                        % largePrimeNumber;
                textHash = (textHash * alphabetSize + eligibleText.charAt(textIndex)) % largePrimeNumber;

                int offset = textIndex - patternLength + 1;

                if (patternHash == textHash && check(eligibleText, offset)) {
                    return textStartIndex + offset;  // match
                }
            }

            return textStartIndex + textLength;      // no match
        }

    }

    public static void main(String[] args) {
        Exercise10 exercise10 = new Exercise10();

        String text = "abcdrenetestreneabdreneabcdd";

        String pattern1 = "rene";
        RabinKarpSearchAll rabinKarpSearchAll1 = exercise10.new RabinKarpSearchAll(pattern1, true);
        int count1 = rabinKarpSearchAll1.count(text);
        StdOut.println("Count 1: " + count1 + " Expected: 3");

        StdOut.println("Occurrences");
        rabinKarpSearchAll1.searchAll(text);
        StdOut.println("Expected: 4, 12, 19\n");

        String pattern2 = "abcd";
        RabinKarpSearchAll rabinKarpSearchAll2 = exercise10.new RabinKarpSearchAll(pattern2, true);
        int count2 = rabinKarpSearchAll2.count(text);
        StdOut.println("Count 2: " + count2 + " Expected: 2");

        StdOut.println("Occurrences");
        rabinKarpSearchAll2.searchAll(text);
        StdOut.println("Expected: 0, 23\n");

        String pattern3 = "d";
        RabinKarpSearchAll rabinKarpSearchAll3 = exercise10.new RabinKarpSearchAll(pattern3, true);
        int count3 = rabinKarpSearchAll3.count(text);
        StdOut.println("Count 3: " + count3 + " Expected: 4");

        StdOut.println("Occurrences");
        rabinKarpSearchAll3.searchAll(text);
        StdOut.println("Expected: 3, 18, 26, 27\n");

        String pattern4 = "zzz";
        RabinKarpSearchAll rabinKarpSearchAll4 = exercise10.new RabinKarpSearchAll(pattern4, true);
        int count4 = rabinKarpSearchAll4.count(text);
        StdOut.println("Count 4: " + count4 + " Expected: 0");

        StdOut.println("Occurrences");
        rabinKarpSearchAll4.searchAll(text);
        StdOut.println("Expected: No occurrences");
    }

}
