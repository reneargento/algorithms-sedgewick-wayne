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

        //Average cost of search miss = ~1/2 * (1 + (1 / (1 - a)^2))
        public double getAverageCostOfSearchMiss_byLoadFactor() {
            double loadFactor = getLoadFactor();
            return 0.5 * (1 + (1 / Math.pow(1 - loadFactor, 2)));

    }

    //Average cost of search miss = 1 + N/2M + (sum(sqrt(cluster[i].size)))/2M for all clusters in the table
    public double getAverageCostOfSearchMiss_byClusterSizes() {
      int clusterSize = 0;
      int clusterSizeSqrtSum = 0;
      for (int i = 0; i < size; i++) {
        if (keys[i] != null) {
          clusterSize++;
        }
        else {
          clusterSizeSqrtSum += clusterSize * clusterSize;
          clusterSize = 0;
        }
      }

      return 1 + ((double)(keysSize + clusterSizeSqrtSum))/ (size << 1);
        }
    }

    public static void main(String[] args) {
        Exercise21 exercise21 = new Exercise21();
        LinearProbingHashTableAvgSearchMissCost<Integer, Integer> linearProbingHashTableAvgSearchMissCost =
                exercise21.new LinearProbingHashTableAvgSearchMissCost<>(1000000);

        for(int i = 0; i < 500000; i++) {
            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            linearProbingHashTableAvgSearchMissCost.put(randomKey, randomKey);
        }

    StdOut.printf("Average cost of search miss: byLoadFactor=%.2f, byClusterSizes=%.2f",
        linearProbingHashTableAvgSearchMissCost.getAverageCostOfSearchMiss_byLoadFactor(),
        linearProbingHashTableAvgSearchMissCost.getAverageCostOfSearchMiss_byClusterSizes());
    }

}
