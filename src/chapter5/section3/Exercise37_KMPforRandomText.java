package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;

/**
 * Created by Rene Argento on 09/03/18.
 */
public class Exercise37_KMPforRandomText {

    public class KnuthMorrisPrattWithCompareCount extends KnuthMorrisPratt {

        private int numberOfCompares = 0;

        KnuthMorrisPrattWithCompareCount(String pattern) {
            super(pattern);
        }

        @Override
        public int search(String text) {
            int textIndex;
            int patternIndex;
            int textLength = text.length();
            int patternLength = pattern.length();

            for (textIndex = 0, patternIndex = 0; textIndex < textLength && patternIndex < patternLength; textIndex++) {
                patternIndex = dfa[text.charAt(textIndex)][patternIndex];
                numberOfCompares++;
            }
            if (patternIndex == patternLength) {
                return textIndex - patternLength; // found
            } else {
                return textLength;                // not found
            }
        }

        public int getNumberOfCompares() {
            return numberOfCompares;
        }
    }

    private void doExperiment(int patternLength, int textLength, int experiments) {

        long totalNumberOfCompares = 0;

        for(int experiment = 0; experiment < experiments; experiment++) {
            String randomPattern = ArrayGenerator.generateRandomStringOfSpecifiedLength(patternLength);
            String randomText = ArrayGenerator.generateRandomStringOfSpecifiedLength(textLength);

            KnuthMorrisPrattWithCompareCount knuthMorrisPrattWithCompareCount =
                    new KnuthMorrisPrattWithCompareCount(randomPattern);
            knuthMorrisPrattWithCompareCount.search(randomText);

            totalNumberOfCompares += knuthMorrisPrattWithCompareCount.getNumberOfCompares();
        }

        double averageNumberOfCompares = totalNumberOfCompares / (double) experiments;
        StdOut.println("Average number of compares: " + averageNumberOfCompares);
    }

    // Parameters example: 10 100000 10
    public static void main(String[] args) {
        int patternLength = Integer.parseInt(args[0]);
        int textLength = Integer.parseInt(args[1]);
        int experiments = Integer.parseInt(args[2]);

        new Exercise37_KMPforRandomText().doExperiment(patternLength, textLength, experiments);
    }

}
