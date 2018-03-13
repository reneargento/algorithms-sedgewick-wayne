package chapter2.section2;

/**
 * Created by Rene Argento on 20/02/17.
 */
public class Exercise26_ArrayCreationMergeSort {

    public static void mergeSort(Comparable[] array) {
        sort(array, 0, array.length - 1);
    }

    private static void sort(Comparable[] array, int low, int high) {

        if (high <= low) {
            return;
        }

        int middle = low + (high - low) / 2;

        sort(array, low, middle);
        sort(array, middle + 1, high);

        merge(array, low, middle, high);
    }

    @SuppressWarnings("unchecked")
    private static void merge(Comparable[] array, int low, int middle, int high) {
        Comparable[] aux = new Comparable[array.length];

        for (int i = low; i <= high; i++) {
            aux[i] = array[i];
        }

        int indexLeft = low;
        int indexRight = middle + 1;
        int arrayIndex = low;

        while (indexLeft <= middle && indexRight <= high) {
            if (aux[indexLeft].compareTo(aux[indexRight]) <= 0) {
                array[arrayIndex] = aux[indexLeft];
                indexLeft++;
            } else {
                array[arrayIndex] = aux[indexRight];
                indexRight++;
            }
            arrayIndex++;
        }

        while (indexLeft <= middle) {
            array[arrayIndex] = aux[indexLeft];

            indexLeft++;
            arrayIndex++;
        }
    }

}
