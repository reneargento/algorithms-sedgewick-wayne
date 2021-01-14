package chapter3.section3;

import edu.princeton.cs.algs4.Queue;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 18/06/17.
 */
public class RedBlackBST<Key extends Comparable<Key>, Value> {

    protected static final boolean RED = true;
    protected static final boolean BLACK = false;

    protected class Node {
        public Key key;
        public Value value;
        public Node left, right;

        boolean color;
        int size;

        Node(Key key, Value value, int size, boolean color) {
            this.key = key;
            this.value = value;

            this.size = size;
            this.color = color;
        }
    }

    protected Node root;

    public int size() {
        return size(root);
    }

    protected int size(Node node) {
        if (node == null) {
            return 0;
        }

        return node.size;
    }

    public boolean isEmpty() {
        return size(root) == 0;
    }

    protected boolean isRed(Node node) {
        if (node == null) {
            return false;
        }

        return node.color == RED;
    }

    protected Node rotateLeft(Node node) {
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

    protected Node rotateRight(Node node) {
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

    protected void flipColors(Node node) {
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

        if (compare < 0) {
            node.left = put(node.left, key, value);
        } else if (compare > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
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

    protected Node min(Node node) {
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
            return null;
        }

        if (!isRed(node.right) && !isRed(node.right.left)) {
            node = moveRedRight(node);
        }

        node.right = deleteMax(node.right);
        return balance(node);
    }

    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if (isEmpty() || !contains(key)) {
            return;
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

    protected Node moveRedLeft(Node node) {
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

    protected Node moveRedRight(Node node) {
        //Assuming that node is red and both node.right and node.right.left are black,
        // make node.right or one of its children red
        flipColors(node);

        if (node.left != null && isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }

        return node;
    }

    protected Node balance(Node node) {
        if (node == null) {
            return null;
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
            return rank(high) - rank(low) + 1;
        } else {
            return rank(high) - rank(low);
        }
    }

}
