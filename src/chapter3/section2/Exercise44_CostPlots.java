package chapter3.section2;

import util.Constants;
import util.FileUtil;
import util.VisualAccumulator;

/**
 * Created by Rene Argento on 12/06/17.
 */
public class Exercise44_CostPlots {

    private class BinarySearchTreeCostPlots<Key extends Comparable<Key>, Value> extends BinarySearchTree<Key, Value> {

        private int cost;

        public int putAndComputeCost(Key key, Value value) {
            cost = 0;
            root = put(root, key, value);
            return cost;
        }

        private Node put(Node node, Key key, Value value) {
            if (node == null) {
                return new Node(key, value, 1);
            }

            int compare = key.compareTo(node.key);
            cost++;

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
    }

    private static final String TALE_FILE_PATH = Constants.FILES_PATH + Constants.TALE_OF_TWO_CITIES_FILE;

    public static void main(String[] args) {
        String[] wordsInTale = FileUtil.getAllStringsFromFile(TALE_FILE_PATH);
        int minLength = 8; //Same as the book analysis
        new Exercise44_CostPlots().frequencyCounter(wordsInTale, minLength);
    }

    private String frequencyCounter(String[] words, int minLength) {

        String title = "BST costs using put() in FrequencyCounter";
        String xAxisLabel = "operations";
        String yAxisLabel = "cost";
        double maxNumberOfOperations = 18000;
        double maxCost = 50;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);
        BinarySearchTreeCostPlots<String, Integer> binarySearchTree = new BinarySearchTreeCostPlots<>();

        for(String word : words) {

            if (word.length() < minLength) {
                continue;
            }

            int cost;
            if (!binarySearchTree.contains(word)) {
                cost = binarySearchTree.putAndComputeCost(word, 1);
            } else {
                cost = binarySearchTree.putAndComputeCost(word, binarySearchTree.get(word) + 1);
            }
            visualAccumulator.addDataValue(cost, true);
        }

        String max = "";
        int cost = binarySearchTree.putAndComputeCost(max, 0);
        visualAccumulator.addDataValue(cost, true);

        for(String word : binarySearchTree.keys()) {
            if (binarySearchTree.get(word) > binarySearchTree.get(max)) {
                max = word;
            }
        }

        visualAccumulator.writeFinalMean();

        return max + " " + binarySearchTree.get(max);
    }

}
