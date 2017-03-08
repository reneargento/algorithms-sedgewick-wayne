package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArraySortUtil;
import util.RandomArrayGenerator;

import java.util.Map;

/**
 * Created by rene on 06/03/17.
 */
public class Exercise18_MedianOf3Partitioning {

    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]); // 8
        int initialArraySize = Integer.parseInt(args[1]); // 131072

        Map<Integer, Comparable[]> allInputArrays = RandomArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize);

        doExperiment(numberOfExperiments, initialArraySize, allInputArrays);
    }

    private static void doExperiment(int numberOfExperiments, int initialArraySize, Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %23s %30s\n", "Array Size | ", "QuickSort Running Time |", "QuickSort with median-of-three");

        int arraySize = initialArraySize;

        for(int i=0; i < numberOfExperiments; i++) {

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

        if(low >= high) {
            return;
        }

        int partition = partition(array, low, high);
        quickSort(array, low, partition - 1);
        quickSort(array, partition + 1, high);
    }

    private static int partition(Comparable[] array, int low, int high) {
        int middle = low + (high - low) / 2;

        //Place items in order: Low < Middle < HighArraySortUtil.exchange(array, middle, low);

        if(ArraySortUtil.less(array[middle], array[low])) {
            ArraySortUtil.exchange(array, middle, low);
        }
        if(ArraySortUtil.less(array[high], array[low])) {
            ArraySortUtil.exchange(array, high, low);
        }
        if(ArraySortUtil.less(array[high], array[middle])) {
            ArraySortUtil.exchange(array, high, middle);
        }

        //Swap median with low
        ArraySortUtil.exchange(array, middle, low);
        Comparable pivot = array[low];

        int i = low;
        int j = high + 1;

        while(true) {
            while (ArraySortUtil.less(array[++i], pivot));

            while(ArraySortUtil.less(pivot, array[--j]));

            if(i >= j) {
                break;
            }

            ArraySortUtil.exchange(array, i, j);
        }

        //Place pivot in the right place
        ArraySortUtil.exchange(array, low, j);
        return j;
    }

    private static void printResults(int arraySize, double defaultQuickSortRunningTime, double quickSortWithMedianOfThree) {
        StdOut.printf("%10d %25.1f %32.1f\n", arraySize, defaultQuickSortRunningTime, quickSortWithMedianOfThree);
    }

}
