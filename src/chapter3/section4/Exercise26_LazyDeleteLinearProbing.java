package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rene Argento on 22/07/17.
 */
public class Exercise26_LazyDeleteLinearProbing {

    private class LinearProbingHashTableLazyDelete<Key, Value> extends LinearProbingHashTable<Key, Value> {

        private int tombstoneItemCount;

        LinearProbingHashTableLazyDelete(int size) {
            super(size);
        }

        private void resize(int newSize) {
            StdOut.println("Deleting tombstone items");
            tombstoneItemCount = 0;

            //Resize
            LinearProbingHashTableLazyDelete<Key, Value> tempHashTable = new LinearProbingHashTableLazyDelete<>(newSize);
            for(int i = 0; i < size; i++) {
                if (values[i] != null) {
                    tempHashTable.put(keys[i], values[i]);
                }
            }

            keys = tempHashTable.keys;
            values = tempHashTable.values;
            size = tempHashTable.size;
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            if (keysSize + tombstoneItemCount >= size / (double) 2) {
                resize(size * 2);
                lgM++;
            }

            int tableIndex;
            for(tableIndex = hash(key); keys[tableIndex] != null; tableIndex = (tableIndex + 1) % size) {
                if (keys[tableIndex].equals(key)) {

                    if (values[tableIndex] == null) {
                        tombstoneItemCount--;
                        keysSize++;
                    }

                    values[tableIndex] = value;
                    return;
                }
            }

            keys[tableIndex] = key;
            values[tableIndex] = value;
            keysSize++;
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (!contains(key)) {
                return;
            }

            int tableIndex = hash(key);
            while (!keys[tableIndex].equals(key)) {
                tableIndex = (tableIndex + 1) % size;
            }

            keys[tableIndex] = null;
            values[tableIndex] = null;
            keysSize--;

            tableIndex = (tableIndex + 1) % size;

            while (keys[tableIndex] != null) {
                Key keyToRedo = keys[tableIndex];
                Value valueToRedo = values[tableIndex];

                keys[tableIndex] = null;
                values[tableIndex] = null;
                keysSize--;

                put(keyToRedo, valueToRedo);
                tableIndex = (tableIndex + 1) % size;
            }

            if (keysSize > 1 && keysSize <= size / (double) 8) {
                resize(size / 2);
                lgM--;
            }
        }

        public void lazyDelete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (!contains(key)) {
                return;
            }

            int tableIndex = hash(key);
            while (!keys[tableIndex].equals(key)) {
                tableIndex = (tableIndex + 1) % size;
            }

            values[tableIndex] = null;
            tombstoneItemCount++;

            keysSize--;

            if (keysSize <= size / (double) 8) {
                resize(size / 2);
                lgM--;
            }
        }
    }

    public static void main(String[] args) {
        Exercise26_LazyDeleteLinearProbing lazyDeleteLinearProbing = new Exercise26_LazyDeleteLinearProbing();
        LinearProbingHashTableLazyDelete<Integer, Integer> linearProbingHashTableLazyDelete =
                lazyDeleteLinearProbing.new LinearProbingHashTableLazyDelete<>(16);

        for(int i = 0; i < 4; i++) {
            linearProbingHashTableLazyDelete.put(i, i);
        }

        StdOut.println("Size: " + linearProbingHashTableLazyDelete.size() + " Expected: 4");
        StdOut.println("Lazy delete 2");
        linearProbingHashTableLazyDelete.lazyDelete(2);
        StdOut.println("Size: " + linearProbingHashTableLazyDelete.size() + " Expected: 3");
        StdOut.println("Lazy delete 0");
        linearProbingHashTableLazyDelete.lazyDelete(0);
        StdOut.println("Expected: Deleting tombstone items");
        StdOut.println("Size: " + linearProbingHashTableLazyDelete.size() + " Expected: 2");

        for(int i = 4; i < 8; i++) {
            linearProbingHashTableLazyDelete.put(i, i);
        }

        StdOut.println("\nLazy delete 3");
        linearProbingHashTableLazyDelete.lazyDelete(3);
        StdOut.println("Lazy delete 4");
        linearProbingHashTableLazyDelete.lazyDelete(4);
        StdOut.println("Lazy delete 5");
        linearProbingHashTableLazyDelete.lazyDelete(5);
        StdOut.println("Put 3");
        linearProbingHashTableLazyDelete.put(3, 3);
        StdOut.println("Lazy delete 6");
        linearProbingHashTableLazyDelete.lazyDelete(6);
        StdOut.println("Lazy delete 7");
        linearProbingHashTableLazyDelete.lazyDelete(7);
        StdOut.println("Expected: Deleting tombstone items");
        StdOut.println("Size: " + linearProbingHashTableLazyDelete.size() + " Expected: 2");
    }

}
