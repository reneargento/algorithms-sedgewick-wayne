package chapter2.section2;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 12/02/17.
 */
@SuppressWarnings("unchecked")
public class Exercise11_Improvements {

    private static final int CUTOFF = 15;

    public static void main(String[] args) {
        Comparable[] array = generateRandomArray(1000);
        topDownMergeSort(array);
    }

    private static Comparable[] generateRandomArray(int arrayLength) {
        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = StdRandom.uniform();
        }

        return array;
    }

    private static void topDownMergeSort(Comparable[] array) {
        //Improvement #3 - Eliminate the copy to the auxiliary array on merge
        Comparable[] aux = array.clone();

        topDownMergeSort(aux, array, 0, array.length - 1);
    }

    private static void topDownMergeSort(Comparable[] array, Comparable[] aux, int low, int high) {
        //Improvement #1 - Cutoff for small arrays
        if (high - low <= CUTOFF) {
            insertionSort(aux, low, high);
            return;
        }

        int middle = low + (high - low) / 2;

        topDownMergeSort(aux, array, low, middle);
        topDownMergeSort(aux, array, middle + 1, high);

        //Improvement #2 - Skip merge if the array is already in order
        if (array[middle].compareTo(array[middle + 1]) <= 0) {
            System.arraycopy(array, low, aux, low, high - low + 1);
            return;
        }

        merge(array, aux, low, middle, high);
    }

    private static void merge(Comparable[] array, Comparable[] aux, int low, int middle, int high) {
        int indexLeft = low;
        int indexRight = middle + 1;

        for (int i = low; i <= high; i++) {
            if (indexLeft > middle) {
                aux[i] = array[indexRight++];
            } else if (indexRight > high) {
                aux[i] = array[indexLeft++];
            } else if (array[indexLeft].compareTo(array[indexRight]) <= 0) {
                aux[i] = array[indexLeft++];   // to ensure stability
            } else {
                aux[i] = array[indexRight++];
            }
        }
    }

    private static void insertionSort(Comparable[] array, int low, int high) {
        for(int i = low; i <= high; i++) {
            for(int j = i; j > low && array[j - 1].compareTo(array[j]) > 0; j--) {
                Comparable temp = array[j - 1];
                array[j - 1] = array[j];
                array[j] = temp;
            }
        }
    }
}
