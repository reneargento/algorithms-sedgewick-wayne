package chapter3.section4;

import edu.princeton.cs.algs4.Queue;
import util.Constants;
import util.FileUtil;
import util.VisualAccumulator;

import java.util.Arrays;

/**
 * Created by Rene Argento on 29/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise40_PlotsSeparateChainingFixedSize {

    private class SeparateChainingHashTableFixedSizeCost<Key, Value> {

        private class SequentialSearchSymbolTable<Key, Value> {

            private class Node {
                Key key;
                Value value;
                Node next;

                public Node(Key key, Value value, Node next) {
                    this.key = key;
                    this.value = value;
                    this.next = next;
                }
            }

            private Node first;
            private int size;

            public int size() {
                return size;
            }

            public boolean isEmpty() {
                return size == 0;
            }

            public boolean contains(Key key) {
                return get(key) != null;
            }

            public Value get(Key key) {
                for(Node node = first; node != null; node = node.next) {
                    if (key.equals(node.key)) {
                        return node.value;
                    }
                }

                return null;
            }

            public int putAndComputeCost(Key key, Value value) {
                int costOfPutCompares = 1;

                for(Node node = first; node != null; node = node.next) {
                    costOfPutCompares++;

                    if (key.equals(node.key)) {
                        node.value = value;
                        return costOfPutCompares;
                    }
                }

                first = new Node(key, value, first);
                size++;
                return costOfPutCompares;
            }

            public void delete(Key key) {
                if (first.key.equals(key)) {
                    first = first.next;
                    size--;
                    return;
                }

                for(Node node = first; node != null; node = node.next) {
                    if (node.next != null && node.next.key.equals(key)) {
                        node.next = node.next.next;
                        size--;
                        return;
                    }
                }
            }

            public Iterable<Key> keys() {
                Queue<Key> keys = new Queue<>();

                for(Node node = first; node != null; node = node.next) {
                    keys.enqueue(node.key);
                }

                return keys;
            }
        }

        private int size;
        private int keysSize;
        private SequentialSearchSymbolTable<Key, Value>[] symbolTable;

        private static final int DEFAULT_HASH_TABLE_SIZE = 997;

        public SeparateChainingHashTableFixedSizeCost() {
            this(DEFAULT_HASH_TABLE_SIZE);
        }

        public SeparateChainingHashTableFixedSizeCost(int size) {
            this.size = size;
            symbolTable = new SequentialSearchSymbolTable[size];

            for(int i = 0; i < size; i++) {
                symbolTable[i] = new SequentialSearchSymbolTable<>();
            }
        }

        public int size() {
            return keysSize;
        }

        public boolean isEmpty() {
            return keysSize == 0;
        }

        private int hash(Key key) {
            int hash = key.hashCode() & 0x7fffffff;
            hash = (31 * hash) & 0x7fffffff;
            return hash % size;
        }

        public boolean contains(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }

            return get(key) != null;
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            return symbolTable[hash(key)].get(key);
        }

        public int putAndComputeCost(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return 0;
            }

            int hashIndex = hash(key);
            int currentSize = symbolTable[hashIndex].size;
            int costOfPutCompares = symbolTable[hashIndex].putAndComputeCost(key, value);

            if (currentSize < symbolTable[hashIndex].size) {
                keysSize++;
            }

            return costOfPutCompares;
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
        }

        public Iterable<Key> keys() {
            Queue<Key> keys = new Queue<>();

            for(SequentialSearchSymbolTable<Key, Value> sequentialSearchST : symbolTable) {
                for(Key key : sequentialSearchST.keys()) {
                    keys.enqueue(key);
                }
            }

            if (!keys.isEmpty() && keys.peek() instanceof Comparable) {
                Key[] keysToBeSorted = (Key[]) new Comparable[keys.size()];
                for(int i = 0; i < keysToBeSorted.length; i++) {
                    keysToBeSorted[i] = keys.dequeue();
                }

                Arrays.sort(keysToBeSorted);

                for(Key key : keysToBeSorted) {
                    keys.enqueue(key);
                }
            }

            return keys;
        }

    }

    private static final String TALE_FILE_PATH = Constants.FILES_PATH + Constants.TALE_OF_TWO_CITIES_FILE;

    public static void main(String[] args) {
        String[] wordsInTale = FileUtil.getAllStringsFromFile(TALE_FILE_PATH);
        int minLength = 8; //Same as the book analysis
        new Exercise40_PlotsSeparateChainingFixedSize().frequencyCounter(wordsInTale, minLength);
    }

    private String frequencyCounter(String[] words, int minLength) {

        String title = "Separate-chaining hash table (fixed size) costs using put() in FrequencyCounter";
        String xAxisLabel = "operations";
        String yAxisLabel = "equality tests";
        double maxNumberOfOperations = 18000;
        double maxCost = 25;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);
        SeparateChainingHashTableFixedSizeCost<String, Integer> separateChainingHashTableFixedSizeCost =
                new SeparateChainingHashTableFixedSizeCost<>(997);

        for(String word : words) {

            if (word.length() < minLength) {
                continue;
            }

            int cost;
            if (!separateChainingHashTableFixedSizeCost.contains(word)) {
                cost = separateChainingHashTableFixedSizeCost.putAndComputeCost(word, 1);
            } else {
                cost = separateChainingHashTableFixedSizeCost.
                        putAndComputeCost(word, separateChainingHashTableFixedSizeCost.get(word) + 1);
            }
            visualAccumulator.addDataValue(cost, true);
        }

        String max = "";
        int cost = separateChainingHashTableFixedSizeCost.putAndComputeCost(max, 0);
        visualAccumulator.addDataValue(cost, true);

        for(String word : separateChainingHashTableFixedSizeCost.keys()) {
            if (separateChainingHashTableFixedSizeCost.get(word) > separateChainingHashTableFixedSizeCost.get(max)) {
                max = word;
            }
        }

        visualAccumulator.writeExactFinalMean();

        return max + " " + separateChainingHashTableFixedSizeCost.get(max);
    }

}
