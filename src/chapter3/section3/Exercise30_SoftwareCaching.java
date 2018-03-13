package chapter3.section3;

import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 29/06/17.
 */
public class Exercise30_SoftwareCaching {

    private class RedBlackBSTWithCache<Key extends Comparable<Key>, Value> extends RedBlackBST<Key, Value>{

        private Node cacheItem;

        public void put(Key key, Value value) {
            if (key == null) {
                return;
            }

            if (value == null) {
                delete(key);
                return;
            }

            if (cacheItem != null && cacheItem.key.compareTo(key) == 0) {
                cacheItem.value = value;
                StdOut.println("Cache hit");
                return;
            }
            StdOut.println("Cache miss");

            root = put(root, key, value);
            root.color = BLACK;
        }

        private Node put(Node node, Key key, Value value) {
            if (node == null) {
                Node newNode = new Node(key, value, 1, RED);
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

            if (isRed(node.right) && !isRed(node.left)) {
                node = rotateLeft(node);
            }
            if (isRed(node.left) && isRed(node.left.left)) {
                node = rotateRight(node);
            }
            if (isRed(node.left) && isRed(node.right)) {
                flipColors(node);
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public Value get(Key key) {
            if (key == null) {
                return null;
            }

            if (cacheItem != null && cacheItem.key.compareTo(key) == 0) {
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

        public Key min() {
            if (root == null) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            return min(root).key;
        }

        protected Node min(Node node) {
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

        //Returns the highest key in the symbol table smaller than or equal to key.
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

        //Returns the smallest key in the symbol table greater than or equal to key.
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
            if (isEmpty()) {
                return;
            }

            if (!isRed(root.left) && !isRed(root.right)) {
                root.color = RED;
            }

            root = deleteMin(root);

            if (!isEmpty()) {
                root.color = BLACK;
            }
        }

        protected Node deleteMin(Node node) {
            if (node.left == null) {
                if (cacheItem != null && cacheItem == node) {
                    invalidateCache();
                }

                return null;
            }

            if (!isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }

            node.left = deleteMin(node.left);
            return balance(node);
        }

        public void deleteMax() {
            if (isEmpty()) {
                return;
            }

            if (!isRed(root.left) && !isRed(root.right)) {
                root.color = RED;
            }

            root = deleteMax(root);

            if (!isEmpty()) {
                root.color = BLACK;
            }
        }

        private Node deleteMax(Node node) {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }

            if (node.right == null) {
                if (cacheItem != null && cacheItem == node) {
                    invalidateCache();
                }

                return null;
            }

            if (!isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }

            node.right = deleteMax(node.right);
            return balance(node);
        }

        public void delete(Key key) {
            if (isEmpty()) {
                return;
            }

            if (!contains(key)) {
                return;
            }

            if (cacheItem != null && cacheItem.key.compareTo(key) == 0) {
                invalidateCache();
            }

            if (!isRed(root.left) && !isRed(root.right)) {
                root.color = RED;
            }

            root = delete(root, key);

            if (!isEmpty()) {
                root.color = BLACK;
            }
        }

        private Node delete(Node node, Key key) {
            if (node == null) {
                return null;
            }

            if (key.compareTo(node.key) < 0) {
                if (!isRed(node.left) && node.left != null && !isRed(node.left.left)) {
                    node = moveRedLeft(node);
                }

                node.left = delete(node.left, key);
            } else {
                if (isRed(node.left)) {
                    node = rotateRight(node);
                }

                if (key.compareTo(node.key) == 0 && node.right == null) {
                    return null;
                }

                if (!isRed(node.right) && node.right != null && !isRed(node.right.left)) {
                    node = moveRedRight(node);
                }

                if (key.compareTo(node.key) == 0) {
                    Node aux = min(node.right);
                    node.key = aux.key;
                    node.value = aux.value;
                    node.right = deleteMin(node.right);
                } else {
                    node.right = delete(node.right, key);
                }
            }

            return balance(node);
        }

        private void invalidateCache() {
            cacheItem = null;
        }

    }

    public static void main(String[] args) {
        Exercise30_SoftwareCaching softwareCaching = new Exercise30_SoftwareCaching();
        RedBlackBSTWithCache<Integer, String> redBlackBSTWithCache = softwareCaching.new RedBlackBSTWithCache<>();

        for(int i = 0; i < 10; i++) {
            redBlackBSTWithCache.put(i, "Value " + i);
        }

        //Cache on get operation
        StdOut.println("\nGet key 2:");
        redBlackBSTWithCache.get(2);
        StdOut.println("Expected: cache miss");

        StdOut.println("\nGet key 2:");
        redBlackBSTWithCache.get(2);
        StdOut.println("Expected: cache hit");

        StdOut.println("\nDelete key 4:");
        redBlackBSTWithCache.delete(4);

        StdOut.println("\nGet key 7:");
        redBlackBSTWithCache.get(7);
        StdOut.println("Expected: cache miss");

        StdOut.println("\nGet key 7:");
        redBlackBSTWithCache.get(7);
        StdOut.println("Expected: cache hit");

        StdOut.println("\nDelete key 7:");
        redBlackBSTWithCache.delete(7);

        redBlackBSTWithCache.min();
        StdOut.println("\nGet key 0:");
        redBlackBSTWithCache.get(0);
        StdOut.println("Expected: cache hit");

        StdOut.println("\nGet key 9:");
        redBlackBSTWithCache.get(9);
        StdOut.println("Expected: cache miss");

        redBlackBSTWithCache.select(3);

        StdOut.println("\nGet key 3:");
        redBlackBSTWithCache.get(3);
        StdOut.println("Expected: cache hit");

        redBlackBSTWithCache.max();
        StdOut.println("\nGet key 9:");
        redBlackBSTWithCache.get(9);
        StdOut.println("Expected: cache hit");
    }

}
