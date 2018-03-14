package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 05/08/17.
 */
public class Exercise9 {

    /**
     * This tree has the following property:
     *
     * Let x be a node in the binary search tree.
     * If y is a node in the left subtree of x, then y:key <= x:key.
     * If y is a node in the right subtree of x, then y:key > x:key.
     */
    private class BinarySearchTreeDuplicateKeys<Key extends Comparable<Key>, Value> {

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

            //If it is a duplicate key, put it on the left subtree
            if (compare <= 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
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
            if (root == null) {
                return;
            }

            Key minKey = min();

            while (contains(minKey)) {
                root = deleteMin(root);
            }
        }

        private Node deleteMin(Node node) {
            if (node.left == null) {
                return node.right;
            }

            node.left = deleteMin(node.left);
            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        //O(n lg n) since we are removing all duplicate max keys
        public void deleteMax() {
            if (root == null) {
                return;
            }

            Key maxKey = max();

            while (contains(maxKey)) {
                root = deleteMax(root);
            }
        }

        private Node deleteMax(Node node) {
            if (node.right == null) {
                return node.left;
            }

            node.right = deleteMax(node.right);
            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        //O(n lg n) since we are removing all duplicate keys
        public void delete(Key key) {
            if (isEmpty()) {
                return;
            }

            while(contains(key)) {
                root = delete(root, key);
            }
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
        Exercise9 exercise9 = new Exercise9();
        BinarySearchTreeDuplicateKeys<Integer, Integer> binarySearchTreeDuplicateKeys =
                exercise9.new BinarySearchTreeDuplicateKeys<>();

        //Test put()
        binarySearchTreeDuplicateKeys.put(0, 0);
        binarySearchTreeDuplicateKeys.put(0, 1);
        binarySearchTreeDuplicateKeys.put(0, 2);
        binarySearchTreeDuplicateKeys.put(0, 3);

        binarySearchTreeDuplicateKeys.put(5, 7);
        binarySearchTreeDuplicateKeys.put(5, 8);

        binarySearchTreeDuplicateKeys.put(8, 9);
        binarySearchTreeDuplicateKeys.put(8, 10);

        binarySearchTreeDuplicateKeys.put(20, 11);
        binarySearchTreeDuplicateKeys.put(20, 12);
        binarySearchTreeDuplicateKeys.put(20, 13);
        binarySearchTreeDuplicateKeys.put(20, 14);

        binarySearchTreeDuplicateKeys.put(21, 15);
        binarySearchTreeDuplicateKeys.put(22, 16);
        binarySearchTreeDuplicateKeys.put(23, 17);
        binarySearchTreeDuplicateKeys.put(24, 18);

        StdOut.println("Keys() test");
        for(Integer key : binarySearchTreeDuplicateKeys.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTreeDuplicateKeys.get(key));
        }
        StdOut.println("\nExpected:");
        //When there are duplicate keys the expected value is the value of the first inserted key
        StdOut.println("Key 0: 0");
        StdOut.println("Key 0: 0");
        StdOut.println("Key 0: 0");
        StdOut.println("Key 0: 0");
        StdOut.println("Key 5: 7");
        StdOut.println("Key 5: 7");
        StdOut.println("Key 8: 9");
        StdOut.println("Key 8: 9");
        StdOut.println("Key 20: 11");
        StdOut.println("Key 20: 11");
        StdOut.println("Key 20: 11");
        StdOut.println("Key 20: 11");
        StdOut.println("Key 21: 15");
        StdOut.println("Key 22: 16");
        StdOut.println("Key 23: 17");
        StdOut.println("Key 24: 18");

        //Test size()
        StdOut.println("Keys size: " + binarySearchTreeDuplicateKeys.size() + " Expected: 16");

        //Test size() with range
        StdOut.println("Keys size [0, 20]: " + binarySearchTreeDuplicateKeys.size(0, 20) + " Expected: 12");

        //Test contains()
        StdOut.println("\nContains 8: " + binarySearchTreeDuplicateKeys.contains(8) + " Expected: true");
        StdOut.println("Contains 9: " + binarySearchTreeDuplicateKeys.contains(9) + " Expected: false");

        //Test min()
        StdOut.println("\nMin key: " + binarySearchTreeDuplicateKeys.min() + " Expected: 0");

        //Test max()
        StdOut.println("Max key: " + binarySearchTreeDuplicateKeys.max() + " Expected: 24");

        //Test floor()
        StdOut.println("Floor of 5: " + binarySearchTreeDuplicateKeys.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + binarySearchTreeDuplicateKeys.floor(15) + " Expected: 8");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + binarySearchTreeDuplicateKeys.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + binarySearchTreeDuplicateKeys.ceiling(15) + " Expected: 20");

        //Test select()
        StdOut.println("Select key of rank 3: " + binarySearchTreeDuplicateKeys.select(3) + " Expected: 0");
        StdOut.println("Select key of rank 4: " + binarySearchTreeDuplicateKeys.select(4) + " Expected: 5");

        //Test rank()
        //Note that the expected rank of key 8 is 7 and not 6, because we are assuming that rank returns the index
        // of the rightmost key when there are duplicates
        StdOut.println("Rank of key 8: " + binarySearchTreeDuplicateKeys.rank(8) + " Expected: 7");
        StdOut.println("Rank of key 9: " + binarySearchTreeDuplicateKeys.rank(9) + " Expected: 8");

        //Test delete()
        StdOut.println("\nDelete key 20");
        binarySearchTreeDuplicateKeys.delete(20);

        for(Integer key : binarySearchTreeDuplicateKeys.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTreeDuplicateKeys.get(key));
        }
        StdOut.println("Keys size: " + binarySearchTreeDuplicateKeys.size() + " Expected: 12");

        StdOut.println("\nDelete key 5");
        binarySearchTreeDuplicateKeys.delete(5);

        for(Integer key : binarySearchTreeDuplicateKeys.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTreeDuplicateKeys.get(key));
        }
        StdOut.println("Keys size: " + binarySearchTreeDuplicateKeys.size() + " Expected: 10");

        //Test deleteMin()
        StdOut.println("\nDelete min (key 0)");
        binarySearchTreeDuplicateKeys.deleteMin();

        for(Integer key : binarySearchTreeDuplicateKeys.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTreeDuplicateKeys.get(key));
        }

        //Test deleteMax()
        StdOut.println("\nDelete max (key 24)");
        binarySearchTreeDuplicateKeys.deleteMax();

        for(Integer key : binarySearchTreeDuplicateKeys.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTreeDuplicateKeys.get(key));
        }

        //Test keys() with range
        StdOut.println("\nKeys in range [2, 10]");
        for(Integer key : binarySearchTreeDuplicateKeys.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + binarySearchTreeDuplicateKeys.get(key));
        }

        StdOut.println("\nKeys in range [20, 22]");
        for(Integer key : binarySearchTreeDuplicateKeys.keys(20, 22)) {
            StdOut.println("Key " + key + ": " + binarySearchTreeDuplicateKeys.get(key));
        }

        //Delete all
        StdOut.println("\nDelete all");
        while (binarySearchTreeDuplicateKeys.size() > 0) {
            for(Integer key : binarySearchTreeDuplicateKeys.keys()) {
                StdOut.println("Key " + key + ": " + binarySearchTreeDuplicateKeys.get(key));
            }
            //binarySearchTreeDuplicateKeys.delete(binarySearchTreeDuplicateKeys.select(0));
            binarySearchTreeDuplicateKeys.delete(binarySearchTreeDuplicateKeys.select(binarySearchTreeDuplicateKeys.size() - 1));
            StdOut.println();
        }
    }

}
