package chapter3.section4;

import edu.princeton.cs.algs4.Queue;

/**
 * Created by rene on 17/07/17.
 */
@SuppressWarnings("unchecked")
public class SeparateChainingHashTable<Key, Value> {

    private class SequentialSearchSymbolTable {

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
            if(isEmpty()) {
                return;
            }

            if(first.key.equals(key)) {
                first = first.next;
                size--;
                return;
            }

            for(Node node = first; node != null; node = node.next) {
                if(node.next != null && node.next.key.equals(key)) {
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
    private SequentialSearchSymbolTable[] symbolTable;

    public SeparateChainingHashTable() {
        this(997);
    }

    public SeparateChainingHashTable(int size) {
        this.size = size;
        symbolTable = (SequentialSearchSymbolTable[]) new Object[size];

        for(int i=0; i < size; i++) {
            symbolTable[i] = new SequentialSearchSymbolTable();
        }
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % size;
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

        if(value == null) {
            delete(key);
            return;
        }

        symbolTable[hash(key)].put(key, value);
    }

    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Argument to delete() cannot be null");
        }

        symbolTable[hash(key)].delete(key);
    }

    public Iterable<Key> keys() {
        //Will be implemented in exercise 3.4.19
        return null;
    }

}
