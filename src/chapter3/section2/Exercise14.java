package chapter3.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Created by Rene Argento on 31/05/17.
 */
public class Exercise14 {

    private class BinarySearchTree<Key extends Comparable<Key>, Value> extends chapter3.section2.BinarySearchTree<Key, Value>{

        public Key min() {
            if (root == null) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            Node current = root;

            while (current != null) {
                if (current.left == null) {
                    return current.key;
                } else {
                    current = current.left;
                }
            }

            return null;
        }

        //Used for the delete operation
        public Node min(Node current) {

            while (current != null) {
                if (current.left == null) {
                    return current;
                } else {
                    current = current.left;
                }
            }

            return null;
        }

        public Key max() {
            if (root == null) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            Node current = root;

            while (current != null) {
                if (current.right == null) {
                    return current.key;
                } else {
                    current = current.right;
                }
            }

            return null;
        }

        public Key floor(Key key) {

            Node current = root;
            Key currentFloor = null;

            while (current != null) {
                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    current = current.left;
                } else if (compare > 0) {
                    currentFloor = current.key;
                    current = current.right;
                } else {
                    currentFloor = current.key;
                    break;
                }
            }

            return currentFloor;
        }

        public Key ceiling(Key key) {

            Node current = root;
            Key currentCeiling = null;

            while (current != null) {
                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    currentCeiling = current.key;
                    current = current.left;
                } else if (compare > 0) {
                    current = current.right;
                } else {
                    currentCeiling = current.key;
                    break;
                }
            }

            return currentCeiling;
        }

        public Key select(int index) {
            if (index >= size()) {
                throw new IllegalArgumentException("Index is higher than tree size");
            }

            Node current = root;

            while (current != null) {
                int leftSubtreeSize = size(current.left);

                if (leftSubtreeSize == index) {
                    return current.key;
                } else if (leftSubtreeSize > index) {
                    current = current.left;
                } else {
                    index -= (leftSubtreeSize + 1);
                    current = current.right;
                }

            }

            return null;
        }

        public int rank(Key key) {
            Node current = root;

            int rank = 0;

            while (current != null) {
                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    current = current.left;
                } else if (compare > 0) {
                    rank += size(current.left) + 1;
                    current = current.right;
                } else {
                    rank += size(current.left);
                    return rank;
                }
            }

            return rank;
        }

        public Iterable<Key> keys() {
            Queue<Key> queue = new Queue<>();

            Stack<Node> stack = new Stack<>();

            Node current = root;

            while (current != null || !stack.isEmpty()) {
                if (current != null) {
                    stack.push(current);
                    current = current.left;
                } else {
                    current = stack.pop();
                    queue.enqueue(current.key);

                    current = current.right;
                }
            }

            return queue;
        }

    }

    public static void main(String[] args) {
        Exercise14 exercise14 = new Exercise14();
        BinarySearchTree<Integer, String> binarySearchTree = exercise14.new BinarySearchTree<>();

        //Test put()
        binarySearchTree.put(5, "Value 5");
        binarySearchTree.put(1, "Value 1");
        binarySearchTree.put(9, "Value 9");
        binarySearchTree.put(2, "Value 2");
        binarySearchTree.put(0, "Value 0");
        binarySearchTree.put(99, "Value 99");

        StdOut.println();

        //Test size()
        StdOut.println("Size: " + binarySearchTree.size() + " Expected: 6");

        //Test get() and keys()
        for(Integer key : binarySearchTree.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        //Test delete()
        StdOut.println("\nDelete key 2");
        binarySearchTree.delete(2);
        for(Integer key : binarySearchTree.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        StdOut.println();

        //Test size()
        StdOut.println("Size: " + binarySearchTree.size() + " Expected: 5");

        //Test min()
        StdOut.println("Min key: " + binarySearchTree.min() + " Expected: 0");

        //Test max()
        StdOut.println("Max key: " + binarySearchTree.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + binarySearchTree.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + binarySearchTree.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + binarySearchTree.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + binarySearchTree.ceiling(15) + " Expected: 99");

        //Test select()
        StdOut.println("Select key of rank 4: " + binarySearchTree.select(4) + " Expected: 99");

        //Test rank()
        StdOut.println("Rank of key 9: " + binarySearchTree.rank(9) + " Expected: 3");
        StdOut.println("Rank of key 10: " + binarySearchTree.rank(10) + " Expected: 4");

        //Test deleteMin()
        StdOut.println("\nDelete min (key 0)");

        binarySearchTree.deleteMin();
        for(Integer key : binarySearchTree.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        //Test deleteMax()
        StdOut.println("\nDelete max (key 99)");

        binarySearchTree.deleteMax();
        for(Integer key : binarySearchTree.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        //Test keys() with range
        StdOut.println();
        StdOut.println("Keys in range [2, 10]");
        for(Integer key : binarySearchTree.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        StdOut.println("Size: " + binarySearchTree.size() + " Expected: 3");
    }

}
