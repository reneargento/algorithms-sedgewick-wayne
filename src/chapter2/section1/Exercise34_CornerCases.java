package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;

/**
 * Created by Rene Argento on 05/02/17.
 */
public class Exercise34_CornerCases {

    public static void main(String[] args) {

        int arrayLength = Integer.parseInt(args[0]);
        int experiments = Integer.parseInt(args[1]);

        runAllExperiments(arrayLength, experiments);
    }

    private static void runAllExperiments(int initialArrayLength, int experiments) {

        int arrayLength = initialArrayLength;

        StdOut.printf("%15s %17s %8s\n", "Sort Type", "Array Length", "Time");
        StdOut.println("ORDERED ARRAY");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] orderedArray = generateOrderedArray(arrayLength);
            runExperiments(orderedArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("REVERSE ORDERED ARRAY");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] reverseOrderedArray = generateReverseOrderedArray(arrayLength);
            runExperiments(reverseOrderedArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("SAME KEYS ARRAY");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] equalKeysArray = generateEqualKeysArray(arrayLength);
            runExperiments(equalKeysArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("TWO VALUES ARRAY");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] twoValuesArray = generateTwoValuesArray(arrayLength);
            runExperiments(twoValuesArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        StdOut.println("SIZE 1 ARRAY");

        Comparable[] size1Array = generateSize1Array();
        runExperiments(size1Array);

        StdOut.println();
        StdOut.println();

        StdOut.println("SIZE 0 ARRAY");

        Comparable[] emptyArray = generateEmptyArray();
        runExperiments(emptyArray);
    }

    private static void runExperiments(Comparable[] array) {
        Comparable[] arrayCopy1 = Arrays.copyOf(array, array.length);
        Comparable[] arrayCopy2 = Arrays.copyOf(array, array.length);

        double timeSelection = time(array, SortTypes.SELECTION);
        printExperiment(SortTypes.SELECTION, array.length, timeSelection);

        double timeInsertion = time(arrayCopy1, SortTypes.INSERTION);
        printExperiment(SortTypes.INSERTION, arrayCopy1.length, timeInsertion);

        double timeShell = time(arrayCopy2, SortTypes.SHELL);
        printExperiment(SortTypes.SHELL, arrayCopy2.length, timeShell);
    }

    private static Comparable[] generateOrderedArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = i;
        }

        return array;
    }

    private static Comparable[] generateReverseOrderedArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = arrayLength - i;
        }

        return array;
    }

    private static Comparable[] generateEqualKeysArray(int arrayLength) {

        Integer equalKey = 100;

        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = equalKey;
        }

        return array;
    }

    private static Comparable[] generateTwoValuesArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            if (i % 2 == 0) {
                array[i] = 0;
            } else {
                array[i] = 1;
            }
        }

        return array;
    }

    private static Comparable[] generateSize1Array() {
        return new Comparable[] {0};
    }

    private static Comparable[] generateEmptyArray() {
        return new Comparable[] {};
    }

    public static double time(Comparable[] array, SortTypes sortType) {
        Stopwatch timer = new Stopwatch();

        switch (sortType) {
            case SELECTION: SelectionSort.selectionSort(array);
                break;
            case INSERTION: InsertionSort.insertionSort(array);
                break;
            case SHELL: ShellSort.shellSort(array);
                break;
        }

        return timer.elapsedTime();
    }

    private static void printExperiment(SortTypes sortType, int arrayLength, double time) {
        StdOut.printf("%15s %17d %8.2f\n", sortType, arrayLength, time);
    }

}
