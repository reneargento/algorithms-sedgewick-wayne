package chapter3.section4;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

/**
 * Created by rene on 17/07/17.
 */
@SuppressWarnings("unchecked")
public class SeparateChainingHashTable<Key, Value> {

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

        public boolean contains(Key key) {
            return get(key) != null;
        }

        public Value get(Key key) {
            for(Node node = first; node != null; node = node.next) {
                if(key.equals(node.key)) {
                    return node.value;
                }
            }

            return null;
        }

        public void put(Key key, Value value) {
            for(Node node = first; node != null; node = node.next) {
                if(key.equals(node.key)) {
                    node.value = value;
                    return;
                }
            }

            first = new Node(key, value, first);
            size++;
        }

        public void delete(Key key) {
            size--;

            if(first.key.equals(key)) {
                first = first.next;
                return;
            }

            for(Node node = first; node != null; node = node.next) {
                if(node.next != null && node.next.key.equals(key)) {
                    node.next = node.next.next;
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

    public static final double INCREASE_THRESHOLD = 0.7;
    public static final double DECREASE_THRESHOLD = 0.25;

    private int size;
    private int keysSize;
    SequentialSearchSymbolTable[] symbolTable;

    public SeparateChainingHashTable() {
        this(997);
    }

    public SeparateChainingHashTable(int size) {
        this.size = size;
        symbolTable = new SequentialSearchSymbolTable[size];

        for(int i=0; i < size; i++) {
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
        return (key.hashCode() & 0x7fffffff) % size;
    }

    private double getLoadFactor() {
        return ((double) keysSize) / (double) size;
    }

    public boolean contains(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Argument to contains() cannot be null");
        }

        return get(key) != null;
    }

    public void resize(int newSize) {
        SeparateChainingHashTable<Key, Value> separateChainingHashTableTemp =
                new SeparateChainingHashTable<>(newSize);

        for(Key key : keys()) {
            separateChainingHashTableTemp.put(key, get(key));
        }

        symbolTable = separateChainingHashTableTemp.symbolTable;
        size = separateChainingHashTableTemp.size;
        keysSize = separateChainingHashTableTemp.keysSize;
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

        if(value == null) {
            delete(key);
            return;
        }

        int currentSize = symbolTable[hash(key)].size;
        symbolTable[hash(key)].put(key, value);

        if(currentSize < symbolTable[hash(key)].size) {
            keysSize++;
        }

        if(getLoadFactor() > INCREASE_THRESHOLD) {
            resize(size * 2);
        }
    }

    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Argument to delete() cannot be null");
        }

        if(isEmpty()) {
            return;
        }

        if(!contains(key)) {
            return;
        }

        symbolTable[hash(key)].delete(key);
        keysSize--;

        if(getLoadFactor() < DECREASE_THRESHOLD) {
            resize(size / 2);
        }
    }

    public Iterable<Key> keys() {
        Queue<Key> keys = new Queue<>();

        for(SequentialSearchSymbolTable sequentialSearchST : symbolTable) {
            for(Object key : sequentialSearchST.keys()) {
                keys.enqueue((Key) key);
            }
        }

        if(!keys.isEmpty() && keys.peek() instanceof Comparable) {
            Key[] keysToBeSorted = (Key[]) new Comparable[keys.size()];
            for(int i=0; i < keysToBeSorted.length; i++) {
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
