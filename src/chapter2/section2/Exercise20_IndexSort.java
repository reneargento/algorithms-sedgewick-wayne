package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 18/02/17.
 */
//Based on http://algs4.cs.princeton.edu/22mergesort/Merge.java.html
public class Exercise20_IndexSort {

    public static void main(String[] args) {

        Comparable[] array1 = generateArray1();
        int[] indexSortedArray1 = indexSort(array1);

        int[] expectedIndexSortedArray1 = {5, 4, 6, 3, 7, 2, 8, 1, 9, 0};
        if (validate(expectedIndexSortedArray1, indexSortedArray1)) {
            StdOut.println("Index sorted OK");
        } else {
            StdOut.println("Index sorted NOT OK");
        }
        StdOut.println("Expected: Index sorted OK\n");

        Comparable[] array2 = generateArray2();
        int[] indexSortedArray2 = indexSort(array2);

        int[] expectedIndexSortedArray2 = {3, 2, 1, 0};
        if (validate(expectedIndexSortedArray2, indexSortedArray2)) {
            StdOut.println("Index sorted OK");
        } else {
            StdOut.println("Index sorted NOT OK");
        }
        StdOut.println("Expected: Index sorted OK");
    }

    private static Comparable[] generateArray1() {
        Comparable[] array = new Comparable[10];

        array[0] = 10; //Correct index: 9
        array[1] = 9; //Correct index: 7
        array[2] = 8; //Correct index: 5
        array[3] = 7; //Correct index: 3
        array[4] = 6; //Correct index: 1
        array[5] = 5; //Correct index: 0
        array[6] = 6; //Correct index: 2
        array[7] = 7; //Correct index: 4
        array[8] = 8; //Correct index: 6
        array[9] = 9; //Correct index: 8

        //Expected: [5, 4, 6, 3, 7, 2, 8, 1, 9, 0]

        return array;
    }

    private static Comparable[] generateArray2() {

        Comparable[] array = new Comparable[4];

        array[0] = 4; //Correct index: 3
        array[1] = 3; //Correct index: 2
        array[2] = 2; //Correct index: 1
        array[3] = 1; //Correct index: 0

        //Expected: [3, 2, 1, 0]

        return array;
    }

    private static int[] indexSort(Comparable[] array) {
        int[] aux = new int[array.length];
        int[] indexSort = new int[array.length];

        for(int i = 0; i < array.length; i++) {
            indexSort[i] = i;
        }

        indexSort(array, aux, indexSort, 0, array.length - 1);

        return indexSort;
    }

    private static void indexSort(Comparable[] array, int[] aux, int[] indexSort, int low, int high) {

        if (low >= high) {
            return;
        }

        int middle = low + (high - low) / 2;

        indexSort(array, aux, indexSort, low, middle);
        indexSort(array, aux, indexSort, middle + 1, high);

        merge(array, aux, indexSort, low, middle, high);
    }

    @SuppressWarnings("unchecked")
    private static void merge(Comparable[] array, int[] aux, int[] indexSort, int low, int middle, int high) {

        for(int i = low; i <= high; i++) {
            aux[i] = indexSort[i];
        }

        int leftIndex = low;
        int rightIndex = middle + 1;
        int arrayIndex = low;

        while (leftIndex <= middle && rightIndex <= high) {

            if (array[aux[leftIndex]].compareTo(array[aux[rightIndex]]) <= 0) {
                indexSort[arrayIndex] = aux[leftIndex];

                leftIndex++;
            } else {
                indexSort[arrayIndex] = aux[rightIndex];
                rightIndex++;
            }

            arrayIndex++;
        }

        while (leftIndex <= middle) {
            indexSort[arrayIndex] = aux[leftIndex];
            leftIndex++;
            arrayIndex++;
        }
    }

    private static boolean validate(int[] expectedIndexSortedArray, int[] indexSortedArray) {
        if (expectedIndexSortedArray.length != indexSortedArray.length) {
            return false;
        }

        for(int i = 0; i < expectedIndexSortedArray.length; i++) {
            if (expectedIndexSortedArray[i] != indexSortedArray[i]) {
                return false;
            }
        }

        return true;
    }

}
