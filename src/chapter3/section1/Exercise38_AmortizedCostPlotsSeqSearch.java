package chapter3.section1;

import edu.princeton.cs.algs4.Queue;
import util.Constants;
import util.FileUtil;
import util.VisualAccumulator;

/**
 * Created by Rene Argento on 01/05/17.
 */
public class Exercise38_AmortizedCostPlotsSeqSearch {

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

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
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

            for(Node node = first; node != null; node = node.next) {
                if (key.equals(node.key)) {
                    return node.value;
                }
            }

            return null;
        }

        public int put(Key key, Value value) {
            int numberOfNodeAccesses = 0;

            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return 0; //Delete is not used in the analysis of node accesses
            }

            for(Node node = first; node != null; node = node.next) {
                numberOfNodeAccesses++;
                if (key.equals(node.key)) {
                    node.value = value;
                    return numberOfNodeAccesses;
                }
            }

            first = new Node(key, value, first);
            size++;

            return numberOfNodeAccesses;
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty()) {
                return;
            }

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

    private static final String TALE_FILE_PATH = Constants.FILES_PATH + Constants.TALE_OF_TWO_CITIES_FILE;

    public static void main(String[] args) {
        String[] wordsInTale = FileUtil.getAllStringsFromFile(TALE_FILE_PATH);
        int minLength = 8; //Same as the book analysis
        new Exercise38_AmortizedCostPlotsSeqSearch().frequencyCounter(wordsInTale, minLength);
    }

    private String frequencyCounter(String[] words, int minLength) {

        String title = "SequentialSearchST costs using put() in FrequencyCounter";
        String xAxisLabel = "operations";
        String yAxisLabel = "cost";
        double maxNumberOfOperations = 18000;
        double maxCost = 20000;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);
        SequentialSearchSymbolTable<String, Integer> sequentialSearchSymbolTable = new SequentialSearchSymbolTable<>();

        for(String word : words) {

            if (word.length() < minLength) {
                continue;
            }

            int numberOfNodeAccesses;
            if (!sequentialSearchSymbolTable.contains(word)) {
                numberOfNodeAccesses = sequentialSearchSymbolTable.put(word, 1);
            } else {
                numberOfNodeAccesses = sequentialSearchSymbolTable.put(word, sequentialSearchSymbolTable.get(word) + 1);
            }
            visualAccumulator.addDataValue(numberOfNodeAccesses, true);
        }

        String max = "";
        int numberOfNodeAccesses = sequentialSearchSymbolTable.put(max, 0);
        visualAccumulator.addDataValue(numberOfNodeAccesses, true);

        for(String word : sequentialSearchSymbolTable.keys()) {
            if (sequentialSearchSymbolTable.get(word) > sequentialSearchSymbolTable.get(max)) {
                max = word;
            }
        }

        visualAccumulator.writeFinalMean();

        return max + " " + sequentialSearchSymbolTable.get(max);
    }

}
