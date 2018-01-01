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
        public double getAverageCostOfSearchMiss() {
            double loadFactor = getLoadFactor();
            return 0.5 * (1 + (1 / Math.pow(1 - loadFactor, 2)));
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

        StdOut.printf("Average cost of search miss: %.2f", linearProbingHashTableAvgSearchMissCost.getAverageCostOfSearchMiss());
    }

}
