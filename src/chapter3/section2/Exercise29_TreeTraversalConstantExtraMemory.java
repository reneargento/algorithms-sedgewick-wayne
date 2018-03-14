package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 03/06/17.
 */
@SuppressWarnings("unchecked")
//Based on http://www.geeksforgeeks.org/inorder-tree-traversal-without-recursion-and-without-stack/
public class Exercise29_TreeTraversalConstantExtraMemory {

    public static void main(String[] args) {
        Exercise29_TreeTraversalConstantExtraMemory treeTraversalConstantExtraMemory = new Exercise29_TreeTraversalConstantExtraMemory();
        BinarySearchTree<Integer, String> binarySearchTree = new BinarySearchTree<>();

        binarySearchTree.put(5, "Value 5");
        binarySearchTree.put(1, "Value 1");
        binarySearchTree.put(9, "Value 9");
        binarySearchTree.put(2, "Value 2");
        binarySearchTree.put(0, "Value 0");
        binarySearchTree.put(99, "Value 99");

        treeTraversalConstantExtraMemory.inorderTraversalConstantMemory(binarySearchTree);
    }

    //Morris inorder tree traversal
    //Threaded binary tree
    private void inorderTraversalConstantMemory(BinarySearchTree<Integer, String> binarySearchTree) {
        BinarySearchTree.Node current = binarySearchTree.root;

        while (current != null) {

            if (current.left == null) {
                StdOut.print(current.key + " ");

                current = current.right;
            } else {
                /* Find the inorder predecessor of current */
                BinarySearchTree.Node pre = current.left;

                while (pre.right != null && pre.right != current) {
                    pre = pre.right;
                }

                if (pre.right == null) {
                    /* Make current as right child of its inorder predecessor */
                    pre.right = current;
                    current = current.left;
                } else {
                    /* Revert the changes made to restore the original tree i.e.,fix the right child of predecessor*/
                    pre.right = null;
                    StdOut.print(current.key + " ");
                    current = current.right;
                }
            }
        }
    }

}
