package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 21/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise18 {

    private class SeparateChainingHashTableResize<Key, Value> extends SeparateChainingHashTable<Key, Value>{

        private int averageListSize;

        //The largest prime <= 2^i for i = 1 to 31
        //Used to distribute keys uniformly in the hash table after resizes
        //PRIMES[n] = 2^k - Ak where k is the power of 2 and Ak is the value to subtract to reach the previous prime number
        private final int[] PRIMES = {
                1, 1, 3, 7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
                32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
                8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
                536870909, 1073741789, 2147483647
        };

        //The lg of the hash table size
        //Used in combination with PRIMES[] to distribute keys uniformly in the hash function after resizes
        private int lgM;

        public SeparateChainingHashTableResize(int initialSize, int averageListSize) {
            super(initialSize, averageListSize);

            this.size = initialSize;
            this.averageListSize = averageListSize;
            symbolTable = new SequentialSearchSymbolTable[size];

            for(int i = 0; i < size; i++) {
                symbolTable[i] = new SequentialSearchSymbolTable();
            }

            lgM = (int) (Math.log(size) / Math.log(2));
        }

        protected int hash(Key key) {
            int hash = key.hashCode() & 0x7fffffff;

            if (lgM < 26) {
                hash = hash % PRIMES[lgM + 5];
            }

            return hash % size;
        }

        public void resize(int newSize) {
            SeparateChainingHashTable<Key, Value> separateChainingHashTableTemp =
                    new SeparateChainingHashTable<>(newSize, averageListSize);

            for(Key key : keys()) {
                separateChainingHashTableTemp.put(key, get(key));
            }

            symbolTable = separateChainingHashTableTemp.symbolTable;
            size = separateChainingHashTableTemp.size;
            keysSize = separateChainingHashTableTemp.keysSize;
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            int hashIndex = hash(key);
            int currentSize = symbolTable[hashIndex].size;
            symbolTable[hashIndex].put(key, value);

            if (currentSize < symbolTable[hashIndex].size) {
                keysSize++;
            }

            if (getLoadFactor() >= averageListSize) {
                StdOut.println("Resize - doubling hash table size");

                resize(size * 2);
                lgM++;
            }
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty() || !contains(key)) {
                return;
            }

            symbolTable[hash(key)].delete(key);
            keysSize--;

            if (size > 1 && getLoadFactor() <= averageListSize / (double) 4) {
                StdOut.println("Resize - shrinking hash table size");

                resize(size / 2);
                lgM--;
            }
        }
    }

    public static void main(String[] args) {
        Exercise18 exercise18 = new Exercise18();
        SeparateChainingHashTableResize<Integer, Integer> separateChainingHashTableResize =
                exercise18.new SeparateChainingHashTableResize<>(5, 2);

        for(int i = 0; i < 20; i++) {
            separateChainingHashTableResize.put(i, i);
        }

        StdOut.println("Expected: Resize - doubling hash table size 2x");

        for(int i = 0; i < 10; i++) {
            separateChainingHashTableResize.delete(i);
        }

        StdOut.println("Expected: Resize - shrinking hash table size");

        for(int i = 10; i < 15; i++) {
            separateChainingHashTableResize.delete(i);
        }

        StdOut.println("Expected: Resize - shrinking hash table size");
    }

}
