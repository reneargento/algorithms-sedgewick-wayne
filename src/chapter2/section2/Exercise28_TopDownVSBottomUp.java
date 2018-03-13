package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 21/02/17.
 */
public class Exercise28_TopDownVSBottomUp {

    private enum MergeSortType {
        TOP_DOWN, BOTTOM_UP;
    }

    public static void main(String[] args) {
        List<Comparable[]> arrays = new ArrayList<>(4);
        Comparable[] array1 = ArrayGenerator.generateRandomArray(1000);
        Comparable[] array2 = ArrayGenerator.generateRandomArray(10000);
        Comparable[] array3 = ArrayGenerator.generateRandomArray(100000);
        Comparable[] array4 = ArrayGenerator.generateRandomArray(1000000);

        arrays.add(array1);
        arrays.add(array2);
        arrays.add(array3);
        arrays.add(array4);

        sortCompare(arrays);
    }

    private static void sortCompare(List<Comparable[]> arrays) {

        int numberOfExperiments = 4;

        for(int i = 0; i < numberOfExperiments; i++) {
            Comparable[] currentArray = arrays.get(i);

            double timeTopDownMergeSort = time(MergeSortType.TOP_DOWN, currentArray);
            double timeBottomUpMergeSort = time(MergeSortType.BOTTOM_UP, currentArray);

            StdOut.printf("For %d random doubles\n top-down mergesort is", currentArray.length);
            StdOut.printf(" %.1f times faster than bottom-up mergersort.\n", timeBottomUpMergeSort / timeTopDownMergeSort);
        }
    }

    public static double time(MergeSortType mergeSortType, Comparable[] originalArray) {

        Comparable[] copyArray = new Comparable[originalArray.length];
        System.arraycopy(originalArray, 0, copyArray, 0, originalArray.length);

        Stopwatch timer = new Stopwatch();

        if (mergeSortType == MergeSortType.TOP_DOWN) {
            TopDownMergeSort.mergeSort(copyArray);
        } else if (mergeSortType == MergeSortType.BOTTOM_UP) {
            BottomUpMergeSort.mergeSort(copyArray);
        }

        return timer.elapsedTime();
    }

}
