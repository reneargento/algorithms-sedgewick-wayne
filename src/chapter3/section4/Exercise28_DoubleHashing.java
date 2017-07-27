package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by rene on 22/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise28_DoubleHashing {

    static class DoubleHashingHashTable<Key, Value> extends LinearProbingHashTable<Key, Value> {

        int tombstoneItemCount;

        DoubleHashingHashTable(int size) {
            super(size);
        }

        int secondaryHash(Key key) {
            int hash = key.hashCode() & 0x7fffffff;
            int hash2 = hash;

            Set<Integer> hashTableSizeFactors = getFactors(size);

            for(int i = 1; i < size; i++) {
                int modHash = (i + hash) % size;

                if(!hashTableSizeFactors.contains(modHash)) {
                    hash2 = modHash;
                    break;
                }
            }

            hash2 = hash2 != 0? hash2 : 1;
            return hash2;
        }

        private Set<Integer> getFactors(int number) {
            Set<Integer> factors = new HashSet<>();

            int sqrt = (int) Math.sqrt(number);

            for(int i = 1; i <= sqrt; i++) {

                if(number % i == 0) {
                    factors.add(i);

                    if(i != number / i) {
                        factors.add(number / i);
                    }
                }
            }

            return factors;
        }

        void resize(int newSize) {
            StdOut.println("Deleting tombstone items");
            tombstoneItemCount = 0;

            DoubleHashingHashTable<Key, Value> tempHashTable = new DoubleHashingHashTable<>(newSize);

            for(int i = 0; i < size; i++) {
                if(values[i] != null) {
                    tempHashTable.put(keys[i], values[i]);
                }
            }

            keys = tempHashTable.keys;
            values = tempHashTable.values;
            size = tempHashTable.size;
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            for(int tableIndex = hash(key); keys[tableIndex] != null; tableIndex = (tableIndex + secondaryHash(key)) % size) {
                if(keys[tableIndex].equals(key)) {
                    return values[tableIndex];
                }
            }

            return null;
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if(value == null) {
                delete(key);
                return;
            }

            if(keysSize + tombstoneItemCount >= size / (double) 2) {
                resize(size * 2);
                lgM++;
            }

            int tableIndex;
            for(tableIndex = hash(key); keys[tableIndex] != null; tableIndex = (tableIndex + secondaryHash(key)) % size) {
                if(keys[tableIndex].equals(key)) {

                    if(values[tableIndex] == null) {
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

            if(!contains(key)) {
                return;
            }

            int tableIndex = hash(key);
            while (!keys[tableIndex].equals(key)) {
                tableIndex = (tableIndex + secondaryHash(key)) % size;
            }

            keysSize--;

            values[tableIndex] = null;
            tombstoneItemCount++;

            if(keysSize <= size / (double) 8) {
                resize(size / 2);
                lgM--;
            }
        }
    }

    //Test class to force collisions and the use of double hashing
    private static class TestClass {
        int key;

        TestClass(int key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object that) {
            if(!(that instanceof TestClass)) {
                return false;
            }
            return this.key == ((TestClass) that).key;
        }

        @Override
        public int hashCode() {
            return 4;
        }
    }

    public static void main(String[] args) {
        DoubleHashingHashTable<TestClass, Integer> doubleHashingHashTable = new DoubleHashingHashTable<>(7);
        for(int i = 0; i < 4; i++) {
            doubleHashingHashTable.put(new TestClass(i), i);
        }

        StdOut.println("Size: " + doubleHashingHashTable.size() + " Expected: 4");
        doubleHashingHashTable.delete(new TestClass(2));
        StdOut.println("Size: " + doubleHashingHashTable.size() + " Expected: 3");
        doubleHashingHashTable.delete(new TestClass(0));
        StdOut.println("Size: " + doubleHashingHashTable.size() + " Expected: 2");

        for(int i = 4; i < 100; i++) {
            doubleHashingHashTable.put(new TestClass(i), i);
        }

        doubleHashingHashTable.delete(new TestClass(3));
        doubleHashingHashTable.delete(new TestClass(4));
        doubleHashingHashTable.delete(new TestClass(5));
        StdOut.println("Put 3");
        doubleHashingHashTable.put(new TestClass(3), 3);
        doubleHashingHashTable.delete(new TestClass(6));
        doubleHashingHashTable.delete(new TestClass(7));
        StdOut.println("Size: " + doubleHashingHashTable.size() + " Expected: 94");
    }

}
