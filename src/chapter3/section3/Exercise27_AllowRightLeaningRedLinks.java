package chapter3.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 28/06/17.
 */
public class Exercise27_AllowRightLeaningRedLinks {

    private class RedBlackTopDown234RightLeaningBST<Key extends Comparable<Key>, Value> extends Exercise25_TopDown234Trees.RedBlackTopDown234BST<Key, Value> {

        public void put(Key key, Value value) {
            if (key == null) {
                return;
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

            if (isRed(node.left) && isRed(node.right)) {
                flipColors(node);
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }

            if (isRed(node.right) && isRed(node.right.left)) {
                node.right = rotateRight(node.right);
            }
            if (isRed(node.right) && isRed(node.right.right)) {
                node = rotateLeft(node);
            }
            if (isRed(node.left) && isRed(node.left.right)) {
                node.left = rotateLeft(node.left);
            }
            if (isRed(node.left) && isRed(node.left.left)) {
                node = rotateRight(node);
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
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
            if (isRed(node.left) && !isRed(node.right)) {
                node = rotateRight(node);
            }

            if (node.right == null) {
                return null;
            }

            if (!isRed(node.right) && node.right != null && !isRed(node.right.left) && !isRed(node.right.right)) {
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
                if (isRed(node.left) && !isRed(node.right)) {
                    node = rotateRight(node);
                }

                if (key.compareTo(node.key) == 0 && node.right == null) {
                    return null;
                }

                if (!isRed(node.right) && node.right != null && !isRed(node.right.left) && !isRed(node.right.right)) {
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
            //Assuming that node is red and node.left and node.left.left and node.left.right are black,
            // make node.left or one of its children red
            flipColors(node);

            if (node.right != null && isRed(node.right.left)) {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
                flipColors(node);
            } else if (node.right != null && isRed(node.right.right)) {
                node.right = rotateLeft(node.right);
                node = rotateLeft(node);
                flipColors(node);
            }

            return node;
        }

        private Node moveRedRight(Node node) {
            //Assuming that node is red and node.right and node.right.left and node.right.right are black,
            // make node.right or one of its children red
            flipColors(node);

            if (node.left != null && isRed(node.left.left)) {
                node = rotateRight(node);
                flipColors(node);
            } else if (node.left != null && isRed(node.left.right)) {
                node.left = rotateLeft(node.left);
                node = rotateRight(node);
                flipColors(node);
            }

            return node;
        }

        private Node balance(Node node) {
            if (node == null) {
                return null;
            }

            if (isRed(node.right) && isRed(node.right.left)) {
                node.right = rotateRight(node.right);
            }
            if (isRed(node.right) && isRed(node.right.right)) {
                node = rotateLeft(node);
            }
            if (isRed(node.left) && isRed(node.left.right)) {
                node.left = rotateLeft(node.left);
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

        public boolean isValid234RightLeaningTree() {
            return isValid234RightLeaningTree(root);
        }

        private boolean isValid234RightLeaningTree(Node node) {
            if (node == null) {
                return true;
            }

            if (isRed(node.left) && isRed(node.left.left)) {
                return false;
            }
            if (isRed(node.left) && isRed(node.left.right)) {
                return false;
            }
            if (isRed(node.right) && isRed(node.right.right)) {
                return false;
            }
            if (isRed(node.right) && isRed(node.right.left)) {
                return false;
            }

            return isValid234RightLeaningTree(node.left) && isValid234RightLeaningTree(node.right);
        }
    }

    public static void main(String[] args) {
        //Expected 2-3-4 tree
        //
        //                 (B)1
        //         (R)-1          (R)5
        //    (B)-2   (B)0    (B)2    (B)9
        // (R)-5                (R)3    (R)99
        //

        Exercise27_AllowRightLeaningRedLinks allowRightLeaningRedLinks = new Exercise27_AllowRightLeaningRedLinks();
        RedBlackTopDown234RightLeaningBST<Integer, Integer> redBlackTopDown234RightLeaningBST =
                allowRightLeaningRedLinks.new RedBlackTopDown234RightLeaningBST<>();
        redBlackTopDown234RightLeaningBST.put(5, 5);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        redBlackTopDown234RightLeaningBST.put(1, 1);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        redBlackTopDown234RightLeaningBST.put(9, 9);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        redBlackTopDown234RightLeaningBST.put(2, 2);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        redBlackTopDown234RightLeaningBST.put(0, 0);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        redBlackTopDown234RightLeaningBST.put(99, 99);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        redBlackTopDown234RightLeaningBST.put(-1, -1);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        redBlackTopDown234RightLeaningBST.put(-2, -2);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        redBlackTopDown234RightLeaningBST.put(3, 3);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        redBlackTopDown234RightLeaningBST.put(-5, -5);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true\n");

        StdOut.println("Size consistent: " + redBlackTopDown234RightLeaningBST.isSubtreeCountConsistent() + " Expected: true\n");

        StdOut.println("Keys() test");

        for(Integer key : redBlackTopDown234RightLeaningBST.keys()) {
            StdOut.println("Key " + key + ": " + redBlackTopDown234RightLeaningBST.get(key));
        }
        StdOut.println("Expected: -5 -2 -1 0 1 2 3 5 9 99\n");

        //Test min()
        StdOut.println("Min key: " + redBlackTopDown234RightLeaningBST.min() + " Expected: -5");

        //Test max()
        StdOut.println("Max key: " + redBlackTopDown234RightLeaningBST.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + redBlackTopDown234RightLeaningBST.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + redBlackTopDown234RightLeaningBST.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + redBlackTopDown234RightLeaningBST.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + redBlackTopDown234RightLeaningBST.ceiling(15) + " Expected: 99");

        //Test select()
        StdOut.println("Select key of rank 4: " + redBlackTopDown234RightLeaningBST.select(4) + " Expected: 1");

        //Test rank()
        StdOut.println("Rank of key 9: " + redBlackTopDown234RightLeaningBST.rank(9) + " Expected: 8");
        StdOut.println("Rank of key 10: " + redBlackTopDown234RightLeaningBST.rank(10) + " Expected: 9");

        //Test delete()
        StdOut.println("\nDelete key 2");
        redBlackTopDown234RightLeaningBST.delete(2);

        for(Integer key : redBlackTopDown234RightLeaningBST.keys()) {
            StdOut.println("Key " + key + ": " + redBlackTopDown234RightLeaningBST.get(key));
        }
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + redBlackTopDown234RightLeaningBST.isSubtreeCountConsistent() + " Expected: true");

        //Test deleteMin()
        StdOut.println("\nDelete min (key -5)");
        redBlackTopDown234RightLeaningBST.deleteMin();

        for(Integer key : redBlackTopDown234RightLeaningBST.keys()) {
            StdOut.println("Key " + key + ": " + redBlackTopDown234RightLeaningBST.get(key));
        }
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + redBlackTopDown234RightLeaningBST.isSubtreeCountConsistent() + " Expected: true");

        //Test deleteMax()
        StdOut.println("\nDelete max (key 99)");
        redBlackTopDown234RightLeaningBST.deleteMax();

        for(Integer key : redBlackTopDown234RightLeaningBST.keys()) {
            StdOut.println("Key " + key + ": " + redBlackTopDown234RightLeaningBST.get(key));
        }
        StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
        StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + redBlackTopDown234RightLeaningBST.isSubtreeCountConsistent() + " Expected: true");

        //Test keys() with range
        StdOut.println("\nKeys in range [2, 10]");
        for(Integer key : redBlackTopDown234RightLeaningBST.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + redBlackTopDown234RightLeaningBST.get(key));
        }

        StdOut.println("\nKeys in range [-4, -1]");
        for(Integer key : redBlackTopDown234RightLeaningBST.keys(-4, -1)) {
            StdOut.println("Key " + key + ": " + redBlackTopDown234RightLeaningBST.get(key));
        }

        //Delete all
        StdOut.println("\nDelete all");
        while (redBlackTopDown234RightLeaningBST.size() > 0) {
            for(Integer key : redBlackTopDown234RightLeaningBST.keys()) {
                StdOut.println("Key " + key + ": " + redBlackTopDown234RightLeaningBST.get(key));
            }

            //redBlackIterative234BST.delete(redBlackIterative234BST.select(0));
            redBlackTopDown234RightLeaningBST.delete(redBlackTopDown234RightLeaningBST.select(redBlackTopDown234RightLeaningBST.size() - 1));
            StdOut.println("Is valid 2-3-4 tree: " + redBlackTopDown234RightLeaningBST.isValid234RightLeaningTree() + " Expected: true");
            StdOut.println("Is BST: " + redBlackTopDown234RightLeaningBST.isBST() + " Expected: true");
            StdOut.println("Size consistent: " + redBlackTopDown234RightLeaningBST.isSubtreeCountConsistent() + " Expected: true");

            StdOut.println();
        }
    }

}
