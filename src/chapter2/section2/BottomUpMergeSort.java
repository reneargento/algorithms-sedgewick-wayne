package chapter2.section2;

/**
 * Created by Rene Argento on 11/02/17.
 */
public class BottomUpMergeSort {

    public static void mergeSort(Comparable[] array) {
        Comparable[] aux = new Comparable[array.length];

        for (int size = 1; size < array.length; size = size + size) {
            for(int low = 0; low + size < array.length; low += size + size) {
                int high = Math.min(low + size + size - 1, array.length - 1);
                merge(array, aux, low, low + size - 1, high);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void merge (Comparable[] array, Comparable[] aux, int low, int middle, int high) {
        for(int i = low; i <= high; i++) {
            aux[i] = array[i];
        }

        int leftIndex = low;
        int rightIndex = middle + 1;
        int arrayIndex = low;

        while(leftIndex <= middle && rightIndex <= high) {
            if (aux[leftIndex].compareTo(aux[rightIndex]) <= 0) {
                array[arrayIndex] = aux[leftIndex];
                leftIndex++;
            } else {
                array[arrayIndex] = aux[rightIndex];
                rightIndex++;
            }

            arrayIndex++;
        }

        while (leftIndex <= middle) {
            array[arrayIndex] = aux[leftIndex];
            leftIndex++;
            arrayIndex++;
        }
    }

}
