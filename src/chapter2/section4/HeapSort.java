package chapter2.section4;

import util.ArrayUtil;

/**
 * Created by Rene Argento on 30/03/17.
 */
public class HeapSort {

    public static void heapSort(Comparable[] array) {
        constructHeap(array);
        sortdown(array);
    }

    private static void constructHeap(Comparable[] array) {
        for(int i = array.length / 2; i >= 1; i--) {
            sink(array, i, array.length - 1);
        }
    }

    private static void sortdown(Comparable[] array) {
        int endIndex = array.length - 1;

        while (endIndex > 1) {
            ArrayUtil.exchange(array, 1, endIndex);
            endIndex--;
            sink(array, 1, endIndex);
        }
    }

    private static void sink(Comparable[] array, int index, int endIndex) {
        while (index * 2 <= endIndex) {
            int biggestChildIndex = index * 2;

            if (index * 2 + 1 <= endIndex
                    && ArrayUtil.more(array[index * 2 + 1], array[index * 2])) {
                biggestChildIndex = index * 2 + 1;
            }

            if (ArrayUtil.less(array[index], array[biggestChildIndex])) {
                ArrayUtil.exchange(array, index, biggestChildIndex);
            } else {
                break;
            }

            index = biggestChildIndex;
        }
    }

}
