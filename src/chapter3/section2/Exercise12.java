package chapter3.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 28/05/17.
 */
public class Exercise12 {

    private class BinarySearchTree<Key extends Comparable<Key>, Value>{

        private int size;

        private class Node {
            private Key key;
            private Value value;

            private Node left;
            private Node right;

            public Node(Key key, Value value) {
                this.key = key;
                this.value = value;
            }
        }

        private Node root;

        public int size() {
           return size;
        }

        public Value get(Key key) {
            return get(root, key);
        }

        private Value get(Node node, Key key) {
            if (node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);
            if (compare < 0) {
                return get(node.left, key);
            } else if (compare > 0) {
                return get(node.right, key);
            } else {
                return node.value;
            }
        }

        public void put(Key key, Value value) {
            root = put(root, key, value);
        }

        private Node put(Node node, Key key, Value value) {
            if (node == null) {
                size++;
                return new Node(key, value);
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }

            return node;
        }

        public Key min() {
            if (root == null) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            return min(root).key;
        }

        private Node min(Node node) {
            if (node.left == null) {
                return node;
            }

            return min(node.left);
        }

        public Key max() {
            if (root == null) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            return max(root).key;
        }

        private Node max(Node node) {
            if (node.right == null) {
                return node;
            }

            return max(node.right);
        }

        public Key floor(Key key) {
            Node node = floor(root, key);
            if (node == null) {
                return null;
            }

            return node.key;
        }

        private Node floor(Node node, Key key) {
            if (node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);

            if (compare == 0) {
                return node;
            } else if (compare < 0) {
                return floor(node.left, key);
            } else {
                Node rightNode = floor(node.right, key);
                if (rightNode != null) {
                    return rightNode;
                } else {
                    return node;
                }
            }
        }

        public Key ceiling(Key key) {
            Node node = ceiling(root, key);
            if (node == null) {
                return null;
            }

            return node.key;
        }

        private Node ceiling(Node node, Key key) {
            if (node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);

            if (compare == 0) {
                return node;
            } else if (compare > 0) {
                return ceiling(node.right, key);
            } else {
                Node leftNode = ceiling(node.left, key);
                if (leftNode != null) {
                    return leftNode;
                } else {
                    return node;
                }
            }
        }

        public void deleteMin() {
            root = deleteMin(root);
        }

        private Node deleteMin(Node node) {
            if (node == null) {
                return null;
            }

            if (node.left == null) {
                size--;
                return node.right;
            }

            node.left = deleteMin(node.left);
            return node;
        }

        public void deleteMax() {
            root = deleteMax(root);
        }

        private Node deleteMax(Node node) {
            if (node == null) {
                return null;
            }

            if (node.right == null) {
                size--;
                return node.left;
            }

            node.right = deleteMax(node.right);
            return node;
        }

        public void delete(Key key) {
            root = delete(root, key);
        }

        private Node delete(Node node, Key key) {
            if (node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);
            if (compare < 0) {
                node.left = delete(node.left, key);
            } else if (compare > 0) {
                node.right = delete(node.right, key);
            } else {
                size--;

                if (node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                } else {
                    Node aux = node;
                    node = min(aux.right);
                    node.right = deleteMin(aux.right);
                    node.left = aux.left;
                }
            }

            return node;
        }

        public Iterable<Key> keys() {
            return keys(min(), max());
        }

        public Iterable<Key> keys(Key low, Key high) {
            Queue<Key> queue = new Queue<>();
            keys(root, queue, low, high);
            return queue;
        }

        private void keys(Node node, Queue<Key> queue, Key low, Key high) {
            if (node == null) {
                return;
            }

            int compareLow = low.compareTo(node.key);
            int compareHigh = high.compareTo(node.key);

            if (compareLow < 0) {
                keys(node.left, queue, low, high);
            }

            if (compareLow <= 0 && compareHigh >= 0) {
                queue.enqueue(node.key);
            }

            if (compareHigh > 0) {
                keys(node.right, queue, low, high);
            }
        }

    }

    public static void main(String[] args) {
        Exercise12 exercise12 = new Exercise12();
        BinarySearchTree<Integer, String> binarySearchTree = exercise12.new BinarySearchTree<>();

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

