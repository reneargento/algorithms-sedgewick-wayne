package chapter2.section3;

import chapter2.section1.InsertionSort;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayUtil;
import util.ArrayGenerator;

import java.util.Map;

/**
 * Created by Rene Argento on 12/03/17.
 */
public class Exercise27_IgnoreSmallSubarrays {

    //Based on the best results achieved on the experiments of exercise 3.2.25
    private static final int CUTOFF_SIZE = 28;

    // Parameters example: 8 131072
    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);
        int initialArraySize = Integer.parseInt(args[1]);

        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize, 2);

        doExperiment(numberOfExperiments, initialArraySize, allInputArrays);
    }

    private static void doExperiment(int numberOfExperiments, int initialArraySize, Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %25s %35s\n", "Array Size | ", "QuickSort W/ Cutoff Running Time |", "QuickSort Ignoring Small SubArrays");

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            //QuickSort with cutoff for small subarrays
            Stopwatch quickSortWithCutoffTimer = new Stopwatch();
            QuickSortWithCutoff.quickSortWithCutoff(originalArray, CUTOFF_SIZE);
            double quickSortWithCutoffRunningTime = quickSortWithCutoffTimer.elapsedTime();

            //QuickSort ignoring small sub arrays
            Stopwatch quickSortIgnoringSmallSubArraysTimer = new Stopwatch();
            quickSortIgnoringSmallSubArrays(array);
            double quickSortIgnoringSmallSubArraysRunningTime = quickSortIgnoringSmallSubArraysTimer.elapsedTime();

            printResults(arraySize, quickSortWithCutoffRunningTime, quickSortIgnoringSmallSubArraysRunningTime);

            arraySize *= 2;
        }
    }

    private static void quickSortIgnoringSmallSubArrays(Comparable[] array) {
        StdRandom.shuffle(array);
        quickSort(array, 0, array.length - 1);

        InsertionSort.insertionSort(array);
    }

    private static void quickSort(Comparable[] array, int low, int high) {

        if (low >= high) {
            return;
        }

        int subArraySize = high - low + 1;

        //Ignoring small sub arrays
        if (subArraySize < CUTOFF_SIZE) {
            return;
        }

        int partition = partition(array, low, high);
        quickSort(array, low, partition - 1);
        quickSort(array, partition + 1, high);
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

    private static void printResults(int arraySize, double quickSortWithCutoffRunningTime, double quickSortIgnoringSmallSubArraysRunningTime) {
        StdOut.printf("%10d %35.1f %37.1f\n", arraySize, quickSortWithCutoffRunningTime, quickSortIgnoringSmallSubArraysRunningTime);
    }

}
