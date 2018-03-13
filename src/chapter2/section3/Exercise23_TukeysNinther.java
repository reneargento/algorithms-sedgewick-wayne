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
 * Created by Rene Argento on 10/03/17.
 */
public class Exercise23_TukeysNinther {

    private static final int SIZE_REQUIRED_FOR_TUKEY_NINTHER = 9;
    private static final int INSERTION_SORT_CUTOFF = 9;

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

        StdOut.printf("%13s %16s %38s %52s\n", "Array Size | ", "QuickSort 3-Way |", "QuickSort with fast 3-way partitioning | ",
                "QuickSort w/ fast 3-way partitioning + Tukey Ninther");

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] arrayCopy1 = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, arrayCopy1, 0, originalArray.length);
            Comparable[] arrayCopy2 = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, arrayCopy2, 0, originalArray.length);

            //QuickSort 3-Way
            Stopwatch quickSort3WaySortTimer = new Stopwatch();

            QuickSort3Way.quickSort3Way(originalArray);

            double quickSort3WayRunningTime = quickSort3WaySortTimer.elapsedTime();

            //QuickSort with fast 3-way partitioning (Bentley-McIlroy)
            Stopwatch quickSortWithFast3WayPartitioning = new Stopwatch();

            Exercise22_Fast3WayPartitioning.quickSortWithFast3WayPartitioning(arrayCopy1);

            double quickSortWithFast3WayPartitioningRunningTime = quickSortWithFast3WayPartitioning.elapsedTime();

            //QuickSort with fast 3-way partitioning (Bentley-McIlroy) + Tukey Ninther
            Stopwatch quickSortWithFast3WayPartitioningTukeyNinther = new Stopwatch();

            quickSortWithFast3WayPartitioningTukeyNinther(arrayCopy2);

            double quickSortWithFast3WayPartitioningTukeyNintherRunningTime = quickSortWithFast3WayPartitioningTukeyNinther.elapsedTime();

            printResults(arraySize, quickSort3WayRunningTime, quickSortWithFast3WayPartitioningRunningTime,
                    quickSortWithFast3WayPartitioningTukeyNintherRunningTime);

            arraySize *= 2;
        }
    }

    private static void quickSortWithFast3WayPartitioningTukeyNinther(Comparable[] array) {
        StdRandom.shuffle(array);
        quickSort(array, 0, array.length - 1);
    }

    @SuppressWarnings("unchecked")
    private static void quickSort(Comparable[] array, int low, int high) {

        if (low >= high) {
            return;
        }

        if (high - low + 1 < INSERTION_SORT_CUTOFF) {
            InsertionSort.insertionSort(array, low, high);
            return;
        }

        int i = low;
        int j = high + 1;

        int p = low;
        int q = high + 1;

        int pivotIndex = getPivotIndex(array, low, high);
        ArrayUtil.exchange(array, low, pivotIndex);
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

    //Tukey's ninther
    private static int getPivotIndex(Comparable[] array, int low, int high) {
        int numberOfValues = high - low + 1;

        int eps = numberOfValues / SIZE_REQUIRED_FOR_TUKEY_NINTHER;
        int middle = low + (high - low) / 2;

        int medianIndex1 = getMedianIndex(array, low, low + eps, low + eps + eps);
        int medianIndex2 = getMedianIndex(array, middle - eps, middle, middle + eps);
        int medianIndex3 = getMedianIndex(array, high - eps - eps, high - eps, high);

        return getMedianIndex(array, medianIndex1, medianIndex2, medianIndex3);
    }

    private static int getMedianIndex(Comparable[] array, int index1, int index2, int index3) {
        return ArrayUtil.less(array[index1], array[index2]) ?
                (ArrayUtil.less(array[index2], array[index3]) ? index2 : ArrayUtil.less(array[index1], array[index3])
                        ? index3 : index1) :
                (ArrayUtil.less(index1, index3) ? index1 : ArrayUtil.less(index2, index3) ? index3 : index2);
    }

    private static void printResults(int arraySize, double quickSort3WayRunningTime, double quickSortWithFast3WayPartitioningRunningTime,
                                     double quickSortWithFast3WayPartitioningTukeyNintherRunningTime) {
        StdOut.printf("%10d %18.1f %40.1f %55.1f\n", arraySize, quickSort3WayRunningTime, quickSortWithFast3WayPartitioningRunningTime,
                quickSortWithFast3WayPartitioningTukeyNintherRunningTime);
    }

}
