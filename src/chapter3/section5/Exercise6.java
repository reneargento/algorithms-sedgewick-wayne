package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by rene on 03/08/17.
 */
@SuppressWarnings("unchecked")
public class Exercise6 {

    private class HashSETInt {

        private int keysSize;
        private int size;
        private int[] keys;

        final static int EMPTY_VALUE = Integer.MIN_VALUE;

        //The largest prime <= 2^i for i = 3 to 31
        //Used to distribute keys uniformly in the hash table after resizes
        //PRIMES[n] = 2^k - Ak where k is the power of 2 and Ak is the value to subtract to reach the previous prime number
        private final int[] PRIMES = {
                7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
                32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
                8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
                536870909, 1073741789, 2147483647
        };

        //The lg of the hash table size
        //Used in combination with PRIMES[] to distribute keys uniformly in the hash function after resizes
        private int lgM;

        private HashSETInt(int size) {
            this.size = size;
            keys = new int[size];

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

        private int hash(int key) {
            int hash = Integer.valueOf(key).hashCode() & 0x7fffffff;

            if(lgM < 26) {
                hash = hash % PRIMES[lgM + 5];
            }

            return hash % size;
        }

        private double getLoadFactor() {
            return keysSize / (double) size;
        }

        private void resize(int newSize) {
            HashSETInt tempSet = new HashSETInt(newSize);

            for(int i = 0; i < size; i++) {
                if(keys[i] != EMPTY_VALUE) {
                    tempSet.add(keys[i]);
                }
            }

            keys = tempSet.keys;
            size = tempSet.size;
        }

        public boolean contains(int key) {
            if (key == EMPTY_VALUE) {
                throw new IllegalArgumentException("Invalid key");
            }

            for(int tableIndex = hash(key); keys[tableIndex] != EMPTY_VALUE; tableIndex = (tableIndex + 1) % size) {
                if(keys[tableIndex] == key) {
                    return true;
                }
            }

            return false;
        }

        public void add(int key) {
            if (key == EMPTY_VALUE) {
                throw new IllegalArgumentException("Invalid key");
            }

            if(keysSize >= size / (double) 2) {
                resize(size * 2);
                lgM++;
            }

            int tableIndex;
            for(tableIndex = hash(key); keys[tableIndex] != EMPTY_VALUE; tableIndex = (tableIndex + 1) % size) {
                if(keys[tableIndex] == key) {
                    keys[tableIndex] = key;
                    return;
                }
            }

            keys[tableIndex] = key;
            keysSize++;
        }

        public void delete(int key) {
            if (key == EMPTY_VALUE) {
                throw new IllegalArgumentException("Invalid key");
            }

            if(!contains(key)) {
                return;
            }

            int tableIndex = hash(key);
            while (keys[tableIndex] != key) {
                tableIndex = (tableIndex + 1) % size;
            }

            keys[tableIndex] = EMPTY_VALUE;
            keysSize--;

            tableIndex = (tableIndex + 1) % size;

            while (keys[tableIndex] != EMPTY_VALUE) {
                int keyToRedo = keys[tableIndex];

                keys[tableIndex] = EMPTY_VALUE;
                keysSize--;

                add(keyToRedo);
                tableIndex = (tableIndex + 1) % size;
            }

            if(keysSize > 0 && keysSize <= size / (double) 8) {
                resize(size / 2);
                lgM--;
            }
        }

        public int[] keys() {
            Queue<Integer> keySet = new Queue<>();

            for(int key : keys) {
                if(key != EMPTY_VALUE) {
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

        @Override
        public String toString() {
            if(isEmpty()) {
                return "{ }";
            }

            StringBuilder stringBuilder = new StringBuilder("{");

            boolean isFirstKey = true;
            for(int key : keys()) {
                if(isFirstKey) {
                    isFirstKey = false;
                } else {
                    stringBuilder.append(",");
                }

                stringBuilder.append(" ").append(key);
            }

            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    private class HashSETDouble {
        private int keysSize;
        private int size;
        private double[] keys;

        final static double EMPTY_VALUE = Double.MIN_VALUE;

        //The largest prime <= 2^i for i = 3 to 31
        //Used to distribute keys uniformly in the hash table after resizes
        //PRIMES[n] = 2^k - Ak where k is the power of 2 and Ak is the value to subtract to reach the previous prime number
        private final int[] PRIMES = {
                7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
                32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
                8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
                536870909, 1073741789, 2147483647
        };

        //The lg of the hash table size
        //Used in combination with PRIMES[] to distribute keys uniformly in the hash function after resizes
        private int lgM;

        private HashSETDouble(int size) {
            this.size = size;
            keys = new double[size];

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
            int hash = Double.valueOf(key).hashCode() & 0x7fffffff;

            if(lgM < 26) {
                hash = hash % PRIMES[lgM + 5];
            }

            return hash % size;
        }

        private double getLoadFactor() {
            return keysSize / (double) size;
        }

        private void resize(int newSize) {
            HashSETDouble tempSet = new HashSETDouble(newSize);

            for(int i = 0; i < size; i++) {
                if(keys[i] != EMPTY_VALUE) {
                    tempSet.add(keys[i]);
                }
            }

            keys = tempSet.keys;
            size = tempSet.size;
        }

        public boolean contains(double key) {
            if (key == EMPTY_VALUE) {
                throw new IllegalArgumentException("Invalid key");
            }

            for(int tableIndex = hash(key); keys[tableIndex] != EMPTY_VALUE; tableIndex = (tableIndex + 1) % size) {
                if(keys[tableIndex] == key) {
                    return true;
                }
            }

            return false;
        }

        public void add(double key) {
            if (key == EMPTY_VALUE) {
                throw new IllegalArgumentException("Invalid key");
            }

            if(keysSize >= size / (double) 2) {
                resize(size * 2);
                lgM++;
            }

            int tableIndex;
            for(tableIndex = hash(key); keys[tableIndex] != EMPTY_VALUE; tableIndex = (tableIndex + 1) % size) {
                if(keys[tableIndex] == key) {
                    keys[tableIndex] = key;
                    return;
                }
            }

            keys[tableIndex] = key;
            keysSize++;
        }

        public void delete(double key) {
            if (key == EMPTY_VALUE) {
                throw new IllegalArgumentException("Invalid key");
            }

            if(!contains(key)) {
                return;
            }

            int tableIndex = hash(key);
            while (keys[tableIndex] != key) {
                tableIndex = (tableIndex + 1) % size;
            }

            keys[tableIndex] = EMPTY_VALUE;
            keysSize--;

            tableIndex = (tableIndex + 1) % size;

            while (keys[tableIndex] != EMPTY_VALUE) {
                double keyToRedo = keys[tableIndex];

                keys[tableIndex] = EMPTY_VALUE;
                keysSize--;

                add(keyToRedo);
                tableIndex = (tableIndex + 1) % size;
            }

            if(keysSize > 0 && keysSize <= size / (double) 8) {
                resize(size / 2);
                lgM--;
            }
        }

        public double[] keys() {
            Queue<Double> keySet = new Queue<>();

            for(double key : keys) {
                if(key != EMPTY_VALUE) {
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

        @Override
        public String toString() {
            if(isEmpty()) {
                return "{ }";
            }

            StringBuilder stringBuilder = new StringBuilder("{");

            boolean isFirstKey = true;
            for(double key : keys()) {
                if(isFirstKey) {
                    isFirstKey = false;
                } else {
                    stringBuilder.append(",");
                }

                stringBuilder.append(" ").append(key);
            }

            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Exercise6 exercise6 = new Exercise6();
        exercise6.testHashSTInt();
        exercise6.testHashSTDouble();
    }

    private void testHashSTInt() {
        StdOut.println("HashSTInt test");
        HashSETInt hashSTInt = new HashSETInt(5);

        hashSTInt.add(5);
        hashSTInt.add(1);
        hashSTInt.add(9);
        hashSTInt.add(2);
        hashSTInt.add(0);
        hashSTInt.add(99);
        hashSTInt.add(-1);
        hashSTInt.add(-2);
        hashSTInt.add(3);
        hashSTInt.add(-5);

        StdOut.println("Keys() test");

        for(Integer key : hashSTInt.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: -5 -2 -1 0 1 2 3 5 9 99");

        StdOut.println("\ntoString() test: " + hashSTInt);

        StdOut.println("\nContains 0: " + hashSTInt.contains(0) + " Expected: true");
        StdOut.println("Contains 100: " + hashSTInt.contains(100) + " Expected: false");

        //Test delete()
        StdOut.println("\nDelete key 2");
        hashSTInt.delete(2);
        StdOut.println(hashSTInt);

        StdOut.println("\nDelete key 99");
        hashSTInt.delete(99);
        StdOut.println(hashSTInt);

        StdOut.println("\nDelete key -5");
        hashSTInt.delete(-5);
        StdOut.println(hashSTInt);
    }

    private void testHashSTDouble() {
        StdOut.println("\n\nHashSTDouble test");
        HashSETDouble hashSTDouble = new HashSETDouble(5);

        hashSTDouble.add(5.0);
        hashSTDouble.add(1.0);
        hashSTDouble.add(9.5);
        hashSTDouble.add(2.1);
        hashSTDouble.add(0);
        hashSTDouble.add(99.999);
        hashSTDouble.add(-1.05);
        hashSTDouble.add(-2.20);
        hashSTDouble.add(3.0);
        hashSTDouble.add(-5.9);

        StdOut.println("Keys() test");

        for(Double key : hashSTDouble.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: -5.9 -2.2 -1.05 0.0 1.0 2.1 3.0 5.0 9.5 99.999");

        StdOut.println("\ntoString() test: " + hashSTDouble);

        StdOut.println("\nContains 0: " + hashSTDouble.contains(0) + " Expected: true");
        StdOut.println("Contains 100: " + hashSTDouble.contains(100) + " Expected: false");

        //Test delete()
        StdOut.println("\nDelete key 2.1");
        hashSTDouble.delete(2.1);
        StdOut.println(hashSTDouble);

        StdOut.println("\nDelete key 99.999");
        hashSTDouble.delete(99.999);
        StdOut.println(hashSTDouble);

        StdOut.println("\nDelete key -5.9");
        hashSTDouble.delete(-5.9);
        StdOut.println(hashSTDouble);
    }

}
