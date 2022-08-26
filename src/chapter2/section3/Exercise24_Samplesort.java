package chapter2.section3;

import chapter2.section1.InsertionSort;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayUtil;
import util.ArrayGenerator;

import java.util.Map;

/**
 * Created by Rene Argento on 10/03/17.
 */
// Thanks to ckwastra (https://github.com/ckwastra) for the samplesort implementation.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/263
@SuppressWarnings("unchecked")
public class Exercise24_Samplesort {

    private static final int INSERTION_SORT_CUTOFF = 2;

    // Parameters example: 8 131072
    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);
        int initialArraySize = Integer.parseInt(args[1]);

        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize, 2);
        doExperiment(numberOfExperiments, initialArraySize, allInputArrays);
    }

    private static <T extends Comparable<T>> void doExperiment(int numberOfExperiments, int initialArraySize,
                                     Map<Integer, Comparable[]> allInputArrays) {
        StdOut.printf("%13s %23s %25s\n", "Array Size | ", "QuickSort Running Time |", "SampleSort Running Time");

        int arraySize = initialArraySize;

        for (int i = 0; i < numberOfExperiments; i++) {
            Comparable<T>[] originalArray = allInputArrays.get(i);
            Comparable<T>[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            // Default QuickSort
            Stopwatch quickSortTimer = new Stopwatch();
            QuickSort.quickSort(originalArray);
            double quickSortRunningTime = quickSortTimer.elapsedTime();

            // SampleSort
            Stopwatch sampleSortTimer = new Stopwatch();
            sampleSort(array);
            double sampleSortRunningTime = sampleSortTimer.elapsedTime();

            printResults(arraySize, quickSortRunningTime, sampleSortRunningTime);
            arraySize *= 2;
        }
    }

    private static <T extends Comparable<T>> void sampleSort(Comparable<T>[] array) {
        sampleSort(array, array.length);
    }

    private static <T extends Comparable<T>> void sampleSort(Comparable<T>[] array, int size) {
        if (size <= INSERTION_SORT_CUTOFF) {
            InsertionSort.insertionSort(array, 0, size);
            return;
        }

        // Based on Python's (2.23) version of Samplesort
        // numberOfPivots ~= lg(n / ln(n))
        double sizeByLogSize = size / Math.log(size);
        int k = (int) Math.round(Math.log(sizeByLogSize) / Math.log(2));
        int numberOfPivots = (int) Math.pow(2, k) - 1;

        // Pick random samples
        for (int i = 0; i < numberOfPivots; i++) {
            int randomIndex = StdRandom.uniform(size);
            ArrayUtil.exchange(array, i, randomIndex);
        }

        // Recursively sort the sample
        sampleSort(array, numberOfPivots);

        // | 0 1 ... numberOfPivots-1 | numberOfPivots ... n-1 |
        // | <-   sorted sample  ->   | <-    unknown    ->    |
        //                            | lo                  hi |
        sampleSort(array, numberOfPivots, size - 1, numberOfPivots);
    }

    private static <T extends Comparable<T>> void sampleSort(Comparable<T>[] array, int low, int high,
                                                             int numberOfPivots) {
        if (low > high) {
            return;
        }
        if (numberOfPivots == 0) {
            InsertionSort.insertionSort(array, low, high);
            return;
        }

        // Move pivots in-place to always have them on both partitions.
        if (numberOfPivots > 0) {
            // The pivots are to the left of low. Move half to the right end.
            int halfPivots = numberOfPivots / 2;
            for (int i = 0; i < halfPivots; i++) {
                low--;
                ArrayUtil.exchange(array, low, high);
                high--;
            }
        } else {
            // The pivots are to the right of high. Move half to the left end.
            numberOfPivots = -numberOfPivots;
            int halfPivots = (numberOfPivots + 1) / 2;
            for (int i = 0; i < halfPivots; i++) {
                high++;
                ArrayUtil.exchange(array, low, high);
                low++;
            }
        }

        low--;
        int partition = partition(array, low, high);

        int halfPivots;
        int halfPivotsOnLeftPartition;
        if (numberOfPivots % 2 == 0) {
            halfPivots = numberOfPivots / 2;
            halfPivotsOnLeftPartition = halfPivots - 1;
        } else {
            halfPivots = numberOfPivots / 2;
            halfPivotsOnLeftPartition = halfPivots;
        }
        sampleSort(array, low, partition - 1, halfPivotsOnLeftPartition);
        sampleSort(array, partition + 1, high, -halfPivots);
    }

    private static <T extends Comparable<T>> int partition(Comparable<T>[] array, int low, int high) {
        Comparable<T> pivot = array[low];

        int i = low;
        int j = high + 1;

        while (true) {
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

            if (i >= j) {
                break;
            }

            ArrayUtil.exchange(array, i, j);
        }

        // Place pivot in the right place
        ArrayUtil.exchange(array, low, j);
        return j;
    }

    private static void printResults(int arraySize, double quickSortRunningTime, double sampleSortRunningTime) {
        StdOut.printf("%10d %25.1f %27.1f\n", arraySize, quickSortRunningTime, sampleSortRunningTime);
    }
}
