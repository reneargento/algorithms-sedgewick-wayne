package chapter3.section3;

import util.Constants;
import util.FileUtil;
import util.VisualAccumulator;

/**
 * Created by Rene Argento on 08/07/17.
 */
public class Exercise46_Height {

    private int bstHeight(RedBlackBST<String, Integer> redBlackBST) {
        //Minus 1 because the root has height 0
        return bstHeight(redBlackBST.root) - 1;
    }

    private int bstHeight(RedBlackBST.Node node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = bstHeight(node.left);
        int rightHeight = bstHeight(node.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }

    private static final String TALE_FILE_PATH = Constants.FILES_PATH + Constants.TALE_OF_TWO_CITIES_FILE;

    public static void main(String[] args) {
        String[] wordsInTale = FileUtil.getAllStringsFromFile(TALE_FILE_PATH);
        int minLength = 8; //Same as the book analysis
        new Exercise46_Height().frequencyCounter(wordsInTale, minLength);
    }

    private String frequencyCounter(String[] words, int minLength) {

        String title = "Red-black BST height using put() in FrequencyCounter";
        String xAxisLabel = "operations";
        String yAxisLabel = "height";
        double maxNumberOfOperations = 18000;
        double maxCost = 20;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);
        RedBlackBST<String, Integer> redBlackBST = new RedBlackBST<>();

        for(String word : words) {

            if (word.length() < minLength) {
                continue;
            }

            if (!redBlackBST.contains(word)) {
                redBlackBST.put(word, 1);
            } else {
                redBlackBST.put(word, redBlackBST.get(word) + 1);
            }
            int height = bstHeight(redBlackBST);
            visualAccumulator.addDataValue(height, true);
        }

        String max = "";
        redBlackBST.put(max, 0);
        int height = bstHeight(redBlackBST);
        visualAccumulator.addDataValue(height, true);

        for(String word : redBlackBST.keys()) {
            if (redBlackBST.get(word) > redBlackBST.get(max)) {
                max = word;
            }
        }

        visualAccumulator.writeFinalMean();

        return max + " " + redBlackBST.get(max);
    }

}
