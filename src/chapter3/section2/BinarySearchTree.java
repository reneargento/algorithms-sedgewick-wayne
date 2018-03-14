package chapter3.section2;

import chapter3.section1.SymbolTable;
import edu.princeton.cs.algs4.Queue;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 09/05/17.
 */
public class BinarySearchTree<Key extends Comparable<Key>, Value> implements SymbolTable<Key, Value> {

    protected class Node {
        protected Key key;
        protected Value value;

        protected Node left;
        protected Node right;

        protected int size; //# of nodes in subtree rooted here

        public Node(Key key, Value value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
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

    public void put(Key key, Value value) {
        if (key == null) {
            return;
        }

        if (value == null) {
            delete(key);
            return;
        }

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

    // Returns the highest key in the symbol table smaller than or equal to key.
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

    // Returns the smallest key in the symbol table greater than or equal to key.
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
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Index cannot be negative and must be lower than tree size");
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

        // Returns the number of keys less than node.key in the subtree rooted at node
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
        if (root == null) {
            return;
        }

        root = deleteMin(root);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) {
            return node.right;
        }

        node.left = deleteMin(node.left);
        node.size = size(node.left) + 1 + size(node.right);
        return node;
    }

    public void deleteMax() {
        if (root == null) {
            return;
        }

        root = deleteMax(root);
    }

    private Node deleteMax(Node node) {
        if (node.right == null) {
            return node.left;
        }

        node.right = deleteMax(node.right);
        node.size = size(node.left) + 1 + size(node.right);
        return node;
    }

    public void delete(Key key) {
        if (isEmpty()) {
            return;
        }

        if (!contains(key)) {
            return;
        }

        root = delete(root, key);
    }

    private Node delete(Node node, Key key) {
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
