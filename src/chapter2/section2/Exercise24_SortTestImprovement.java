package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.Map;

/**
 * Created by Rene Argento on 19/02/17.
 */
public class Exercise24_SortTestImprovement {

    private enum TEST_TYPE {
        BASELINE_MERGESORT, SORT_TEST;
    }

    private int numberOfTimesSortTestSucceeded;

    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]); // 8
        int initialArraySize = Integer.parseInt(args[1]); // 131072

        Exercise24_SortTestImprovement sortTestImprovement = new Exercise24_SortTestImprovement();

        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize, 2);

        StdOut.println("BASELINE - TOP DOWN MERGESORT");
        StdOut.printf("%13s %12s\n", "Array Size | ", "Running Time");
        sortTestImprovement.doExperiment(numberOfExperiments, initialArraySize, allInputArrays, TEST_TYPE.BASELINE_MERGESORT);

        StdOut.println();

        StdOut.println("IMPROVEMENT - TEST IF ARRAY IS ALREADY SORTED TO AVOID MERGE");
        StdOut.printf("%13s %14s %36s\n", "Array Size | ", "Running Time |", " AVG NUMBER OF TIMES THE TEST SUCCEEDED (%)");
        sortTestImprovement.doExperiment(numberOfExperiments, initialArraySize, allInputArrays, TEST_TYPE.SORT_TEST);
    }

    private void doExperiment(int numberOfExperiments, int initialArraySize,
                              Map<Integer, Comparable[]> allInputArrays, TEST_TYPE testType) {

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            numberOfTimesSortTestSucceeded = 0;

            Stopwatch timer = new Stopwatch();

            switch (testType) {
                case BASELINE_MERGESORT: TopDownMergeSort.mergeSort(array);
                    break;
                case SORT_TEST:
                    mergeSortWithSortTest(array);
                    break;
            }

            double runningTime = timer.elapsedTime();

            if (testType == TEST_TYPE.BASELINE_MERGESORT) {
                printResults(arraySize, runningTime);
            } else if (testType == TEST_TYPE.SORT_TEST) {
                double avgTimesTestSucceeded = ((double) numberOfTimesSortTestSucceeded) / ((double) arraySize) * 100;

                printSortTestResults(arraySize, runningTime, avgTimesTestSucceeded);
            }

            arraySize *= 2;
        }
    }

    private void mergeSortWithSortTest(Comparable[] array) {
        Comparable[] aux = new Comparable[array.length];

        sort(array, aux, 0, array.length - 1);
    }

    @SuppressWarnings("unchecked")
    private void sort(Comparable[] array, Comparable[] aux, int low, int high) {

        if (high <= low) {
            return;
        }

        int middle = low + (high - low) / 2;

        sort(array, aux, low, middle);
        sort(array, aux, middle + 1, high);

        //Improvement - Skip merge if the array is already in order
        if (array[middle].compareTo(array[middle + 1]) <= 0) {
            numberOfTimesSortTestSucceeded++;
            return;
        }

        merge(array, aux, low, middle, high);
    }

    @SuppressWarnings("unchecked")
    private void merge(Comparable[] array, Comparable[] aux, int low, int middle, int high) {
        for(int i = low; i <= high; i++) {
            aux[i] = array[i];
        }

        int indexLeft = low;
        int indexRight = middle + 1;
        int arrayIndex = low;

        while (indexLeft <= middle && indexRight <= high) {
            if (aux[indexLeft].compareTo(aux[indexRight]) <= 0) {
                array[arrayIndex] = aux[indexLeft];
                indexLeft++;
            } else {
                array[arrayIndex] = aux[indexRight];
                indexRight++;
            }
            arrayIndex++;
        }

        while (indexLeft <= middle) {
            array[arrayIndex] = aux[indexLeft];

            indexLeft++;
            arrayIndex++;
        }
    }

    private void printResults(int arraySize, double runningTime) {
        StdOut.printf("%10d %15.1f\n", arraySize, runningTime);
    }

    private void printSortTestResults(int arraySize, double runningTime, double avgTimesTestSucceeded) {
        StdOut.printf("%10d %15.1f %23.1f\n", arraySize, runningTime, avgTimesTestSucceeded);
    }
}
