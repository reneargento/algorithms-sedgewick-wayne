package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 11/03/17.
 */
public class Exercise25_CutoffToInsertionSort {

    private static final int NUMBER_OF_EXPERIMENTS = 4;

    public static void main(String[] args) {
        Map<Integer, Comparable[]> allInputArrays = new HashMap<>();

        int arraySize = 1000;

        for(int i = 0; i < NUMBER_OF_EXPERIMENTS; i++) {
            Comparable[] array = ArrayGenerator.generateRandomArray(arraySize);
            allInputArrays.put(i, array);

            arraySize *= 10;
        }

        doExperiment(allInputArrays);
    }

    private static void doExperiment(Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %23s\n", "Cutoff Size | ", "Average Running Time");

        for(int cutoffSize = 0; cutoffSize <= 30; cutoffSize++) {
            double totalRunningTime = 0;

            for(int i = 0; i < NUMBER_OF_EXPERIMENTS; i++) {

                Comparable[] originalArray = allInputArrays.get(i);
                Comparable[] array = new Comparable[originalArray.length];
                System.arraycopy(originalArray, 0, array, 0, originalArray.length);

                Stopwatch timer = new Stopwatch();

                QuickSortWithCutoff.quickSortWithCutoff(array, cutoffSize);

                totalRunningTime += timer.elapsedTime();
            }

            printResults(cutoffSize, totalRunningTime / NUMBER_OF_EXPERIMENTS);
        }
    }

    private static void printResults(int cutoffSize, double averageRunningTime) {
        StdOut.printf("%11d %26.5f\n", cutoffSize, averageRunningTime);
    }

}
