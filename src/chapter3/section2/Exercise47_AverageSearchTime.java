package chapter3.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import util.VisualAccumulator;

/**
 * Created by Rene Argento on 13/06/17.
 */
public class Exercise47_AverageSearchTime {

    private class BinarySearchTreeInternalPathLength<Key extends Comparable<Key>, Value> extends BinarySearchTree<Key, Value> {

        private class Node {
            private Key key;
            private Value value;

            private Node left;
            private Node right;

            private int size; //# of nodes in subtree rooted here
            private int depth; //used only to compute the internal path length

            public Node(Key key, Value value, int size) {
                this.key = key;
                this.value = value;
                this.size = size;
            }
        }

        private Node root;

        public int size() {
            return size(root);
        }

        private int size(Node node) {
            if (node == null) {
                return 0;
            }

            return node.size;
        }

        public void put(Key key, Value value) {
            root = put(root, key, value);
        }

        private Node put(Node node, Key key, Value value) {
            if (node == null) {
                return new Node(key, value, 1);
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public int internalPathLength() {
            if (root == null) {
                return 0;
            }

            int internalPathLength = 0;

            Queue<Node> queue = new Queue<>();
            root.depth = 0;
            queue.enqueue(root);

            while (!queue.isEmpty()) {
                Node current = queue.dequeue();
                internalPathLength += current.depth;

                if (current.left != null) {
                    current.left.depth = current.depth + 1;
                    queue.enqueue(current.left);
                }
                if (current.right != null) {
                    current.right.depth = current.depth + 1;
                    queue.enqueue(current.right);
                }
            }

            return internalPathLength;
        }

        public double averagePathLength() {
            if (size() == 0) {
                return 0;
            }

            return (internalPathLength() / (double) size()) + 1;
        }
    }

    private void doExperiment() {
        String title = "Average path length to a random node in a BST built from random keys";
        String xAxisLabel = "number of keys N";
        String yAxisLabel = "compares";
        double maxNumberOfOperations = 10000;
        double maxCost = 20;
        int originValue = 100;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);

        double lastComputedAveragePathLength = 0;
        double lastExpectedAveragePathLength = -1;

        for(int size = originValue; size <= maxNumberOfOperations; size++) {
            int numberOfTrials = 1000;

            long totalAvgPathLengths = 0;

            for(int t = 0; t < numberOfTrials; t++) {
                BinarySearchTreeInternalPathLength<Integer, Integer> binarySearchTreeInternalPathLength =
                        new BinarySearchTreeInternalPathLength<>();

                for(int i = 0; i < size; i++) {
                    Integer randomKey = StdRandom.uniform(Integer.MAX_VALUE);
                    binarySearchTreeInternalPathLength.put(randomKey, randomKey);
                }

                double averagePathLength = binarySearchTreeInternalPathLength.averagePathLength();
                totalAvgPathLengths += averagePathLength;

                if (size % 200 == 0) {
                    visualAccumulator.drawDataValue(size, averagePathLength, StdDraw.GRAY);
                }
            }

            double averageOfAveragesPathLength = totalAvgPathLengths / (double) numberOfTrials;

            if (size % 200 == 0) {
                visualAccumulator.drawDataValue(size, averageOfAveragesPathLength, StdDraw.RED);

                //Draw the expected average path length -> 1.39 lg N - 1.85
                double expectedAveragePathLength = 1.39 * (Math.log(size) / Math.log(2)) - 1.85;
                visualAccumulator.drawDataValue(size, expectedAveragePathLength, StdDraw.BLACK);

                if (lastExpectedAveragePathLength != -1) {
                    StdDraw.line(size - 200, lastExpectedAveragePathLength, size, expectedAveragePathLength);
                }
                lastExpectedAveragePathLength = expectedAveragePathLength;
            }

            lastComputedAveragePathLength = averageOfAveragesPathLength;
        }

        double xCoordinate = maxNumberOfOperations + (maxNumberOfOperations * 0.02);
        String formattedLastComputedAveragePathLength = String.format("%.2f", lastComputedAveragePathLength);
        visualAccumulator.writeText(formattedLastComputedAveragePathLength, xCoordinate,
                lastComputedAveragePathLength, StdDraw.RED);
    }

    public static void main(String[] args) {
        new Exercise47_AverageSearchTime().doExperiment();
    }

}