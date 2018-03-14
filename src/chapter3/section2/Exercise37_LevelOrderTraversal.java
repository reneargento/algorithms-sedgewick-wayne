package chapter3.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 10/06/17.
 */
public class Exercise37_LevelOrderTraversal {

    private void printLevel(BinarySearchTree.Node node) {
        Queue<BinarySearchTree.Node> queue = new Queue<>();
        queue.enqueue(node);

        while (!queue.isEmpty()) {
            BinarySearchTree.Node current = queue.dequeue();
            StdOut.print(current.key + " ");

            if (current.left != null) {
                queue.enqueue(current.left);
            }
            if (current.right != null) {
                queue.enqueue(current.right);
            }
        }
    }

    public static void main(String[] args) {
        Exercise37_LevelOrderTraversal levelOrderTraversal = new Exercise37_LevelOrderTraversal();

        /**
         *       10
         *   4       15
         * 1   6   12  20
         *  2            25
         */

        BinarySearchTree<Integer, String> binarySearchTree = new BinarySearchTree<>();
        binarySearchTree.put(10, "Value 10");
        binarySearchTree.put(4, "Value 4");
        binarySearchTree.put(6, "Value 6");
        binarySearchTree.put(1, "Value 1");
        binarySearchTree.put(2, "Value 2");
        binarySearchTree.put(15, "Value 15");
        binarySearchTree.put(12, "Value 12");
        binarySearchTree.put(20, "Value 20");
        binarySearchTree.put(25, "Value 25");

        levelOrderTraversal.printLevel(binarySearchTree.root);
        StdOut.println("\nExpected: 10 4 15 1 6 12 20 2 25");
    }

}
