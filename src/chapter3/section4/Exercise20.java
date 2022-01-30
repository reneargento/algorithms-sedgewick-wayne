package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 21/07/17.
 */
// Thanks to faame (https://github.com/faame) for fixing a bug in the compare count in this exercise.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/236
public class Exercise20 {

    private class LinearProbingHashTableAvgSearchHitCost<Key, Value> extends LinearProbingHashTable<Key, Value> {

        LinearProbingHashTableAvgSearchHitCost(int size) {
            super(size);
        }

        private double totalNumberOfComparesForSearchHit;

        public double getAverageCostOfSearchHit() {
            return totalNumberOfComparesForSearchHit / keysSize;
        }

        private void resize(int newSize) {
            LinearProbingHashTableAvgSearchHitCost<Key, Value> tempHashTable =
                    new LinearProbingHashTableAvgSearchHitCost<>(newSize);

            for (int i = 0; i < size; i++) {
                if (keys[i] != null) {
                    tempHashTable.put(keys[i], values[i]);
                }
            }

            keys = tempHashTable.keys;
            values = tempHashTable.values;
            size = tempHashTable.size;
            totalNumberOfComparesForSearchHit = tempHashTable.totalNumberOfComparesForSearchHit;
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }
            int numberOfComparesBeforeFindingKey = 0;

            for (int tableIndex = hash(key); keys[tableIndex] != null; tableIndex = (tableIndex + 1) % size) {
                numberOfComparesBeforeFindingKey++;

                if (keys[tableIndex].equals(key)) {
                    totalNumberOfComparesForSearchHit += numberOfComparesBeforeFindingKey;
                    return values[tableIndex];
                }
            }
            return null;
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            if (keysSize >= size / (double) 2) {
                resize(size * 2);
                lgM++;
            }

            int numberOfComparesBeforeFindingKey = 0;

            int tableIndex;
            for (tableIndex = hash(key); keys[tableIndex] != null; tableIndex = (tableIndex + 1) % size) {
                numberOfComparesBeforeFindingKey++;

                if (keys[tableIndex].equals(key)) {
                    totalNumberOfComparesForSearchHit += numberOfComparesBeforeFindingKey;
                    values[tableIndex] = value;
                    return;
                }
            }

            keys[tableIndex] = key;
            values[tableIndex] = value;
            keysSize++;
        }
    }

    public static void main(String[] args) {
        Exercise20 exercise20 = new Exercise20();
        LinearProbingHashTableAvgSearchHitCost<Integer, Integer> linearProbingHashTableAvgSearchHitCost =
                exercise20.new LinearProbingHashTableAvgSearchHitCost<>(20);

        linearProbingHashTableAvgSearchHitCost.put(5, 5);
        StdOut.println(linearProbingHashTableAvgSearchHitCost.getAverageCostOfSearchHit() + " Expected: 0.0");

        linearProbingHashTableAvgSearchHitCost.get(5);
        StdOut.println(linearProbingHashTableAvgSearchHitCost.getAverageCostOfSearchHit() + " Expected: 1.0");

        linearProbingHashTableAvgSearchHitCost.get(5);
        StdOut.println(linearProbingHashTableAvgSearchHitCost.getAverageCostOfSearchHit() + " Expected: 2.0");

        linearProbingHashTableAvgSearchHitCost.put(5, 5);
        StdOut.println(linearProbingHashTableAvgSearchHitCost.getAverageCostOfSearchHit() + " Expected: 3.0");

        linearProbingHashTableAvgSearchHitCost.put(7, 7);
        StdOut.println(linearProbingHashTableAvgSearchHitCost.getAverageCostOfSearchHit() + " Expected: 1.5");
    }
}
