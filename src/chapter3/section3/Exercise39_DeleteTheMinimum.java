package chapter3.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 19/06/17.
 */
public class Exercise39_DeleteTheMinimum {

    private class RedBlackBSTDeleteMin<Key extends Comparable<Key>, Value> extends RedBlackBST<Key, Value> {

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
        Exercise39_DeleteTheMinimum deleteTheMinimum = new Exercise39_DeleteTheMinimum();
        RedBlackBSTDeleteMin<Integer, Integer> redBlackBST = deleteTheMinimum.new RedBlackBSTDeleteMin<>();

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

            StdOut.println("Delete min");
            redBlackBST.deleteMin();
        }
    }
}
