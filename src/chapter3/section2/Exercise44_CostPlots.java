package chapter3.section2;

import util.FileUtil;
import util.VisualAccumulator;

/**
 * Created by rene on 12/06/17.
 */
public class Exercise44_CostPlots {

    private class BinarySearchTreeCostPlots<Key extends Comparable<Key>, Value> extends BinarySearchTree<Key, Value> {

        private int numberOfArrayAccesses;

        public int putAndComputeCost(Key key, Value value) {
            numberOfArrayAccesses = 0;
            root = put(root, key, value);
            return numberOfArrayAccesses;
        }

        private Node put(Node node, Key key, Value value) {
            if(node == null) {
                numberOfArrayAccesses++;
                return new Node(key, value, 1);
            }

            int compare = key.compareTo(node.key);
            numberOfArrayAccesses += 2;

            numberOfArrayAccesses++;
            if(compare < 0) {
                node.left = put(node.left, key, value);
            } else if(compare > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }
    }

    private static final String TALE_FILE_PATH = "/Users/rene/Desktop/Algorithms/Books/Algorithms, 4th ed. - Exercises/Data/tale_of_two_cities.txt";

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
        double maxCost = 110;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);
        BinarySearchTreeCostPlots<String, Integer> binarySearchTree = new BinarySearchTreeCostPlots<>();

        for(String word : words) {

            if(word.length() < minLength) {
                continue;
            }

            int numberOfArrayAccesses;
            if(!binarySearchTree.contains(word)) {
                numberOfArrayAccesses = binarySearchTree.putAndComputeCost(word, 1);
            } else {
                numberOfArrayAccesses = binarySearchTree.putAndComputeCost(word, binarySearchTree.get(word) + 1);
            }
            visualAccumulator.addDataValue(numberOfArrayAccesses, true);
        }

        String max = "";
        int numberOfArrayAccesses = binarySearchTree.putAndComputeCost(max, 0);
        visualAccumulator.addDataValue(numberOfArrayAccesses, true);

        for(String word : binarySearchTree.keys()) {
            if(binarySearchTree.get(word) > binarySearchTree.get(max)) {
                max = word;
            }
        }

        visualAccumulator.writeFinalMean();

        return max + " " + binarySearchTree.get(max);
    }

}
