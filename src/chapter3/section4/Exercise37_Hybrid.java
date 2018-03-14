package chapter3.section4;

import edu.princeton.cs.algs4.*;

import java.util.Arrays;

/**
 * Created by Rene Argento on 28/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise37_Hybrid {

    private class SeparateChainingHashTableWithRedBlackBST<Key extends Comparable<Key>, Value> {

        private int size;
        private int keysSize;
        RedBlackBST<Key, Value>[] symbolTable;

        private static final int DEFAULT_HASH_TABLE_SIZE = 997;

        public SeparateChainingHashTableWithRedBlackBST() {
            this(DEFAULT_HASH_TABLE_SIZE);
        }

        public SeparateChainingHashTableWithRedBlackBST(int size) {
            this.size = size;
            symbolTable = new RedBlackBST[size];

            for(int i = 0; i < size; i++) {
                symbolTable[i] = new RedBlackBST<>();
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
            int currentSize = symbolTable[hashIndex].size();
            symbolTable[hashIndex].put(key, value);

            if (currentSize < symbolTable[hashIndex].size()) {
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

            for(RedBlackBST redBlackBST : symbolTable) {
                for(Object key : redBlackBST.keys()) {
                    keys.enqueue((Key) key);
                }
            }

            Key[] keysToBeSorted = (Key[]) new Comparable[keys.size()];
            for(int i = 0; i < keysToBeSorted.length; i++) {
                keysToBeSorted[i] = keys.dequeue();
            }

            Arrays.sort(keysToBeSorted);

            for(Key key : keysToBeSorted) {
                keys.enqueue(key);
            }

            return keys;
        }

    }

    private void doExperiment() {
        SeparateChainingHashTableFixedSize<Integer, Integer> separateChainingHashTable = new SeparateChainingHashTableFixedSize<>();

        SeparateChainingHashTableWithRedBlackBST<Integer, Integer> separateChainingHashTableWithRedBlackBST =
                new SeparateChainingHashTableWithRedBlackBST<>();

        StdOut.printf("%12s %20s %16s\n", "Operation | ","Sequential Search time | ", "Red-black BST time");

        //Put tests
        int[] randomKeysPut = new int[1000000];

        for(int i = 0; i < randomKeysPut.length; i++) {
            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            randomKeysPut[i] = randomKey;
        }

        Stopwatch stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length; i++) {
            separateChainingHashTable.put(randomKeysPut[i], randomKeysPut[i]);
        }
        double timeSpentOnPutSequentialSearch = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length; i++) {
            separateChainingHashTableWithRedBlackBST.put(randomKeysPut[i], randomKeysPut[i]);
        }
        double timeSpentOnPutRedBlackBST = stopwatch.elapsedTime();

        printResults("Put", timeSpentOnPutSequentialSearch, timeSpentOnPutRedBlackBST);

        //Get tests
        int[] randomKeysGet = new int[500000];

        for(int i = 0; i < randomKeysGet.length; i++) {
            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            randomKeysGet[i] = randomKey;
        }

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysGet.length; i++) {
            separateChainingHashTable.get(randomKeysGet[i]);
        }
        double timeSpentOnGetSequentialSearch = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysGet.length; i++) {
            separateChainingHashTableWithRedBlackBST.get(randomKeysGet[i]);
        }
        double timeSpentOnGetRedBlackBST = stopwatch.elapsedTime();

        printResults("Get", timeSpentOnGetSequentialSearch, timeSpentOnGetRedBlackBST);

        //Delete tests
        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length / 2; i++) {
            separateChainingHashTable.delete(randomKeysPut[i]);
        }
        double timeSpentOnDeleteSequentialSearch = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length / 2; i++) {
            separateChainingHashTableWithRedBlackBST.delete(randomKeysPut[i]);
        }
        double timeSpentOnDeleteRedBlackBST = stopwatch.elapsedTime();

        printResults("Delete", timeSpentOnDeleteSequentialSearch, timeSpentOnDeleteRedBlackBST);
    }

    public static void main(String[] args) {
        new Exercise37_Hybrid().doExperiment();
    }

    private void printResults(String operation, double sequentialSearchTime, double redBlackBSTTime) {
        StdOut.printf("%9s %25.2f %21.2f\n", operation, sequentialSearchTime, redBlackBSTTime);
    }

}
