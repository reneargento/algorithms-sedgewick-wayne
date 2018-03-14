package chapter3.section1;

import edu.princeton.cs.algs4.Stopwatch;
import util.Constants;
import util.FileUtil;
import util.VisualAccumulator;

/**
 * Created by Rene Argento on 12/06/17.
 */
public class Exercise39_ActualTimings {

    private static final String TALE_FILE_PATH = Constants.FILES_PATH + Constants.TALE_OF_TWO_CITIES_FILE;

    public static void main(String[] args) {
        String[] wordsInTale = FileUtil.getAllStringsFromFile(TALE_FILE_PATH);
        int minLength = 8; //Same as the book analysis

        Exercise39_ActualTimings actualTimings = new Exercise39_ActualTimings();
        String title;

        //Sequential search symbol table
//        SequentialSearchSymbolTable<String, Integer> sequentialSearchSymbolTable = new SequentialSearchSymbolTable<>();
//        title = "SequentialSearchST running time calling get() or put() in FrequencyCounter";
//        actualTimings.frequencyCounter(sequentialSearchSymbolTable, wordsInTale, minLength, title);

        //Binary search symbol table
        BinarySearchSymbolTable<String, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();
        title = "BinarySearchST running time calling get() or put() in FrequencyCounter";
        actualTimings.frequencyCounter(binarySearchSymbolTable, wordsInTale, minLength, title);
    }

    private String frequencyCounter(SymbolTable<String, Integer> symbolTable, String[] words, int minLength, String title) {

        String xAxisLabel = "calls to get() or put()";
        String yAxisLabel = "running time";
        double maxNumberOfOperations = 45000;
        double maxRunningTime = 3000;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxRunningTime, title,
                xAxisLabel, yAxisLabel);

        double totalRunningTime = 0;
        Stopwatch timer;

        for(String word : words) {

            if (word.length() < minLength) {
                continue;
            }

            if (!symbolTable.contains(word)) {
                timer = new Stopwatch();
                symbolTable.put(word, 1);
                totalRunningTime += timer.elapsedTime() * 1000;
                visualAccumulator.addDataValue(totalRunningTime, false);
            } else {
                timer = new Stopwatch();
                int wordFrequency = symbolTable.get(word);
                totalRunningTime += timer.elapsedTime() * 1000;
                visualAccumulator.addDataValue(totalRunningTime, false);

                timer = new Stopwatch();
                symbolTable.put(word, wordFrequency + 1);
                totalRunningTime += timer.elapsedTime() * 1000;
                visualAccumulator.addDataValue(totalRunningTime, false);
            }
        }

        String max = "";
        timer = new Stopwatch();
        symbolTable.put(max, 0);
        totalRunningTime += timer.elapsedTime() * 1000;
        visualAccumulator.addDataValue(totalRunningTime, false);

        for(String word : symbolTable.keys()) {
            timer = new Stopwatch();
            int wordFrequency = symbolTable.get(word);
            totalRunningTime += timer.elapsedTime() * 1000;
            visualAccumulator.addDataValue(totalRunningTime, false);

            timer = new Stopwatch();
            int maxWordFrequency = symbolTable.get(max);
            totalRunningTime += timer.elapsedTime() * 1000;
            visualAccumulator.addDataValue(totalRunningTime, false);

            if (wordFrequency > maxWordFrequency) {
                max = word;
            }
        }

        timer = new Stopwatch();
        int maxFrequency = symbolTable.get(max);
        totalRunningTime += timer.elapsedTime() * 1000;
        visualAccumulator.addDataValue(totalRunningTime, false);

        visualAccumulator.writeLastComputedValue();

        return max + " " + maxFrequency;
    }

}
