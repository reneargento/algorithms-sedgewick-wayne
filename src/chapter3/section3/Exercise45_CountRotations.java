package chapter3.section3;

import util.Constants;
import util.FileUtil;
import util.VisualAccumulator;

/**
 * Created by Rene Argento on 08/07/17.
 */
public class Exercise45_CountRotations {

    private class RedBlackBSTCostPlots<Key extends Comparable<Key>, Value> extends RedBlackBST<Key, Value> {

        private int cost;

        private int putAndComputeCost(Key key, Value value) {
            if (key == null) {
                return 0;
            }

            if (value == null) {
                delete(key);
                return 0;
            }

            cost = 0;

            root = put(root, key, value);
            root.color = BLACK;

            return cost;
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
                cost++;
                node = rotateLeft(node);
            }
            if (isRed(node.left) && isRed(node.left.left)) {
                cost++;
                node = rotateRight(node);
            }
            if (isRed(node.left) && isRed(node.right)) {
                cost++;
                flipColors(node);
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }
    }

    private static final String TALE_FILE_PATH = Constants.FILES_PATH + Constants.TALE_OF_TWO_CITIES_FILE;

    public static void main(String[] args) {
        String[] wordsInTale = FileUtil.getAllStringsFromFile(TALE_FILE_PATH);
        int minLength = 8; //Same as the book analysis
        new Exercise45_CountRotations().frequencyCounter(wordsInTale, minLength);
    }

    private String frequencyCounter(String[] words, int minLength) {

        String title = "Red-black BST rotations and splits using put() in FrequencyCounter";
        String xAxisLabel = "operations";
        String yAxisLabel = "cost";
        double maxNumberOfOperations = 18000;
        double maxCost = 20;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);
        RedBlackBSTCostPlots<String, Integer> redBlackBSTCostPlots = new RedBlackBSTCostPlots<>();

        for(String word : words) {

            if (word.length() < minLength) {
                continue;
            }

            int cost;
            if (!redBlackBSTCostPlots.contains(word)) {
                cost = redBlackBSTCostPlots.putAndComputeCost(word, 1);
            } else {
                cost = redBlackBSTCostPlots.putAndComputeCost(word, redBlackBSTCostPlots.get(word) + 1);
            }
            visualAccumulator.addDataValue(cost, true);
        }

        String max = "";
        int cost = redBlackBSTCostPlots.putAndComputeCost(max, 0);
        visualAccumulator.addDataValue(cost, true);

        for(String word : redBlackBSTCostPlots.keys()) {
            if (redBlackBSTCostPlots.get(word) > redBlackBSTCostPlots.get(max)) {
                max = word;
            }
        }

        visualAccumulator.writeFinalMean();

        return max + " " + redBlackBSTCostPlots.get(max);
    }

}
