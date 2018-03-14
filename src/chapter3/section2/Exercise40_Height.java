package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 11/06/17.
 */
public class Exercise40_Height {

    private class BinarySearchTreeHeight<Key extends Comparable<Key>, Value> extends BinarySearchTree<Key, Value> {

        public int height() {
            if (root == null) {
                return -1;
            }

            return height(root, -1);
        }

        private int height(Node node, int currentHeight) {
            if (node == null) {
                return currentHeight;
            }

            int height = currentHeight;

            int leftHeight = height(node.left, currentHeight + 1);
            if (leftHeight > height) {
                height = leftHeight;
            }

            int rightHeight = height(node.right, currentHeight + 1);
            if (rightHeight > height) {
                height = rightHeight;
            }

            return height;
        }
    }

    private int totalHeights;

    private void doExperiment() {
        int[] binaryTreeSizes = {10000, 100000, 1000000};
        int numberOfExperiments = 100;

        for(int i = 0; i < binaryTreeSizes.length; i++) {
            totalHeights = 0;

            for(int j = 0; j < numberOfExperiments; j++) {
                computeBSTHeight(binaryTreeSizes[i]);
            }

            double averageBSTHeight = totalHeights / (double) numberOfExperiments;
            double expectedAverageBSTHeight = 2.99 * (Math.log(binaryTreeSizes[i]) / Math.log(2));

            StdOut.println("Experiments with N = " + binaryTreeSizes[i]);
            StdOut.printf("Average BST height: %.2f\n", averageBSTHeight);
            StdOut.printf("Expected average BST height: %.2f\n\n", expectedAverageBSTHeight);
        }
    }

    private void computeBSTHeight(int treeSize) {
        int maxValue = 10000000;
        BinarySearchTreeHeight<Integer, Integer> binarySearchTree = new BinarySearchTreeHeight<>();

        for(int i = 0; i < treeSize; i++) {
            int randomValue = StdRandom.uniform(maxValue);
            binarySearchTree.put(randomValue, randomValue);
        }

        totalHeights += binarySearchTree.height();
    }

    public static void main(String[] args) {
        new Exercise40_Height().doExperiment();
    }

}
