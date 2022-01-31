package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 21/07/17.
 */
public class Exercise21 {

    private class LinearProbingHashTableAvgSearchMissCost<Key, Value> extends LinearProbingHashTable<Key, Value> {

        LinearProbingHashTableAvgSearchMissCost(int size) {
            super(size);
        }

        // Average cost of search miss = ~1/2 * (1 + (1 / (1 - a)^2))
        public double getAverageCostOfSearchMissByLoadFactor() {
            double loadFactor = getLoadFactor();
            return 0.5 * (1 + (1 / Math.pow(1 - loadFactor, 2)));
        }

        // Average cost of search miss = 1 + N / 2M + (sum((cluster[i].size)^2)) / 2M for all clusters in the table
        public double getAverageCostOfSearchMissByClusterSizes() {
            int clusterSize = 0;
            int clusterSizeSquareSum = 0;
            
            for (int i = 0; i < size; i++) {
                if (keys[i] != null) {
                    clusterSize++;
                } else {
                    clusterSizeSquareSum += clusterSize * clusterSize;
                    clusterSize = 0;
                }
            }

            if (clusterSize != 0) {
                clusterSizeSquareSum += clusterSize * clusterSize;
            }
            return 1 + ((double) (keysSize + clusterSizeSquareSum)) / (size * 2);
        }
    }

    public static void main(String[] args) {
        Exercise21 exercise21 = new Exercise21();
        LinearProbingHashTableAvgSearchMissCost<Integer, Integer> linearProbingHashTableAvgSearchMissCost =
                exercise21.new LinearProbingHashTableAvgSearchMissCost<>(1000000);

        for (int i = 0; i < 500000; i++) {
            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            linearProbingHashTableAvgSearchMissCost.put(randomKey, randomKey);
        }

        StdOut.printf("Average cost of search miss by load factor: %.2f\n",
                linearProbingHashTableAvgSearchMissCost.getAverageCostOfSearchMissByLoadFactor());
        StdOut.printf("Average cost of search miss by cluster sizes: %.2f\n",
                linearProbingHashTableAvgSearchMissCost.getAverageCostOfSearchMissByClusterSizes());
    }
}
