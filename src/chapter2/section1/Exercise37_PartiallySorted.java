package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rene Argento on 06/02/17.
 */
public class Exercise37_PartiallySorted {

    public static void main(String[] args) {

        int arrayLength = Integer.parseInt(args[0]);
        int experiments = Integer.parseInt(args[1]);

        runAllExperiments(arrayLength, experiments);
    }

    private static void runAllExperiments(int initialArrayLength, int experiments) {

        int arrayLength = initialArrayLength;

        StdOut.printf("%15s %17s %8s\n", "Sort Type", "Array Length", "Time");
        StdOut.println("95% sorted and last percent random");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] ninetyFivePercentSortedExceptEndingArray = generate95PercentSortedExceptEndingArray(arrayLength);
            runExperiments(ninetyFivePercentSortedExceptEndingArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("All entries within 10 positions of their final place");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] elementsWithin10PositionsFromFinalPositionArray = generateElementsWithin10PositionsFromFinalPositionArray(arrayLength);
            runExperiments(elementsWithin10PositionsFromFinalPositionArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("Sorted except for 5% of entries randomly dispersed");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] ninetyFivePercentSortedArray = generate95PercentSortedArray(arrayLength);
            runExperiments(ninetyFivePercentSortedArray);
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

    private static Comparable[] generate95PercentSortedExceptEndingArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        int lastFivePercentStartIndex = (int) (arrayLength * 0.95);

        for(int i = 0; i <= lastFivePercentStartIndex; i++) {
            array[i] = i;
        }

        for(int i =lastFivePercentStartIndex; i < arrayLength; i++) {
            array[i] = StdRandom.uniform(Integer.MAX_VALUE);
        }

        return array;
    }

    private static Comparable[] generateElementsWithin10PositionsFromFinalPositionArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        //Used to guarantee that all values are unique and that they will be within 10 positions of their final place in the array
        Set<Integer> generatedValues = new HashSet<>();

        for(int i = 0; i < arrayLength; i++) {
            int randomValue = StdRandom.uniform(i - 10, i + 10 + 1);

            while (generatedValues.contains(randomValue)) {
                randomValue = StdRandom.uniform(i - 10, i + 10 + 1);
            }

            generatedValues.add(randomValue);

            array[i] = randomValue;
        }

        return array;
    }

    private static Comparable[] generate95PercentSortedArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        int fivePercentLength = (int) (arrayLength * 0.05);

        for(int i = 0; i < arrayLength; i++) {
            array[i] = i;
        }

        //Shuffle 5% of values
        for(int i = 0; i <= fivePercentLength / 2; i++) {
            int randomIndex1 = StdRandom.uniform(0, arrayLength);
            int randomIndex2 = StdRandom.uniform(0, arrayLength);

            Comparable temp = array[randomIndex1];
            array[randomIndex1] = array[randomIndex2];
            array[randomIndex2] = temp;
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
