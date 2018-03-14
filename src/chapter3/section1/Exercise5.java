package chapter3.section1;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/04/17.
 */
public class Exercise5 {

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

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
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

    public static void main(String[] args) {
        Exercise5 exercise5 = new Exercise5();
        SequentialSearchSymbolTable<Integer, String> sequentialSearchSymbolTable = exercise5.new SequentialSearchSymbolTable<>();

        //Test isEmpty()
        StdOut.println("Is empty: " + sequentialSearchSymbolTable.isEmpty() + " Expected: true");

        //Test size()
        StdOut.println("Size: " + sequentialSearchSymbolTable.size() + " Expected: 0");

        //Test put() and get() and keys()
        sequentialSearchSymbolTable.put(5, "Value 5");
        sequentialSearchSymbolTable.put(1, "Value 1");
        sequentialSearchSymbolTable.put(9, "Value 9");
        sequentialSearchSymbolTable.put(2, "Value 2");
        sequentialSearchSymbolTable.put(0, "Value 0");
        sequentialSearchSymbolTable.put(99, "Value 99");

        StdOut.println();

        for(Integer key : sequentialSearchSymbolTable.keys()) {
            StdOut.println("Key " + key + ": " + sequentialSearchSymbolTable.get(key));
        }

        //Test delete()
        StdOut.println("\nDelete key 2");
        sequentialSearchSymbolTable.delete(2);
        for(Integer key : sequentialSearchSymbolTable.keys()) {
            StdOut.println("Key " + key + ": " + sequentialSearchSymbolTable.get(key));
        }

        StdOut.println();

        //Test isEmpty()
        StdOut.println("Is empty: " + sequentialSearchSymbolTable.isEmpty() + " Expected: false");

        //Test size()
        StdOut.println("Size: " + sequentialSearchSymbolTable.size() + " Expected: 5");
    }

}
