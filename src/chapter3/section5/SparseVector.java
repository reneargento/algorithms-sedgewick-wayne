package chapter3.section5;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

/**
 * Created by Rene Argento on 04/08/17.
 */
public class SparseVector {

    public int dimension;
    private HashSTintKeysdoubleValues hashTable;

    public SparseVector(int dimension) {
        this(dimension, 997);
    }

    public SparseVector(int dimension, int initialSize) {
        hashTable = new HashSTintKeysdoubleValues(initialSize);
        this.dimension = dimension;
    }

    public int size() {
        return hashTable.size();
    }

    public double get(int key) {
        if (!hashTable.contains(key)) {
            return 0;
        } else {
            return hashTable.get(key);
        }
    }

    public void put(int key, double value) {
        if (value == 0) {
            hashTable.delete(key);
            return;
        }

        hashTable.put(key, value);
    }

    public void delete(int key) {
        hashTable.delete(key);
    }

    public SparseVector plus(SparseVector sparseVector) {
        if (dimension != sparseVector.dimension) {
            throw new IllegalArgumentException("Sparse vector dimensions must be the same.");
        }

        SparseVector result = new SparseVector(dimension);

        //Copy values
        for(int key : hashTable.keys()) {
            result.put(key, get(key));
        }
        //Sum values
        for(int key : sparseVector.hashTable.keys()) {
            double sum = get(key) + sparseVector.get(key);

            if (sum != 0) {
                result.put(key, sum);
            } else {
                result.delete(key);
            }
        }

        return result;
    }

    public double dot(SparseVector sparseVector) {
        if (dimension != sparseVector.dimension) {
            throw new IllegalArgumentException("Sparse vector dimensions must be the same.");
        }

        double sum = 0;

        //Iterate over the vector with the fewest nonzeros
        if (size() <= sparseVector.size()) {
            for(int key : hashTable.keys()) {
                if (sparseVector.hashTable.contains(key)) {
                    sum += get(key) * sparseVector.get(key);
                }
            }
        } else {
            for(int key : sparseVector.hashTable.keys()) {
                if (hashTable.contains(key)) {
                    sum += get(key) * sparseVector.get(key);
                }
            }
        }

        return sum;
    }

    public double dot(double[] that) {
        double sum = 0.0;

        for(int key : hashTable.keys()) {
            sum += this.get(key) * that[key];
        }

        return sum;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int key : hashTable.keys()) {
            stringBuilder.append("(").append(key).append(", ").append(get(key)).append(") ");
        }

        return stringBuilder.toString();
    }

    //Optimized hash map with primitive keys and values, not requiring autoboxing and unboxing
    private class HashSTintKeysdoubleValues {

        private int keysSize;
        private int size;
        private int[] keys;
        private double[] values;

        private final static int EMPTY_KEY = Integer.MIN_VALUE;

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

        private HashSTintKeysdoubleValues(int size) {
            this.size = size;
            keys = new int[size];
            values = new double[size];

            for(int i = 0; i < size; i++) {
                keys[i] = EMPTY_KEY;
            }

            lgM = (int) (Math.log(size) / Math.log(2));
        }

        public int size() {
            return keysSize;
        }

        public boolean isEmpty() {
            return keysSize == 0;
        }

        private int hash(int key) {
            int hash = Integer.valueOf(key).hashCode() & 0x7fffffff;

            if (lgM < 26) {
                hash = hash % PRIMES[lgM + 5];
            }

            return hash % size;
        }

        private double getLoadFactor() {
            return keysSize / (double) size;
        }

        private void resize(int newSize) {
            HashSTintKeysdoubleValues tempHashTable = new HashSTintKeysdoubleValues(newSize);

            for(int i = 0; i < size; i++) {
                if (keys[i] != EMPTY_KEY) {
                    tempHashTable.put(keys[i], values[i]);
                }
            }

            keys = tempHashTable.keys;
            values = tempHashTable.values;
            size = tempHashTable.size;
        }

        public boolean contains(int key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            for(int tableIndex = hash(key); keys[tableIndex] != EMPTY_KEY; tableIndex = (tableIndex + 1) % size) {
                if (keys[tableIndex] == key) {
                    return true;
                }
            }

            return false;
        }

        public double get(int key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            for(int tableIndex = hash(key); keys[tableIndex] != EMPTY_KEY; tableIndex = (tableIndex + 1) % size) {
                if (keys[tableIndex] == key) {
                    return values[tableIndex];
                }
            }

            return EMPTY_KEY;
        }

        public void put(int key, double value) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            if (keysSize >= size / (double) 2) {
                resize(size * 2);
                lgM++;
            }

            int tableIndex;
            for(tableIndex = hash(key); keys[tableIndex] != EMPTY_KEY; tableIndex = (tableIndex + 1) % size) {
                if (keys[tableIndex] == key) {
                    values[tableIndex] = value;
                    return;
                }
            }

            keys[tableIndex] = key;
            values[tableIndex] = value;
            keysSize++;
        }

        public void delete(int key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            if (!contains(key)) {
                return;
            }

            int tableIndex = hash(key);
            while (keys[tableIndex] != key) {
                tableIndex = (tableIndex + 1) % size;
            }

            keys[tableIndex] = EMPTY_KEY;
            values[tableIndex] = EMPTY_KEY;
            keysSize--;

            tableIndex = (tableIndex + 1) % size;

            while (keys[tableIndex] != EMPTY_KEY) {
                int keyToRedo = keys[tableIndex];
                double valueToRedo = values[tableIndex];

                keys[tableIndex] = EMPTY_KEY;
                values[tableIndex] = EMPTY_KEY;
                keysSize--;

                put(keyToRedo, valueToRedo);
                tableIndex = (tableIndex + 1) % size;
            }

            if (keysSize > 1 && keysSize <= size / (double) 8) {
                resize(size / 2);
                lgM--;
            }
        }

        public int[] keys() {
            Queue<Integer> keySet = new Queue<>();

            for(int key : keys) {
                if (key != EMPTY_KEY) {
                    keySet.enqueue(key);
                }
            }

            int[] keys = new int[keySet.size()];
            for(int i = 0; i < keys.length; i++) {
                keys[i] = keySet.dequeue();
            }

            Arrays.sort(keys);

            return keys;
        }
    }

}