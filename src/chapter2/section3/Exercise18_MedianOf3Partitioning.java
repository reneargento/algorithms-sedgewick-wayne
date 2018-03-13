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
public class Exercise18_MedianOf3Partitioning {

    // Parameters example: 8 131072
    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);
        int initialArraySize = Integer.parseInt(args[1]);

        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize, 2);

        doExperiment(numberOfExperiments, initialArraySize, allInputArrays);
    }

    private static void doExperiment(int numberOfExperiments, int initialArraySize, Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %23s %30s\n", "Array Size | ", "QuickSort Running Time |", "QuickSort with median-of-three");

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            //Default QuickSort
            Stopwatch defaultQuickSortTimer = new Stopwatch();

            QuickSort.quickSort(array);

            double defaultQuickSortRunningTime = defaultQuickSortTimer.elapsedTime();

            //QuickSort with median-of-three partitioning
            Stopwatch quickSortWithMedianOfThreeTimer = new Stopwatch();

            quickSortWithMedianOfThree(originalArray);

            double quickSortWithMedianOfThreeRunningTime = quickSortWithMedianOfThreeTimer.elapsedTime();

            printResults(arraySize, defaultQuickSortRunningTime, quickSortWithMedianOfThreeRunningTime);

            arraySize *= 2;
        }
    }

    public static void quickSortWithMedianOfThree(Comparable[] array) {
        StdRandom.shuffle(array);
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
        int middle = low + (high - low) / 2;

        //Place items in order: Low < Middle < HighArraySortUtil.exchange(array, middle, low);

        if (ArrayUtil.less(array[middle], array[low])) {
            ArrayUtil.exchange(array, middle, low);
        }
        if (ArrayUtil.less(array[high], array[low])) {
            ArrayUtil.exchange(array, high, low);
        }
        if (ArrayUtil.less(array[high], array[middle])) {
            ArrayUtil.exchange(array, high, middle);
        }

        //Swap median with low
        ArrayUtil.exchange(array, middle, low);
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

    private static void printResults(int arraySize, double defaultQuickSortRunningTime, double quickSortWithMedianOfThree) {
        StdOut.printf("%10d %25.1f %32.1f\n", arraySize, defaultQuickSortRunningTime, quickSortWithMedianOfThree);
    }

}
