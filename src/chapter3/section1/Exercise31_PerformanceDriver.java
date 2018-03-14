package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 28/04/17.
 */
public class Exercise31_PerformanceDriver {

    private static final int ARRAY_SIZE = 100000;
    private static final int MIN_STRING_LENGTH = 2;
    private static final int MAX_STRING_LENGTH = 50;
    private static final int NUMBER_OF_EXPERIMENTS = 5;

    private static final String SEARCH_MISS_KEY = "#ThisValueIsNotInTheTable";

    public static void main(String[] args) {
        Map<Integer, String[]> allInputArrays = new HashMap<>();
        for(int i = 0; i < NUMBER_OF_EXPERIMENTS; i++) {
            String[] array = ArrayGenerator.generateRandomStringArray(ARRAY_SIZE, MIN_STRING_LENGTH, MAX_STRING_LENGTH);
            allInputArrays.put(i, array);
        }

        Exercise31_PerformanceDriver performanceDriver = new Exercise31_PerformanceDriver();
        performanceDriver.doExperiment(allInputArrays);
    }

    private void doExperiment(Map<Integer, String[]> allInputArrays) {

        double totalRunningTime = 0;

        StdOut.printf("%20s %10s %10s\n", "Number of Experiments | ","Array Size | ", "AVG Time Taken");

        for(int experiment = 1; experiment <= NUMBER_OF_EXPERIMENTS; experiment++) {
            BinarySearchSymbolTable<String, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

            String[] array = allInputArrays.get(experiment - 1);

            Stopwatch timer = new Stopwatch();

            for(String key : array) {
                int randomValue = StdRandom.uniform(0, 2);
                binarySearchSymbolTable.put(key, randomValue);
            }

            for(String key : binarySearchSymbolTable.keys()) {

                //Each key is hit an average of ten times and there is about the same number of misses
                for(int search = 0; search < 20; search++) {
                    int randomSearchChoice = StdRandom.uniform(0, 2);
                    if (randomSearchChoice == 0) {
                        binarySearchSymbolTable.get(SEARCH_MISS_KEY);
                    } else {
                        binarySearchSymbolTable.get(key);
                    }
                }
            }

            double currentRunningTime = timer.elapsedTime();
            totalRunningTime += currentRunningTime;

            double averageRunningTime = totalRunningTime / experiment;

            printResults(experiment, array.length, averageRunningTime);
        }
    }

    private void printResults(int experiment, int arraySize, double timeTaken) {
        StdOut.printf("%21d %13d %17.2f\n", experiment, arraySize, timeTaken);
    }

}
