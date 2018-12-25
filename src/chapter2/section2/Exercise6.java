package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 11/02/17.
 */
// Thanks to ajfg93 (https://github.com/ajfg93) for correcting the array access count.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/12
public class Exercise6 {

    private enum SortType {
        TOP_DOWN_MERGESORT, BOTTOM_UP_MERGESORT;
    }

    private static int numberOfArrayAccesses;

    public static void main(String[] args) {

        int initialArraySize = 1;
        int numberOfExperiments = 512;

        Map<Integer, Comparable[]> allInputArrays = generateAllArrays(initialArraySize, numberOfExperiments);

        StdOut.printf("%6s %15s %11s\n", "N", "Array Accesses", "Upper Bound");
        StdOut.println();

        StdOut.printf("Top-Down MergeSort");
        StdOut.println();
        doExperiments(SortType.TOP_DOWN_MERGESORT, 1, 512, allInputArrays);

        StdOut.println();
        StdOut.printf("Bottom-Up MergeSort");
        StdOut.println();
        doExperiments(SortType.BOTTOM_UP_MERGESORT, 1, 512, allInputArrays);
    }

    private static void doExperiments(SortType sortType, int arrayLength, int numberOfExperiments,
                                      Map<Integer, Comparable[]> allInputArrays) {

        for(int i = 0; i < numberOfExperiments; i++) {

            numberOfArrayAccesses = 0;

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            if (sortType == SortType.TOP_DOWN_MERGESORT) {
                topDownMergeSort(array);
            } else if (sortType == SortType.BOTTOM_UP_MERGESORT) {
                bottomUpMergeSort(array);
            }

            printExperiment(arrayLength);

            arrayLength++;
        }
    }

    private static Map<Integer, Comparable[]> generateAllArrays(int initialArraySize, int numberOfExperiments) {

        Map<Integer, Comparable[]> allArrays = new HashMap<>();

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {
            Comparable[] array = generateRandomArray(arraySize);
            allArrays.put(i, array);

            arraySize++;
        }

        return allArrays;
    }

    private static Comparable[] generateRandomArray(int arrayLength) {
        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = StdRandom.uniform();
        }

        return array;
    }

    private static void topDownMergeSort(Comparable[] array) {
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
        for(int i = low; i <= high; i++) {
            aux[i] = array[i];

            numberOfArrayAccesses += 2;
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
            numberOfArrayAccesses += 4;
        }

        while (indexLeft <= middle) {
            array[arrayIndex] = aux[indexLeft];

            indexLeft++;
            arrayIndex++;

            numberOfArrayAccesses += 2;
        }
    }

    private static void printExperiment(int arrayLength) {
        double upperBound = 6 * arrayLength * (Math.log10(arrayLength) / Math.log10(2));

        StdOut.printf("%6d %15d %11.0f\n", arrayLength, numberOfArrayAccesses, upperBound);
    }

}
