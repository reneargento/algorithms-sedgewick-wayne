package chapter3.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 07/07/17.
 */
public class Exercise42_CountRedNodes {

    public static void main(String[] args) {
        StdOut.printf("%12s %12s\n", "Tree Size | ","Red Nodes %");

        new Exercise42_CountRedNodes().doExperiment();
    }

    private void doExperiment() {
        int[] treeSizes = {10000, 100000, 1000000};
        int numberOfExperiments = 100;

        for (int size = 0; size < treeSizes.length; size++) {
            int totalNodesCount = treeSizes[size] * numberOfExperiments;
            int totalRedNodesCount = 0;

            for(int trial=0; trial < numberOfExperiments; trial++) {
                int treeSize = treeSizes[size];

                RedBlackBST<Integer, Integer> redBlackBST = new RedBlackBST<>();
                for(int i = 0; i < treeSize; i++) {
                    int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
                    redBlackBST.put(randomKey, randomKey);
                }

                totalRedNodesCount += countRedNodes(redBlackBST);
            }

            double percentageOfRedNodes = ((double) totalRedNodesCount) / ((double) totalNodesCount) * 100;
            printResults(treeSizes[size], percentageOfRedNodes);
        }
    }

    private int countRedNodes(RedBlackBST<Integer, Integer> redBlackBST) {
        if (redBlackBST.isEmpty()) {
            return 0;
        }

        return countRedNodes(redBlackBST.root);
    }

    private int countRedNodes(RedBlackBST.Node node) {
        if (node == null) {
            return 0;
        }

        int redNodeCount = node.color == RedBlackBST.RED ? 1 : 0;

        return redNodeCount + countRedNodes(node.left) + countRedNodes(node.right);
    }

    private void printResults(int treeSize, double percentageOfRedNodes) {
        StdOut.printf("%9d %15.2f\n", treeSize, percentageOfRedNodes);
    }

}
