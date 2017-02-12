package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by rene on 11/02/17.
 */
//The experiment shows that the number of compares used by mergesort is not monotonically increasing
public class Exercise7 {

    private static int numberOfCompares;

    public static void main(String[] args) {

        boolean isMonotonicallyIncreasing = doExperiments(1, 512);
        StdOut.println("Is Monotonically Increasing: " + isMonotonicallyIncreasing);
    }

    private static boolean doExperiments(int arrayLength, int numberOfExperiments) {

        int previousNumberOfCompares = -1;

        for(int i=0; i < numberOfExperiments; i++) {

            numberOfCompares = 0;

            Comparable[] array = generateRandomArray(arrayLength);
            topDownMergeSort(array);

            if(numberOfCompares < previousNumberOfCompares) {
                return false;
            } else {
                previousNumberOfCompares = numberOfCompares;
            }

            arrayLength++;
        }

        return true;
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

    @SuppressWarnings("unchecked")
    private static void merge(Comparable[] array, Comparable[] aux, int low, int middle, int high) {
        for(int i=low; i <= high; i++) {
            aux[i] = array[i];
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
            numberOfCompares++;
        }

        while (indexLeft <= middle) {
            array[arrayIndex] = aux[indexLeft];

            indexLeft++;
            arrayIndex++;
        }
    }

}
