package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 04/06/17.
 */
@SuppressWarnings("unchecked")
//Based on http://www.geeksforgeeks.org/construct-bst-from-given-preorder-traversa/
public class Exercise30_BSTReconstruction {

    public static void main(String[] args) {

        /**
         *     10
         *  5      40
         * 1 7   30  50
         *    8
         */

        Exercise30_BSTReconstruction bstReconstruction = new Exercise30_BSTReconstruction();
        int pre[] = new int[]{10, 5, 1, 7, 8, 40, 30, 50};

        BinarySearchTree.Node root = bstReconstruction.constructBSTWithPreorder(pre);
        bstReconstruction.printBSTInOrder(root);

        StdOut.println();

        int post[] = new int[] {1, 8, 7, 5, 30, 50, 40, 10};

        BinarySearchTree.Node root2 = bstReconstruction.constructBSTWithPostorder(post);
        bstReconstruction.printBSTInOrder(root2);
    }

    private int preIndex;
    private int postIndex;

    private BinarySearchTree.Node constructBSTWithPreorder(int[] pre) {
        if (pre == null || pre.length == 0) {
            return null;
        }

        preIndex = 0;

        return constructBSTWithPreorder(pre, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private BinarySearchTree.Node constructBSTWithPreorder(int[] pre, int min, int max) {
        if (preIndex >= pre.length) {
            return null;
        }

        BinarySearchTree.Node root = null;

        //Check if the key is in range
        if (min < pre[preIndex] && pre[preIndex] < max) {
            root = new BinarySearchTree().new Node(pre[preIndex], pre[preIndex], 1);
            preIndex++;

            root.left = constructBSTWithPreorder(pre, min, pre[preIndex - 1]);
            root.right = constructBSTWithPreorder(pre, pre[preIndex - 1], max);

            //Update node's size
            if (root.left != null) {
                root.size += root.left.size;
            }
            if (root.right != null) {
                root.size += root.right.size;
            }
        }

        return root;
    }

    private BinarySearchTree.Node constructBSTWithPostorder(int[] post) {
        if (post == null || post.length == 0) {
            return null;
        }

        int[] reverseArray = new int[post.length];
        for(int i = 0; i < reverseArray.length; i++) {
            reverseArray[i] = post[post.length - 1 - i];
        }

        postIndex = 0;

        return constructBSTWithPostorder(reverseArray, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private BinarySearchTree.Node constructBSTWithPostorder(int[] post, int min, int max) {
        if (postIndex >= post.length) {
            return null;
        }

        BinarySearchTree.Node root = null;

        //Check if the key is in range
        if (min < post[postIndex] && post[postIndex] < max) {
            root = new BinarySearchTree().new Node(post[postIndex], post[postIndex], 1);
            postIndex++;

            root.right = constructBSTWithPostorder(post, post[postIndex - 1], max);
            root.left = constructBSTWithPostorder(post, min, post[postIndex - 1]);

            //Update node's size
            if (root.left != null) {
                root.size += root.left.size;
            }
            if (root.right != null) {
                root.size += root.right.size;
            }
        }

        return root;
    }

    private void printBSTInOrder(BinarySearchTree.Node node) {
        if (node == null) {
            return;
        }

        printBSTInOrder(node.left);
        StdOut.print(node.key + " ");
        printBSTInOrder(node.right);
    }

}
