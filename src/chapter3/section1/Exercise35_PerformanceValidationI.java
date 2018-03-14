package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.FileUtil;

/**
 * Created by Rene Argento on 30/04/17.
 */
public class Exercise35_PerformanceValidationI {

    public static void main(String[] args) {
        String filePath = args[0];
        String[] words = FileUtil.getAllStringsFromFile(filePath);
        int numberOfExperiments = 8;

        new Exercise35_PerformanceValidationI().doExperiments(numberOfExperiments, words);
    }

    private void doExperiments(int numberOfExperiments, String[] words) {

        int numberOfWords = 1000;
        int minLength = 4;

        StdOut.printf("%18s %15s %10s %15s\n", "Number of Words | ","Running Time | ", "Ratio | ", "Complexity (N^X)");

        //Previous time
        String[] currentWords = new String[numberOfWords / 2];
        System.arraycopy(words, 0, currentWords, 0, currentWords.length);

        Stopwatch initialTimer = new Stopwatch();
        frequencyCounter(currentWords, minLength);
        double previousRunningTime = initialTimer.elapsedTime();

        for(int i = 0; i < numberOfExperiments; i++) {

            currentWords = new String[numberOfWords];
            System.arraycopy(words, 0, currentWords, 0, currentWords.length);

            Stopwatch timer = new Stopwatch();
            frequencyCounter(currentWords, minLength);
            double runningTime = timer.elapsedTime();

            double ratio = runningTime / previousRunningTime;

            double lgRatio = Math.log10(ratio) / Math.log10(2);
            printResults(numberOfWords, runningTime, ratio, lgRatio);

            previousRunningTime = runningTime;
            numberOfWords *= 2;
        }
    }

    private String frequencyCounter(String[] words, int minLength) {

        SequentialSearchSymbolTable<String, Integer> sequentialSearchSymbolTable = new SequentialSearchSymbolTable<>();

        for(String word : words) {

            if (word.length() < minLength) {
                continue;
            }

            if (!sequentialSearchSymbolTable.contains(word)) {
                sequentialSearchSymbolTable.put(word, 1);
            } else {
                sequentialSearchSymbolTable.put(word, sequentialSearchSymbolTable.get(word) + 1);
            }
        }

        String max = "";
        sequentialSearchSymbolTable.put(max, 0);

        for(String word : sequentialSearchSymbolTable.keys()) {
            if (sequentialSearchSymbolTable.get(word) > sequentialSearchSymbolTable.get(max)) {
                max = word;
            }
        }

        return max + " " + sequentialSearchSymbolTable.get(max);
    }

    private void printResults(int numberOfWords, double runningTime, double ratio, double lgRatio) {
        StdOut.printf("%15d %15.2f %10.2f %19.2f\n", numberOfWords, runningTime, ratio, lgRatio);
    }

}
