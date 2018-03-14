package chapter3.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 03/06/17.
 */
public class Exercise28_SoftwareCaching {

    private class BinarySearchTree<Key extends Comparable<Key>, Value>{

        private class Node {
            private Key key;
            private Value value;

            private Node left;
            private Node right;

            private int size; //# of nodes in subtree rooted here

            public Node(Key key, Value value, int size) {
                this.key = key;
                this.value = value;
                this.size = size;
            }
        }

        private Node root;

        private Node cacheItem;

        public int size() {
            return size(root);
        }

        private int size(Node node) {
            if (node == null) {
                return 0;
            }

            return node.size;
        }

        public Value get(Key key) {

            if (cacheItem != null && cacheItem.key == key) {
                StdOut.println("Cache hit");
                return cacheItem.value;
            }
            StdOut.println("Cache miss");

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
                cacheItem = node;

                return node.value;
            }
        }

        public void put(Key key, Value value) {

            if (cacheItem != null && cacheItem.key == key) {
                cacheItem.value = value;
                StdOut.println("Cache hit");
                return;
            }

            StdOut.println("Cache miss");

            root = put(root, key, value);
        }

        private Node put(Node node, Key key, Value value) {
            if (node == null) {
                Node newNode = new Node(key, value, 1);
                cacheItem = newNode;
                return newNode;
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
                cacheItem = node;
            }

            node.size = size(node.left) + 1 + size(node.right);
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
                cacheItem = node;
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
                cacheItem = node;
                return node;
            }

            return max(node.right);
        }

        public Key floor(Key key) {
            Node node = floor(root, key);
            if (node == null) {
                return null;
            }

            cacheItem = node;
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

            cacheItem = node;
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
                cacheItem = node;

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
                cacheItem = node;

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
                if (cacheItem != null && cacheItem == node) {
                    invalidateCache();
                }

                return node.right;
            }

            node.left = deleteMin(node.left);
            node.size = size(node.left) + 1 + size(node.right);
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
                if (cacheItem != null && cacheItem == node) {
                    invalidateCache();
                }

                return node.left;
            }

            node.right = deleteMax(node.right);
            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public void delete(Key key) {
            if (cacheItem != null && cacheItem.key == key) {
                invalidateCache();
            }

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

        private void invalidateCache() {
            cacheItem = null;
        }

    }

    public static void main(String[] args) {
        Exercise28_SoftwareCaching softwareCaching = new Exercise28_SoftwareCaching();

        BinarySearchTree<Integer, String> binarySearchTree = softwareCaching.new BinarySearchTree<>();

        for(int i = 0; i < 10; i++) {
            binarySearchTree.put(i, "Value " + i);
        }

        //Cache on get operation
        StdOut.println("\nGet key 2:");
        binarySearchTree.get(2);
        StdOut.println("Expected: cache miss");

        StdOut.println("\nGet key 2:");
        binarySearchTree.get(2);
        StdOut.println("Expected: cache hit");

        StdOut.println("\nDelete key 4:");
        binarySearchTree.delete(4);

        StdOut.println("\nGet key 7:");
        binarySearchTree.get(7);
        StdOut.println("Expected: cache miss");

        StdOut.println("\nGet key 7:");
        binarySearchTree.get(7);
        StdOut.println("Expected: cache hit");

        StdOut.println("\nDelete key 7:");
        binarySearchTree.delete(7);

        binarySearchTree.min();
        StdOut.println("\nGet key 0:");
        binarySearchTree.get(0);
        StdOut.println("Expected: cache hit");

        StdOut.println("\nGet key 9:");
        binarySearchTree.get(9);
        StdOut.println("Expected: cache miss");

        binarySearchTree.select(3);

        StdOut.println("\nGet key 3:");
        binarySearchTree.get(3);
        StdOut.println("Expected: cache hit");

        binarySearchTree.max();
        StdOut.println("\nGet key 9:");
        binarySearchTree.get(9);
        StdOut.println("Expected: cache hit");
    }

}
