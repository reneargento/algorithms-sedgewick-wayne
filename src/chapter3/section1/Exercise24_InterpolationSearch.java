package chapter3.section1;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 24/04/17.
 */
@SuppressWarnings("unchecked")
//Based on http://www.geeksforgeeks.org/interpolation-search/
//https://en.wikipedia.org/wiki/Interpolation_search

//Average case: log(log (n))
//Worst case: O(n)
public class Exercise24_InterpolationSearch {

    public class BinarySearchSymbolTable<Value> {

        private Integer[] keys;
        private Value[] values;
        private int size;

        private static final int DEFAULT_INITIAL_CAPACITY = 2;

        public BinarySearchSymbolTable() {
            keys = new Integer[DEFAULT_INITIAL_CAPACITY];
            values = (Value[]) new Object[DEFAULT_INITIAL_CAPACITY];
        }

        public BinarySearchSymbolTable(int capacity) {
            keys = (Integer[]) new Comparable[capacity];
            values = (Value[]) new Object[capacity];
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public Value get(Integer key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            if (isEmpty()) {
                return null;
            }

            int rank = rank(key);
            if (rank < size && keys[rank].compareTo(key) == 0) {
                return values[rank];
            } else {
                return null;
            }
        }

        public int rank(Integer key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            int low = 0;
            int high = size - 1;

            while (low <= high) {

                if (keys[low] > key) {
                    return low;
                }

                int middle;
                if (keys[high] - keys[low] == 0) {
                    //Low and high have the same value - avoid division by zero
                    middle = low;
                } else {
                    //Interpolation search
                    middle = low + ((key - keys[low]) * (high - low) / (keys[high] - keys[low]));
                }

                if (middle > high) {
                    middle = high;
                }

                int comparison = key.compareTo(keys[middle]);
                if (comparison < 0) {
                    high = middle - 1;
                } else if (comparison > 0) {
                    low = middle + 1;
                } else {
                    return middle;
                }
            }

            return low;
        }

        public void put(Integer key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            int rank = rank(key);

            if (rank < size && keys[rank].compareTo(key) == 0) {
                values[rank] = value;
                return;
            }

            if (size == keys.length) {
                resize(keys.length * 2);
            }

            for(int i = size; i > rank; i--) {
                keys[i] = keys[i - 1];
                values[i] = values[i - 1];
            }
            keys[rank] = key;
            values[rank] = value;
            size++;
        }

        public boolean contains(Integer key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }
            return get(key) != null;
        }

        public void delete(Integer key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty() || !contains(key)) {
                return;
            }

            int rank = rank(key);
            for(int i = rank; i < size - 1; i++) {
                keys[i] = keys[i + 1];
                values[i] = values[i + 1];
            }

            keys[size - 1] = null;
            values[size - 1] = null;
            size--;

            if (size > 1 && size == keys.length / 4) {
                resize(keys.length / 2);
            }
        }

        public Integer min() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return keys[0];
        }

        public Integer max() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return keys[size - 1];
        }

        public Integer select(int k) {
            if (isEmpty() || k >= size) {
                throw new IllegalArgumentException("Invalid argument: " + k);
            }

            return keys[k];
        }

        public Integer ceiling(Integer key) {
            int rank = rank(key);

            if (rank == size) {
                return null;
            }

            return keys[rank];
        }

        public Integer floor(Integer key) {
            if (contains(key)) {
                return key;
            }

            int rank = rank(key);

            if (rank == 0) {
                return null;
            }

            return keys[rank - 1];
        }

        public void deleteMin() {
            if (isEmpty()) {
                throw new NoSuchElementException("Symbol table underflow error");
            }

            delete(min());
        }

        public void deleteMax() {
            if (isEmpty()) {
                throw new NoSuchElementException("Symbol table underflow error");
            }

            delete(max());
        }

        public int size(Integer low, Integer high) {
            if (low == null || high == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (high.compareTo(low) < 0) {
                return 0;
            } else if (contains(high)) {
                return rank(high) - rank(low) + 1;
            } else {
                return rank(high) - rank(low);
            }
        }

        public Iterable<Integer> keys(Integer low, Integer high) {
            if (low == null || high == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            Queue<Integer> queue = new Queue<>();

            for(int i = rank(low); i < rank(high); i++) {
                queue.enqueue(keys[i]);
            }

            if (contains(high)) {
                queue.enqueue(keys[rank(high)]);
            }

            return queue;
        }

        public Iterable<Integer> keys() {
            return keys(min(), max());
        }

        private void resize(int newSize) {
            Integer[] tempKeys = new Integer[newSize];
            Value[] tempValues = (Value[]) new Object[newSize];

            System.arraycopy(keys, 0, tempKeys, 0, size);
            System.arraycopy(values, 0, tempValues, 0, size);

            keys = tempKeys;
            values = tempValues;
        }
    }

    public static void main(String[] args) {
        int[] values = {0, 2, 1, 3, 4, 2, 3, 4, 5, 6, 7, 8, 9, 10, 4};
        StdOut.println("Highest frequency: " + new Exercise24_InterpolationSearch().frequencyCounter(values, 1));
        StdOut.println("Expected: 4 3");
    }

    private String frequencyCounter(int[] values, int minLength) {

        BinarySearchSymbolTable<Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        for(int value : values) {

            if (String.valueOf(value).length() < minLength) {
                continue;
            }

            if (!binarySearchSymbolTable.contains(value)) {
                binarySearchSymbolTable.put(value, 1);
            } else {
                binarySearchSymbolTable.put(value, binarySearchSymbolTable.get(value) + 1);
            }
        }

        int max = -1;
        binarySearchSymbolTable.put(max, 0);

        for(int value : binarySearchSymbolTable.keys()) {
            if (binarySearchSymbolTable.get(value) > binarySearchSymbolTable.get(max)) {
                max = value;
            }
        }

        return max + " " + binarySearchSymbolTable.get(max);
    }
}
