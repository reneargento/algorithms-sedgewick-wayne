package chapter3.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 29/06/17.
 */
@SuppressWarnings("unchecked")
public class Exercise33_Certification {

    private class RedBlackBSTCertification<Key extends Comparable<Key>, Value> extends RedBlackBST<Key, Value> {
        public boolean is23() {
            return is23(root);
        }

        private boolean is23(Node node) {
            if (node == null) {
                return true;
            }

            if (isRed(node.right)) {
                return false;
            }
            if (isRed(node) && isRed(node.left)) {
                return false;
            }

            return is23(node.left) && is23(node.right);
        }

        public boolean isBalanced() {
            int blackNodes = 0; // number of black links on path from root to min

            Node currentNode = root;
            while (currentNode != null) {
                if (!isRed(currentNode)) {
                    blackNodes++;
                }

                currentNode = currentNode.left;
            }

            return isBalanced(root, blackNodes);
        }

        private boolean isBalanced(Node node, int blackNodes) {
            if (node == null) {
                return blackNodes == 0;
            }

            if (!isRed(node)) {
                blackNodes--;
            }

            return isBalanced(node.left, blackNodes) && isBalanced(node.right, blackNodes);
        }

        public boolean isBST() {
            return isBST(root, null, null);
        }

        private boolean isBST(Node node, Comparable low, Comparable high) {
            if (node == null) {
                return true;
            }

            if (low != null && low.compareTo(node.key) >= 0) {
                return false;
            }
            if (high != null && high.compareTo(node.key) <= 0) {
                return false;
            }

            return isBST(node.left, low, node.key) && isBST(node.right, node.key, high);
        }

        public boolean isRedBlackBST() {
            return isBST() && is23() && isBalanced();
        }

    }

    public static void main(String[] args) {
        Exercise33_Certification certification = new Exercise33_Certification();
        RedBlackBSTCertification<Integer, String> redBlackBSTCertification = certification.new RedBlackBSTCertification<>();

        RedBlackBST.Node root = new RedBlackBST().new Node(10, "Value 10", 7, false);
        root.left = new RedBlackBST().new Node(5, "Value 5", 4, true);
        root.left.left = new RedBlackBST().new Node(2, "Value 2", 1, false);
        root.left.right = new RedBlackBST().new Node(9, "Value 9", 2, false);
        root.left.right.left = new RedBlackBST().new Node(7, "Value 7", 1, true);

        root.right = new RedBlackBST().new Node(14, "Value 14", 2, false);
        root.right.left = new RedBlackBST().new Node(11, "Value 11", 1, true);

        StdOut.println("Test 1");
        StdOut.println(redBlackBSTCertification.is23(root) + " Expected: true");
        StdOut.println(redBlackBSTCertification.isBalanced(root, 2) + " Expected: true");
        StdOut.println(redBlackBSTCertification.isBST(root, null, null) + " Expected: true\n");

        RedBlackBST.Node root2 = new RedBlackBST().new Node(20, "Value 20", 7, false);
        root2.left = new RedBlackBST().new Node(5, "Value 5", 4, true);
        root2.left.left = new RedBlackBST().new Node(2, "Value 2", 1, false);
        root2.left.right = new RedBlackBST().new Node(9, "Value 9", 2, false);
        root2.left.right.left = new RedBlackBST().new Node(1, "Value 1", 1, true); //Not a BST

        root2.right = new RedBlackBST().new Node(24, "Value 24", 2, false);
        root2.right.left = new RedBlackBST().new Node(21, "Value 21", 1, true);

        StdOut.println("Test 2");
        StdOut.println(redBlackBSTCertification.is23(root2) + " Expected: true");
        StdOut.println(redBlackBSTCertification.isBalanced(root2, 2) + " Expected: true");
        StdOut.println(redBlackBSTCertification.isBST(root2, null, null) + " Expected: false\n");

        RedBlackBST.Node root3 = new RedBlackBST().new Node(10, "Value 10", 7, false);
        root3.left = new RedBlackBST().new Node(5, "Value 5", 4, true);
        root3.left.left = new RedBlackBST().new Node(2, "Value 2", 1, false);
        root3.left.right = new RedBlackBST().new Node(9, "Value 9", 2, false);
        root3.left.right.left = new RedBlackBST().new Node(7, "Value 7", 1, true);

        root3.right = new RedBlackBST().new Node(14, "Value 14", 2, true); //Not 2-3 tree, not balanced
        root3.right.left = new RedBlackBST().new Node(11, "Value 11", 1, true);

        StdOut.println("Test 3");
        StdOut.println(redBlackBSTCertification.is23(root3) + " Expected: false");
        StdOut.println(redBlackBSTCertification.isBalanced(root3, 2) + " Expected: false");
        StdOut.println(redBlackBSTCertification.isBST(root3, null, null) + " Expected: true\n");

        RedBlackBST.Node root4 = new RedBlackBST().new Node(10, "Value 10", 7, false);
        root4.left = new RedBlackBST().new Node(5, "Value 5", 4, true);
        root4.left.left = new RedBlackBST().new Node(2, "Value 2", 1, true); //Not 2-3 tree, not balanced
        root4.left.right = new RedBlackBST().new Node(9, "Value 9", 2, false);
        root4.left.right.left = new RedBlackBST().new Node(7, "Value 7", 1, true);

        root.right = new RedBlackBST().new Node(14, "Value 14", 2, false);
        root.right.left = new RedBlackBST().new Node(11, "Value 11", 1, true);

        StdOut.println("Test 4");
        StdOut.println(redBlackBSTCertification.is23(root4) + " Expected: false");
        StdOut.println(redBlackBSTCertification.isBalanced(root4, 2) + " Expected: false");
        StdOut.println(redBlackBSTCertification.isBST(root4, null, null) + " Expected: true");
    }

}
