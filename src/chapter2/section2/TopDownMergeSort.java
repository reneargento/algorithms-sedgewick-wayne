package chapter2.section2;

/**
 * Created by Rene Argento on 11/02/17.
 */
public class TopDownMergeSort {

    public static void mergeSort(Comparable[] array) {
        Comparable[] aux = new Comparable[array.length];

        sort(array, aux, 0, array.length - 1);
    }

    private static void sort(Comparable[] array, Comparable[] aux, int low, int high) {

        if (high <= low) {
            return;
        }

        int middle = low + (high - low) / 2;

        sort(array, aux, low, middle);
        sort(array, aux, middle + 1, high);

        merge(array, aux, low, middle, high);
    }

    @SuppressWarnings("unchecked")
    private static void merge(Comparable[] array, Comparable[] aux, int low, int middle, int high) {
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

    //Other possible merge implementation
//    private static void merge(Comparable[] array, Comparable[] aux, int low, int middle, int high) {
//        for(int i = low; i <= high; i++) {
//            aux[i] = array[i];
//        }
//
//        int leftIndex = low;
//        int rightIndex = middle + 1;
//
//        for(int i = low; i <= high; i++) {
//            if (leftIndex > middle) {
//                array[i] = aux[rightIndex];
//                rightIndex++;
//            } else if (rightIndex > high) {
//                array[i] = aux[leftIndex];
//                leftIndex++;
//            } else if (aux[leftIndex].compareTo(aux[rightIndex]) <= 0) {
//                array[i] = aux[leftIndex];
//                leftIndex++;
//            } else {
//                array[i] = aux[rightIndex];
//                rightIndex++;
//            }
//        }
//    }

}
