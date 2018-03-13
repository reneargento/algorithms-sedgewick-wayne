package chapter2.section2;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 12/02/17.
 */
public class Exercise10_FasterMerge {

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

    public static void topDownMergeSort(Comparable[] array) {
        Comparable[] aux = new Comparable[array.length];

        topDownMergeSort(array, aux, 0, array.length - 1);
    }

    private static void topDownMergeSort(Comparable[] array, Comparable[] aux, int low, int high) {

        if (high <= low) {
            return;
        }

        int middle = low + (high - low) / 2;

        topDownMergeSort(array, aux, low, middle);
        topDownMergeSort(array, aux, middle + 1, high);

        merge(array, aux, low, middle, high);
    }

    @SuppressWarnings("unchecked")
    private static void merge(Comparable[] array, Comparable[] aux, int low, int middle, int high) {

        int auxIndex = low;

        for(int i = low; i <= middle; i++) {
            aux[auxIndex] = array[i];

            auxIndex++;
        }

        for(int i = high; i >= middle + 1; i--) {
            aux[auxIndex] = array[i];

            auxIndex++;
        }

        int indexLeft = low;
        int indexRight = high;
        int arrayIndex = low;

        while (indexLeft <= middle) {
            if (aux[indexLeft].compareTo(aux[indexRight]) <= 0) {
                array[arrayIndex] = aux[indexLeft];
                indexLeft++;
            } else {
                array[arrayIndex] = aux[indexRight];
                indexRight--;
            }

            arrayIndex++;
        }
    }

}
