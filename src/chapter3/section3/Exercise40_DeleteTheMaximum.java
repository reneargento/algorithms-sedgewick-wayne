package chapter3.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 21/06/17.
 */
public class Exercise40_DeleteTheMaximum {

    private class RedBlackBSTDeleteMax<Key extends Comparable<Key>, Value>  extends RedBlackBST<Key, Value> {

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

            if (isRed(node.left) && node.left != null && isRed(node.left.left)) {
                node = rotateRight(node);
            }

            if (isRed(node.left) && isRed(node.right)) {
                flipColors(node);
            }

            node.size = size(node.left) + 1 + size(node.right);

            return node;
        }

        protected void flipColors(Node node) {
            if (node != null) {
                node.color = !node.color;

                if (node.left != null) {
                    node.left.color = !node.left.color;
                }
                if (node.right != null) {
                    node.right.color = !node.right.color;
                }
            }
        }

    }

    public static void main(String[] args) {
        Exercise40_DeleteTheMaximum deleteTheMaximum = new Exercise40_DeleteTheMaximum();
        RedBlackBSTDeleteMax<Integer, Integer> redBlackBST = deleteTheMaximum.new RedBlackBSTDeleteMax<>();

        redBlackBST.put(10, 10);
        redBlackBST.put(4, 4);
        redBlackBST.put(6, 6);
        redBlackBST.put(1, 1);
        redBlackBST.put(2, 2);
        redBlackBST.put(15, 15);
        redBlackBST.put(12, 12);

        while (!redBlackBST.isEmpty()) {

            for(Integer key : redBlackBST.keys()) {
                StdOut.println(key);
            }

            StdOut.println();

            StdOut.println("Delete max");
            redBlackBST.deleteMax();
        }
    }
}
