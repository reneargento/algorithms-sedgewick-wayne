package chapter2.section3;

import chapter2.section1.InsertionSort;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.ArrayUtil;
import util.ArrayGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 12/03/17.
 */
public class Exercise28_RecursionDepth {

    private static final int NUMBER_OF_EXPERIMENTS = 4;

    private static int numberOfRecursiveCalls = 0;
    private static int totalRecursionDepth = 0;

    public static void main(String[] args) {
        Map<Integer, Comparable[]> allInputArrays = new HashMap<>();

        int arraySize = 1000;

        for(int i = 0; i < NUMBER_OF_EXPERIMENTS; i++) {
            Comparable[] array = ArrayGenerator.generateDistinctValuesShuffledArray(arraySize);
            allInputArrays.put(i, array);

            arraySize *= 10;
        }

        doExperiment(allInputArrays);
    }

    private static void doExperiment(Map<Integer, Comparable[]> allInputArrays) {

        int[] cutoffSizes = {10, 20, 50};

        StdOut.printf("%13s %13s %23s\n", "Cutoff Size | ", "Array Size | ", "Average Recursive Depth");

        for(int cutoffSize : cutoffSizes) {

            for(int i = 0; i < NUMBER_OF_EXPERIMENTS; i++) {

                Comparable[] originalArray = allInputArrays.get(i);
                Comparable[] array = new Comparable[originalArray.length];
                System.arraycopy(originalArray, 0, array, 0, originalArray.length);

                quickSortWithCutoff(array, cutoffSize);
                int averageRecursionDepth = totalRecursionDepth / numberOfRecursiveCalls;

                printResults(cutoffSize, originalArray.length, averageRecursionDepth);

                numberOfRecursiveCalls = 0;
                totalRecursionDepth = 0;
            }
        }
    }

    private static void quickSortWithCutoff(Comparable[] array, int cutoffSize) {
        StdRandom.shuffle(array);
        quickSort(array, 0, array.length - 1, cutoffSize, 0);
    }

    private static void quickSort(Comparable[] array, int low, int high, int cutoffSize, int recursionDepth) {

        numberOfRecursiveCalls++;
        totalRecursionDepth += recursionDepth;

        if (low >= high) {
            return;
        }

        int subArraySize = high - low + 1;

        if (subArraySize < cutoffSize) {
            InsertionSort.insertionSort(array, low, high);
            return;
        }

        int partition = partition(array, low, high);

        int newRecursionDepth = recursionDepth + 1;
        quickSort(array, low, partition - 1, cutoffSize, newRecursionDepth);
        quickSort(array, partition + 1, high, cutoffSize, newRecursionDepth);
    }

    private static int partition(Comparable[] array, int low, int high) {
        Comparable pivot = array[low];

        int i = low;
        int j = high + 1;

        while(true) {
            while (ArrayUtil.less(array[++i], pivot)) {
                if (i == high) {
                    break;
                }
            }

            while(ArrayUtil.less(pivot, array[--j])) {
                if (j == low) {
                    break;
                }
            }

            if (i >= j) {
                break;
            }

            ArrayUtil.exchange(array, i, j);
        }

        //Place pivot in the right place
        ArrayUtil.exchange(array, low, j);
        return j;
    }

    private static void printResults(int cutoffSize, int arraySize, int averageRecursionDepth) {
        StdOut.printf("%11d %13d %26d\n", cutoffSize, arraySize, averageRecursionDepth);
    }

}
