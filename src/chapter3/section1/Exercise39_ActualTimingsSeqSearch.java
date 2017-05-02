package chapter3.section1;

import edu.princeton.cs.algs4.Stopwatch;
import util.FileUtil;
import util.VisualAccumulator;

/**
 * Created by rene on 01/05/17.
 */
public class Exercise39_ActualTimingsSeqSearch {

    private static final String TALE_FILE_PATH = "/Users/rene/Desktop/Algorithms/Books/Algorithms, 4th ed. - Exercises/Data/tale_of_two_cities.txt";

    public static void main(String[] args) {
        String[] wordsInTale = FileUtil.getAllStringsFromFile(TALE_FILE_PATH);
        int minLength = 8; //Same as the book analysis
        new Exercise39_ActualTimingsSeqSearch().frequencyCounter(wordsInTale, minLength);
    }

    private String frequencyCounter(String[] words, int minLength) {

        String title = "SequentialSearchST running time calling get() or put() in FrequencyCounter";
        String xAxisLabel = "calls to get() or put()";
        String yAxisLabel = "running time";
        double maxNumberOfOperations = 45000;
        double maxRunningTime = 3000;
        int originValue = 0;

        VisualAccumulator visualAccumulator = new VisualAccumulator(originValue, maxNumberOfOperations, maxRunningTime, title,
                xAxisLabel, yAxisLabel);
        SequentialSearchSymbolTable<String, Integer> sequentialSearchSymbolTable = new SequentialSearchSymbolTable<>();

        double totalRunningTime = 0;
        Stopwatch timer;

        for(String word : words) {

            if(word.length() < minLength) {
                continue;
            }

            if(!sequentialSearchSymbolTable.contains(word)) {
                timer = new Stopwatch();
                sequentialSearchSymbolTable.put(word, 1);
                totalRunningTime += timer.elapsedTime() * 1000;
                visualAccumulator.addDataValue(totalRunningTime, false);
            } else {
                timer = new Stopwatch();
                int wordFrequency = sequentialSearchSymbolTable.get(word);
                totalRunningTime += timer.elapsedTime() * 1000;
                visualAccumulator.addDataValue(totalRunningTime, false);

                timer = new Stopwatch();
                sequentialSearchSymbolTable.put(word, wordFrequency + 1);
                totalRunningTime += timer.elapsedTime() * 1000;
                visualAccumulator.addDataValue(totalRunningTime, false);
            }
        }

        String max = "";
        timer = new Stopwatch();
        sequentialSearchSymbolTable.put(max, 0);
        totalRunningTime += timer.elapsedTime() * 1000;
        visualAccumulator.addDataValue(totalRunningTime, false);

        for(String word : sequentialSearchSymbolTable.keys()) {
            timer = new Stopwatch();
            int wordFrequency = sequentialSearchSymbolTable.get(word);
            totalRunningTime += timer.elapsedTime() * 1000;
            visualAccumulator.addDataValue(totalRunningTime, false);

            timer = new Stopwatch();
            int maxWordFrequency = sequentialSearchSymbolTable.get(max);
            totalRunningTime += timer.elapsedTime() * 1000;
            visualAccumulator.addDataValue(totalRunningTime, false);

            if(wordFrequency > maxWordFrequency) {
                max = word;
            }
        }

        timer = new Stopwatch();
        int maxFrequency = sequentialSearchSymbolTable.get(max);
        totalRunningTime += timer.elapsedTime() * 1000;
        visualAccumulator.addDataValue(totalRunningTime, false);

        visualAccumulator.writeLastComputedValue();

        return max + " " + maxFrequency;
    }

}
