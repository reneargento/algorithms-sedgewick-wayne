package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 05/08/17.
 */
public class Exercise10 {

    /**
     * This tree has the following property:
     *
     * Let x be a node in the red-black binary search tree.
     * If y is a node in the left subtree of x, then y:key <= x:key.
     * If y is a node in the right subtree of x, then y:key >= x:key.
     */
    private class RedBlackBSTDuplicateKeys<Key extends Comparable<Key>, Value> {

        private static final boolean RED = true;
        private static final boolean BLACK = false;

        private class Node {
            Key key;
            Value value;
            Node left, right;

            boolean color;
            int size;

            Node(Key key, Value value, int size, boolean color) {
                this.key = key;
                this.value = value;

                this.size = size;
                this.color = color;
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

        public boolean isEmpty() {
            return size(root) == 0;
        }

        private boolean isRed(Node node) {
            if (node == null) {
                return false;
            }

            return node.color == RED;
        }

        private Node rotateLeft(Node node) {
            if (node == null || node.right == null) {
                return node;
            }

            Node newRoot = node.right;

            node.right = newRoot.left;
            newRoot.left = node;

            newRoot.color = node.color;
            node.color = RED;

            newRoot.size = node.size;
            node.size = size(node.left) + 1 + size(node.right);

            return newRoot;
        }

        private Node rotateRight(Node node) {
            if (node == null || node.left == null) {
                return node;
            }

            Node newRoot = node.left;

            node.left = newRoot.right;
            newRoot.right = node;

            newRoot.color = node.color;
            node.color = RED;

            newRoot.size = node.size;
            node.size = size(node.left) + 1 + size(node.right);

            return newRoot;
        }

        private void flipColors(Node node) {
            if (node == null || node.left == null || node.right == null) {
                return;
            }

            //The root must have opposite color of its two children
            if ((isRed(node) && !isRed(node.left) && !isRed(node.right))
                    || (!isRed(node) && isRed(node.left) && isRed(node.right))) {
                node.color = !node.color;
                node.left.color = !node.left.color;
                node.right.color = !node.right.color;
            }
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            root = put(root, key, value);
            root.color = BLACK;
        }

        private Node put(Node node, Key key, Value value) {
            if (node == null) {
                return new Node(key, value, 1, RED);
            }

            int compare = key.compareTo(node.key);

            //If it is a duplicate key, put it on the left subtree
            //It is important to notice that since we have rotations,
            // the duplicate keys may be on the left or right subtrees later
            if (compare <= 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
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

        public boolean contains(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }
            return get(key) != null;
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

        //Returns the highest key in the symbol table smaller than or equal to key.
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

        //Returns the smallest key in the symbol table greater than or equal to key.
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

        public int rankFirst(Key key) {
            return rankFirst(root, key);
        }

        private int rankFirst(Node node, Key key) {
            if (node == null) {
                return 0;
            }

            //Returns the number of keys less than node.key in the subtree rooted at node
            int compare = key.compareTo(node.key);
            if (compare < 0) {
                return rankFirst(node.left, key);
            } else if (compare > 0) {
                return size(node.left) + 1 + rankFirst(node.right, key);
            } else {
                boolean hasDuplicateOnLeftSubtree = false;

                if (node.left != null && max(node.left).key.compareTo(key) == 0) {
                    hasDuplicateOnLeftSubtree = true;
                }

                if (hasDuplicateOnLeftSubtree) {
                    return rankFirst(node.left, key);
                } else {
                    return size(node.left);
                }
            }
        }

        public int rankLast(Key key) {
            return rankLast(root, key);
        }

        private int rankLast(Node node, Key key) {
            if (node == null) {
                return 0;
            }

            //Returns the number of keys less than node.key in the subtree rooted at node
            int compare = key.compareTo(node.key);
            if (compare < 0) {
                return rankLast(node.left, key);
            } else if (compare > 0) {
                return size(node.left) + 1 + rankLast(node.right, key);
            } else {
                boolean hasDuplicateOnRightSubtree = false;

                if (node.right != null && min(node.right).key.compareTo(key) == 0) {
                    hasDuplicateOnRightSubtree = true;
                }

                if (hasDuplicateOnRightSubtree) {
                    return size(node.left) + 1 + rankLast(node.right, key);
                } else {
                    return size(node.left);
                }
            }
        }

        //In the case of duplicates, return the rank of the rightmost key
        public int rank(Key key) {
            return rankLast(key);
        }

        //O(n lg n) since we are removing all duplicate min keys
        public void deleteMin() {
            if (isEmpty()) {
                return;
            }

            Key minKey = min();

            while (contains(minKey)) {
                if (!isRed(root.left) && !isRed(root.right)) {
                    root.color = RED;
                }

                root = deleteMin(root);

                if (!isEmpty()) {
                    root.color = BLACK;
                }
            }
        }

        private Node deleteMin(Node node) {
            if (node.left == null) {
                return null;
            }

            if (!isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }

            node.left = deleteMin(node.left);
            return balance(node);
        }

        //O(n lg n) since we are removing all duplicate max keys
        public void deleteMax() {
            if (isEmpty()) {
                return;
            }

            Key maxKey = max();

            while (contains(maxKey)) {
                if (!isRed(root.left) && !isRed(root.right)) {
                    root.color = RED;
                }

                root = deleteMax(root);

                if (!isEmpty()) {
                    root.color = BLACK;
                }
            }
        }

        private Node deleteMax(Node node) {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }

            if (node.right == null) {
                return null;
            }

            if (!isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }

            node.right = deleteMax(node.right);
            return balance(node);
        }

        //O(n lg n) since we are removing all duplicate keys
        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (isEmpty() || !contains(key)) {
                return;
            }

            while (contains(key)) {
                if (!isRed(root.left) && !isRed(root.right)) {
                    root.color = RED;
                }

                root = delete(root, key);

                if (!isEmpty()) {
                    root.color = BLACK;
                }
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

        private Node moveRedLeft(Node node) {
            //Assuming that node is red and both node.left and node.left.left are black,
            // make node.left or one of its children red
            flipColors(node);

            if (node.right != null && isRed(node.right.left)) {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
                flipColors(node);
            }

            return node;
        }

        private Node moveRedRight(Node node) {
            //Assuming that node is red and both node.right and node.right.left are black,
            // make node.right or one of its children red
            flipColors(node);

            if (node.left != null && isRed(node.left.left)) {
                node = rotateRight(node);
                flipColors(node);
            }

            return node;
        }

        private Node balance(Node node) {
            if (node == null) {
                return null;
            }

            if (isRed(node.right)) {
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

        public Iterable<Key> keys() {
            return keys(min(), max());
        }

        public Iterable<Key> keys(Key low, Key high) {
            if (low == null)  {
                throw new IllegalArgumentException("First argument to keys() cannot be null");
            }
            if (high == null) {
                throw new IllegalArgumentException("Second argument to keys() cannot be null");
            }

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

            if (compareLow <= 0) {
                keys(node.left, queue, low, high);
            }

            if (compareLow <= 0 && compareHigh >= 0) {
                queue.enqueue(node.key);
            }

            if (compareHigh >= 0) {
                keys(node.right, queue, low, high);
            }
        }

        public int size(Key low, Key high) {
            if (low == null)  {
                throw new IllegalArgumentException("First argument to size() cannot be null");
            }
            if (high == null) {
                throw new IllegalArgumentException("Second argument to size() cannot be null");
            }

            if (low.compareTo(high) > 0) {
                return 0;
            }

            if (contains(high)) {
                return rankLast(high) - rankFirst(low) + 1;
            } else {
                return rankLast(high) - rankFirst(low);
            }
        }
    }

    public static void main(String[] args) {
        Exercise10 exercise10 = new Exercise10();
        RedBlackBSTDuplicateKeys<Integer, Integer> redBlackBSTDuplicateKeys = exercise10.new RedBlackBSTDuplicateKeys<>();

        //Test put()
        redBlackBSTDuplicateKeys.put(0, 0);
        redBlackBSTDuplicateKeys.put(0, 1);
        redBlackBSTDuplicateKeys.put(0, 2);
        redBlackBSTDuplicateKeys.put(0, 3);

        redBlackBSTDuplicateKeys.put(5, 7);
        redBlackBSTDuplicateKeys.put(5, 8);

        redBlackBSTDuplicateKeys.put(8, 9);
        redBlackBSTDuplicateKeys.put(8, 10);

        redBlackBSTDuplicateKeys.put(20, 11);
        redBlackBSTDuplicateKeys.put(20, 12);
        redBlackBSTDuplicateKeys.put(20, 13);
        redBlackBSTDuplicateKeys.put(20, 14);

        redBlackBSTDuplicateKeys.put(21, 15);
        redBlackBSTDuplicateKeys.put(22, 16);
        redBlackBSTDuplicateKeys.put(23, 17);
        redBlackBSTDuplicateKeys.put(24, 18);

        StdOut.println("Keys() test");
        for(Integer key : redBlackBSTDuplicateKeys.keys()) {
            StdOut.println("Key " + key + ": " + redBlackBSTDuplicateKeys.get(key));
        }
        StdOut.println("\nExpected:");
        StdOut.println("Key 0: {0, 1, 2 or 3}");
        StdOut.println("Key 0: {0, 1, 2 or 3}");
        StdOut.println("Key 0: {0, 1, 2 or 3}");
        StdOut.println("Key 0: {0, 1, 2 or 3}");
        StdOut.println("Key 5: {7 or 8}");
        StdOut.println("Key 5: {7 or 8}");
        StdOut.println("Key 8: {9 or 10}");
        StdOut.println("Key 8: {9 or 10}");
        StdOut.println("Key 20: {11, 12, 13 or 14}");
        StdOut.println("Key 20: {11, 12, 13 or 14}");
        StdOut.println("Key 20: {11, 12, 13 or 14}");
        StdOut.println("Key 20: {11, 12, 13 or 14}");
        StdOut.println("Key 21: 15");
        StdOut.println("Key 22: 16");
        StdOut.println("Key 23: 17");
        StdOut.println("Key 24: 18");

        //Test size()
        StdOut.println("Keys size: " + redBlackBSTDuplicateKeys.size() + " Expected: 16");

        //Test size() with range
        StdOut.println("Keys size [0, 20]: " + redBlackBSTDuplicateKeys.size(0, 20) + " Expected: 12");

        //Test contains()
        StdOut.println("\nContains 8: " + redBlackBSTDuplicateKeys.contains(8) + " Expected: true");
        StdOut.println("Contains 9: " + redBlackBSTDuplicateKeys.contains(9) + " Expected: false");

        //Test min()
        StdOut.println("\nMin key: " + redBlackBSTDuplicateKeys.min() + " Expected: 0");

        //Test max()
        StdOut.println("Max key: " + redBlackBSTDuplicateKeys.max() + " Expected: 24");

        //Test floor()
        StdOut.println("Floor of 5: " + redBlackBSTDuplicateKeys.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + redBlackBSTDuplicateKeys.floor(15) + " Expected: 8");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + redBlackBSTDuplicateKeys.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + redBlackBSTDuplicateKeys.ceiling(15) + " Expected: 20");

        //Test select()
        StdOut.println("Select key of rank 3: " + redBlackBSTDuplicateKeys.select(3) + " Expected: 0");
        StdOut.println("Select key of rank 4: " + redBlackBSTDuplicateKeys.select(4) + " Expected: 5");

        //Test rank()
        //Note that the expected rank of key 8 is 7 and not 6, because we are assuming that rank returns the index
        // of the rightmost key when there are duplicates
        StdOut.println("Rank of key 8: " + redBlackBSTDuplicateKeys.rank(8) + " Expected: 7");
        StdOut.println("Rank of key 9: " + redBlackBSTDuplicateKeys.rank(9) + " Expected: 8");

        //Test delete()
        StdOut.println("\nDelete key 20");
        redBlackBSTDuplicateKeys.delete(20);

        for(Integer key : redBlackBSTDuplicateKeys.keys()) {
            StdOut.println("Key " + key + ": " + redBlackBSTDuplicateKeys.get(key));
        }
        StdOut.println("Keys size: " + redBlackBSTDuplicateKeys.size() + " Expected: 12");

        StdOut.println("\nDelete key 5");
        redBlackBSTDuplicateKeys.delete(5);

        for(Integer key : redBlackBSTDuplicateKeys.keys()) {
            StdOut.println("Key " + key + ": " + redBlackBSTDuplicateKeys.get(key));
        }
        StdOut.println("Keys size: " + redBlackBSTDuplicateKeys.size() + " Expected: 10");

        //Test deleteMin()
        StdOut.println("\nDelete min (key 0)");
        redBlackBSTDuplicateKeys.deleteMin();

        for(Integer key : redBlackBSTDuplicateKeys.keys()) {
            StdOut.println("Key " + key + ": " + redBlackBSTDuplicateKeys.get(key));
        }
        StdOut.println("Keys size: " + redBlackBSTDuplicateKeys.size() + " Expected: 6");

        //Test deleteMax()
        StdOut.println("\nDelete max (key 24)");
        redBlackBSTDuplicateKeys.deleteMax();

        for(Integer key : redBlackBSTDuplicateKeys.keys()) {
            StdOut.println("Key " + key + ": " + redBlackBSTDuplicateKeys.get(key));
        }
        StdOut.println("Keys size: " + redBlackBSTDuplicateKeys.size() + " Expected: 5");

        //Test keys() with range
        StdOut.println("\nKeys in range [2, 10]");
        for(Integer key : redBlackBSTDuplicateKeys.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + redBlackBSTDuplicateKeys.get(key));
        }

        StdOut.println("\nKeys in range [20, 22]");
        for(Integer key : redBlackBSTDuplicateKeys.keys(20, 22)) {
            StdOut.println("Key " + key + ": " + redBlackBSTDuplicateKeys.get(key));
        }

        //Delete all
        StdOut.println("\nDelete all");
        while (redBlackBSTDuplicateKeys.size() > 0) {
            for(Integer key : redBlackBSTDuplicateKeys.keys()) {
                StdOut.println("Key " + key + ": " + redBlackBSTDuplicateKeys.get(key));
            }
            //redBlackBSTDuplicateKeys.delete(redBlackBSTDuplicateKeys.select(0));
            redBlackBSTDuplicateKeys.delete(redBlackBSTDuplicateKeys.select(redBlackBSTDuplicateKeys.size() - 1));
            StdOut.println();
        }
    }

}
