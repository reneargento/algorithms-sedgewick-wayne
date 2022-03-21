package chapter2.section2;

import util.ArrayGenerator;

/**
 * Created by Rene Argento on 12/02/17.
 */
@SuppressWarnings("rawtypes")
public class Exercise10_FasterMerge {

    public static void main(String[] args) {
        Comparable[] array = ArrayGenerator.generateRandomArray(1000);
        topDownMergeSort(array);
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

        for (int i = low; i <= middle; i++) {
            aux[auxIndex] = array[i];
            auxIndex++;
        }

        for (int i = high; i >= middle + 1; i--) {
            aux[auxIndex] = array[i];
            auxIndex++;
        }

        int indexLeft = low;
        int indexRight = high;
        int arrayIndex = low;

        while (indexLeft <= indexRight) {
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
