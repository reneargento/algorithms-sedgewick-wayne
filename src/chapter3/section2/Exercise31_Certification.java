package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 04/06/17.
 */
@SuppressWarnings("unchecked")
public class Exercise31_Certification {

    public static void main(String[] args) {
        Exercise31_Certification certification = new Exercise31_Certification();

        BinarySearchTree.Node root = new BinarySearchTree().new Node(10, "Value 10", 7);
        root.left = new BinarySearchTree().new Node(5, "Value 5", 4);
        root.left.left = new BinarySearchTree().new Node(2, "Value 2", 1);
        root.left.right = new BinarySearchTree().new Node(7, "Value 7", 2);
        root.left.right.right = new BinarySearchTree().new Node(9, "Value 9", 1);

        root.right = new BinarySearchTree().new Node(14, "Value 14", 2);
        root.right.left = new BinarySearchTree().new Node(11, "Value 11", 1);

        StdOut.println(certification.isBST(root) + " Expected: true");

        BinarySearchTree.Node root2 = new BinarySearchTree().new Node(20, "Value 1", 7);
        root2.left = new BinarySearchTree().new Node(5, "Value 5", 4);
        root2.left.left = new BinarySearchTree().new Node(2, "Value 2", 1);
        root2.left.right = new BinarySearchTree().new Node(1, "Value 1", 2); //Not a BST
        root2.left.right.right = new BinarySearchTree().new Node(9, "Value 9", 1);

        root2.right = new BinarySearchTree().new Node(24, "Value 24", 2);
        root2.right.left = new BinarySearchTree().new Node(21, "Value 21", 1);

        StdOut.println(certification.isBST(root2) + " Expected: false");
    }

    private boolean isBST(BinarySearchTree.Node root) {
        return isBST(root, null, null);
    }

    private boolean isBST(BinarySearchTree.Node node, Comparable low, Comparable high) {

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

}
