package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;

/**
 * Created by Rene Argento on 05/02/17.
 */
public class Exercise36_NonuniformData {

    public static void main(String[] args) {

        int arrayLength = Integer.parseInt(args[0]);
        int experiments = Integer.parseInt(args[1]);

        runAllExperiments(arrayLength, experiments);
    }

    private static void runAllExperiments(int initialArrayLength, int experiments) {

        int arrayLength = initialArrayLength;

        StdOut.printf("%15s %17s %8s\n", "Sort Type", "Array Length", "Time");
        StdOut.println("Half 0s and half 1s");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] halfZeroHalfOneValuesArray = generateHalfZeroHalfOneValuesArray(arrayLength);
            runExperiments(halfZeroHalfOneValuesArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("Half 0s, half remainder 1s, half remainder 2s, ...");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] halfDecrementedValuesArray = generateHalfIncrementingValuesArray(arrayLength);
            runExperiments(halfDecrementedValuesArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("Half 0s and half random integers");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] halfZeroHalfRandomValuesArray = generateHalfZeroHalfRandomValuesArray(arrayLength);
            runExperiments(halfZeroHalfRandomValuesArray);
            arrayLength *= 2;
        }
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

    public static Comparable[] generateHalfZeroHalfOneValuesArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = i % 2 == 0? 0 : 1;
        }

        return array;
    }

    public static Comparable[] generateHalfIncrementingValuesArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        generateHalfIncrementingValuesArray(array, 0, 0);

        return array;
    }

    private static void generateHalfIncrementingValuesArray(Comparable[] array, int startIndex, int value) {

        if (startIndex == array.length) {
            return;
        }

        int endIndex = startIndex + (array.length - startIndex) / 2;

        for(int i = startIndex; i <= endIndex; i++) {
            array[i] = value;
        }

        generateHalfIncrementingValuesArray(array, endIndex+1, value + 1);
    }

    public static Comparable[] generateHalfZeroHalfRandomValuesArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength / 2; i++) {
            array[i] = 0;
        }

        for(int i = arrayLength / 2; i < arrayLength; i++) {
            array[i] = StdRandom.uniform(Integer.MAX_VALUE);
        }

        return array;
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
