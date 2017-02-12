package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by rene on 11/02/17.
 */
public class Exercise6 {

    private enum SortType {
        TOP_DOWN_MERGESORT, BOTTOM_UP_MERGESORT;
    }

    private static int numberOfArrayAccesses;

    public static void main(String[] args) {

        StdOut.printf("%6s %15s %11s\n", "N", "Array Accesses", "Upper Bound");
        StdOut.println();

        StdOut.printf("Top-Down MergeSort");
        StdOut.println();
        doExperiments(SortType.TOP_DOWN_MERGESORT, 1, 512);

        StdOut.println();
        StdOut.printf("Bottom-Up MergeSort");
        StdOut.println();
        doExperiments(SortType.BOTTOM_UP_MERGESORT, 1, 512);
    }

    private static void doExperiments(SortType sortType, int arrayLength, int numberOfExperiments) {

        for(int i=0; i < numberOfExperiments; i++) {

            numberOfArrayAccesses = 0;

            Comparable[] array = generateRandomArray(arrayLength);

            if(sortType == SortType.TOP_DOWN_MERGESORT) {
                topDownMergeSort(array);
            } else if (sortType == SortType.BOTTOM_UP_MERGESORT) {
                bottomUpMergeSort(array);
            }

            printExperiment(arrayLength);

            arrayLength++;
        }
    }

    private static Comparable[] generateRandomArray(int arrayLength) {
        Comparable[] array = new Comparable[arrayLength];

        for(int i=0; i < arrayLength; i++) {
            array[i] = StdRandom.uniform();
        }

        return array;
    }

    private static void topDownMergeSort(Comparable[] array) {
        Comparable[] aux = new Comparable[array.length];

        topDownMergeSort(array, aux, 0, array.length - 1);
    }

    private static void topDownMergeSort(Comparable[] array, Comparable[] aux, int low, int high) {

        if(high <= low) {
            return;
        }

        int middle = low + (high - low) / 2;

        topDownMergeSort(array, aux, low, middle);
        topDownMergeSort(array, aux, middle + 1, high);

        merge(array, aux, low, middle, high);
    }

    private static void bottomUpMergeSort(Comparable[] array) {

        Comparable[] aux = new Comparable[array.length];

        for (int size = 1; size < array.length; size = size + size) {

            for(int low = 0; low + size < array.length; low += size + size) {
                int high = Math.min(low + size + size - 1, array.length - 1);

                merge(array, aux, low, low + size - 1, high);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void merge(Comparable[] array, Comparable[] aux, int low, int middle, int high) {
        for(int i=low; i <= high; i++) {
            aux[i] = array[i];

            numberOfArrayAccesses++;
        }

        int indexLeft = low;
        int indexRight = middle + 1;
        int arrayIndex = low;

        while (indexLeft <= middle && indexRight <= high) {
            if(aux[indexLeft].compareTo(aux[indexRight]) <= 0) {
                array[arrayIndex] = aux[indexLeft];
                indexLeft++;
            } else {
                array[arrayIndex] = aux[indexRight];
                indexRight++;
            }

            arrayIndex++;
            numberOfArrayAccesses++;
        }

        while (indexLeft <= middle) {
            array[arrayIndex] = aux[indexLeft];

            indexLeft++;
            arrayIndex++;

            numberOfArrayAccesses++;
        }
    }

    private static void printExperiment(int arrayLength) {
        int upperBound = 6 * arrayLength * (int) (Math.log(arrayLength) / Math.log(2));

        StdOut.printf("%6d %15d %11d\n", arrayLength, numberOfArrayAccesses, upperBound);
    }

}
