package chapter2.section3;

import chapter2.section1.InsertionSort;
import edu.princeton.cs.algs4.StdRandom;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 11/03/17.
 */
public class QuickSortWithCutoff {

    public static void quickSortWithCutoff(Comparable[] array, int cutoffSize) {
        StdRandom.shuffle(array);
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

}
