package chapter3.section4;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

/**
 * Created by Rene Argento on 28/07/17.
 */
@SuppressWarnings("unchecked")
public class SeparateChainingHashTableFixedSize<Key, Value> {

    class SequentialSearchSymbolTable<Key, Value> {

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
    SequentialSearchSymbolTable[] symbolTable;

    private static final int DEFAULT_HASH_TABLE_SIZE = 997;

    public SeparateChainingHashTableFixedSize() {
        this(DEFAULT_HASH_TABLE_SIZE);
    }

    public SeparateChainingHashTableFixedSize(int size) {
        this.size = size;
        symbolTable = new SequentialSearchSymbolTable[size];

        for(int i = 0; i < size; i++) {
            symbolTable[i] = new SequentialSearchSymbolTable();
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

        return (Value) symbolTable[hash(key)].get(key);
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
