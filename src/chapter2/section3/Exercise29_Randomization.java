package chapter2.section3;

import chapter2.section1.InsertionSort;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayUtil;
import util.ArrayGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 12/03/17.
 */
public class Exercise29_Randomization {

    private static final int NUMBER_OF_EXPERIMENTS = 4;

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

        StdOut.printf("%13s %13s %30s %23s\n", "Cutoff Size | ", "Array Size | ", "QuickSort W/ Randomized Array Running Time |",
                "QuickSort W/ Random Pivot");

        for(int cutoffSize : cutoffSizes) {
            for(int i = 0; i < NUMBER_OF_EXPERIMENTS; i++) {

                Comparable[] originalArray = allInputArrays.get(i);
                Comparable[] arrayCopy1 = new Comparable[originalArray.length];
                System.arraycopy(originalArray, 0, arrayCopy1, 0, originalArray.length);
                Comparable[] arrayCopy2 = new Comparable[originalArray.length];
                System.arraycopy(originalArray, 0, arrayCopy2, 0, originalArray.length);

                //QuickSort with cutoff and randomized array
                Stopwatch quickSortWithRandomizedArrayTimer = new Stopwatch();
                QuickSortWithCutoff.quickSortWithCutoff(arrayCopy1, cutoffSize);
                double quickSortWithRandomizedArrayRunningTime = quickSortWithRandomizedArrayTimer.elapsedTime();

                //QuickSort with cutoff and choosing a random pivot
                Stopwatch quickSortWithRandomPivotTimer = new Stopwatch();
                quickSortWithCutoffAndRandomPivot(arrayCopy2, cutoffSize);
                double quickSortWithRandomPivotRunningTime = quickSortWithRandomPivotTimer.elapsedTime();

                printResults(cutoffSize, originalArray.length, quickSortWithRandomizedArrayRunningTime,
                        quickSortWithRandomPivotRunningTime);
            }
        }
    }

    private static void quickSortWithCutoffAndRandomPivot(Comparable[] array, int cutoffSize) {
        quickSort(array, 0, array.length - 1, cutoffSize);
    }

    private static void quickSort(Comparable[] array, int low, int high, int cutoffSize) {

        if (low >= high) {
            return;
        }

        int subArraySize = high - low + 1;

        if (subArraySize < cutoffSize) {
            InsertionSort.insertionSort(array, low, high);
            return;
        }

        int partition = partition(array, low, high);
        quickSort(array, low, partition - 1, cutoffSize);
        quickSort(array, partition + 1, high, cutoffSize);
    }

    private static int partition(Comparable[] array, int low, int high) {

        int randomPivotIndex = StdRandom.uniform(low, high + 1);
        ArrayUtil.exchange(array, low, randomPivotIndex);
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

    private static void printResults(int cutoffSize, int arraySize, double quickSortWithRandomizedArrayRunningTime, double quickSortWithRandomPivotRunningTime) {
        StdOut.printf("%11d %13d %45.1f %27.1f\n", cutoffSize, arraySize, quickSortWithRandomizedArrayRunningTime, quickSortWithRandomPivotRunningTime);
    }

}
