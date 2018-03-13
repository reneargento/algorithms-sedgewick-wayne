package chapter2.section2;

/**
 * Created by Rene Argento on 19/02/17.
 */
//This implementation uses the first mergesort improvement:
    //Improvement #1 - Cutoff for small arrays
@SuppressWarnings("unchecked")
public class Exercise23_Improvements1_Cutoff {

    private int cutoff;

    Exercise23_Improvements1_Cutoff(int cutoff) {
        this.cutoff = cutoff;
    }

    public void mergeSort(Comparable[] array) {
        Comparable[] aux = new Comparable[array.length];

        sort(array, aux, 0, array.length - 1);
    }

    private void sort(Comparable[] array, Comparable[] aux, int low, int high) {

        if (high <= low) {
            return;
        }

        //Improvement #1 - Cutoff for small arrays
        if (high - low <= cutoff) {
            insertionSort(array, low, high);
            return;
        }

        int middle = low + (high - low) / 2;

        sort(array, aux, low, middle);
        sort(array, aux, middle + 1, high);

        merge(array, aux, low, middle, high);
    }

    private void merge(Comparable[] array, Comparable[] aux, int low, int middle, int high) {
        for(int i = low; i <= high; i++) {
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

    private void insertionSort(Comparable[] array, int low, int high) {

        for(int i = low; i <= high; i++) {
            for(int j = i; j > low && array[j - 1].compareTo(array[j]) > 0; j--) {

                Comparable temp = array[j - 1];
                array[j - 1] = array[j];
                array[j] = temp;
            }
        }
    }

}
