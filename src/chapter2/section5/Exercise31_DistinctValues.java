package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rene Argento on 16/04/17.
 */
public class Exercise31_DistinctValues {

    // Parameters example: 500000 1000000 10
    public static void main(String[] args) {

        Exercise31_DistinctValues distinctValues = new Exercise31_DistinctValues();

        if (args.length == 3) {
            int maxValue = Integer.parseInt(args[0]);
            int numberOfValues = Integer.parseInt(args[1]);
            int numberOfTrials = Integer.parseInt(args[2]);

            int distinct[] = new int[numberOfTrials];
            int distinctArrayIndex = 0;

            for(int i = 0; i < numberOfTrials; i++) {
                int numberOfDistinctValues = distinctValues.countDistinctValues(numberOfValues, maxValue);
                distinct[distinctArrayIndex++] = numberOfDistinctValues;
            }

            double distinctValuesMean = StdStats.mean(distinct);
            StdOut.printf("Number of distinct values: %.2f", distinctValuesMean);

            double alpha = ((double) numberOfValues) / ((double) maxValue);
            double expectedValue = maxValue * (1 - Math.exp(-alpha));
            StdOut.printf("\nExpected:  %.2f", expectedValue);
        } else {
            distinctValues.doExperiment();
        }
    }

    public void doExperiment() {

        int numberOfTrials = 10;

        /**
         * T = 10
         * N = 10^3, 10^4, 10^5, 10^6
         * M = N/2, N, 2N
         */
        int[] values = {1000, 10000, 100000, 1000000};

        StdOut.printf("%13s %13s %13s %23s\n", "Values Generated | ", "Max Value | ", "Distinct Values | "
                , "Expected Distinct Values");

        for(int n = 0; n < values.length; n++) {
            for(int m = 0 ; m < 3; m++) {
                int numberOfValues = values[n];

                int maxValue = 0;
                if (m == 0) {
                    maxValue = numberOfValues / 2;
                } else if (m == 1) {
                    maxValue = numberOfValues;
                } else if (m == 2) {
                    maxValue = 2 * numberOfValues;
                }

                int distinct[] = new int[numberOfTrials];
                int distinctArrayIndex = 0;

                for(int trial = 0; trial < numberOfTrials; trial++) {
                    int distinctValues = countDistinctValues(numberOfValues, maxValue);
                    distinct[distinctArrayIndex++] = distinctValues;
                }

                double distinctValuesMean = StdStats.mean(distinct);
                double alpha = ((double) numberOfValues) / ((double) maxValue);
                double expectedValue = maxValue * (1 - Math.pow(Math.E, -alpha));

                printResults(numberOfValues, maxValue, distinctValuesMean, expectedValue);
            }
        }
    }

    private int countDistinctValues(int numberOfValues, int maxValue) {
        int[] values = new int[maxValue];

        for(int i = 0; i < numberOfValues; i++) {
            int generatedValue = StdRandom.uniform(maxValue);
            values[generatedValue]++;
        }

        int distinctValues = 0;

        for (int value : values) {
            if (value != 0) {
                distinctValues++;
            }
        }

        return distinctValues;
    }

    private void printResults(int numberOfValues, int maxValue, double distinctValues, double expectedValue) {
        StdOut.printf("%16d %13d %18.2f %27.2f\n", numberOfValues, maxValue, distinctValues, expectedValue);
    }

}
