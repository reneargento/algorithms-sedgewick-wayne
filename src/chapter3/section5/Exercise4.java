package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 04/08/17.
 */
@SuppressWarnings("unchecked")
public class Exercise4 {

    class HashSTint<Value> {

        private int keysSize;
        private int size;
        private int[] keys;
        private Value[] values;

        public final static int EMPTY_KEY = Integer.MIN_VALUE;

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

        HashSTint(int size) {
            this.size = size;
            keys = new int[size];
            values = (Value[]) new Object[size];

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
            HashSTint<Value> tempHashTable = new HashSTint<>(newSize);

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

        public Value get(int key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            for(int tableIndex = hash(key); keys[tableIndex] != EMPTY_KEY; tableIndex = (tableIndex + 1) % size) {
                if (keys[tableIndex] == key) {
                    return values[tableIndex];
                }
            }

            return null;
        }

        public void put(int key, Value value) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            if (value == null) {
                delete(key);
                return;
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
            values[tableIndex] = null;
            keysSize--;

            tableIndex = (tableIndex + 1) % size;

            while (keys[tableIndex] != EMPTY_KEY) {
                int keyToRedo = keys[tableIndex];
                Value valueToRedo = values[tableIndex];

                keys[tableIndex] = EMPTY_KEY;
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

    class HashSTdouble<Value> {

        private int keysSize;
        private int size;
        private double[] keys;
        private Value[] values;

        public final static double EMPTY_VALUE = Double.MIN_VALUE;

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

        HashSTdouble(int size) {
            this.size = size;
            keys = new double[size];
            values = (Value[]) new Object[size];

            for(int i = 0; i < size; i++) {
                keys[i] = EMPTY_VALUE;
            }

            lgM = (int) (Math.log(size) / Math.log(2));
        }

        public int size() {
            return keysSize;
        }

        public boolean isEmpty() {
            return keysSize == 0;
        }

        private int hash(double key) {
            int hash = ((Double) key).hashCode() & 0x7fffffff;

            if (lgM < 26) {
                hash = hash % PRIMES[lgM + 5];
            }

            return hash % size;
        }

        private double getLoadFactor() {
            return keysSize / (double) size;
        }

        private void resize(int newSize) {
            HashSTdouble<Value> tempHashTable = new HashSTdouble<>(newSize);

            for(int i = 0; i < size; i++) {
                if (keys[i] != EMPTY_VALUE) {
                    tempHashTable.put(keys[i], values[i]);
                }
            }

            keys = tempHashTable.keys;
            values = tempHashTable.values;
            size = tempHashTable.size;
        }

        public boolean contains(double key) {
            if (key == EMPTY_VALUE) {
                throw new IllegalArgumentException("Invalid key");
            }

            for(int tableIndex = hash(key); keys[tableIndex] != EMPTY_VALUE; tableIndex = (tableIndex + 1) % size) {
                if (keys[tableIndex] == key) {
                    return true;
                }
            }

            return false;
        }

        public Value get(double key) {
            if (key == EMPTY_VALUE) {
                throw new IllegalArgumentException("Invalid key");
            }

            for(int tableIndex = hash(key); keys[tableIndex] != EMPTY_VALUE; tableIndex = (tableIndex + 1) % size) {
                if (keys[tableIndex] == key) {
                    return values[tableIndex];
                }
            }

            return null;
        }

        public void put(double key, Value value) {
            if (key == EMPTY_VALUE) {
                throw new IllegalArgumentException("Invalid key");
            }

            if (value == null) {
                delete(key);
                return;
            }

            if (keysSize >= size / (double) 2) {
                resize(size * 2);
                lgM++;
            }

            int tableIndex;
            for(tableIndex = hash(key); keys[tableIndex] != EMPTY_VALUE; tableIndex = (tableIndex + 1) % size) {
                if (keys[tableIndex] == key) {
                    values[tableIndex] = value;
                    return;
                }
            }

            keys[tableIndex] = key;
            values[tableIndex] = value;
            keysSize++;
        }

        public void delete(double key) {
            if (key == EMPTY_VALUE) {
                throw new IllegalArgumentException("Invalid key");
            }

            if (!contains(key)) {
                return;
            }

            int tableIndex = hash(key);
            while (keys[tableIndex] != key) {
                tableIndex = (tableIndex + 1) % size;
            }

            keys[tableIndex] = EMPTY_VALUE;
            values[tableIndex] = null;
            keysSize--;

            tableIndex = (tableIndex + 1) % size;

            while (keys[tableIndex] != EMPTY_VALUE) {
                double keyToRedo = keys[tableIndex];
                Value valueToRedo = values[tableIndex];

                keys[tableIndex] = EMPTY_VALUE;
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

        public double[] keys() {
            Queue<Double> keySet = new Queue<>();

            for(double key : keys) {
                if (key != EMPTY_VALUE) {
                    keySet.enqueue(key);
                }
            }

            double[] keys = new double[keySet.size()];
            for(int i = 0; i < keys.length; i++) {
                keys[i] = keySet.dequeue();
            }

            Arrays.sort(keys);

            return keys;
        }
    }

    public static void main(String[] args) {
        Exercise4 exercise4 = new Exercise4();
        exercise4.testHashSTint();
        exercise4.testHashSTdouble();
    }

    private void testHashSTint() {
        StdOut.println("HashSTint test");
        HashSTint hashSTint = new HashSTint(5);

        hashSTint.put(5, 5);
        hashSTint.put(1, 1);
        hashSTint.put(9, 9);
        hashSTint.put(2, 2);
        hashSTint.put(0, 0);
        hashSTint.put(99, 99);
        hashSTint.put(-1, -1);
        hashSTint.put(-2, -2);
        hashSTint.put(3, 3);
        hashSTint.put(-5, -5);

        StdOut.println("\nKeys() test");

        for(Integer key : hashSTint.keys()) {
            StdOut.println("Key " + key + ": " + hashSTint.get(key));
        }
        StdOut.println("Expected: -5 -2 -1 0 1 2 3 5 9 99\n");

        //Test delete()
        StdOut.println("Delete key 2");
        hashSTint.delete(2);

        for(Integer key : hashSTint.keys()) {
            StdOut.println("Key " + key + ": " + hashSTint.get(key));
        }

        StdOut.println("\nDelete key 99");
        hashSTint.delete(99);

        for(Integer key : hashSTint.keys()) {
            StdOut.println("Key " + key + ": " + hashSTint.get(key));
        }

        StdOut.println("\nDelete key -5");
        hashSTint.delete(-5);

        for(Integer key : hashSTint.keys()) {
            StdOut.println("Key " + key + ": " + hashSTint.get(key));
        }
    }

    private void testHashSTdouble() {
        StdOut.println("\n\nHashSTdouble test");
        HashSTdouble hashSTdouble = new HashSTdouble(5);

        hashSTdouble.put(5.0, 5.0);
        hashSTdouble.put(1.0, 1.0);
        hashSTdouble.put(9.5, 9.5);
        hashSTdouble.put(2.1, 2.1);
        hashSTdouble.put(0.0, 0.0);
        hashSTdouble.put(99.999, 99.999);
        hashSTdouble.put(-1.05, -1.05);
        hashSTdouble.put(-2.2, -2.2);
        hashSTdouble.put(3.0, 3.0);
        hashSTdouble.put(-5.9, -5.9);

        StdOut.println("\nKeys() test");

        for(Double key : hashSTdouble.keys()) {
            StdOut.println("Key " + key + ": " + hashSTdouble.get(key));
        }
        StdOut.println("\nExpected: -5.9 -2.2 -1.05 0.0 1.0 2.1 3.0 5.0 9.5 99.999");

        //Test delete()
        StdOut.println("\nDelete key 2.1");
        hashSTdouble.delete(2.1);

        for(Double key : hashSTdouble.keys()) {
            StdOut.println("Key " + key + ": " + hashSTdouble.get(key));
        }

        StdOut.println("\nDelete key 99.999");
        hashSTdouble.delete(99.999);

        for(Double key : hashSTdouble.keys()) {
            StdOut.println("Key " + key + ": " + hashSTdouble.get(key));
        }

        StdOut.println("\nDelete key -5.9");
        hashSTdouble.delete(-5.9);

        for(Double key : hashSTdouble.keys()) {
            StdOut.println("Key " + key + ": " + hashSTdouble.get(key));
        }
    }

}