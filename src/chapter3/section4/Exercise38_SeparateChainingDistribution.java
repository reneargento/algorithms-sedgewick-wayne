package chapter3.section4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.VisualAccumulator;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Rene Argento on 29/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise38_SeparateChainingDistribution {

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

            public void put(Key key, Value value) {
                costOfPutCompares = 0;

                for(Node node = first; node != null; node = node.next) {
                    costOfPutCompares++;

                    if (key.equals(node.key)) {
                        node.value = value;
                        return;
                    }
                }

                first = new Node(key, value, first);
                size++;
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

        private int costOfPutCompares;

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

    private void doExperiment() {

        SeparateChainingHashTableFixedSizeCost<Integer, Integer> separateChainingHashTableFixedSizeCost =
                new SeparateChainingHashTableFixedSizeCost<>(100000);

        int maxInt = 1000000;

        StdOut.printf("%20s %20s %25s\n", "Total cost for 10^3 inserts | ","Average cost for each insert | ", "Expected number of keys in list");

        String title = "Separate-Chaining Hash Table costs using put()";
        String xAxisLabel = "operations";
        String yAxisLabel = "cost";
        double maxNumberOfOperations = 100000;
        double maxCost = 1500;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);

        long totalCostOfPutCompares = 0;

        for(int operation = 1; operation <= 100000; operation++) {
            int randomKey = StdRandom.uniform(maxInt);
            separateChainingHashTableFixedSizeCost.put(randomKey, randomKey);

            totalCostOfPutCompares += separateChainingHashTableFixedSizeCost.costOfPutCompares;

            if (operation % 1000 == 0) {
                double averageCostOfInsert = (totalCostOfPutCompares / (double) 1000);
                double expectedNumberOfKeysInList =
                        separateChainingHashTableFixedSizeCost.keysSize / (double) separateChainingHashTableFixedSizeCost.size;

                printResults(totalCostOfPutCompares, averageCostOfInsert, expectedNumberOfKeysInList);

                visualAccumulator.drawDataValue(operation, totalCostOfPutCompares, Color.BLACK);
                totalCostOfPutCompares = 0;
            }
        }
    }

    private void printResults(long totalCostOfPutCompares, double averageCostOfInsert, double expectedNumberOfKeysInList) {
        StdOut.printf("%27s %31.3f %34.3f\n", totalCostOfPutCompares, averageCostOfInsert, expectedNumberOfKeysInList);
    }

    public static void main(String[] args) {
        new Exercise38_SeparateChainingDistribution().doExperiment();
    }

}
