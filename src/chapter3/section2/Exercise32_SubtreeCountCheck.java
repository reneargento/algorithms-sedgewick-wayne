package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 04/06/17.
 */
@SuppressWarnings("unchecked")
public class Exercise32_SubtreeCountCheck {

    private boolean isSubtreeCountConsistent(BinarySearchTree.Node node) {
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

        if (node.size == totalSubtreeCount + 1) {
            return isSubtreeCountConsistent(node.left) && isSubtreeCountConsistent(node.right);
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        Exercise32_SubtreeCountCheck subtreeCountCheck = new Exercise32_SubtreeCountCheck();

        BinarySearchTree.Node root = new BinarySearchTree().new Node(10, "Value 10", 7);
        root.left = new BinarySearchTree().new Node(5, "Value 5", 4);
        root.left.left = new BinarySearchTree().new Node(2, "Value 2", 1);
        root.left.right = new BinarySearchTree().new Node(7, "Value 7", 2);
        root.left.right.right = new BinarySearchTree().new Node(9, "Value 9", 1);

        root.right = new BinarySearchTree().new Node(14, "Value 14", 2);
        root.right.left = new BinarySearchTree().new Node(11, "Value 11", 1);

        StdOut.println(subtreeCountCheck.isSubtreeCountConsistent(root) + " Expected: true");

        BinarySearchTree.Node root2 = new BinarySearchTree().new Node(10, "Value 10", 7);
        root.left = new BinarySearchTree().new Node(5, "Value 5", 4);
        root.left.left = new BinarySearchTree().new Node(2, "Value 2", 2); //Wrong count
        root.left.right = new BinarySearchTree().new Node(7, "Value 7", 2);
        root.left.right.right = new BinarySearchTree().new Node(9, "Value 9", 1);

        root.right = new BinarySearchTree().new Node(14, "Value 14", 2);
        root.right.left = new BinarySearchTree().new Node(11, "Value 11", 1);

        StdOut.println(subtreeCountCheck.isSubtreeCountConsistent(root2) + " Expected: false");

        BinarySearchTree.Node root3 = new BinarySearchTree().new Node(10, "Value 10", 8); //Wrong count
        root3.left = new BinarySearchTree().new Node(5, "Value 5", 4);
        root3.left.left = new BinarySearchTree().new Node(2, "Value 2", 1);
        root3.left.right = new BinarySearchTree().new Node(7, "Value 7", 2);
        root3.left.right.right = new BinarySearchTree().new Node(9, "Value 9", 1);

        root3.right = new BinarySearchTree().new Node(14, "Value 14", 2);
        root3.right.left = new BinarySearchTree().new Node(11, "Value 11", 1);

        StdOut.println(subtreeCountCheck.isSubtreeCountConsistent(root3) + " Expected: false");
    }

}
