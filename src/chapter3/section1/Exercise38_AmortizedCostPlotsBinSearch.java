package chapter3.section1;

import edu.princeton.cs.algs4.Queue;
import util.Constants;
import util.FileUtil;
import util.VisualAccumulator;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 01/05/17.
 */
@SuppressWarnings("unchecked")
public class Exercise38_AmortizedCostPlotsBinSearch {

    private class BinarySearchSymbolTable<Key extends Comparable<Key>, Value> {

        private Key[] keys;
        private Value[] values;
        private int size;

        private static final int DEFAULT_INITIAL_CAPACITY = 2;

        public BinarySearchSymbolTable() {
            keys = (Key[]) new Comparable[DEFAULT_INITIAL_CAPACITY];
            values = (Value[]) new Object[DEFAULT_INITIAL_CAPACITY];
        }

        public BinarySearchSymbolTable(int capacity) {
            keys = (Key[]) new Comparable[capacity];
            values = (Value[]) new Object[capacity];
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            if (isEmpty()) {
                return null;
            }

            int[] rankResult = rank(key);
            int rank = rankResult[0];
            if (rank < size && keys[rank].compareTo(key) == 0) {
                return values[rank];
            } else {
                return null;
            }
        }

        public int[] rank(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            int numberOfArrayAccesses = 0;

            int low = 0;
            int high = size - 1;

            while (low <= high) {
                int middle = low + (high - low) / 2;

                int comparison = key.compareTo(keys[middle]);
                numberOfArrayAccesses++;

                if (comparison < 0) {
                    high = middle - 1;
                } else if (comparison > 0) {
                    low = middle + 1;
                } else {
                    return new int[]{middle, numberOfArrayAccesses};
                }
            }

            return new int[]{low, numberOfArrayAccesses};
        }

        public int put(Key key, Value value) {
            int numberOfArrayAccesses = 0;

            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return 0; //Delete is not used in the analysis of array accesses
            }

            int[] rankResult = rank(key);
            int rank = rankResult[0];
            numberOfArrayAccesses += rankResult[1];

            if (rank < size) {
                numberOfArrayAccesses++;
                if (keys[rank].compareTo(key) == 0) {
                    values[rank] = value;
                    return numberOfArrayAccesses;
                }
            }

            if (size == keys.length) {
                resize(keys.length * 2);
                numberOfArrayAccesses += size;
            }

            for(int i = size; i > rank; i--) {
                keys[i] = keys[i - 1];
                numberOfArrayAccesses += 2;

                values[i] = values[i - 1];
            }
            keys[rank] = key;
            numberOfArrayAccesses++;

            values[rank] = value;
            size++;

            return numberOfArrayAccesses;
        }

        public boolean contains(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }
            return get(key) != null;
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty() || !contains(key)) {
                return;
            }

            int[] rankResult = rank(key);
            int rank = rankResult[0];

            for(int i = rank; i < size - 1; i++) {
                keys[i] = keys[i + 1];
                values[i] = values[i + 1];
            }

            keys[size - 1] = null;
            values[size - 1] = null;
            size--;

            if (size > 0 && size == keys.length / 4) {
                resize(keys.length / 2);
            }
        }

        public Key min() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return keys[0];
        }

        public Key max() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return keys[size - 1];
        }

        public Key select(int k) {
            if (isEmpty() || k >= size) {
                throw new IllegalArgumentException("Invalid argument: " + k);
            }

            return keys[k];
        }

        public Key ceiling(Key key) {
            int[] rankResult = rank(key);
            int rank = rankResult[0];

            if (rank == size) {
                return null;
            }

            return keys[rank];
        }

        public Key floor(Key key) {
            if (contains(key)) {
                return key;
            }

            int[] rankResult = rank(key);
            int rank = rankResult[0];

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

        public int size(Key low, Key high) {
            if (low == null || high == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (high.compareTo(low) < 0) {
                return 0;
            } else {
                int[] rankHighResult = rank(high);
                int rankHigh = rankHighResult[0];

                int[] rankLowResult = rank(low);
                int rankLow = rankLowResult[0];

                if (contains(high)) {
                    return rankHigh - rankLow + 1;
                } else {
                    return rankHigh - rankLow;
                }
            }
        }

        public Iterable<Key> keys(Key low, Key high) {
            if (low == null || high == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            Queue<Key> queue = new Queue<>();

            int[] rankLowResult = rank(low);
            int rankLow = rankLowResult[0];

            int[] rankHighResult = rank(high);
            int rankHigh = rankHighResult[0];

            for(int i = rankLow; i < rankHigh; i++) {
                queue.enqueue(keys[i]);
            }

            if (contains(high)) {
                queue.enqueue(keys[rankHigh]);
            }

            return queue;
        }

        public Iterable<Key> keys() {
            return keys(min(), max());
        }

        private void resize(int newSize) {
            Key[] tempKeys = (Key[]) new Comparable[newSize];
            Value[] tempValues = (Value[]) new Object[newSize];

            System.arraycopy(keys, 0, tempKeys, 0, size);
            System.arraycopy(values, 0, tempValues, 0, size);

            keys = tempKeys;
            values = tempValues;
        }
    }

    private static final String TALE_FILE_PATH = Constants.FILES_PATH + Constants.TALE_OF_TWO_CITIES_FILE;

    public static void main(String[] args) {
        String[] wordsInTale = FileUtil.getAllStringsFromFile(TALE_FILE_PATH);
        int minLength = 8; //Same as the book analysis
        new Exercise38_AmortizedCostPlotsBinSearch().frequencyCounter(wordsInTale, minLength);
    }

    private String frequencyCounter(String[] words, int minLength) {

        String title = "BinarySearchST costs using put() in FrequencyCounter";
        String xAxisLabel = "operations";
        String yAxisLabel = "cost";
        double maxNumberOfOperations = 18000;
        double maxCost = 20000;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);
        BinarySearchSymbolTable<String, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        for(String word : words) {

            if (word.length() < minLength) {
                continue;
            }

            int numberOfArrayAccesses;
            if (!binarySearchSymbolTable.contains(word)) {
                numberOfArrayAccesses = binarySearchSymbolTable.put(word, 1);
            } else {
                numberOfArrayAccesses = binarySearchSymbolTable.put(word, binarySearchSymbolTable.get(word) + 1);
            }
            visualAccumulator.addDataValue(numberOfArrayAccesses, true);
        }

        String max = "";
        int numberOfArrayAccesses = binarySearchSymbolTable.put(max, 0);
        visualAccumulator.addDataValue(numberOfArrayAccesses, true);

        for(String word : binarySearchSymbolTable.keys()) {
            if (binarySearchSymbolTable.get(word) > binarySearchSymbolTable.get(max)) {
                max = word;
            }
        }

        visualAccumulator.writeFinalMean();

        return max + " " + binarySearchSymbolTable.get(max);
    }

}
