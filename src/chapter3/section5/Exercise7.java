package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 05/08/17.
 */
public class Exercise7 {

    private class SETint {

        private static final boolean RED = true;
        private static final boolean BLACK = false;

        public final static int EMPTY_KEY = Integer.MIN_VALUE;

        private class Node {
            int key;
            Node left, right;

            boolean color;
            int size;

            Node(int key, int size, boolean color) {
                this.key = key;

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

        public void add(int key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            root = add(root, key);
            root.color = BLACK;
        }

        private Node add(Node node, int key) {
            if (node == null) {
                return new Node(key, 1, RED);
            }

            if (key < node.key) {
                node.left = add(node.left, key);
            } else if (key > node.key) {
                node.right = add(node.right, key);
            } else {
                node.key = key;
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

        public boolean contains(int key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            Node currentNode = root;
            while (currentNode != null) {
                if (key < currentNode.key) {
                    currentNode = currentNode.left;
                } else if (key > currentNode.key) {
                    currentNode = currentNode.right;
                } else {
                    return true;
                }
            }

            return false;
        }

        public int min() {
            if (root == null) {
                throw new NoSuchElementException("Empty set");
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
                throw new NoSuchElementException("Empty set");
            }

            return max(root).key;
        }

        private Node max(Node node) {
            if (node.right == null) {
                return node;
            }

            return max(node.right);
        }

        //Returns the highest key in the set smaller than or equal to key.
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

        //Returns the smallest key in the set greater than or equal to key.
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
                throw new IllegalArgumentException("Index is higher than set size");
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

    private class SETdouble {

        private static final boolean RED = true;
        private static final boolean BLACK = false;

        public final static double EMPTY_KEY = Double.MIN_VALUE;

        private class Node {
            double key;
            Node left, right;

            boolean color;
            int size;

            Node(double key, int size, boolean color) {
                this.key = key;

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

        public void add(double key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            root = add(root, key);
            root.color = BLACK;
        }

        private Node add(Node node, double key) {
            if (node == null) {
                return new Node(key, 1, RED);
            }

            if (key < node.key) {
                node.left = add(node.left, key);
            } else if (key > node.key) {
                node.right = add(node.right, key);
            } else {
                node.key = key;
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

        public boolean contains(double key) {
            if (key == EMPTY_KEY) {
                throw new IllegalArgumentException("Invalid key");
            }

            Node currentNode = root;
            while (currentNode != null) {
                if (key < currentNode.key) {
                    currentNode = currentNode.left;
                } else if (key > currentNode.key) {
                    currentNode = currentNode.right;
                } else {
                    return true;
                }
            }

            return false;
        }

        public double min() {
            if (root == null) {
                throw new NoSuchElementException("Empty set");
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
                throw new NoSuchElementException("Empty set");
            }

            return max(root).key;
        }

        private Node max(Node node) {
            if (node.right == null) {
                return node;
            }

            return max(node.right);
        }

        //Returns the highest key in the set smaller than or equal to key.
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

        //Returns the smallest key in the set greater than or equal to key.
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
                throw new IllegalArgumentException("Index is higher than set size");
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

    public static void main(String[] args) {
        Exercise7 exercise7 = new Exercise7();
        exercise7.testSETint();
        exercise7.testSETdouble();
    }

    private void testSETint() {
        StdOut.println("SETint test");

        SETint setInt = new SETint();

        setInt.add(5);
        setInt.add(1);
        setInt.add(9);
        setInt.add(2);
        setInt.add(0);
        setInt.add(99);
        setInt.add(-1);
        setInt.add(-2);
        setInt.add(3);
        setInt.add(-5);

        StdOut.println("\nKeys() test");

        for(Integer key : setInt.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: -5 -2 -1 0 1 2 3 5 9 99");

        //Test min()
        StdOut.println("\nMin key: " + setInt.min() + " Expected: -5");

        //Test max()
        StdOut.println("Max key: " + setInt.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + setInt.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + setInt.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + setInt.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + setInt.ceiling(15) + " Expected: 99");

        //Test select()
        StdOut.println("Select key of rank 4: " + setInt.select(4) + " Expected: 1");

        //Test rank()
        StdOut.println("Rank of key 9: " + setInt.rank(9) + " Expected: 8");
        StdOut.println("Rank of key 10: " + setInt.rank(10) + " Expected: 9");

        //Test delete()
        StdOut.println("\nDelete key 2");
        setInt.delete(2);

        for(Integer key : setInt.keys()) {
            StdOut.print(key + " ");
        }

        //Test deleteMin()
        StdOut.println("\n\nDelete min (key -5)");
        setInt.deleteMin();

        for(Integer key : setInt.keys()) {
            StdOut.print(key + " ");
        }

        //Test deleteMax()
        StdOut.println("\n\nDelete max (key 99)");
        setInt.deleteMax();

        for(Integer key : setInt.keys()) {
            StdOut.print(key + " ");
        }

        //Test keys() with range
        StdOut.println("\n\nKeys in range [2, 10]");
        for(Integer key : setInt.keys(2, 10)) {
            StdOut.print(key + " ");
        }

        StdOut.println("\n\nKeys in range [-4, -1]");
        for(Integer key : setInt.keys(-4, -1)) {
            StdOut.print(key + " ");
        }

        //Delete all
        StdOut.println("\n\nDelete all");
        while (setInt.size() > 0) {
            for(Integer key : setInt.keys()) {
                StdOut.print(key + " ");
            }
            //setInt.delete(setInt.select(0));
            setInt.delete(setInt.select(setInt.size() - 1));
            StdOut.println();
        }
    }

    private void testSETdouble() {
        StdOut.println("\nSETdouble test");

        SETdouble setDouble = new SETdouble();

        setDouble.add(5.0);
        setDouble.add(1.0);
        setDouble.add(9.5);
        setDouble.add(2.1);
        setDouble.add(0);
        setDouble.add(99.999);
        setDouble.add(-1.05);
        setDouble.add(-2.20);
        setDouble.add(3.0);
        setDouble.add(-5.9);

        StdOut.println("\nKeys() test");

        for(Double key : setDouble.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: -5.9 -2.2 -1.05 0.0 1.0 2.1 3.0 5.0 9.5 99.999");

        //Test min()
        StdOut.println("\nMin key: " + setDouble.min() + " Expected: -5.9");

        //Test max()
        StdOut.println("Max key: " + setDouble.max() + " Expected: 99.999");

        //Test floor()
        StdOut.println("Floor of 5.0: " + setDouble.floor(5.0) + " Expected: 5.0");
        StdOut.println("Floor of 15.0: " + setDouble.floor(15.0) + " Expected: 9.5");

        //Test ceiling()
        StdOut.println("Ceiling of 5.0: " + setDouble.ceiling(5.0) + " Expected: 5.0");
        StdOut.println("Ceiling of 15.0: " + setDouble.ceiling(15.0) + " Expected: 99.999");

        //Test select()
        StdOut.println("Select key of rank 4: " + setDouble.select(4) + " Expected: 1.0");

        //Test rank()
        StdOut.println("Rank of key 9: " + setDouble.rank(9.5) + " Expected: 8");
        StdOut.println("Rank of key 10: " + setDouble.rank(10.0) + " Expected: 9");

        //Test delete()
        StdOut.println("\nDelete key 2.1");
        setDouble.delete(2.1);

        for(Double key : setDouble.keys()) {
            StdOut.print(key + " ");
        }

        //Test deleteMin()
        StdOut.println("\n\nDelete min (key -5.9)");
        setDouble.deleteMin();

        for(Double key : setDouble.keys()) {
            StdOut.print(key + " ");
        }

        //Test deleteMax()
        StdOut.println("\n\nDelete max (key 99.999)");
        setDouble.deleteMax();

        for(Double key : setDouble.keys()) {
            StdOut.print(key + " ");
        }

        //Test keys() with range
        StdOut.println("\n\nKeys in range [2.0, 10.0]");
        for(Double key : setDouble.keys(2.0, 10.0)) {
            StdOut.print(key + " ");
        }

        StdOut.println("\n\nKeys in range [-4.0, -1.0]");
        for(Double key : setDouble.keys(-4.0, -1.0)) {
            StdOut.print(key + " ");
        }

        //Delete all
        StdOut.println("\n\nDelete all");
        while (setDouble.size() > 0) {
            for(Double key : setDouble.keys()) {
                StdOut.print(key + " ");
            }
            //setDouble.delete(setDouble.select(0));
            setDouble.delete(setDouble.select(setDouble.size() - 1));
            StdOut.println();
        }
    }

}
