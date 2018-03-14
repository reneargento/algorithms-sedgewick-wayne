package chapter3.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import util.VisualAccumulator;

/**
 * Created by Rene Argento on 08/07/17.
 */
public class Exercise44_AverageSearchTime {

    private class RedBlackBSTInternalPathLength<Key extends Comparable<Key>, Value> extends RedBlackBST<Key, Value> {

        private class Node {
            Key key;
            Value value;
            Node left, right;

            boolean color;
            int size;
            private int depth; //used only to compute the internal path length

            Node(Key key, Value value, int size, boolean color) {
                this.key = key;
                this.value = value;

                this.size = size;
                this.color = color;
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

        private boolean isRed(Node node) {
            if (node == null) {
                return false;
            }

            return node.color == RED;
        }

        private Node rotateLeft(Node node) {
            if (node == null || node.right == null) {
                return node;
            }

            Node newRoot = node.right;

            node.right = newRoot.left;
            newRoot.left = node;

            newRoot.color = node.color;
            node.color = RED;

            newRoot.size = node.size;
            node.size = size(node.left) + 1 + size(node.right);

            return newRoot;
        }

        private Node rotateRight(Node node) {
            if (node == null || node.left == null) {
                return node;
            }

            Node newRoot = node.left;

            node.left = newRoot.right;
            newRoot.right = node;

            newRoot.color = node.color;
            node.color = RED;

            newRoot.size = node.size;
            node.size = size(node.left) + 1 + size(node.right);

            return newRoot;
        }

        private void flipColors(Node node) {
            if (node == null || node.left == null || node.right == null) {
                return;
            }

            //The root must have opposite color of its two children
            if ((isRed(node) && !isRed(node.left) && !isRed(node.right))
                    || (!isRed(node) && isRed(node.left) && isRed(node.right))) {
                node.color = !node.color;
                node.left.color = !node.left.color;
                node.right.color = !node.right.color;
            }
        }

        public void put(Key key, Value value) {
            if (key == null) {
                return;
            }

            if (value == null) {
                delete(key);
                return;
            }

            root = put(root, key, value);
            root.color = BLACK;
        }

        private Node put(Node node, Key key, Value value) {
            if (node == null) {
                return new Node(key, value, 1, RED);
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }

            if (isRed(node.right) && !isRed(node.left)) {
                node = rotateLeft(node);
            }
            if (isRed(node.left) && isRed(node.left.left)) {
                node = rotateRight(node);
            }
            if (isRed(node.left) && isRed(node.right)) {
                flipColors(node);
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        private int internalPathLength() {
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

        private double averagePathLength() {
            if (size() == 0) {
                return 0;
            }

            return (internalPathLength() / (double) size()) + 1;
        }
    }

    private void doExperiment() {
        String title = "Average path length to a random node in a red-black BST built from random keys";
        String xAxisLabel = "number of keys N";
        String yAxisLabel = "compares";
        double maxNumberOfOperations = 10000;
        double maxCost = 20;
        int originValue = 1;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);

        double lastComputedAveragePathLength = 0;
        double lastExpectedAveragePathLength = -1;

        for(int size = originValue; size <= maxNumberOfOperations; size++) {
            int numberOfTrials = 1000;

            long totalAvgPathLengths = 0;

            for(int t=0; t < numberOfTrials; t++) {
                RedBlackBSTInternalPathLength<Integer, Integer> redBlackBSTInternalPathLength =
                        new RedBlackBSTInternalPathLength<>();

                for(int i = 0; i < size; i++) {
                    Integer randomKey = StdRandom.uniform(Integer.MAX_VALUE);
                    redBlackBSTInternalPathLength.put(randomKey, randomKey);
                }

                double averagePathLength = redBlackBSTInternalPathLength.averagePathLength();
                totalAvgPathLengths += averagePathLength;

                if (size % 200 == 0) {
                    visualAccumulator.drawDataValue(size, averagePathLength, StdDraw.GRAY);
                }
            }

            double averageOfAveragesPathLength = totalAvgPathLengths / (double) numberOfTrials;

            if (size % 200 == 0) {
                visualAccumulator.drawDataValue(size, averageOfAveragesPathLength, StdDraw.RED);

                //Draw the expected average path length -> lg N - .5
                double expectedAveragePathLength = (Math.log(size) / Math.log(2)) - 0.5;
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
        new Exercise44_AverageSearchTime().doExperiment();
    }

}
