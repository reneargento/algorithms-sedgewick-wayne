package chapter3.section4;

import util.Constants;
import util.FileUtil;
import util.VisualAccumulator;

/**
 * Created by Rene Argento on 29/07/17.
 */
public class Exercise40_PlotsLinearProbing {

    private class LinearProbingHashTableCost<Key, Value> extends LinearProbingHashTable<Key, Value> {

        LinearProbingHashTableCost(int size) {
            super(size);
        }

        private int costOfPutCompares;

        public int putAndComputeCost(Key key, Value value, boolean resetCostOfPutCompares) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return 0;
            }

            if (resetCostOfPutCompares) {
                costOfPutCompares = 0;
            }

            if (keysSize >= size / (double) 2) {
                resize(size * 2);
                lgM++;
            }

            costOfPutCompares++;

            int tableIndex;
            for(tableIndex = hash(key); keys[tableIndex] != null; tableIndex = (tableIndex + 1) % size) {
                costOfPutCompares++;

                if (keys[tableIndex].equals(key)) {
                    values[tableIndex] = value;
                    return costOfPutCompares;
                }
            }

            keys[tableIndex] = key;
            values[tableIndex] = value;
            keysSize++;

            return costOfPutCompares;
        }

        private void resize(int newSize) {
            LinearProbingHashTableCost<Key, Value> tempHashTable = new LinearProbingHashTableCost<>(newSize);

            for(int i = 0; i < size; i++) {
                if (keys[i] != null) {
                    tempHashTable.putAndComputeCost(keys[i], values[i], false);
                }
            }

            keys = tempHashTable.keys;
            values = tempHashTable.values;
            size = tempHashTable.size;

            costOfPutCompares += tempHashTable.costOfPutCompares;
        }

    }

    private static final String TALE_FILE_PATH = Constants.FILES_PATH + Constants.TALE_OF_TWO_CITIES_FILE;

    public static void main(String[] args) {
        String[] wordsInTale = FileUtil.getAllStringsFromFile(TALE_FILE_PATH);
        int minLength = 8; //Same as the book analysis
        new Exercise40_PlotsLinearProbing().frequencyCounter(wordsInTale, minLength);
    }

    private String frequencyCounter(String[] words, int minLength) {

        String title = "Linear-probing hash table costs using put() in FrequencyCounter";
        String xAxisLabel = "operations";
        String yAxisLabel = "equality tests";
        double maxNumberOfOperations = 18000;
        double maxCost = 25;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxCost, title,
                xAxisLabel, yAxisLabel);
        LinearProbingHashTableCost<String, Integer> linearProbingHashTableCost =
                new LinearProbingHashTableCost<>(10);

        for(String word : words) {

            if (word.length() < minLength) {
                continue;
            }

            int cost;
            if (!linearProbingHashTableCost.contains(word)) {
                cost = linearProbingHashTableCost.putAndComputeCost(word, 1, true);
            } else {
                cost = linearProbingHashTableCost.
                        putAndComputeCost(word, linearProbingHashTableCost.get(word) + 1, true);
            }
            visualAccumulator.addDataValue(cost, true);
        }

        String max = "";
        int cost = linearProbingHashTableCost.putAndComputeCost(max, 0, true);
        visualAccumulator.addDataValue(cost, true);

        for(String word : linearProbingHashTableCost.keys()) {
            if (linearProbingHashTableCost.get(word) > linearProbingHashTableCost.get(max)) {
                max = word;
            }
        }

        visualAccumulator.writeExactFinalMean();

        return max + " " + linearProbingHashTableCost.get(max);
    }

}
