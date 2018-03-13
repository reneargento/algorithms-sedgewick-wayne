package chapter2.section3;

import edu.princeton.cs.algs4.StdRandom;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 04/03/17.
 */
public class QuickSort {

    public static void quickSort(Comparable[] array) {
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
        Comparable pivot = array[low];

        int lowIndex = low;
        int highIndex = high + 1;

        while(true) {
            while (ArrayUtil.less(array[++lowIndex], pivot)) {
                if (lowIndex == high) {
                    break;
                }
            }

            while(ArrayUtil.less(pivot, array[--highIndex])) {
                if (highIndex == low) {
                    break;
                }
            }

            if (lowIndex >= highIndex) {
                break;
            }

            ArrayUtil.exchange(array, lowIndex, highIndex);
        }

        //Place pivot in the right place
        ArrayUtil.exchange(array, low, highIndex);
        return highIndex;
    }

}
