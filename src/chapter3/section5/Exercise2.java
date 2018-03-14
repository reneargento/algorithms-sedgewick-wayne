package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 03/08/17.
 */
public class Exercise2 {

    private class SequentialSearchSet<Key> {

        private class Node {
            Key key;
            Node next;

            public Node(Key key, Node next) {
                this.key = key;
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

            for(Node node = first; node != null; node = node.next) {
                if (key.equals(node.key)) {
                    return true;
                }
            }

            return false;
        }

        public void add(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            for(Node node = first; node != null; node = node.next) {
                if (key.equals(node.key)) {
                    node.key = key;
                    return;
                }
            }

            first = new Node(key, first);
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

        @Override
        public String toString() {
            if (isEmpty()) {
                return "{ }";
            }

            StringBuilder stringBuilder = new StringBuilder("{");

            boolean isFirstKey = true;
            for(Key key : keys()) {
                if (isFirstKey) {
                    isFirstKey = false;
                } else {
                    stringBuilder.append(",");
                }

                stringBuilder.append(" ").append(key);
            }

            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Exercise2 exercise2 = new Exercise2();
        SequentialSearchSet<Integer> sequentialSearchSet = exercise2.new SequentialSearchSet<>();

        sequentialSearchSet.add(5);
        sequentialSearchSet.add(1);
        sequentialSearchSet.add(9);
        sequentialSearchSet.add(2);
        sequentialSearchSet.add(0);
        sequentialSearchSet.add(99);
        sequentialSearchSet.add(-1);
        sequentialSearchSet.add(-2);
        sequentialSearchSet.add(3);
        sequentialSearchSet.add(-5);

        StdOut.println("Keys() test");

        for(Integer key : sequentialSearchSet.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: -5 -2 -1 0 1 2 3 5 9 99 - Not necessarily in this order");

        StdOut.println("\ntoString() test: " + sequentialSearchSet);

        StdOut.println("\nContains 0: " + sequentialSearchSet.contains(0) + " Expected: true");
        StdOut.println("Contains 100: " + sequentialSearchSet.contains(100) + " Expected: false");

        //Test delete()
        StdOut.println("\nDelete key 2");
        sequentialSearchSet.delete(2);
        StdOut.println(sequentialSearchSet);

        StdOut.println("\nDelete key 99");
        sequentialSearchSet.delete(99);
        StdOut.println(sequentialSearchSet);

        StdOut.println("\nDelete key -5");
        sequentialSearchSet.delete(-5);
        StdOut.println(sequentialSearchSet);
    }

}
