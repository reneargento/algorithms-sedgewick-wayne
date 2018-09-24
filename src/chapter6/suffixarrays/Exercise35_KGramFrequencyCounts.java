package chapter6.suffixarrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 20/09/18.
 */
public class Exercise35_KGramFrequencyCounts {

    public class KGramFrequencyCounter {

        private String string;
        private SuffixArrayLinearTime suffixArray;

        KGramFrequencyCounter(String string) {
            this.string = string;
            suffixArray = new SuffixArrayLinearTime(string);
        }

        // Runtime complexity: O(k lg N), where k is the kGram size
        public int countFrequency(String kGram) {
            int firstOccurrenceIndex = suffixArray.rank(kGram);

            if (compareSuffixAndKGram(firstOccurrenceIndex, kGram) != 0) {
                // K-gram is not in the text
                return 0;
            }

            int lastOccurrenceIndex = getLastOccurrenceIndex(firstOccurrenceIndex, kGram);
            return lastOccurrenceIndex - firstOccurrenceIndex + 1;
        }

        private int getLastOccurrenceIndex(int firstOccurrenceIndex, String kGram) {
            int lastOccurrenceIndex = firstOccurrenceIndex;

            int low = firstOccurrenceIndex;
            int high = suffixArray.length() - 1;

            while (low <= high) {
                int middle = low + (high - low) / 2;

                int compare = compareSuffixAndKGram(middle, kGram);

                if (compare > 0) {
                    high = middle - 1;
                } else if (compare < 0) {
                    low = middle + 1;
                } else {
                    lastOccurrenceIndex = middle;
                    low = middle + 1;
                }
            }

            return lastOccurrenceIndex;
        }

        // Runtime complexity: O(min(N, k)) time, where k is the kGram size
        private int compareSuffixAndKGram(int suffixIndex, String kGram) {
            int kGramCharIndex = 0;
            int suffixCharIndex = 0;
            int suffixLength = string.length() - suffixIndex;

            while (suffixCharIndex < suffixLength && kGramCharIndex < kGram.length()) {
                char currentSuffixChar = suffixArray.getSuffixCharAt(suffixIndex, suffixCharIndex);

                if (currentSuffixChar < kGram.charAt(kGramCharIndex)) {
                    return -1;
                } else if (currentSuffixChar > kGram.charAt(kGramCharIndex)) {
                    return 1;
                }

                suffixCharIndex++;
                kGramCharIndex++;
            }

            // If the k first characters of the suffix match, we consider it a match
            if (kGramCharIndex == kGram.length()) {
                return 0;
            }

            return suffixLength - kGram.length();
        }
    }

    public static void main(String[] args) {
        Exercise35_KGramFrequencyCounts kGramFrequencyCounts = new Exercise35_KGramFrequencyCounts();

        String text1 = "it was the best of times, it was the worst of times";
        StdOut.println("Text 1: " + text1);
        KGramFrequencyCounter kGramFrequencyCounter1 = kGramFrequencyCounts.new KGramFrequencyCounter(text1);

        String string1 = "it";
        int frequency1 = kGramFrequencyCounter1.countFrequency(string1);
        StdOut.println("Frequency for \"" + string1 + "\": " + frequency1);
        StdOut.println("Expected:           2");

        String string2 = " ";
        int frequency2 = kGramFrequencyCounter1.countFrequency(string2);
        StdOut.println("\nFrequency for \"" + string2 + "\": " + frequency2);
        StdOut.println("Expected:          11");

        String string3 = "of times";
        int frequency3 = kGramFrequencyCounter1.countFrequency(string3);
        StdOut.println("\nFrequency for \"" + string3 + "\": " + frequency3);
        StdOut.println("Expected:                 2");

        String string4 = "rene";
        int frequency4 = kGramFrequencyCounter1.countFrequency(string4);
        StdOut.println("\nFrequency for \"" + string4 + "\": " + frequency4);
        StdOut.println("Expected:             0");

        String text2 = "quicksort mergesort heapsort bubblesort";
        StdOut.println("\nText 2: " + text2);
        KGramFrequencyCounter kGramFrequencyCounter2 = kGramFrequencyCounts.new KGramFrequencyCounter(text2);

        String string5 = "sort";
        int frequency5 = kGramFrequencyCounter2.countFrequency(string5);
        StdOut.println("\nFrequency for \"" + string5 + "\": " + frequency5);
        StdOut.println("Expected:             4");
    }

}