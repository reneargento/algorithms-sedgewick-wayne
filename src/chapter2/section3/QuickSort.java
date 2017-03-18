package chapter2.section3;

import edu.princeton.cs.algs4.StdRandom;
import util.ArrayUtil;

/**
 * Created by rene on 04/03/17.
 */
public class QuickSort {

    public static void quickSort(Comparable[] array) {
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
        Comparable pivot = array[low];

        int i = low;
        int j = high + 1;

        while(true) {
            while (ArrayUtil.less(array[++i], pivot)) {
                if(i == high) {
                    break;
                }
            }

            while(ArrayUtil.less(pivot, array[--j])) {
                if(j == low) {
                    break;
                }
            }

            if(i >= j) {
                break;
            }

            ArrayUtil.exchange(array, i, j);
        }

        //Place pivot in the right place
        ArrayUtil.exchange(array, low, j);
        return j;
    }

}
