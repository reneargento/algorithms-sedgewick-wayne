package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 04/08/17.
 */
public class Exercise5 {

    class STint<Value> {

        private static final boolean RED = true;
        private static final boolean BLACK = false;

        public final static int EMPTY_KEY = Integer.MIN_VALUE;

        private class Node {
            int key;
            Value value;
            Node left, right;

            boolean color;
            int size;

            Node(int key, Value value, int size, boolean color) {
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

        public void put(int key, Value value) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            if (value == null) {
                delete(key);
                return;
            }

            root = put(root, key, value);
            root.color = BLACK;
        }

        private Node put(Node node, int key, Value value) {
            if (node == null) {
                return new Node(key, value, 1, RED);
            }

            if (key < node.key) {
                node.left = put(node.left, key, value);
            } else if (key > node.key) {
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

        public Value get(int key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            return get(root, key);
        }

        private Value get(Node node, int key) {
            if (node == null) {
                return null;
            }

            if (key < node.key) {
                return get(node.left, key);
            } else if (key > node.key) {
                return get(node.right, key);
            } else {
                return node.value;
            }
        }

        public boolean contains(int key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }
            return get(key) != null;
        }

        public int min() {
            if (root == null) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return min(root).key;
        }

        private Node min(Node node) {
            if (node.left == null) {
                return node;
            }

            return min(node.left);
        }

        public int max() {
            if (root == null) {
                throw new NoSuchElementException("Empty symbol table");
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
        public int floor(int key) {
            Node node = floor(root, key);
            if (node == null) {
                return EMPTY_KEY;
            }

            return node.key;
        }

        private Node floor(Node node, int key) {
            if (node == null) {
                return null;
            }

            if (key == node.key) {
                return node;
            } else if (key < node.key) {
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
        public int ceiling(int key) {
            Node node = ceiling(root, key);
            if (node == null) {
                return EMPTY_KEY;
            }

            return node.key;
        }

        private Node ceiling(Node node, int key) {
            if (node == null) {
                return null;
            }

            if (key == node.key) {
                return node;
            } else if (key > node.key) {
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

        public int select(int index) {
            if (index >= size()) {
                throw new IllegalArgumentException("Index is higher than symbol table size");
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

        public int rank(int key) {
            return rank(root, key);
        }

        private int rank(Node node, int key) {
            if (node == null) {
                return 0;
            }

            //Returns the number of keys less than node.key in the subtree rooted at node
            if (key < node.key) {
                return rank(node.left, key);
            } else if (key > node.key) {
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

        public void delete(int key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
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

        private Node delete(Node node, int key) {
            if (node == null) {
                return null;
            }

            if (key < node.key) {
                if (!isRed(node.left) && node.left != null && !isRed(node.left.left)) {
                    node = moveRedLeft(node);
                }

                node.left = delete(node.left, key);
            } else {
                if (isRed(node.left)) {
                    node = rotateRight(node);
                }

                if (key == node.key && node.right == null) {
                    return null;
                }

                if (!isRed(node.right) && node.right != null && !isRed(node.right.left)) {
                    node = moveRedRight(node);
                }

                if (key == node.key) {
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

        public int[] keys() {
            return keys(min(), max());
        }

        public int[] keys(int low, int high) {
            if (low == EMPTY_KEY)  {
                throw new IllegalArgumentException("Invalid key on the first argument");
            }
            if (high == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key on the second argument");
            }

            Queue<Integer> queue = new Queue<>();
            keys(root, queue, low, high);

            int[] keys = new int[queue.size()];
            for(int i = 0; i < keys.length; i++) {
                keys[i] = queue.dequeue();
            }

            return keys;
        }

        private void keys(Node node, Queue<Integer> queue, int low, int high) {
            if (node == null) {
                return;
            }

            int compareLow;

            if (low < node.key) {
                compareLow = -1;
            } else if (low > node.key) {
                compareLow = 1;
            } else {
                compareLow = 0;
            }

            int compareHigh;

            if (high < node.key) {
                compareHigh = -1;
            } else if (high > node.key) {
                compareHigh = 1;
            } else {
                compareHigh = 0;
            }

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

        public int size(int low, int high) {
            if (low == EMPTY_KEY)  {
                throw new IllegalArgumentException("Invalid key on the first argument");
            }
            if (high == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key on the second argument");
            }

            if (low > high) {
                return 0;
            }

            if (contains(high)) {
                return rank(high) - rank(low) + 1;
            } else {
                return rank(high) - rank(low);
            }
        }

    }

    class STdouble<Value> {

        private static final boolean RED = true;
        private static final boolean BLACK = false;

        public final static double EMPTY_KEY = Double.MIN_VALUE;

        private class Node {
            double key;
            Value value;
            Node left, right;

            boolean color;
            int size;

            Node(double key, Value value, int size, boolean color) {
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

        public void put(double key, Value value) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            if (value == null) {
                delete(key);
                return;
            }

            root = put(root, key, value);
            root.color = BLACK;
        }

        private Node put(Node node, double key, Value value) {
            if (node == null) {
                return new Node(key, value, 1, RED);
            }

            if (key < node.key) {
                node.left = put(node.left, key, value);
            } else if (key > node.key) {
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

        public Value get(double key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            return get(root, key);
        }

        private Value get(Node node, double key) {
            if (node == null) {
                return null;
            }

            if (key < node.key) {
                return get(node.left, key);
            } else if (key > node.key) {
                return get(node.right, key);
            } else {
                return node.value;
            }
        }

        public boolean contains(double key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }
            return get(key) != null;
        }

        public double min() {
            if (root == null) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return min(root).key;
        }

        private Node min(Node node) {
            if (node.left == null) {
                return node;
            }

            return min(node.left);
        }

        public double max() {
            if (root == null) {
                throw new NoSuchElementException("Empty symbol table");
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
        public double floor(double key) {
            Node node = floor(root, key);
            if (node == null) {
                return EMPTY_KEY;
            }

            return node.key;
        }

        private Node floor(Node node, double key) {
            if (node == null) {
                return null;
            }

            if (key == node.key) {
                return node;
            } else if (key < node.key) {
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
        public double ceiling(double key) {
            Node node = ceiling(root, key);
            if (node == null) {
                return EMPTY_KEY;
            }

            return node.key;
        }

        private Node ceiling(Node node, double key) {
            if (node == null) {
                return null;
            }

            if (key == node.key) {
                return node;
            } else if (key > node.key) {
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

        public double select(int index) {
            if (index >= size()) {
                throw new IllegalArgumentException("Index is higher than symbol table size");
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

        public int rank(double key) {
            return rank(root, key);
        }

        private int rank(Node node, double key) {
            if (node == null) {
                return 0;
            }

            //Returns the number of keys less than node.key in the subtree rooted at node
            if (key < node.key) {
                return rank(node.left, key);
            } else if (key > node.key) {
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

        public void delete(double key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
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

        private Node delete(Node node, double key) {
            if (node == null) {
                return null;
            }

            if (key < node.key) {
                if (!isRed(node.left) && node.left != null && !isRed(node.left.left)) {
                    node = moveRedLeft(node);
                }

                node.left = delete(node.left, key);
            } else {
                if (isRed(node.left)) {
                    node = rotateRight(node);
                }

                if (key == node.key && node.right == null) {
                    return null;
                }

                if (!isRed(node.right) && node.right != null && !isRed(node.right.left)) {
                    node = moveRedRight(node);
                }

                if (key == node.key) {
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

        public double[] keys() {
            return keys(min(), max());
        }

        public double[] keys(double low, double high) {
            if (low == EMPTY_KEY)  {
                throw new IllegalArgumentException("Invalid key on the first argument");
            }
            if (high == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key on the second argument");
            }

            Queue<Double> queue = new Queue<>();
            keys(root, queue, low, high);

            double[] keys = new double[queue.size()];
            for(int i = 0; i < keys.length; i++) {
                keys[i] = queue.dequeue();
            }

            return keys;
        }

        private void keys(Node node, Queue<Double> queue, double low, double high) {
            if (node == null) {
                return;
            }

            int compareLow;

            if (low < node.key) {
                compareLow = -1;
            } else if (low > node.key) {
                compareLow = 1;
            } else {
                compareLow = 0;
            }

            int compareHigh;

            if (high < node.key) {
                compareHigh = -1;
            } else if (high > node.key) {
                compareHigh = 1;
            } else {
                compareHigh = 0;
            }

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

        public int size(double low, double high) {
            if (low == EMPTY_KEY)  {
                throw new IllegalArgumentException("Invalid key on the first argument");
            }
            if (high == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key on the second argument");
            }

            if (low > high) {
                return 0;
            }

            if (contains(high)) {
                return rank(high) - rank(low) + 1;
            } else {
                return rank(high) - rank(low);
            }
        }

    }

    private class SparseVectorIntKeys {

        private STint<Double> stInt;

        public SparseVectorIntKeys() {
            stInt = new STint<>();
        }

        public int size() {
            return stInt.size();
        }

        public void put(int key, double value) {
            stInt.put(key, value);
        }

        public double get(int key) {
            if (!stInt.contains(key)) {
                return 0;
            } else {
                return stInt.get(key);
            }
        }

        public double dot(double[] that) {
            double sum = 0.0;

            for(int key : stInt.keys()) {
                sum += that[key] * this.get(key);
            }

            return sum;
        }

    }

    private class SparseVectorDoubleKeys {

        private STdouble<Double> stDouble;

        public SparseVectorDoubleKeys() {
            stDouble = new STdouble<>();
        }

        public int size() {
            return stDouble.size();
        }

        public void put(double key, double value) {
            stDouble.put(key, value);
        }

        public double get(double key) {
            if (!stDouble.contains(key)) {
                return 0;
            } else {
                return stDouble.get(key);
            }
        }

        public double dot(double[] that) {
            double sum = 0.0;

            for(double key : stDouble.keys()) {
                sum += that[(int) key] * this.get(key);
            }

            return sum;
        }

    }

    public static void main(String[] args) {
        Exercise5 exercise5 = new Exercise5();
        exercise5.testSTint();
        exercise5.testSTdouble();
    }

    private void testSTint() {
        //Matrix
        // 0  .90    0    0    0
        // 0    0  .36  .36  .18
        // 0    0    0  .90    0
        // .90  0    0    0    0
        // .47  0  .47    0    0

        //Vector
        // .05
        // .04
        // .36
        // .37
        // .19

        StdOut.println("STint tests");

        SparseVectorIntKeys matrixColumn0 = new SparseVectorIntKeys();
        matrixColumn0.put(1, 0.90);

        SparseVectorIntKeys matrixColumn1 = new SparseVectorIntKeys();
        matrixColumn1.put(2, 0.36);
        matrixColumn1.put(3, 0.36);
        matrixColumn1.put(4, 0.18);

        SparseVectorIntKeys matrixColumn2 = new SparseVectorIntKeys();
        matrixColumn2.put(3, 0.90);

        SparseVectorIntKeys matrixColumn3 = new SparseVectorIntKeys();
        matrixColumn3.put(0, 0.90);

        SparseVectorIntKeys matrixColumn4 = new SparseVectorIntKeys();
        matrixColumn4.put(0, 0.47);
        matrixColumn4.put(2, 0.47);

        SparseVectorIntKeys[] matrix = {matrixColumn0, matrixColumn1, matrixColumn2, matrixColumn3, matrixColumn4};

        double[] vector = {.05, .04, .36, .37, .19};

        double[] resultVector = new double[vector.length];

        for(int matrixColumn = 0; matrixColumn < matrix.length; matrixColumn++) {
            resultVector[matrixColumn] = matrix[matrixColumn].dot(vector);
        }

        StdOut.println("\nResult");
        for(double value : resultVector) {
            StdOut.printf("%.3f\n", value);
        }

        StdOut.println("\nExpected:");
        StdOut.println("0.036\n" +
                "0.297\n" +
                "0.333\n" +
                "0.045\n" +
                "0.193");
    }

    private void testSTdouble() {
        //Matrix
        // 0  .90    0    0    0
        // 0    0  .36  .36  .18
        // 0    0    0  .90    0
        // .90  0    0    0    0
        // .47  0  .47    0    0

        //Vector
        // .05
        // .04
        // .36
        // .37
        // .19

        StdOut.println("\nSTdouble tests");

        SparseVectorDoubleKeys matrixColumn0 = new SparseVectorDoubleKeys();
        matrixColumn0.put(1.0, 0.90);

        SparseVectorDoubleKeys matrixColumn1 = new SparseVectorDoubleKeys();
        matrixColumn1.put(2.0, 0.36);
        matrixColumn1.put(3.0, 0.36);
        matrixColumn1.put(4.0, 0.18);

        SparseVectorDoubleKeys matrixColumn2 = new SparseVectorDoubleKeys();
        matrixColumn2.put(3.0, 0.90);

        SparseVectorDoubleKeys matrixColumn3 = new SparseVectorDoubleKeys();
        matrixColumn3.put(0.0, 0.90);

        SparseVectorDoubleKeys matrixColumn4 = new SparseVectorDoubleKeys();
        matrixColumn4.put(0.0, 0.47);
        matrixColumn4.put(2.0, 0.47);

        SparseVectorDoubleKeys[] matrix = {matrixColumn0, matrixColumn1, matrixColumn2, matrixColumn3, matrixColumn4};

        double[] vector = {.05, .04, .36, .37, .19};

        double[] resultVector = new double[vector.length];

        for(int matrixColumn = 0; matrixColumn < matrix.length; matrixColumn++) {
            resultVector[matrixColumn] = matrix[matrixColumn].dot(vector);
        }

        StdOut.println("\nResult");
        for(double value : resultVector) {
            StdOut.printf("%.3f\n", value);
        }

        StdOut.println("\nExpected:");
        StdOut.println("0.036\n" +
                "0.297\n" +
                "0.333\n" +
                "0.045\n" +
                "0.193");
    }

}