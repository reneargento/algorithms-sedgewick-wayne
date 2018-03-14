package chapter3.section1;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 23/04/17.
 */
public class Exercise3 {

    public class OrderedSequentialSearchST<Key extends Comparable<Key>, Value> {

        private class Node {
            private Key key;
            private Value value;
            private Node next;

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

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            if (isEmpty()) {
                first = new Node(key, value, null);
                size++;
                return;
            }

            //Check first node
            if (first.key.compareTo(key) == 0) {
                first.value = value;
                return;
            } else if (first.key.compareTo(key) > 0) {
                first = new Node(key, value, first);
                size++;
                return;
            }

            //Check all other nodes
            for(Node node = first; node != null; node = node.next) {
                if (node.next != null) {
                    if (node.next.key.compareTo(key) == 0) {
                        node.next.value = value;
                        return;
                    } else if (node.next.key.compareTo(key) > 0) {
                        Node newNode = new Node(key, value, node.next);
                        node.next = newNode;
                        size++;
                        return;
                    }
                } else {
                    Node newNode = new Node(key, value, null);
                    node.next = newNode;
                    size++;
                    return;
                }
            }
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            for(Node node = first; node != null; node = node.next) {
                if (node.key.compareTo(key) == 0) {
                    return node.value;
                }
            }

            return null;
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty()) {
                return;
            }

            //Check first node
            if (first.key.compareTo(key) == 0) {
                first = first.next;
                size--;
                return;
            }

            //Check all other nodes
            for(Node node = first; node != null; node = node.next) {
                if (node.next != null && node.next.key.compareTo(key) == 0) {
                    node.next = node.next.next;
                    size--;
                    return;
                }
            }
        }

        public boolean contains(Key key) {
            for(Node node = first; node != null; node = node.next) {
                if (node.key.compareTo(key) == 0) {
                    return true;
                }
            }

            return false;
        }

        public Key min() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return first.key;
        }

        public Key max() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty symbol table");
            }

            for(Node node = first; node != null; node = node.next) {
                if (node.next == null) {
                    return node.key;
                }
            }

            return null;
        }

        public Key floor(Key key) {
            if (isEmpty()) {
                return null;
            }

            //Check first node
            if (first.key.compareTo(key) > 0) {
                return null;
            } else if (first.key.compareTo(key) == 0) {
                return first.key;
            }

            //Check all other nodes
            for(Node node = first; node != null; node = node.next) {
                if (node.next != null) {
                    if (node.next.key.compareTo(key) == 0) {
                        return node.next.key;
                    } else if (node.next.key.compareTo(key) > 0) {
                        return node.key;
                    }
                } else {
                    return node.key;
                }
            }

            return null;
        }

        public Key ceiling(Key key) {
            if (isEmpty()) {
                return null;
            }

            //Check first node
            if (first.key.compareTo(key) == 0
                    || first.key.compareTo(key) > 0) {
                return first.key;
            }

            //Check all other nodes
            for(Node node = first; node != null; node = node.next) {
                if (node.next != null) {
                    if (node.next.key.compareTo(key) == 0
                            || node.next.key.compareTo(key) > 0) {
                        return node.next.key;
                    }
                }
            }

            return null;
        }

        public int rank(Key key) {
            int rank = 0;

            for(Node node = first; node != null; node = node.next) {
                if (node.key.compareTo(key) < 0) {
                    rank++;
                } else {
                    break;
                }
            }

            return rank;
        }

        public Key select(int rank) {
            if (rank < 0 || rank >= size) {
                throw new IllegalArgumentException("Invalid argument: " + rank);
            }

            int currentRank = 0;

            for(Node node = first; node != null; node = node.next) {
                if (currentRank == rank) {
                    return node.key;
                }

                currentRank++;
            }

            return null;
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
            int size = 0;
            boolean inTheRange = false;

            for(Node node = first; node != null; node = node.next) {
                if (!inTheRange) {
                    if (node.key.compareTo(low) == 0
                            || node.key.compareTo(low) > 0) {
                        size++;
                        inTheRange = true;
                    }
                } else {
                    if (node.key.compareTo(high) == 0
                            || node.key.compareTo(high) < 0) {
                        size++;
                    } else {
                        return size;
                    }
                }
            }

            return size;
        }

        public Iterable<Key> keys(Key low, Key high) {
            Queue<Key> keys = new Queue<>();
            boolean inTheRange = false;

            for(Node node = first; node != null; node = node.next) {
                if (!inTheRange) {
                    if (node.key.compareTo(low) == 0
                            || node.key.compareTo(low) > 0) {
                        keys.enqueue(node.key);
                        inTheRange = true;
                    }
                } else {
                    if (node.key.compareTo(high) == 0
                            || node.key.compareTo(high) < 0) {
                        keys.enqueue(node.key);
                    } else {
                        return keys;
                    }
                }
            }

            return keys;
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
        Exercise3 exercise3 = new Exercise3();
        OrderedSequentialSearchST<Integer, String> orderedSequentialSearchST = exercise3.new OrderedSequentialSearchST<>();

        //Test put() and get() and keys()
        orderedSequentialSearchST.put(5, "Value 5");
        orderedSequentialSearchST.put(1, "Value 1");
        orderedSequentialSearchST.put(9, "Value 9");
        orderedSequentialSearchST.put(2, "Value 2");
        orderedSequentialSearchST.put(0, "Value 0");
        orderedSequentialSearchST.put(99, "Value 99");

        StdOut.println();

        for(Integer key : orderedSequentialSearchST.keys()) {
            StdOut.println("Key " + key + ": " + orderedSequentialSearchST.get(key));
        }

        //Test delete()
        StdOut.println("\nDelete key 2");
        orderedSequentialSearchST.delete(2);
        for(Integer key : orderedSequentialSearchST.keys()) {
            StdOut.println("Key " + key + ": " + orderedSequentialSearchST.get(key));
        }

        StdOut.println();

        //Test contains()
        StdOut.println("Contains key 98: " + orderedSequentialSearchST.contains(98) + " Expected: false");
        StdOut.println("Contains key 99: " + orderedSequentialSearchST.contains(99) + " Expected: true");

        //Test isEmpty()
        StdOut.println("Is empty: " + orderedSequentialSearchST.isEmpty() + " Expected: false");

        //Test size()
        StdOut.println("Size: " + orderedSequentialSearchST.size() + " Expected: 5");

        //Test min()
        StdOut.println("Min key: " + orderedSequentialSearchST.min() + " Expected: 0");

        //Test max()
        StdOut.println("Max key: " + orderedSequentialSearchST.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + orderedSequentialSearchST.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + orderedSequentialSearchST.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + orderedSequentialSearchST.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + orderedSequentialSearchST.ceiling(15) + " Expected: 99");

        //Test rank()
        StdOut.println("Rank of key 9: " + orderedSequentialSearchST.rank(9) + " Expected: 3");
        StdOut.println("Rank of key 10: " + orderedSequentialSearchST.rank(10) + " Expected: 4");

        //Test select()
        StdOut.println("Select key of rank 4: " + orderedSequentialSearchST.select(4) + " Expected: 99");

        //Test deleteMin()
        StdOut.println("\nDelete min (key 0)");

        orderedSequentialSearchST.deleteMin();
        for(Integer key : orderedSequentialSearchST.keys()) {
            StdOut.println("Key " + key + ": " + orderedSequentialSearchST.get(key));
        }

        //Test deleteMax()
        StdOut.println("\nDelete max (key 99)");

        orderedSequentialSearchST.deleteMax();
        for(Integer key : orderedSequentialSearchST.keys()) {
            StdOut.println("Key " + key + ": " + orderedSequentialSearchST.get(key));
        }

        StdOut.println();

        //Test size()
        StdOut.println("Size of keys between 2 and 10: " + orderedSequentialSearchST.size(2, 10) + " Expected: 2");

        //Test keys() with range
        StdOut.println();
        for(Integer key : orderedSequentialSearchST.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + orderedSequentialSearchST.get(key));
        }
    }

}
