package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;

/**
 * Created by Rene Argento on 09/03/18.
 */
public class Exercise38_BoyerMooreForRandomText {

    public class BoyerMooreWithCompareCount extends BoyerMoore {

        private int numberOfCompares = 0;

        BoyerMooreWithCompareCount(String pattern) {
            super(pattern);
        }

        @Override
        public int search(String text) {
            int textLength = text.length();
            int patternLength = pattern.length();

            int skip;

            for (int textIndex = 0; textIndex <= textLength - patternLength; textIndex += skip) {
                // Does the pattern match the text at position textIndex?
                skip = 0;

                for (int patternIndex = patternLength - 1; patternIndex >= 0; patternIndex--) {
                    numberOfCompares++;

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

        public int getNumberOfCompares() {
            return numberOfCompares;
        }
    }

    private void doExperiment(int patternLength, int textLength, int experiments) {

        long totalNumberOfCompares = 0;

        for(int experiment = 0; experiment < experiments; experiment++) {
            String randomPattern = ArrayGenerator.generateRandomStringOfSpecifiedLength(patternLength);
            String randomText = ArrayGenerator.generateRandomStringOfSpecifiedLength(textLength);

            BoyerMooreWithCompareCount boyerMooreWithCompareCount = new BoyerMooreWithCompareCount(randomPattern);
            boyerMooreWithCompareCount.search(randomText);

            totalNumberOfCompares += boyerMooreWithCompareCount.getNumberOfCompares();
        }

        double averageNumberOfCompares = totalNumberOfCompares / (double) experiments;
        StdOut.println("Average number of compares: " + averageNumberOfCompares);
    }

    // Parameters example: 10 100000 10
    public static void main(String[] args) {
        int patternLength = Integer.parseInt(args[0]);
        int textLength = Integer.parseInt(args[1]);
        int experiments = Integer.parseInt(args[2]);

        new Exercise38_BoyerMooreForRandomText().doExperiment(patternLength, textLength, experiments);
    }

}
