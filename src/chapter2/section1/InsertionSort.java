package chapter2.section1;

import util.ArrayUtil;

/**
 * Created by Rene Argento on 02/02/17.
 */
@SuppressWarnings("unchecked")
public class InsertionSort {

    public static void insertionSort(Comparable[] array) {

        for(int i = 0; i < array.length; i++) {
            for(int j = i; j > 0 && array[j - 1].compareTo(array[j]) > 0; j--) {
                Comparable temp = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp;
            }
        }
    }

    public static void insertionSort(Comparable[] array, int low, int high) {
        for(int i = low; i <= high; i++) {
            for(int j = i; j > 0 && ArrayUtil.more(array[j - 1], array[j]); j--) {
                Comparable temp = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp;
            }
        }
    }

}
