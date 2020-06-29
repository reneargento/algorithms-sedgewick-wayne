package chapter3.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 29/06/17.
 */
// Based on http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/AVLTreeST.java.html
// Thanks to williamcheng-web (https://github.com/williamcheng-web) for finding a bug on the height update after a rotation:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/162
public class Exercise32_AVLTrees {

    private class AVLTree<Key extends Comparable<Key>, Value> {

        private class Node {
            Key key;
            Value value;
            Node left, right;

            int height;
            int size;

            Node(Key key, Value value, int size, int height) {
                this.key = key;
                this.value = value;

                this.size = size;
                this.height = height;
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

        public int height() {
            return height(root);
        }

        private int height(Node node) {
            if (node == null) {
                return -1;
            }

            return node.height;
        }

        public boolean isEmpty() {
            return size(root) == 0;
        }

        private Node rotateLeft(Node node) {
            if (node == null || node.right == null) {
                return node;
            }

            Node newRoot = node.right;

            node.right = newRoot.left;
            newRoot.left = node;

            node.height = 1 + Math.max(height(node.left), height(node.right));
            newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right));

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

            node.height = 1 + Math.max(height(node.left), height(node.right));
            newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right));

            newRoot.size = node.size;
            node.size = size(node.left) + 1 + size(node.right);

            return newRoot;
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
                return new Node(key, value, 1, 0);
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }

            node.height = 1 + Math.max(height(node.left), height(node.right));
            node.size = size(node.left) + 1 + size(node.right);

            return balance(node);
        }

        private Node balance(Node node) {

            if (balanceFactor(node) < -1) {
                //right-left case
                if (balanceFactor(node.right) > 0) {
                    node.right = rotateRight(node.right);
                }
                node = rotateLeft(node);
            }

            if (balanceFactor(node) > 1) {
                //left-right case
                if (balanceFactor(node.left) < 0) {
                    node.left = rotateLeft(node.left);
                }
                node = rotateRight(node);
            }

            return node;
        }

        /**
         * Returns the balance factor of the subtree. The balance factor is defined
         * as the difference in height of the left subtree and right subtree, in
         * this order. Therefore, a subtree with a balance factor of -1, 0 or 1 has
         * the AVL property since the heights of the two child subtrees differ by at
         * most one.
         */
        private int balanceFactor(Node node) {
            if (node == null) {
                return 0;
            }
            return height(node.left) - height(node.right);
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

            root = deleteMin(root);
        }

        private Node deleteMin(Node node) {
            if (node.left == null) {
                return node.right;
            }

            node.left = deleteMin(node.left);

            node.size = size(node.left) + 1 + size(node.right);
            node.height = 1 + Math.max(height(node.left), height(node.right));
            return balance(node);
        }

        public void deleteMax() {
            if (isEmpty()) {
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
            node.height = 1 + Math.max(height(node.left), height(node.right));
            return balance(node);
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
                    Node aux = min(node.right);
                    node.key = aux.key;
                    node.value = aux.value;
                    node.right = deleteMin(node.right);
                }
            }

            node.size = size(node.left) + 1 + size(node.right);
            node.height = 1 + Math.max(height(node.left), height(node.right));
            return balance(node);
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

        private boolean isAVL() {
            return isAVL(root);
        }

        private boolean isAVL(Node node) {
            if (node == null) {
                return true;
            }

            int balanceFactor = balanceFactor(node);
            if (balanceFactor < -1 || balanceFactor > 1) {
                return false;
            }

            return isAVL(node.left) && isAVL(node.right);
        }

        private boolean isSubtreeCountConsistent() {
            return isSubtreeCountConsistent(root);
        }

        private boolean isSubtreeCountConsistent(Node node) {
            if (node == null) {
                return true;
            }

            int totalSubtreeCount = 0;
            if (node.left != null) {
                totalSubtreeCount += node.left.size;
            }
            if (node.right != null) {
                totalSubtreeCount += node.right.size;
            }

            if (node.size != totalSubtreeCount + 1) {
                return false;
            }

            return isSubtreeCountConsistent(node.left) && isSubtreeCountConsistent(node.right);
        }
    }

    public static void main(String[] args) {
        Exercise32_AVLTrees avlTrees = new Exercise32_AVLTrees();
        AVLTree<Integer, Integer> avlTree = avlTrees.new AVLTree<>();

        avlTree.put(5, 5);
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        avlTree.put(1, 1);
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        avlTree.put(9, 9);
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        avlTree.put(2, 2);
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        avlTree.put(0, 0);
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        avlTree.put(99, 99);
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        avlTree.put(-1, -1);
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        avlTree.put(-2, -2);
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        avlTree.put(3, 3);
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        avlTree.put(-5, -5);
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");

        StdOut.println("Size consistent: " + avlTree.isSubtreeCountConsistent() + " Expected: true\n");

        StdOut.println("Keys() test");

        for(Integer key : avlTree.keys()) {
            StdOut.println("Key " + key + ": " + avlTree.get(key));
        }
        StdOut.println("Expected: -5 -2 -1 0 1 2 3 5 9 99\n");

        //Test min()
        StdOut.println("Min key: " + avlTree.min() + " Expected: -5");

        //Test max()
        StdOut.println("Max key: " + avlTree.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + avlTree.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + avlTree.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + avlTree.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + avlTree.ceiling(15) + " Expected: 99");

        //Test select()
        StdOut.println("Select key of rank 4: " + avlTree.select(4) + " Expected: 1");

        //Test rank()
        StdOut.println("Rank of key 9: " + avlTree.rank(9) + " Expected: 8");
        StdOut.println("Rank of key 10: " + avlTree.rank(10) + " Expected: 9");

        //Test delete()
        StdOut.println("\nDelete key 2");
        avlTree.delete(2);

        for(Integer key : avlTree.keys()) {
            StdOut.println("Key " + key + ": " + avlTree.get(key));
        }
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        StdOut.println("Size consistent: " + avlTree.isSubtreeCountConsistent() + " Expected: true");

        //Test deleteMin()
        StdOut.println("\nDelete min (key -5)");
        avlTree.deleteMin();

        for(Integer key : avlTree.keys()) {
            StdOut.println("Key " + key + ": " + avlTree.get(key));
        }
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        StdOut.println("Size consistent: " + avlTree.isSubtreeCountConsistent() + " Expected: true");

        //Test deleteMax()
        StdOut.println("\nDelete max (key 99)");
        avlTree.deleteMax();

        for(Integer key : avlTree.keys()) {
            StdOut.println("Key " + key + ": " + avlTree.get(key));
        }
        StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
        StdOut.println("Size consistent: " + avlTree.isSubtreeCountConsistent() + " Expected: true");

        //Test keys() with range
        StdOut.println("\nKeys in range [2, 10]");
        for(Integer key : avlTree.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + avlTree.get(key));
        }

        StdOut.println("\nKeys in range [-4, -1]");
        for(Integer key : avlTree.keys(-4, -1)) {
            StdOut.println("Key " + key + ": " + avlTree.get(key));
        }

        //Delete all
        StdOut.println("\nDelete all");
        while (avlTree.size() > 0) {
            for(Integer key : avlTree.keys()) {
                StdOut.println("Key " + key + ": " + avlTree.get(key));
            }

            //avlTree.delete(avlTree.select(0));
            avlTree.delete(avlTree.select(avlTree.size() - 1));
            StdOut.println("Is AVL: " + avlTree.isAVL() + " Expected: true");
            StdOut.println("Size consistent: " + avlTree.isSubtreeCountConsistent() + " Expected: true");

            StdOut.println();
        }
    }
}
