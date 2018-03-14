package chapter3.section1;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 26/04/17.
 */
@SuppressWarnings("unchecked")
public class Exercise25_SoftwareCaching {

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

        private Node cacheItem;

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

            if (cacheItem != null && cacheItem.key == key) {
                StdOut.println("Cache hit");
                return cacheItem.value;
            }

            StdOut.println("Cache miss");

            for(Node node = first; node != null; node = node.next) {
                if (key.equals(node.key)) {

                    cacheItem = node;
                    return node.value;
                }
            }

            return null;
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            if (cacheItem != null && cacheItem.key == key) {
                cacheItem.value = value;

                StdOut.println("Cache hit");
                return;
            } else {
                StdOut.println("Cache miss");
            }

            for(Node node = first; node != null; node = node.next) {
                if (key.equals(node.key)) {
                    node.value = value;
                    return;
                }
            }

            first = new Node(key, value, first);
            size++;
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty()) {
                return;
            }

            if (cacheItem != null && cacheItem.key == key) {
                invalidateCache();
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

        private void invalidateCache() {
            cacheItem = null;
        }
    }

    private class BinarySearchSymbolTable<Key extends Comparable<Key>, Value> {

        private Key[] keys;
        private Value[] values;
        private int size;

        private Key cacheKey;
        private int cacheKeyRank;

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

            int rank = getRankFromCacheOrCompute(key);

            if (rank < size && keys[rank].compareTo(key) == 0) {

                if (cacheKey == null || cacheKey != key) {
                    cacheKey = key;
                    cacheKeyRank = rank;
                }

                return values[rank];
            } else {
                return null;
            }
        }

        public int rank(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (cacheKey != null && cacheKey.compareTo(key) == 0) {
                return cacheKeyRank;
            }

            int low = 0;
            int high = size - 1;

            while (low <= high) {
                int middle = low + (high - low) / 2;

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

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            int rank = getRankFromCacheOrCompute(key);

            if (cacheKey != null && key.compareTo(cacheKey) <= 0) {
                invalidateCache();
            }

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

            int rank = getRankFromCacheOrCompute(key);

            if (key.compareTo(cacheKey) <= 0) {
                invalidateCache();
            }

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
            int rank = getRankFromCacheOrCompute(key);

            if (rank == size) {
                return null;
            }

            return keys[rank];
        }

        public Key floor(Key key) {
            if (contains(key)) {
                return key;
            }

            int rank = getRankFromCacheOrCompute(key);

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
            } else if (contains(high)) {
                return rank(high) - rank(low) + 1;
            } else {
                return rank(high) - rank(low);
            }
        }

        public Iterable<Key> keys(Key low, Key high) {
            if (low == null || high == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            Queue<Key> queue = new Queue<>();

            for(int i = rank(low); i < rank(high); i++) {
                queue.enqueue(keys[i]);
            }

            if (contains(high)) {
                queue.enqueue(keys[rank(high)]);
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

        private int getRankFromCacheOrCompute(Key key) {

            int rank;

            if (cacheKey != null && cacheKey.compareTo(key) == 0) {
                StdOut.println("Cache hit");
                rank = cacheKeyRank;
            } else {
                StdOut.println("Cache miss");
                rank = rank(key);
            }

            return rank;
        }

        private void invalidateCache() {
            cacheKey = null;
            cacheKeyRank = -1;
        }
    }


    public static void main(String[] args) {

        Exercise25_SoftwareCaching softwareCaching = new Exercise25_SoftwareCaching();

        StdOut.println("Sequential Search Symbol Table tests");
        SequentialSearchSymbolTable<Integer, String> sequentialSearchSymbolTable = softwareCaching.new SequentialSearchSymbolTable<>();

        for(int i = 0; i < 10; i++) {
            sequentialSearchSymbolTable.put(i, "Value " + i);
        }

        //Cache on get operation
        StdOut.println("\nGet key 2:");
        sequentialSearchSymbolTable.get(2);
        StdOut.println("Expected: cache miss");

        StdOut.println("\nGet key 2:");
        sequentialSearchSymbolTable.get(2);
        StdOut.println("Expected: cache hit");

        //Cache on delete operation
        StdOut.println("\nDelete key 2:");
        sequentialSearchSymbolTable.delete(2);

        sequentialSearchSymbolTable.get(2);
        StdOut.println("Expected: cache miss");

        sequentialSearchSymbolTable.get(9);
        //Cache with put operation
        StdOut.println("\nPut (update) key 9:");
        sequentialSearchSymbolTable.put(9, "Value 99");
        StdOut.println("Expected: cache hit");

        StdOut.println("\nGet key 5:");
        sequentialSearchSymbolTable.get(5);
        StdOut.println("Expected: cache miss");


        StdOut.println("\nBinary Search Symbol Table tests");
        BinarySearchSymbolTable<Integer, String> binarySearchSymbolTable = softwareCaching.new BinarySearchSymbolTable<>();

        for(int i = 0; i < 10; i++) {
            binarySearchSymbolTable.put(i, "Value " + i);
        }

        //Cache on get operation
        StdOut.println("\nGet key 2:");
        binarySearchSymbolTable.get(2);
        StdOut.println("Expected: cache miss");

        StdOut.println("\nGet key 2:");
        binarySearchSymbolTable.get(2);
        StdOut.println("Expected: cache hit");

        //Cache on delete operation
        StdOut.println("\nDelete key 4:");
        binarySearchSymbolTable.delete(4);
        StdOut.println("Expected: cache miss and hit");

        binarySearchSymbolTable.get(7);

        StdOut.println("\nDelete key 7:");
        binarySearchSymbolTable.delete(7);
        StdOut.println("Expected: cache hit and hit");

        binarySearchSymbolTable.get(9);
        //Cache with put operation
        StdOut.println("\nPut higher value than cache:");
        binarySearchSymbolTable.put(11, "Value 11");

        binarySearchSymbolTable.get(9);
        StdOut.println("Expected: cache hit");

        binarySearchSymbolTable.put(-1, "Value -1");
        StdOut.println("\nPut lower value than cache:");
        binarySearchSymbolTable.get(9);
        StdOut.println("Expected: cache miss");
    }

}
