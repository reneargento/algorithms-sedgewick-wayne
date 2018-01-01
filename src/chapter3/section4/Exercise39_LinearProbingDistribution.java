package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 29/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise39_LinearProbingDistribution {

    private class LinearProbingHashTableAvgSearchMissCost<Key, Value> extends LinearProbingHashTable<Key, Value> {

        LinearProbingHashTableAvgSearchMissCost(int size) {
            super(size);
        }

        public double getAverageCostOfSearchMissFromClusterLengths() {
            long totalCostOfSearchMisses = 0;

            //Simulates a search miss starting at each array position
            for(int i = 0; i < size; i++) {
                int index = i;
                totalCostOfSearchMisses++;

                while (keys[index] != null) {
                    totalCostOfSearchMisses++;
                    index = (index + 1) % size;
                }
            }

            return totalCostOfSearchMisses / (double) size;
        }

        //Proposition M
        //Expected average cost of search miss = ~1/2 * (1 + (1 / (1 - a)^2))
        public double getExpectedAverageCostOfSearchMiss() {
            double loadFactor = getLoadFactor();
            return 0.5 * (1 + (1 / Math.pow(1 - loadFactor, 2)));
        }
    }

    private void doExperiment() {
        StdOut.printf("%12s %20s %25s\n", "Table size | ","Avg cost of search miss | ", "Expected avg cost of search miss");

        int[] hashTableSizes = {1000, 10000, 100000, 1000000};

        for(int hashTableSizeIndex = 0; hashTableSizeIndex < hashTableSizes.length; hashTableSizeIndex++) {
            int hashTableSize = hashTableSizes[hashTableSizeIndex];

            LinearProbingHashTableAvgSearchMissCost<Integer, Integer> linearProbingHashTableAvgSearchMissCost =
                    new LinearProbingHashTableAvgSearchMissCost(hashTableSize);

            for(int i = 0; i < hashTableSize / 2; i++) {
                int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
                linearProbingHashTableAvgSearchMissCost.put(randomKey, randomKey);
            }

            double averageCostOfSearchMissFromClusterLengths = linearProbingHashTableAvgSearchMissCost.getAverageCostOfSearchMissFromClusterLengths();
            double expectedAverageCostOfSearchMiss = linearProbingHashTableAvgSearchMissCost.getExpectedAverageCostOfSearchMiss();
            printResults(hashTableSize, averageCostOfSearchMissFromClusterLengths, expectedAverageCostOfSearchMiss);
        }
    }

    private void printResults(int hashTableSize, double averageCostOfSearchMiss, double expectedAverageCostOfSearchMiss) {
        StdOut.printf("%10d %26.2f %35.2f\n", hashTableSize, averageCostOfSearchMiss, expectedAverageCostOfSearchMiss);
    }

    public static void main(String[] args) {
        new Exercise39_LinearProbingDistribution().doExperiment();
    }

}