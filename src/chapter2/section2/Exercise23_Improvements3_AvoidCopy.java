package chapter2.section2;

/**
 * Created by Rene Argento on 19/02/17.
 */
// This implementation uses the third mergesort improvement:
// Improvement #3 - Avoid array copy during merge by switching arguments
public class Exercise23_Improvements3_AvoidCopy {

    public static void mergeSort(Comparable[] array) {
        //Improvement #3 - Eliminate the copy to the auxiliary array on merge
        Comparable[] aux = array.clone();

        sort(aux, array, 0, array.length - 1);
    }

    private static void sort(Comparable[] array, Comparable[] aux, int low, int high) {
        if (high <= low) {
            return;
        }

        int middle = low + (high - low) / 2;

        sort(aux, array, low, middle);
        sort(aux, array, middle + 1, high);

        merge(array, aux, low, middle, high);
    }

    @SuppressWarnings("unchecked")
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
}
