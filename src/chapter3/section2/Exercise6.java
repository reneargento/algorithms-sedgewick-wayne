package chapter3.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 09/05/17.
 */
public class Exercise6 {

    private class BinarySearchTree<Key extends Comparable<Key>, Value>{

        private class Node {
            private Key key;
            private Value value;

            private Node left;
            private Node right;

            private int size; //# of nodes in subtree rooted here
            private int height; //height of the subtree rooted here

            public Node(Key key, Value value, int size) {
                this.key = key;
                this.value = value;
                this.size = size;
            }
        }

        private Node root;

        public int size() {
            return size(root);
        }

        private int size(Node node) {
            if (node == null) {
                return 0;
            }

            return node.size;
        }

        public int heightRecursive() {
            if (root == null) {
                return -1;
            }

            return heightRecursive(root, -1);
        }

        private int heightRecursive(Node node, int currentHeight) {
            if (node == null) {
                return currentHeight;
            }

            int height = currentHeight;

            int leftHeight = heightRecursive(node.left, currentHeight + 1);
            if (leftHeight > height) {
                height = leftHeight;
            }

            int rightHeight = heightRecursive(node.right, currentHeight + 1);
            if (rightHeight > height) {
                height = rightHeight;
            }

            return height;
        }

        public int heightConstant() {
            return heightConstant(root);
        }

        private int heightConstant(Node node) {
            if (node == null) {
                return -1;
            }

            return node.height;
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
                return new Node(key, value, 1);
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }

            node.size = size(node.left) + 1 + size(node.right);
            node.height = Math.max(heightConstant(node.left), heightConstant(node.right)) + 1;

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

        public Key select(int index) {
            if (index >= size()) {
                throw new IllegalArgumentException("Index is higher than tree size");
            }

            return select(root, index).key;
        }

        private Node select(Node node, int index) {
            int leftSubtreeSize = size(node.left);

            if (leftSubtreeSize == index) {
                return node;
            } else if (leftSubtreeSize > index) {
                return select(node.left, index);
            } else {
                return select(node.right, index - leftSubtreeSize - 1);
            }
        }

        public int rank(Key key) {
            return rank(root, key);
        }

        private int rank(Node node, Key key) {
            if (node == null) {
                return 0;
            }

            //Returns the number of keys less than node.key in the subtree rooted at node
            int compare = key.compareTo(node.key);
            if (compare < 0) {
                return rank(node.left, key);
            } else if (compare > 0) {
                return size(node.left) + 1 + rank(node.right, key);
            } else {
                return size(node.left);
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
                return node.right;
            }

            node.left = deleteMin(node.left);

            node.size = size(node.left) + 1 + size(node.right);
            node.height = Math.max(heightConstant(node.left), heightConstant(node.right)) + 1;

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
                return node.left;
            }

            node.right = deleteMax(node.right);

            node.size = size(node.left) + 1 + size(node.right);
            node.height = Math.max(heightConstant(node.left), heightConstant(node.right)) + 1;

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

            node.size = size(node.left) + 1 + size(node.right);
            node.height = Math.max(heightConstant(node.left), heightConstant(node.right)) + 1;
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
        Exercise6 exercise6 = new Exercise6();

        exercise6.testRecursiveHeightMethod();
        exercise6.testNonRecursiveHeightMethod();
    }

    private void testRecursiveHeightMethod() {
        BinarySearchTree<Integer, Integer> binarySearchTree = new BinarySearchTree<>();

        StdOut.println("Recursive height method tests");
        StdOut.println("Height 1: " + binarySearchTree.heightRecursive() + " Expected: -1");

        binarySearchTree.put(0, 0);
        binarySearchTree.put(1, 1);
        binarySearchTree.put(2, 2);
        binarySearchTree.put(3, 3);

        StdOut.println("Height 2: " + binarySearchTree.heightRecursive() + " Expected: 3");

        binarySearchTree.put(-1, -1);
        binarySearchTree.put(-2, -2);

        StdOut.println("Height 3: " + binarySearchTree.heightRecursive() + " Expected: 3");

        binarySearchTree.put(-10, -10);
        binarySearchTree.put(-7, -7);

        StdOut.println("Height 4: " + binarySearchTree.heightRecursive() + " Expected: 4");

        binarySearchTree.delete(-7);
        StdOut.println("Height 5: " + binarySearchTree.heightRecursive() + " Expected: 3");

        binarySearchTree.deleteMin();
        binarySearchTree.deleteMax();
        StdOut.println("Height 6: " + binarySearchTree.heightRecursive() + " Expected: 2");
    }

    private void testNonRecursiveHeightMethod() {
        BinarySearchTree<Integer, Integer> binarySearchTree = new BinarySearchTree<>();

        StdOut.println("\nAdded-field height method tests");
        StdOut.println("Height 1: " + binarySearchTree.heightConstant() + " Expected: -1");

        binarySearchTree.put(0, 0);
        binarySearchTree.put(1, 1);
        binarySearchTree.put(2, 2);
        binarySearchTree.put(3, 3);

        StdOut.println("Height 2: " + binarySearchTree.heightConstant() + " Expected: 3");

        binarySearchTree.put(-1, -1);
        binarySearchTree.put(-2, -2);

        StdOut.println("Height 3: " + binarySearchTree.heightConstant() + " Expected: 3");

        binarySearchTree.put(-10, -10);
        binarySearchTree.put(-7, -7);

        StdOut.println("Height 4: " + binarySearchTree.heightConstant() + " Expected: 4");

        binarySearchTree.delete(-7);
        StdOut.println("Height 5: " + binarySearchTree.heightConstant() + " Expected: 3");

        binarySearchTree.deleteMin();
        binarySearchTree.deleteMax();
        StdOut.println("Height 6: " + binarySearchTree.heightConstant() + " Expected: 2");
    }

}
