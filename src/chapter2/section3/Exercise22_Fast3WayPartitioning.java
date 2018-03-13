package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayUtil;
import util.ArrayGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 09/03/17.
 */
//Based on: http://algs4.cs.princeton.edu/23quicksort/QuickX.java.html
public class Exercise22_Fast3WayPartitioning {

    // Parameters example: 8 1048576
    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);
        int initialArraySize = Integer.parseInt(args[1]);

        Map<Integer, Comparable[]> allInputArrays = new HashMap<>();

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {
            Comparable[] array = ArrayGenerator.generateRandomArrayWith3Values(arraySize);
            allInputArrays.put(i, array);

            arraySize *= 2;
        }

        doExperiment(numberOfExperiments, initialArraySize, allInputArrays);
    }

    private static void doExperiment(int numberOfExperiments, int initialArraySize, Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %16s %38s\n", "Array Size | ", "QuickSort 3-Way |", "QuickSort with fast 3-way partitioning");

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            //QuickSort 3-Way
            Stopwatch quickSort3WaySortTimer = new Stopwatch();

            QuickSort3Way.quickSort3Way(originalArray);

            double quickSort3WayRunningTime = quickSort3WaySortTimer.elapsedTime();

            //QuickSort with fast 3-way partitioning (Bentley-McIlroy)
            Stopwatch quickSortWithFast3WayPartitioning = new Stopwatch();

            quickSortWithFast3WayPartitioning(array);

            double quickSortWithFast3WayPartitioningRunningTime = quickSortWithFast3WayPartitioning.elapsedTime();

            printResults(arraySize, quickSort3WayRunningTime, quickSortWithFast3WayPartitioningRunningTime);

            arraySize *= 2;
        }
    }

    public static void quickSortWithFast3WayPartitioning(Comparable[] array) {
        StdRandom.shuffle(array);
        quickSort(array, 0, array.length - 1);
    }

    @SuppressWarnings("unchecked")
    private static void quickSort(Comparable[] array, int low, int high) {

        if (low >= high) {
            return;
        }

        int i = low;
        int j = high + 1;

        int p = low;
        int q = high + 1;

        Comparable pivot = array[low];

        while (true) {

            if (i > low && array[i].compareTo(pivot) == 0) {
                ArrayUtil.exchange(array, ++p, i);
            }
            if (j <= high && array[j].compareTo(pivot) == 0) {
                ArrayUtil.exchange(array, --q, j);
            }

            while (ArrayUtil.less(array[++i], pivot)) {
                if (i == high) {
                    break;
                }
            }

            while (ArrayUtil.less(pivot, array[--j])) {
                if (j == low) {
                    break;
                }
            }

            //pointers cross
            if (i == j && array[i].compareTo(pivot) == 0) {
                ArrayUtil.exchange(array, ++p, i);
            }
            if (i >= j) {
                break;
            }

            ArrayUtil.exchange(array, i, j);
        }

        //Currently:
        // array[low..p] == pivot
        // array[p..i] < pivot
        // array[j..q] > pivot
        // array[q..high] == pivot

        i = j + 1;

        for(int k = low; k <= p; k++) {
            ArrayUtil.exchange(array, k, j--);
        }
        for(int k = high; k >= q; k--) {
            ArrayUtil.exchange(array, k, i++);
        }

        //Now:
        // array[low..j] < pivot
        // array[j..i] == pivot
        // array[i..high] > pivot

        quickSort(array, low, j);
        quickSort(array, i, high);
    }

    private static void printResults(int arraySize, double quickSort3WayRunningTime, double quickSortWithFast3WayPartitioningRunningTime) {
        StdOut.printf("%10d %18.1f %40.1f\n", arraySize, quickSort3WayRunningTime, quickSortWithFast3WayPartitioningRunningTime);
    }

}
