package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise29_Deletion {

    private class SeparateChainingHashTableDoubleProbingDelete<Key, Value> extends DoubleProbingHashTable<Key, Value> {

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty() || !contains(key)) {
                return;
            }

            keysSize--;

            int hash1 = hash1(key);
            int hash2 = hash2(key);

            if (!symbolTable[hash1].isEmpty() &&
                    (symbolTable[hash1].size <= symbolTable[hash2].size || symbolTable[hash2].isEmpty())) {
                int symbolTableSize = symbolTable[hash1].size;

                symbolTable[hash1].delete(key);
                //Key is not on the shorter list
                if (symbolTableSize == symbolTable[hash1].size) {
                    symbolTable[hash2].delete(key);
                }
            } else {
                int symbolTableSize = symbolTable[hash2].size;

                symbolTable[hash2].delete(key);
                //Key is not on the shorter list
                if (symbolTableSize == symbolTable[hash2].size) {
                    symbolTable[hash1].delete(key);
                }
            }

            if (size > 0 && getLoadFactor() <= averageListSize / (double) 4) {
                resize(size / 2);
            }
        }
    }

    private class DoubleHashingHashTableDelete<Key, Value> extends DoubleHashingHashTable<Key, Value> {

        DoubleHashingHashTableDelete(int size) {
            super(size);
        }

        //Eager delete in double hashing requires a rehash() operation, making it O(n)
        public void eagerDelete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (!contains(key)) {
                return;
            }

            int tableIndex = hash(key);
            while (!keys[tableIndex].equals(key)) {
                tableIndex = (tableIndex + secondaryHash(key)) % size;
            }

            keysSize--;
            keys[tableIndex] = null;
            values[tableIndex] = null;

            if (keysSize <= size / (double) 8) {
                resize(size / 2);
                lgM--;
            } else {
                rehash();
            }
        }

        private void rehash() {
            tombstoneItemCount = 0;

            DoubleHashingHashTableDelete<Key, Value> tempHashTable =
                    new DoubleHashingHashTableDelete<>(size);

            for(int i = 0; i < keys.length; i++) {
                if (values[i] != null) {
                    tempHashTable.put(keys[i], values[i]);
                }
            }

            keys = tempHashTable.keys;
            values = tempHashTable.values;
        }
    }

    public static void main(String[] args) {
        Exercise29_Deletion deletion = new Exercise29_Deletion();
        //Double probing test
        SeparateChainingHashTableDoubleProbingDelete separateChainingHashTableDoubleProbingDelete =
                deletion.new SeparateChainingHashTableDoubleProbingDelete();

        for(int key = 1; key <= 100; key++) {
            separateChainingHashTableDoubleProbingDelete.put(key, key);
        }
        StdOut.println("Size: " + separateChainingHashTableDoubleProbingDelete.size() + " Expected: 100");

        for(int key = 1; key <= 100; key += 2) {
            separateChainingHashTableDoubleProbingDelete.delete(key);
        }
        StdOut.println("Size: " + separateChainingHashTableDoubleProbingDelete.size() + " Expected: 50");

        //Double hashing test
        DoubleHashingHashTableDelete doubleHashingHashTableDelete = deletion.new DoubleHashingHashTableDelete(50);

        for(int key = 1; key <= 100; key++) {
            doubleHashingHashTableDelete.put(key, key);
        }
        StdOut.println("\nSize: " + doubleHashingHashTableDelete.size() + " Expected: 100");

        for(int key = 1; key <= 100; key += 2) {
            doubleHashingHashTableDelete.eagerDelete(key);
        }
        StdOut.println("Size: " + doubleHashingHashTableDelete.size() + " Expected: 50");
    }

}
