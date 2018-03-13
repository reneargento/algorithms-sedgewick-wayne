package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayUtil;
import util.ArrayGenerator;

import java.util.Map;

/**
 * Created by Rene Argento on 06/03/17.
 */
public class Exercise17_Sentinels {

    // Parameters example: 8 131072
    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);
        int initialArraySize = Integer.parseInt(args[1]);

        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize, 2);

        doExperiment(numberOfExperiments, initialArraySize, allInputArrays);
    }

    private static void doExperiment(int numberOfExperiments, int initialArraySize, Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %23s %39s\n", "Array Size | ", "QuickSort Running Time |", "QuickSort W/ No Sentinels Running Time");

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            //Default QuickSort
            Stopwatch defaultQuickSortTimer = new Stopwatch();

            QuickSort.quickSort(array);

            double defaultQuickSortRunningTime = defaultQuickSortTimer.elapsedTime();

            //QuickSort with no sentinels
            Stopwatch quickSortWithNoSentinelsTimer = new Stopwatch();

            quickSortWithNoSentinels(originalArray);

            double quickSortWithNoSentinelsRunningTime = quickSortWithNoSentinelsTimer.elapsedTime();

            printResults(arraySize, defaultQuickSortRunningTime, quickSortWithNoSentinelsRunningTime);

            arraySize *= 2;
        }
    }

    private static void quickSortWithNoSentinels(Comparable[] array) {
        StdRandom.shuffle(array);

        //Place biggest item on the right end
        Comparable maxValue = array[0];
        int maxValueIndex = 0;
        for(int i = 1; i < array.length; i++) {
            if (ArrayUtil.less(maxValue, array[i])) {
                maxValue = array[i];
                maxValueIndex = i;
            }
        }

        ArrayUtil.exchange(array, maxValueIndex, array.length - 1);

        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(Comparable[] array, int low, int high) {

        if (low >= high) {
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
            while (ArrayUtil.less(array[++i], pivot));

            while(ArrayUtil.less(pivot, array[--j]));

            if (i >= j) {
                break;
            }

            ArrayUtil.exchange(array, i, j);
        }

        //Place pivot in the right place
        ArrayUtil.exchange(array, low, j);
        return j;
    }

    private static void printResults(int arraySize, double defaultQuickSortRunningTime, double quickSortWithNoSentinelsRunningTime) {
        StdOut.printf("%10d %25.1f %41.1f\n", arraySize, defaultQuickSortRunningTime, quickSortWithNoSentinelsRunningTime);
    }

}
