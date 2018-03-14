package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 21/07/17.
 */
public class Exercise20 {

    private class LinearProbingHashTableAvgSearchHitCost<Key, Value> extends LinearProbingHashTable<Key, Value> {

        LinearProbingHashTableAvgSearchHitCost(int size) {
            super(size);
        }

        private long totalNumberOfComparesForSearchHit;

        public long getAverageCostOfSearchHit() {
            return totalNumberOfComparesForSearchHit / keysSize;
        }

        private void resize(int newSize) {
            LinearProbingHashTableAvgSearchHitCost<Key, Value> tempHashTable =
                    new LinearProbingHashTableAvgSearchHitCost<>(newSize);

            for(int i = 0; i < size; i++) {
                if (keys[i] != null) {
                    tempHashTable.put(keys[i], values[i]);
                }
            }

            keys = tempHashTable.keys;
            values = tempHashTable.values;
            size = tempHashTable.size;
            totalNumberOfComparesForSearchHit = tempHashTable.totalNumberOfComparesForSearchHit;
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

            int numberOfComparesBeforeFindingKey = 1;

            int tableIndex;
            for(tableIndex = hash(key); keys[tableIndex] != null; tableIndex = (tableIndex + 1) % size) {
                numberOfComparesBeforeFindingKey++;

                if (keys[tableIndex].equals(key)) {
                    values[tableIndex] = value;
                    return;
                }
            }

            totalNumberOfComparesForSearchHit += numberOfComparesBeforeFindingKey;

            keys[tableIndex] = key;
            values[tableIndex] = value;
            keysSize++;
        }
    }

    //Class to enable hash collisions test
    private class TestKey {
        int key;

        TestKey(int key) {
            this.key = key;
        }

        @Override
        public int hashCode() {
            return key % 4;
        }
    }

    public static void main(String[] args) {
        Exercise20 exercise20 = new Exercise20();
        LinearProbingHashTableAvgSearchHitCost<TestKey, Integer> linearProbingHashTableAvgSearchHitCost =
                exercise20.new LinearProbingHashTableAvgSearchHitCost<>(20);

        //Hash code 1
        linearProbingHashTableAvgSearchHitCost.put(exercise20.new TestKey(5), 5);
        StdOut.println(linearProbingHashTableAvgSearchHitCost.getAverageCostOfSearchHit() + " Expected: 1");

        //Hash code 0
        linearProbingHashTableAvgSearchHitCost.put(exercise20.new TestKey(8), 8);
        //Hash code 2
        linearProbingHashTableAvgSearchHitCost.put(exercise20.new TestKey(2), 2);
        StdOut.println(linearProbingHashTableAvgSearchHitCost.getAverageCostOfSearchHit() + " Expected: 1");

        //Hash code 1 -> hash collision and is sent to index 3
        linearProbingHashTableAvgSearchHitCost.put(exercise20.new TestKey(1), 1);
        //Hash code 1 -> hash collision and is sent to index 4
        linearProbingHashTableAvgSearchHitCost.put(exercise20.new TestKey(9), 9);
        StdOut.println(linearProbingHashTableAvgSearchHitCost.getAverageCostOfSearchHit() + " Expected: 2");
    }

}
