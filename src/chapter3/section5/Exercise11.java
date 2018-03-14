package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 06/08/17.
 */
public class Exercise11 {

    /**
     * This tree has the following property:
     *
     * Let x be a node in the red-black binary search tree.
     * If y is a node in the left subtree of x, then y:key <= x:key.
     * If y is a node in the right subtree of x, then y:key >= x:key.
     */
    private class MultiSET<Key extends Comparable<Key>> {

        private static final boolean RED = true;
        private static final boolean BLACK = false;

        private class Node {
            Key key;
            Node left, right;

            boolean color;
            int size;

            Node(Key key, int size, boolean color) {
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

        public void add(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            root = add(root, key);
            root.color = BLACK;
        }

        private Node add(Node node, Key key) {
            if (node == null) {
                return new Node(key, 1, RED);
            }

            int compare = key.compareTo(node.key);

            //If it is a duplicate key, put it on the left subtree
            //It is important to notice that since we have rotations,
            // the duplicate keys may be on the left or right subtrees later
            if (compare <= 0) {
                node.left = add(node.left, key);
            } else if (compare > 0) {
                node.right = add(node.right, key);
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

        public boolean contains(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }

            Node currentNode = root;

            while (currentNode != null) {
                int compare = key.compareTo(currentNode.key);

                if (compare < 0) {
                    currentNode = currentNode.left;
                } else if (compare > 0) {
                    currentNode = currentNode.right;
                } else {
                    return true;
                }
            }

            return false;
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
        Exercise11 exercise11 = new Exercise11();
        MultiSET<Integer> multiset = exercise11.new MultiSET<>();

        //Test add()
        multiset.add(0);
        multiset.add(0);
        multiset.add(0);
        multiset.add(0);

        multiset.add(5);
        multiset.add(5);

        multiset.add(8);
        multiset.add(8);

        multiset.add(20);
        multiset.add(20);
        multiset.add(20);
        multiset.add(20);

        multiset.add(21);
        multiset.add(22);
        multiset.add(23);
        multiset.add(24);

        StdOut.println("Keys() test");
        for(Integer key : multiset.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: 0 0 0 0 5 5 8 8 20 20 20 20 21 22 23 24");

        //Test size()
        StdOut.println("Keys size: " + multiset.size() + " Expected: 16");

        //Test size() with range
        StdOut.println("Keys size [0, 20]: " + multiset.size(0, 20) + " Expected: 12");

        //Test contains()
        StdOut.println("\nContains 8: " + multiset.contains(8) + " Expected: true");
        StdOut.println("Contains 9: " + multiset.contains(9) + " Expected: false");

        //Test min()
        StdOut.println("\nMin key: " + multiset.min() + " Expected: 0");

        //Test max()
        StdOut.println("Max key: " + multiset.max() + " Expected: 24");

        //Test floor()
        StdOut.println("Floor of 5: " + multiset.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + multiset.floor(15) + " Expected: 8");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + multiset.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + multiset.ceiling(15) + " Expected: 20");

        //Test select()
        StdOut.println("Select key of rank 3: " + multiset.select(3) + " Expected: 0");
        StdOut.println("Select key of rank 4: " + multiset.select(4) + " Expected: 5");

        //Test rank()
        //Note that the expected rank of key 8 is 7 and not 6, because we are assuming that rank returns the index
        // of the rightmost key when there are duplicates
        StdOut.println("Rank of key 8: " + multiset.rank(8) + " Expected: 7");
        StdOut.println("Rank of key 9: " + multiset.rank(9) + " Expected: 8");

        //Test delete()
        StdOut.println("\nDelete key 20");
        multiset.delete(20);

        for(Integer key : multiset.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nKeys size: " + multiset.size() + " Expected: 12");

        StdOut.println("\nDelete key 5");
        multiset.delete(5);

        for(Integer key : multiset.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nKeys size: " + multiset.size() + " Expected: 10");

        //Test deleteMin()
        StdOut.println("\nDelete min (key 0)");
        multiset.deleteMin();

        for(Integer key : multiset.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nKeys size: " + multiset.size() + " Expected: 6");

        //Test deleteMax()
        StdOut.println("\nDelete max (key 24)");
        multiset.deleteMax();

        for(Integer key : multiset.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nKeys size: " + multiset.size() + " Expected: 5");

        //Test keys() with range
        StdOut.println("\nKeys in range [2, 10]");
        for(Integer key : multiset.keys(2, 10)) {
            StdOut.print(key + " ");
        }

        StdOut.println("\n\nKeys in range [20, 22]");
        for(Integer key : multiset.keys(20, 22)) {
            StdOut.print(key + " ");
        }

        //Delete all
        StdOut.println("\n\nDelete all");
        while (multiset.size() > 0) {
            for(Integer key : multiset.keys()) {
                StdOut.print(key + " ");
            }
            //multiset.delete(multiset.select(0));
            multiset.delete(multiset.select(multiset.size() - 1));
            StdOut.println();
        }
    }

}
