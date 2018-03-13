package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 19/02/17.
 */
public class Exercise23_Improvements {

    private enum TEST_TYPE {
        BASELINE_MERGESORT, IMPROVEMENT_SKIP_SORTED, IMPROVEMENT_AVOID_COPY, FASTER_MERGE;
    }

    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]); // 8
        int initialArraySize = Integer.parseInt(args[1]); // 131072

        Map<Integer, Comparable[]> allInputArrays = generateAllArrays(numberOfExperiments, initialArraySize);

        StdOut.println("BASELINE - TOP DOWN MERGESORT");
        doExperiment(numberOfExperiments, initialArraySize, allInputArrays, TEST_TYPE.BASELINE_MERGESORT);

        StdOut.println();

        StdOut.println("IMPROVEMENT #1 - CUTOFF FOR SMALL SUBARRAYS");
        testCutoffImprovement(numberOfExperiments, initialArraySize, allInputArrays);

        StdOut.println();

        StdOut.println("IMPROVEMENT #2 - TEST IF ARRAY IS ALREADY SORTED TO AVOID MERGE");
        doExperiment(numberOfExperiments, initialArraySize, allInputArrays, TEST_TYPE.IMPROVEMENT_SKIP_SORTED);

        StdOut.println();

        StdOut.println("IMPROVEMENT #3 - AVOID ARRAY COPY BY SWITCHING ARGUMENTS");
        doExperiment(numberOfExperiments, initialArraySize, allInputArrays, TEST_TYPE.IMPROVEMENT_AVOID_COPY);

        StdOut.println();

        StdOut.println("COPY SECOND HALF OF SUBARRAY IN DECREASING ORDER DURING MERGE");
        doExperiment(numberOfExperiments, initialArraySize, allInputArrays, TEST_TYPE.FASTER_MERGE);
    }

    private static void doExperiment(int numberOfExperiments, int initialArraySize,
                                     Map<Integer, Comparable[]> allInputArrays, TEST_TYPE testType) {

        StdOut.printf("%13s %12s\n", "Array Size | ", "Running Time");

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            Stopwatch timer = new Stopwatch();

            switch (testType) {
                case BASELINE_MERGESORT: TopDownMergeSort.mergeSort(array);
                    break;
                case IMPROVEMENT_SKIP_SORTED: Exercise23_Improvements2_TestSorted.mergeSort(array);
                    break;
                case IMPROVEMENT_AVOID_COPY: Exercise23_Improvements3_AvoidCopy.mergeSort(array);
                    break;
                case FASTER_MERGE: Exercise10_FasterMerge.topDownMergeSort(array);
                    break;
            }

            double runningTime = timer.elapsedTime();

            printResults(arraySize, runningTime);
            arraySize *= 2;
        }
    }

    private static void testCutoffImprovement(int numberOfExperiments, int initialArraySize,
                                              Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%10s %13s %12s\n", "Cutoff | ", "Array Size | ", "Running Time");

        for(int cutoff = 5; cutoff <= 30; cutoff += 5) {
            int arraySize = initialArraySize;
            Exercise23_Improvements1_Cutoff improvements1 = new Exercise23_Improvements1_Cutoff(cutoff);

            for(int i = 0; i < numberOfExperiments; i++) {

                Comparable[] originalArray = allInputArrays.get(i);
                Comparable[] array = new Comparable[originalArray.length];
                System.arraycopy(originalArray, 0, array, 0, originalArray.length);

                Stopwatch timer = new Stopwatch();

                improvements1.mergeSort(array);

                double runningTime = timer.elapsedTime();

                printCutoffResults(cutoff, arraySize, runningTime);
                arraySize *= 2;
            }
        }
    }

    private static Map<Integer, Comparable[]> generateAllArrays(int numberOfExperiments, int initialArraySize) {

        Map<Integer, Comparable[]> allArrays = new HashMap<>();

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {
            Comparable[] array = generateArray(arraySize);
            allArrays.put(i, array);

            arraySize *= 2;
        }

        return allArrays;
    }

    private static Comparable[] generateArray(int length) {
        Comparable[] array = new Comparable[length];

        for(int i = 0; i < length; i++) {
            array[i] = StdRandom.uniform();
        }

        return array;
    }

    private static void printResults(int arraySize, double runningTime) {
        StdOut.printf("%8d %14.1f\n", arraySize, runningTime);
    }

    private static void printCutoffResults(int cutoff, int arraySize, double runningTime) {
        StdOut.printf("%8d %13d %15.1f\n", cutoff, arraySize, runningTime);
    }
}
