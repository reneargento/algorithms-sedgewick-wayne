package chapter5.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Rene Argento on 18/02/18.
 */
public class Exercise23_DuplicatesRevisitedAgain {

    private class DistinctCounter {

        public int deDupWithIntegerValuesAndHashSet(int numberOfValues, int maxValue) {
            // Using Java's super optimized Set and HashSet since we are dealing with very large numbers
            // and every optimization counts
            java.util.Set<Integer> hashSet = new java.util.HashSet<>();

            for(int i = 0; i < numberOfValues; i++) {
                int key = StdRandom.uniform(maxValue);

                if (!hashSet.contains(key)) {
                    hashSet.add(key);
                }
            }

            return hashSet.size();
        }

        public long deDupWithLongValuesAndHashSet(int numberOfValues, long maxValue) {
            // Using Java's super optimized Set and HashSet since we are dealing with very large numbers
            // and every optimization counts
            java.util.Set<Long> hashSet = new java.util.HashSet<>();

            for(int i = 0; i < numberOfValues; i++) {
                long key = ThreadLocalRandom.current().nextLong(maxValue);

                if (!hashSet.contains(key)) {
                    hashSet.add(key);
                }
            }

            return hashSet.size();
        }

        public int deDupWithIntegerValuesAndStringSet(int numberOfValues, int maxValue) {
            StringSet stringSet = new StringSet();

            for(int i = 0; i < numberOfValues; i++) {
                int key = StdRandom.uniform(maxValue);
                String stringKey = String.valueOf(key);

                if (!stringSet.contains(stringKey)) {
                    stringSet.add(stringKey);
                }
            }

            return stringSet.size();
        }

        public long deDupWithLongValuesAndStringSet(int numberOfValues, long maxValue) {
            StringSet stringSet = new StringSet();

            for(int i = 0; i < numberOfValues; i++) {
                long key = ThreadLocalRandom.current().nextLong(maxValue);
                String stringKey = String.valueOf(key);

                if (!stringSet.contains(stringKey)) {
                    stringSet.add(stringKey);
                }
            }

            return stringSet.size();
        }

        private int countDistinctIntegerValuesUsingIndex(int numberOfValues, int maxValue) {
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

        private long countDistinctLongValuesUsingIndex(int numberOfValues, int maxValue) {
            long[] values = new long[maxValue];

            for (int i = 0; i < numberOfValues; i++) {
                long key = ThreadLocalRandom.current().nextLong(maxValue);
                values[(int) key]++; // Ok to cast since the max value is 2x 10^9
            }

            int distinctValues = 0;

            for (long value : values) {
                if (value != 0) {
                    distinctValues++;
                }
            }

            return distinctValues;
        }
    }

    private void doExperiments() {
        StdOut.printf("%21s %17s %16s %15s %15s %10s\n", "Method | ", "Data structure |", "Values type | ",
                "Values Generated | ", "Max Value | ", "Time spent");

        int[] initialValues = {1000, 10000, 100000, 1000000};
        int[] bigValues = {10000000, 100000000, 1000000000};

        // Generating integer values
        countDistinctValues(false, false, false, initialValues);
        countDistinctValues(true, false,false, initialValues);
        countDistinctValues(true, true,false, initialValues);
        countDistinctValues(true, false, false, bigValues);
        countDistinctValues(true, true, false, bigValues);

        // Generating long values
        countDistinctValues(false, false,true, initialValues);
        countDistinctValues(true, false, true, initialValues);
        countDistinctValues(true, true, true, initialValues);
        countDistinctValues(true, false, true, bigValues);
        countDistinctValues(true, true, true, bigValues);
    }

    public void countDistinctValues(boolean useDedup, boolean useStringSet, boolean generateLongValues, int[] values) {
        int numberOfTrials = 10;

        String method;
        String dataStructure;
        String valuesType;

        if (useDedup) {
            method = "DeDup";

            if (!useStringSet) {
                dataStructure = "HashSet";
            } else {
                dataStructure = "StringSet";
            }
        } else {
            method = "Array index count";
            dataStructure = "Array";
        }

        if (!generateLongValues) {
            valuesType = "Integer";
        } else {
            valuesType = "Long";
        }

        /**
         * T = 10
         * N = 10^3, 10^4, 10^5, 10^6, 10^7, 10^8, 10^9
         * M = N/2, N, 2N
         */

        DistinctCounter distinctCounter = new DistinctCounter();

        for(int n = 0; n < values.length; n++) {
            for(int m = 0; m < 3; m++) {
                int numberOfValues = values[n];

                int maxValue = 0;
                if (m == 0) {
                    maxValue = numberOfValues / 2;
                } else if (m == 1) {
                    maxValue = numberOfValues;
                } else if (m == 2) {
                    maxValue = 2 * numberOfValues;
                }

                Stopwatch stopwatch = new Stopwatch();

                for(int trial = 0; trial < numberOfTrials; trial++) {

                    // Count the distinct values, but will not be used in this exercise since we are interested
                    // only in the running time
                    long distinctValues;

                    if (!useDedup) {
                        if (!generateLongValues) {
                            distinctValues = distinctCounter.countDistinctIntegerValuesUsingIndex(numberOfValues, maxValue);
                        } else {
                            distinctValues = distinctCounter.countDistinctLongValuesUsingIndex(numberOfValues, maxValue);
                        }
                    } else {
                        if (!generateLongValues) {
                            if (!useStringSet) {
                                distinctValues = distinctCounter.deDupWithIntegerValuesAndHashSet(numberOfValues, maxValue);
                            } else {
                                distinctValues = distinctCounter.deDupWithIntegerValuesAndStringSet(numberOfValues, maxValue);
                            }
                        } else {
                            if (!useStringSet) {
                                distinctValues = distinctCounter.deDupWithLongValuesAndHashSet(numberOfValues, maxValue);
                            } else {
                                distinctValues = distinctCounter.deDupWithLongValuesAndStringSet(numberOfValues, maxValue);
                            }
                        }
                    }
                }

                double timeSpent = stopwatch.elapsedTime();
                printResults(method, dataStructure, valuesType, numberOfValues, maxValue, timeSpent);
            }
        }
    }

    public static void main(String[] args) {
        new Exercise23_DuplicatesRevisitedAgain().doExperiments();
    }

    private void printResults(String method, String dataStructure, String valuesType, int numberOfValues,
                              int maxValue, double timeSpent) {
        StdOut.printf("%18s %18s %15s %19d %15d %13.2f\n", method, dataStructure, valuesType, numberOfValues,
                maxValue, timeSpent);
    }

}
