package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 02/02/17.
 */
public class Exercise28_EqualKeys {

    public static void main(String[] args) {
        sortCompare();
    }

    private static Comparable[] createArrayWithAnyValues(int size) {
        Comparable[] array = new Comparable[size];

        for(int i = 0; i < array.length; i++) {
            array[i] = StdRandom.uniform(100000);
        }

        return array;
    }

    private static Comparable[] createArrayWith2Values(int size) {
        Comparable[] array = new Comparable[size];

        for(int i = 0; i < array.length; i++) {
            array[i] = StdRandom.uniform(2);
        }

        return array;
    }

    private static void sortCompare() {
        int arrayLength = 128;
        int numberOfExperiments = 11;

        StdOut.printf("Selection Sort\n");
        double timeSelectionSort = timeRandomInput(SortTypes.SELECTION, arrayLength, numberOfExperiments);
        StdOut.printf("Insertion Sort\n");
        double timeInsertionSort = timeRandomInput(SortTypes.INSERTION, arrayLength, numberOfExperiments);

        StdOut.printf("For only 2 values, Insertion Sort is %.1f times faster than Selection Sort", timeSelectionSort / timeInsertionSort);
    }

    private static double timeRandomInput(SortTypes sortType, int initialLength, int numberOfExperiments) {
        double total = 0;
        int length = initialLength;

        for(int experiment = 0; experiment < numberOfExperiments; experiment++) {
            Comparable[] array1 = createArrayWithAnyValues(length);
            Comparable[] array2 = createArrayWith2Values(length);

            double timeForArrayWithAnyValues = time(sortType, array1);
            double timeForArrayWith2Values = time(sortType, array2);

            StdOut.printf("For an array of size %d: \n", length);
            StdOut.printf("Time for an array with any values: %.1f \n", timeForArrayWithAnyValues);
            StdOut.printf("Time for an array with 2 values: %.1f \n", timeForArrayWith2Values);
            StdOut.println();

            total += time(sortType, array2);

            length *= 2;
        }

        return total;
    }

    public static double time(SortTypes sortType, Comparable[] array) {
        Stopwatch timer = new Stopwatch();

        if (sortType == SortTypes.SELECTION) {
            SelectionSort.selectionSort(array);
        } else if (sortType == SortTypes.INSERTION) {
            InsertionSort.insertionSort(array);
        }

        return timer.elapsedTime();
    }

}
