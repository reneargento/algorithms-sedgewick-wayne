package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 30/07/17.
 */
public class Exercise43_ParkingProblem {

    private class LinearProbingHashTableFixedSize<Key, Value> extends LinearProbingHashTable<Key, Value> {

        LinearProbingHashTableFixedSize(int size) {
            super(size);
        }

        private int totalNumberOfCompares;

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
            }

            totalNumberOfCompares++;

            int tableIndex;
            for(tableIndex = hash(key); keys[tableIndex] != null; tableIndex = (tableIndex + 1) % size) {
                totalNumberOfCompares++;

                if (keys[tableIndex].equals(key)) {
                    values[tableIndex] = value;
                    return;
                }
            }

            keys[tableIndex] = key;
            values[tableIndex] = value;
            keysSize++;
        }
    }

    private void doExperiment() {
        StdOut.printf("%12s %20s %20s\n", "Hash table size | ","Number of compares | ", "Expected number of compares");

        int[] hashTableSizes = {1000, 10000, 100000, 1000000};
        for(int hashTableSize : hashTableSizes) {
            putKeysAndPrintResults(hashTableSize);
        }
    }

    private void putKeysAndPrintResults(int hashTableSize) {

        LinearProbingHashTableFixedSize<Integer, Integer> linearProbingHashTable = new LinearProbingHashTableFixedSize<>(hashTableSize);

        for(int i = 0; i < hashTableSize; i++) {
            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            linearProbingHashTable.put(randomKey, randomKey);
        }

        long totalNumberOfCompares = linearProbingHashTable.totalNumberOfCompares;

        double constant = Math.sqrt(Math.PI / 2);
        long expectedTotalNumberOfCompares = (long) (constant * Math.pow(hashTableSize, 1.5));

        printResults(hashTableSize, totalNumberOfCompares, expectedTotalNumberOfCompares);
    }

    private void printResults(int hashTableSize, long totalNumberOfCompares, long expectedTotalNumberOfCompares) {
        StdOut.printf("%15d %21d %30d\n", hashTableSize, totalNumberOfCompares, expectedTotalNumberOfCompares);
    }

    public static void main(String[] args) {
        new Exercise43_ParkingProblem().doExperiment();
    }

}
